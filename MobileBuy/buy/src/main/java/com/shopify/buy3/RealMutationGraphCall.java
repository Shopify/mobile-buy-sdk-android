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

final class RealMutationGraphCall extends RealGraphCall<Storefront.Mutation> implements MutationGraphCall {

  RealMutationGraphCall(final Storefront.MutationQuery query, final HttpUrl serverUrl, final Call.Factory httpCallFactory,
    final ScheduledExecutorService dispatcher, final CachePolicy cachePolicy, final HttpCache httpCache) {
    super(query, serverUrl, httpCallFactory, response -> new Storefront.Mutation(response.getData()), dispatcher, cachePolicy, httpCache);
  }

  private RealMutationGraphCall(final RealGraphCall<Storefront.Mutation> other) {
    super(other);
  }

  @SuppressWarnings("CloneDoesntCallSuperClone")
  @NonNull @Override public GraphCall<Storefront.Mutation> clone() {
    return new RealMutationGraphCall(this);
  }
}
