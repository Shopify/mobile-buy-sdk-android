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

import com.shopify.buy.dataprovider.cache.CheckoutCacheHook;
import com.shopify.buy.model.Checkout;
import com.shopify.buy.model.Payment;
import com.shopify.buy.model.ShippingRate;

import java.util.List;

import rx.functions.Action1;

final class CheckoutCacheRxHookProvider {

    private static final Action1 EMPTY_CACHE_HOOK = new Action1() {
        @Override
        public void call(Object o) {
        }
    };

    final CheckoutCacheHook cacheHook;

    CheckoutCacheRxHookProvider(final CheckoutCacheHook cacheHook) {
        this.cacheHook = cacheHook;
    }

    @SuppressWarnings("unchecked")
    Action1<Checkout> getCheckoutCacheHook() {
        if (cacheHook == null) {
            return EMPTY_CACHE_HOOK;
        } else {
            return new Action1<Checkout>() {
                @Override
                public void call(final Checkout checkout) {
                    try {
                        cacheHook.cacheCheckout(checkout);
                    } catch (Exception e) {

                    }
                }
            };
        }
    }

    @SuppressWarnings("unchecked")
    Action1<List<ShippingRate>> getShippingRatesCacheHook(final String checkoutToken) {
        if (cacheHook == null) {
            return EMPTY_CACHE_HOOK;
        } else {
            return new Action1<List<ShippingRate>>() {
                @Override
                public void call(final List<ShippingRate> shippingRates) {
                    try {
                        cacheHook.cacheShippingRates(checkoutToken, shippingRates);
                    } catch (Exception e) {

                    }
                }
            };
        }
    }

    @SuppressWarnings("unchecked")
    Action1<Payment> getPaymentCacheHook(final String checkoutToken) {
        if (cacheHook == null) {
            return EMPTY_CACHE_HOOK;
        } else {
            return new Action1<Payment>() {
                @Override
                public void call(final Payment payment) {
                    try {
                        cacheHook.cachePayment(checkoutToken, payment);
                    } catch (Exception e) {
                    }
                }
            };
        }
    }
}
