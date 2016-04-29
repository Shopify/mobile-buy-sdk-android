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

import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Represents an image associated with a {@link Product} or {@link ProductVariant}
 */
public class Image extends ShopifyObject {

    @SerializedName("created_at")
    protected String createdAt;

    protected int position;

    @SerializedName("updated_at")
    protected String updatedAt;

    @SerializedName("product_id")
    protected long productId;

    @SerializedName("variant_ids")
    protected List<Long> variantIds;

    protected String src;

    /**
     * @return Creation date of the image.
     */
    public String getCreatedAt() {
        return createdAt;
    }

    /**
     * @return The index of the image, relative to other images of the product.
     */
    public int getPosition() {
        return position;
    }

    /**
     * @return The date the image was last updated.
     */
    public String getUpdatedAt() {
        return updatedAt;
    }

    /**
     * @return The associated product ID for the image.
     */
    public long getProductId() {
        return productId;
    }

    /**
     * @return An array of product variant ids associated with the image.
     */
    public List<Long> getVariantIds() {
        return variantIds;
    }

    /**
     * @return Specifies the location of the product image.
     */
    public String getSrc() {
        return src;
    }

    @Override
    public boolean equals(Object other) {
        return super.equals(other) && TextUtils.equals(((Image) other).getSrc(), src);
    }
}
