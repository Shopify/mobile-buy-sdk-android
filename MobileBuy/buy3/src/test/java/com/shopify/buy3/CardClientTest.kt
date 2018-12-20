/*
 *   The MIT License (MIT)
 *
 *   Copyright (c) 2015 Shopify Inc.
 *
 *   Permission is hereby granted, free of charge, to any person obtaining a copy
 *   of this software and associated documentation files (the "Software"), to deal
 *   in the Software without restriction, including without limitation the rights
 *   to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *   copies of the Software, and to permit persons to whom the Software is
 *   furnished to do so, subject to the following conditions:
 *
 *   The above copyright notice and this permission notice shall be included in
 *   all copies or substantial portions of the Software.
 *
 *   THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *   IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *   FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *   AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *   LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *   OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 *   THE SOFTWARE.
 */
package com.shopify.buy3

import com.google.common.truth.Truth.assertThat
import com.shopify.buy3.internal.RealCreditCardVaultCall
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.json.JSONObject
import org.junit.Test
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicReference

class CardClientTest {
    private val cardClient = CardClient()
    private val creditCard = CreditCard.mock()
    private val server = MockWebServer()

    @Test fun cardClientInstantiateWithDefaultOkHttpClient() {
        val cardClient = CardClient()
        assertThat(cardClient.httpCallFactory).isNotNull()

        val call = cardClient.vault(CreditCard.mock(), "http://google.com") as RealCreditCardVaultCall
        assertThat(call.httpCallFactory).isEqualTo(cardClient.httpCallFactory)
    }

    @Test fun cardClientInstantiateWithOkHttpClient() {
        val okHttpClient = OkHttpClient.Builder().build()

        val cardClient = CardClient(okHttpClient)
        assertThat(cardClient.httpCallFactory).isEqualTo(okHttpClient)

        val call = cardClient.vault(CreditCard.mock(), "http://google.com") as RealCreditCardVaultCall
        assertThat(call.httpCallFactory).isEqualTo(okHttpClient)
    }

    @Test fun creditCardPrecondition() {
        checkForIllegalArgumentException { CreditCard.mock(firstName = "") }
        checkForIllegalArgumentException { CreditCard.mock(lastName = "") }
        checkForIllegalArgumentException { CreditCard.mock(number = "") }
        checkForIllegalArgumentException { CreditCard.mock(expireMonth = "") }
        checkForIllegalArgumentException { CreditCard.mock(expireYear = "") }
        checkForIllegalArgumentException { CreditCard.mock(verificationCode = "") }
    }

    @Test fun realCardVaultCallInstantiatedCorrectly() {
        val call = cardClient.vault(creditCard, "http://google.com") as RealCreditCardVaultCall
        assertThat(call.creditCard).isEqualTo(creditCard)
        assertThat(call.endpointUrl.toString()).isEqualTo("http://google.com/")
        assertThat(call.httpCallFactory).isEqualTo(cardClient.httpCallFactory)
    }

    @Test fun realCardVaultCallClone() {
        val call = cardClient.vault(creditCard, "http://google.com") as RealCreditCardVaultCall
        val clonedCall = call.clone() as RealCreditCardVaultCall

        assertThat(clonedCall).isNotEqualTo(call)
        assertThat(clonedCall.creditCard).isEqualTo(call.creditCard)
        assertThat(clonedCall.endpointUrl).isEqualTo(call.endpointUrl)
        assertThat(clonedCall.httpCallFactory).isEqualTo(call.httpCallFactory)
    }

//    @Test fun vaultCreditCardSuccess() {
//        server.enqueue(MockResponse().setResponseCode(200).setBody("{\"id\": \"83hru3obno3hu434b3u\"}"))
//
//        val token = cardClient.vault(creditCard, server.url("/").toString()).fetchVaultToken()
//
//        val recordedRequest = server.takeRequest()
//        assertThat(recordedRequest.getHeader("Accept")).isEqualTo("application/json")
//        assertThat(recordedRequest.getHeader("Content-Type")).isEqualTo("application/json; charset=utf-8")
//
//        val jsonObject = JSONObject(recordedRequest.body.readUtf8())
//        assertThat(jsonObject).isEqualTo(creditCard.asJson())
//        assertThat(token).isEqualTo("83hru3obno3hu434b3u")
//    }
//
//    @Test
//    fun vaultCreditCardFail() {
//        server.enqueue(MockResponse().setResponseCode(400))
//        server.enqueue(MockResponse().setResponseCode(200).setBody(""))
//
//        var result = cardClient.vault(creditCard, server.url("/").toString()).fetchVaultToken()
//        assertThat(result).isInstanceOf(CardVaultResult.Failure::class.java)
//
//        result = cardClient.vault(creditCard, server.url("/").toString()).fetchVaultToken()
//        assertThat(result).isInstanceOf(CardVaultResult.Failure::class.java)
//    }
}

private fun CreditCard.Companion.mock(
    firstName: String = "John",
    lastName: String = "Smith",
    number: String = "1",
    expireMonth: String = "06",
    expireYear: String = "2017",
    verificationCode: String = "111"
) = CreditCard(
    firstName = firstName,
    lastName = lastName,
    number = number,
    expireMonth = expireMonth,
    expireYear = expireYear,
    verificationCode = verificationCode
)

private fun CreditCard.asJson() = JSONObject().put(
    "credit_card", JSONObject()
        .put("number", number)
        .put("first_name", firstName)
        .put("last_name", lastName)
        .put("month", expireMonth)
        .put("year", expireYear)
        .put("verification_value", verificationCode)
)

private fun CreditCardVaultCall.fetchVaultToken(): CardVaultResult {
    val latch = NamedCountDownLatch("get CreditCardVaultCall result", 1)
    val result = AtomicReference<CardVaultResult>()
    enqueue {
        result.set(it)
        latch.countDown()
    }
    latch.awaitOrThrowWithTimeout(2, TimeUnit.SECONDS)
    return result.get()!!
}