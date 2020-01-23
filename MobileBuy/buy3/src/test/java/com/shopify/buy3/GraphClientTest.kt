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
import android.os.Handler
import com.google.common.truth.Truth.assertThat
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.doAnswer
import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.intellij.lang.annotations.Language
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import java.net.SocketTimeoutException
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicReference

private const val PACKAGE_NAME = "com.shopify.buy3.test"

@RunWith(MockitoJUnitRunner::class)
class GraphClientTest {
    private val server = MockWebServer()
    @Mock lateinit var mockContext: Context
    private lateinit var graphClient: GraphClient

    @Before fun setUp() {
        doReturn(PACKAGE_NAME).whenever(mockContext).getPackageName()
        val okHttpClient = OkHttpClient.Builder()
            .connectTimeout(1, TimeUnit.SECONDS)
            .readTimeout(1, TimeUnit.SECONDS)
            .build()
        graphClient = GraphClient.build(context = mockContext, shopDomain = "shopDomain", accessToken = "accessToken", configure = {
            httpClient = okHttpClient
            endpointUrl = server.url("/")
        })
    }

    @Test fun httpRequestHeaders() {
        server.enqueueShopNameResponse()

        val result = graphClient.fetchShopName()

        assertThat(result).isInstanceOf(GraphCallResult.Success::class.java)

        with(server.takeRequest()) {
            assertThat(headers["Accept"]).isEqualTo("application/json")
            assertThat(headers["User-Agent"]).isEqualTo("Mobile Buy SDK Android/${BuildConfig.VERSION_NAME}/$PACKAGE_NAME")
            assertThat(headers["X-SDK-Version"]).isEqualTo(BuildConfig.VERSION_NAME)
            assertThat(headers["X-SDK-Variant"]).isEqualTo("android")
            assertThat(headers["X-Shopify-Storefront-Access-Token"]).isEqualTo("accessToken")
            assertThat(this.body.readUtf8()).isEqualTo("{shop{name}}")
        }
    }

    @Test fun graphResponse() {
        server.enqueueShopNameResponse()

        val result = graphClient.fetchShopName()
        assertThat(result).isInstanceOf(GraphCallResult.Success::class.java)
        with(result as GraphCallResult.Success) {
            assertThat(response.data!!.shop.name).isEqualTo("MyShop")
            assertThat(response.errors).isEmpty()
            assertThat(response.hasErrors).isFalse()
        }
    }

    @Test fun graphResponseWithLocale() {
        server.enqueueShopNameResponse()

        val okHttpClient = OkHttpClient.Builder()
                .connectTimeout(1, TimeUnit.SECONDS)
                .readTimeout(1, TimeUnit.SECONDS)
                .build()

        graphClient = GraphClient.build(context = mockContext, shopDomain = "shopDomain", accessToken = "accessToken", configure = {
            httpClient = okHttpClient
            endpointUrl = server.url("/")
        }, locale = "fr")

        val result = graphClient.fetchShopName()
        assertThat(result).isInstanceOf(GraphCallResult.Success::class.java)
        with(result as GraphCallResult.Success) {
            assertThat(response.data!!.shop.name).isEqualTo("MyShop")
            assertThat(response.errors).isEmpty()
            assertThat(response.hasErrors).isFalse()
        }
    }

    @Test fun graphResponseErrors() {
        server.enqueueGraphErrorResponse()

        val result = graphClient.fetchShopName()
        assertThat(result).isInstanceOf(GraphCallResult.Success::class.java)
        with(result as GraphCallResult.Success) {
            assertThat(response.data).isNull()
            assertThat(response.errors).isNotEmpty()
            assertThat(response.hasErrors).isTrue()
            assertThat(response.formattedErrorMessage).isEqualTo("Field 'names' doesn't exist on type 'Shop'")
        }
    }

    @Test fun httpResponseError() {
        server.enqueue(MockResponse().setResponseCode(401).setBody("Unauthorized request!"))

        val result = graphClient.fetchShopName()
        assertThat(result).isInstanceOf(GraphCallResult.Failure::class.java)
        with(result as GraphCallResult.Failure) {
            assertThat(error).isInstanceOf(GraphError.HttpError::class.java)
            with(error as GraphError.HttpError) {
                assertThat(statusCode).isEqualTo(401)
                assertThat(message).isEqualTo("HTTP(401) Client Error")
            }
        }
    }

    @Test fun networkError() {
        val result = graphClient.fetchShopName()
        assertThat(result).isInstanceOf(GraphCallResult.Failure::class.java)
        with(result as GraphCallResult.Failure) {
            assertThat(error).isInstanceOf(GraphError.NetworkError::class.java)
            with(error as GraphError.NetworkError) {
                assertThat(cause).isInstanceOf(SocketTimeoutException::class.java)
            }
        }
    }

