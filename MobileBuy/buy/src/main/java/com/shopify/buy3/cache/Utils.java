package com.shopify.buy3.cache;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.shopify.buy3.HttpCachePolicy;

import java.io.IOException;
import java.util.Date;

import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.internal.Util;
import okhttp3.internal.http.HttpDate;

import static com.shopify.buy3.cache.HttpCache.CACHE_EXPIRE_TIMEOUT_HEADER;
import static com.shopify.buy3.cache.HttpCache.CACHE_FETCH_STRATEGY_HEADER;
import static com.shopify.buy3.cache.HttpCache.CACHE_KEY_HEADER;
import static com.shopify.buy3.cache.HttpCache.CACHE_SERVED_DATE_HEADER;

final class Utils {
  @Nullable static Response strip(@Nullable final Response response) {
    return response != null && response.body() != null
      ? response.newBuilder().body(null).networkResponse(null).cacheResponse(null).build()
      : response;
  }

  @NonNull static Response withServedDateHeader(@NonNull final Response response) throws IOException {
    return response.newBuilder()
      .addHeader(CACHE_SERVED_DATE_HEADER, HttpDate.format(new Date()))
      .build();
  }

  static boolean shouldSkipCache(@NonNull final Request request) {
    HttpCachePolicy.FetchStrategy fetchStrategy = fetchStrategy(request);
    String cacheKey = request.header(CACHE_KEY_HEADER);
    return fetchStrategy == null
      || cacheKey == null
      || cacheKey.isEmpty();
  }

  static boolean shouldSkipNetwork(@NonNull final Request request) {
    HttpCachePolicy.FetchStrategy fetchStrategy = fetchStrategy(request);
    return fetchStrategy == HttpCachePolicy.FetchStrategy.CACHE_ONLY;
  }

  static boolean isNetworkFirst(@NonNull final Request request) {
    HttpCachePolicy.FetchStrategy fetchStrategy = fetchStrategy(request);
    return fetchStrategy == HttpCachePolicy.FetchStrategy.NETWORK_ONLY
      || fetchStrategy == HttpCachePolicy.FetchStrategy.NETWORK_FIRST;
  }

  @NonNull static Response unsatisfiableCacheRequest(@NonNull final Request request) {
    return new Response.Builder()
      .request(request)
      .protocol(Protocol.HTTP_1_1)
      .code(504)
      .message("Unsatisfiable Request (cache-only)")
      .body(Util.EMPTY_RESPONSE)
      .sentRequestAtMillis(-1L)
      .receivedResponseAtMillis(System.currentTimeMillis())
      .build();
  }

  static boolean isStale(@NonNull final Request request, @NonNull final Response response) {
    if (fetchStrategy(request) == HttpCachePolicy.FetchStrategy.CACHE_ONLY) {
      return false;
    }

    String timeoutStr = request.header(CACHE_EXPIRE_TIMEOUT_HEADER);
    String servedDateStr = response.header(CACHE_SERVED_DATE_HEADER);
    if (servedDateStr == null || timeoutStr == null) {
      return true;
    }

    long timeout = Long.parseLong(timeoutStr);
    Date servedDate = HttpDate.parse(servedDateStr);

    long now = System.currentTimeMillis();
    return servedDate == null || now - servedDate.getTime() > timeout;
  }

  static void closeQuietly(@Nullable final Response response) {
    try {
      if (response != null) {
        response.close();
      }
    } catch (Exception ignore) {
      // ignore
    }
  }

  static void closeQuietly(@Nullable final ResponseCacheRecord cacheRecord) {
    try {
      if (cacheRecord != null) {
        cacheRecord.close();
      }
    } catch (Exception ignore) {
      // ignore
    }
  }

  static void abortQuietly(@Nullable final ResponseCacheRecordEditor cacheRecordEditor) {
    try {
      if (cacheRecordEditor != null) {
        cacheRecordEditor.abort();
      }
    } catch (Exception ignore) {
      // ignore
    }
  }

  static <T> T checkNotNull(T reference, @Nullable Object errorMessage) {
    if (reference == null) {
      throw new NullPointerException(String.valueOf(errorMessage));
    }
    return reference;
  }

  private static HttpCachePolicy.FetchStrategy fetchStrategy(@NonNull final Request request) {
    String fetchStrategyHeader = request.header(CACHE_FETCH_STRATEGY_HEADER);
    if (fetchStrategyHeader == null || fetchStrategyHeader.isEmpty()) {
      return null;
    }

    for (HttpCachePolicy.FetchStrategy fetchStrategy : HttpCachePolicy.FetchStrategy.values()) {
      if (fetchStrategy.name().equals(fetchStrategyHeader)) {
        return fetchStrategy;
      }
    }
    return null;
  }

  private Utils() {
  }
}
