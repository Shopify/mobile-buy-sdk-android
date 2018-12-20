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
package com.shopify.buy3

import com.shopify.graphql.support.AbstractResponse
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicInteger

typealias Predicate<T> = (value: T) -> Boolean

/**
 * Handler for retrying [GraphCall] calls.
 *
 * Encapsulates the retry state and customization parameters for how the [GraphCall] will retry subsequent HTTP requests.
 *
 * @see GraphCall.enqueue
 */
class RetryHandler<T : AbstractResponse<T>> private constructor(
    private val maxCount: Int,
    private val delayBetweenRetriesMs: Long,
    private val backoffMultiplier: Float,
    private val shouldRetry: Predicate<GraphCallResult<T>>
) {
    private val retryAttempt = AtomicInteger()

    /**
     * Checks if [GraphCall] call should be retried for provided [GraphCallResult] and max attempt count hasn't been reached.
     *
     * @param result to check if retry is required for
     * @return `true` if [GraphCall] call should be retried, `false` otherwise
     */
    fun retry(result: GraphCallResult<T>): Boolean {
        if (shouldRetry(result)) {
            var attempt = retryAttempt.get()
            while (attempt < maxCount && !retryAttempt.compareAndSet(attempt, attempt + 1)) {
                attempt = retryAttempt.get()
            }
            return attempt < maxCount
        }
        return false
    }

    /**
     * Calculated delay in ms for the next retry attempt.
     */
    val nextRetryDelayMs: Long
        get() {
            return Math.max(
                (delayBetweenRetriesMs * Math.pow(backoffMultiplier.toDouble(), retryAttempt.get().toDouble())).toLong(),
                delayBetweenRetriesMs
            )
        }

    /**
     * Builds new [RetryHandler] instance.
     */
    class Builder<T : AbstractResponse<T>> private constructor(
        private val delayBetweenRetries: Long,
        private val timeUnit: TimeUnit
    ) {
        private var maxCount: Int = -1
        private var backoffMultiplier: Float = 1f
        private var shouldRetry: Predicate<GraphCallResult<T>> = { it is GraphCallResult.Failure }

        /**
         * Sets the maximum retry attempt count.
         *
         * @param count maximum retry attempt count
         */
        fun maxAttempts(count: Int) {
            this.maxCount = count
        }

        /**
         * Sets exponential backoff multiplier.
         *
         * @param multiplier exponential backoff multiplier
         */
        fun exponentialBackoff(multiplier: Float) {
            this.backoffMultiplier = multiplier
        }

        /**
         * Sets condition on [GraphCallResult] to check before next retry attempt.
         *
         * @param shouldRetry predicated to be checked
        </T> */
        fun retryWhen(shouldRetry: Predicate<GraphCallResult<T>>) {
            this.shouldRetry = shouldRetry
        }

        /**
         * Builds new [RetryHandler] instance with provided configuration options.
         *
         * @return [RetryHandler] configured retry handler
         */
        fun build(): RetryHandler<T> {
            return RetryHandler(
                maxCount = if (maxCount == -1) Integer.MAX_VALUE else maxCount,
                delayBetweenRetriesMs = timeUnit.toMillis(delayBetweenRetries),
                backoffMultiplier = backoffMultiplier,
                shouldRetry = shouldRetry
            )
        }

        companion object {
            internal inline fun <T : AbstractResponse<T>> create(
                delay: Long,
                timeUnit: TimeUnit,
                crossinline configure: Builder<T>.() -> Unit
            ): Builder<T> {
                return Builder<T>(delayBetweenRetries = delay, timeUnit = timeUnit).also(configure)
            }
        }
    }

    companion object {

        /**
         * Handler with no retries configuration.
         */
        fun <T : AbstractResponse<T>> noRetry(): RetryHandler<T> {
            return Builder.create<T>(delay = 0, timeUnit = TimeUnit.MILLISECONDS, configure = {
                maxAttempts(0)
            }).build()
        }

        /**
         * Prepares builder for retry handler.
         *
         * @param delay  between attempts
         * @param timeUnit [TimeUnit] time unit
         * @param configure function to configure optional parameters
         * @return prepared [Builder]
         */
        fun <T : AbstractResponse<T>> build(delay: Long, timeUnit: TimeUnit, configure: Builder<T>.() -> Unit): RetryHandler<T> {
            return Builder.create(delay = delay, timeUnit = timeUnit, configure = configure).build()
        }
    }
}