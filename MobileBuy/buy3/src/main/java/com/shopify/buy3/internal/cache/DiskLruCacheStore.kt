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

import okhttp3.internal.cache.DiskLruCache
import okhttp3.internal.io.FileSystem
import okio.Sink
import okio.Source
import java.io.File
import java.io.IOException

private const val VERSION = 99991
private const val ENTRY_HEADERS = 0
private const val ENTRY_BODY = 1
private const val ENTRY_COUNT = 2

internal class DiskLruCacheStore : ResponseCacheStore {

    private val cache: DiskLruCache

    constructor(directory: File, maxSize: Long) {
        this.cache = DiskLruCache.create(FileSystem.SYSTEM, directory, VERSION, ENTRY_COUNT, maxSize)
    }

    constructor(fileSystem: FileSystem, directory: File, maxSize: Long) {
        this.cache = DiskLruCache.create(fileSystem, directory, VERSION, ENTRY_COUNT, maxSize)
    }

    @Throws(IOException::class)
    override fun cacheRecord(cacheKey: String): ResponseCacheRecord? {
        val snapshot = cache.get(cacheKey) ?: return null
        return object : ResponseCacheRecord {
            override fun headerSource(): Source {
                return snapshot.getSource(ENTRY_HEADERS)
            }

            override fun bodySource(): Source {
                return snapshot.getSource(ENTRY_BODY)
            }

            override fun close() {
                snapshot.close()
            }
        }
    }

    @Throws(IOException::class)
    override fun cacheRecordEditor(cacheKey: String): ResponseCacheRecordEditor? {
        val editor = cache.edit(cacheKey) ?: return null
        return object : ResponseCacheRecordEditor {
            override fun headerSink(): Sink {
                return editor.newSink(ENTRY_HEADERS)
            }

            override fun bodySink(): Sink {
                return editor.newSink(ENTRY_BODY)
            }

            @Throws(IOException::class)
            override fun abort() {
                editor.abort()
            }

            @Throws(IOException::class)
            override fun commit() {
                editor.commit()
            }
        }
    }

    @Throws(IOException::class)
    override fun delete() {
        cache.delete()
    }

    @Throws(IOException::class)
    override fun remove(cacheKey: String) {
        cache.remove(cacheKey)
    }
}