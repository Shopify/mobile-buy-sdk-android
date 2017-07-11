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
import java.util.concurrent.atomic.AtomicReference;

import okhttp3.Call;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.shopify.buy3.Utils.checkNotNull;

@SuppressWarnings("WeakerAccess")
abstract class RealGraphCall<T extends AbstractResponse<T>> implements GraphCall<T>, Cloneable {
  static final String ACCEPT_HEADER = "application/json";
  static final MediaType GRAPHQL_MEDIA_TYPE = MediaType.parse("application/graphql; charset=utf-8");

  protected final Query query;
  protected final HttpUrl serverUrl;
  protected final Call.Factory httpCallFactory;
  protected final HttpResponseParser<T> httpResponseParser;
  protected final ScheduledExecutorService dispatcher;
  protected final HttpCachePolicy.Policy httpCachePolicy;
  protected final HttpCache httpCache;
  protected final AtomicBoolean executed = new AtomicBoolean();
  private volatile Call httpCall;
  private volatile HttpCallbackWithRetry httpCallbackWithRetry;
  private volatile boolean canceled;
  private CallbackProxy<T> responseCallback;

  RealGraphCall(final Query query, final HttpUrl serverUrl, final Call.Factory httpCallFactory,
    final ResponseDataConverter<T> responseDataConverter, final ScheduledExecutorService dispatcher,
    final HttpCachePolicy.Policy httpCachePolicy, final HttpCache httpCache) {
    this.query = query;
    this.serverUrl = serverUrl;
    this.httpCallFactory = httpCallFactory;
    this.httpResponseParser = new HttpResponseParser<>(responseDataConverter);
    this.dispatcher = dispatcher;
    this.httpCachePolicy = httpCachePolicy;
    this.httpCache = httpCache;
  }

  RealGraphCall(final RealGraphCall<T> other) {
    this.query = other.query;
    this.serverUrl = other.serverUrl;
    this.httpCallFactory = other.httpCallFactory;
    this.httpResponseParser = other.httpResponseParser;
    this.dispatcher = other.dispatcher;
    this.httpCachePolicy = other.httpCachePolicy;
    this.httpCache = other.httpCache;
  }

  @Override public void cancel() {
    canceled = true;

    CallbackProxy<T> callbackProxy = responseCallback;
    if (callbackProxy != null) {
      callbackProxy.cancel();
    }

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
        removeCachedResponse();
      }
    } catch (Exception rethrow) {
      if (rethrow instanceof GraphParseError) {
        removeCachedResponse();
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

    responseCallback = new CallbackProxy<T>(this, callback, handler);

    dispatcher.execute(() -> {
      if (canceled) {
        if (handler != null) {
          handler.post(() -> responseCallback.cancel());
        } else {
          responseCallback.cancel();
        }
        return;
      }

      httpCall = httpCall();
      httpCallbackWithRetry = new HttpCallbackWithRetry<>(httpCall, httpResponseParser, retryHandler, responseCallback, dispatcher,
        handler);
      httpCall.enqueue(httpCallbackWithRetry);
    });

    return this;
  }

  @NonNull @Override public abstract GraphCall<T> clone();

  private Call httpCall() {
    RequestBody body = RequestBody.create(GRAPHQL_MEDIA_TYPE, query.toString());
    String cacheKey = httpCache != null ? HttpCache.cacheKey(body) : "";
    Request request = new Request.Builder()
      .url(serverUrl)
      .post(body)
      .header("Accept", ACCEPT_HEADER)
      .header(HttpCache.CACHE_KEY_HEADER, cacheKey)
      .header(HttpCache.CACHE_FETCH_STRATEGY_HEADER, httpCachePolicy.fetchStrategy.name())
      .header(HttpCache.CACHE_EXPIRE_TIMEOUT_HEADER, String.valueOf(httpCachePolicy.expireTimeoutMs()))
      .build();
    return httpCallFactory.newCall(request);
  }

  private void checkIfCanceled() throws GraphCallCanceledError {
    if (canceled) {
      throw new GraphCallCanceledError();
    }
  }

  private void removeCachedResponse() {
    String cacheKey = httpCall != null ? httpCall.request().header(HttpCache.CACHE_KEY_HEADER) : null;
    if (httpCache == null || cacheKey == null || cacheKey.isEmpty()) {
      return;
    }

    httpCache.removeQuietly(cacheKey);
  }

  interface ResponseDataConverter<R extends AbstractResponse<R>> {
    R convert(TopLevelResponse response) throws SchemaViolationError;
  }

  private static class CallbackProxy<T extends AbstractResponse<T>> implements Callback<T> {
    final RealGraphCall<T> graphCall;
    final AtomicReference<Callback<T>> originalCallbackRef;
    final Handler handler;

    CallbackProxy(final RealGraphCall<T> graphCall, final Callback<T> originalCallback, final Handler handler) {
      this.graphCall = graphCall;
      this.originalCallbackRef = new AtomicReference<>(originalCallback);
      this.handler = handler;
    }

    @Override public void onResponse(@NonNull final GraphResponse<T> response) {
      if (response.hasErrors()) {
        graphCall.removeCachedResponse();
      }

      Callback<T> originalCallback = originalCallbackRef.get();
      if (originalCallback == null || !originalCallbackRef.compareAndSet(originalCallback, null)) {
        return;
      }
      originalCallback.onResponse(response);
    }

    @Override public void onFailure(@NonNull final GraphError error) {
      if (error instanceof GraphParseError) {
        graphCall.removeCachedResponse();
      }

      Callback<T> originalCallback = originalCallbackRef.get();
      if (originalCallback != null && originalCallbackRef.compareAndSet(originalCallback, null)) {
        originalCallback.onFailure(error);
      }
    }

    void cancel() {
      Callback<T> originalCallback = originalCallbackRef.getAndSet(null);
      if (originalCallback != null) {
        if (handler != null) {
          handler.post(() -> originalCallback.onFailure(new GraphCallCanceledError()));
        } else {
          originalCallback.onFailure(new GraphCallCanceledError());
        }
      }
    }
  }
}
