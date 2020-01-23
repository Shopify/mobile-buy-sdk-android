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
import com.shopify.buy3.internal.RealMutationGraphCall
import com.shopify.buy3.internal.RealQueryGraphCall
import com.shopify.buy3.internal.cache.DiskLruCacheStore
import com.shopify.buy3.internal.cache.HTTP_CACHE_KEY_HEADER
import com.shopify.buy3.internal.cache.cacheKey
import com.shopify.graphql.support.ID
import okhttp3.OkHttpClient
import okhttp3.internal.io.InMemoryFileSystem
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.intellij.lang.annotations.Language
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import java.io.File
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicReference

private const val PACKAGE_NAME = "com.shopify.buy3.test"
private const val SHOP_DOMAIN = "myshop.shopify.com"
private const val ACCESS_TOKEN = "access_token"

@RunWith(MockitoJUnitRunner::class)
class CacheTest {
    @Mock lateinit var mockContext: Context
    @get:Rule val server = MockWebServer()
    @get:Rule val inMemoryFileSystem = InMemoryFileSystem()
    private lateinit var graphClient: GraphClient
    private lateinit var lastHttRequest: okhttp3.Request
    private lateinit var lastHttResponse: okhttp3.Response
    private lateinit var okHttpClient: OkHttpClient
    private val mutationQuery = Storefront.mutation { root ->
        root.checkoutCompleteFree(ID("test")) { query -> query.checkout { it.ready() } }
    }

    @Before fun setUp() {
        doReturn(PACKAGE_NAME).whenever(mockContext).packageName
        okHttpClient = OkHttpClient.Builder()
            .addInterceptor { chain ->
                lastHttRequest = chain.request()
                lastHttResponse = chain.proceed(lastHttRequest)
                lastHttResponse
            }
            .readTimeout(4, TimeUnit.SECONDS)
            .connectTimeout(4, TimeUnit.SECONDS)
            .writeTimeout(4, TimeUnit.SECONDS)
            .build()

        graphClient = GraphClient.build(
                context = mockContext,
                shopDomain = SHOP_DOMAIN,
                accessToken = ACCESS_TOKEN,
                configure = {
                    httpClient = okHttpClient
                    endpointUrl = server.url(SHOP_DOMAIN)
                    httpCache(DiskLruCacheStore(inMemoryFileSystem, File("/cache/"), Integer.MAX_VALUE.toLong())) {
                        defaultCachePolicy = HttpCachePolicy.Default.NETWORK_FIRST
                    }
                }
        )
    }

    @After fun tearDown() {
        try {
            server.shutdown()
        } catch (ignore: Exception) {
            // ignore
        }

        try {
            graphClient.clearCache()
        } catch (ignore: Exception) {
            // ignore
        }
    }

    @Test
    fun defaultQueryCallCachePolicy() {
        var graphCall = graphClient.queryGraph(Storefront.query(ShopWithCollectionsQuery())) as RealQueryGraphCall
        with(graphCall) {
            assertThat(httpCachePolicy.fetchStrategy).isEqualTo(HttpCachePolicy.FetchStrategy.NETWORK_FIRST)
            assertThat((httpCachePolicy as HttpCachePolicy.ExpirePolicy).expireTimeoutMs).isEqualTo(0)
        }

        graphCall = graphCall.cachePolicy(HttpCachePolicy.Default.CACHE_FIRST.expireAfter(60, TimeUnit.SECONDS)) as RealQueryGraphCall
        with(graphCall) {
            assertThat(httpCachePolicy.fetchStrategy).isEqualTo(HttpCachePolicy.FetchStrategy.CACHE_FIRST)
            assertThat((httpCachePolicy as HttpCachePolicy.ExpirePolicy).expireTimeoutMs).isEqualTo(TimeUnit.SECONDS.toMillis(60))
        }
    }

    @Test
    fun defaultMutationCallCachePolicy() {
        val graphCall = graphClient.mutateGraph(mutationQuery) as RealMutationGraphCall
        assertThat(graphCall.httpCachePolicy.fetchStrategy).isEqualTo(HttpCachePolicy.FetchStrategy.NETWORK_ONLY)
    }

