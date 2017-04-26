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

import okhttp3.Response;

/**
 * Thrown when GraphQL call executed but HTTP response status code is not from {@code 200} series
 */
public class GraphHttpError extends GraphError {
  private final int code;
  private final String message;

  GraphHttpError(@NonNull final Response rawResponse) {
    super(formatMessage(rawResponse));
    this.code = rawResponse.code();
    this.message = rawResponse.message();
  }

  /**
   * Return HTTP status code.
   *
   * @return http status code
   */
  public int code() {
    return code;
  }

  /**
   * Return HTTP status message.
   *
   * @return http status message
   */
  public String message() {
    return message;
  }

  private static String formatMessage(Response response) {
    return "HTTP " + response.code() + " " + response.message();
  }
}
