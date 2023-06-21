package com.shopify.checkout

import com.shopify.checkout.models.errors.InternalErrorPayload
import com.shopify.checkout.webView.WebViewClientEventListener

internal class AndroidWebViewEventListener(private val eventListener: ShopifyCheckoutEventListener): WebViewClientEventListener {
    override fun onError(error: InternalErrorPayload) {
        eventListener.onError(listOf(error))
    }
}
