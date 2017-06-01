package com.shopify.buy3.cache;

import android.support.annotation.NonNull;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import timber.log.Timber;

import static com.shopify.buy3.cache.HttpCache.CACHE_KEY_HEADER;
import static com.shopify.buy3.cache.Utils.closeQuietly;
import static com.shopify.buy3.cache.Utils.isNetworkFirst;
import static com.shopify.buy3.cache.Utils.isStale;
import static com.shopify.buy3.cache.Utils.shouldSkipCache;
import static com.shopify.buy3.cache.Utils.shouldSkipNetwork;
import static com.shopify.buy3.cache.Utils.strip;
import static com.shopify.buy3.cache.Utils.unsatisfiableCacheRequest;
import static com.shopify.buy3.cache.Utils.withServedDateHeader;

final class HttpCacheInterceptor implements Interceptor {
  private final HttpCache cache;

  HttpCacheInterceptor(@NonNull final HttpCache cache) {
    this.cache = cache;
  }

  @Override public Response intercept(final Chain chain) throws IOException {
    Request request = chain.request();
    if (shouldSkipCache(request)) {
      return chain.proceed(request);
    }

    if (shouldSkipNetwork(request)) {
      return cacheOnlyResponse(request);
    }

    if (isNetworkFirst(request)) {
      return networkFirst(request, chain);
    } else {
      return cacheFirst(request, chain);
    }
  }

  private Response cacheOnlyResponse(@NonNull final Request request) throws IOException {
    Response cacheResponse = cachedResponse(request);
    if (cacheResponse == null) {
      logCacheMiss(request);
      return unsatisfiableCacheRequest(request);
    }

    logCacheHit(request);
    return cacheResponse.newBuilder()
      .cacheResponse(strip(cacheResponse))
      .build();
  }

  private Response networkFirst(@NonNull final Request request, @NonNull final Chain chain) throws IOException {
    Response networkResponse = withServedDateHeader(chain.proceed(request));
    if (networkResponse.isSuccessful()) {
      String cacheKey = request.header(CACHE_KEY_HEADER);
      return cache.proxyResponse(networkResponse, cacheKey);
    }

    Response cacheResponse = cachedResponse(request);
    if (cacheResponse == null) {
      logCacheMiss(request);
      return networkResponse;
    }

    logCacheHit(request);
    return cacheResponse.newBuilder()
      .cacheResponse(strip(cacheResponse))
      .networkResponse(strip(networkResponse))
      .request(request)
      .build();
  }

  private Response cacheFirst(@NonNull final Request request, @NonNull final Chain chain) throws IOException {
    Response cacheResponse = cachedResponse(request);
    if (cacheResponse == null) {
      logCacheMiss(request);
      Response networkResponse = withServedDateHeader(chain.proceed(request));
      if (networkResponse.isSuccessful()) {
        String cacheKey = request.header(CACHE_KEY_HEADER);
        return cache.proxyResponse(networkResponse, cacheKey);
      }
      return networkResponse;
    }

    logCacheHit(request);
    return cacheResponse.newBuilder()
      .cacheResponse(strip(cacheResponse))
      .request(request)
      .build();
  }

  private Response cachedResponse(@NonNull final Request request) {
    String cacheKey = request.header(CACHE_KEY_HEADER);
    if (cacheKey == null || cacheKey.isEmpty()) {
      return null;
    }

    Response cachedResponse = cache.read(cacheKey);
    if (cachedResponse == null) {
      return null;
    }

    if (isStale(request, cachedResponse)) {
      closeQuietly(cachedResponse);
      return null;
    }

    return cachedResponse;
  }

  private void logCacheHit(@NonNull final Request request) {
    String cacheKey = request.header(CACHE_KEY_HEADER);
    if (cacheKey == null || cacheKey.isEmpty()) {
      return;
    }
    Timber.d("cache HIT for key: %s", cacheKey);
  }

  private void logCacheMiss(@NonNull final Request request) {
    String cacheKey = request.header(CACHE_KEY_HEADER);
    if (cacheKey == null || cacheKey.isEmpty()) {
      return;
    }
    Timber.d("cache MISS for key: %s", cacheKey);
  }
}
