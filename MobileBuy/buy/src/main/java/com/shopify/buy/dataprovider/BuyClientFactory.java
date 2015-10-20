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

package com.shopify.buy.dataprovider;

import android.app.Activity;
import android.text.TextUtils;
import android.util.Base64;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.shopify.buy.BuildConfig;
import com.shopify.buy.model.Product;
import com.shopify.buy.model.Product.ProductDeserializer;
import com.shopify.buy.utils.DateUtility;
import com.shopify.buy.utils.DateUtility.DateDeserializer;
import com.squareup.okhttp.OkHttpClient;

import java.util.Date;

import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.client.OkClient;
import retrofit.converter.GsonConverter;

/**
 * Provides the factory to initialize and build a {@link BuyClient}.
 */
public class BuyClientFactory {

    /**
     * Returns a BuyClient initialized with the given shop domain and API key.
     * After you have <a href="https://docs.shopify.com/api/mobile-buy-sdk/adding-mobile-app-sales-channel">added the mobile sales channel to your store</a>,
     * you can find the API Key and Channel ID in your store admin page. Click on Mobile App and then click on Integration.
     *
     * @param shopDomain      the domain of the shop to checkout with, in the format 'shopname.myshopify.com'
     * @param apiKey          a valid Shopify API key
     * @param channelId       a valid Shopify Channel ID
     * @param applicationName the name to attribute orders to. The suggested value for {@code applicationName} is the value returned by {@link Activity#getPackageName()}.
     * @return a {@link BuyClient}
     */
    public static BuyClient getBuyClient(final String shopDomain, final String apiKey, final String channelId, final String applicationName) throws IllegalArgumentException {
        if (TextUtils.isEmpty(shopDomain) || shopDomain.contains(":") || shopDomain.contains("/") || !shopDomain.contains(".myshopify.com")) {
            throw new IllegalArgumentException("shopDomain must be of the form 'shopname.myshopify.com' and cannot start with 'http://'");
        }

        if (TextUtils.isEmpty(apiKey)) {
            throw new IllegalArgumentException("apiKey must be provided, and cannot be empty");
        }

        if (TextUtils.isEmpty(channelId)) {
            throw new IllegalArgumentException("channelId must be provided, and cannot be empty");
        }

        if (TextUtils.isEmpty(applicationName)) {
            throw new IllegalArgumentException("applicationName must be provided, and cannot be empty");
        }

        RequestInterceptor requestInterceptor = new RequestInterceptor() {
            @Override
            public void intercept(RequestFacade request) {
                request.addHeader("Authorization", "Basic " + Base64.encodeToString(apiKey.getBytes(), Base64.NO_WRAP));

                // Using the full package name for BuildConfig here as a work around for Javadoc.  The source paths need to be adjusted
                request.addHeader("User-Agent", "Mobile Buy SDK Android/" + com.shopify.buy.BuildConfig.VERSION_NAME);
            }
        };

        OkHttpClient httpClient = new OkHttpClient();

        RestAdapter adapter = new RestAdapter.Builder()
                .setEndpoint("https://" + shopDomain)
                .setConverter(new GsonConverter(createDefaultGson()))
                .setClient(new OkClient(httpClient))
                .setLogLevel(BuildConfig.RETROFIT_LOG_LEVEL)
                .setRequestInterceptor(requestInterceptor)
                .build();

        return new BuyClient(adapter.create(BuyRetrofitService.class), apiKey, channelId, applicationName, shopDomain, httpClient);
    }

    public static Gson createDefaultGson() {
        return new GsonBuilder().setDateFormat(DateUtility.DEFAULT_DATE_PATTERN)
                .registerTypeAdapter(Product.class, new ProductDeserializer())
                .registerTypeAdapter(Date.class, new DateDeserializer())
                .create();
    }

}
