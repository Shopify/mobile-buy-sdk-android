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

import java.net.HttpURLConnection;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.functions.Func1;

/**
 * Provides polling retry policy that can be chained to any Rx API calls via {@link Observable#retryWhen(Func1)}
 */
final class PollingPolicyProvider {

    final long retryDelayMs;

    final long timeoutMs;

    PollingPolicyProvider(final long retryDelayMs, final long timeoutMs) {
        this.retryDelayMs = retryDelayMs;
        this.timeoutMs = timeoutMs;
    }

    Func1<Observable<? extends Throwable>, Observable<?>> provide() {
        return new PollingPolicy(retryDelayMs, timeoutMs);
    }

    private static final class PollingPolicy implements Func1<Observable<? extends Throwable>, Observable<?>> {

        private final long retryDelayMs;

        private final long timeoutMs;

        private volatile long pollingStartTime = -1;

        PollingPolicy(final long retryDelayMs, final long timeoutMs) {
            this.retryDelayMs = retryDelayMs;
            this.timeoutMs = timeoutMs;
        }

        @Override
        public Observable<?> call(Observable<? extends Throwable> failedAttempt) {

            return failedAttempt.flatMap(

                new Func1<Throwable, Observable<?>>() {
                    @Override
                    public Observable<?> call(final Throwable t) {
                        boolean pollingRequired = false;

                        if (t instanceof RetrofitError) {
                            RetrofitError error = (RetrofitError) t;

                            if (HttpURLConnection.HTTP_ACCEPTED == error.getCode()) {
                                pollingRequired = true;
                            }
                        } else if (t instanceof PollingRequiredException) {
                            pollingRequired = true;
                        }

                        if (pollingRequired) {
                            if (pollingStartTime == -1) {
                                pollingStartTime = System.currentTimeMillis();
                            } else if (System.currentTimeMillis() - pollingStartTime > timeoutMs) {
                                pollingRequired = false;
                            }
                        }

                        if(pollingRequired) {
                            return Observable.timer(retryDelayMs, TimeUnit.MILLISECONDS);
                        } else {
                            return Observable.error(t);
                        }
                    }
                });
        }
    }
}
