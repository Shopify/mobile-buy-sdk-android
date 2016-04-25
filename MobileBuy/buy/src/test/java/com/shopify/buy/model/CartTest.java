package com.shopify.buy.model;

import com.shopify.buy.extensions.ProductVariantPrivateAPIs;
import com.shopify.buy.extensions.ShopifyAndroidTestCase;

import org.junit.Before;
import org.junit.Test;

import static junit.framework.TestCase.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


/**
 * Unit tests for {@code Cart}
 */

public class CartTest extends ShopifyAndroidTestCase {

    private Cart cart;

    private final Long variantId1 = 1l;
    private final Long variantId2 = 2l;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        cart = new Cart();
    }

    @Test
    public void testCartIsEmpty() {
        assert(cart.isEmpty());
    }

    @Test
    public void testCartIsNotEmpty() {
        addVariant(variantId1);
        assert(!cart.isEmpty());
    }

    @Test
    public void testClear() {
        addVariant(variantId1);
        assert(!cart.isEmpty());

        cart.clear();
        assert(cart.isEmpty());
    }

    @Test
    public void testAddVariantWillAddALineItem() {
        addVariant(variantId1);

        assertEquals(cart.getLineItems().get(0).getVariantId(), variantId1);
        assertEquals(cart.getSize(), 1);
    }

    @Test
    public void testAddingTwoDifferentVariantsWillAddDifferentLineItems() {
        addVariant(variantId1);
        addVariant(variantId2);

        assertEquals(cart.getLineItems().size(), 2);
        assertEquals(cart.getLineItems().get(0).getVariantId(), variantId1);
        assertEquals(cart.getLineItems().get(1).getVariantId(), variantId2);
        assertEquals(cart.getSize(), 2);
    }

    @Test
    public void testAddingAVariantOfTheSameTypeWillNotAddAnotherLineItem() {
        addVariant(variantId1);
        addVariant(variantId1);

        assertEquals(cart.getLineItems().size(), 1);
        assertEquals(cart.getLineItems().get(0).getVariantId(), variantId1);
        assertEquals(cart.getSize(), 1);
    }

    @Test
    public void testRemovingAVariantDecrementsQuantity() {
        addVariant(variantId1);
        addVariant(variantId1);

        decrementVariant(variantId1);

        assertEquals(cart.getLineItems().size(), 1);
        assertEquals(cart.getLineItems().get(0).getVariantId(), variantId1);
        assertEquals(cart.getSize(), 1);
    }

    @Test
    public void testRemovingAllVariantsOfASingleTypeRemovesItsLineItem() {
        addVariant(variantId1);
        decrementVariant(variantId1);

        assertEquals(cart.getLineItems().size(), 0);
        assertEquals(cart.getSize(), 0);
    }

    // This test is only here to make sure that Mockito got pulled in properly.  We are not using it for anything real yet
    @Test
    public void testUsingMockito() {
        Cart mockCart = mock(Cart.class);
        when(mockCart.isEmpty()).thenReturn(false);

        // The cart should be empty at this point, but the mocked value is false
        assertEquals(mockCart.isEmpty(), false);
    }

    // Make sure the new CartLineItem class works
    @Test
    public void testCartLineItemClass() {
        ProductVariantPrivateAPIs variant = new ProductVariantPrivateAPIs();
        variant.setId(variantId1);

        cart.addVariant(variant);

        assertEquals(cart.getSize(), 1);
        assertEquals(cart.getLineItems().size(), 1);
        assertEquals(cart.getLineItems().get(0).getVariant(), variant);
    }

    @Test
    public void testSetVariantQuantity() {
        ProductVariantPrivateAPIs variant1 = new ProductVariantPrivateAPIs();
        ProductVariantPrivateAPIs variant2 = new ProductVariantPrivateAPIs();
        variant1.setId(variantId1);
        variant2.setId(variantId2);

        cart.setVariantQuantity(variant1, 2);
        assertEquals(cart.getLineItems().size(), 1);
        assertEquals(cart.getLineItems().get(0).getQuantity(), 2);

        cart.addVariant(variant1);
        assertEquals(cart.getLineItems().get(0).getQuantity(), 3);

        cart.setVariantQuantity(variant2, 4);
        assertEquals(cart.getLineItems().size(), 2);
        assertEquals(cart.getLineItems().get(1).getQuantity(), 4);
        assertEquals(cart.getLineItems().get(0).getQuantity(), 3);

        cart.setVariantQuantity(variant1, 0);
        assertEquals(cart.getLineItems().size(), 1);
        assertEquals(cart.getLineItems().get(0).getQuantity(), 4);
    }

    /**
     * Helpers
     */
    private void addVariant(Long id) {
        ProductVariantPrivateAPIs variant = new ProductVariantPrivateAPIs();
        variant.setId(id);

        cart.addVariant(variant);
    }

    private void decrementVariant(Long id) {
        ProductVariantPrivateAPIs variant = new ProductVariantPrivateAPIs();
        variant.setId(id);

        cart.decrementVariant(variant);
    }
}
