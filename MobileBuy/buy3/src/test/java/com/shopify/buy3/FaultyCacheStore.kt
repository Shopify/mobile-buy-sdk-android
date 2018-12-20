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

import com.shopify.buy3.internal.cache.ResponseCacheRecord
import com.shopify.buy3.internal.cache.ResponseCacheRecordEditor
import com.shopify.buy3.internal.cache.ResponseCacheStore
import okhttp3.internal.cache.DiskLruCache
import okhttp3.internal.io.FileSystem
import okio.Buffer
import okio.Sink
import okio.Source
import okio.Timeout
import java.io.File
import java.io.IOException

private const val VERSION = 99991
private const val ENTRY_HEADERS = 0
private const val ENTRY_BODY = 1
private const val ENTRY_COUNT = 2

internal class FaultyCacheStore(fileSystem: FileSystem) : ResponseCacheStore {
    private val cache: DiskLruCache = DiskLruCache.create(fileSystem, File("/cache/"),
        VERSION,
        ENTRY_COUNT, Integer.MAX_VALUE.toLong())
    private val faultySource = FaultySource()
    private val faultySink = FaultySink()
    private var failStrategy: FailStrategy? = null

    override fun cacheRecord(cacheKey: String): ResponseCacheRecord? {
        val snapshot = cache.get(cacheKey) ?: return null

        return object : ResponseCacheRecord {
            override fun headerSource(): Source {
                return if (failStrategy == FailStrategy.FAIL_HEADER_READ) {
                    faultySource
                } else {
                    snapshot.getSource(ENTRY_HEADERS)
                }
            }

            override fun bodySource(): Source {
                return if (failStrategy == FailStrategy.FAIL_BODY_READ) {
                    faultySource
                } else {
                    snapshot.getSource(ENTRY_BODY)
                }
            }

            override fun close() {
                snapshot.close()
            }
        }
    }

    override fun cacheRecordEditor(cacheKey: String): ResponseCacheRecordEditor? {
        val editor = cache.edit(cacheKey) ?: return null

        return object : ResponseCacheRecordEditor {
            override fun headerSink(): Sink {
                return if (failStrategy == FailStrategy.FAIL_HEADER_WRITE) {
                    faultySink
                } else {
                    editor.newSink(ENTRY_HEADERS)
                }
            }

            override fun bodySink(): Sink {
                return if (failStrategy == FailStrategy.FAIL_BODY_WRITE) {
                    faultySink
                } else {
                    editor.newSink(ENTRY_BODY)
                }
            }

            override fun abort() {
                editor.abort()
            }

            override fun commit() {
                editor.commit()
            }
        }
    }

    override fun delete() {
        cache.delete()
    }

    override fun remove(cacheKey: String) {
        cache.remove(cacheKey)
    }

    fun failStrategy(failStrategy: FailStrategy) {
        this.failStrategy = failStrategy
    }

    internal enum class FailStrategy {
        FAIL_HEADER_READ,
        FAIL_BODY_READ,
        FAIL_HEADER_WRITE,
        FAIL_BODY_WRITE,
        NO_FAIL
    }

    private class FaultySource : Source {
        @Throws(IOException::class)
        override fun read(sink: Buffer, byteCount: Long): Long {
            throw IOException("failed to read")
        }

        override fun timeout(): Timeout {
            return Timeout()
        }

        @Throws(IOException::class)
        override fun close() {

        }
    }

    private class FaultySink : Sink {
        @Throws(IOException::class)
        override fun write(source: Buffer, byteCount: Long) {
            throw IOException("failed to write")
        }

        @Throws(IOException::class)
        override fun flush() {
        }

        override fun timeout(): Timeout {
            return Timeout()
        }

        @Throws(IOException::class)
        override fun close() {
        }
    }
}