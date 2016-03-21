package com.shopify.buy.model;

import com.google.gson.Gson;
import com.shopify.buy.dataprovider.BuyClientFactory;
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

    // Tests both serialization of attributes as a hash map, and deserialization of attributes from a hash map
    public void testAttributesSerialization() {
        Cart cart = new Cart();
        ProductVariantPrivateAPIs variant1 = new ProductVariantPrivateAPIs();
        variant1.setId(1l);
        cart.addVariant(variant1);
        Checkout checkout = new Checkout(cart);

        CheckoutAttribute attribute = new CheckoutAttribute("foo", "bar");
        checkout.getAttributes().add(attribute);

        Gson gson = BuyClientFactory.createDefaultGson();
        String json = gson.toJson(checkout);

        Checkout checkoutFromJson = gson.fromJson(json, Checkout.class);

        CheckoutAttribute returnedAttribute = checkoutFromJson.getAttributes().get(0);
        assertEquals(attribute.getName(), returnedAttribute.getName());
        assertEquals(attribute.getValue(), returnedAttribute.getValue());
    }

    // Tests deserialization of the JSON returned over the wire from the server
    public void testAttributeDeserialization() {
        String jsonString = "{\"channel\":\"mobile_app\",\"line_items\":[{\"variant\":{\"available\":false,\"grams\":0,\"position\":0,\"productId\":0,\"requires_shipping\":false,\"taxable\":false,\"id\":1},\"grams\":0,\"quantity\":1,\"requires_shipping\":false,\"taxable\":false,\"variant_id\":1}],\"attributes\":[{\"name\":\"foo\",\"value\":\"bar\"}]}";

        Gson gson = BuyClientFactory.createDefaultGson();
        Checkout checkout = gson.fromJson(jsonString, Checkout.class);

        CheckoutAttribute returnedAttribute = checkout.getAttributes().get(0);
        assertEquals("foo", returnedAttribute.getName());
        assertEquals("bar", returnedAttribute.getValue());

    }
}
