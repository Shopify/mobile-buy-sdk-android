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

import com.shopify.buy3.cache.HttpCache;

import java.util.concurrent.ScheduledExecutorService;

import okhttp3.Call;
import okhttp3.HttpUrl;

import static com.shopify.buy3.Utils.checkNotNull;

final class RealQueryGraphCall extends RealGraphCall<Storefront.QueryRoot> implements QueryGraphCall {

  RealQueryGraphCall(final Storefront.QueryRootQuery query, final HttpUrl serverUrl, final Call.Factory httpCallFactory,
    final ScheduledExecutorService dispatcher, final HttpCachePolicy httpCachePolicy, final HttpCache httpCache) {
    super(query, serverUrl, httpCallFactory, response -> new Storefront.QueryRoot(response.getData()), dispatcher, httpCachePolicy, httpCache);
  }

  private RealQueryGraphCall(final RealGraphCall<Storefront.QueryRoot> other) {
    super(other);
  }

  @NonNull @Override public QueryGraphCall cachePolicy(@NonNull final HttpCachePolicy httpCachePolicy) {
    if (executed.get()) throw new IllegalStateException("Already Executed");
    return new RealQueryGraphCall((Storefront.QueryRootQuery) query, serverUrl, httpCallFactory, dispatcher,
      checkNotNull(httpCachePolicy, "cachePolicy == null"), httpCache);
  }

  @SuppressWarnings("CloneDoesntCallSuperClone")
  @NonNull @Override public RealQueryGraphCall clone() {
    return new RealQueryGraphCall(this);
  }
}
