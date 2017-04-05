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

@SuppressWarnings("unchecked")
public final class RetryHandler {
  public static RetryHandler.Builder builder() {
    return new Builder();
  }

  public static RetryHandler simple(final int maxCount, final long delay, @NonNull final TimeUnit timeUnit, final float backoffMultiplier) {
    return new Builder()
      .maxCount(maxCount)
      .delayBetweenRetries(delay, timeUnit)
      .backoffMultiplier(backoffMultiplier)
      .build();
  }

  private static final RetryHandler NO_RETRY = builder()
    .maxCount(0)
    .responseRetryCondition(response -> false)
    .errorRetryCondition(error -> false)
    .build();

  static <T extends AbstractResponse> RetryHandler noRetry() {
    return NO_RETRY;
  }

  private final int maxCount;
  private final long delayBetweenRetriesMs;
  private final float backoffMultiplier;
  private final AtomicInteger retryAttempt = new AtomicInteger();
  private final Condition<GraphResponse> responseRetryCondition;
  private final Condition<GraphError> errorRetryCondition;

  private RetryHandler(final Builder builder) {
    this.maxCount = builder.maxCount;
    this.delayBetweenRetriesMs = builder.delayBetweenRetriesMs;
    this.backoffMultiplier = builder.backoffMultiplier;
    this.responseRetryCondition = builder.responseRetryCondition;
    this.errorRetryCondition = builder.errorRetryCondition;
  }

  public boolean retry(final GraphResponse response) {
    return responseRetryCondition != null && responseRetryCondition.check(response) && retry();
  }

  public boolean retry(final GraphError error) {
    return (errorRetryCondition == null || errorRetryCondition.check(error)) && retry();
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

  public interface Condition<T> {
    boolean check(T t);
  }

  public static final class Builder {
    private static final int DEFAULT_MAX_COUNT = 3;
    private static final long DEFAULT_DELAY_BETWEEN_RETRIES = TimeUnit.MILLISECONDS.toMillis(200);
    private static final float DEFAULT_BACKOFF_MULTIPLIER = 1.5f;

    int maxCount = DEFAULT_MAX_COUNT;
    long delayBetweenRetriesMs = DEFAULT_DELAY_BETWEEN_RETRIES;
    float backoffMultiplier = DEFAULT_BACKOFF_MULTIPLIER;
    Condition<GraphResponse> responseRetryCondition;
    Condition<GraphError> errorRetryCondition;

    public Builder() {
    }

    public Builder maxCount(final int maxCount) {
      this.maxCount = Math.max(maxCount, 0);
      return this;
    }

    public Builder delayBetweenRetries(final long delay, @NonNull final TimeUnit timeUnit) {
      this.delayBetweenRetriesMs = Math.max(timeUnit.toMillis(delay), DEFAULT_DELAY_BETWEEN_RETRIES);
      return this;
    }

    public Builder backoffMultiplier(final float backoffMultiplier) {
      this.backoffMultiplier = Math.max(backoffMultiplier, 1f);
      return this;
    }

    public <T extends AbstractResponse<T>> Builder responseRetryCondition(@NonNull final Condition<GraphResponse<T>> retryCondition) {
      checkNotNull(retryCondition, "retryCondition == null");
      this.responseRetryCondition = (Condition) retryCondition;
      return this;
    }

    public Builder errorRetryCondition(@NonNull final Condition<GraphError> retryCondition) {
      this.errorRetryCondition = checkNotNull(retryCondition, "retryCondition == null");
      return this;
    }

    public RetryHandler build() {
      return new RetryHandler(this);
    }
  }
}
