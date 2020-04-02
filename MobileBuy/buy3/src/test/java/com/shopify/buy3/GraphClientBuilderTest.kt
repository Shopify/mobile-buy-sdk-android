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
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import java.io.File

private const val PACKAGE_NAME = "com.shopify.buy3.test"
private const val SHOP_DOMAIN = "shopDomain"
private const val ACCESS_TOKEN = "access_token"
private val ENDPOINT_URL = HttpUrl.parse(String.format("https://%s/api/%s/graphql", SHOP_DOMAIN, Storefront.API_VERSION))

@RunWith(MockitoJUnitRunner::class)
class GraphClientBuilderTest {
    @Mock lateinit var mockContext: Context

    @Before fun setUp() {
        doReturn(PACKAGE_NAME).whenever(mockContext).packageName
    }

    @Test fun buildSuccessWithCustomOkHttpClient() {
        val networkInterceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC)
        val okHttpClient = OkHttpClient.Builder()
            .addNetworkInterceptor(networkInterceptor)
            .build()

        val graphClient = GraphClient.build(context = mockContext, shopDomain = SHOP_DOMAIN, accessToken = ACCESS_TOKEN, configure = {
            httpClient = okHttpClient
        })

        with(graphClient) {
            assertThat(serverUrl).isEqualTo(ENDPOINT_URL)
            assertThat(httpCallFactory).isNotNull()
            assertThat(httpCallFactory).isInstanceOf(OkHttpClient::class.java)
            assertThat((httpCallFactory as OkHttpClient).networkInterceptors()).contains(networkInterceptor)
        }
    }

    @Test fun buildSuccessWithDefaultClient() {
        val graphClient = GraphClient.build(context = mockContext, shopDomain = SHOP_DOMAIN, accessToken = ACCESS_TOKEN)

        with(graphClient) {
            assertThat(serverUrl).isEqualTo(ENDPOINT_URL)
            assertThat(httpCallFactory).isNotNull()
            assertThat(httpCallFactory).isInstanceOf(OkHttpClient::class.java)
        }
    }

    @Test fun buildFailWithPreconditions() {
        checkForIllegalArgumentException { GraphClient.build(context = mockContext, shopDomain = "", accessToken = ACCESS_TOKEN) }
        checkForIllegalArgumentException { GraphClient.build(context = mockContext, shopDomain = SHOP_DOMAIN, accessToken = "") }
        checkForIllegalArgumentException {
            GraphClient.build(context = mockContext, shopDomain = SHOP_DOMAIN, accessToken = ACCESS_TOKEN, configure = {
                httpCache(cacheFolder = File("")) {
                    cacheMaxSizeBytes = 0
                }
            })
        }
        checkForIllegalArgumentException {
            GraphClient.build(context = mockContext, shopDomain = SHOP_DOMAIN, accessToken = ACCESS_TOKEN, configure = {
                httpCache(cacheFolder = File("")) {
                    cacheMaxSizeBytes = -1
                }
            })
        }
    }
}
