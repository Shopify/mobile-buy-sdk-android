package com.shopify.checkout

import com.shopify.checkout.fixtures.models.CHECKOUT_COMPLETED_PAYLOAD_FAKE
import com.shopify.checkout.fixtures.models.CHECKOUT_ERROR_PAYLOAD_FAKE
import com.shopify.checkout.fixtures.models.CHECKOUT_STATE_FAKE
import com.shopify.checkout.fixtures.models.DELIVERY_ERROR_PAYLOAD_FAKE
import com.shopify.checkout.fixtures.models.PAYMENT_ERROR_PAYLOAD_FAKE
import com.shopify.checkout.fixtures.models.INIT_PAYLOAD_FAKE
import com.shopify.checkout.fixtures.models.INVENTORY_ERROR_PAYLOAD_FAKE
import com.shopify.checkout.models.*
import com.shopify.checkout.models.errors.ErrorPayload
import com.shopify.checkout.webMessage.MessageParser
import com.shopify.checkout.webMessage.MessageParserListener
import com.shopify.checkout.webMessage.WebMessage
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
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
class MessageParserTest {
    @Mock
    internal lateinit var mockListener: MessageParserListener
    private lateinit var messageParser: MessageParser

    @Before
    fun init() {
        messageParser = MessageParser(mockListener)
    }

    @Test
    fun `parseMessage calls web bridge listener onStateChange when stateChange message received`() {
        val captor = argumentCaptor<CheckoutStatePayload>()
        val stringPayload = Json.encodeToString(CHECKOUT_STATE_FAKE)

        messageParser.parseMessage(WebMessage.stateChange, stringPayload)

        verify(mockListener, times(1)).onStateChange(captor.capture())
        assertEquals(CHECKOUT_STATE_FAKE, captor.firstValue)
    }

    @Test
    fun `parseMessage calls web bridge listener onError when validation payment error message received`() {
        val captor = argumentCaptor<List<ErrorPayload>>()
        val stringPayload = Json.encodeToString(listOf(PAYMENT_ERROR_PAYLOAD_FAKE))

        messageParser.parseMessage(WebMessage.error, stringPayload)

        verify(mockListener).onError(captor.capture())
        assertEquals(listOf(PAYMENT_ERROR_PAYLOAD_FAKE), captor.firstValue)
    }

    @Test
    fun `parseMessage calls web bridge listener onError when validation inventory error message received`() {
        val captor = argumentCaptor<List<ErrorPayload>>()
        val stringPayload = Json.encodeToString(listOf(INVENTORY_ERROR_PAYLOAD_FAKE))

        messageParser.parseMessage(WebMessage.error, stringPayload)

        verify(mockListener).onError(captor.capture())
        assertEquals(listOf(INVENTORY_ERROR_PAYLOAD_FAKE), captor.firstValue)
    }

    @Test
    fun `parseMessage calls web bridge listener onError when validation delivery error message received`() {
        val captor = argumentCaptor<List<ErrorPayload>>()
        val stringPayload = Json.encodeToString(listOf(DELIVERY_ERROR_PAYLOAD_FAKE))

        messageParser.parseMessage(WebMessage.error, stringPayload)

        verify(mockListener).onError(captor.capture())
        assertEquals(listOf(DELIVERY_ERROR_PAYLOAD_FAKE), captor.firstValue)
    }

    @Test
    fun `parseMessage calls web bridge listener onError when checkout error message received`() {
        val captor = argumentCaptor<List<ErrorPayload>>()
        val stringPayload = Json.encodeToString(listOf(CHECKOUT_ERROR_PAYLOAD_FAKE))

        messageParser.parseMessage(WebMessage.error, stringPayload)

        verify(mockListener).onError(captor.capture())
        assertEquals(listOf(CHECKOUT_ERROR_PAYLOAD_FAKE), captor.firstValue)
    }

    @Test
    fun `parseMessage calls web bridge listener onComplete when completed message received`() {
        val captor = argumentCaptor<CheckoutCompletedPayload>()
        val stringPayload = Json.encodeToString(CHECKOUT_COMPLETED_PAYLOAD_FAKE)

        messageParser.parseMessage(WebMessage.completed, stringPayload)

        verify(mockListener).onComplete(captor.capture())
        assertEquals(CHECKOUT_COMPLETED_PAYLOAD_FAKE, captor.firstValue)
    }

    @Test
    fun `parseMessage calls web bridge listener onINit when init message received`() {
        val captor = argumentCaptor<InitPayload>()
        val stringPayload = Json.encodeToString(INIT_PAYLOAD_FAKE)

        messageParser.parseMessage(WebMessage.init, stringPayload)

        verify(mockListener).onInit(captor.capture())
        assertEquals(INIT_PAYLOAD_FAKE, captor.firstValue)
    }
}
