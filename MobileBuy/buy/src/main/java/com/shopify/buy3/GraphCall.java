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

/**
 * <p> {@code GraphCall} is an abstraction for a {@code GraphQL} request that has been prepared for execution. Since this class represents a
 * single request/response pair, it can't be executed twice. A call can be canceled.</p>
 * <p>To execute the request again, use the {@link GraphCall#clone()} method which creates a new identical call object.</p>
 *
 * @param <T> type of the {@link AbstractResponse} response this call returns
 */
public interface GraphCall<T extends AbstractResponse<T>> {

  /**
   * Cancels the call, if possible.
   */
  void cancel();

  /**
   * Checks if this call has been canceled.
   *
   * @return {@code true} if this call has been canceled, otherwise {@code false}
   */
  boolean isCanceled();

  /**
   * Creates a new, identical call to this one which can be enqueued or executed even if this call has already been executed or canceled.
   *
   * @return new cloned call
   */
  @NonNull GraphCall<T> clone();

  /**
   * Sends the {@code GraphQL} request immediately and blocks until the response can be processed or is an error.
   *
   * @return {@code GraphQL} response
   * @throws IllegalStateException when the call has already been executed
   * @throws GraphError            if the request could not be executed due to cancellation, timeouts, network failure, parsing error, etc.
   * @see GraphCallCanceledError
   * @see GraphNetworkError
   * @see GraphHttpError
   * @see GraphParseError
   */
  @NonNull GraphResponse<T> execute() throws GraphError;

  /**
   * Schedules the call to be executed at some point in the future.
   *
   * @param callback which will handle the response or a failure exception
   * @return call that has been scheduled for execution
   * @throws IllegalStateException when the call has already been executed
   * @see Callback
   */
  @NonNull GraphCall<T> enqueue(@NonNull Callback<T> callback);

  /**
   * Schedules the call to be executed at some point in the future.
   *
   * @param callback        which will handle the response or a failure exception
   * @param callbackHandler the callback will be run on the thread to which this handler is attached
   * @return call that has been scheduled for execution
   * @throws IllegalStateException when the call has already been executed
   * @see Callback
   */
  @NonNull GraphCall<T> enqueue(@NonNull Callback<T> callback, @Nullable Handler callbackHandler);

  /**
   * Schedules the call to be executed at some point in the future.
   *
   * @param callback     which will handle the response or a failure exception
   * @param handler      the callback will be run on the thread to which this handler is attached
   * @param retryHandler request retry policy
   * @return call that has been scheduled for execution
   * @throws IllegalStateException when the call has already been executed
   * @see Callback
   * @see RetryHandler
   */
  @NonNull GraphCall<T> enqueue(@NonNull Callback<T> callback, @Nullable Handler handler, @NonNull RetryHandler retryHandler);

  /**
   * Callback used for notifying response of {@code GraphQL} call that has been scheduled for execution.
   *
   * @param <T> type of the {@link AbstractResponse} response this callback serves
   */
  interface Callback<T extends AbstractResponse<T>> {

    /**
     * Called when {@code GraphQL} response is received and parsed successfully.
     *
     * @param response parsed {@code GraphQL} response
     */
    void onResponse(@NonNull GraphResponse<T> response);

    /**
     * Called when {@code GraphQL} call could not be executed.
     * Possible reasons for failure include cancellation, timeouts, network failure, and parsing error.
     *
     * @param error {@link GraphError} that has been thrown during call execution
     * @see GraphCallCanceledError
     * @see GraphNetworkError
     * @see GraphHttpError
     * @see GraphParseError
     */
    void onFailure(@NonNull GraphError error);
  }
}
