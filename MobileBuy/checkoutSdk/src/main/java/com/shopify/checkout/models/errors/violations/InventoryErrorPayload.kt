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
data class InventoryErrorPayload internal constructor(
    val flowType: String,
    val code: InventoryErrorCode,
    val reason: String? = null,
    override val group: ErrorGroup,
    val type: ViolationErrorType,
) : ErrorPayload {}

@Serializable(with = InventoryErrorCodeSerializer::class)
enum class InventoryErrorCode(val value: String) {
    InsufficientQuantity("insufficient_quantity"),
    OutOfStock("out_of_stock"),
    UnavailableProduct("unavailable_product"),
    UnpurchasableProduct("unpurchasable_product"),
}

object InventoryErrorCodeSerializer : KSerializer<InventoryErrorCode> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("InventoryErrorCode", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: InventoryErrorCode) {
        encoder.encodeString(value.value)
    }

    override fun deserialize(decoder: Decoder): InventoryErrorCode {
        val value = decoder.decodeString()
        return InventoryErrorCode.values().firstOrNull { it.value == value }
            ?: throw IllegalArgumentException("Unknown InventoryErrorCode value")
    }
}
