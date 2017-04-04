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

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import okhttp3.Response;

public final class GraphError extends Exception {
  static GraphError runtimeError(final Throwable cause) {
    return new GraphError(Type.NETWORK, null, cause);
  }

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
    RUNTIME,
    NETWORK,
    INVALID_RESPONSE,
    PARSE
  }

  @Override public String toString() {
    return "GraphError{" +
      "type=" + type +
      ", rawResponse=" + rawResponse +
      '}';
  }
}
