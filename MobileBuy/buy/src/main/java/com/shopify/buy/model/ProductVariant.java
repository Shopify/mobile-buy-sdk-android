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
import com.shopify.buy.utils.DateUtility;

import java.util.Date;
import java.util.List;

/**
 * Represents a specific version of {@link Product}. Each variant has a different combination of size, color or other option(s).
 */
public class ProductVariant extends ShopifyObject {

    protected String title;

    protected String price;

    @SerializedName("option_values")
    private List<OptionValue> optionValues;

    private long grams;

    @SerializedName("compare_at_price")
    private String compareAtPrice;

    private String sku;

    @SerializedName("requires_shipping")
    private boolean requiresShipping;

    private boolean taxable;

    private int position;

    long productId;

    @SerializedName("created_at")
    private Date createdAtDate;

    @SerializedName("updated_at")
    private Date updatedAtDate;

    private boolean available;

    /**
     * @return The title of this variant.
     */
    public String getTitle() {
        return title;
    }

    /**
     * @return The combination of {@link OptionValue} objects that make this variant different from other variants of the same product.
     */
    public List<OptionValue> getOptionValues() {
        return optionValues;
    }

    /**
     * @return The price of this variant.
     */
    public String getPrice() {
        return price;
    }

    /**
     * @return The weight of this variant (in grams).
     */
    public long getGrams() {
        return grams;
    }

    /**
     * @return The competitor's price for the same item. You need to set this value on the {@link Product} in your shop admin portal.
     */
    public String getCompareAtPrice() {
        return compareAtPrice;
    }

    /**
     * @return The unique SKU identifier for this variant.
     */
    public String getSku() {
        return sku;
    }

    /**
     * @return {@code true} if this variant requires shipping, {@code false} otherwise.
     */
    public boolean isRequiresShipping() {
        return requiresShipping;
    }

    /**
     * @return {@code true} if this variant is taxable, {@code false} otherwise.
     */
    public boolean isTaxable() {
        return taxable;
    }

    /**
     * @return The position of this variant relative to other variants of the same product.
     */
    public int getPosition() {
        return position;
    }

    /**
     * @return The unique identifier of the {@link Product} to which this variant belongs.
     */
    public long getProductId() {
        return productId;
    }

    /**
     * Use {@link #getCreatedAtDate() getCreatedAtDate()}.
     */
    @Deprecated
    public String getCreatedAt() {
        return createdAtDate == null ? null : DateUtility.createDefaultDateFormat().format(createdAtDate);
    }

    /**
     * Use {@link #getUpdatedAtDate() getUpdatedAtDate()}.
     */
    @Deprecated
    public String getUpdatedAt() {
        return updatedAtDate == null ? null : DateUtility.createDefaultDateFormat().format(updatedAtDate);

    }

    /**
     * @return {@code true} if this variant is in stock and available for purchase, {@code false} otherwise.
     */
    public boolean isAvailable() {
        return available;
    }

    /**
     * @return The date this variant was created.
     */
    public Date getCreatedAtDate() {
        return createdAtDate;
    }

    /**
     * @return The date this variant was last updated.
     */
    public Date getUpdatedAtDate() {
        return updatedAtDate;
    }

}