    @Test fun prematureDisconnect() {
        server.enqueue(MockResponse().setChunkedBody(shopWithCollections.take(65), 32))

        graphClient.queryGraph(Storefront.query(ShopWithCollectionsQuery()))
            .cachePolicy(HttpCachePolicy.Default.NETWORK_ONLY)
            .getResult()
            .also {
                assertThat(it).isInstanceOf(GraphCallResult.Failure::class.java)
                assertThat((it as GraphCallResult.Failure).error).isInstanceOf(GraphError.ParseError::class.java)
            }

        checkNoCachedResponse()
    }

    @Test fun defaultCachePolicy() {
        server.enqueue(MockResponse().setChunkedBody(shopWithCollections, 32))

        graphClient.queryGraph(Storefront.query(ShopWithCollectionsQuery()))
            .getResult()
            .also { assertThat(it).isInstanceOf(GraphCallResult.Success::class.java) }

        assertThat(server.requestCount).isEqualTo(1)
        assertThat(lastHttResponse.cacheResponse()).isNull()

        checkCachedResponse(shopWithCollections)
    }

    @Test fun networkOnly() {
        server.enqueue(MockResponse().setChunkedBody(shopWithCollections, 32))

        graphClient.queryGraph(Storefront.query(ShopWithCollectionsQuery()))
            .cachePolicy(HttpCachePolicy.Default.NETWORK_ONLY)
            .getResult()
            .also { assertThat(it).isInstanceOf(GraphCallResult.Success::class.java) }

        assertThat(server.requestCount).isEqualTo(1)
        assertThat(lastHttResponse.cacheResponse()).isNull()

        checkCachedResponse(shopWithCollections)
    }

    @Test fun cacheOnlyHit() {
        server.enqueue(MockResponse().setChunkedBody(shopWithCollections, 32))

        graphClient.queryGraph(Storefront.query(ShopWithCollectionsQuery()))
            .cachePolicy(HttpCachePolicy.Default.NETWORK_FIRST.expireAfter(10, TimeUnit.SECONDS))
            .getResult()
            .also { assertThat(it).isInstanceOf(GraphCallResult.Success::class.java) }

        assertThat(server.requestCount).isEqualTo(1)

        server.enqueue(MockResponse().setChunkedBody(shopWithCollections, 32))
        graphClient.queryGraph(Storefront.query(ShopWithCollectionsQuery()))
            .cachePolicy(HttpCachePolicy.Default.CACHE_ONLY)
            .getResult()
            .also { assertThat(it).isInstanceOf(GraphCallResult.Success::class.java) }

        assertThat(server.requestCount).isEqualTo(1)
        assertThat(lastHttResponse.networkResponse()).isNull()
        assertThat(lastHttResponse.cacheResponse()).isNotNull()

        checkCachedResponse(shopWithCollections)
    }

    @Test fun cacheOnlyMiss() {
        graphClient.queryGraph(Storefront.query(ShopWithCollectionsQuery()))
            .cachePolicy(HttpCachePolicy.Default.CACHE_ONLY)
            .getResult()
            .also {
                assertThat(it).isInstanceOf(GraphCallResult.Failure::class.java)
                with(it as GraphCallResult.Failure) {
                    assertThat(error).isInstanceOf(GraphError.HttpError::class.java)
                    with(error as GraphError.HttpError) {
                        assertThat(statusCode).isEqualTo(504)
                        assertThat(message).isEqualTo("HTTP(504) Unsatisfiable Request (cache-only)")
                    }
                }
            }
    }

    @Test fun cacheNonStale() {
        server.enqueue(MockResponse().setChunkedBody(shopWithCollections, 32))

        graphClient.queryGraph(Storefront.query(ShopWithCollectionsQuery()))
            .cachePolicy(HttpCachePolicy.Default.NETWORK_FIRST)
            .getResult()
            .also { assertThat(it).isInstanceOf(GraphCallResult.Success::class.java) }

        assertThat(server.requestCount).isEqualTo(1)

        graphClient.queryGraph(Storefront.query(ShopWithCollectionsQuery()))
            .cachePolicy(HttpCachePolicy.Default.CACHE_FIRST.expireAfter(10, TimeUnit.SECONDS))
            .getResult()
            .also { assertThat(it).isInstanceOf(GraphCallResult.Success::class.java) }

        assertThat(server.requestCount).isEqualTo(1)
        assertThat(lastHttResponse.networkResponse()).isNull()
        assertThat(lastHttResponse.cacheResponse()).isNotNull()

        checkCachedResponse(shopWithCollections)
    }

