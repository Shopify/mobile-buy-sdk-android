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

import com.shopify.buy3.HttpCachePolicy
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.Response
import okhttp3.internal.Util
import okhttp3.internal.http.HttpDate
import okio.Buffer
import java.io.Closeable
import java.io.IOException
import java.util.Date

internal val RequestBody.cacheKey: String
    get() {
        return try {
            val hashBuffer = Buffer()
            writeTo(hashBuffer)
            hashBuffer.readByteString().md5().hex()
        } catch (e: Exception) {
            ""
        }
    }

internal fun Response?.strip(): Response? {
    return this?.body()?.let {
        newBuilder().body(null).networkResponse(null).cacheResponse(null).build()
    } ?: this
}

@Throws(IOException::class)
internal fun Response.withServedDateHeader(): Response {
    return newBuilder()
        .addHeader(HTTP_CACHE_SERVED_DATE_HEADER, HttpDate.format(Date()))
        .build()
}

internal val Request.shouldSkipCache: Boolean
    get() {
        val cacheKey = header(HTTP_CACHE_KEY_HEADER)
        return (fetchStrategy == null
                || cacheKey == null
                || cacheKey.isEmpty())
    }

internal val Request.shouldSkipNetwork: Boolean
    get() {
        return fetchStrategy === HttpCachePolicy.FetchStrategy.CACHE_ONLY
    }

internal val Request.isNetworkFirst: Boolean
    get() {
        return fetchStrategy === HttpCachePolicy.FetchStrategy.NETWORK_ONLY || fetchStrategy === HttpCachePolicy.FetchStrategy.NETWORK_FIRST
    }

internal val Request.unsatisfiableCacheRequest: Response
    get() {
        return Response.Builder()
            .request(this)
            .protocol(Protocol.HTTP_1_1)
            .code(504)
            .message("Unsatisfiable Request (cache-only)")
            .body(Util.EMPTY_RESPONSE)
            .sentRequestAtMillis(-1L)
            .receivedResponseAtMillis(System.currentTimeMillis())
            .build()
    }

internal fun isStale(request: Request, response: Response): Boolean {
    if (request.fetchStrategy === HttpCachePolicy.FetchStrategy.CACHE_ONLY) {
        return false
    }

    val timeoutStr = request.header(HTTP_CACHE_EXPIRE_TIMEOUT_HEADER)
    val servedDateStr = response.header(HTTP_CACHE_SERVED_DATE_HEADER)
    if (servedDateStr == null || timeoutStr == null) {
        return true
    }

    val timeout = timeoutStr.toLong()
    val servedDate = HttpDate.parse(servedDateStr)

    val now = System.currentTimeMillis()
    return servedDate == null || now - servedDate.time > timeout
}

internal fun Response?.closeQuietly() {
    try {
        this?.close()
    } catch (ignore: Exception) {
        // ignore
    }
}

internal fun ResponseCacheRecord?.closeQuietly() {
    try {
        this?.close()
    } catch (ignore: Exception) {
        // ignore
    }
}

internal fun ResponseCacheRecordEditor?.abortQuietly() {
    try {
        this?.abort()
    } catch (ignore: Exception) {
        // ignore
    }
}

internal fun Closeable?.closeQuietly() {
    try {
        this?.close()
    } catch (ignored: Exception) {
        // ignore
    }
}

private val Request.fetchStrategy: HttpCachePolicy.FetchStrategy?
    get() {
        val fetchStrategyHeader = header(HTTP_CACHE_FETCH_STRATEGY_HEADER)
        if (fetchStrategyHeader == null || fetchStrategyHeader.isEmpty()) {
            return null
        }
        return HttpCachePolicy.FetchStrategy.values().find { it.name == fetchStrategyHeader }
    }