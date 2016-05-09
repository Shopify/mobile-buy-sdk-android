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

/**
 * Service that provides Checkout API endpoints.
 */
public interface CheckoutService {

    String getWebReturnToUrl();

    String getWebReturnToLabel();

    /**
     * Initiate the Shopify checkout process with a new Checkout object.
     *
     * @param checkout the {@link Checkout} object to use for initiating the checkout process
     * @param callback the {@link Callback} that will be used to indicate the response from the asynchronous network operation, not null
     */
    void createCheckout(Checkout checkout, Callback<Checkout> callback);

    /**
     * Initiate the Shopify checkout process with a new Checkout object.
     *
     * @param checkout the {@link Checkout} object to use for initiating the checkout process
     * @return cold observable that emits created checkout object
     */
    Observable<Checkout> createCheckout(Checkout checkout);

    /**
     * Update an existing Checkout's attributes
     *
     * @param checkout the {@link Checkout} to update
     * @param callback the {@link Callback} that will be used to indicate the response from the asynchronous network operation, not null
     */
    void updateCheckout(Checkout checkout, Callback<Checkout> callback);

    /**
     * Update an existing Checkout's attributes
     *
     * @param checkout the {@link Checkout} to update
     * @return cold observable that emits updated checkout object
     */
    Observable<Checkout> updateCheckout(Checkout checkout);

    /**
     * Complete the checkout and process the payment session
     *
     * @param checkout a {@link Checkout} that has had a {@link CreditCard} associated with it using {@link #storeCreditCard(CreditCard, Checkout)}
     * @param callback the {@link Callback} that will be used to indicate the response from the asynchronous network operation, not null
     */
    void completeCheckout(Checkout checkout, Callback<Checkout> callback);

    /**
     * Complete the checkout and process the payment session
     *
     * @param checkout a {@link Checkout} that has had a {@link CreditCard} associated with it using {@link #storeCreditCard(CreditCard, Checkout)}
     * @return cold observable that emits completed checkout
     */
    Observable<Checkout> completeCheckout(Checkout checkout);

    /**
     * Get the status of the payment session associated with {@code checkout}. {@code callback} will be
     * called with a boolean value indicating whether the session has completed or not.
     *
     * @param checkout a {@link Checkout} that has been passed as a parameter to {@link #completeCheckout(Checkout, Callback)} or {@link #completeCheckout(Checkout)}
     * @param callback the {@link Callback} that will be used to indicate the response from the asynchronous network operation, not null
     *
     */
    void getCheckoutCompletionStatus(Checkout checkout, final Callback<Boolean> callback);

    /**
     * Get the status of the payment session associated with {@code checkout}. {@code callback} will be
     * called with a boolean value indicating whether the session has completed or not.
     *
     * @param checkout a {@link Checkout} that has been passed as a parameter to {@link #completeCheckout(Checkout, Callback)} or {@link #completeCheckout(Checkout)}
     * @return cold observable that emits a Boolean that indicates whether the checkout has been completed
     *
     */
    Observable<Boolean> getCheckoutCompletionStatus(Checkout checkout);

    /**
     * Fetch an existing Checkout from Shopify
     *
     * @param checkoutToken the token associated with the existing {@link Checkout}
     * @param callback      the {@link Callback} that will be used to indicate the response from the asynchronous network operation, not null
     */
    void getCheckout(String checkoutToken, Callback<Checkout> callback);

    /**
     * Fetch an existing Checkout from Shopify
     *
     * @param checkoutToken the token associated with the existing {@link Checkout}
     * @return cold observable that emits requested existing checkout
     */
    Observable<Checkout> getCheckout(String checkoutToken);

    /**
     * Fetch shipping rates for a given Checkout
     *
     * @param checkoutToken the {@link Checkout#token} from an existing Checkout
     * @param callback      the {@link Callback} that will be used to indicate the response from the asynchronous network operation, not null
     */
    void getShippingRates(String checkoutToken, Callback<List<ShippingRate>> callback);

    /**
     * Fetch shipping rates for a given Checkout
     *
     * @param checkoutToken the {@link Checkout#token} from an existing Checkout
     * @return cold observable that emits requested list of shipping rates for a given checkout
     */
    Observable<List<ShippingRate>> getShippingRates(String checkoutToken);

    /**
     * Post a credit card to Shopify's card server and associate it with a Checkout
     *
     * @param card     the {@link CreditCard} to associate
     * @param checkout the {@link Checkout} to associate the card with
     * @param callback the {@link Callback} that will be used to indicate the response from the asynchronous network operation, not null
     */
    void storeCreditCard(CreditCard card, Checkout checkout, Callback<Checkout> callback);

    /**
     * Post a credit card to Shopify's card server and associate it with a Checkout
     *
     * @param card     the {@link CreditCard} to associate
     * @param checkout the {@link Checkout} to associate the card with
     * @return cold observable that emits updated checkout with posted credit card
     */
    Observable<Checkout> storeCreditCard(CreditCard card, Checkout checkout);

    /**
     * Apply a gift card to a Checkout
     *
     * @param giftCardCode the gift card code for a gift card associated with the current Shop
     * @param checkout     the {@link Checkout} object to apply the gift card to
     * @param callback     the {@link Callback} that will be used to indicate the response from the asynchronous network operation, not null
     */
    void applyGiftCard(String giftCardCode, Checkout checkout, Callback<Checkout> callback);

    /**
     * Apply a gift card to a Checkout
     *
     * @param giftCardCode the gift card code for a gift card associated with the current Shop
     * @param checkout     the {@link Checkout} object to apply the gift card to
     * @return cold observable that emits updated checkout
     */
    Observable<Checkout> applyGiftCard(String giftCardCode, Checkout checkout);

    /**
     * Remove a gift card that was previously applied to a Checkout
     *
     * @param giftCard the {@link GiftCard} to remove from the {@link Checkout}
     * @param checkout the {@code Checkout} to remove the {@code GiftCard} from
     * @param callback the {@link Callback} that will be used to indicate the response from the asynchronous network operation, not null
     */
    void removeGiftCard(GiftCard giftCard, Checkout checkout, Callback<Checkout> callback);

    /**
     * Remove a gift card that was previously applied to a Checkout
     *
     * @param giftCard the {@link GiftCard} to remove from the {@link Checkout}
     * @param checkout the {@code Checkout} to remove the {@code GiftCard} from
     * @return cold observable that emits updated checkout
     */
    Observable<Checkout> removeGiftCard(GiftCard giftCard, Checkout checkout);

    /**
     * Convenience method to release all product inventory reservations by setting the `reservationTime` of the checkout `0` and calling {@link #updateCheckout(Checkout, Callback) updateCheckout(Checkout, Callback)}.
     * We recommend creating a new `Checkout` object from a `Cart` for further API calls.
     *
     * @param checkout the {@link Checkout} to expire
     * @param callback the {@link Callback} that will be used to indicate the response from the asynchronous network operation, not null
     */
    void removeProductReservationsFromCheckout(Checkout checkout, Callback<Checkout> callback);

    /**
     * Convenience method to release all product inventory reservations by setting the `reservationTime` of the checkout `0` and calling {@link #updateCheckout(Checkout, Callback) updateCheckout(Checkout, Callback)}.
     * We recommend creating a new `Checkout` object from a `Cart` for further API calls.
     *
     * @param checkout the {@link Checkout} to expire
     * @return cold observable that emits updated checkout
     */
    Observable<Checkout> removeProductReservationsFromCheckout(Checkout checkout);
}
