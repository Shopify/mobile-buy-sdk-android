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

final class RealGraphCall<T extends AbstractResponse<T>> implements GraphCall<T> {
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

  RealGraphCall(final Query query, final HttpUrl serverUrl, final Call.Factory httpCallFactory,
    final ResponseDataConverter<T> responseDataConverter, final ScheduledExecutorService dispatcher) {
    this.query = query;
    this.serverUrl = serverUrl;
    this.httpCallFactory = httpCallFactory;
    this.httpResponseParser = new HttpResponseParser<>(responseDataConverter);
    this.dispatcher = dispatcher;
  }

  private RealGraphCall(final Query query, final HttpUrl serverUrl, final Call.Factory httpCallFactory,
    final HttpResponseParser<T> httpResponseParser, final ScheduledExecutorService dispatcher) {
    this.query = query;
    this.serverUrl = serverUrl;
    this.httpCallFactory = httpCallFactory;
    this.httpResponseParser = httpResponseParser;
    this.dispatcher = dispatcher;
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

  @SuppressWarnings("CloneDoesntCallSuperClone")
  @NonNull @Override public GraphCall<T> clone() {
    return new RealGraphCall<>(query, serverUrl, httpCallFactory, httpResponseParser, dispatcher);
  }

  @NonNull @Override public GraphResponse<T> execute() throws GraphError {
    if (!executed.compareAndSet(false, true)) {
      throw new IllegalStateException("Already Executed");
    }

    httpCall = httpCall();

    Response response;
    try {
      response = httpCall.execute();
    } catch (IOException e) {
      throw new GraphNetworkError("Failed to execute GraphQL http request", e);
    }

    return httpResponseParser.parse(response);
  }

  @NonNull @Override public GraphCall<T> enqueue(@NonNull final Callback<T> callback) {
    return enqueue(callback, null, RetryHandler.NO_RETRY);
  }

  @NonNull @Override public GraphCall<T> enqueue(@NonNull final Callback<T> callback, @Nullable final Handler handler) {
    return enqueue(callback, handler, RetryHandler.NO_RETRY);
  }

  @NonNull @Override public GraphCall<T> enqueue(@NonNull final Callback<T> callback, @Nullable final Handler handler,
    @NonNull final RetryHandler retryHandler) {
    if (!executed.compareAndSet(false, true)) {
      throw new IllegalStateException("Already Executed");
    }

    checkNotNull(retryHandler, "retryHandler == null");

    dispatcher.execute(() -> {
      httpCall = httpCall();
      httpCallbackWithRetry = new HttpCallbackWithRetry<>(httpCall, httpResponseParser, retryHandler, callback, dispatcher, handler);
      httpCall.enqueue(httpCallbackWithRetry);
    });
    return this;
  }

  private Call httpCall() {
    RequestBody body = RequestBody.create(GRAPHQL_MEDIA_TYPE, query.toString());
    Request request = new Request.Builder()
      .url(serverUrl)
      .post(body)
      .header("Accept", ACCEPT_HEADER)
      .build();
    return httpCallFactory.newCall(request);
  }

  interface ResponseDataConverter<R extends AbstractResponse<R>> {
    R convert(TopLevelResponse response) throws SchemaViolationError;
  }
}
