/*
 *   The MIT License (MIT)
 *
 *   Copyright (c) 2015 Shopify Inc.
 *
 *   Permission is hereby granted, free of charge, to any person obtaining a copy
 *   of this software and associated documentation files (the "Software"), to deal
 *   in the Software without restriction, including without limitation the rights
 *   to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *   copies of the Software, and to permit persons to whom the Software is
 *   furnished to do so, subject to the following conditions:
 *
 *   The above copyright notice and this permission notice shall be included in
 *   all copies or substantial portions of the Software.
 *
 *   THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *   IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *   FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *   AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *   LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *   OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 *   THE SOFTWARE.
 */
package com.shopify.buy.dataprovider;

import com.shopify.buy.model.Checkout;
import com.shopify.buy.model.CreditCard;
import com.shopify.buy.model.GiftCard;
import com.shopify.buy.model.ShippingRate;

import java.util.List;

import rx.Observable;

public interface CheckoutService {

    void createCheckout(Checkout checkout, Callback<Checkout> callback);

    Observable<Checkout> createCheckout(Checkout checkout);

    void updateCheckout(Checkout checkout, Callback<Checkout> callback);

    Observable<Checkout> updateCheckout(Checkout checkout);

    void completeCheckout(Checkout checkout, Callback<Checkout> callback);

    Observable<Checkout> completeCheckout(Checkout checkout);

    void getCheckoutCompletionStatus(Checkout checkout, Callback<Boolean> callback);

    Observable<Boolean> getCheckoutCompletionStatus(Checkout checkout);

    void getCheckout(String checkoutToken, Callback<Checkout> callback);

    Observable<Checkout> getCheckout(String checkoutToken);

    void getShippingRates(String checkoutToken, Callback<List<ShippingRate>> callback);

    Observable<List<ShippingRate>> getShippingRates(String checkoutToken);

    void storeCreditCard(CreditCard card, Checkout checkout, Callback<Checkout> callback);

    Observable<Checkout> storeCreditCard(CreditCard card, Checkout checkout);

    void applyGiftCard(String giftCardCode, Checkout checkout, Callback<Checkout> callback);

    Observable<Checkout> applyGiftCard(String giftCardCode, Checkout checkout);

    void removeGiftCard(GiftCard giftCard, Checkout checkout, Callback<Checkout> callback);

    Observable<Checkout> removeGiftCard(GiftCard giftCard, Checkout checkout);

    void removeProductReservationsFromCheckout(Checkout checkout, Callback<Checkout> callback);

    Observable<Checkout> removeProductReservationsFromCheckout(Checkout checkout);
}
