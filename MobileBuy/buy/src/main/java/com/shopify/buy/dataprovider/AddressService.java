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
import com.shopify.buy.model.Customer;

import java.util.List;

import rx.Observable;

/**
 * Service that provides Address API endpoints.
 */
public interface AddressService {

    /**
     * Create an Address and associate it with a Customer
     *
     * @param customer the {@link Customer} to create and address for, not null
     * @param address  the {@link Address} to create, not null
     * @param callback the {@link Callback} that will be used to indicate the response from the asynchronous network operation, not null
     * @return cancelable task
     */
    CancellableTask createAddress(Customer customer, Address address, Callback<Address> callback);

    /**
     * Create an Address and associate it with a Customer
     *
     * @param customer the {@link Customer} to create and address for, not null
     * @param address  the {@link Address} to create, not null
     * @return cold observable that emits created address associated with a customer
     */
    Observable<Address> createAddress(Customer customer, Address address);

    /**
     * Fetch all of the Addresses associated with a Customer.
     *
     * @param customer the {@link Customer} to fetch addresses for, not null
     * @param callback the {@link Callback} that will be used to indicate the response from the asynchronous network operation, not null
     * @return cancelable task
     */
    CancellableTask getAddresses(Customer customer, Callback<List<Address>> callback);

    /**
     * Fetch all of the Addresses associated with a Customer.
     *
     * @param customer the {@link Customer} to fetch addresses for, not null
     * @return cold observable that emits the requested list of addresses associated with a customer
     */
    Observable<List<Address>> getAddresses(Customer customer);

    /**
     * Fetch an existing Address from Shopify
     *
     * @param customer  the {@link Customer} to fetch an address for, not null
     * @param addressId the identifier of the {@link Address}
     * @param callback  the {@link Callback} that will be used to indicate the response from the asynchronous network operation, not null
     * @return cancelable task
     */
    CancellableTask getAddress(Customer customer, String addressId, Callback<Address> callback);

    /**
     * Fetch an existing Address from Shopify
     *
     * @param customer  the {@link Customer} to fetch an address for, not null
     * @param addressId the identifier of the {@link Address}
     * @return cold observable that emits requested existing address
     */
    Observable<Address> getAddress(Customer customer, String addressId);

    /**
     * Update the attributes of an existing Address
     *
     * @param customer the {@link Customer} to updatne an address for, not null
     * @param address  the {@link Address} to update
     * @param callback the {@link Callback} that will be used to indicate the response from the asynchronous network operation, not null
     * @return cancelable task
     */
    CancellableTask updateAddress(Customer customer, Address address, Callback<Address> callback);

    /**
     * Update the attributes of an existing Address
     *
     * @param customer the {@link Customer} to updatne an address for, not null
     * @param address  the {@link Address} to update
     * @return returns cold observable that emits updated existing address
     */
    Observable<Address> updateAddress(Customer customer, Address address);
}
