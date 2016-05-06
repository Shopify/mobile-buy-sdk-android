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

import com.shopify.buy.dataprovider.cache.OrderCacheHook;
import com.shopify.buy.model.Order;
import com.shopify.buy.model.internal.OrderWrapper;
import com.shopify.buy.model.internal.OrdersWrapper;

import java.util.List;

import retrofit2.Retrofit;
import rx.Observable;
import rx.Scheduler;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Default implementation of {@link OrderService}
 */
final class OrderServiceDefault implements OrderService {

    final OrderRetrofitService retrofitService;

    final NetworkRetryPolicyProvider networkRetryPolicyProvider;

    final Scheduler callbackScheduler;

    private Func1<Long, Action1<Order>> cacheOrderHookProvider;

    private Func1<Long, Action1<List<Order>>> cacheOrdersHookProvider;

    OrderServiceDefault(
        final Retrofit retrofit,
        final NetworkRetryPolicyProvider networkRetryPolicyProvider,
        final OrderCacheHook cacheHook,
        final Scheduler callbackScheduler
    ) {
        this.retrofitService = retrofit.create(OrderRetrofitService.class);
        this.networkRetryPolicyProvider = networkRetryPolicyProvider;
        this.callbackScheduler = callbackScheduler;

        initCacheHookProviders(cacheHook);
    }

    private void initCacheHookProviders(final OrderCacheHook cacheHook) {
        cacheOrderHookProvider = new Func1<Long, Action1<Order>>() {
            @Override
            public Action1<Order> call(final Long customerId) {
                return new Action1<Order>() {
                    @Override
                    public void call(final Order order) {
                        if (cacheHook != null) {
                            try {
                                cacheHook.cacheOrder(customerId, order);
                            } catch (Exception e) {
                            }
                        }
                    }
                };
            }
        };

        cacheOrdersHookProvider = new Func1<Long, Action1<List<Order>>>() {
            @Override
            public Action1<List<Order>> call(final Long customerId) {
                return new Action1<List<Order>>() {
                    @Override
                    public void call(final List<Order> orders) {
                        if (cacheHook != null) {
                            try {
                                cacheHook.cacheOrders(customerId, orders);
                            } catch (Exception e) {
                            }
                        }
                    }
                };
            }
        };
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
            .doOnNext(cacheOrdersHookProvider.call(customerId))
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
            .doOnNext(cacheOrderHookProvider.call(customerId))
            .onErrorResumeNext(new BuyClientExceptionHandler<Order>())
            .observeOn(callbackScheduler);
    }
}
