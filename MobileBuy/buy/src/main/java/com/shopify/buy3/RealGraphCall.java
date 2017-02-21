package com.shopify.buy3;

import android.support.annotation.NonNull;

import com.google.gson.Gson;
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

abstract class RealGraphCall<T extends AbstractResponse<T>> implements GraphCall<T> {
  private static final String ACCEPT_HEADER = "application/json";
  private static final String CONTENT_TYPE_HEADER = "application/graphql";
  private static final MediaType GRAPHQL_MEDIA_TYPE = MediaType.parse("application/graphql; charset=utf-8");

  final Query query;
  final HttpUrl serverUrl;
  final Call.Factory httpCallFactory;
  private volatile Call httpCall;
  private boolean executed;

  RealGraphCall(final Query query, final HttpUrl serverUrl, final Call.Factory httpCallFactory) {
    this.query = query;
    this.serverUrl = serverUrl;
    this.httpCallFactory = httpCallFactory;
  }

  @Override public void cancel() {
    Call call = httpCall;
    if (call != null) {
      call.cancel();
    }
  }

  @NonNull @Override public T execute() throws IOException, SchemaViolationError {
    synchronized (this) {
      if (executed) throw new IllegalStateException("Already Executed");
      executed = true;
    }
    httpCall = httpCall();
    Response response = httpCall.execute();
    TopLevelResponse topLevelResponse = parse(response);
    return responseConverter().convert(topLevelResponse);
  }

  @NonNull @Override public GraphCall<T> enqueue(@NonNull final Callback<T> callback) {
    synchronized (this) {
      if (executed) throw new IllegalStateException("Already Executed");
      executed = true;
    }

    httpCall = httpCall();
    httpCall.enqueue(new okhttp3.Callback() {
      @Override public void onFailure(final Call call, final IOException e) {
        callback.onFailure(e);
      }

      @Override public void onResponse(final Call call, final Response response) throws IOException {
        try {
          TopLevelResponse topLevelResponse = parse(response);
          callback.onResponse(responseConverter().convert(topLevelResponse));
        } catch (Exception e) {
          callback.onFailure(e);
        }
      }
    });

    return this;
  }

  @NonNull abstract ResponseConverter<T> responseConverter();

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

  private TopLevelResponse parse(final Response response) {
    return new Gson().fromJson(response.body().toString(), TopLevelResponse.class);
  }

  interface ResponseConverter<R extends AbstractResponse<R>> {
    R convert(TopLevelResponse response) throws SchemaViolationError;
  }
}
