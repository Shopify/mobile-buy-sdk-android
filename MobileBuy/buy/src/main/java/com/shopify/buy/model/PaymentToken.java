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
package com.shopify.buy.model;

import com.google.gson.annotations.SerializedName;

/**
 *  Class that represents the payment session associated with the checkout to be completed
 */
public final class PaymentToken {

    public static PaymentToken createCreditCardPaymentToken(String paymentSessionId) {
        return new PaymentToken(paymentSessionId);
    }

    public static PaymentToken createAndroidPayPaymentToken(String token, String publicKeyHash) {
        return new PaymentToken(token, "android_pay", publicKeyHash);
    }

    @SerializedName("payment_session_id")
    private final String paymentSessionId;

    @SerializedName("payment_token")
    private final Wrapper wrapper;

    private PaymentToken(String paymentSessionId) {
        this.paymentSessionId = paymentSessionId;
        this.wrapper = null;
    }

    private PaymentToken(String token, String type, String publicKeyHash) {
        this.paymentSessionId = null;
        this.wrapper = new Wrapper(token, type, publicKeyHash);
    }

    private static final class Wrapper {

        @SerializedName("payment_data")
        private final String paymentData;

        @SerializedName("type")
        private final String type;

        @SerializedName("identifier")
        private final String identifier;

        private Wrapper(String paymentData, String type, String identifier) {
            this.paymentData = paymentData;
            this.type = type;
            this.identifier = identifier;
        }
    }
}
