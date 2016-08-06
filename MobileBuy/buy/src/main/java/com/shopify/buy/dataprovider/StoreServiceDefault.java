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

import com.shopify.buy.model.Shop;

import retrofit2.Response;
import retrofit2.Retrofit;
import rx.Observable;
import rx.Scheduler;
import rx.functions.Func1;

final class StoreServiceDefault implements StoreService {

    final StoreRetrofitService retrofitService;

    final NetworkRetryPolicyProvider networkRetryPolicyProvider;

    final Scheduler callbackScheduler;

    final StoreCacheRxHookProvider cacheRxHookProvider;

    StoreServiceDefault(
        final Retrofit retrofit,
        final NetworkRetryPolicyProvider networkRetryPolicyProvider,
        final StoreCacheRxHookProvider cacheRxHookProvider,
        final Scheduler callbackScheduler
    ) {
        this.retrofitService = retrofit.create(StoreRetrofitService.class);
        this.networkRetryPolicyProvider = networkRetryPolicyProvider;
        this.cacheRxHookProvider = cacheRxHookProvider;
        this.callbackScheduler = callbackScheduler;
    }

    @Override
    public CancellableTask getShop(final Callback<Shop> callback) {
        return new CancellableTaskSubscriptionWrapper(getShop().subscribe(new InternalCallbackSubscriber<>(callback)));
    }

    @Override
    public Observable<Shop> getShop() {
        return retrofitService
            .getShop()
            .retryWhen(networkRetryPolicyProvider.provide())
            .doOnNext(new RetrofitSuccessHttpStatusCodeHandler<>())
            .map(new Func1<Response<Shop>, Shop>() {
                @Override
                public Shop call(Response<Shop> response) {
                    return response.body();
                }
            })
            .doOnNext(cacheRxHookProvider.getStoreCacheHook())
            .onErrorResumeNext(new BuyClientExceptionHandler<Shop>())
            .observeOn(callbackScheduler);
    }
}
