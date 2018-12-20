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

import java.util.concurrent.TimeUnit

/**
 * Cache policies for [QueryGraphCall] HTTP request / response cache.
 *
 * Defines different strategies to fetch HTTP response from the cache store and validate its expiration date.
 *
 * @see HttpCachePolicy.CACHE_ONLY
 * @see HttpCachePolicy.NETWORK_ONLY
 * @see HttpCachePolicy.CACHE_FIRST
 * @see HttpCachePolicy.NETWORK_FIRST
 */
interface HttpCachePolicy {

    /**
     * Fetch response strategy.
     */
    val fetchStrategy: FetchStrategy

    /**
     * Cache policy with provided expiration configuration.
     */
    interface ExpirePolicy : HttpCachePolicy {

        /**
         * Cached response expiration timeout in milliseconds.
         */
        val expireTimeoutMs: Long

        /**
         * Creates new cache policy that's will expire after timeout. Cached response is treated as expired if its
         * served date exceeds expireTimeout.
         *
         * @param expireTimeout  expire timeout after which cached response is treated as expired
         * @param expireTimeUnit [TimeUnit] time unit
         * @return new cache policy
         */
        fun expireAfter(expireTimeout: Long, expireTimeUnit: TimeUnit): ExpirePolicy
    }

    object Default {
        /**
         * Fetch response from cache only without loading from network.
         */
        @JvmField
        val CACHE_ONLY: ExpirePolicy = HttpCachePolicyImpl(FetchStrategy.CACHE_ONLY, 0, null)

        /**
         * Fetch response from network without loading from cache.
         */
        @JvmField
        val NETWORK_ONLY: HttpCachePolicy = HttpCachePolicyImpl(FetchStrategy.NETWORK_ONLY, 0, null)

        /**
         * Fetch response from cache first, if response is missing load from network.
         */
        @JvmField
        val CACHE_FIRST: ExpirePolicy = HttpCachePolicyImpl(FetchStrategy.CACHE_FIRST, 0, null)

        /**
         * Fetch response from network first, but fallback to cached data if the request fails.
         */
        @JvmField
        val NETWORK_FIRST: ExpirePolicy = HttpCachePolicyImpl(FetchStrategy.NETWORK_FIRST, 0, null)
    }

    /**
     * Fetch response strategy.
     */
    enum class FetchStrategy {
        CACHE_ONLY,
        NETWORK_ONLY,
        CACHE_FIRST,
        NETWORK_FIRST
    }
}

private class HttpCachePolicyImpl internal constructor(
    override val fetchStrategy: HttpCachePolicy.FetchStrategy,
    internal val expireTimeout: Long,
    internal val expireTimeUnit: TimeUnit?
) : HttpCachePolicy, HttpCachePolicy.ExpirePolicy {

    override val expireTimeoutMs: Long
        get() = expireTimeUnit?.toMillis(expireTimeout) ?: 0

    override fun expireAfter(expireTimeout: Long, expireTimeUnit: TimeUnit): HttpCachePolicy.ExpirePolicy {
        return HttpCachePolicyImpl(fetchStrategy, expireTimeout, expireTimeUnit)
    }
}