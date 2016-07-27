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
 * Represents a Gift Card
 */
public class GiftCard extends ShopifyObject {

    public GiftCard(String code) {
        this.code = code;
    }

    private String code;

    private Checkout checkout;

    @SerializedName("last_characters")
    private String lastCharacters;

    private String balance;

    @SerializedName("amount_used")
    private String amountUsed;

    /**
     * @return The unique identifier of this gift card.
     */
    public Long getId() {
        return super.getId();
    }

    /**
     * @return The gift card code. This is only used when applying a gift card and is not visible on a {@link Checkout} object synced with Shopify.
     */
    public String getCode() {
        return code;
    }

    /**
     * @return The last characters of the applied gift card code.
     */
    public String getLastCharacters() {
        return lastCharacters;
    }

    /**
     * @return The amount left on this gift card.
     */
    public String getBalance() {
        return balance;
    }

    /**
     * @return The amount already spent from this gift card.
     */
    public String getAmountUsed() {
        return amountUsed;
    }

    /**
     * For internal use only.
     *
     * @return The {@link Checkout} associated with this gift card.
     */
    public Checkout getCheckout() {
        return checkout;
    }

}
