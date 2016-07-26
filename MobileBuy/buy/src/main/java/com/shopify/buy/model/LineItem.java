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
import com.shopify.buy.dataprovider.Callback;

import java.util.HashMap;
import java.util.Map;


/**
 * Represents a Line Item, containing a {@link ProductVariant} and an associated quantity
 */
public class LineItem {

    protected long quantity;

    protected String id;

    protected String price;

    @SerializedName("requires_shipping")
    protected Boolean requiresShipping;

    @SerializedName("variant_id")
    protected Long variantId;

    protected String title;

    @SerializedName("product_id")
    protected String productId;

    @SerializedName("variant_title")
    protected String variantTitle;

    @SerializedName("line_price")
    protected String linePrice;

    @SerializedName("compare_at_price")
    protected String compareAtPrice;

    protected String sku;

    protected Boolean taxable;

    protected long grams;

    @SerializedName("fulfillment_service")
    protected String fulfillmentService;

    protected Map<String, String> properties;

    @SerializedName("total_discount")
    protected String totalDiscount;

    protected LineItem() {
    }

    public LineItem(ProductVariant variant) {
        variantId = variant.getId();
        price = variant.getPrice();
        title = variant.getTitle();
        requiresShipping = variant.isRequiresShipping();
        quantity = 1;
    }

    public LineItem(Long variantId, boolean requiresShipping, long quantity) {
        this.variantId = variantId;
        this.requiresShipping = requiresShipping;
        this.quantity = quantity;
    }

    /**
     * @return The title for the {@link ProductVariant} on this line item.
     */
    public String getVariantTitle() {
        return variantTitle;
    }


    /**
     * @return The line price of the item (price * quantity). This is only available for line items returned using {@link com.shopify.buy.dataprovider.BuyClient#getCheckout(String, Callback)}
     */
    public String getLinePrice() {
        return linePrice;
    }

    /**
     * @return The competitor's price for the same item. You need to set this value on the {@link Product} in your shop admin portal. This is only available for line items returned using {@link com.shopify.buy.dataprovider.BuyClient#getCheckout(String, Callback)}
     */
    public String getCompareAtPrice() {
        return compareAtPrice;
    }

    /**
     * @return The unique SKU for the line item.
     */
    public String getSku() {
        return sku;
    }

    /**
     * @return {@code true} if this line item is taxable, {@code false} otherwise.
     */
    public boolean isTaxable() {
        return taxable != null && taxable;
    }

    /**
     * @return The id of the {@link Product} in this line item.
     */
    public String getProductId() {
        return productId;
    }

    /**
     * @return The weight of the {@link ProductVariant} in this line item (in grams).
     */
    public long getGrams() {
        return grams;
    }

    /**
     * @return Name of the service provider that is doing the fulfillment.
     */
    public String getFulfillmentService() {
        return fulfillmentService;
    }

    /**
     * @return Custom properties set on this line item.
     */
    public Map<String, String> getProperties() {
        if (properties == null) {
            properties = new HashMap<>();
        }
        return properties;
    }

    /**
     * @return The quantity of the {@link ProductVariant} being purchased in this line item.
     */
    public long getQuantity() {
        return quantity;
    }

    /**
     * @return The price of the line item. This price does not need to match the product variant.
     */
    public String getPrice() {
        return price;
    }

    /**
     * @return The title of the line item. The title does not need to match the product variant.
     */
    public String getTitle() {
        return title;
    }

    /**
     * @return The unique identifier for the {@link ProductVariant} being purchased in this line item.
     */
    public Long getVariantId() {
        return variantId;
    }

    /**
     * @return The unique identifier of this line item.
     */
    public String getId() {
        return id;
    }

    /**
     * @return The total discount applied to this line item. This is only available for line items returned using {@link com.shopify.buy.dataprovider.BuyClient#getCustomer(Callback)}.
     */
    public String getTotalDiscount() {
        return totalDiscount;
    }

    /**
     * @return {@code true} if the product that is being purchased in this line item requires shipping, {@code false} otherwise.
     */
    public boolean isRequiresShipping() {
        return requiresShipping != null && requiresShipping;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof LineItem) {
            Long otherVariantId = ((LineItem) o).getVariantId();
            return (otherVariantId != null && otherVariantId.equals(variantId));
        }
        return false;
    }
}
