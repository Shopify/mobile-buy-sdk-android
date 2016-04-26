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

package com.shopify.buy.model;

import com.google.gson.annotations.SerializedName;

/**
 * Represents the token object associated with a payment
 */
public class Payment {

    @SerializedName("amount")
    private String amount;

    @SerializedName("amount_in")
    private String amountIn;

    @SerializedName("amount_out")
    private String amountOut;

    @SerializedName("amount_rounding")
    private String amountRounding;

    @SerializedName("authorization")
    private String authorization;

    @SerializedName("created_at")
    private String createdAt;

    @SerializedName("currency")
    private String currency;

    @SerializedName("error_code")
    private String errorCode;

    @SerializedName("gateway")
    private String gateWay;

    @SerializedName("id")
    private Long id;

    @SerializedName("kind")
    private String kind;

    @SerializedName("message")
    private String message;

    @SerializedName("status")
    private String status;

    @SerializedName("test")
    private Boolean test;

    @SerializedName("checkout")
    private Checkout checkout;

    public Payment() {
    }

    public String getAmount() {
        return amount;
    }

    public String getAmountIn() {
        return amountIn;
    }

    public String getAmountOut() {
        return amountOut;
    }

    public String getAmountRounding() {
        return amountRounding;
    }

    public String getAuthorization() {
        return authorization;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getCurrency() {
        return currency;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getGateWay() {
        return gateWay;
    }

    public Long getId() {
        return id;
    }

    public String getKind() {
        return kind;
    }

    public String getMessage() {
        return message;
    }

    public String getStatus() {
        return status;
    }

    public Boolean getTest() {
        return test;
    }

    public Checkout getCheckout() {
        return checkout;
    }
}
