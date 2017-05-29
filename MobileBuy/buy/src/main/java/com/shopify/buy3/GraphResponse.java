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
 * <p>A {@code GraphQL} operation response.</p>
 * Response received as a result of {@link QueryGraphCall} or {@link MutationGraphCall} execution.
 * <p><b>Note: {@link GraphResponse#data} and {@link GraphResponse#errors} are not mutually exclusively.</b></p>
 *
 * @param <T> subtype of parsed {@link AbstractResponse} data. Can be either {@link Storefront.QueryRoot} or {@link Storefront.Mutation}
 */
public final class GraphResponse<T extends AbstractResponse<T>> {
  private final T data;
  private final List<Error> errors;

  GraphResponse(final T data, final List<Error> errors) {
    this.data = data;
    this.errors = errors != null ? errors : Collections.emptyList();
  }

  /**
   * Returns parsed response data.
   * It can be either {@link Storefront.QueryRoot} or {@link Storefront.Mutation} for {@link QueryGraphCall} and
   * {@link MutationGraphCall} calls respectively.
   *
   * @return parsed {@code GraphQL} response data, can be either {@link Storefront.QueryRoot} or {@link Storefront.Mutation}
   */
  @Nullable public T data() {
    return data;
  }

  /**
   * Returns {@code GraphQL} operation errors.
   * <p>These errors are not meant to be displayed to the end-user. <b>They are for debug purposes only</b>.</p>
   *
   * @return {@code GraphQL} operation errors
   */
  @NonNull public List<Error> errors() {
    return errors;
  }

  /**
   * Checks if returned response had any {@code GraphQL} operation errors.
   *
   * @return {@code true} if response has errors, {@code false} otherwise
   */
  public boolean hasErrors() {
    return !errors.isEmpty();
  }

  /**
   * Returns formatted {@code GraphQL} operation errors as one message.
   *
   * @return formatted {@code GraphQL} operation error message
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
