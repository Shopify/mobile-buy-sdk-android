package com.shopify.checkout.models

import kotlinx.serialization.Serializable

@Serializable
data class CheckoutStatePayload(val cart: CartInfo, val buyer: BuyerInfo? = null)
