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

package com.shopify.sample.application;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.widget.Toast;

import com.google.android.gms.wallet.FullWallet;
import com.google.android.gms.wallet.MaskedWallet;
import com.google.android.gms.wallet.WalletConstants;
import com.shopify.sample.BuildConfig;
import com.shopify.sample.R;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.logging.HttpLoggingInterceptor;

/**
 * Application class that maintains instances of BuyClient and Checkout for the lifetime of the app.
 */
public class SampleApplication extends Application {

    private static final String SHOP_PROPERTIES_INSTRUCTION =
        "\n\tAdd your shop credentials to a shop.properties file in the main app folder (e.g. 'app/shop.properties'). Include these keys:\n" +
            "\t\tSHOP_DOMAIN=<myshop>.myshopify.com\n" +
            "\t\tAPI_KEY=0123456789abcdefghijklmnopqrstuvw\n";

    private static SampleApplication instance;

    public static final String ANDROID_PAY_FLOW = "com.shopify.sample.androidpayflow";

    // Use ENVIRONMENT_TEST for testing
    public static final int WALLET_ENVIRONMENT = WalletConstants.ENVIRONMENT_TEST;

    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;

        initializeBuyClient();
    }

    private void initializeBuyClient() {
        String shopUrl = BuildConfig.SHOP_DOMAIN;
        if (TextUtils.isEmpty(shopUrl)) {
            throw new IllegalArgumentException(SHOP_PROPERTIES_INSTRUCTION + "You must add 'SHOP_DOMAIN' entry in app/shop.properties, in the form '<myshop>.myshopify.com'");
        }

        String shopifyApiKey = BuildConfig.API_KEY;
        if (TextUtils.isEmpty(shopifyApiKey)) {
            throw new IllegalArgumentException(SHOP_PROPERTIES_INSTRUCTION + "You must populate the 'API_KEY' entry in app/shop.properties");
        }

        String shopifyAppId = BuildConfig.APP_ID;
        if (TextUtils.isEmpty(shopifyAppId)) {
            throw new IllegalArgumentException(SHOP_PROPERTIES_INSTRUCTION + "You must populate the 'APP_ID' entry in app/shop.properties");
        }

        String applicationName = getPackageName();

        final HttpLoggingInterceptor logging = new HttpLoggingInterceptor().setLevel(BuildConfig.OKHTTP_LOG_LEVEL);
    }
}
