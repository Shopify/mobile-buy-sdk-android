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
package com.shopify.buy.dataprovider;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.functions.Func1;

/**
 * Provides network request retry policy that can be chained to any Rx API calls via {@link Observable#retryWhen(Func1)}
 */
final class NetworkRetryPolicyProvider {

    final int retryMaxCount;

    final long retryDelayMs;

    final float retryBackoffMultiplier;

    NetworkRetryPolicyProvider(final int retryMaxCount, final long retryDelayMs, final float retryBackoffMultiplier) {
        this.retryMaxCount = retryMaxCount;
        this.retryDelayMs = retryDelayMs;
        this.retryBackoffMultiplier = retryBackoffMultiplier;
    }

    Func1<Observable<? extends Throwable>, Observable<?>> provide() {
        return new NetworkErrorRetryPolicy(retryMaxCount, retryDelayMs, retryBackoffMultiplier);
    }

    private static class NetworkErrorRetryPolicy implements Func1<Observable<? extends Throwable>, Observable<?>> {

        private final long delayBeforeRetryMs;

        private final float backoffMultiplier;

        private volatile int retryAttempt;

        private volatile long nextAttemptDelay;

        NetworkErrorRetryPolicy(final int maxRetryCount, final long delayMs, final float backoffMultiplier) {
            this.retryAttempt = maxRetryCount;
            this.delayBeforeRetryMs = delayMs;
            this.backoffMultiplier = backoffMultiplier;
        }

        @Override
        public Observable<?> call(Observable<? extends Throwable> failedAttempt) {
            return failedAttempt.flatMap(
                    new Func1<Throwable, Observable<?>>() {
                        @Override
                        public Observable<?> call(Throwable t) {
                            Observable<?> resultObservable = Observable.error(t);
                            if (t instanceof IOException) {
                                if (--retryAttempt >= 0) {
                                    nextAttemptDelay = Math.max((long) (backoffMultiplier * nextAttemptDelay), delayBeforeRetryMs);
                                    resultObservable = Observable.timer(nextAttemptDelay, TimeUnit.MILLISECONDS);
                                }
                            }
                            return resultObservable;
                        }
                    });
        }
    }
}
