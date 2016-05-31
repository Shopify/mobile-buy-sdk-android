package com.shopify.buy.extensions;

import com.shopify.buy.model.GiftCard;

/**
 * Wrapper to expose private members for testing
 */
public class GiftCardPrivateAPIs extends GiftCard {

    public void setId(Long id) {
        this.id = id;
    }

    public GiftCardPrivateAPIs(String code) {
        super(code);
    }
}
