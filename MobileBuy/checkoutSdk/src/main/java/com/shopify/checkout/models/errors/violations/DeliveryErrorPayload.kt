package com.shopify.checkout.models.errors.violations

import com.shopify.checkout.models.errors.ErrorGroup
import com.shopify.checkout.models.errors.ErrorPayload
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@Serializable
data class DeliveryErrorPayload internal constructor(
    val flowType: String,
    val code: DeliveryErrorCode,
    val reason: String? = null,
    override val group: ErrorGroup,
    val type: ViolationErrorType,
) : ErrorPayload {}

@Serializable(with = DeliveryErrorCodeSerializer::class)
enum class DeliveryErrorCode(val value: String) {
    UnshippableProduct("unshippable_product"),
}

object DeliveryErrorCodeSerializer : KSerializer<DeliveryErrorCode> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("DeliveryErrorCode", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: DeliveryErrorCode) {
        encoder.encodeString(value.value)
    }

    override fun deserialize(decoder: Decoder): DeliveryErrorCode {
        val value = decoder.decodeString()
        return DeliveryErrorCode.values().firstOrNull { it.value == value }
            ?: throw IllegalArgumentException("Unknown DeliveryErrorCode value")
    }
}
