package com.shopify.checkout.webView

import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import com.shopify.checkout.models.errors.InternalErrorPayload

internal interface WebViewClientEventListener {
    fun onError(error: InternalErrorPayload)
}
