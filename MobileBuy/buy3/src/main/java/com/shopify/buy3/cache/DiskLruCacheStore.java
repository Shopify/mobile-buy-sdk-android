package com.shopify.buy3.cache;

import android.support.annotation.NonNull;

import java.io.File;
import java.io.IOException;

import okhttp3.internal.cache.DiskLruCache;
import okhttp3.internal.io.FileSystem;
import okio.Sink;
import okio.Source;

final class DiskLruCacheStore implements ResponseCacheStore {
  private static final int VERSION = 99991;
  private static final int ENTRY_HEADERS = 0;
  private static final int ENTRY_BODY = 1;
  private static final int ENTRY_COUNT = 2;

  private final DiskLruCache cache;

  DiskLruCacheStore(@NonNull final File directory, final long maxSize) {
    this.cache = DiskLruCache.create(FileSystem.SYSTEM, directory, VERSION, ENTRY_COUNT, maxSize);
  }

  DiskLruCacheStore(@NonNull final FileSystem fileSystem, @NonNull final File directory, final long maxSize) {
    this.cache = DiskLruCache.create(fileSystem, directory, VERSION, ENTRY_COUNT, maxSize);
  }

  @Override public ResponseCacheRecord cacheRecord(@NonNull final String cacheKey) throws IOException {
    final DiskLruCache.Snapshot snapshot = cache.get(cacheKey);
    if (snapshot == null) {
      return null;
    }

    return new ResponseCacheRecord() {
      @NonNull @Override public Source headerSource() {
        return snapshot.getSource(ENTRY_HEADERS);
      }

      @NonNull @Override public Source bodySource() {
        return snapshot.getSource(ENTRY_BODY);
      }

      @Override public void close() {
        snapshot.close();
      }
    };
  }

  @Override public ResponseCacheRecordEditor cacheRecordEditor(@NonNull final String cacheKey) throws IOException {
    final DiskLruCache.Editor editor = cache.edit(cacheKey);
    if (editor == null) {
      return null;
    }

    return new ResponseCacheRecordEditor() {
      @NonNull @Override public Sink headerSink() {
        return editor.newSink(ENTRY_HEADERS);
      }

      @NonNull @Override public Sink bodySink() {
        return editor.newSink(ENTRY_BODY);
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

  @Override public void remove(@NonNull final String cacheKey) throws IOException {
    cache.remove(cacheKey);
  }
}
