/*
 *   The MIT License (MIT)
 *
 *   Copyright (c) 2015 Shopify Inc.
 *
 *   Permission is hereby granted, free of charge, to any person obtaining a copy
 *   of this software and associated documentation files (the "Software"), to deal
 *   in the Software without restriction, including without limitation the rights
 *   to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *   copies of the Software, and to permit persons to whom the Software is
 *   furnished to do so, subject to the following conditions:
 *
 *   The above copyright notice and this permission notice shall be included in
 *   all copies or substantial portions of the Software.
 *
 *   THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *   IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *   FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *   AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *   LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *   OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 *   THE SOFTWARE.
 */
package com.shopify.buy.dataprovider;

import android.text.TextUtils;

import com.shopify.buy.BuildConfig;
import com.shopify.buy.model.CustomerToken;
import com.shopify.buy.model.Product;

import okhttp3.Interceptor;
import rx.Scheduler;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Builds default implementation of {@link BuyClient}
 */
public final class BuyClientBuilder {

    public static final int MAX_PAGE_SIZE = 250;

    public static final int MIN_PAGE_SIZE = 1;

    public static final int DEFAULT_PAGE_SIZE = 25;

    private String shopDomain;

    private String apiKey;

    private String appId;

    private String applicationName;

    private String completeCheckoutWebReturnUrl;

    private String completeCheckoutWebReturnLabel;

    private CustomerToken customerToken;

    private Scheduler callbackScheduler = AndroidSchedulers.mainThread();

    private Interceptor[] interceptors;

    private int productPageSize = DEFAULT_PAGE_SIZE;

    /**
     * Sets store domain url (usually {store name}.myshopify.com
     */
    public BuyClientBuilder shopDomain(final String shopDomain) {
        this.shopDomain = shopDomain;
        return this;
    }

    /**
     * Sets Shopify store api key
     */
    public BuyClientBuilder apiKey(final String apiKey) {
        this.apiKey = apiKey;
        return this;
    }

    /**
     * Sets Shopify store application id
     */
    public BuyClientBuilder appId(final String appId) {
        this.appId = appId;
        return this;
    }

    /**
     * Sets Shopify store application name
     */
    public BuyClientBuilder applicationName(final String applicationName) {
        this.applicationName = applicationName;
        return this;
    }

    /**
     * Sets the web url to be invoked by the button on the completion page of the web checkout,
     * defined as a custom scheme in the Android Manifest file
     */
    public BuyClientBuilder completeCheckoutWebReturnUrl(final String completeCheckoutWebReturnUrl) {
        this.completeCheckoutWebReturnUrl = completeCheckoutWebReturnUrl;
        return this;
    }

    /**
     * Sets the text to be displayed on the button on the completion page of the web checkout
     */
    public BuyClientBuilder completeCheckoutWebReturnLabel(final String completeCheckoutWebReturnLabel) {
        this.completeCheckoutWebReturnLabel = completeCheckoutWebReturnLabel;
        return this;
    }

    /**
     * Sets the customer token
     */
    public BuyClientBuilder customerToken(final CustomerToken customerToken) {
        this.customerToken = customerToken;
        return this;
    }

    /**
     * Sets the Rx scheduler that will be used for all API callbacks, by default callback will be notified
     * in main thread.
     */
    public BuyClientBuilder callbackScheduler(final Scheduler callbackScheduler) {
        this.callbackScheduler = callbackScheduler;
        return this;
    }

    /**
     * Sets custom OkHttp interceptors
     */
    public BuyClientBuilder interceptors(final Interceptor... interceptors) {
        this.interceptors = interceptors;
        return this;
    }

    /**
     * Sets the page size used for paged product API queries. The number of {@link Product} to include in a page.
     * The maximum page size is {@link #MAX_PAGE_SIZE} and the minimum page size is {@link #MIN_PAGE_SIZE}.
     * If the page size is less than {@code MIN_PAGE_SIZE}, it will be set to {@code MIN_PAGE_SIZE}.
     * If the page size is greater than MAX_PAGE_SIZE it will be set to {@code MAX_PAGE_SIZE}.
     * The default value is {@link #DEFAULT_PAGE_SIZE}
     */
    public BuyClientBuilder productPageSize(final int productPageSize) {
        this.productPageSize = Math.max(Math.min(productPageSize, MAX_PAGE_SIZE), MIN_PAGE_SIZE);
        return this;
    }

    /**
     * Builds default implementation of {@link BuyClient}
     */
    public BuyClient build() {
        if (BuildConfig.DEBUG) {
            if (TextUtils.isEmpty(shopDomain) || shopDomain.contains(":") || shopDomain.contains("/")) {
                throw new IllegalArgumentException("shopDomain is not set or invalid. shopDomain must be a valid URL and cannot start with 'http://'");
            }
        } else {
            if (TextUtils.isEmpty(shopDomain) || shopDomain.contains(":") || shopDomain.contains("/") || !shopDomain.contains(".myshopify.com")) {
                throw new IllegalArgumentException("shopDomain is not set or invalid. shopDomain must be of the form 'shopname.myshopify.com' and cannot start with 'http://'");
            }
        }

        if (TextUtils.isEmpty(this.apiKey)) {
            throw new IllegalArgumentException("apiKey is not set or invalid. apiKey must be provided, and cannot be empty");
        }

        if (TextUtils.isEmpty(appId)) {
            throw new IllegalArgumentException("appId is not set or invalid. appId must be provided, and cannot be empty");
        }

        if (TextUtils.isEmpty(applicationName)) {
            throw new IllegalArgumentException("applicationName is not set or invalid. applicationName must be provided, and cannot be empty");
        }

        return new BuyClientImpl(
                this.apiKey,
                this.appId,
                this.applicationName,
                this.shopDomain,
                this.completeCheckoutWebReturnUrl,
                this.completeCheckoutWebReturnLabel,
                this.customerToken,
                this.callbackScheduler,
                this.productPageSize,
                this.interceptors
        );
    }
}
