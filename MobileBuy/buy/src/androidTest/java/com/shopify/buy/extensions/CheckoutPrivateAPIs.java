package com.shopify.buy.extensions;

import com.google.gson.Gson;
import com.shopify.buy.dataprovider.BuyClientFactory;
import com.shopify.buy.model.Cart;
import com.shopify.buy.model.Checkout;

/**
 * Wrapper used to expose private API's for testing
 */
public class CheckoutPrivateAPIs extends Checkout {

    public void setToken(String token) {
        this.token = token;
    }

    public CheckoutPrivateAPIs(Cart cart) {
        super(cart);
    }

    public static CheckoutPrivateAPIs fromCheckout(Checkout checkout) {
        Gson gson = BuyClientFactory.createDefaultGson();
        String checkoutJson = gson.toJson(checkout);
        return gson.fromJson(checkoutJson, CheckoutPrivateAPIs.class);
    }

    public void setReservationTimeLeft(Long reservationTimeLeft) {
        this.reservationTimeLeft = reservationTimeLeft;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}
