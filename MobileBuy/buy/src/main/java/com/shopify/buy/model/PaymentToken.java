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

    /**
     * Creates a {@code PaymentToken} for use in completing credit card transaction
     * @param paymentSessionId The payment session id
     * @return a {@code PaymentToken}
     */
    public static PaymentToken createCreditCardPaymentToken(String paymentSessionId) {
        return new PaymentToken(paymentSessionId);
    }

    /**
     * Creates a {@code PaymentToken} for use in completing an Android Pay transaction
     * @param token The Android Pay token
     * @param publicKeyHash The Android Pay public key
     * @return an empty {@code PaymentToken}
     */
    public static PaymentToken createAndroidPayPaymentToken(String token, String publicKeyHash) {
        return new PaymentToken(token, "android_pay", publicKeyHash);
    }

    /**
     * Creates a {@code PaymentToken} with an empty body.  This token is only valid if the {@link Checkout#getPaymentDue()} is 0.
     * @return an empty {@code PaymentToken}
     */
    public static PaymentToken createEmptyPaymentToken() {
        return new PaymentToken();
    }

    @SerializedName("payment_session_id")
    private final String paymentSessionId;

    @SerializedName("payment_token")
    private final Wrapper wrapper;

    private PaymentToken(String paymentSessionId) {
        this.paymentSessionId = paymentSessionId;
        wrapper = null;
    }

    private PaymentToken() {
        paymentSessionId = null;
        wrapper = null;
    }

    private PaymentToken(String token, String type, String publicKeyHash) {
        paymentSessionId = null;
        wrapper = new Wrapper(token, type, publicKeyHash);
    }

    private static final class Wrapper {

        private final static String SOURCE_NAME = "sdk";

        @SerializedName("payment_data")
        private final String paymentData;

        @SerializedName("type")
        private final String type;

        private final String source = SOURCE_NAME;

        @SerializedName("identifier")
        private final String identifier;

        private Wrapper(String paymentData, String type, String identifier) {
            this.paymentData = paymentData;
            this.type = type;
            this.identifier = identifier;
        }
    }
}
