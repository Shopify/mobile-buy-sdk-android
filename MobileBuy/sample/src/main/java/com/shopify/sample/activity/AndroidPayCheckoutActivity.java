/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2016 Shopify Inc.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.shopify.sample.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wallet.FullWallet;
import com.google.android.gms.wallet.FullWalletRequest;
import com.google.android.gms.wallet.MaskedWallet;
import com.google.android.gms.wallet.Wallet;
import com.google.android.gms.wallet.WalletConstants;
import com.google.android.gms.wallet.fragment.SupportWalletFragment;
import com.google.android.gms.wallet.fragment.WalletFragmentInitParams;
import com.google.android.gms.wallet.fragment.WalletFragmentMode;
import com.google.android.gms.wallet.fragment.WalletFragmentOptions;
import com.google.android.gms.wallet.fragment.WalletFragmentStyle;
import com.shopify.buy.dataprovider.BuyClientError;
import com.shopify.buy.dataprovider.Callback;
import com.shopify.buy.model.Checkout;
import com.shopify.buy.utils.AndroidPayHelper;
import com.shopify.sample.R;
import com.shopify.sample.activity.base.SampleActivity;
import com.shopify.sample.application.SampleApplication;


public class AndroidPayCheckoutActivity extends SampleActivity implements GoogleApiClient.OnConnectionFailedListener {

    private static final String LOG_TAG = AndroidPayCheckoutActivity.class.getSimpleName();

    private MaskedWallet maskedWallet;

    private GoogleApiClient googleApiClient;

    private Button confirmationButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        maskedWallet = getSampleApplication().getMaskedWallet();

        setTitle(R.string.checkout);
        setContentView(R.layout.android_pay_checkout_activity);

        confirmationButton = (Button) findViewById(R.id.confirm_button);
        confirmationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLoadingDialog(R.string.completing_checkout);
                Checkout checkout = getSampleApplication().getCheckout();
                MaskedWallet maskedWallet = getSampleApplication().getMaskedWallet();

                FullWalletRequest fullWalletRequest = AndroidPayHelper.createFullWalletRequest(checkout, maskedWallet);
                Wallet.Payments.loadFullWallet(googleApiClient, fullWalletRequest, AndroidPayHelper.REQUEST_CODE_FULL_WALLET);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        updateOrderSummary();

        createGoogleAPIClient();

