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
 * <p>A call to the {@code GraphQL} server.</p>
 * Represents {@code GraphQL} operation request that has been prepared for execution. Since this class represents a single
 * request/response pair, it can't be executed twice. To execute the request again, use the {@link GraphCall#clone()} method which
 * creates a new identical call object.
 * <p>A call can be canceled.</p>
 *
 * @param <T> subtype of the {@link AbstractResponse} response object this call serves.
 */
public interface GraphCall<T extends AbstractResponse<T>> {

  /**
   * Cancels this call if possible.
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
   * @return {@link GraphCall} cloned call
   */
  @NonNull GraphCall<T> clone();

  /**
   * Sends {@code GraphQL} operation request immediately and blocks until the response can be processed or is an error.
   *
   * @return {@link GraphResponse} response of the execution {@code GraphQL} operation
   * @throws IllegalStateException when the call has already been executed
   * @throws GraphError            if the request could not be executed
   * @see GraphCallCanceledError
   * @see GraphNetworkError
   * @see GraphHttpError
   * @see GraphParseError
   */
  @NonNull GraphResponse<T> execute() throws GraphError;

  /**
   * Schedules this call to be executed at some point in the future.
   *
   * @param callback {@link GraphCall.Callback} to handle the response or a failure
   * @return {@link GraphCall} that has been scheduled for execution
   * @throws IllegalStateException when the call has already been executed
   * @see GraphCall.Callback
   */
  @NonNull GraphCall<T> enqueue(@NonNull Callback<T> callback);

  /**
   * Schedules this call to be executed at some point in the future.
   *
   * @param callback        {@link GraphCall.Callback} to handle the response or a failure
   * @param callbackHandler the callback will be running on the thread to which this handler is attached
   * @return {@link GraphCall} that has been scheduled for execution
   * @throws IllegalStateException when the call has already been executed
   * @see GraphCall.Callback
   */
  @NonNull GraphCall<T> enqueue(@NonNull Callback<T> callback, @Nullable Handler callbackHandler);

  /**
   * Schedules this call to be executed at some point in the future with the provided retry handler to repeat subsequent http requests.
   * Can be used for polling {@code GraphQL} resource that is not ready yet.
   *
   * @param callback     {@link GraphCall.Callback} to handle the response or a failure
   * @param handler      the callback will be running on the thread to which this handler is attached
   * @param retryHandler http request retry policy
   * @return {@link GraphCall} that has been scheduled for execution
   * @throws IllegalStateException when the call has already been executed
   * @see GraphCall.Callback
   * @see RetryHandler
   */
  @NonNull GraphCall<T> enqueue(@NonNull Callback<T> callback, @Nullable Handler handler, @NonNull RetryHandler retryHandler);

  /**
   * <p>Callback for {@link GraphCall}</p>
   * Callback used to handle response of the scheduled for execution call.
   *
   * @param <T> subtype of the {@link AbstractResponse} response object this callback serves
   */
  interface Callback<T extends AbstractResponse<T>> {

    /**
     * Called when {@code GraphQL} operation response is received and parsed successfully.
     *
     * @param response {@link GraphResponse} parsed {@code GraphQL} operation response
     */
    void onResponse(@NonNull GraphResponse<T> response);

    /**
     * Called when {@code GraphQL} call could not be executed because of the error.
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
