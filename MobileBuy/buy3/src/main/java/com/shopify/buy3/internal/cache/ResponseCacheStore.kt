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

import okio.Sink
import okio.Source
import java.io.IOException

internal interface ResponseCacheStore {
    @Throws(IOException::class)
    fun cacheRecord(cacheKey: String): ResponseCacheRecord?

    @Throws(IOException::class)
    fun cacheRecordEditor(cacheKey: String): ResponseCacheRecordEditor?

    @Throws(IOException::class)
    fun remove(cacheKey: String)

    @Throws(IOException::class)
    fun delete()
}

internal interface ResponseCacheRecord {
    fun headerSource(): Source

    fun bodySource(): Source

    fun close()
}

internal interface ResponseCacheRecordEditor {
    fun headerSink(): Sink

    fun bodySink(): Sink

    @Throws(IOException::class)
    fun abort()

    @Throws(IOException::class)
    fun commit()
}