    @Test fun cacheFirstStale() {
        server.enqueue(MockResponse().setChunkedBody(shopWithCollections, 32))

        graphClient.queryGraph(Storefront.query(ShopWithCollectionsQuery()))
            .cachePolicy(HttpCachePolicy.Default.NETWORK_FIRST)
            .getResult()
            .also { assertThat(it).isInstanceOf(GraphCallResult.Success::class.java) }

        assertThat(server.requestCount).isEqualTo(1)

        Thread.sleep(TimeUnit.SECONDS.toMillis(2))

        server.enqueue(MockResponse().setChunkedBody(shopWithCollections, 32))
        graphClient.queryGraph(Storefront.query(ShopWithCollectionsQuery()))
            .cachePolicy(HttpCachePolicy.Default.CACHE_FIRST.expireAfter(1, TimeUnit.SECONDS))
            .getResult()
            .also { assertThat(it).isInstanceOf(GraphCallResult.Success::class.java) }

        assertThat(server.requestCount).isEqualTo(2)
        assertThat(lastHttResponse.networkResponse()).isNotNull()
        assertThat(lastHttResponse.cacheResponse()).isNull()

        checkCachedResponse(shopWithCollections)
    }

    @Test fun cacheFirstUpdate() {
        server.enqueue(MockResponse().setChunkedBody(shopWithCollections, 32))
        graphClient.queryGraph(Storefront.query(ShopWithCollectionsQuery()))
            .cachePolicy(HttpCachePolicy.Default.NETWORK_FIRST)
            .getResult()
            .also { assertThat(it).isInstanceOf(GraphCallResult.Success::class.java) }

        server.enqueue(MockResponse().setChunkedBody(shopWithCollectionsUpdate, 32))
        graphClient.queryGraph(Storefront.query(ShopWithCollectionsQuery()))
            .cachePolicy(HttpCachePolicy.Default.NETWORK_FIRST)
            .getResult()
            .also { assertThat(it).isInstanceOf(GraphCallResult.Success::class.java) }

        assertThat(server.requestCount).isEqualTo(2)
        checkCachedResponse(shopWithCollectionsUpdate)

        graphClient.queryGraph(Storefront.query(ShopWithCollectionsQuery()))
            .cachePolicy(HttpCachePolicy.Default.CACHE_FIRST)
            .getResult()
            .also { assertThat(it).isInstanceOf(GraphCallResult.Success::class.java) }

        assertThat(lastHttResponse.networkResponse()).isNull()
        assertThat(lastHttResponse.cacheResponse()).isNotNull()

        checkCachedResponse(shopWithCollectionsUpdate)
    }

    @Test fun networkFirstHttpError() {
        server.enqueue(MockResponse().setChunkedBody(shopWithCollections, 32))
        graphClient.queryGraph(Storefront.query(ShopWithCollectionsQuery()))
            .cachePolicy(HttpCachePolicy.Default.NETWORK_FIRST)
            .getResult()
            .also { assertThat(it).isInstanceOf(GraphCallResult.Success::class.java) }

        server.enqueue(MockResponse().setResponseCode(504).setBody(""))
        graphClient.queryGraph(Storefront.query(ShopWithCollectionsQuery()))
            .cachePolicy(HttpCachePolicy.Default.NETWORK_FIRST)
            .getResult()
            .also { assertThat(it).isInstanceOf(GraphCallResult.Success::class.java) }

        assertThat(server.requestCount).isEqualTo(2)
        assertThat(lastHttResponse.networkResponse().code()).isEqualTo(504)
        assertThat(lastHttResponse.cacheResponse()).isNotNull()

        checkCachedResponse(shopWithCollections)
    }

