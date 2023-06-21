package com.shopify.checkout.webView

import android.graphics.Bitmap
import android.os.Build
import android.webkit.*
import com.shopify.checkout.BuildConfig
import com.shopify.checkout.models.Defaults
import com.shopify.checkout.models.errors.InternalErrorCode
import com.shopify.checkout.models.errors.InternalErrorPayload
import com.shopify.checkout.models.errors.InternalErrorReasons
import com.shopify.checkout.models.errors.WebviewException
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.util.logging.Level
import java.util.logging.Logger

internal class WebViewController :
    WebViewClient() {
    internal var webView: WebView? = null
        set(value) {
            field = value
            setupWebView()
        }
    internal var defaults: Defaults? = null
    internal var webViewClientEventListener: WebViewClientEventListener? = null

    internal var logger = Logger.getLogger("stuff")

    private fun setupWebView() {
        // Enable WebView debug with chrome
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && BuildConfig.DEBUG) {
            WebView.setWebContentsDebuggingEnabled(true)
        }

        webView?.settings?.javaScriptEnabled = true
        webView?.settings?.domStorageEnabled = true
        webView?.webViewClient = this
    }

    override fun onReceivedError(
        view: WebView?,
        request: WebResourceRequest?,
        error: WebResourceError?
    ) {
        super.onReceivedError(view, request, error)
        if (request?.isForMainFrame == true) {
            handleInternalError(WebviewException(request, error, null), InternalErrorCode.WebviewFailedToLoad, InternalErrorReasons.WebViewOnError)
        }
    }

    override fun onReceivedHttpError(
        view: WebView?,
        request: WebResourceRequest?,
        errorResponse: WebResourceResponse?
    ) {
        super.onReceivedHttpError(view, request, errorResponse)
        if (request?.isForMainFrame == true) {
            handleInternalError(WebviewException(request, null, errorResponse), InternalErrorCode.WebviewFailedToLoad, InternalErrorReasons.WebViewOnError)
        }

    }

    private fun onPageStartedSetup() {
        injectSdkConfigs()
    }

    private fun injectSdkConfigs() {
        var script =
            arrayListOf(
                ScriptBuilder.mobileCheckoutSdkVersion(WebViewConfig.sdkVersionNumber),
                ScriptBuilder.mobileCheckoutSdkSchemaVersion(WebViewConfig.schemaVersionNumber)
            )

        defaults?.let {
            try {
                script.add(ScriptBuilder.mobileCheckoutSdkKnownBuyerIdentity(it))
            } catch (e: Exception) {
                handleInternalError(e, InternalErrorCode.EncodingError, InternalErrorReasons.FailToEncodeBuyerIdentity)
            }
        }
        webView?.evaluateJavascript(script.joinToString(" "), null)
    }

    private fun handleInternalError(e: Exception, code: InternalErrorCode, reason: InternalErrorReasons) {
        val internalError = InternalErrorPayload(code, e, reason.value)
        webViewClientEventListener?.onError(internalError)
    }

    fun loadCheckout(rawUrl: String) {
        webView?.loadUrl(rawUrl)
    }

    fun completeCheckout(cardServerSessionId: String? = null) {
        if (cardServerSessionId != null) {
            webView?.evaluateJavascript(ScriptBuilder.submitPayment(cardServerSessionId), null)
            return
        }
        webView?.evaluateJavascript(ScriptBuilder.submitPayment(), null)
    }

    fun updateCart(lineId: String, quantity: Int) {
        webView?.evaluateJavascript(ScriptBuilder.updateCart(lineId, quantity), null)
    }

    override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
        onPageStartedSetup()
        super.onPageStarted(view, url, favicon)
    }
}

internal object ScriptBuilder {
    fun mobileCheckoutSdkVersion(versionNumber: String): String {
        return "window.mobileCheckoutSdkVersion = \"${versionNumber}\";"
    }

    fun mobileCheckoutSdkSchemaVersion(schemaVersionNumber: String): String {
        return "window.mobileCheckoutSdkSchemaVersion = \"${schemaVersionNumber}\";"
    }

    fun submitPayment(): String {
        return "window.MobileCheckoutSdk.dispatchMessage('submitPayment');"
    }

    fun submitPayment(cardServerSessionId: String): String {
        return "window.MobileCheckoutSdk.dispatchMessage('submitPayment', { detail: { sessionId: '$cardServerSessionId' }});"
    }

    fun updateCart(lineId: String, quantity: Int): String {
        return "window.MobileCheckoutSdk.dispatchMessage('updateCart', { detail: { lineId: '$lineId', quantity: $quantity }});"
    }

    fun mobileCheckoutSdkKnownBuyerIdentity(defaults: Defaults): String {
        val defaultsAsJson = Json.encodeToString(defaults)
        return "window.mobileCheckoutSdkIdentity = $defaultsAsJson;"
    }
}

