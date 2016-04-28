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

import com.shopify.buy.model.Checkout;
import com.shopify.buy.model.Customer;
import com.shopify.buy.model.CustomerToken;
import com.shopify.buy.model.Product;
import com.shopify.buy.model.Shop;

import rx.Observable;
import rx.Scheduler;

/**
 * Buy Client Facade provides all requests needed to perform request on the Shopify Checkout API.
 * Use this class to perform tasks such as getting a shop, getting collections and products for a shop,
 * creating a {@link Checkout} on Shopify and completing Checkouts.
 * All API methods presented here run asynchronously and return results via callback or Rx observables on the callback scheduler thread.
 */
public interface IBuyClient extends ProductService, CheckoutService, CustomerService, OrderService, AddressService {

    String getApiKey();

    String getAppId();

    String getApplicationName();

    String getWebReturnToUrl();

    String getWebReturnToLabel();

    String getShopDomain();

    int getPageSize();

    /**
     * Sets the {@link Customer} specific token
     *
     * @param customerToken
     */
    void setCustomerToken(CustomerToken customerToken);

    Scheduler getCallbackScheduler();

    void setCallbackScheduler(Scheduler callbackScheduler);

    /**
     * Returns the {@link Customer} specific token
     *
     * @return customer token
     */
    CustomerToken getCustomerToken();

    /**
     * Sets the web url to be invoked by the button on the completion page of the web checkout.
     *
     * @param webReturnToUrl a url defined as a custom scheme in the Android Manifest file.
     */
    void setWebReturnToUrl(String webReturnToUrl);

    /**
     * Sets the text to be displayed on the button on the completion page of the web checkout
     *
     * @param webReturnToLabel the text to display on the button.
     */
    void setWebReturnToLabel(String webReturnToLabel);

    /**
     * Sets the page size used for paged API queries
     *
     * @param pageSize the number of {@link Product} to include in a page.  The maximum page size is {@link #MAX_PAGE_SIZE} and the minimum page size is {@link #MIN_PAGE_SIZE}.
     *                 If the page size is less than {@code MIN_PAGE_SIZE}, it will be set to {@code MIN_PAGE_SIZE}.  If the page size is greater than MAX_PAGE_SIZE it will be set to {@code MAX_PAGE_SIZE}.
     *                 The default value is {@link #DEFAULT_PAGE_SIZE}
     */
    void setPageSize(int pageSize);

    /**
     * Fetch metadata about your shop
     *
     * @param callback the {@link Callback} that will be used to indicate the response from the asynchronous network operation, not null
     */
    void getShop(Callback<Shop> callback);

    /**
     * Fetch metadata about your shop
     *
     * @return cold observable that emits requested shop metadata
     */
    Observable<Shop> getShop();

}