    @Test fun fileSystemUnavailable() {
        val graphClient = GraphClient.build(
                context = mockContext,
                shopDomain = SHOP_DOMAIN,
                accessToken = ACCESS_TOKEN,
                configure = {
                    httpClient = okHttpClient
                    endpointUrl = server.url(SHOP_DOMAIN)
                    httpCache(DiskLruCacheStore(NoFileSystem(), File("/cache/"), Integer.MAX_VALUE.toLong())) {
                        defaultCachePolicy = HttpCachePolicy.Default.NETWORK_FIRST
                    }
                }
        )

        server.enqueue(MockResponse().setChunkedBody(shopWithCollections, 32))
        graphClient.queryGraph(Storefront.query(ShopWithCollectionsQuery()))
            .cachePolicy(HttpCachePolicy.Default.NETWORK_FIRST)
            .getResult()
            .also { assertThat(it).isInstanceOf(GraphCallResult.Success::class.java) }

        graphClient.queryGraph(Storefront.query(ShopWithCollectionsQuery()))
            .cachePolicy(HttpCachePolicy.Default.CACHE_ONLY)
            .getResult()
            .also {
                assertThat(it).isInstanceOf(GraphCallResult.Failure::class.java)
                assertThat((it as GraphCallResult.Failure).error).isInstanceOf(GraphError.HttpError::class.java)
            }

        checkNoCachedResponse()
    }

    @Test fun fileSystemWriteFailure() {
        val faultyCacheStore = FaultyCacheStore(inMemoryFileSystem)

        val graphClient = GraphClient.build(
                context = mockContext,
                shopDomain = SHOP_DOMAIN,
                accessToken = ACCESS_TOKEN,
                configure = {
                    httpClient = okHttpClient
                    endpointUrl = server.url(SHOP_DOMAIN)
                    httpCache(faultyCacheStore) {
                        defaultCachePolicy = HttpCachePolicy.Default.NETWORK_FIRST
                    }
                }
        )

        server.enqueue(MockResponse().setChunkedBody(shopWithCollections, 32))
        faultyCacheStore.failStrategy(FaultyCacheStore.FailStrategy.FAIL_HEADER_WRITE)

        graphClient.queryGraph(Storefront.query(ShopWithCollectionsQuery()))
            .cachePolicy(HttpCachePolicy.Default.NETWORK_FIRST)
            .getResult()
            .also { assertThat(it).isInstanceOf(GraphCallResult.Success::class.java) }
        assertThat(server.requestCount).isEqualTo(1)
        checkNoCachedResponse()

        server.enqueue(MockResponse().setChunkedBody(shopWithCollections, 32))
        faultyCacheStore.failStrategy(FaultyCacheStore.FailStrategy.FAIL_BODY_WRITE)
        graphClient.queryGraph(Storefront.query(ShopWithCollectionsQuery()))
            .cachePolicy(HttpCachePolicy.Default.NETWORK_FIRST)
            .getResult()
            .also { assertThat(it).isInstanceOf(GraphCallResult.Success::class.java) }

        assertThat(server.requestCount).isEqualTo(2)
        checkNoCachedResponse()
    }

    @Test fun fileSystemReadFailure() {
        val faultyCacheStore = FaultyCacheStore(inMemoryFileSystem)

        val graphClient = GraphClient.build(
                context = mockContext,
                shopDomain = SHOP_DOMAIN,
                accessToken = ACCESS_TOKEN,
                configure = {
                    httpClient = okHttpClient
                    endpointUrl = server.url(SHOP_DOMAIN)
                    httpCache(faultyCacheStore) {
                        defaultCachePolicy = HttpCachePolicy.Default.NETWORK_FIRST
                    }
                }
        )

        server.enqueue(MockResponse().setChunkedBody(shopWithCollections, 32))
        faultyCacheStore.failStrategy(FaultyCacheStore.FailStrategy.NO_FAIL)

        graphClient.queryGraph(Storefront.query(ShopWithCollectionsQuery()))
            .cachePolicy(HttpCachePolicy.Default.NETWORK_ONLY)
            .getResult()
            .also { assertThat(it).isInstanceOf(GraphCallResult.Success::class.java) }
        assertThat(server.requestCount).isEqualTo(1)
        checkCachedResponse(shopWithCollections)

        server.enqueue(MockResponse().setChunkedBody(shopWithCollections, 32))
        faultyCacheStore.failStrategy(FaultyCacheStore.FailStrategy.FAIL_HEADER_READ)
        graphClient.queryGraph(Storefront.query(ShopWithCollectionsQuery()))
            .cachePolicy(HttpCachePolicy.Default.NETWORK_ONLY)
            .getResult()
            .also { assertThat(it).isInstanceOf(GraphCallResult.Success::class.java) }

        assertThat(server.requestCount).isEqualTo(2)

        faultyCacheStore.failStrategy(FaultyCacheStore.FailStrategy.FAIL_BODY_READ)
        graphClient.queryGraph(Storefront.query(ShopWithCollectionsQuery()))
            .cachePolicy(HttpCachePolicy.Default.CACHE_ONLY)
            .getResult()
            .also {
                assertThat(it).isInstanceOf(GraphCallResult.Failure::class.java)
                assertThat((it as GraphCallResult.Failure).error).isInstanceOf(GraphError.ParseError::class.java)
            }
    }

