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

package com.shopify.sample.util;

import android.support.v4.util.Pair;

import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.Scheduler;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

import static com.shopify.sample.util.Util.checkNotNull;

public final class RxRetryHandler {
  private static final long DEFAULT_DELAY = TimeUnit.MILLISECONDS.toMillis(400);

  public static Builder delay(final long delay, @NonNull final TimeUnit timeUnit) {
    return new Builder().delay(delay, timeUnit);
  }

  public static Builder exponentialBackoff(final long delay, @NonNull final TimeUnit timeUnit, final float multiplier) {
    return new Builder().exponentialBackoff(delay, timeUnit, multiplier);
  }

  public static final class Builder {
    private int maxRetries;
    private Predicate<? super Throwable> throwablePredicate = (Predicate<Throwable>) throwable -> true;
    private Scheduler scheduler = Schedulers.computation();
    private Flowable<Long> delayPublisher = Flowable.just(DEFAULT_DELAY).repeat();

    private Builder() {
    }

    public Builder maxRetries(final int maxRetries) {
      this.maxRetries = maxRetries;
      return this;
    }

    public Builder when(final Predicate<? super Throwable> predicate) {
      this.throwablePredicate = checkNotNull(predicate, "predicate == null");
      return this;
    }

    public Builder scheduler(@NonNull final Scheduler scheduler) {
      this.scheduler = checkNotNull(scheduler, "scheduler == null");
      return this;
    }

    Builder delay(final long delay, @NonNull final TimeUnit timeUnit) {
      checkNotNull(timeUnit, "timeUnit == null");
      this.delayPublisher = Flowable.just(timeUnit.toMillis(delay)).repeat();
      return this;
    }

    Builder exponentialBackoff(final long delay, @NonNull final TimeUnit timeUnit, final float multiplier) {
      checkNotNull(timeUnit, "timeUnit == null");
      delayPublisher = Flowable.range(1, Integer.MAX_VALUE)
        .map(attempt -> Math.round(Math.pow(multiplier, attempt - 1) * timeUnit.toMillis(delay)));
      return this;
    }

    public Function<Flowable<? extends Throwable>, Flowable<Object>> build() {
      if (maxRetries >= 0) {
        delayPublisher = delayPublisher.take(maxRetries);
      }
      return source -> source
        .zipWith(delayPublisher.concatWith(Flowable.just(-1L)), Pair::create)
        .flatMap(it -> {
          if (throwablePredicate.test(it.first)) {
            return Flowable.just(it);
          } else {
            return Flowable.error(it.first);
          }
        })
        .flatMap(it -> {
          if (it.second <= 0) {
            return Flowable.error(it.first);
          } else {
            return Flowable.timer(it.second, TimeUnit.MILLISECONDS, scheduler);
          }
        });
    }
  }

  private RxRetryHandler() {
  }
}
