package com.shopify.buy3;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import okhttp3.Response;

public final class GraphError extends Exception {
  static GraphError networkError(final Response rawResponse, final Throwable cause) {
    return new GraphError(Type.NETWORK, rawResponse, cause);
  }

  static GraphError invalidResponseError(final Response rawResponse, final Throwable cause) {
    return new GraphError(Type.INVALID_RESPONSE, rawResponse, cause);
  }

  static GraphError parseError(final Response rawResponse, final Throwable cause) {
    return new GraphError(Type.PARSE, rawResponse, cause);
  }

  private final Type type;
  private final Response rawResponse;

  private GraphError(final Type type, final Response rawResponse, final Throwable cause) {
    super(cause);
    this.rawResponse = rawResponse;
    this.type = type;
  }

  @NonNull public Type type() {
    return type;
  }

  @Nullable public Response rawResponse() {
    return rawResponse;
  }

  public enum Type {
    NETWORK,
    INVALID_RESPONSE,
    PARSE
  }
}