    private fun checkNoCachedResponse() {
        val cacheKey = lastHttRequest.header(HTTP_CACHE_KEY_HEADER)
        val cachedResponse = graphClient.httpCache!!.read(cacheKey)
        assertThat(cachedResponse).isNull()
    }

    private fun checkCachedResponse(expectedResponseBody: String) {
        val cacheKey = lastHttRequest.body().cacheKey
        val response = graphClient.httpCache!!.read(cacheKey)!!
        assertThat(response.body().source().readUtf8()).isEqualTo(expectedResponseBody)
        response.body().source().close()
    }
}

private fun QueryGraphCall.getResult(): GraphCallResult<Storefront.QueryRoot> {
    val result = AtomicReference<GraphCallResult<Storefront.QueryRoot>>()
    val latch = NamedCountDownLatch("getResult", 1)
    enqueue {
        result.set(it)
        latch.countDown()
    }
    latch.awaitOrThrowWithTimeout(2, TimeUnit.SECONDS)
    return result.get()!!
}

private class ShopWithCollectionsQuery : Storefront.QueryRootQueryDefinition {
    override fun define(root: Storefront.QueryRootQuery) {
        root.shop { shop ->
            shop
                .name()
                .currencyCode()
                .description()
                .collections({ it.first(10) }, { collectionConnection ->
                    collectionConnection
                        .edges { collectionEdge ->
                            collectionEdge
                                .cursor()
                                .node { it.title() }
                        }
                })
        }
    }
}

@Language("json") private val shopWithCollections = """
    {
      "data": {
        "shop": {
          "name": "Greats Clone",
          "currencyCode": "USD",
          "description": "A description ",
          "collections": {
            "edges": [
              {
                "cursor": "eyJsYXN0X2lkIjoxNDg3ODQ0NTEsImxhc3RfdmFsdWUiOiIxNDg3ODQ0NTEifQ==",
                "node": {
                  "id": "Z2lkOi8vc2hvcGlmeS9Db2xsZWN0aW9uLzE0ODc4NDQ1MQ==",
                  "title": "Frontpage"
                }
              },
              {
                "cursor": "eyJsYXN0X2lkIjoxNDg4MTc3MzEsImxhc3RfdmFsdWUiOiIxNDg4MTc3MzEifQ==",
                "node": {
                  "id": "Z2lkOi8vc2hvcGlmeS9Db2xsZWN0aW9uLzE0ODgxNzczMQ==",
                  "title": "The Rosen"
                }
              }
            ]
          }
        }
      }
    }
    """.trimIndent()

@Language("json") private val shopWithCollectionsUpdate = """
    {
      "data": {
        "shop": {
          "name": "Greats Clone",
          "currencyCode": "USD",
          "description": "UPDATED VERSION ",
          "collections": {
            "edges": [
              {
                "cursor": "eyJsYXN0X2lkIjoxNDg3ODQ0NTEsImxhc3RfdmFsdWUiOiIxNDg3ODQ0NTEifQ==",
                "node": {
                  "id": "Z2lkOi8vc2hvcGlmeS9Db2xsZWN0aW9uLzE0ODc4NDQ1MQ==",
                  "title": "Frontpage Updated"
                }
              },
              {
                "cursor": "eyJsYXN0X2lkIjoxNDg4MTc3MzEsImxhc3RfdmFsdWUiOiIxNDg4MTc3MzEifQ==",
                "node": {
                  "id": "Z2lkOi8vc2hvcGlmeS9Db2xsZWN0aW9uLzE0ODgxNzczMQ==",
                  "title": "The Rosen Updated"
                }
              }
            ]
          }
        }
      }
    }
    """.trimIndent()
