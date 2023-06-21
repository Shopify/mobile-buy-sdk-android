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
data class VaultedPaymentErrorPayload internal constructor(
    val flowType: String,
    val code: VaultedPaymentErrorCode,
    val reason: String? = null,
    override val group: ErrorGroup,
) : ErrorPayload {}

@Serializable(with = PaymentErrorCodeSerializer::class)
enum class VaultedPaymentErrorCode(val value: String) {
    InvalidSession("invalid_session"),
    InvalidPaymentInfo("invalid_payment_info"),
}

object PaymentErrorCodeSerializer : KSerializer<VaultedPaymentErrorCode> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("VaultedPaymentErrorCode", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: VaultedPaymentErrorCode) {
        encoder.encodeString(value.value)
    }

    override fun deserialize(decoder: Decoder): VaultedPaymentErrorCode {
        val value = decoder.decodeString()
        return VaultedPaymentErrorCode.values().firstOrNull { it.value == value }
            ?: throw IllegalArgumentException("Unknown VaultedPaymentErrorCode value")
    }
}
