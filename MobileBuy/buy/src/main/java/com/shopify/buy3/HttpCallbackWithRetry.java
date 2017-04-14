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

import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Response;

import static com.shopify.buy3.Utils.checkNotNull;

final class HttpCallbackWithRetry<T extends AbstractResponse<T>> implements okhttp3.Callback {
  private volatile Call httpCall;
  private final HttpResponseParser<T> httpResponseParser;
  private final RetryHandler retryHandler;
  private final GraphCall.Callback<T> graphCallback;
  private final ScheduledExecutorService dispatcher;
  private final Handler handler;
  private volatile ScheduledFuture<Void> scheduledFuture;

  HttpCallbackWithRetry(@NonNull final Call httpCall, @NonNull final HttpResponseParser<T> httpResponseParser,
    @Nullable final RetryHandler retryHandler, @NonNull final GraphCall.Callback<T> graphCallback,
    @NonNull final ScheduledExecutorService dispatcher, @Nullable final Handler handler) {
    this.httpCall = checkNotNull(httpCall, "httpCall == null");
    this.httpResponseParser = checkNotNull(httpResponseParser, "httpResponseParser == null");
    this.retryHandler = checkNotNull(retryHandler, "retryHandler == null");
    this.graphCallback = checkNotNull(graphCallback, "graphCallback == null");
    this.dispatcher = checkNotNull(dispatcher, "dispatcher == null");
    this.handler = handler;
  }

  @Override public void onFailure(final Call call, final IOException e) {
    handleError(new GraphNetworkError("Failed to execute GraphQL http request", e));
  }

  @Override public void onResponse(final Call call, final Response response) throws IOException {
    GraphResponse<T> graphResponse;
    try {
      graphResponse = httpResponseParser.parse(response);
    } catch (GraphError e) {
      handleError(e);
      return;
    }

    try {
      if (retryHandler.retry(graphResponse)) {
        schedule();
        return;
      }
    } catch (Exception e) {
      notifyError(new GraphError("Failed to reschedule GraphQL query execution", e));
    }

    notifyResponse(graphResponse);
  }

  private void handleError(final GraphError error) {
    if (retryHandler.retry(error)) {
      schedule();
    } else {
      notifyError(error);
    }
  }

  private void schedule() {
    scheduledFuture = dispatcher.schedule(new Callable<Void>() {
      @Override public Void call() throws Exception {
        httpCall = httpCall.clone();
        httpCall.enqueue(HttpCallbackWithRetry.this);
        return null;
      }
    }, retryHandler.nextRetryDelayMs(), TimeUnit.MILLISECONDS);
  }

  private void notifyResponse(final GraphResponse<T> response) {
    if (httpCall.isCanceled()) {
      return;
    }

    Runnable action = () -> graphCallback.onResponse(response);
    if (handler != null) {
      handler.post(action);
    } else {
      action.run();
    }
  }

  private void notifyError(final GraphError error) {
    if (httpCall.isCanceled()) {
      return;
    }

    Runnable action = () -> {
      if (error instanceof GraphNetworkError) {
        graphCallback.onNetworkError((GraphNetworkError) error);
      } else if (error instanceof GraphInvalidResponseError) {
        graphCallback.onInvalidResponseError((GraphInvalidResponseError) error);
      } else if (error instanceof GraphParseError) {
        graphCallback.onParseError((GraphParseError) error);
      } else {
        graphCallback.onFailure(error);
      }
    };
    if (handler != null) {
      handler.post(action);
    } else {
      action.run();
    }
  }

  void cancel() {
    ScheduledFuture<Void> scheduledFuture = this.scheduledFuture;
    if (scheduledFuture != null) {
      scheduledFuture.cancel(true);
    }

    httpCall.cancel();
  }
}
