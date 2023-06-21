package com.shopify.checkout.models.errors

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@Serializable(with = ErrorGroupSerializer::class)
enum class ErrorGroup(val value: String) {
    Violation("violation"),
    Checkout("checkout"),
    Internal("internal"),
    VaultedPayment("vaulted_payment")
}

object ErrorGroupSerializer : KSerializer<ErrorGroup> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("ErrorGroup", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: ErrorGroup) {
        encoder.encodeString(value.value)
    }

    override fun deserialize(decoder: Decoder): ErrorGroup {
        val value = decoder.decodeString()
        return ErrorGroup.values().firstOrNull { it.value == value }
            ?: throw IllegalArgumentException("Unknown ErrorGroup value")
    }
}
