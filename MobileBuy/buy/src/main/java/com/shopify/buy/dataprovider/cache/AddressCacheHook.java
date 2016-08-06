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
package com.shopify.buy.dataprovider.cache;

import com.shopify.buy.model.Address;

import java.util.List;

/**
 * Cache hook that will be triggered by {@link com.shopify.buy.dataprovider.AddressService}. By default all caching operates
 * on the background thread.
 */
public interface AddressCacheHook {

    /**
     * Caches customer address
     *
     * @param customerId customer id
     * @param address    customer address
     */
    void cacheAddress(Long customerId, Address address);

    /**
     * Caches customer list of addresses
     *
     * @param customerId customer id
     * @param addresses  customer addresses
     */
    void cacheAddresses(Long customerId, List<Address> addresses);

    /**
     * Deletes address from cache
     *
     * @param customerId customer id
     * @param addressId  customer address id
     */
    void deleteAddress(Long customerId, Long addressId);

}
