package com.shopify.checkout.models

import kotlinx.serialization.Serializable

@Serializable
data class BuyerInfo(
    val email: String? = null,
    val selectedPaymentMethod: PaymentMethod? = null,
    val selectedShippingAddress: Address? = null
)
