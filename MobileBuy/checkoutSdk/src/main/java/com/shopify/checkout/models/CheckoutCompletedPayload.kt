package com.shopify.checkout.models

import kotlinx.serialization.Serializable

@Serializable
data class CheckoutCompletedPayload(
    val flowType: String,
    val orderId: String,
    val cart: CartInfo,
    val thankYouPageUrl: String,
)
