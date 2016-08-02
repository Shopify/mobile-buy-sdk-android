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
import com.shopify.buy.model.Customer;
import com.shopify.buy.dataprovider.cache.AddressCacheHook;
import com.shopify.buy.dataprovider.cache.CustomerCacheHook;
import com.shopify.buy.dataprovider.cache.OrderCacheHook;
import com.shopify.buy.dataprovider.cache.ProductCacheHook;
import com.shopify.buy.dataprovider.cache.StoreCacheHook;
import com.shopify.buy.model.CustomerToken;
import com.shopify.buy.model.Product;

import java.util.concurrent.TimeUnit;

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

    public static final long DEFAULT_HTTP_CONNECTION_TIME_OUT_MS = TimeUnit.SECONDS.toMillis(30);

    public static final long DEFAULT_HTTP_READ_WRITE_TIME_OUT_MS = TimeUnit.SECONDS.toMillis(60);

    public static final long MIN_NETWORK_RETRY_DELAY = TimeUnit.MILLISECONDS.toMillis(500);

    private String shopDomain;

    private String apiKey;

    private String appId;

    private String applicationName;

    private CustomerToken customerToken;

    private Scheduler callbackScheduler = AndroidSchedulers.mainThread();

    private Interceptor[] interceptors;

    private int productPageSize = DEFAULT_PAGE_SIZE;

    private int networkRequestRetryMaxCount;

    private long networkRequestRetryDelayMs;

    private float networkRequestRetryBackoffMultiplier;

    private long httpConnectionTimeoutMs = DEFAULT_HTTP_CONNECTION_TIME_OUT_MS;

    private long httpReadWriteTimeoutMs = DEFAULT_HTTP_READ_WRITE_TIME_OUT_MS;

    private AddressCacheHook addressCacheHook;

    private CustomerCacheHook customerCacheHook;

    private OrderCacheHook orderCacheHook;

    private ProductCacheHook productCacheHook;

    private StoreCacheHook storeCacheHook;

    /**
     * Sets store domain url (usually {store name}.myshopify.com
     *
     * @param shopDomain The domain for the shop.
     * @return a {@link BuyClientBuilder}
     */
    public BuyClientBuilder shopDomain(final String shopDomain) {
        this.shopDomain = shopDomain;
        return this;
    }

    /**
     * Sets Shopify store api key
     *
     * @param apiKey The Api Key.
     * @return a {@link BuyClientBuilder}
     */
    public BuyClientBuilder apiKey(final String apiKey) {
        this.apiKey = apiKey;
        return this;
    }

    /**
     * Sets Shopify store application id
     *
     * @param appId The App Id.
     * @return a {@link BuyClientBuilder}
     */
    public BuyClientBuilder appId(final String appId) {
        this.appId = appId;
        return this;
    }

    /**
     * Sets Shopify store application name
     *
     * @param applicationName The application name.
     * @return a {@link BuyClientBuilder}
     */
    public BuyClientBuilder applicationName(final String applicationName) {
        this.applicationName = applicationName;
        return this;
    }

    /**
     * Sets the customer token
     *
     * @param customerToken The token associated with a {@link Customer}
     * @return a {@link BuyClientBuilder}
     */
    public BuyClientBuilder customerToken(final CustomerToken customerToken) {
        this.customerToken = customerToken;
        return this;
    }

    /**
     * Sets the Rx scheduler that will be used for all API callbacks, by default callback will be notified
     * in main thread.
     *
     * @param callbackScheduler The {@link Scheduler} to use for API callbacks.
     * @return a {@link BuyClientBuilder}
     */
    public BuyClientBuilder callbackScheduler(final Scheduler callbackScheduler) {
        this.callbackScheduler = callbackScheduler;
        return this;
    }

    /**
     * Sets custom OkHttp interceptors
     *
     * @param interceptors Interceptors to add to the OkHttp client.
     * @return a {@link BuyClientBuilder}
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
     *
     * @param productPageSize The number of products to return in a page.
     * @return a {@link BuyClientBuilder}
     */
    public BuyClientBuilder productPageSize(final int productPageSize) {
        this.productPageSize = Math.max(Math.min(productPageSize, MAX_PAGE_SIZE), MIN_PAGE_SIZE);
        return this;
    }

    /**
     * Sets the configuration for retry logic in case network request failed (socket timeout, unknown host, etc.).
     * The minimum of the delay between retries should be greater than {@link BuyClientBuilder#MIN_NETWORK_RETRY_DELAY}, otherwise it will be set to {@link BuyClientBuilder#MIN_NETWORK_RETRY_DELAY}
     *
     * @param networkRequestRetryMaxCount          max count of retry attempts
     * @param networkRequestRetryDelayMs           delay between retry attempts in milliseconds
     * @param networkRequestRetryBackoffMultiplier backoff multiplier for next request attempts, can be used for "exponential backoff"
     * @return a {@link BuyClientBuilder}
     */
    public BuyClientBuilder networkRequestRetryPolicy(final int networkRequestRetryMaxCount, final long networkRequestRetryDelayMs, final float networkRequestRetryBackoffMultiplier) {
        this.networkRequestRetryMaxCount = networkRequestRetryMaxCount;
        this.networkRequestRetryDelayMs = Math.max(networkRequestRetryDelayMs, MIN_NETWORK_RETRY_DELAY);
        this.networkRequestRetryBackoffMultiplier = networkRequestRetryBackoffMultiplier;
        return this;
    }

    /**
     * Sets cache hook for {@link AddressService} API requests
     *
     * @param addressCacheHook cache hook
     */
    public BuyClientBuilder addressCacheHook(AddressCacheHook addressCacheHook) {
        this.addressCacheHook = addressCacheHook;
        return this;
    }

    /**
     * Sets cache hook for {@link CustomerService} API requests
     *
     * @param addressCacheHook cache hook
     */
    public BuyClientBuilder customerCacheHook(CustomerCacheHook customerCacheHook) {
        this.customerCacheHook = customerCacheHook;
        return this;
    }

    /**
     * Sets cache hook for {@link OrderService} API requests
     *
     * @param addressCacheHook cache hook
     */
    public BuyClientBuilder orderCacheHook(OrderCacheHook orderCacheHook) {
        this.orderCacheHook = orderCacheHook;
        return this;
    }

    /**
     * Sets cache hook for {@link ProductService} API requests
     *
     * @param addressCacheHook cache hook
     */
    public BuyClientBuilder productCacheHook(ProductCacheHook productCacheHook) {
        this.productCacheHook = productCacheHook;
        return this;
    }

    /**
     * Sets cache hook for {@link StoreService} API requests
     *
     * @param addressCacheHook cache hook
     */
    public BuyClientBuilder storeCacheHook(StoreCacheHook storeCacheHook) {
        this.storeCacheHook = storeCacheHook;
        return this;
    }

    /**
<<<<<<< HEAD
=======
     * Sets the default http timeouts for new connections.
     * A value of 0 means no timeout, otherwise values must be between 1 and Long.MAX_VALUE.
     *
     * @param httpConnectionTimeoutMs default connect timeout for new connections in milliseconds
     * @param httpReadWriteTimeoutMs  default read/write timeout for new connections in milliseconds
     * @return {@link BuyClientBuilder}
     */
    public BuyClientBuilder httpTimeout(final long httpConnectionTimeoutMs, final long httpReadWriteTimeoutMs) {
        this.httpConnectionTimeoutMs = httpConnectionTimeoutMs;
        this.httpReadWriteTimeoutMs = httpReadWriteTimeoutMs;
        return this;
    }

    /**
>>>>>>> badfe80a8c57d2ff613fd612179974209c42d47c
     * Builds default implementation of {@link BuyClient}
     *
     * @return A {@link BuyClient}.
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

        if (TextUtils.isEmpty(apiKey)) {
            throw new IllegalArgumentException("apiKey is not set or invalid. apiKey must be provided, and cannot be empty");
        }

        if (TextUtils.isEmpty(appId)) {
            throw new IllegalArgumentException("appId is not set or invalid. appId must be provided, and cannot be empty");
        }

        if (TextUtils.isEmpty(applicationName)) {
            throw new IllegalArgumentException("applicationName is not set or invalid. applicationName must be provided, and cannot be empty");
        }

        return new BuyClientDefault(
            apiKey,
            appId,
            applicationName,
            shopDomain,
            customerToken,
            callbackScheduler,
            productPageSize,
            networkRequestRetryMaxCount,
            networkRequestRetryDelayMs,
            networkRequestRetryBackoffMultiplier,
            httpConnectionTimeoutMs,
            httpReadWriteTimeoutMs,
            addressCacheHook,
            customerCacheHook,
            orderCacheHook,
            productCacheHook,
            storeCacheHook,
            interceptors
        );
    }
}
