/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 Shopify Inc.
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
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wallet.MaskedWallet;
import com.google.android.gms.wallet.MaskedWalletRequest;
import com.google.android.gms.wallet.Wallet;
import com.google.android.gms.wallet.WalletConstants;
import com.google.android.gms.wallet.fragment.SupportWalletFragment;
import com.google.android.gms.wallet.fragment.WalletFragmentInitParams;
import com.google.android.gms.wallet.fragment.WalletFragmentMode;
import com.google.android.gms.wallet.fragment.WalletFragmentOptions;
import com.google.android.gms.wallet.fragment.WalletFragmentStyle;
import com.shopify.buy.dataprovider.BuyClient;
import com.shopify.buy.dataprovider.BuyClientError;
import com.shopify.buy.dataprovider.Callback;
import com.shopify.buy.model.Checkout;
import com.shopify.buy.utils.AndroidPayHelper;
import com.shopify.sample.BuildConfig;
import com.shopify.sample.R;
import com.shopify.sample.activity.base.SampleActivity;
import com.shopify.sample.application.SampleApplication;

/**
 * After a shipping rate is selected, this activity allows the user to add discount codes or gift card codes to the order.
 * It also shows a summary of the order, including the line item price, any discounts or gift cards used, the shipping charge, the taxes, and the total price.
 * It gives the user the option of continuing to the standard checkout flow, or using Android Pay to complete their purchase.
 */
public class DiscountActivity extends SampleActivity implements GoogleApiClient.OnConnectionFailedListener {

    private static final String LOG_TAG = CheckoutActivity.class.getSimpleName();

    private GoogleApiClient googleApiClient;

    BuyClient buyClient;

    private interface TextEntryDialogListener {
        void onTextSubmitted(String text);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle(R.string.apply_discounts);
        setContentView(R.layout.discount_activity);

