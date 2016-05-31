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

import com.shopify.buy.model.Address;
import com.shopify.buy.model.internal.AddressWrapper;
import com.shopify.buy.model.internal.AddressesWrapper;

import java.util.List;

import retrofit2.Response;
import retrofit2.Retrofit;
import rx.Observable;
import rx.Scheduler;
import rx.functions.Func1;

import static java.net.HttpURLConnection.HTTP_NO_CONTENT;

/**
 * Default implementation of {@link AddressService}
 */
final class AddressServiceDefault implements AddressService {

    final AddressRetrofitService retrofitService;

    final NetworkRetryPolicyProvider networkRetryPolicyProvider;

    final Scheduler callbackScheduler;

    AddressServiceDefault(
        final Retrofit retrofit,
        final NetworkRetryPolicyProvider networkRetryPolicyProvider,
        final Scheduler callbackScheduler
    ) {
        this.retrofitService = retrofit.create(AddressRetrofitService.class);
        this.networkRetryPolicyProvider = networkRetryPolicyProvider;
        this.callbackScheduler = callbackScheduler;
    }

    @Override
    public CancellableTask createAddress(final Long customerId, final Address address, final Callback<Address> callback) {
        return new CancellableTaskSubscriptionWrapper(createAddress(customerId, address).subscribe(new InternalCallbackSubscriber<>(callback)));
    }

    @Override
    public Observable<Address> createAddress(final Long customerId, final Address address) {
        if (customerId == null) {
            throw new NullPointerException("customerId cannot be null");
        }

        if (address == null) {
            throw new NullPointerException("address cannot be null");
        }

        return retrofitService
            .createAddress(customerId, new AddressWrapper(address))
            .doOnNext(new RetrofitSuccessHttpStatusCodeHandler<>())
            .compose(new UnwrapRetrofitBodyTransformer<AddressWrapper, Address>())
            .onErrorResumeNext(new BuyClientExceptionHandler<Address>())
            .observeOn(callbackScheduler);
    }

    public CancellableTask deleteAddress(Long customerId, Long addressId, Callback<Void> callback) {
        return new CancellableTaskSubscriptionWrapper(deleteAddress(customerId, addressId).subscribe(new InternalCallbackSubscriber<>(callback)));
    }

    public Observable<Void> deleteAddress(Long customerId, Long addressId) {
        if (customerId == null) {
            throw new NullPointerException("customerId cannot be null");
        }

        if (addressId == null) {
            throw new NullPointerException("addressId cannot be null");
        }

        int[] successCodes = {HTTP_NO_CONTENT};

        return retrofitService.deleteAddress(customerId, addressId)
            .doOnNext(new RetrofitSuccessHttpStatusCodeHandler<>(successCodes))
            .map(new Func1<Response<Void>, Void>() {
                @Override
                public Void call(Response<Void> voidResponse) {
                    return voidResponse.body();
                }
            })
            .onErrorResumeNext(new BuyClientExceptionHandler<Void>())
            .observeOn(callbackScheduler);
    }

    @Override
    public CancellableTask getAddresses(final Long customerId, final Callback<List<Address>> callback) {
        return new CancellableTaskSubscriptionWrapper(getAddresses(customerId).subscribe(new InternalCallbackSubscriber<>(callback)));
    }

    @Override
    public Observable<List<Address>> getAddresses(final Long customerId) {
        if (customerId == null) {
            throw new NullPointerException("customerId cannot be null");
        }

        return retrofitService
            .getAddresses(customerId)
            .retryWhen(networkRetryPolicyProvider.provide())
            .doOnNext(new RetrofitSuccessHttpStatusCodeHandler<>())
            .compose(new UnwrapRetrofitBodyTransformer<AddressesWrapper, List<Address>>())
            .onErrorResumeNext(new BuyClientExceptionHandler<List<Address>>())
            .observeOn(callbackScheduler);
    }

    @Override
    public CancellableTask getAddress(final Long customerId, final Long addressId, final Callback<Address> callback) {
        return new CancellableTaskSubscriptionWrapper(getAddress(customerId, addressId).subscribe(new InternalCallbackSubscriber<>(callback)));
    }

    @Override
    public Observable<Address> getAddress(final Long customerId, final Long addressId) {
        if (customerId == null) {
            throw new NullPointerException("customerId cannot be null");
        }

        if (addressId == null) {
            throw new NullPointerException("addressId cannot be null");
        }

        return retrofitService
            .getAddress(customerId, addressId)
            .retryWhen(networkRetryPolicyProvider.provide())
            .doOnNext(new RetrofitSuccessHttpStatusCodeHandler<>())
            .compose(new UnwrapRetrofitBodyTransformer<AddressWrapper, Address>())
            .onErrorResumeNext(new BuyClientExceptionHandler<Address>())
            .observeOn(callbackScheduler);
    }

    @Override
    public CancellableTask updateAddress(final Long customerId, final Address address, final Callback<Address> callback) {
        return new CancellableTaskSubscriptionWrapper(updateAddress(customerId, address).subscribe(new InternalCallbackSubscriber<>(callback)));
    }

    @Override
    public Observable<Address> updateAddress(final Long customerId, final Address address) {
        if (customerId == null) {
            throw new NullPointerException("customerId cannot be null");
        }

        if (address == null) {
            throw new NullPointerException("address cannot be null");
        }

        return retrofitService
            .updateAddress(customerId, new AddressWrapper(address), address.getId())
            .doOnNext(new RetrofitSuccessHttpStatusCodeHandler<>())
            .compose(new UnwrapRetrofitBodyTransformer<AddressWrapper, Address>())
            .onErrorResumeNext(new BuyClientExceptionHandler<Address>())
            .observeOn(callbackScheduler);
    }
}
