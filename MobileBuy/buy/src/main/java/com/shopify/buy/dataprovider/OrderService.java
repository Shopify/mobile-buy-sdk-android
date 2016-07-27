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

import com.shopify.buy.model.Order;

import java.util.List;

import rx.Observable;

/**
 * Service that provides Order API endpoints.
 */
public interface OrderService {

    /**
     * Fetch the Orders associated with the currently logged in Customer.
     *
     * @param callback the {@link Callback} that will be used to indicate the response from the asynchronous network operation, not null
     * @return cancelable task
     */
    CancellableTask getOrders(Callback<List<Order>> callback);

    /**
     * Fetch the Orders associated with the currently logged in Customer.
     *
     * @return cold observable that emits requested list of orders associated with a customer
     */
    Observable<List<Order>> getOrders();

    /**
     * Fetch an existing Order from Shopify
     *
     * @param orderId  the identifier of the {@link Order} to retrieve, not null
     * @param callback the {@link Callback} that will be used to indicate the response from the asynchronous network operation, not null
     * @return cancelable task
     */
    CancellableTask getOrder(Long orderId, Callback<Order> callback);

    /**
     * Fetch an existing Order from Shopify
     *
     * @param orderId  the identifier of the {@link Order} to retrieve, not null
     * @return cold observable that emits requested existing order
     */
    Observable<Order> getOrder(Long orderId);

}
