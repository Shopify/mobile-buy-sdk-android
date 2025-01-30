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
import androidx.annotation.VisibleForTesting
import com.shopify.buy3.internal.RealMutationGraphCall
import com.shopify.buy3.internal.RealQueryGraphCall
import okhttp3.Call
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.OkHttpClient
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.ScheduledThreadPoolExecutor
import java.util.concurrent.TimeUnit

private val DEFAULT_HTTP_CONNECTION_TIME_OUT_MS = TimeUnit.SECONDS.toMillis(10)
private val DEFAULT_HTTP_READ_WRITE_TIME_OUT_MS = TimeUnit.SECONDS.toMillis(20)

@DslMarker
annotation class GraphClientBuilder

/**
 * Client for Shopify Storefront `GraphQL` API server.
 *
 * Client is responsible for creating and preparing [GraphCall] calls, which can be used to send `GraphQL` operation http
 * requests.
 *
 * Internally client is based on [OkHttpClient] that means it holds its own connection pool and thread pool. It is
 * recommended to only create a single instance and use that for execution of all the `GraphQL` calls, as this would reduce latency
 * and would also save memory.
 *
 * ****NOTE:**** This client should be shared between calls to the same shop domain.
 */
class GraphClient private constructor(
    internal val serverUrl: HttpUrl,
    internal val httpCallFactory: Call.Factory,
    internal val dispatcher: ScheduledExecutorService
) {

    /**
     * Creates call to execute `GraphQL` query operation.
     *
     * Creates and prepares [QueryGraphCall] that represents `GraphQL` query operation to be executed at some point in the future.
     *
     * @param query [Storefront.QueryRootQuery] to be executed
     * @return prepared [QueryGraphCall] call for later execution
     */
    fun queryGraph(query: Storefront.QueryRootQuery): QueryGraphCall {
        return RealQueryGraphCall(query, serverUrl, httpCallFactory, dispatcher)
    }

    /**
     * Creates call to execute `GraphQL` mutation operation.
     *
     * Creates and prepares [MutationGraphCall] that represents `GraphQL` mutation operation to be executed at some point in the
     * future.
     *
     * @param query [Storefront.MutationQuery] to be executed
     * @return prepared [MutationGraphCall] call for later execution
     */
    fun mutateGraph(query: Storefront.MutationQuery): MutationGraphCall {
        return RealMutationGraphCall(query, serverUrl, httpCallFactory, dispatcher)
    }

    companion object {
        /**
         * Instantiates and builds new [GraphClient] instance.
         *
         * @param context android context
         * @param shopDomain Shopify store domain URL (usually {@code {store name}.myshopify.com})
         * @param accessToken Shopify store access token obtained on your shop's admin page
         * @param configure function to configure optional parameters
         * @return [GraphClient.Config] client builder
         */
        fun build(
                context: Context,
                shopDomain: String,
                accessToken: String,
                configure: Config.() -> Unit = {},
                locale: String? = null
        ): GraphClient = Config.create(
            context = context,
            shopDomain = shopDomain,
            accessToken = accessToken,
            configure = configure
        ).build(locale)

        fun build(
                context: Context,
                shopDomain: String,
                accessToken: String,
                configure: Config.() -> Unit = {}
        ): GraphClient = build(
            context = context,
            shopDomain = shopDomain,
            accessToken = accessToken,
            configure = configure,
            locale = null
        )
    }

    /**
     * Builds new [GraphClient] instance.
     */
    @GraphClientBuilder
    class Config private constructor(
        context: Context,
        private val shopDomain: String,
        private val accessToken: String
    ) {
        private val applicationName = context.packageName

        @VisibleForTesting
        internal var dispatcher: ScheduledThreadPoolExecutor? = null

        @VisibleForTesting
        internal var endpointUrl =
            "https://$shopDomain/api/${Storefront.API_VERSION}/graphql".toHttpUrl()

        init {
            shopDomain.checkNotBlank("shopDomain can't be empty")
            accessToken.checkNotBlank("accessToken can't be empty")
        }

        /**
         * [OkHttpClient] to be used as network layer for making HTTP requests.
         */
        var httpClient: OkHttpClient = defaultOkHttpClient()

        /**
         * Builds the [GraphClient] instance with provided configuration options.
         *
         * @return configured [GraphClient]
         */
        fun build(locale: String?): GraphClient {
            val okHttpClient = httpClient.withSdkHeaderInterceptor(
                    applicationName = applicationName,
                    accessToken = accessToken,
                    locale = locale
            )

            return GraphClient(
                serverUrl = endpointUrl,
                httpCallFactory = okHttpClient,
                dispatcher = dispatcher ?: defaultDispatcher()
            )
        }

        companion object {
            internal inline fun create(
                context: Context,
                shopDomain: String,
                accessToken: String,
                crossinline configure: Config.() -> Unit
            ): Config {
                return Config(
                    context = context,
                    shopDomain = shopDomain,
                    accessToken = accessToken
                ).apply(configure)
            }
        }
    }
}

private fun defaultOkHttpClient(): OkHttpClient {
    return OkHttpClient.Builder()
        .connectTimeout(DEFAULT_HTTP_CONNECTION_TIME_OUT_MS, TimeUnit.MILLISECONDS)
        .readTimeout(DEFAULT_HTTP_READ_WRITE_TIME_OUT_MS, TimeUnit.MILLISECONDS)
        .writeTimeout(DEFAULT_HTTP_READ_WRITE_TIME_OUT_MS, TimeUnit.MILLISECONDS)
        .build()
}

private fun OkHttpClient.withSdkHeaderInterceptor(applicationName: String, accessToken: String, locale: String?): OkHttpClient {
    return newBuilder().addInterceptor { chain ->
        val original = chain.request()
        val builder = original.newBuilder().method(original.method, original.body)
        builder.header("User-Agent", "Mobile Buy SDK Android/" + BuildConfig.BUY_SDK_VERSION + "/" + applicationName)
        builder.header("X-SDK-Version", BuildConfig.BUY_SDK_VERSION)
        builder.header("X-SDK-Variant", "android")
        builder.header("X-Shopify-Storefront-Access-Token", accessToken)
        if (locale!= null) {
            builder.header("Accept-Language", locale.toString())
        }
        chain.proceed(builder.build())
    }.build()
}

private fun defaultDispatcher(): ScheduledThreadPoolExecutor {
    return ScheduledThreadPoolExecutor(1) { runnable ->
        Thread(runnable, "GraphClient Call Dispatcher")
    }
        .apply { setKeepAliveTime(1, TimeUnit.SECONDS) }
        .apply { allowCoreThreadTimeOut(true) }
}

private fun String.checkNotBlank(message: String) {
    if (isBlank()) throw IllegalArgumentException(message)
}
