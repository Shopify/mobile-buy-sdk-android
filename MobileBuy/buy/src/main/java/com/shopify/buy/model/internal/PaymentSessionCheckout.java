/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 Shopify Inc.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.shopify.buy.model.internal;

import com.google.gson.annotations.SerializedName;
import com.shopify.buy.model.Address;
import com.shopify.buy.model.CreditCard;

/**
 * Internal structure used to serialize data for creating a payment session
 */
public class PaymentSessionCheckout {

    private String checkoutToken;

    @SerializedName("credit_card")
    private CreditCard creditCard;

    @SerializedName("billing_address")
    private Address billingAddress;

    public PaymentSessionCheckout(String checkoutToken, CreditCard creditCard, Address billingAddress) {
        this.checkoutToken = checkoutToken;
        this.creditCard = creditCard;
        this.billingAddress = billingAddress;
    }
}
