import android.widget.FrameLayout
import com.shopify.checkout.AndroidWebView
import com.shopify.checkout.ShopifyCheckoutManager
import com.shopify.checkout.ShopifyCheckoutEventListener
import com.shopify.checkout.models.CheckoutOptions
import com.shopify.checkout.models.Defaults
import com.shopify.checkout.webView.WebViewController
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.kotlin.verify
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment

@RunWith(RobolectricTestRunner::class)
class ShopifyCheckoutManagerTest {
    private lateinit var mockEventListeners: ShopifyCheckoutEventListener
    private lateinit var mockWebViewController: WebViewController
    private lateinit var shopifyCheckoutManager: ShopifyCheckoutManager

    @Before
    fun setup() {
        mockEventListeners = Mockito.mock(ShopifyCheckoutEventListener::class.java)
        mockWebViewController = Mockito.mock(WebViewController::class.java)
        shopifyCheckoutManager = ShopifyCheckoutManager(mockEventListeners)
        shopifyCheckoutManager.webViewController = mockWebViewController
    }

    @Test
    fun `handlePayNow triggers submitPayment js message on webView`() {
        shopifyCheckoutManager.completeCheckout()
        verify(mockWebViewController).completeCheckout()
    }

    @Test
    fun `handlePayNow with one-time vaulted token triggers submitPayment js message on webView with correct payload`() {
        shopifyCheckoutManager.completeCheckout("VAULTED-TOKEN")
        verify(mockWebViewController).completeCheckout("VAULTED-TOKEN")
    }

    @Test
    fun `presentCheckout adds a webView to the provided frameLayout and triggers webViewController loadCheckout`() {
        val context = RuntimeEnvironment.getApplication().applicationContext
        val mockedLayout = FrameLayout(context)
        val expectedUrl = "https://shopify.com"

        assertEquals(mockedLayout.childCount, 0)
        shopifyCheckoutManager.presentCheckout(expectedUrl, mockedLayout)
        assertEquals(mockedLayout.childCount, 1)
        verify(mockWebViewController).loadCheckout(expectedUrl)
        assertEquals(mockedLayout.getChildAt(0).javaClass, AndroidWebView::class.java)
    }

    @Test
    fun `presentCheckout with checkout options adds a webView to the provided frameLayout and triggers webViewController loadCheckout`() {
        val context = RuntimeEnvironment.getApplication().applicationContext
        val mockedLayout = FrameLayout(context)
        val expectedUrl = "https://shopify.com"

        assertEquals(mockedLayout.childCount, 0)
        val options = CheckoutOptions(Defaults("TEST"))
        shopifyCheckoutManager.presentCheckout(expectedUrl, mockedLayout, options)
        assertEquals(mockedLayout.childCount, 1)
        verify(mockWebViewController).loadCheckout(expectedUrl)
        assertEquals(mockedLayout.getChildAt(0).javaClass, AndroidWebView::class.java)
    }

    @Test
    fun `version returns the correct version number`() {
        assertEquals( "0.0.0", shopifyCheckoutManager.version())
    }
}