package com.shopify.checkout.models

import kotlinx.serialization.Serializable

@Serializable
data class CartLineImage(
    val sm: String,
    val md: String,
    val lg: String,
    val altText: String? = null
)

@Serializable
data class CartLine(
    val merchandiseId: String? = null,
    val productId: String? = null,
    val image: CartLineImage? = null,
    val quantity: Int,
    val title: String,
    val price: Money
)
