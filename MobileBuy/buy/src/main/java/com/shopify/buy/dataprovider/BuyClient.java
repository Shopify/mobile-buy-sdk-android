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
public interface BuyClient extends ProductService, CheckoutService, CustomerService, OrderService, AddressService {

    String getApiKey();

    String getAppId();

    String getApplicationName();

    String getWebReturnToUrl();

    String getWebReturnToLabel();

    String getShopDomain();

    int getPageSize();

    Scheduler getCallbackScheduler();

    /**
     * Returns the {@link Customer} specific token
     *
     * @return customer token
     */
    CustomerToken getCustomerToken();

    void setCustomerToken(CustomerToken customerToken);

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
