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

import java.util.concurrent.TimeUnit;

import static com.shopify.buy3.Utils.checkNotNull;

/**
 * <p>Cache policies for {@link QueryGraphCall} HTTP request / response cache.</p>
 * Defines different strategies to fetch HTTP response from the cache store and validate its expiration date.
 *
 * @see HttpCachePolicy#CACHE_ONLY
 * @see HttpCachePolicy#NETWORK_ONLY
 * @see HttpCachePolicy#CACHE_FIRST
 * @see HttpCachePolicy#NETWORK_FIRST
 */
public final class HttpCachePolicy {
  /**
   * Fetch response from cache only without loading from network
   */
  public static final ExpirePolicy CACHE_ONLY = new ExpirePolicy(FetchStrategy.CACHE_ONLY);

  /**
   * Fetch response from network without loading from cache
   */
  public static final Policy NETWORK_ONLY = new Policy(FetchStrategy.NETWORK_ONLY, 0, null);

  /**
   * Fetch response from cache first, if response is missing load from network
   */
  public static final ExpirePolicy CACHE_FIRST = new ExpirePolicy(FetchStrategy.CACHE_FIRST);

  /**
   * Fetch response from network first, but fallback to cached data if the request fails
   */
  public static final ExpirePolicy NETWORK_FIRST = new ExpirePolicy(FetchStrategy.NETWORK_FIRST);

  private HttpCachePolicy() {
  }

  /**
   * Cache policy configurations.
   */
  public static class Policy {
    final FetchStrategy fetchStrategy;
    final long expireTimeout;
    final TimeUnit expireTimeUnit;

    Policy(FetchStrategy fetchStrategy, long expireTimeout, TimeUnit expireTimeUnit) {
      this.fetchStrategy = fetchStrategy;
      this.expireTimeout = expireTimeout;
      this.expireTimeUnit = expireTimeUnit;
    }

    long expireTimeoutMs() {
      if (expireTimeUnit == null) {
        return 0;
      }
      return expireTimeUnit.toMillis(expireTimeout);
    }
  }

  /**
   * Cache policy with provided expiration configuration.
   */
  @SuppressWarnings("WeakerAccess")
  public static final class ExpirePolicy extends Policy {
    ExpirePolicy(FetchStrategy fetchStrategy) {
      super(fetchStrategy, 0, null);
    }

    ExpirePolicy(FetchStrategy fetchStrategy, long expireTimeout, TimeUnit expireTimeUnit) {
      super(fetchStrategy, expireTimeout, expireTimeUnit);
    }

    /**
     * Creates new cache policy that's will expire after timeout. Cached response is treated as expired if its
     * served date exceeds expireTimeout.
     *
     * @param expireTimeout  expire timeout after which cached response is treated as expired
     * @param expireTimeUnit {@link TimeUnit} time unit
     * @return new cache policy
     */
    public ExpirePolicy expireAfter(long expireTimeout, @NonNull TimeUnit expireTimeUnit) {
      return new ExpirePolicy(fetchStrategy, expireTimeout, checkNotNull(expireTimeUnit, "expireTimeUnit == null"));
    }
  }

  public enum FetchStrategy {
    CACHE_ONLY,
    NETWORK_ONLY,
    CACHE_FIRST,
    NETWORK_FIRST
  }
}
