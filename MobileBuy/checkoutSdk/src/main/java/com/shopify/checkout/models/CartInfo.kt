package com.shopify.checkout.models

import kotlinx.serialization.Serializable

@Serializable
data class CartInfo(
    val lines: List<CartLine>,
    val price: Money? = null
)
