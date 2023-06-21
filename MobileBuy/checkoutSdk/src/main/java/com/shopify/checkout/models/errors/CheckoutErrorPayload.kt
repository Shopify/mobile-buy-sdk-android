package com.shopify.checkout.models.errors

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@Serializable
data class CheckoutErrorPayload internal constructor(
    val flowType: String,
    val type: CheckoutErrorType,
    override val group: ErrorGroup
): ErrorPayload {}

@Serializable(with = CheckoutErrorTypeSerializer::class)
enum class CheckoutErrorType(val value: String) {
    Inventory("inventory"),
    Payment("payment"),
    Other("other"),
    Discount("discount"),
    Order("order"),
    CustomerPersistence("customer_persistence"),

}

object CheckoutErrorTypeSerializer : KSerializer<CheckoutErrorType> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("CheckoutErrorType", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: CheckoutErrorType) {
        encoder.encodeString(value.value)
    }

    override fun deserialize(decoder: Decoder): CheckoutErrorType {
        val value = decoder.decodeString()
        return CheckoutErrorType.values().firstOrNull { it.value == value }
            ?: throw IllegalArgumentException("Unknown CheckoutErrorType value")
    }
}
