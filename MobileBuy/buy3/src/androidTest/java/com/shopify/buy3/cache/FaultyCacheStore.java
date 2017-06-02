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

package com.shopify.buy3.cache;

import android.support.annotation.NonNull;

import java.io.File;
import java.io.IOException;

import okhttp3.internal.cache.DiskLruCache;
import okhttp3.internal.io.FileSystem;
import okio.Buffer;
import okio.Sink;
import okio.Source;
import okio.Timeout;

class FaultyCacheStore implements ResponseCacheStore {
  private static final int VERSION = 99991;
  private static final int ENTRY_HEADERS = 0;
  private static final int ENTRY_BODY = 1;
  private static final int ENTRY_COUNT = 2;

  private DiskLruCache cache;
  private final FaultySource faultySource = new FaultySource();
  private final FaultySink faultySink = new FaultySink();
  private FailStrategy failStrategy;

  FaultyCacheStore(FileSystem fileSystem) {
    this.cache = DiskLruCache.create(fileSystem, new File("/cache/"), VERSION, ENTRY_COUNT, Integer.MAX_VALUE);
  }

  @Override public ResponseCacheRecord cacheRecord(@NonNull String cacheKey) throws IOException {
    final DiskLruCache.Snapshot snapshot = cache.get(cacheKey);
    if (snapshot == null) {
      return null;
    }

    return new ResponseCacheRecord() {
      @NonNull @Override public Source headerSource() {
        if (failStrategy == FailStrategy.FAIL_HEADER_READ) {
          return faultySource;
        } else {
          return snapshot.getSource(ENTRY_HEADERS);
        }
      }

      @NonNull @Override public Source bodySource() {
        if (failStrategy == FailStrategy.FAIL_BODY_READ) {
          return faultySource;
        } else {
          return snapshot.getSource(ENTRY_BODY);
        }
      }

      @Override public void close() {
        snapshot.close();
      }
    };
  }

  @Override public ResponseCacheRecordEditor cacheRecordEditor(@NonNull String cacheKey) throws IOException {
    final DiskLruCache.Editor editor = cache.edit(cacheKey);
    if (editor == null) {
      return null;
    }

    return new ResponseCacheRecordEditor() {
      @NonNull @Override public Sink headerSink() {
        if (failStrategy == FailStrategy.FAIL_HEADER_WRITE) {
          return faultySink;
        } else {
          return editor.newSink(ENTRY_HEADERS);
        }
      }

      @NonNull @Override public Sink bodySink() {
        if (failStrategy == FailStrategy.FAIL_BODY_WRITE) {
          return faultySink;
        } else {
          return editor.newSink(ENTRY_BODY);
        }
      }

      @Override public void abort() throws IOException {
        editor.abort();
      }

      @Override public void commit() throws IOException {
        editor.commit();
      }
    };
  }

  @Override public void delete() throws IOException {
    cache.delete();
  }

  @Override public void remove(@NonNull String cacheKey) throws IOException {
    cache.remove(cacheKey);
  }

  void failStrategy(FailStrategy failStrategy) {
    this.failStrategy = failStrategy;
  }

  enum FailStrategy {
    FAIL_HEADER_READ,
    FAIL_BODY_READ,
    FAIL_HEADER_WRITE,
    FAIL_BODY_WRITE
  }

  private static class FaultySource implements Source {
    @Override public long read(Buffer sink, long byteCount) throws IOException {
      throw new IOException("failed to read");
    }

    @Override public Timeout timeout() {
      return new Timeout();
    }

    @Override public void close() throws IOException {

    }
  }

  private static class FaultySink implements Sink {
    @Override public void write(Buffer source, long byteCount) throws IOException {
      throw new IOException("failed to write");
    }

    @Override public void flush() throws IOException {
    }

    @Override public Timeout timeout() {
      return new Timeout();
    }

    @Override public void close() throws IOException {
    }
  }
}
