package com.shopify.buy3.cache;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.io.File;
import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;
import okio.ForwardingSource;
import okio.Source;

import static com.shopify.buy3.cache.Utils.abortQuietly;
import static com.shopify.buy3.cache.Utils.checkNotNull;
import static com.shopify.buy3.cache.Utils.closeQuietly;

/**
 * <p>Represents http request / response cache.</p>
 * Caches http responses on the disk using DiskLRU model.
 */
@SuppressWarnings("WeakerAccess")
public final class HttpCache {
  public static final String CACHE_KEY_HEADER = "X-BUY3-SDK-CACHE-KEY";
  public static final String CACHE_FETCH_STRATEGY_HEADER = "X-BUY3-SDK-CACHE-FETCH-STRATEGY";
  public static final String CACHE_SERVED_DATE_HEADER = "X-BUY3-SDK-SERVED-DATE";
  public static final String CACHE_EXPIRE_TIMEOUT_HEADER = "X-BUY3-SDK-EXPIRE-TIMEOUT";

  private final ResponseCacheStore cacheStore;

  public HttpCache(@NonNull final File directory, long maxSize) {
    this(new DiskLruCacheStore(checkNotNull(directory, "directory == null"), maxSize));
  }

  HttpCache(@NonNull final ResponseCacheStore cacheStore) {
    this.cacheStore = cacheStore;
  }

  /**
   * Clear all cached http responses.
   */
  public void clear() {
    try {
      cacheStore.delete();
    } catch (IOException e) {
      //TODO log me
      //logger.e(e, "Failed to clear http cache");
    }
  }

  /**
   * Remove cached response by cache key.
   *
   * @param cacheKey cache key
   */
  public void removeQuietly(@NonNull final String cacheKey) {
    try {
      cacheStore.remove(cacheKey);
    } catch (IOException e) {
      //TODO log me
      //logger.e(e, "Failed to clear http cache");
    }
  }

  @Nullable Response read(@NonNull final String cacheKey) {
    ResponseCacheRecord responseCacheRecord = null;
    try {
      responseCacheRecord = cacheStore.cacheRecord(cacheKey);
      if (responseCacheRecord == null) {
        return null;
      }

      final ResponseCacheRecord cacheRecord = responseCacheRecord;
      Source cacheResponseSource = new ForwardingSource(responseCacheRecord.bodySource()) {
        @Override public void close() throws IOException {
          super.close();
          closeQuietly(cacheRecord);
        }
      };

      Response response = new ResponseHeaderRecord(responseCacheRecord.headerSource()).response();
      String contentType = response.header("Content-Type");
      String contentLength = response.header("Content-Length");

      return response.newBuilder()
        .body(new CacheResponseBody(cacheResponseSource, contentType, contentLength))
        .build();
    } catch (Exception e) {
      closeQuietly(responseCacheRecord);
      //TODO log me
      //logger.e(e, "Failed to read http cache entry for key: %s", cacheKey);
      return null;
    }
  }

  public Interceptor httpInterceptor() {
    return new HttpCacheInterceptor(this);
  }

  /**
   * Return cache key for request body, md5 hash from string representation of request body.
   *
   * @param requestBody cache key to be resolved of
   * @return md5 hash of request body
   */
  public static String cacheKey(@NonNull final RequestBody requestBody) {
    try {
      Buffer hashBuffer = new Buffer();
      requestBody.writeTo(hashBuffer);
      return hashBuffer.readByteString().md5().hex();
    } catch (Exception e) {
      //TODO log me
      return "";
    }
  }

  Response proxyResponse(@NonNull final Response response, @NonNull final String cacheKey) {
    ResponseCacheRecordEditor cacheRecordEditor = null;
    try {
      cacheRecordEditor = cacheStore.cacheRecordEditor(cacheKey);
      if (cacheRecordEditor != null) {
        new ResponseHeaderRecord(response).writeTo(cacheRecordEditor);
        return response.newBuilder()
          .body(new ResponseBodyProxy(cacheRecordEditor, response))
          .build();
      }
    } catch (Exception e) {
      abortQuietly(cacheRecordEditor);
      //TODO log me
      //logger.e(e, "Failed to proxy http response for key: %s", cacheKey);
    }
    return response;
  }
}
