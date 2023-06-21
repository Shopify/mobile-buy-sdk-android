package com.shopify.checkout.models.errors.violations

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@Serializable(with = ViolationErrorTypeSerializer::class)
enum class ViolationErrorType(val value: String) {
    Delivery("delivery"),
    Inventory("inventory")
}

object ViolationErrorTypeSerializer : KSerializer<ViolationErrorType> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("ViolationErrorType", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: ViolationErrorType) {
        encoder.encodeString(value.value)
    }

    override fun deserialize(decoder: Decoder): ViolationErrorType {
        val value = decoder.decodeString()
        return ViolationErrorType.values().firstOrNull { it.value == value }
            ?: throw IllegalArgumentException("Unknown ViolationErrorType value")
    }
}
