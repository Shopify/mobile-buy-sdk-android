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

import java.util.List;

import rx.Observable;

/**
 * Service that provides Address API endpoints.
 */
public interface AddressService {

    /**
     * Create an Address and associate it with the currently logged in Customer
     *
     * @param address  the {@link Address} to create, not null
     * @param callback the {@link Callback} that will be used to indicate the response from the asynchronous network operation, not null
     * @return cancelable task
     */
    CancellableTask createAddress(Address address, Callback<Address> callback);

    /**
     * Create an Address and associate it with the currently logged in Customer
     *
     * @param address  the {@link Address} to create, not null
     * @return cold observable that emits created address associated with a customer
     */
    Observable<Address> createAddress(Address address);

    /**
     * Fetch all of the Addresses associated with the currently logged in Customer.
     *
     * @param callback the {@link Callback} that will be used to indicate the response from the asynchronous network operation, not null
     * @return cancelable task
     */
    CancellableTask getAddresses(Callback<List<Address>> callback);

    /**
     * Fetch all of the Addresses associated with the currently logged in Customer.
     *
     * @return cold observable that emits the requested list of addresses associated with a customer
     */
    Observable<List<Address>> getAddresses();

    /**
     * Fetch an existing Address from Shopify
     *
     * @param addressId the identifier of the {@link Address} to fetch, not null
     * @param callback  the {@link Callback} that will be used to indicate the response from the asynchronous network operation, not null
     * @return cancelable task
     */
    CancellableTask getAddress(Long addressId, Callback<Address> callback);

    /**
     * Fetch an existing Address from Shopify
     *
     * @param addressId the identifier of the {@link Address} to fetch, not null
     * @return cold observable that emits requested existing address
     */
    Observable<Address> getAddress(Long addressId);

    /**
     * Update the attributes of an existing Address
     *
     * @param address  the {@link Address} to update, not null
     * @param callback the {@link Callback} that will be used to indicate the response from the asynchronous network operation, not null
     * @return cancelable task
     */
    CancellableTask updateAddress(Address address, Callback<Address> callback);

    /**
     * Update the attributes of an existing Address
     *
     * @param address  the {@link Address} to update, not null
     * @return returns cold observable that emits updated existing address
     */
    Observable<Address> updateAddress(Address address);

    /**
     * Delete an Address
     *
     * @param addressId the identifier of the {@link Address} to fetch, not null
     * @param callback  the {@link Callback} that will be used to indicate the response from the asynchronous network operation, not null
     * @return cancelable task
     */
    CancellableTask deleteAddress(Long addressId, Callback<Void> callback);

    /**
     * Delete an Address
     *
     * @param addressId the identifier of the {@link Address} to fetch, not null
     * @return cancelable task
     */
    Observable<Void> deleteAddress(Long addressId);
}
