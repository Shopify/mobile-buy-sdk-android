package com.shopify.checkout

import com.shopify.checkout.models.CheckoutCompletedPayload
import com.shopify.checkout.models.CheckoutStatePayload
import com.shopify.checkout.models.InitPayload
import com.shopify.checkout.models.errors.ErrorPayload

interface ShopifyCheckoutEventListener {
    fun onStateChange(state: CheckoutStatePayload)
    fun onComplete(checkoutCompletedPayload: CheckoutCompletedPayload)
    fun onError(errors: List<ErrorPayload>)
    fun onInit(initPayload: InitPayload)
}
