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

import com.shopify.graphql.support.AbstractResponse;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static com.shopify.buy3.Utils.checkNotNull;

/**
 * <p>Handler for retrying {@link GraphCall} calls.</p>
 * Encapsulates the retry state and customization parameters for how the {@link GraphCall} will retry subsequent http requests.
 *
 * @see GraphCall#enqueue(GraphCall.Callback, android.os.Handler, RetryHandler)
 */
@SuppressWarnings("unchecked")
public final class RetryHandler {

  /**
   * Handler with no retries configuration.
   */
  public static final RetryHandler NO_RETRY = delay(0, TimeUnit.MILLISECONDS).maxCount(0).build();

  /**
   * Prepares builder with periodic retries strategy.
   *
   * @param delay    between attempts
   * @param timeUnit {@link TimeUnit} time unit
   * @return prepared {@link Builder}
   */
  public static Builder delay(final long delay, @NonNull final TimeUnit timeUnit) {
    return new Builder().delay(delay, timeUnit);
  }

  /**
   * Prepares builder with exponential backoff multiplier strategy.
   *
   * @param delay      minimum delay between attempts
   * @param timeUnit   {@link TimeUnit} time unit
   * @param multiplier exponential backoff multiplier
   * @return prepared {@link Builder}
   */
  public static Builder exponentialBackoff(final long delay, @NonNull final TimeUnit timeUnit, final float multiplier) {
    return new Builder().exponentialBackoff(delay, timeUnit, multiplier);
  }

  private final int maxCount;
  private final long delayBetweenRetriesMs;
  private final float backoffMultiplier;
  private final AtomicInteger retryAttempt = new AtomicInteger();
  private final Condition<GraphResponse> responseRetryCondition;
  private final Condition<GraphError> errorRetryCondition;

  private RetryHandler(final Builder builder) {
    this.maxCount = builder.maxCount == -1 ? Integer.MAX_VALUE : builder.maxCount;
    this.delayBetweenRetriesMs = builder.delayBetweenRetriesMs;
    this.backoffMultiplier = builder.backoffMultiplier;
    this.responseRetryCondition = builder.responseRetryCondition;
    this.errorRetryCondition = builder.errorRetryCondition;
  }

  /**
   * Checks if {@link GraphCall} call should be retried for provided {@code GraphResponse} and max attempt count hasn't been reached.
   *
   * @param response to check if retry is required for
   * @return {@code true} if {@link GraphCall} call should be retried, {@code false} otherwise
   */
  public boolean retry(final GraphResponse response) {
    return responseRetryCondition.check(response) && retry();
  }

  /**
   * Checks if {@link GraphCall} call should be retried for provided {@link GraphError} and max attempt count hasn't been reached.
   *
   * @param error to check if retry is required for
   * @return {@code true} if {@link GraphCall} call should be retried, {@code false} otherwise
   */
  public boolean retry(final GraphError error) {
    return errorRetryCondition.check(error) && retry();
  }

  private boolean retry() {
    int attempt = retryAttempt.get();
    while (attempt < maxCount && !retryAttempt.compareAndSet(attempt, attempt + 1)) {
      attempt = retryAttempt.get();
    }
    return attempt < maxCount;
  }

  long nextRetryDelayMs() {
    return Math.max((long) (delayBetweenRetriesMs * Math.pow(backoffMultiplier, retryAttempt.get())), delayBetweenRetriesMs);
  }

  /**
   * Builds new {@link RetryHandler} instance
   */
  public static final class Builder {
    int maxCount = -1;
    long delayBetweenRetriesMs;
    float backoffMultiplier = 1;
    Condition<GraphResponse> responseRetryCondition = it -> false;
    Condition<GraphError> errorRetryCondition = it -> true;

    private Builder() {
    }

    Builder delay(final long delay, @NonNull final TimeUnit timeUnit) {
      this.delayBetweenRetriesMs = checkNotNull(timeUnit, "timeUnit == null").toMillis(delay);
      return this;
    }

    Builder exponentialBackoff(final long delay, @NonNull final TimeUnit timeUnit, final float multiplier) {
      this.delayBetweenRetriesMs = checkNotNull(timeUnit, "timeUnit == null").toMillis(delay);
      this.backoffMultiplier = Math.max(multiplier, 1f);
      return this;
    }

    /**
     * Sets maximum retry attempt count.
     *
     * @param maxCount maximum retry attempt count
     * @return {@link Builder} to be used for chaining method calls
     */
    public Builder maxCount(final int maxCount) {
      this.maxCount = maxCount;
      return this;
    }

    /**
     * Sets condition on {@link GraphResponse} to check before next retry attempt.
     *
     * @param retryCondition condition to be checked
     * @param <T>            type of the {@link AbstractResponse} response
     * @return {@link Builder} to be used for chaining method calls
     */
    public <T extends AbstractResponse<T>> Builder whenResponse(@NonNull final Condition<GraphResponse<T>> retryCondition) {
      checkNotNull(retryCondition, "retryCondition == null");
      this.responseRetryCondition = (Condition) retryCondition;
      return this;
    }

    /**
     * Sets condition on {@link GraphError} to check before next retry attempt.
     *
     * @param retryCondition condition to be checked
     * @return to be used for chaining method calls
     */
    public Builder whenError(@NonNull final Condition<GraphError> retryCondition) {
      this.errorRetryCondition = checkNotNull(retryCondition, "retryCondition == null");
      return this;
    }

    /**
     * Builds new {@link RetryHandler} instance with provided configuration options.
     *
     * @return {@link RetryHandler}
     */
    public RetryHandler build() {
      return new RetryHandler(this);
    }
  }
}
