package com.shopify.checkout.models

import kotlinx.serialization.Serializable

@Serializable
data class Money(val amount: Float, val currencyCode: String)
