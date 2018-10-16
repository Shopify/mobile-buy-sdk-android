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

import okhttp3.internal.io.FileSystem
import okio.Sink
import okio.Source
import java.io.File

internal class NoFileSystem : FileSystem {
    override fun source(file: File): Source {
        throw UnsupportedOperationException()
    }

    override fun sink(file: File): Sink {
        throw UnsupportedOperationException()
    }

    override fun appendingSink(file: File): Sink {
        throw UnsupportedOperationException()
    }

    override fun delete(file: File) {
        throw UnsupportedOperationException()
    }

    override fun exists(file: File): Boolean {
        throw UnsupportedOperationException()
    }

    override fun size(file: File): Long {
        throw UnsupportedOperationException()
    }

    override fun rename(from: File, to: File) {
        throw UnsupportedOperationException()
    }

    override fun deleteContents(directory: File) {
        throw UnsupportedOperationException()
    }
}
