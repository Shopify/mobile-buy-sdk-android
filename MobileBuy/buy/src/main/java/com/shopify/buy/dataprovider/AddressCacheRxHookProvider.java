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

import com.shopify.buy.dataprovider.cache.AddressCacheHook;
import com.shopify.buy.model.Address;
import com.shopify.buy.model.Customer;

import java.util.List;

import rx.functions.Action1;

final class AddressCacheRxHookProvider {

    private static final Action1 EMPTY_CACHE_HOOK = new Action1() {
        @Override
        public void call(Object o) {
        }
    };

    final AddressCacheHook cacheHook;

    AddressCacheRxHookProvider(final AddressCacheHook cacheHook) {
        this.cacheHook = cacheHook;
    }

    @SuppressWarnings("unchecked")
    Action1<Address> getAddressCacheHook(final Customer customer) {
        if (cacheHook == null) {
            return EMPTY_CACHE_HOOK;
        } else {
            return new Action1<Address>() {
                @Override
                public void call(final Address address) {
                    try {
                        cacheHook.cacheAddress(customer, address);
                    } catch (Exception e) {

                    }
                }
            };
        }
    }

    @SuppressWarnings("unchecked")
    Action1<List<Address>> getAddressesCacheHook(final Customer customer) {
        if (cacheHook == null) {
            return EMPTY_CACHE_HOOK;
        } else {
            return new Action1<List<Address>>() {
                @Override
                public void call(final List<Address> addresses) {
                    try {
                        cacheHook.cacheAddresses(customer, addresses);
                    } catch (Exception e) {

                    }
                }
            };
        }
    }
}
