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

import com.shopify.buy.dataprovider.cache.AddressCacheHook;
import com.shopify.buy.model.Address;
import com.shopify.buy.model.Customer;
import com.shopify.buy.model.internal.AddressWrapper;
import com.shopify.buy.model.internal.AddressesWrapper;

import java.util.List;

import retrofit2.Retrofit;
import rx.Observable;
import rx.Scheduler;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Default implementation of {@link AddressService}
 */
final class AddressServiceDefault implements AddressService {

    final AddressRetrofitService retrofitService;

    final NetworkRetryPolicyProvider networkRetryPolicyProvider;

    final Scheduler callbackScheduler;

    private Func1<Customer, Action1<Address>> cacheAddressHookProvider;

    private Func1<Customer, Action1<List<Address>>> cacheAddressesHookProvider;

    AddressServiceDefault(
            final Retrofit retrofit,
            final NetworkRetryPolicyProvider networkRetryPolicyProvider,
            final AddressCacheHook cacheHook,
            final Scheduler callbackScheduler
    ) {
        this.retrofitService = retrofit.create(AddressRetrofitService.class);
        this.networkRetryPolicyProvider = networkRetryPolicyProvider;
        this.callbackScheduler = callbackScheduler;

        initCacheHookProviders(cacheHook);
    }

    private void initCacheHookProviders(final AddressCacheHook cacheHook) {
        cacheAddressHookProvider = new Func1<Customer, Action1<Address>>() {
            @Override
            public Action1<Address> call(final Customer customer) {
                return new Action1<Address>() {
                    @Override
                    public void call(final Address address) {
                        if (cacheHook != null) {
                            try {
                                cacheHook.cacheAddress(customer, address);
                            } catch (Exception e) {

                            }
                        }
                    }
                };
            }
        };

        cacheAddressesHookProvider = new Func1<Customer, Action1<List<Address>>>() {
            @Override
            public Action1<List<Address>> call(final Customer customer) {
                return new Action1<List<Address>>() {
                    @Override
                    public void call(final List<Address> addresses) {
                        if (cacheHook != null) {
                            try {
                                cacheHook.cacheAddresses(customer, addresses);
                            } catch (Exception e) {

                            }
                        }
                    }
                };
            }
        };
    }

    @Override
    public void createAddress(final Customer customer, final Address address, final Callback<Address> callback) {
        createAddress(customer, address).subscribe(new InternalCallbackSubscriber<>(callback));
    }

    @Override
    public Observable<Address> createAddress(final Customer customer, final Address address) {
        if (address == null) {
            throw new NullPointerException("address cannot be null");
        }

        if (customer == null) {
            throw new NullPointerException("customer cannot be null");
        }

        return retrofitService
                .createAddress(customer.getId(), new AddressWrapper(address))
                .doOnNext(new RetrofitSuccessHttpStatusCodeHandler<>())
                .compose(new UnwrapRetrofitBodyTransformer<AddressWrapper, Address>())
                .doOnNext(cacheAddressHookProvider.call(customer))
                .observeOn(callbackScheduler);
    }

    @Override
    public void getAddresses(final Customer customer, final Callback<List<Address>> callback) {
        getAddresses(customer).subscribe(new InternalCallbackSubscriber<>(callback));
    }

    @Override
    public Observable<List<Address>> getAddresses(final Customer customer) {
        if (customer == null) {
            throw new NullPointerException("customer cannot be null");
        }

        return retrofitService
                .getAddresses(customer.getId())
                .retryWhen(networkRetryPolicyProvider.provide())
                .doOnNext(new RetrofitSuccessHttpStatusCodeHandler<>())
                .compose(new UnwrapRetrofitBodyTransformer<AddressesWrapper, List<Address>>())
                .doOnNext(cacheAddressesHookProvider.call(customer))
                .observeOn(callbackScheduler);
    }

    @Override
    public void getAddress(final Customer customer, final String addressId, final Callback<Address> callback) {
        getAddress(customer, addressId).subscribe(new InternalCallbackSubscriber<>(callback));
    }

    @Override
    public Observable<Address> getAddress(final Customer customer, final String addressId) {
        if (TextUtils.isEmpty(addressId)) {
            throw new IllegalArgumentException("addressId cannot be empty");
        }

        if (customer == null) {
            throw new NullPointerException("customer cannot be null");
        }

        return retrofitService
                .getAddress(customer.getId(), addressId)
                .retryWhen(networkRetryPolicyProvider.provide())
                .doOnNext(new RetrofitSuccessHttpStatusCodeHandler<>())
                .compose(new UnwrapRetrofitBodyTransformer<AddressWrapper, Address>())
                .doOnNext(cacheAddressHookProvider.call(customer))
                .observeOn(callbackScheduler);
    }

    @Override
    public void updateAddress(final Customer customer, final Address address, final Callback<Address> callback) {
        updateAddress(customer, address).subscribe(new InternalCallbackSubscriber<>(callback));
    }

    @Override
    public Observable<Address> updateAddress(final Customer customer, final Address address) {
        if (address == null) {
            throw new NullPointerException("address cannot be null");
        }

        if (customer == null) {
            throw new NullPointerException("customer cannot be null");
        }

        return retrofitService
                .updateAddress(customer.getId(), new AddressWrapper(address), address.getAddressId())
                .doOnNext(new RetrofitSuccessHttpStatusCodeHandler<>())
                .compose(new UnwrapRetrofitBodyTransformer<AddressWrapper, Address>())
                .doOnNext(cacheAddressHookProvider.call(customer))
                .observeOn(callbackScheduler);
    }
}
