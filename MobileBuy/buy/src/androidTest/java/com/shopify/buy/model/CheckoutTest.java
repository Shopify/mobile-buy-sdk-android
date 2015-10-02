package com.shopify.buy.model;

import com.shopify.buy.extensions.ProductVariantPrivateAPIs;
import com.shopify.buy.extensions.ShopifyAndroidTestCase;

/**
 * Basic unit test for initializing a checkout and adding {@code LineItems}
 */
public class CheckoutTest extends ShopifyAndroidTestCase {

    public void testInitializeWithCart() {
        Cart cart = new Cart();

        ProductVariantPrivateAPIs variant1 = new ProductVariantPrivateAPIs();
        variant1.setId(1l);
        cart.addVariant(variant1);

        assertEquals(1, cart.getLineItems().size());
        Checkout checkout = new Checkout(cart);
        assertEquals(1, checkout.getLineItems().size());
        LineItem lineItem = checkout.getLineItems().get(0);
        assertEquals(variant1.getId(), lineItem.getVariantId());
        assertEquals(lineItem.getQuantity(), 1);

        ProductVariantPrivateAPIs variant2 = new ProductVariantPrivateAPIs();
        variant2.setId(2l);
        cart.addVariant(variant2);

        assertEquals(2, cart.getLineItems().size());
        checkout = new Checkout(cart);
        assertEquals(2, checkout.getLineItems().size());
        lineItem = checkout.getLineItems().get(1);
        assertEquals(variant2.getId(), lineItem.getVariantId());
        assertEquals(lineItem.getQuantity(), 1);

        cart.addVariant(variant1);

        assertEquals(2, cart.getLineItems().size());
        checkout = new Checkout(cart);
        assertEquals(2, checkout.getLineItems().size());
        lineItem = checkout.getLineItems().get(0);
        assertEquals(variant1.getId(), lineItem.getVariantId());
        assertEquals(lineItem.getQuantity(), 2);
    }
}