        Button discountButton = (Button) findViewById(R.id.discount_button);
        discountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDiscountButtonClicked();
            }
        });

        Button giftCardButton = (Button) findViewById(R.id.gift_card_button);
        giftCardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onGiftCardButtonClicked();
            }
        });

        Button checkoutButton = (Button) findViewById(R.id.checkout_button);
        checkoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCheckoutButtonClicked();
            }
        });

        buyClient = getSampleApplication().getBuyClient();

        updateOrderSummary();

        createGoogleAPIClient();

        createAndAddAndroidPayButton();
    }

    /**
     * Check the pre-requisites for enabling Android Pay, and create and add the Masked Wallet fragment if applicable.
     *
     * To show Android Pay, the following conditions must be met:
     * 1) Google Play Services are available
     * 2) Android Pay App is installed, and has valid credit cards for in app purchase
     * 3) Android Pay is enable on the BuyClient
     */
    private void createAndAddAndroidPayButton() {

        // Check if device is configured to support Android Pay
        AndroidPayHelper.androidPayIsAvailable(this, googleApiClient, new AndroidPayHelper.AndroidPayReadyCallback() {
            @Override
            public void onResult(boolean androidPayAvailable) {

                if (androidPayAvailable) {
                    createAndAddWalletFragment();
                } else {
                    Log.e(LOG_TAG, "Android Pay was not available");
                }
            }
        });
    }

    /**
     * Create a {@link SupportWalletFragment} to use as a Buy With Android button
     * This fragment should be configured with as much information as possible to allow
     * accurate fraud detection by Google
     */
    private void createAndAddWalletFragment() {
        Checkout checkout = getSampleApplication().getCheckout();

        // Create the wallet fragment style.
        WalletFragmentStyle walletFragmentStyle = new WalletFragmentStyle()
                .setBuyButtonText(WalletFragmentStyle.BuyButtonText.BUY_WITH)
                .setStyleResourceId(R.style.SampleWalletButton);

        WalletFragmentOptions walletFragmentOptions = WalletFragmentOptions.newBuilder()
                .setEnvironment(SampleApplication.WALLET_ENVIRONMENT)
                .setFragmentStyle(walletFragmentStyle)
                .setTheme(WalletConstants.THEME_LIGHT)
                .setMode(WalletFragmentMode.BUY_BUTTON)
                .build();
        SupportWalletFragment walletFragment = SupportWalletFragment.newInstance(walletFragmentOptions);

        // The Merchant Name will be shown on the Android Pay dialogs, and should be representative of your shop
        String merchantName = getString(R.string.merchant_name);

        // Create the Masked Wallet request
        MaskedWalletRequest maskedWalletRequest = AndroidPayHelper.createMaskedWalletRequest(merchantName, checkout, BuildConfig.ANDROID_PAY_PUBLIC_KEY, true);

        WalletFragmentInitParams.Builder startParamsBuilder = WalletFragmentInitParams.newBuilder()
                .setMaskedWalletRequest(maskedWalletRequest)
                .setMaskedWalletRequestCode(AndroidPayHelper.REQUEST_CODE_MASKED_WALLET);

        walletFragment.initialize(startParamsBuilder.build());

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.google_wallet_fragment_container, walletFragment)
                .commit();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // retrieve the error code, if available
        int errorCode = -1;
        if (data != null) {
            errorCode = data.getIntExtra(WalletConstants.EXTRA_ERROR_CODE, -1);
        }
        switch (requestCode) {
            case AndroidPayHelper.REQUEST_CODE_MASKED_WALLET:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        if (data != null) {
                            MaskedWallet maskedWallet = data.getParcelableExtra(WalletConstants.EXTRA_MASKED_WALLET);

                            getSampleApplication().setMaskedWallet(maskedWallet);
                            onAndroidPayButtonClicked(maskedWallet);
                        }
                        break;
                    case Activity.RESULT_CANCELED:
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

    /**
     * Create a Google Api client for use in Android Pay
     */
    private void createGoogleAPIClient() {
        googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Wallet.API, new Wallet.WalletOptions.Builder().build())
                .enableAutoManage(this, this)
                .build();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.e(LOG_TAG, "onConnectionFailed:" + connectionResult.getErrorMessage());
        Toast.makeText(this, "Google Play Services error", Toast.LENGTH_SHORT).show();
    }
    private void onDiscountButtonClicked() {
        showTextEntryDialog(R.string.discount_code, R.string.enter_discount_code, R.string.apply_discount, new TextEntryDialogListener() {
            @Override
            public void onTextSubmitted(String text) {
                setDiscountCode(text);
            }
        });
    }

    private void onGiftCardButtonClicked() {
        showTextEntryDialog(R.string.gift_card_code, R.string.enter_gift_card_code, R.string.apply_gift_card, new TextEntryDialogListener() {
            @Override
            public void onTextSubmitted(String text) {
                addGiftCardCode(text);
            }
        });
    }

    /**
     * Displays a simple dialog with an EditText field and a single button, allowing the user to enter either a gift card code or a discount code.
     *
     * @param hint
     * @param title
     * @param button
     * @param listener
     */
    private void showTextEntryDialog(final int hint, final int title, final int button, final TextEntryDialogListener listener) {
        final View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_code_entry, null);
        final EditText editText = (EditText) dialogView.findViewById(R.id.edit_text);
        editText.setHint(hint);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setPositiveButton(button, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                listener.onTextSubmitted(editText.getText().toString());
            }
        });
        builder.create().show();
    }

    /**
     * Add the discount code to the checkout and update the order summary when the request completes.
     *
     * @param discountCode
     */
    private void setDiscountCode(final String discountCode) {
        showLoadingDialog(R.string.syncing_data);
        getSampleApplication().setDiscountCode(discountCode, new Callback<Checkout>() {
            @Override
            public void success(Checkout checkout) {
                dismissLoadingDialog();
                updateOrderSummary();
            }

            @Override
            public void failure(BuyClientError error) {
                dismissLoadingDialog();
                Toast.makeText(DiscountActivity.this, getString(R.string.discount_error, discountCode), Toast.LENGTH_LONG).show();
            }
        });
    }


    /**
     * Add the gift card code to the checkout and update the order summary when the request completes.
     *
     * @param giftCardCode
     */
    private void addGiftCardCode(final String giftCardCode) {
        showLoadingDialog(R.string.syncing_data);
        getSampleApplication().addGiftCard(giftCardCode, new Callback<Checkout>() {
            @Override
            public void success(Checkout checkout) {
                dismissLoadingDialog();
                updateOrderSummary();
            }

            @Override
            public void failure(BuyClientError error) {
                dismissLoadingDialog();
                Toast.makeText(DiscountActivity.this, getString(R.string.gift_card_error, giftCardCode), Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * Called when the AndroidPay button is clicked and the Masked Wallet has been returned.
     */
    private void onAndroidPayButtonClicked(MaskedWallet maskedWallet) {
        showLoadingDialog(R.string.syncing_data);

        getSampleApplication().updateCheckout(getSampleApplication().getCheckout(), maskedWallet, new Callback<Checkout>() {
            @Override
            public void success(Checkout checkout) {
                dismissLoadingDialog();

                if (checkout.isRequiresShipping()) {
                    Intent intent = new Intent(DiscountActivity.this, ShippingRateListActivity.class);
                    intent.putExtra(SampleApplication.ANDROID_PAY_FLOW, true);
                    startActivity(intent);
                } else {
                    startActivity(new Intent(DiscountActivity.this, AndroidPayCheckoutActivity.class));
                }
            }

            @Override
            public void failure(BuyClientError error) {
                Log.e(LOG_TAG, "Error updating checkout: " + error.getRetrofitErrorBody());
                Toast.makeText(DiscountActivity.this, "Could not start the checkout, please try again later", Toast.LENGTH_SHORT).show();
                dismissLoadingDialog();
            }
        });

    }

    /**
     * When the checkout button is clicked, proceed to the checkout activity (final activity in the app flow).
     */
    private void onCheckoutButtonClicked() {
        if (getSampleApplication().getCheckout().isRequiresShipping()) {
            startActivity(new Intent(this, ShippingRateListActivity.class));
        } else {
            startActivity(new Intent(this, CheckoutActivity.class));
        }
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

    private void launchProductListActivity() {
        startActivity(new Intent(this, ProductListActivity.class));
    }
}
