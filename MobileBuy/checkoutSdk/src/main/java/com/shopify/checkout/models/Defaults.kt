package com.shopify.checkout.models

import kotlinx.serialization.Serializable

@Serializable
data class Defaults(
    val email: String? = null,
    val shippingAddresses: List<Address>? = null,
    val paymentMethods: List<PaymentMethod>? = null
)
