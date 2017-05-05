package com.shopify.buy3.cache;

import android.support.annotation.NonNull;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

import static com.shopify.buy3.cache.HttpCache.CACHE_KEY_HEADER;
import static com.shopify.buy3.cache.Utils.isNetworkFirst;
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
      //TODO log me
      //logger.d("Skip cache for request: %s", request);
      return chain.proceed(request);
    }

    if (shouldSkipNetwork(request)) {
      //TODO log me
      //logger.d("Cache only for request: %s", request);
      return cacheOnlyResponse(request);
    }

    String cacheKey = request.header(CACHE_KEY_HEADER);
    if (isNetworkFirst(request)) {
      //TODO log me
      //logger.d("Network first for request: %s", request);
      return networkFirst(request, chain, cacheKey);
    } else {
      //TODO log me
      //logger.d("Cache first for request: %s", request);
      return cacheFirst(request, chain, cacheKey);
    }
  }

  private Response cacheOnlyResponse(@NonNull final Request request) throws IOException {
    String cacheKey = request.header(CACHE_KEY_HEADER);
    Response cacheResponse = cache.read(cacheKey);
    if (cacheResponse != null) {
      logCacheHit(request, cacheKey);
      return cacheResponse.newBuilder()
        .cacheResponse(strip(cacheResponse))
        .build();
    }
    logCacheMiss(request, cacheKey);
    return unsatisfiableCacheRequest(request);
  }

  private Response networkFirst(@NonNull final Request request, @NonNull final Chain chain, @NonNull final String cacheKey)
    throws IOException {
    Response networkResponse = withServedDateHeader(chain.proceed(request));
    if (networkResponse.isSuccessful()) {
      //TODO log me
      //logger.d("Network success, skip cache for request: %s, with cache key: %s", request, cacheKey);
      return cache.proxyResponse(networkResponse, cacheKey);
    }

    Response cacheResponse = cache.read(cacheKey);
    if (cacheResponse == null) {
      logCacheMiss(request, cacheKey);
      return networkResponse;
    } else {
      logCacheHit(request, cacheKey);
      return cacheResponse.newBuilder()
        .cacheResponse(strip(cacheResponse))
        .networkResponse(strip(networkResponse))
        .request(request)
        .build();
    }
  }

  private Response cacheFirst(@NonNull final Request request, @NonNull final Chain chain, @NonNull final String cacheKey)
    throws IOException {
    Response cacheResponse = cache.read(cacheKey);
    if (cacheResponse == null) {
      logCacheMiss(request, cacheKey);
      Response networkResponse = withServedDateHeader(chain.proceed(request));
      if (networkResponse.isSuccessful()) {
        return cache.proxyResponse(networkResponse, cacheKey);
      }
      return networkResponse;
    } else {
      logCacheHit(request, cacheKey);
      return cacheResponse.newBuilder()
        .cacheResponse(strip(cacheResponse))
        .request(request)
        .build();
    }
  }

  private void logCacheHit(@NonNull final Request request, @NonNull final String cacheKey) {
    //TODO log me
    //logger.d("Cache HIT for request: %s, with cache key: %s", request, cacheKey);
  }

  private void logCacheMiss(@NonNull final Request request, @NonNull final String cacheKey) {
    //TODO log me
    //logger.d("Cache MISS for request: %s, with cache key: %s", request, cacheKey);
  }
}
