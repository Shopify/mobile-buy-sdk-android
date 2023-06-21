package com.shopify.checkout

import android.content.Context
import android.view.MotionEvent
import android.webkit.JavascriptInterface
import android.webkit.WebView
import com.shopify.checkout.webMessage.MessageParser
import com.shopify.checkout.webMessage.MessageParserListener
import com.shopify.checkout.webMessage.WebMessage
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

internal class AndroidWebView(
    context: Context,
    listener: MessageParserListener,
) : WebView(context) {
    private val messageParser: MessageParser = MessageParser(listener)

    init {
        addJavascriptInterface(this, "android")
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        requestDisallowInterceptTouchEvent(true)
        return super.onTouchEvent(event)
    }

    @JavascriptInterface
    fun postMessage(message: String) {
        val decoder = Json { ignoreUnknownKeys = true }
        val jsMessage = decoder.decodeFromString<JSMessage>(message)
        val parsedMessage = WebMessage.valueOf(jsMessage.name)

        messageParser.parseMessage(message = parsedMessage, payload = jsMessage.body)
    }

    @Serializable
    private data class JSMessage(val name: String, val body: String)
}
