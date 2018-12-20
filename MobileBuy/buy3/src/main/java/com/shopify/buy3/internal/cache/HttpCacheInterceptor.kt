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
import okhttp3.Request
import okhttp3.Response
import timber.log.Timber
import java.io.IOException

internal class HttpCacheInterceptor(private val cache: HttpCache) : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        return when {
            request.shouldSkipCache -> chain.proceed(request)
            request.shouldSkipNetwork -> cacheOnlyResponse(request)
            request.isNetworkFirst -> networkFirst(request, chain)
            else -> cacheFirst(request, chain)
        }
    }

    @Throws(IOException::class)
    private fun cacheOnlyResponse(request: Request): Response {
        val cacheResponse = cachedResponse(request)
        if (cacheResponse == null) {
            logCacheMiss(request)
            return request.unsatisfiableCacheRequest
        }

        logCacheHit(request)
        return cacheResponse.newBuilder()
            .cacheResponse(cacheResponse.strip())
            .build()
    }

    @Throws(IOException::class)
    private fun networkFirst(request: Request, chain: Interceptor.Chain): Response {
        val networkResponse = chain.proceed(request).withServedDateHeader()
        if (networkResponse.isSuccessful) {
            val cacheKey = request.header(HTTP_CACHE_KEY_HEADER)
            return cache.proxyResponse(networkResponse, cacheKey)
        }

        val cacheResponse = cachedResponse(request)
        if (cacheResponse == null) {
            logCacheMiss(request)
            return networkResponse
        }

        logCacheHit(request)
        return cacheResponse.newBuilder()
            .cacheResponse(cacheResponse.strip())
            .networkResponse(networkResponse.strip())
            .request(request)
            .build()
    }

    @Throws(IOException::class)
    private fun cacheFirst(request: Request, chain: Interceptor.Chain): Response {
        val cacheResponse = cachedResponse(request)
        if (cacheResponse == null) {
            logCacheMiss(request)
            val networkResponse = chain.proceed(request).withServedDateHeader()
            if (networkResponse.isSuccessful) {
                val cacheKey = request.header(HTTP_CACHE_KEY_HEADER)
                return cache.proxyResponse(networkResponse, cacheKey)
            }
            return networkResponse
        }

        logCacheHit(request)
        return cacheResponse.newBuilder()
            .cacheResponse(cacheResponse.strip())
            .request(request)
            .build()
    }

    private fun cachedResponse(request: Request): Response? {
        val cacheKey = request.header(HTTP_CACHE_KEY_HEADER)
        if (cacheKey == null || cacheKey.isEmpty()) {
            return null
        }

        val cachedResponse = cache.read(cacheKey) ?: return null

        if (isStale(request, cachedResponse)) {
            cachedResponse.closeQuietly()
            return null
        }

        return cachedResponse
    }

    private fun logCacheHit(request: Request) {
        val cacheKey = request.header(HTTP_CACHE_KEY_HEADER)
        if (cacheKey == null || cacheKey.isEmpty()) {
            return
        }
        Timber.d("cache HIT for key: %s", cacheKey)
    }

    private fun logCacheMiss(request: Request) {
        val cacheKey = request.header(HTTP_CACHE_KEY_HEADER)
        if (cacheKey == null || cacheKey.isEmpty()) {
            return
        }
        Timber.d("cache MISS for key: %s", cacheKey)
    }
}