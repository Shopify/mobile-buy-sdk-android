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

import com.shopify.graphql.support.AbstractResponse;
import com.shopify.graphql.support.Error;

import java.util.Collections;
import java.util.List;

/**
 * Represents parsed response returned by the {@code GraphQL} server.
 *
 * @param <T> type of parsed {@code GraphQL} response data
 */
public final class GraphResponse<T extends AbstractResponse<T>> {
  private final T data;
  private final List<Error> errors;

  GraphResponse(final T data, final List<Error> errors) {
    this.data = data;
    this.errors = errors != null ? errors : Collections.emptyList();
  }

  /**
   * Return parsed {@code GraphQL} response data.
   *
   * @return parsed {@code GraphQL} response data
   */
  @Nullable public T data() {
    return data;
  }

  /**
   * Return parsed {@code GraphQL} response errors.
   *
   * @return parsed {@code GraphQL} response errors
   */
  @NonNull public List<Error> errors() {
    return errors;
  }

  /**
   * Check if returned response had any errors
   *
   * @return {@code true} if response has errors, {@code false} otherwise
   */
  public boolean hasErrors() {
    return !errors.isEmpty();
  }

  /**
   * Return formatted {@code GraphQL} error message
   *
   * @return formatted {@code GraphQL} error message
   */
  @NonNull public String formatErrorMessage() {
    StringBuilder message = new StringBuilder();
    boolean first = true;
    for (Error error : errors) {
      if (first) {
        first = false;
      } else {
        message.append("\n");
      }
      message.append(error.message());
    }
    return message.toString();
  }
}
