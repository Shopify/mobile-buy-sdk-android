package com.shopify.buy3;

import com.shopify.graphql.support.AbstractResponse;
import com.shopify.graphql.support.SchemaViolationError;
import com.shopify.graphql.support.TopLevelResponse;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

final class OperationResponseCallback<T extends AbstractResponse<T>> implements Callback {
  private final ResponseParser responseParser;
  private final ResponseConverter<T> responseConverter;
  private final OperationCallback<T> callback;

  public OperationResponseCallback(final ResponseParser responseParser, final ResponseConverter<T> responseConverter,
                                   final OperationCallback<T> callback) {
    this.responseParser = responseParser;
    this.responseConverter = responseConverter;
    this.callback = callback;
  }

  @Override
  public void onFailure(final Call call, final IOException e) {
    callback.onError();
  }

  @Override
  public void onResponse(final Call call, final Response response) throws IOException {
    try {
      TopLevelResponse topLevelResponse = responseParser.parse(response);
      if (topLevelResponse.getErrors() == null || topLevelResponse.getErrors().isEmpty()) {
        T operationResponse = responseConverter.convert(topLevelResponse);
        callback.onResponse(operationResponse);
      } else {
        callback.onError();
      }
    } catch (Exception e) {
      callback.onError();
    }
  }

  interface ResponseParser {
    TopLevelResponse parse(Response response) throws IOException;
  }

  interface ResponseConverter<R extends AbstractResponse<R>> {
    R convert(TopLevelResponse response) throws SchemaViolationError;
  }
}
