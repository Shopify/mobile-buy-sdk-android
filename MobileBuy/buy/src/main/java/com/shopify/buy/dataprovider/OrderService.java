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

import com.shopify.buy.model.Customer;
import com.shopify.buy.model.Order;

import java.util.List;

import rx.Observable;

/**
 * Service that provides Order API endpoints.
 */
public interface OrderService {

    /**
     * Fetch the Orders associated with a Customer.
     *
     * @param customer the {@link Customer} to fetch the orders for, not null
     * @param callback the {@link Callback} that will be used to indicate the response from the asynchronous network operation, not null
     */
    void getOrders(Customer customer, Callback<List<Order>> callback);

    /**
     * Fetch the Orders associated with a Customer.
     *
     * @param customer the {@link Customer} to fetch the orders for, not null
     * @return cold observable that emits requested list of orders associated with a customer
     */
    Observable<List<Order>> getOrders(Customer customer);

    /**
     * Fetch an existing Order from Shopify
     *
     * @param customer the {@link Customer} to fetch the order for
     * @param orderId  the identifier of the {@link Order} to retrieve
     * @param callback the {@link Callback} that will be used to indicate the response from the asynchronous network operation, not null
     */
    void getOrder(Customer customer, String orderId, Callback<Order> callback);

    /**
     * Fetch an existing Order from Shopify
     *
     * @param customer the {@link Customer} to fetch the order for
     * @param orderId  the identifier of the {@link Order} to retrieve
     * @return cold observable that emits requested existing order
     */
    Observable<Order> getOrder(Customer customer, String orderId);

}
