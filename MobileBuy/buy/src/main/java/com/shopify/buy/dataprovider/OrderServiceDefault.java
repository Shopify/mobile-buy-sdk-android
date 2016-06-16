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
import com.shopify.buy.model.internal.OrderWrapper;
import com.shopify.buy.model.internal.OrdersWrapper;

import java.util.List;

import retrofit2.Retrofit;
import rx.Observable;
import rx.Scheduler;

/**
 * Default implementation of {@link OrderService}
 */
final class OrderServiceDefault implements OrderService {

    final OrderRetrofitService retrofitService;

    final NetworkRetryPolicyProvider networkRetryPolicyProvider;

    final OrderCacheRxHookProvider cacheRxHookProvider;

    final Scheduler callbackScheduler;

    OrderServiceDefault(
        final Retrofit retrofit,
        final NetworkRetryPolicyProvider networkRetryPolicyProvider,
        final OrderCacheRxHookProvider cacheRxHookProvider,
        final Scheduler callbackScheduler
    ) {
        this.retrofitService = retrofit.create(OrderRetrofitService.class);
        this.networkRetryPolicyProvider = networkRetryPolicyProvider;
        this.cacheRxHookProvider = cacheRxHookProvider;
        this.callbackScheduler = callbackScheduler;
    }

    @Override
    public CancellableTask getOrders(final Long customerId, final Callback<List<Order>> callback) {
        return new CancellableTaskSubscriptionWrapper(getOrders(customerId).subscribe(new InternalCallbackSubscriber<>(callback)));
    }

    @Override
    public Observable<List<Order>> getOrders(final Long customerId) {
        if (customerId == null) {
            throw new NullPointerException("customerId cannot be null");
        }

        return retrofitService
            .getOrders(customerId)
            .retryWhen(networkRetryPolicyProvider.provide())
            .doOnNext(new RetrofitSuccessHttpStatusCodeHandler<>())
            .compose(new UnwrapRetrofitBodyTransformer<OrdersWrapper, List<Order>>())
            .doOnNext(cacheRxHookProvider.getOrdersCacheHook(customerId))
            .onErrorResumeNext(new BuyClientExceptionHandler<List<Order>>())
            .observeOn(callbackScheduler);
    }

    @Override
    public CancellableTask getOrder(final Long customerId, final Long orderId, final Callback<Order> callback) {
        return new CancellableTaskSubscriptionWrapper(getOrder(customerId, orderId).subscribe(new InternalCallbackSubscriber<>(callback)));
    }

    @Override
    public Observable<Order> getOrder(final Long customerId, final Long orderId) {
        if (orderId == null) {
            throw new NullPointerException("orderId cannot be null");
        }

        if (customerId == null) {
            throw new NullPointerException("customerId cannot be null");
        }

        return retrofitService
            .getOrder(customerId, orderId)
            .retryWhen(networkRetryPolicyProvider.provide())
            .doOnNext(new RetrofitSuccessHttpStatusCodeHandler<>())
            .compose(new UnwrapRetrofitBodyTransformer<OrderWrapper, Order>())
            .doOnNext(cacheRxHookProvider.getOrderCacheHook(customerId))
            .onErrorResumeNext(new BuyClientExceptionHandler<Order>())
            .observeOn(callbackScheduler);
    }
}
