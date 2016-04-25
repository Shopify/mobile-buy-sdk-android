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

import com.shopify.buy.dataprovider.BuyClientFactory;
import com.shopify.buy.utils.CollectionUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * The Cart is the starting point for the Checkout API. You are responsible for building a cart, then transforming it into a {@link Checkout} using the {@link com.shopify.buy.dataprovider.BuyClient}.
 */
public class Cart {

    protected final List<CartLineItem> lineItems;
    protected final Set<ProductVariant> productVariants;

    public Cart() {
        lineItems = new ArrayList<>();
        productVariants = new HashSet<>();
    }

    /**
     * Adds a ProductVariant to the cart. If a LineItem with this ProductVariant already exists in the Cart, that LineItems's quantity is increased by one.
     *
     * @param variant the {@link ProductVariant} to add to the {@code cart}
     */
    public void addVariant(ProductVariant variant) {
        productVariants.add(variant);

        for (CartLineItem lineItem : lineItems) {
            if (lineItem.getVariantId().equals(variant.getId())) {
                lineItem.setQuantity(lineItem.getQuantity() + 1);
                return;
            }
        }

        lineItems.add(new CartLineItem(variant));
    }

    /**
     * If the cart contains a LineItem matching the ProductVariant, its quantity is decremented by 1.
     * If the LineItem's quantity is decremented to 0, it is removed from the cart.
     *
     * @param variant the {@link ProductVariant} to decrement
     */
    public void decrementVariant(ProductVariant variant) {
        for (CartLineItem lineItem : lineItems) {
            if (lineItem.getVariantId().equals(variant.getId()) && lineItem.getQuantity() > 0) {
                lineItem.setQuantity(lineItem.getQuantity() - 1);
                if (lineItem.getQuantity() == 0) {
                    lineItems.remove(lineItem);
                    productVariants.remove(variant);
                }
                return;
            }
        }
    }

    /**
     * Sets the quantity of a ProductVariant in the cart.
     * If a LineItem with this ProductVariant already exists in the Cart, that LineItems's quantity will be set to the new value.
     * If no LineItem with this ProductVariant exists in the Cart, a new LineItem will be created with the specified ProductVariant and quantity.
     *
     * @param variant  the {@link ProductVariant} to update
     * @param quantity the new quantity
     */
    public void setVariantQuantity(ProductVariant variant, int quantity) {
        if (quantity <= 0) {
            productVariants.remove(variant);
        } else {
            productVariants.add(variant);
        }

        for (CartLineItem lineItem : lineItems) {
            if (lineItem.getVariantId().equals(variant.getId())) {
                if (quantity <= 0) {
                    lineItems.remove(lineItem);
                } else {
                    lineItem.setQuantity(quantity);
                }
                return;
            }
        }

        if (quantity > 0) {
            CartLineItem lineItem = new CartLineItem(variant);
            lineItem.setQuantity(quantity);
            lineItems.add(lineItem);
        }
    }

    /**
     * @return The list of {@link CartLineItem} objects in the cart. Note that these are different from {@link LineItem} objects in that they include the {@link ProductVariant}.
     */
    public List<CartLineItem> getLineItems() {
        return lineItems;
    }

    /**
     * @param lineItem The {@link LineItem} containing the {@link ProductVariant} that you would like.
     * @return The ProductVariant within the specified LineItem, or {@code null} if no associated variant was found.
     */
    public ProductVariant getProductVariant(LineItem lineItem) {
        for (ProductVariant variant : productVariants) {
            if (variant.getId().equals(lineItem.getVariantId())) {
                return variant;
            }
        }
        return null;
    }

    /**
     * @return the number of LineItems contained in this Cart
     */
    public int getSize() {
        return lineItems.size();
    }

    /**
     * @return {@code true} if the Cart has zero line items, {@code false} otherwise
     */
    public boolean isEmpty() {
        return lineItems.isEmpty();
    }

    /**
     * Removes all line items from the cart.
     */
    public void clear() {
        lineItems.clear();
    }

    public String toJsonString() {
        return BuyClientFactory.createDefaultGson().toJson(this);
    }

    public static Cart fromJson(String json) {
        return BuyClientFactory.createDefaultGson().fromJson(json, Cart.class);
    }

    /**
     * Convenience function to return the subtotal price for this cart, before taxes and shipping.
     *
     * @return The subtotal price for this cart.
     */
    public double getSubtotal() {
        double subtotal = 0;
        for (CartLineItem lineItem : lineItems) {
            subtotal += (Double.parseDouble(lineItem.getPrice()) * lineItem.getQuantity());
        }
        return subtotal;
    }

    /**
     * @return The total number of product variants in the cart (the sum of quantities across all line items).
     */
    public Integer getTotalQuantity() {
        if (CollectionUtils.isEmpty(lineItems)) {
            return 0;
        }

        int quantity = 0;
        for (LineItem lineItem : lineItems) {
            quantity += lineItem.getQuantity();
        }
        return quantity;
    }

}
