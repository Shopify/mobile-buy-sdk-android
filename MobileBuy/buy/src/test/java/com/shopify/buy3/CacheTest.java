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

import android.content.Context;

import com.shopify.graphql.support.ID;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.mockwebserver.MockWebServer;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CacheTest {
  private static final String PACKAGE_NAME = "com.shopify.buy3.test";
  private static final String SHOP_DOMAIN = "myshop.shopify.com";
  private static final String ACCESS_TOKEN = "access_token";

  @Mock public Context mockContext;
  @Rule public MockWebServer server = new MockWebServer();
  private GraphClient graphClient;
  private final Storefront.QueryRootQuery shopNameQuery = Storefront.query(root -> root.shop(Storefront.ShopQuery::name));
  private final Storefront.MutationQuery checkCompleteQuery = Storefront.mutation(root -> root.checkoutCompleteFree(new ID("test"),
    query -> query.checkout(Storefront.CheckoutQuery::ready)));

  @Before public void setUp() {
    when(mockContext.getPackageName()).thenReturn(PACKAGE_NAME);
    graphClient = GraphClient.builder(mockContext)
      .accessToken(ACCESS_TOKEN)
      .serverUrl(server.url(SHOP_DOMAIN))
      .httpClient(new OkHttpClient.Builder()
        .connectTimeout(3, TimeUnit.SECONDS)
        .readTimeout(3, TimeUnit.SECONDS)
        .build())
      .defaultCachePolicy(CachePolicy.NETWORK_FIRST.obtain(10, TimeUnit.MINUTES))
      .build();
  }

  @Test public void defaultCachePolicy() throws Exception {
    QueryGraphCall queryGraphCall = graphClient.queryGraph(shopNameQuery);
    RealGraphCall realGraphCall = (RealGraphCall) queryGraphCall;
    assertThat(realGraphCall.cachePolicy.fetchStrategy).isEqualTo(CachePolicy.FetchStrategy.NETWORK_FIRST);
    assertThat(realGraphCall.cachePolicy.expireTimeout).isEqualTo(10);
    assertThat(realGraphCall.cachePolicy.expireTimeUnit).isEqualTo(TimeUnit.MINUTES);

    realGraphCall = (RealGraphCall) queryGraphCall.cachePolicy(CachePolicy.CACHE_FIRST.obtain(60, TimeUnit.SECONDS));
    assertThat(realGraphCall.cachePolicy.fetchStrategy).isEqualTo(CachePolicy.FetchStrategy.CACHE_FIRST);
    assertThat(realGraphCall.cachePolicy.expireTimeout).isEqualTo(60);
    assertThat(realGraphCall.cachePolicy.expireTimeUnit).isEqualTo(TimeUnit.SECONDS);

    MutationGraphCall mutationGraphCall = graphClient.mutateGraph(checkCompleteQuery);
    realGraphCall = (RealGraphCall) mutationGraphCall;
    assertThat(realGraphCall.cachePolicy.fetchStrategy).isEqualTo(CachePolicy.FetchStrategy.NETWORK_ONLY);
  }
}
