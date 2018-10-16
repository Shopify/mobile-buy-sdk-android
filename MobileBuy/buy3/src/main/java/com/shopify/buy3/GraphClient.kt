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
import android.support.annotation.VisibleForTesting
import com.shopify.buy3.internal.RealMutationGraphCall
import com.shopify.buy3.internal.RealQueryGraphCall
import com.shopify.buy3.internal.cache.HttpCache
import okhttp3.Call
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okio.ByteString
import java.io.File
import java.nio.charset.Charset
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.ScheduledThreadPoolExecutor
import java.util.concurrent.TimeUnit

private val DEFAULT_HTTP_CONNECTION_TIME_OUT_MS = TimeUnit.SECONDS.toMillis(10)
private val DEFAULT_HTTP_READ_WRITE_TIME_OUT_MS = TimeUnit.SECONDS.toMillis(20)

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
    internal val defaultHttpCachePolicy: HttpCachePolicy,
    internal val httpCache: HttpCache?,
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
        return RealQueryGraphCall(query, serverUrl, httpCallFactory, dispatcher, defaultHttpCachePolicy, httpCache)
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

    /**
     * Clear all cached http responses ignoring any exceptions.
     */
    fun clearCache() {
        httpCache?.clear()
    }

    /**
     * Remove cached http response by its cache key, ignoring any exceptions.
     *
     * @param cacheKey cache key
     */
    fun removeCachedResponseQuietly(cacheKey: String) {
        httpCache?.removeQuietly(cacheKey)
    }

    companion object {
        /**
         * Instantiates and builds new [GraphClient] instance.
         *
         * @param context android context
         * @param shopDomain Shopify store domain URL (usually {@code {store name}.myshopify.com})
         * @param accessToken Shopify store access token obtained on your shop's admin page
         * @param configure function to configure optional parameters
         * @return [GraphClient.Builder] client builder
         */
        fun build(
            context: Context,
            shopDomain: String,
            accessToken: String,
            configure: Builder.() -> Unit = {}
        ): GraphClient = Builder.create(
            context = context,
            shopDomain = shopDomain,
            accessToken = accessToken,
            configure = configure
        ).build()
    }

    /**
     * Builds new [GraphClient] instance.
     */
    class Builder private constructor(
        context: Context,
        shopDomain: String,
        private val accessToken: String
    ) {
        private val applicationName = context.packageName
        private var endpointUrl = HttpUrl.parse("https://$shopDomain/api/graphql")
        private var defaultHttpCachePolicy: HttpCachePolicy = HttpCachePolicy.Default.NETWORK_ONLY
        private var okHttpClient: OkHttpClient? = null
        private var dispatcher: ScheduledThreadPoolExecutor? = null
        private var httpCache: HttpCache? = null

        init {
            shopDomain.checkNotBlank("shopDomain can't be empty")
            accessToken.checkNotBlank("accessToken can't be empty")
        }

        /**
         * Enables http cache with provided storage settings.
         *
         * @param cacheFolder  a writable cache directory
         * @param cacheMaxSizeBytes the maximum number of bytes this cache should use to store, must be > 0
         * @param defaultCachePolicy to be used as default for all [QueryGraphCall] calls. By default [HttpCachePolicy.NETWORK_FIRST]
         * @see HttpCache
         */
        fun withHttpCache(
            cacheFolder: File,
            cacheMaxSizeBytes: Long,
            defaultCachePolicy: HttpCachePolicy = HttpCachePolicy.Default.NETWORK_FIRST
        ) {
            if (cacheMaxSizeBytes <= 0) {
                throw IllegalArgumentException("cacheMaxSizeBytes must be > 0")
            }

            val version = BuildConfig.VERSION_NAME
            val tmp = (endpointUrl.toString() + "/" + version + "/" + accessToken).toByteArray(Charset.forName("UTF-8"))
            val httpCacheFolder = File(cacheFolder, ByteString.of(*tmp).md5().hex())
            httpCache = HttpCache(httpCacheFolder, cacheMaxSizeBytes)

            this.defaultHttpCachePolicy = defaultCachePolicy
        }

        internal fun withHttpCache(httpCache: HttpCache, defaultCachePolicy: HttpCachePolicy = HttpCachePolicy.Default.NETWORK_FIRST) {
            this.httpCache = httpCache
            this.defaultHttpCachePolicy = defaultCachePolicy
        }

        /**
         * Sets the custom [OkHttpClient] to be used as network layer for making HTTP requests.
         *
         * @param okHttpClient [OkHttpClient] client to be used
         */
        fun withCustomOkHttpClient(okHttpClient: OkHttpClient) {
            this.okHttpClient = okHttpClient
        }

        @VisibleForTesting
        internal fun withCustomDispatcher(dispatcher: ScheduledThreadPoolExecutor) {
            this.dispatcher = dispatcher
        }

        @VisibleForTesting
        internal fun endpointUrl(endpointUrl: HttpUrl) {
            this.endpointUrl = endpointUrl
        }

        /**
         * Builds the [GraphClient] instance with provided configuration options.
         *
         * @return configured [GraphClient]
         */
        fun build(): GraphClient {
            return GraphClient(
                serverUrl = endpointUrl,
                httpCallFactory = (okHttpClient ?: defaultOkHttpClient())
                    .withSdkHeaderInterceptor(
                        applicationName = applicationName,
                        accessToken = accessToken
                    )
                    .withHttpCacheInterceptor(httpCache),
                defaultHttpCachePolicy = defaultHttpCachePolicy,
                httpCache = httpCache,
                dispatcher = dispatcher ?: defaultDispatcher()
            )
        }

        private fun defaultOkHttpClient(): OkHttpClient {
            return OkHttpClient.Builder()
                .connectTimeout(DEFAULT_HTTP_CONNECTION_TIME_OUT_MS, TimeUnit.MILLISECONDS)
                .readTimeout(DEFAULT_HTTP_READ_WRITE_TIME_OUT_MS, TimeUnit.MILLISECONDS)
                .writeTimeout(DEFAULT_HTTP_READ_WRITE_TIME_OUT_MS, TimeUnit.MILLISECONDS)
                .build()
        }

        private fun OkHttpClient.withHttpCacheInterceptor(httpCache: HttpCache?): OkHttpClient {
            return if (httpCache != null) {
                newBuilder().addInterceptor(httpCache.httpInterceptor()).build()
            } else {
                this
            }
        }

        private fun OkHttpClient.withSdkHeaderInterceptor(applicationName: String, accessToken: String): OkHttpClient {
            return newBuilder().addInterceptor { chain ->
                val original = chain.request()
                val builder = original.newBuilder().method(original.method(), original.body())
                builder.header("User-Agent", "Mobile Buy SDK Android/" + BuildConfig.VERSION_NAME + "/" + applicationName)
                builder.header("X-SDK-Version", BuildConfig.VERSION_NAME)
                builder.header("X-SDK-Variant", "android")
                builder.header("X-Shopify-Storefront-Access-Token", accessToken)
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

        companion object {
            internal inline fun create(
                context: Context,
                shopDomain: String,
                accessToken: String,
                crossinline configure: Builder.() -> Unit
            ): Builder {
                return Builder(
                    context = context,
                    shopDomain = shopDomain,
                    accessToken = accessToken
                ).apply(configure)
            }
        }
    }
}

private fun String.checkNotBlank(message: String) {
    if (isBlank()) throw IllegalArgumentException(message)
}