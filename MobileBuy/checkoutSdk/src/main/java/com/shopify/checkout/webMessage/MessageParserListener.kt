package com.shopify.checkout.webMessage

import com.shopify.checkout.ShopifyCheckoutEventListener
import com.shopify.checkout.models.CheckoutCompletedPayload
import com.shopify.checkout.models.CheckoutStatePayload
import com.shopify.checkout.models.InitPayload
import com.shopify.checkout.models.errors.ErrorPayload

internal class MessageParserListener(private val eventListeners: ShopifyCheckoutEventListener) {
    fun onStateChange(state: CheckoutStatePayload) {
        eventListeners.onStateChange(state)
    }

    fun onComplete(completionPayload: CheckoutCompletedPayload) {
        eventListeners.onComplete(completionPayload)
    }

    fun onError(errors: List<ErrorPayload>) {
        eventListeners.onError(errors)
    }

    fun onInit(initPayload: InitPayload) {
        eventListeners.onInit(initPayload)
    }
}
