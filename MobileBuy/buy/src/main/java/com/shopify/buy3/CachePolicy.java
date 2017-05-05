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
 * The caching policy defines the method for executing the query.
 * Some policies require an {@code expireTimeout} parameter that determines the time until the cache is invalidated and is considered to
 * be stale.
 *
 * @see CachePolicy#CACHE_ONLY
 * @see CachePolicy#NETWORK_ONLY
 * @see CachePolicy#CACHE_FIRST
 * @see CachePolicy#NETWORK_FIRST
 */
public final class CachePolicy {
  /**
   * Load from cache without loading from network
   */
  public static final NeverExpireFactory CACHE_ONLY = () -> new CachePolicy(FetchStrategy.CACHE_ONLY, 0, null);

  /**
   * Load from network without loading from cache
   */
  public static final NeverExpireFactory NETWORK_ONLY = () -> new CachePolicy(FetchStrategy.NETWORK_ONLY, 0, null);

  /**
   * Load from cache if staleness interval is not exceeded, otherwise load from network
   */
  public static final ExpireTimeoutFactory CACHE_FIRST = (expireTimeout, expireTimeUnit) ->
    new CachePolicy(FetchStrategy.CACHE_FIRST, expireTimeout, checkNotNull(expireTimeUnit, "expireTimeUnit == null"));

  /**
   * Load from network but fallback to cached data if the request fails
   */
  public static final ExpireTimeoutFactory NETWORK_FIRST = (expireTimeout, expireTimeUnit) ->
    new CachePolicy(FetchStrategy.NETWORK_FIRST, expireTimeout, checkNotNull(expireTimeUnit, "expireTimeUnit == null"));

  final FetchStrategy fetchStrategy;
  final long expireTimeout;
  final TimeUnit expireTimeUnit;

  private CachePolicy(final FetchStrategy fetchStrategy, final long expireTimeout, final TimeUnit expireTimeUnit) {
    this.fetchStrategy = fetchStrategy;
    this.expireTimeout = expireTimeout;
    this.expireTimeUnit = expireTimeUnit;
  }

  long expireTimeoutMs() {
    return expireTimeUnit.toMillis(expireTimeout);
  }

  public interface NeverExpireFactory {
    /**
     * Obtain cache policy without expiration timeout
     *
     * @return {@link CachePolicy}
     */
    @NonNull CachePolicy obtain();
  }

  public interface ExpireTimeoutFactory {
    /**
     * Obtain cache policy with expiration timeout
     *
     * @param expireTimeout  expire timeout
     * @param expireTimeUnit {@link TimeUnit} expire timeout unit
     * @return {@link CachePolicy}
     */
    @NonNull CachePolicy obtain(long expireTimeout, @NonNull TimeUnit expireTimeUnit);
  }

  public enum FetchStrategy {
    CACHE_ONLY,
    NETWORK_ONLY,
    CACHE_FIRST,
    NETWORK_FIRST
  }
}
