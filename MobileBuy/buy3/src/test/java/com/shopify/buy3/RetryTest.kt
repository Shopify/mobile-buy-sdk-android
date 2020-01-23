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

import android.content.Context
import com.google.common.truth.Truth.assertThat
import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.whenever
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.intellij.lang.annotations.Language
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicReference

private const val PACKAGE_NAME = "com.shopify.buy3.test"

@RunWith(MockitoJUnitRunner::class)
class RetryTest {
    @Mock lateinit var mockContext: Context
    private val server = MockWebServer()
    private lateinit var graphClient: GraphClient
    private val shopNameQuery = Storefront.query { root -> root.shop { it.name() } }
    @Language("json") private val shopNameResponse1 = """
            {
              "data": {
                "shop" : {
                  "name" : "shopName1"
                }
              }
            }

        """.trimIndent()
    @Language("json") private val shopNameResponse2 = """
            {
              "data": {
                "shop" : {
                  "name" : "shopName2"
                }
              }
            }

        """.trimIndent()

    @Before fun setUp() {
        doReturn(PACKAGE_NAME).whenever(mockContext).getPackageName()
        graphClient = GraphClient.build(context = mockContext, shopDomain = "shopDomain", accessToken = "accessToken", configure = {
            endpointUrl = server.url("/")
        })
    }

    @Test
    fun retryNoConditionNetworkError() {
        server.enqueue(MockResponse().setResponseCode(500).setBody(""))
        server.enqueue(MockResponse().setResponseCode(500).setBody(""))
        server.enqueue(MockResponse().setResponseCode(500).setBody(""))
        server.enqueue(MockResponse().setResponseCode(500).setBody(""))
        server.enqueue(MockResponse().setResponseCode(500).setBody(""))

        val result = graphClient.retryAndGet(
            retryHandler = RetryHandler.build(100, TimeUnit.MILLISECONDS) { maxAttempts(3) },
            timeout = 5,
            timeUnit = TimeUnit.SECONDS
        )

        assertThat(result).isNotNull()
        assertThat(result).isInstanceOf(GraphCallResult.Failure::class.java)
        assertThat((result as GraphCallResult.Failure).error).isInstanceOf(GraphError.HttpError::class.java)
        assertThat(server.requestCount).isEqualTo(4)
    }

    @Test
    fun retryWihConditionNetworkError() {
        server.enqueue(MockResponse().setResponseCode(500).setBody(""))
        server.enqueue(MockResponse().setResponseCode(500).setBody(""))
        server.enqueue(MockResponse().setResponseCode(500).setBody(""))
        server.enqueue(MockResponse().setResponseCode(200).setBody(""))
        server.enqueue(MockResponse().setResponseCode(500).setBody(""))

        val result = graphClient.retryAndGet(
            retryHandler = RetryHandler.build(100, TimeUnit.MILLISECONDS) {
                maxAttempts(4)
                retryWhen { it is GraphCallResult.Failure && it.error is GraphError.HttpError }
            },
            timeout = 5,
            timeUnit = TimeUnit.SECONDS
        )

        assertThat(result).isNotNull()
        assertThat(result).isInstanceOf(GraphCallResult.Failure::class.java)
        assertThat((result as GraphCallResult.Failure).error).isInstanceOf(GraphError.ParseError::class.java)
        assertThat(server.requestCount).isEqualTo(4)
    }

    @Test
    fun retryNoConditionSuccess() {
        server.enqueue(MockResponse().setResponseCode(500).setBody(""))
        server.enqueue(MockResponse().setResponseCode(500).setBody(""))
        server.enqueue(MockResponse().setResponseCode(200).setBody(shopNameResponse1))

        val result = graphClient.retryAndGet(
            retryHandler = RetryHandler.build(100, TimeUnit.MILLISECONDS) { maxAttempts(3) },
            timeout = 5,
            timeUnit = TimeUnit.SECONDS
        )

        assertThat(result).isInstanceOf(GraphCallResult.Success::class.java)
        assertThat((result as GraphCallResult.Success).response.data!!.shop.name).isEqualTo("shopName1")
    }

    @Test
    fun retryWithConditionSuccess() {
        server.enqueue(MockResponse().setResponseCode(500).setBody(""))
        server.enqueue(MockResponse().setResponseCode(200).setBody(shopNameResponse1))
        server.enqueue(MockResponse().setResponseCode(500).setBody(""))
        server.enqueue(MockResponse().setResponseCode(200).setBody(shopNameResponse2))

        val result = graphClient.retryAndGet(
            retryHandler = RetryHandler.build(100, TimeUnit.MILLISECONDS) {
                maxAttempts(3)
                retryWhen {
                    it is GraphCallResult.Failure || (it is GraphCallResult.Success && it.response.data!!.shop.name == "shopName1")
                }
            },
            timeout = 5,
            timeUnit = TimeUnit.SECONDS
        )

        assertThat(result).isInstanceOf(GraphCallResult.Success::class.java)
        assertThat((result as GraphCallResult.Success).response.data!!.shop.name).isEqualTo("shopName2")
    }

    @Test
    fun retryAndCancel() {
        server.enqueue(MockResponse().setResponseCode(500).setBody(""))
        server.enqueue(MockResponse().setResponseCode(500).setBody(""))
        server.enqueue(MockResponse().setResponseCode(200).setBody(shopNameResponse1))

        val result = AtomicReference<GraphCallResult<Storefront.QueryRoot>>()
        val retryHandler = RetryHandler.build<Storefront.QueryRoot>(2, TimeUnit.SECONDS) {
            maxAttempts(3)
        }
        val call = graphClient.queryGraph(shopNameQuery).enqueue(retryHandler = retryHandler) {
            result.set(it)
        }

        Thread.sleep(TimeUnit.SECONDS.toMillis(3))
        call.cancel()
        Thread.sleep(TimeUnit.SECONDS.toMillis(2))

        assertThat(result.get()).isInstanceOf(GraphCallResult.Failure::class.java)
        assertThat((result.get() as GraphCallResult.Failure).error).isInstanceOf(GraphError.CallCanceledError::class.java)
        assertThat(call.isCanceled).isTrue()
        assertThat(server.requestCount).isEqualTo(2)
    }

    private fun GraphClient.retryAndGet(
        retryHandler: RetryHandler<Storefront.QueryRoot>,
        timeout: Long,
        timeUnit: TimeUnit
    ): GraphCallResult<Storefront.QueryRoot> {
        val result = AtomicReference<GraphCallResult<Storefront.QueryRoot>>()
        val latch = NamedCountDownLatch("retryAndWait", 1)
        queryGraph(shopNameQuery).enqueue(retryHandler = retryHandler) {
            result.set(it)
            latch.countDown()
        }
        latch.awaitOrThrowWithTimeout(timeout, timeUnit)

        return result.get()!!
    }
}
