package com.shopify.checkout.webMessage

import com.shopify.checkout.models.CheckoutCompletedPayload
import com.shopify.checkout.models.CheckoutStatePayload
import com.shopify.checkout.models.InitPayload
import com.shopify.checkout.models.errors.*
import com.shopify.checkout.models.errors.violations.DeliveryErrorPayload
import com.shopify.checkout.models.errors.violations.InventoryErrorPayload
import com.shopify.checkout.models.errors.violations.VaultedPaymentErrorPayload
import com.shopify.checkout.models.errors.violations.ViolationErrorType
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.*

internal interface MessageParserInterface {
    val listener: MessageParserListener
    fun parseMessage(message: WebMessage, payload: String)
}

internal class MessageParser(
    override val listener: MessageParserListener,
) :
    MessageParserInterface {
    override fun parseMessage(message: WebMessage, payload: String) {
        try {
            when (message) {
                WebMessage.stateChange -> {
                    val parsedPayload = parsePayload<CheckoutStatePayload>(payload)
                    parsedPayload.let {
                        listener.onStateChange(parsedPayload)
                    }
                }

                WebMessage.completed -> {
                    val parsedPayload = parsePayload<CheckoutCompletedPayload>(payload)
                    parsedPayload.let {
                        listener.onComplete(parsedPayload)
                    }
                }

                WebMessage.error -> {
                    val errors = parseErrorPayload(payload)
                    errors.let {
                        listener.onError(errors)
                    }
                }

                WebMessage.init -> {
                    val parsedPayload = parsePayload<InitPayload>(payload)
                    parsedPayload.let {
                        listener.onInit(parsedPayload)
                    }
                }
            }
        } catch (e: Exception) {
            listener.onError(
                listOf(InternalErrorPayload(InternalErrorCode.DecodingError, e, InternalErrorReasons.FailToParseMessage.value))
            )
        }
    }

    private fun parseErrorPayload(payload: String): List<ErrorPayload> {
        val errorPayloadArray = parsePayload<JsonArray>(payload)
        return errorPayloadArray.map { jsonElement ->
            try {
                val groupString = jsonElement.jsonObject.getValue("group").jsonPrimitive.content
                val group = ErrorGroup.values().firstOrNull { it.value == groupString }
                    ?: throw IllegalArgumentException("${InternalErrorReasons.InvalidErrorType} $groupString" )
                when (group) {
                    ErrorGroup.Checkout -> parsePayload<CheckoutErrorPayload>(jsonElement)
                    ErrorGroup.Violation -> parseViolationError(jsonElement)
                    ErrorGroup.VaultedPayment -> parsePayload<VaultedPaymentErrorPayload>(jsonElement)
                    ErrorGroup.Internal -> throw IllegalArgumentException("Internal errors should not be decoded")
                }
            } catch (e: Exception) {
                InternalErrorPayload(
                    InternalErrorCode.DecodingError, e, InternalErrorReasons.FailToParseErrorJson.value
                )
            }
        }
    }

    private fun parseViolationError(violationJson: JsonElement): ErrorPayload {
        val typeString = violationJson.jsonObject.getValue("type").jsonPrimitive.content
        val type = ViolationErrorType.values().firstOrNull { it.value == typeString }
            ?: throw IllegalArgumentException("Unknown type value: $typeString")

        return when (type) {
            ViolationErrorType.Inventory -> parsePayload<InventoryErrorPayload>(violationJson)
            ViolationErrorType.Delivery -> parsePayload<DeliveryErrorPayload>(violationJson)
        }
    }

    private inline fun <reified T> parsePayload(payload: String): T {
        val decoder = Json { ignoreUnknownKeys = true }
        return decoder.decodeFromString<T>(payload)
    }

    private inline fun <reified T> parsePayload(payload: JsonElement): T {
        val decoder = Json { ignoreUnknownKeys = true }
        return decoder.decodeFromJsonElement<T>(payload)
    }
}
