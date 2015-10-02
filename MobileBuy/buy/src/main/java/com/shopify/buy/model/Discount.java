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

/**
 * Represents a Discount that can be applied to a Checkout.
 *
 * @see com.shopify.buy.model.Checkout#setDiscountCode(String)
 */
public class Discount {

    private String amount;

    private boolean applicable;

    private String code;

    public Discount() {}

    public Discount(String code) {
        this.code = code;
    }

    /**
     * @return The amount taken off of the checkout price by this discount.
     */
    public String getAmount() {
        return amount;
    }

    /**
     * @return {@code true} if this discount applies to the checkout, {@code false} otherwise.
     */
    public boolean isApplicable() {
        return applicable;
    }

    /**
     * @return The discount code.
     */
    public String getCode() {
        return code;
    }
}
