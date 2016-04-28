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

package com.shopify.sample.activity.base;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.shopify.buy.dataprovider.BuyClient;
import com.shopify.buy.model.Checkout;
import com.shopify.buy.model.Discount;
import com.shopify.buy.model.GiftCard;
import com.shopify.sample.R;
import com.shopify.sample.application.SampleApplication;

import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Base class for all activities in the app. Manages the ProgressDialog that is displayed while network activity is occurring.
 */
public class SampleActivity extends Activity {

    private static final String LOG_TAG = SampleActivity.class.getSimpleName();

    // The amount of time in milliseconds to delay between network calls when you are polling for Shipping Rates and Checkout Completion
    protected static final long POLL_DELAY = 500;

    protected Handler pollingHandler;

    private ProgressDialog progressDialog;
    private boolean webCheckoutInProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        pollingHandler = new Handler();

        initializeProgressDialog();
    }

    @Override
    protected void onResume() {
        super.onResume();

        // If we are being launched by a url scheme, check the scheme and retrieve the checkout token if provided
        Intent intent = getIntent();
        Uri uri = intent.getData();

        String scheme = getString(R.string.web_return_to_scheme);

        if (uri != null && TextUtils.equals(uri.getScheme(), scheme)) {
            webCheckoutInProgress = false;

            // If the app was launched using the scheme, we know we just successfully completed an order
            onCheckoutComplete();

        } else {
            // If a Web checkout was previously launched, we should check its status
            if (webCheckoutInProgress && getSampleApplication().getCheckout() != null) {
                pollCheckoutCompletionStatus(getSampleApplication().getCheckout());
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    protected SampleApplication getSampleApplication() {
        return (SampleApplication) getApplication();
    }

    /**
     * Initializes a simple progress dialog that gets presented while the app is communicating with the server.
     */
    private void initializeProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }

        progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(true);
        progressDialog.setTitle(getString(R.string.please_wait));
        progressDialog.setCancelable(true);
        progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                SampleActivity.this.finish();
            }
        });
    }

    /**
     * Present the progress dialog.
     *
     * @param messageId The identifier (R.string value) of the string to display in the dialog.
     */
    protected void showLoadingDialog(final int messageId) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressDialog.setMessage(getString(messageId));
                progressDialog.show();
            }
        });
    }

    protected void dismissLoadingDialog() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressDialog.dismiss();
            }
        });
    }

    protected void onError(RetrofitError error) {
        onError(BuyClient.getErrorBody(error));
    }

    /**
     * When we encounter an error with one of our network calls, we abort and return to the previous activity.
     * In a production app, you'll want to handle these types of errors more gracefully.
     *
     * @param errorMessage
     */
    protected void onError(String errorMessage) {
        progressDialog.dismiss();
        Log.e(LOG_TAG, "Error: " + errorMessage);
        Toast.makeText(this, R.string.error, Toast.LENGTH_LONG).show();
        finish();
    }

    /**
     * Use the latest Checkout objects details to populate the text views in the order summary section.
     */
    protected void updateOrderSummary() {
        final Checkout checkout = getSampleApplication().getCheckout();

        if (checkout == null) {
            return;
        }

        ((TextView) findViewById(R.id.line_item_price_value)).setText('$' + checkout.getLineItems().get(0).getPrice());

        double totalDiscount = 0;
        Discount discount = checkout.getDiscount();
        if (discount != null && !TextUtils.isEmpty(discount.getAmount())) {
            totalDiscount += Double.parseDouble(discount.getAmount());
        }
        ((TextView) findViewById(R.id.discount_value)).setText("-$" + Double.toString(totalDiscount));

        double totalGiftCards = 0;
        List<GiftCard> giftCards = checkout.getGiftCards();
        if (giftCards != null) {
            for (GiftCard giftCard : giftCards) {
                if (!TextUtils.isEmpty(giftCard.getAmountUsed())) {
                    totalGiftCards += Double.parseDouble(giftCard.getAmountUsed());
                }
            }
        }
        ((TextView) findViewById(R.id.gift_card_value)).setText("-$" + Double.toString(totalGiftCards));
        ((TextView) findViewById(R.id.taxes_value)).setText('$' + checkout.getTotalTax());
        ((TextView) findViewById(R.id.total_value)).setText('$' + checkout.getPaymentDue());

        if (checkout.getShippingRate() != null) {
            ((TextView) findViewById(R.id.shipping_value)).setText('$' + checkout.getShippingRate().getPrice());
        } else {
            ((TextView) findViewById(R.id.shipping_value)).setText("N/A");
        }
    }

    /**
     * Polls until the web checkout has completed.
     *
     * @param checkout the checkout to check the status on
     */
    protected void pollCheckoutCompletionStatus(final Checkout checkout) {
        showLoadingDialog(R.string.getting_checkout_status);

        getSampleApplication().getCheckoutCompletionStatus(new Callback<Boolean>() {
            @Override

            public void success(Boolean complete, Response response) {
                if (complete) {
                    dismissLoadingDialog();
                    onCheckoutComplete();
                } else {
                    pollingHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            pollCheckoutCompletionStatus(checkout);
                        }
                    }, POLL_DELAY);
                }
            }

            @Override
            public void failure(RetrofitError error) {
                onError(error);
            }
        });
    }

    /**
     * When our polling determines that the checkout is completely processed, show a toast.
     */
    private void onCheckoutComplete() {
        dismissLoadingDialog();
        webCheckoutInProgress = false;

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(SampleActivity.this, R.string.checkout_complete, Toast.LENGTH_LONG).show();
            }
        });
    }

}
