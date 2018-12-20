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

import okhttp3.MediaType
import okhttp3.Response
import okhttp3.ResponseBody
import okhttp3.internal.Util.discard
import okhttp3.internal.http.HttpCodec
import okio.Buffer
import okio.BufferedSink
import okio.BufferedSource
import okio.ForwardingSink
import okio.Okio
import okio.Source
import okio.Timeout
import timber.log.Timber
import java.io.IOException
import java.util.concurrent.TimeUnit

internal class ResponseBodyProxy(cacheRecordEditor: ResponseCacheRecordEditor, sourceResponse: Response) : ResponseBody() {
    private val contentType = sourceResponse.header("Content-Type")
    private val contentLength = sourceResponse.header("Content-Length")
    private val responseBodySource = ProxySource(cacheRecordEditor, sourceResponse.body().source())

    override fun contentType(): MediaType? {
        return if (contentType != null) MediaType.parse(contentType) else null
    }

    override fun contentLength(): Long {
        return try {
            contentLength?.toLong() ?: -1
        } catch (e: NumberFormatException) {
            Timber.w(e, "failed to parse content length")
            -1
        }
    }

    override fun source(): BufferedSource = Okio.buffer(responseBodySource)
}

private class ProxySource internal constructor(
    val cacheRecordEditor: ResponseCacheRecordEditor,
    val responseBodySource: Source
) : Source {
    internal val responseBodyCacheSink: ResponseBodyCacheSink
    internal var closed: Boolean = false

    init {
        responseBodyCacheSink = object : ResponseBodyCacheSink(Okio.buffer(cacheRecordEditor.bodySink())) {
            override fun onException(e: Exception) {
                abortCacheQuietly()
                Timber.w(e, "failed to write to cache response sink")
            }
        }
    }

    @Throws(IOException::class)
    override fun read(sink: Buffer, byteCount: Long): Long {
        val bytesRead = try {
            responseBodySource.read(sink, byteCount)
        } catch (e: IOException) {
            if (!closed) {
                closed = true
                abortCacheQuietly()
            }
            throw e
        }

        if (bytesRead == -1L) {
            if (!closed) {
                closed = true
                commitCache()
            }
            return -1
        }

        responseBodyCacheSink.copyFrom(sink, sink.size() - bytesRead, bytesRead)
        return bytesRead
    }

    override fun timeout(): Timeout {
        return responseBodySource.timeout()
    }

    @Throws(IOException::class)
    override fun close() {
        if (closed) return
        closed = true

        if (discard(this, HttpCodec.DISCARD_STREAM_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS)) {
            responseBodySource.close()
            commitCache()
        } else {
            responseBodySource.close()
            abortCacheQuietly()
        }
    }

    private fun commitCache() {
        try {
            responseBodyCacheSink.close()
            cacheRecordEditor.commit()
        } catch (e: Exception) {
            responseBodyCacheSink.closeQuietly()
            abortCacheQuietly()
            Timber.w(e, "failed to commit cache response")
        }
    }

    private fun abortCacheQuietly() {
        responseBodyCacheSink.closeQuietly()
        try {
            cacheRecordEditor.abort()
        } catch (ignore: Exception) {
            // ignore
        }
    }
}

private abstract class ResponseBodyCacheSink(delegate: BufferedSink) : ForwardingSink(delegate) {
    private var failed: Boolean = false

    @Throws(IOException::class)
    override fun write(source: Buffer, byteCount: Long) {
        if (failed) return
        try {
            super.write(source, byteCount)
        } catch (e: Exception) {
            failed = true
            onException(e)
        }
    }

    @Throws(IOException::class)
    override fun flush() {
        if (failed) return
        try {
            super.flush()
        } catch (e: Exception) {
            failed = true
            onException(e)
        }
    }

    @Throws(IOException::class)
    override fun close() {
        if (failed) return
        try {
            super.close()
        } catch (e: Exception) {
            failed = true
            onException(e)
        }
    }

    fun copyFrom(buffer: Buffer, offset: Long, bytesCount: Long) {
        if (failed) return
        try {
            val outSink = delegate() as BufferedSink
            buffer.copyTo(outSink.buffer(), offset, bytesCount)
            outSink.emitCompleteSegments()
        } catch (e: Exception) {
            failed = true
            onException(e)
        }
    }

    abstract fun onException(e: Exception)
}