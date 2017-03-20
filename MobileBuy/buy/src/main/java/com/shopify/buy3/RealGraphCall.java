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

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import com.shopify.graphql.support.AbstractResponse;
import com.shopify.graphql.support.Query;
import com.shopify.graphql.support.SchemaViolationError;
import com.shopify.graphql.support.TopLevelResponse;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

final class RealGraphCall<T extends AbstractResponse<T>> implements GraphCall<T> {
  private static final String ACCEPT_HEADER = "application/json";
  private static final String CONTENT_TYPE_HEADER = "application/graphql";
  private static final MediaType GRAPHQL_MEDIA_TYPE = MediaType.parse("application/graphql; charset=utf-8");

  private final Query query;
  private final HttpUrl serverUrl;
  private final Call.Factory httpCallFactory;
  private final ResponseDataConverter<T> responseDataConverter;
  private volatile Call httpCall;
  private boolean executed;

  RealGraphCall(final Query query, final HttpUrl serverUrl, final Call.Factory httpCallFactory,
                final ResponseDataConverter<T> responseDataConverter) {
    this.query = query;
    this.serverUrl = serverUrl;
    this.httpCallFactory = httpCallFactory;
    this.responseDataConverter = responseDataConverter;
  }

  @Override public void cancel() {
    Call call = httpCall;
    if (call != null) {
      call.cancel();
    }
  }

  @SuppressWarnings("CloneDoesntCallSuperClone")
  @NonNull @Override public GraphCall<T> clone() {
    return new RealGraphCall<>(query, serverUrl, httpCallFactory, responseDataConverter);
  }

  @NonNull @Override public GraphResponse<T> execute() throws GraphError {
    synchronized (this) {
      if (executed) throw new IllegalStateException("Already Executed");
      executed = true;
    }
    httpCall = httpCall();

    Response response;
    try {
      response = httpCall.execute();
    } catch (IOException e) {
      throw GraphError.networkError(null, e);
    }

    checkResponse(response);

    return parseResponse(response);
  }

  @NonNull @Override public GraphCall<T> enqueue(@NonNull final Callback<T> callback) {
    return enqueue(callback, null);
  }

  @NonNull @Override public GraphCall<T> enqueue(@NonNull final Callback<T> callback, @Nullable final Handler handler) {
    synchronized (this) {
      if (executed) throw new IllegalStateException("Already Executed");
      executed = true;
    }

    httpCall = httpCall();
    httpCall.enqueue(new okhttp3.Callback() {
      @Override public void onFailure(final Call call, final IOException e) {
        Runnable action = () -> callback.onFailure(GraphError.networkError(null, e));
        if (handler != null) {
          handler.post(action);
        } else {
          action.run();
        }
      }

      @Override public void onResponse(final Call call, final Response response) throws IOException {
        Runnable action = () -> {
          try {
            checkResponse(response);
            callback.onResponse(parseResponse(response));
          } catch (GraphError error) {
            callback.onFailure(error);
          }
        };

        if (handler != null) {
          handler.post(action);
        } else {
          action.run();
        }
      }
    });

    return this;
  }

  private Call httpCall() {
    RequestBody body = RequestBody.create(GRAPHQL_MEDIA_TYPE, query.toString());
    Request request = new Request.Builder()
      .url(serverUrl)
      .post(body)
      .header("Accept", ACCEPT_HEADER)
      .header("Content-Type", CONTENT_TYPE_HEADER)
      .build();
    return httpCallFactory.newCall(request);
  }

  private void checkResponse(final Response response) throws GraphError {
    if (!response.isSuccessful()) {
      throw GraphError.networkError(response, null);
    }
  }

  private GraphResponse<T> parseResponse(final Response response) throws GraphError {
    TopLevelResponse topLevelResponse = parseTopLevelResponse(response);
    try {
      T data = responseDataConverter.convert(topLevelResponse);
      return new GraphResponse<T>(data, topLevelResponse.getErrors());
    } catch (Exception e) {
      throw GraphError.parseError(response, e);
    }
  }

  private TopLevelResponse parseTopLevelResponse(final Response response) throws GraphError {
    try {
      JsonReader reader = new JsonReader(response.body().charStream());
      JsonObject root = (JsonObject) new JsonParser().parse(reader);
      return new TopLevelResponse(root);
    } catch (Exception e) {
      throw GraphError.invalidResponseError(response, e);
    }
  }

  interface ResponseDataConverter<R extends AbstractResponse<R>> {
    R convert(TopLevelResponse response) throws SchemaViolationError;
  }
}
