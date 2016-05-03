package com.shopify.buy.extensions;

import com.shopify.buy.model.Shop;

/**
 * Wrapper to expose private members for testing
 */

public class ShopPrivateAPIs extends Shop {

    public void setCurrency(String currency) {
        this.currency = currency;
    }

}
