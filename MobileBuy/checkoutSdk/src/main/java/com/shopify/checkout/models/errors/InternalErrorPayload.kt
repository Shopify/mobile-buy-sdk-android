package com.shopify.checkout.models.errors

data class InternalErrorPayload internal constructor(
    val code: InternalErrorCode,
    val underlyingError: Exception? = null,
    val reason: String? = null
) : ErrorPayload {
    override val group: ErrorGroup = ErrorGroup.Internal
}

enum class InternalErrorCode {
    WebviewFailedToLoad,
    DecodingError,
    EncodingError,
}

enum class InternalErrorReasons(val value: String) {
    // Encoding/Decoding
    InvalidErrorType("invalid_error_type"),
    FailToParseErrorJson("fail_to_parse_error_json"),
    FailToParseMessage("fail_to_parse_message"),
    FailToEncodeBuyerIdentity("fail_to_encode_buyer_identity"),

    // Webview
    WebViewOnError("webview_on_error"),
}
