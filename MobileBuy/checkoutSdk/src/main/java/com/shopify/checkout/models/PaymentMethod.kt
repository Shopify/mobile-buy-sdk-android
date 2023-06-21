package com.shopify.checkout.models

import kotlinx.serialization.Serializable

@Serializable
data class PaymentMethod(
    val referenceId: String,
    val cardType: String,
    val lastFourDigits: String,
    val expirationMonth: String,
    val expirationYear: String,
    val billingAddress: Address
)
