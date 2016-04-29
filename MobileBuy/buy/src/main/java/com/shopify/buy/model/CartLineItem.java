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
 * CartLineItem is a subclass of {@link LineItem} that extends the object by exposing the {@link ProductVariant} that the line item was initialized with.
 *
 * Note that this object is only used for a {@link Cart} and line item objects on a {@link Checkout} are represented by {@link LineItem} objects that only contain the variant ID (if created from a {@link ProductVariant}).
 */
public class CartLineItem extends LineItem {

    private final ProductVariant variant;
    private String errorMessage;

    public CartLineItem(ProductVariant variant) {
        super(variant);
        this.variant = variant;
        this.errorMessage = null;
    }

    public CartLineItem(LineItem lineItem, ProductVariant variant) {
        variantId = lineItem.getVariantId();
        price = lineItem.getPrice();
        title = lineItem.getTitle();
        requiresShipping = lineItem.isRequiresShipping();
        quantity = lineItem.getQuantity();
        this.variant = variant;
    }

    /**
     * @return The {@link ProductVariant} object associated with this line item.
     */
    public ProductVariant getVariant() {
        return variant;
    }

    /**
     * @param quantity The quantity of the {@link ProductVariant} being purchased in this line item.
     */
    void setQuantity(long quantity) {
        this.quantity = quantity;
    }

    /**
     * @return The {@link #errorMessage} associated with this line item.
     */
    public String getErrorMessage() {
        return errorMessage;
    }

    /**
     * @param errorMessage The error message of the {@link ProductVariant} to be displayed in this line item.
     */
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

}
