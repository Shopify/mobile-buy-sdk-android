package com.shopify.checkout.models

import kotlinx.serialization.Serializable

@Serializable
data class Address(
    val referenceId: String? = null,
    val name: String? = null,
    val firstName: String? = null,
    val lastName: String? = null,
    val postalCode: String? = null,
    val address1: String? = null,
    val address2: String? = null,
    val city: String? = null,
    val countryCode: String? = null,
    val zoneCode: String? = null,
    val phone: String? = null
)
