package com.shopify.checkout.models

import kotlinx.serialization.Serializable

@Serializable
data class CheckoutOptions(
    val defaults: Defaults? = null
)
