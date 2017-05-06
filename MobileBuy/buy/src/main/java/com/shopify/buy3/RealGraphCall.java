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

package com.shopify.buy3;

import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.shopify.buy3.cache.HttpCache;
import com.shopify.graphql.support.AbstractResponse;
import com.shopify.graphql.support.Query;
import com.shopify.graphql.support.SchemaViolationError;
import com.shopify.graphql.support.TopLevelResponse;

import java.io.IOException;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.atomic.AtomicBoolean;

import okhttp3.Call;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.shopify.buy3.Utils.checkNotNull;

abstract class RealGraphCall<T extends AbstractResponse<T>> implements GraphCall<T>, Cloneable {
  static final String ACCEPT_HEADER = "application/json";
  static final MediaType GRAPHQL_MEDIA_TYPE = MediaType.parse("application/graphql; charset=utf-8");

  private final Query query;
  private final HttpUrl serverUrl;
  private final Call.Factory httpCallFactory;
  private final HttpResponseParser<T> httpResponseParser;
  private final ScheduledExecutorService dispatcher;
  private final AtomicBoolean executed = new AtomicBoolean();
  private volatile Call httpCall;
  private volatile HttpCallbackWithRetry httpCallbackWithRetry;
  private volatile boolean canceled;
  CachePolicy cachePolicy;
  private final HttpCache httpCache;

  RealGraphCall(final Query query, final HttpUrl serverUrl, final Call.Factory httpCallFactory,
    final ResponseDataConverter<T> responseDataConverter, final ScheduledExecutorService dispatcher,
    final CachePolicy cachePolicy, final HttpCache httpCache) {
    this.query = query;
    this.serverUrl = serverUrl;
    this.httpCallFactory = httpCallFactory;
    this.httpResponseParser = new HttpResponseParser<>(responseDataConverter);
    this.dispatcher = dispatcher;
    this.cachePolicy = cachePolicy;
    this.httpCache = httpCache;
  }

  RealGraphCall(final RealGraphCall<T> other) {
    this.query = other.query;
    this.serverUrl = other.serverUrl;
    this.httpCallFactory = other.httpCallFactory;
    this.httpResponseParser = other.httpResponseParser;
    this.dispatcher = other.dispatcher;
    this.cachePolicy = other.cachePolicy;
    this.httpCache = other.httpCache;
  }

  @Override public void cancel() {
    canceled = true;

    HttpCallbackWithRetry httpCallbackWithRetry = this.httpCallbackWithRetry;
    if (httpCallbackWithRetry != null) {
      httpCallbackWithRetry.cancel();
    }

    Call httpCall = this.httpCall;
    if (httpCall != null) {
      httpCall.cancel();
    }
  }

  @Override public boolean isCanceled() {
    return canceled;
  }

  @NonNull @Override public GraphResponse<T> execute() throws GraphError {
    if (!executed.compareAndSet(false, true)) {
      throw new IllegalStateException("Already Executed");
    }
    checkIfCanceled();

    Response response;
    try {
      httpCall = httpCall();
      response = httpCall.execute();
    } catch (IOException e) {
      checkIfCanceled();
      throw new GraphNetworkError("Failed to execute GraphQL http request", e);
    }

    GraphResponse<T> graphResponse;
    try {
      graphResponse = httpResponseParser.parse(response);
      if (graphResponse.hasErrors()) {
        removeCachedResponse(httpCall.request());
      }
    } catch (Exception rethrow) {
      if (rethrow instanceof GraphParseError) {
        removeCachedResponse(httpCall.request());
      }
      throw rethrow;
    } finally {
      response.close();
    }

    checkIfCanceled();
    return graphResponse;
  }

  @NonNull @Override public GraphCall<T> enqueue(@NonNull final Callback<T> callback) {
    return enqueue(callback, null, RetryHandler.NO_RETRY);
  }

  @NonNull @Override public GraphCall<T> enqueue(@NonNull final Callback<T> callback, @Nullable final Handler callbackHandler) {
    return enqueue(callback, callbackHandler, RetryHandler.NO_RETRY);
  }

  @NonNull @Override public GraphCall<T> enqueue(@NonNull final Callback<T> callback, @Nullable final Handler handler,
    @NonNull final RetryHandler retryHandler) {
    if (!executed.compareAndSet(false, true)) {
      throw new IllegalStateException("Already Executed");
    }

    checkNotNull(retryHandler, "retryHandler == null");

    if (canceled) {
      callback.onFailure(new GraphCallCanceledError());
      return this;
    }

    final Callback<T> proxyCallBack = new Callback<T>() {
      @Override public void onResponse(@NonNull final GraphResponse<T> response) {
        if (canceled) {
          callback.onFailure(new GraphCallCanceledError());
        } else {
          if (response.hasErrors()) {
            removeCachedResponse(httpCall.request());
          }
          callback.onResponse(response);
        }
      }

      @Override public void onFailure(@NonNull final GraphError error) {
        if (error instanceof GraphParseError) {
          removeCachedResponse(httpCall.request());
        }

        if (canceled) {
          callback.onFailure(new GraphCallCanceledError());
        } else {
          callback.onFailure(error);
        }
      }
    };

    dispatcher.execute(() -> {
      httpCall = httpCall();
      httpCallbackWithRetry = new HttpCallbackWithRetry<>(httpCall, httpResponseParser, retryHandler, proxyCallBack, dispatcher, handler);
      httpCall.enqueue(httpCallbackWithRetry);
    });

    return this;
  }

  @NonNull @Override public abstract GraphCall<T> clone();

  void cachePolicyInternal(@NonNull final CachePolicy cachePolicy) {
    if (executed.get()) throw new IllegalStateException("Already Executed");
    this.cachePolicy = cachePolicy;
  }

  private Call httpCall() {
    RequestBody body = RequestBody.create(GRAPHQL_MEDIA_TYPE, query.toString());
    String cacheKey = httpCache != null ? HttpCache.cacheKey(body) : "";
    Request request = new Request.Builder()
      .url(serverUrl)
      .post(body)
      .header("Accept", ACCEPT_HEADER)
      .header(HttpCache.CACHE_KEY_HEADER, cacheKey)
      .header(HttpCache.CACHE_FETCH_STRATEGY_HEADER, cachePolicy.fetchStrategy.name())
      .header(HttpCache.CACHE_EXPIRE_TIMEOUT_HEADER, String.valueOf(cachePolicy.expireTimeoutMs()))
      .build();
    return httpCallFactory.newCall(request);
  }

  private void checkIfCanceled() throws GraphCallCanceledError {
    if (canceled) {
      throw new GraphCallCanceledError();
    }
  }

  private void removeCachedResponse(@NonNull final Request request) {
    String cacheKey = request.header(HttpCache.CACHE_KEY_HEADER);
    if (httpCache == null || cacheKey == null || cacheKey.isEmpty()) {
      return;
    }

    httpCache.removeQuietly(cacheKey);
  }

  interface ResponseDataConverter<R extends AbstractResponse<R>> {
    R convert(TopLevelResponse response) throws SchemaViolationError;
  }
}
