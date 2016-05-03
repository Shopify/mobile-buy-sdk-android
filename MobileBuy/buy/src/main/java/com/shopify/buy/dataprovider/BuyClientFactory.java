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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.shopify.buy.BuildConfig;
import com.shopify.buy.model.Checkout;
import com.shopify.buy.model.Checkout.CheckoutDeserializer;
import com.shopify.buy.model.Checkout.CheckoutSerializer;
import com.shopify.buy.model.Customer;
import com.shopify.buy.model.Customer.CustomerDeserializer;
import com.shopify.buy.model.CustomerToken;
import com.shopify.buy.model.Product;
import com.shopify.buy.model.Product.ProductDeserializer;
import com.shopify.buy.utils.DateUtility;
import com.shopify.buy.utils.DateUtility.DateDeserializer;

import java.util.Date;

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
     * @param applicationName the name to attribute orders to. The value for {@code applicationName} should be the application package name, as used to publish your application on the Play Store.  This is usually the value returned by {@link Activity#getPackageName()}, or BuildConfig.APPLICATION_ID if you are using gradle.
     * @param customerToken   the token for a currently logged in {@link Customer}
     * @return a {@link BuyClient}
     */
    public static BuyClient getBuyClient(final String shopDomain, final String apiKey, final String channelId, final String applicationName, CustomerToken customerToken) throws IllegalArgumentException {
        if (BuildConfig.DEBUG) {
            if (TextUtils.isEmpty(shopDomain) || shopDomain.contains(":") || shopDomain.contains("/")) {
                throw new IllegalArgumentException("shopDomain must be a valid URL and cannot start with 'http://'");
            }
        } else {
            if (TextUtils.isEmpty(shopDomain) || shopDomain.contains(":") || shopDomain.contains("/") || !shopDomain.contains(".myshopify.com")) {
                throw new IllegalArgumentException("shopDomain must be of the form 'shopname.myshopify.com' and cannot start with 'http://'");
            }
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

        return new BuyClient(apiKey, channelId, applicationName, shopDomain, customerToken);
    }

    /**
     * Returns a BuyClient initialized with the given shop domain and API key.
     * After you have <a href="https://docs.shopify.com/api/mobile-buy-sdk/adding-mobile-app-sales-channel">added the mobile sales channel to your store</a>,
     * you can find the API Key and Channel ID in your store admin page. Click on Mobile App and then click on Integration.
     *
     * @param shopDomain      the domain of the shop to checkout with, in the format 'shopname.myshopify.com'
     * @param apiKey          a valid Shopify API key
     * @param channelId       a valid Shopify Channel ID
     * @param applicationName the name to attribute orders to. The value for {@code applicationName} should be the application package name, as used to publish your application on the Play Store.  This is usually the value returned by {@link Activity#getPackageName()}, or BuildConfig.APPLICATION_ID if you are using gradle.
     * @return a {@link BuyClient}
     */
    public static BuyClient getBuyClient(final String shopDomain, final String apiKey, final String channelId, final String applicationName) throws IllegalArgumentException {
        return getBuyClient(shopDomain, apiKey, channelId, applicationName, null);
    }

    public static Gson createDefaultGson() {
        return createDefaultGson(null);
    }

    public static Gson createDefaultGson(Class forClass) {

        GsonBuilder builder = new GsonBuilder()
                .setDateFormat(DateUtility.DEFAULT_DATE_PATTERN)
                .registerTypeAdapter(Date.class, new DateDeserializer());

        if (!Product.class.equals(forClass)) {
            builder.registerTypeAdapter(Product.class, new ProductDeserializer());
        }

        if (!Customer.class.equals(forClass)) {
            builder.registerTypeAdapter(Customer.class, new CustomerDeserializer());
        }

        if (!Checkout.class.equals(forClass)) {
            builder.registerTypeAdapter(Checkout.class, new CheckoutSerializer());
            builder.registerTypeAdapter(Checkout.class, new CheckoutDeserializer());
        }

        return builder.create();
    }

}