package com.shopify.checkout

import android.graphics.Bitmap
import android.widget.FrameLayout
import com.shopify.checkout.fixtures.models.CHECKOUT_DEFAULTS_FAKE
import com.shopify.checkout.webMessage.MessageParserListener
import com.shopify.checkout.webView.WebViewController
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.Shadows
import org.robolectric.shadows.ShadowWebView

@RunWith(RobolectricTestRunner::class)
class WebViewControllerWebViewSetupTest {
    private lateinit var webViewController: WebViewController

    @Before
    fun init() {
        webViewController = WebViewController()
    }

    @Test
    fun `WebViewController setup a webView with javascript and local storage enabled`() {
        val context = RuntimeEnvironment.getApplication().applicationContext
        val mockedLayout = FrameLayout(context)
        val mockEventListener = Mockito.mock(MessageParserListener::class.java)
        val webView = AndroidWebView(
            mockedLayout.context,
            listener = mockEventListener
        )

        Assert.assertEquals(false, webView.settings.javaScriptEnabled)
        Assert.assertEquals(false, webView.settings.domStorageEnabled)

        webViewController.webView = webView

        Assert.assertEquals(true, webView.settings.javaScriptEnabled)
        Assert.assertEquals(true, webView.settings.domStorageEnabled)
    }

    @Test
    fun `injects "sdkVersionNumber" and "schemaVersionNumber" on webView onPageStarted`() {
        val context = RuntimeEnvironment.getApplication().applicationContext
        val mockedLayout = FrameLayout(context)
        val mockEventListener = Mockito.mock(MessageParserListener::class.java)
        val webView = AndroidWebView(
            mockedLayout.context,
            listener = mockEventListener
        )
        val shadowWebView: ShadowWebView = Shadows.shadowOf(webView)

        webViewController = WebViewController()
        webViewController.webView = webView
        webViewController.loadCheckout("about:blank")

        shadowWebView.webViewClient.onPageStarted(
            webView,
            "about:blank",
            Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888)
        );

        Assert.assertEquals(
            shadowWebView.lastEvaluatedJavascript,
            "window.mobileCheckoutSdkVersion = \"0.0.0\"; window.mobileCheckoutSdkSchemaVersion = \"4.0\";"
        )
    }

    @Test
    fun `injects "knownBuyerIdentity" on webView onPageStarted when default options present`() {
        val context = RuntimeEnvironment.getApplication().applicationContext
        val mockedLayout = FrameLayout(context)
        val mockEventListener = Mockito.mock(MessageParserListener::class.java)
        val webView = AndroidWebView(
            mockedLayout.context,
            listener = mockEventListener
        )
        val shadowWebView: ShadowWebView = Shadows.shadowOf(webView)

        webViewController = WebViewController()
        webViewController.webView = webView
        webViewController.defaults = CHECKOUT_DEFAULTS_FAKE
        webViewController.loadCheckout("about:blank")

        shadowWebView.webViewClient.onPageStarted(
            webView,
            "about:blank",
            Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888)
        );

        Assert.assertEquals(
            shadowWebView.lastEvaluatedJavascript,
            "window.mobileCheckoutSdkVersion = \"0.0.0\"; window.mobileCheckoutSdkSchemaVersion = \"4.0\"; window.mobileCheckoutSdkIdentity = ${Json.encodeToString(
                CHECKOUT_DEFAULTS_FAKE)};"
        )
    }
}
