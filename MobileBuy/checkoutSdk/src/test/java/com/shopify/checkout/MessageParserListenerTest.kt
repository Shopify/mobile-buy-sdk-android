package com.shopify.checkout

import com.shopify.checkout.fixtures.models.CHECKOUT_COMPLETED_PAYLOAD_FAKE
import com.shopify.checkout.fixtures.models.CHECKOUT_STATE_FAKE
import com.shopify.checkout.fixtures.models.PAYMENT_ERROR_PAYLOAD_FAKE
import com.shopify.checkout.fixtures.models.INIT_PAYLOAD_FAKE
import com.shopify.checkout.models.*
import com.shopify.checkout.models.errors.ErrorPayload
import com.shopify.checkout.webMessage.MessageParserListener
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
class MessageParserListenerTest {
    @Mock
    private lateinit var mockEventListener: ShopifyCheckoutEventListener
    private lateinit var messageParserListener: MessageParserListener

    @Before
    fun init() {
        messageParserListener = MessageParserListener(mockEventListener)
    }

    @Test
    fun `onStateChange calls listener onStateChange `() {
        messageParserListener.onStateChange(CHECKOUT_STATE_FAKE)

        val captor = argumentCaptor<CheckoutStatePayload>()

        verify(mockEventListener, times(1)).onStateChange(captor.capture())
        assertEquals(CHECKOUT_STATE_FAKE, captor.firstValue)
    }

    @Test
    fun `onError calls listener onError `() {
        val captor = argumentCaptor<List<ErrorPayload>>()

        messageParserListener.onError(listOf(PAYMENT_ERROR_PAYLOAD_FAKE))

        verify(mockEventListener, times(1)).onError(captor.capture())
        assertEquals(listOf(PAYMENT_ERROR_PAYLOAD_FAKE), captor.firstValue)
    }

    @Test
    fun `onComplete calls listener onComplete `() {
        val captor = argumentCaptor<CheckoutCompletedPayload>()

        messageParserListener.onComplete(CHECKOUT_COMPLETED_PAYLOAD_FAKE)

        verify(mockEventListener, times(1)).onComplete(captor.capture())
        assertEquals(CHECKOUT_COMPLETED_PAYLOAD_FAKE, captor.firstValue)
    }

    @Test
    fun `onInit calls listener onInit`() {
        val captor = argumentCaptor<InitPayload>()

        messageParserListener.onInit(INIT_PAYLOAD_FAKE)

        verify(mockEventListener, times(1)).onInit(captor.capture())
        assertEquals(INIT_PAYLOAD_FAKE, captor.firstValue)
    }
}