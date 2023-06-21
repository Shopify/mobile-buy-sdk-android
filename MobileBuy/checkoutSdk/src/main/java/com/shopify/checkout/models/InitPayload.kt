package com.shopify.checkout.models

import kotlinx.serialization.Serializable

@Serializable
data class InitPayload(
    val paymentUrl: String,
)