        createAndAddWalletFragment();
    }

    private void createAndAddWalletFragment() {

        WalletFragmentStyle walletFragmentStyle = new WalletFragmentStyle()
            .setMaskedWalletDetailsHeaderTextAppearance(R.style.SampleWalletFragmentDetailsHeaderTextAppearance)
            .setMaskedWalletDetailsTextAppearance(R.style.SampleWalletFragmentDetailsTextAppearance)
            .setMaskedWalletDetailsBackgroundColor(android.R.color.white);

        WalletFragmentOptions walletFragmentOptions = WalletFragmentOptions.newBuilder()
            .setEnvironment(SampleApplication.WALLET_ENVIRONMENT)
            .setFragmentStyle(walletFragmentStyle)
            .setTheme(WalletConstants.THEME_LIGHT)
            .setMode(WalletFragmentMode.SELECTION_DETAILS)
            .build();

        SupportWalletFragment walletFragment = SupportWalletFragment.newInstance(walletFragmentOptions);

        WalletFragmentInitParams.Builder startParamsBuilder = WalletFragmentInitParams.newBuilder()
            .setMaskedWallet(maskedWallet)
            .setMaskedWalletRequestCode(AndroidPayHelper.REQUEST_CODE_CHANGE_MASKED_WALLET);
        walletFragment.initialize(startParamsBuilder.build());

        // add Wallet fragment to the UI
        getSupportFragmentManager().beginTransaction()
            .replace(R.id.google_wallet_fragment_container, walletFragment)
            .commit();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        int errorCode = data.getIntExtra(WalletConstants.EXTRA_ERROR_CODE, 0);

        switch (requestCode) {
            case AndroidPayHelper.REQUEST_CODE_CHANGE_MASKED_WALLET:
                // This is the result of a change made on the shipping or billing address.
                // If the shipping address has changed, we should recalculate the shipping rates
                // For simplicity in this sample, we will always direct the user back to the Shipping Rate Activity
                if (resultCode == Activity.RESULT_OK && data.hasExtra(WalletConstants.EXTRA_MASKED_WALLET)) {
                    MaskedWallet maskedWallet = data.getParcelableExtra(WalletConstants.EXTRA_MASKED_WALLET);
                    updateCheckoutWithMaskedWallet(maskedWallet);
                }
                break;
            case AndroidPayHelper.REQUEST_CODE_FULL_WALLET:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        if (data.hasExtra(WalletConstants.EXTRA_FULL_WALLET)) {
                            FullWallet fullWallet = data.getParcelableExtra(WalletConstants.EXTRA_FULL_WALLET);
                            completeCheckout(fullWallet);
                        } else if (data.hasExtra(WalletConstants.EXTRA_MASKED_WALLET)) {
                            MaskedWallet maskedWallet = data.getParcelableExtra(WalletConstants.EXTRA_MASKED_WALLET);
                            if (!updateCheckoutWithMaskedWallet(maskedWallet)) {
                                confirmationButton.performClick();
                            }
                        }
                        break;
                    case Activity.RESULT_CANCELED:
                        // Nothing to do here
                        break;
                    default:
                        handleGoogleWalletError(errorCode);
                        break;
                }
                break;
            case WalletConstants.RESULT_ERROR:
                handleGoogleWalletError(errorCode);
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
                break;
        }
    }

    private void completeCheckout(FullWallet fullWallet) {
        // We have a Full Wallet containing a token, so we can now complete the checkout
        getSampleApplication().completeCheckout(fullWallet, new Callback<Checkout>() {
            @Override
            public void success(Checkout checkout) {
                dismissLoadingDialog();
            }

            @Override
            public void failure(BuyClientError error) {
                // The checkout failed.
                Log.e(LOG_TAG, "Could not complete the checkout:" + error.getRetrofitErrorBody());
                Toast.makeText(AndroidPayCheckoutActivity.this, "Could not complete the checkout, please try again later", Toast.LENGTH_SHORT).show();
                dismissLoadingDialog();
            }
        });
    }

    @Override
    protected void onCheckoutComplete() {
        startActivity(new Intent(this, OrderCompleteActivity.class));
    }

    /**
     * Create a Google Api client for use in Android Pay
     */
    private void createGoogleAPIClient() {
        googleApiClient = new GoogleApiClient.Builder(this)
            .addApi(Wallet.API, new Wallet.WalletOptions.Builder().setEnvironment(SampleApplication.WALLET_ENVIRONMENT).build())
            .enableAutoManage(this, this)
            .build();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.e(LOG_TAG, "onConnectionFailed:" + connectionResult.getErrorMessage());
        Toast.makeText(this, "Google Play Services error", Toast.LENGTH_SHORT).show();
    }

    private void launchShippingRateActivity() {
        Intent intent = new Intent(this, ShippingRateListActivity.class);
        intent.putExtra(SampleApplication.ANDROID_PAY_FLOW, true);
        startActivity(intent);
    }

    private void launchProductListActivity() {
        startActivity(new Intent(this, ProductListActivity.class));
    }

    private void handleGoogleWalletError(int errorCode) {
        switch (errorCode) {
            // Recoverable Wallet errors
            case WalletConstants.ERROR_CODE_SPENDING_LIMIT_EXCEEDED:
                Log.e(LOG_TAG, "Spending limit exceeded");
                Toast.makeText(this, "Spending Limit exceeded. Please adjust your cart and try again", Toast.LENGTH_SHORT).show();
                launchProductListActivity();
                break;

            // Unrecoverable Wallet errors
            case WalletConstants.ERROR_CODE_INVALID_PARAMETERS:
            case WalletConstants.ERROR_CODE_AUTHENTICATION_FAILURE:
            case WalletConstants.ERROR_CODE_BUYER_ACCOUNT_ERROR:
            case WalletConstants.ERROR_CODE_MERCHANT_ACCOUNT_ERROR:
            case WalletConstants.ERROR_CODE_SERVICE_UNAVAILABLE:
            case WalletConstants.ERROR_CODE_UNSUPPORTED_API_VERSION:
            case WalletConstants.ERROR_CODE_UNKNOWN:
            default:
                Log.e(LOG_TAG, "Unrecoverable wallet error:" + errorCode);
                Toast.makeText(this, "Could not complete the checkout, please try again later", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private boolean updateCheckoutWithMaskedWallet(MaskedWallet maskedWallet) {
        if (AndroidPayHelper.isCheckoutUpdateRequired(getSampleApplication().getCheckout(), maskedWallet)) {
            showLoadingDialog(R.string.syncing_data);
            getSampleApplication().updateCheckout(getSampleApplication().getCheckout(), maskedWallet, new Callback<Checkout>() {
                @Override
                public void success(Checkout checkout) {
                    dismissLoadingDialog();

                    // The shipping address has changed, we need to recalculate shipping rates
                    // For this sample we will send them back to our ShippingRateListActivity()
                    if (checkout.getShippingRate() == null) {
                        launchShippingRateActivity();
                    }
                }

                @Override
                public void failure(BuyClientError error) {
                    Log.e(LOG_TAG, "Error updating checkout: " + error.getRetrofitErrorBody());
                    Toast.makeText(AndroidPayCheckoutActivity.this, "Could not Sync data with Checkout API", Toast.LENGTH_SHORT).show();
                    dismissLoadingDialog();
                }
            });
            return true;
        } else {
            return false;
        }
    }
}