    @Test fun parseError() {
        server.enqueue(MockResponse().setBody("Noise"))

        val result = graphClient.fetchShopName()
        assertThat(result).isInstanceOf(GraphCallResult.Failure::class.java)
        with(result as GraphCallResult.Failure) {
            assertThat(error).isInstanceOf(GraphError.ParseError::class.java)
            with(error as GraphError.ParseError) {
                assertThat(message).isEqualTo("Failed to parse GraphQL http response")
            }
        }
    }

    @Test fun canceledErrorBeforeExecute() {
        val call = graphClient.queryGraph(Storefront.query { root -> root.shop { it.name() } })
        call.cancel()

        val result = AtomicReference<GraphCallResult<Storefront.QueryRoot>>()
        val latch = NamedCountDownLatch("canceledErrorBeforeExecute", 1)
        call.enqueue {
            result.set(it)
            latch.countDown()
        }

        latch.awaitOrThrowWithTimeout(2, TimeUnit.SECONDS)

        assertThat(result.get()).isInstanceOf(GraphCallResult.Failure::class.java)
        with(result.get() as GraphCallResult.Failure) {
            assertThat(error).isInstanceOf(GraphError.CallCanceledError::class.java)
        }
    }

    @Test fun canceledErrorDuringExecute() {
        server.enqueueShopNameResponse(TimeUnit.SECONDS.toMillis(3))

        val result = AtomicReference<GraphCallResult<Storefront.QueryRoot>>()
        val latch = NamedCountDownLatch("canceledErrorDuringExecute", 1)
        val call = graphClient.queryGraph(Storefront.query { root -> root.shop { it.name() } }).enqueue {
            result.set(it)
            latch.countDown()
        }

        latch.await(1, TimeUnit.SECONDS)
        call.cancel()

        latch.awaitOrThrowWithTimeout(1, TimeUnit.SECONDS)

        assertThat(result.get()).isInstanceOf(GraphCallResult.Failure::class.java)
        with(result.get() as GraphCallResult.Failure) {
            assertThat(error).isInstanceOf(GraphError.CallCanceledError::class.java)
        }
    }

    @Test fun callbackHandler() {
        val deliveredInHandler = AtomicBoolean()
        val handler = mock<Handler> {
            on { post(any()) } doAnswer { invocation ->
                deliveredInHandler.set(true)
                invocation.getArgument<Runnable>(0).run()
                true
            }
        }

        server.enqueueShopNameResponse()

        val result = graphClient.fetchShopName(handler)
        assertThat(result).isInstanceOf(GraphCallResult.Success::class.java)
        with(result as GraphCallResult.Success) {
            assertThat(response.data!!.shop.name).isEqualTo("MyShop")
            assertThat(response.errors).isEmpty()
            assertThat(response.hasErrors).isFalse()
        }
        assertThat(deliveredInHandler.get()).isTrue()
    }
}

private fun GraphClient.fetchShopName(handler: Handler? = null): GraphCallResult<Storefront.QueryRoot> {
    val result = AtomicReference<GraphCallResult<Storefront.QueryRoot>>()
    val latch = NamedCountDownLatch("getGraphCallResult", 1)
    queryGraph(Storefront.query { root -> root.shop { it.name() } }).enqueue(handler) {
        result.set(it)
        latch.countDown()
    }
    latch.awaitOrThrowWithTimeout(2, TimeUnit.SECONDS)
    return result.get()!!
}

private fun MockWebServer.enqueueShopNameResponse(delayMs: Long = 0) {
    @Language("json") val response = """
            {
              "data": {
                "shop" : {
                  "name" : "MyShop"
                }
              }
            }

        """.trimIndent()
    enqueue(
        MockResponse()
            .setResponseCode(200)
            .setBody(response)
            .let { if (delayMs > 0) it.setBodyDelay(delayMs, TimeUnit.MILLISECONDS) else it }
    )
}

private fun MockWebServer.enqueueGraphErrorResponse() {
    @Language("json") val response = """
            {
              "errors": [
                {
                  "message" : "Field 'names' doesn't exist on type 'Shop'",
                  "locations": [
                    {
                      "line" : 1,
                      "column": 7
                    }
                  ],
                  "fields": [
                    "query", "shop", "names"
                  ]
                }
              ]
            }

        """.trimIndent()
    enqueue(MockResponse().setResponseCode(200).setBody(response))
}
