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

import com.shopify.buy.dataprovider.cache.CustomerCacheHook;
import com.shopify.buy.model.Customer;
import com.shopify.buy.model.CustomerToken;

import rx.functions.Action1;

final class CustomerCacheRxHookProvider {

    private static final Action1 EMPTY_CACHE_HOOK = new Action1() {
        @Override
        public void call(Object o) {
        }
    };

    final CustomerCacheHook cacheHook;

    CustomerCacheRxHookProvider(final CustomerCacheHook cacheHook) {
        this.cacheHook = cacheHook;
    }

    @SuppressWarnings("unchecked")
    Action1<Customer> getCustomerCacheHook() {
        if (cacheHook == null) {
            return EMPTY_CACHE_HOOK;
        } else {
            return new Action1<Customer>() {
                @Override
                public void call(final Customer customer) {
                    try {
                        cacheHook.cacheCustomer(customer);
                    } catch (Exception e) {
                    }
                }
            };
        }
    }

    @SuppressWarnings("unchecked")
    Action1<CustomerToken> getCustomerTokenCacheHook() {
        if (cacheHook == null) {
            return EMPTY_CACHE_HOOK;
        } else {
            return new Action1<CustomerToken>() {
                @Override
                public void call(final CustomerToken customerToken) {
                    try {
                        cacheHook.cacheCustomerToken(customerToken);
                    } catch (Exception e) {
                    }
                }
            };
        }
    }
}
