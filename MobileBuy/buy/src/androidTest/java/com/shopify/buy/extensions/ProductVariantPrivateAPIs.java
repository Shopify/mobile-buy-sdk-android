package com.shopify.buy.extensions;

import com.shopify.buy.model.ProductVariant;

/**
 * Wrapper exposing internal API for testing
 */
public class ProductVariantPrivateAPIs extends ProductVariant {

    public void setId(Long id) { this.id = id; }
    public void setPrice(String price) { this.price = price; }
    public void setTitle(String title) { this.title = title; }

}
