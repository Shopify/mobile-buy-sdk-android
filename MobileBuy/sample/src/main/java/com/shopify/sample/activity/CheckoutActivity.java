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

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import com.shopify.buy.dataprovider.Callback;
import com.shopify.buy.dataprovider.RetrofitError;
import com.shopify.buy.model.Checkout;
import com.shopify.buy.model.CreditCard;
import com.shopify.sample.R;
import com.shopify.sample.activity.base.SampleActivity;


/**
 * The final activity in the app flow. Allows the user to choose between:
 * 1. A native checkout where the payment info is hardcoded and the chekcout is completed within the app; or
 * 2. A web checkout where the user enters their payment info and completes the checkout in a web browser
 */
public class CheckoutActivity extends SampleActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle(R.string.checkout);
        setContentView(R.layout.checkout_activity);

        final boolean didCreateCheckout = !TextUtils.isEmpty(getSampleApplication().getCheckout().getToken());

        Button nativeCheckoutButton = (Button) findViewById(R.id.native_checkout_button);
        if (didCreateCheckout) {
            nativeCheckoutButton.setVisibility(View.VISIBLE);
            nativeCheckoutButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onNativeCheckoutButtonClicked();
                }
            });
        } else {
            nativeCheckoutButton.setVisibility(View.GONE);
        }

        Button webCheckoutButton = (Button) findViewById(R.id.web_checkout_button);
        if (didCreateCheckout) {
            webCheckoutButton.setVisibility(View.VISIBLE);
            webCheckoutButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onWebCheckoutButtonClicked();
                }
            });
        } else {
            webCheckoutButton.setVisibility(View.GONE);
        }

        Button cartPermalinkButton = (Button) findViewById(R.id.cart_permalink_button);
        cartPermalinkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCartPermalinkClicked();
            }
        });

        updateOrderSummary();
    }
    
    /**
     * For our sample native checkout, we use a hardcoded credit card.
     */
    private void onNativeCheckoutButtonClicked() {
        // Create the card to send to Shopify.  This is hardcoded here for simplicity, but the user should be prompted for their credit card information.
        final CreditCard creditCard = new CreditCard();
        creditCard.setFirstName("Dinosaur");
        creditCard.setLastName("Banana");
        creditCard.setMonth("2");
        creditCard.setYear("20");
        creditCard.setVerificationValue("123");
        creditCard.setNumber("4242424242424242");

        showLoadingDialog(R.string.completing_checkout);
        getSampleApplication().storeCreditCard(creditCard, new Callback<Checkout>() {
            @Override
            public void success(Checkout checkout) {
                onCreditCardStored();
            }

            @Override
            public void failure(RetrofitError error) {
                onError(error);
            }
        });
    }

    /**
     * When the credit card has successfully been added, complete the checkout and begin polling.
     */
    private void onCreditCardStored() {
        getSampleApplication().completeCheckout(new Callback<Checkout>() {
            @Override
            public void success(Checkout checkout) {
                dismissLoadingDialog();
                pollCheckoutCompletionStatus(checkout);
            }

            @Override
            public void failure(RetrofitError error) {
                onError(error);
            }
        });
    }

    /**
     * Launch the device browser so the user can complete the checkout.
     */
    private void onWebCheckoutButtonClicked() {
        launchBrowser(getSampleApplication().getCheckout().getWebUrl());
    }

    /**
     * Launch the device browser using the cart permalink method
     */
    private void onCartPermalinkClicked() {
        launchBrowser(getSampleApplication().getCartPermalink());
    }

    private void launchBrowser(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.setData(Uri.parse(url));

        try {
            intent.setPackage("com.android.chrome");
            startActivity(intent);

        } catch (Exception launchChromeException) {
            try {
                // Chrome could not be opened, attempt to us other launcher
                intent.setPackage(null);
                startActivity(intent);

            } catch (Exception launchOtherException) {
                onError(getString(R.string.checkout_error));
            }
        }
    }

}
