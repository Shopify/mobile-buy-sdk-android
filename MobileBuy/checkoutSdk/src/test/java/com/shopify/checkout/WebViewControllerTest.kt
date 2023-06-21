package com.shopify.checkout

import android.webkit.ValueCallback
import com.shopify.checkout.webView.WebViewController
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.times
import org.mockito.kotlin.verify


@RunWith(MockitoJUnitRunner::class)
class WebViewControllerTest {
    @Mock
    internal lateinit var mockAndroidWebView: AndroidWebView
    private lateinit var webViewController: WebViewController

    @Before
    fun init() {
        webViewController = WebViewController()
        webViewController.webView = mockAndroidWebView
    }

    @Test
    fun `loadCheckout loads url in common webView`() {
        val testUrl = "http://www.example.com"
        val expectedUrl = "http://www.example.com"
        val captor = argumentCaptor<String>()

        webViewController.loadCheckout(testUrl)

        verify(mockAndroidWebView, times(1)).loadUrl(captor.capture())
        assertEquals(expectedUrl, captor.firstValue)
    }

    @Test
    fun `handlePayNow evaluates java script in common webView`() {
        val expectedScript = "window.MobileCheckoutSdk.dispatchMessage('submitPayment');"
        val scriptCaptor = argumentCaptor<String>()
        val resultCallbackCaptor = argumentCaptor<ValueCallback<String>>()

        webViewController.completeCheckout()

        verify(mockAndroidWebView, times(1)).evaluateJavascript(
            scriptCaptor.capture(),
            resultCallbackCaptor.capture()
        )
        assertEquals(expectedScript, scriptCaptor.firstValue)
        assertEquals(null, resultCallbackCaptor.firstValue)
    }

    @Test
    fun `handlePayNow with one-time vaulted token evaluates javascript in common webView`() {
        val expectedScript = "window.MobileCheckoutSdk.dispatchMessage('submitPayment', { detail: { sessionId: 'VAULTED-TOKEN' }});"
        val scriptCaptor = argumentCaptor<String>()
        val resultCallbackCaptor = argumentCaptor<ValueCallback<String>>()

        webViewController.completeCheckout("VAULTED-TOKEN")

        verify(mockAndroidWebView, times(1)).evaluateJavascript(
            scriptCaptor.capture(),
            resultCallbackCaptor.capture()
        )
        assertEquals(expectedScript, scriptCaptor.firstValue)
        assertEquals(null, resultCallbackCaptor.firstValue)
    }
}
