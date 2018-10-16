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
package com.shopify.buy3.internal.cache

import okhttp3.Interceptor
import okhttp3.Response
import okio.ForwardingSource
import timber.log.Timber
import java.io.File
import java.io.IOException

internal const val HTTP_CACHE_KEY_HEADER = "X-BUY3-SDK-CACHE-KEY"
internal const val HTTP_CACHE_FETCH_STRATEGY_HEADER = "X-BUY3-SDK-CACHE-FETCH-STRATEGY"
internal const val HTTP_CACHE_SERVED_DATE_HEADER = "X-BUY3-SDK-SERVED-DATE"
internal const val HTTP_CACHE_EXPIRE_TIMEOUT_HEADER = "X-BUY3-SDK-EXPIRE-TIMEOUT"

/**
 *
 * Caches HTTP responses to the filesystem so they can be reused.
 * Cached HTTP responses will be stored on the disk with LRU eviction policy.
 */
internal class HttpCache constructor(private val cacheStore: ResponseCacheStore) {

    /**
     * Create new instance of HTTP cache with the provided storage configuration.
     *
     * @param directory a writable directory
     * @param maxSize   the maximum number of bytes this cache should use to store
     */
    constructor(directory: File, maxSize: Long) : this(DiskLruCacheStore(directory, maxSize))

    /**
     * Clear all cached responses ignoring any exceptions.
     */
    fun clear() {
        try {
            cacheStore.delete()
        } catch (e: IOException) {
            Timber.w(e, "failed to clear http cache")
        }
    }

    /**
     * Remove cached response by its cache key, ignoring any exceptions.
     *
     * @param cacheKey cache key
     */
    fun removeQuietly(cacheKey: String) {
        if (cacheKey.isNotBlank()) {
            try {
                cacheStore.remove(cacheKey)
            } catch (e: IOException) {
                Timber.w(e, "failed to remove cached response by key: %s", cacheKey)
            }
        }
    }

    internal fun read(cacheKey: String): Response? {
        var responseCacheRecord: ResponseCacheRecord? = null
        try {
            responseCacheRecord = cacheStore.cacheRecord(cacheKey)
            if (responseCacheRecord == null) {
                return null
            }

            val cacheRecord = responseCacheRecord
            val cacheResponseSource = object : ForwardingSource(responseCacheRecord.bodySource()) {
                @Throws(IOException::class)
                override fun close() {
                    super.close()
                    cacheRecord.closeQuietly()
                }
            }

            val response = ResponseHeaderRecord(responseCacheRecord.headerSource()).response()
            val contentType = response.header("Content-Type")
            val contentLength = response.header("Content-Length")

            return response.newBuilder()
                .body(CacheResponseBody(cacheResponseSource, contentType, contentLength))
                .build()
        } catch (e: Exception) {
            responseCacheRecord.closeQuietly()
            Timber.w(e, "failed to read cached response by key: %s", cacheKey)
            return null
        }
    }

    /**
     * Create [Interceptor] to be injected into [okhttp3.OkHttpClient] application interceptor chain to serve requests from the
     * cache and write responses to the cache.
     *
     * @return [Interceptor] HTTP cache interceptor
     */
    internal fun httpInterceptor(): Interceptor {
        return HttpCacheInterceptor(this)
    }

    internal fun proxyResponse(response: Response, cacheKey: String): Response {
        var cacheRecordEditor: ResponseCacheRecordEditor? = null
        try {
            cacheRecordEditor = cacheStore.cacheRecordEditor(cacheKey)
            if (cacheRecordEditor != null) {
                ResponseHeaderRecord(response).writeTo(cacheRecordEditor)
                return response.newBuilder()
                    .body(ResponseBodyProxy(cacheRecordEditor, response))
                    .build()
            }
        } catch (e: Exception) {
            cacheRecordEditor.abortQuietly()
            Timber.w(e, "failed to proxy response")
        }

        return response
    }
}