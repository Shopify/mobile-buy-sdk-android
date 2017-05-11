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

package com.shopify.buy3.cache;

import android.support.test.runner.AndroidJUnit4;

import com.shopify.buy3.HttpCachePolicy;
import com.shopify.buy3.GraphClient;
import com.shopify.buy3.GraphError;
import com.shopify.buy3.GraphHttpError;
import com.shopify.buy3.GraphParseError;
import com.shopify.buy3.GraphResponse;
import com.shopify.buy3.Storefront;
import com.shopify.buy3.TestUtils;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.internal.io.FileSystem;
import okhttp3.internal.io.InMemoryFileSystem;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okio.Buffer;

import static com.google.common.truth.Truth.assertThat;
import static com.shopify.buy3.TestUtils.readFileToString;
import static junit.framework.Assert.fail;

@RunWith(AndroidJUnit4.class)
public class CacheTest {
  @Rule public MockWebServer server = new MockWebServer();
  @Rule public InMemoryFileSystem inMemoryFileSystem = new InMemoryFileSystem();
  private OkHttpClient okHttpClient;
  private okhttp3.Request lastHttRequest;
  private okhttp3.Response lastHttResponse;
  private GraphClient graphClient;
  private HttpCache httpCache;
  private MockCacheStore cacheStore;

  @Before public void setUp() {
    cacheStore = new MockCacheStore();
    cacheStore.delegate = new DiskLruCacheStore(inMemoryFileSystem, new File("/cache/"), Integer.MAX_VALUE);

    httpCache = new HttpCache(cacheStore);

    okHttpClient = new OkHttpClient.Builder()
      .addInterceptor(chain -> {
        lastHttRequest = chain.request();
        lastHttResponse = chain.proceed(lastHttRequest);
        return lastHttResponse;
      })
      .readTimeout(4, TimeUnit.SECONDS)
      .connectTimeout(4, TimeUnit.SECONDS)
      .writeTimeout(4, TimeUnit.SECONDS)
      .build();

    graphClient = TestUtils.createGraphClient(server.url("/"), okHttpClient, httpCache);
  }

  @After public void tearDown() {
    try {
      server.shutdown();
    } catch (Exception ignore) {
      // ignore
    }

    try {
      //noinspection ConstantConditions
      graphClient.httpCache().clear();
    } catch (Exception ignore) {
      // ignore
    }
  }

  @Test public void prematureDisconnect() throws Exception {
    MockResponse mockResponse = mockResponse("/ShopWithCollections.json");
    Buffer truncatedBody = new Buffer();
    truncatedBody.write(mockResponse.getBody(), 16);
    mockResponse.setBody(truncatedBody);
    server.enqueue(mockResponse);

    try {
      graphClient.queryGraph(Storefront.query(new ShopWithCollectionsQuery()))
        .cachePolicy(HttpCachePolicy.NETWORK_ONLY.obtain())
        .execute();
      fail("Expected GraphError");
    } catch (GraphError expected) {
      // expected
    }

    checkNoCachedResponse();
  }

  @Test public void defaultCachePolicy() throws Exception {
    enqueueResponse("/ShopWithCollections.json");

    graphClient.queryGraph(Storefront.query(new ShopWithCollectionsQuery())).execute();

    assertThat(server.getRequestCount()).isEqualTo(1);
    assertThat(lastHttResponse.cacheResponse()).isNull();

    checkCachedResponse("/ShopWithCollections.json");
  }

  @Test public void networkOnly() throws Exception {
    enqueueResponse("/ShopWithCollections.json");

    graphClient.queryGraph(Storefront.query(new ShopWithCollectionsQuery()))
      .cachePolicy(HttpCachePolicy.NETWORK_ONLY.obtain())
      .execute();

    assertThat(server.getRequestCount()).isEqualTo(1);
    assertThat(lastHttResponse.cacheResponse()).isNull();

    checkCachedResponse("/ShopWithCollections.json");
  }

  @Test public void cacheOnlyHit() throws Exception {
    enqueueResponse("/ShopWithCollections.json");

    graphClient.queryGraph(Storefront.query(new ShopWithCollectionsQuery()))
      .cachePolicy(HttpCachePolicy.NETWORK_FIRST.obtain(10, TimeUnit.SECONDS))
      .execute();
    assertThat(server.getRequestCount()).isEqualTo(1);

    enqueueResponse("/ShopWithCollections.json");
    graphClient.queryGraph(Storefront.query(new ShopWithCollectionsQuery()))
      .cachePolicy(HttpCachePolicy.CACHE_ONLY.obtain())
      .execute();

    assertThat(server.getRequestCount()).isEqualTo(1);
    assertThat(lastHttResponse.networkResponse()).isNull();
    assertThat(lastHttResponse.cacheResponse()).isNotNull();
    checkCachedResponse("/ShopWithCollections.json");
  }

  @Test(expected = GraphHttpError.class) public void cacheOnlyMiss() throws Exception {
    enqueueResponse("/ShopWithCollections.json");
    graphClient.queryGraph(Storefront.query(new ShopWithCollectionsQuery()))
      .cachePolicy(HttpCachePolicy.CACHE_ONLY.obtain())
      .execute();
  }

  @Test public void cacheNonStale() throws Exception {
    enqueueResponse("/ShopWithCollections.json");
    graphClient.queryGraph(Storefront.query(new ShopWithCollectionsQuery()))
      .cachePolicy(HttpCachePolicy.NETWORK_FIRST.obtain(10, TimeUnit.SECONDS))
      .execute();
    assertThat(server.getRequestCount()).isEqualTo(1);

    enqueueResponse("/ShopWithCollections.json");
    graphClient.queryGraph(Storefront.query(new ShopWithCollectionsQuery()))
      .cachePolicy(HttpCachePolicy.CACHE_FIRST.obtain(10, TimeUnit.SECONDS))
      .execute();
    assertThat(server.getRequestCount()).isEqualTo(1);
    assertThat(lastHttResponse.networkResponse()).isNull();
    assertThat(lastHttResponse.cacheResponse()).isNotNull();
    checkCachedResponse("/ShopWithCollections.json");
  }

  @Test public void cacheFirstStale() throws Exception {
    enqueueResponse("/ShopWithCollections.json");
    graphClient.queryGraph(Storefront.query(new ShopWithCollectionsQuery()))
      .cachePolicy(HttpCachePolicy.NETWORK_FIRST.obtain(10, TimeUnit.SECONDS))
      .execute();
    assertThat(server.getRequestCount()).isEqualTo(1);

    Thread.sleep(TimeUnit.SECONDS.toMillis(3));

    enqueueResponse("/ShopWithCollectionsUpdate.json");
    graphClient.queryGraph(Storefront.query(new ShopWithCollectionsQuery()))
      .cachePolicy(HttpCachePolicy.CACHE_FIRST.obtain(2, TimeUnit.SECONDS))
      .execute();

    assertThat(server.getRequestCount()).isEqualTo(2);
    assertThat(lastHttResponse.networkResponse()).isNotNull();
    assertThat(lastHttResponse.cacheResponse()).isNull();
    checkCachedResponse("/ShopWithCollectionsUpdate.json");
  }

  @Test public void cacheFirstUpdate() throws Exception {
    enqueueResponse("/ShopWithCollections.json");
    graphClient.queryGraph(Storefront.query(new ShopWithCollectionsQuery()))
      .cachePolicy(HttpCachePolicy.NETWORK_FIRST.obtain(10, TimeUnit.SECONDS))
      .execute();

    enqueueResponse("/ShopWithCollectionsUpdate.json");
    graphClient.queryGraph(Storefront.query(new ShopWithCollectionsQuery()))
      .cachePolicy(HttpCachePolicy.NETWORK_FIRST.obtain(10, TimeUnit.SECONDS))
      .execute();

    assertThat(server.getRequestCount()).isEqualTo(2);

    graphClient.queryGraph(Storefront.query(new ShopWithCollectionsQuery()))
      .cachePolicy(HttpCachePolicy.CACHE_FIRST.obtain(10, TimeUnit.SECONDS))
      .execute();
    assertThat(lastHttResponse.networkResponse()).isNull();
    assertThat(lastHttResponse.cacheResponse()).isNotNull();
    checkCachedResponse("/ShopWithCollectionsUpdate.json");
  }

  @Test public void networkFirstHttpError() throws Exception {
    enqueueResponse("/ShopWithCollections.json");
    graphClient.queryGraph(Storefront.query(new ShopWithCollectionsQuery()))
      .cachePolicy(HttpCachePolicy.NETWORK_FIRST.obtain(10, TimeUnit.SECONDS))
      .execute();

    server.enqueue(new MockResponse().setResponseCode(504).setBody(""));

    graphClient.queryGraph(Storefront.query(new ShopWithCollectionsQuery()))
      .cachePolicy(HttpCachePolicy.NETWORK_FIRST.obtain(10, TimeUnit.SECONDS))
      .execute();

    assertThat(server.getRequestCount()).isEqualTo(2);
    assertThat(lastHttResponse.networkResponse().code()).isEqualTo(504);
    assertThat(lastHttResponse.cacheResponse()).isNotNull();
    checkCachedResponse("/ShopWithCollections.json");
  }

  @Test public void networkFirstParseError() throws Exception {
    server.enqueue(new MockResponse().setResponseCode(200).setBody("{\"data\": { \"shop\": {"));
    try {
      graphClient.queryGraph(Storefront.query(new ShopWithCollectionsQuery()))
        .cachePolicy(HttpCachePolicy.NETWORK_FIRST.obtain(10, TimeUnit.SECONDS))
        .execute();
      fail("Expected GraphParseError");
    } catch (GraphParseError expected) {
      // expected
    }

    assertThat(server.getRequestCount()).isEqualTo(1);
    assertThat(lastHttResponse.networkResponse()).isNotNull();
    assertThat(lastHttResponse.cacheResponse()).isNull();
    checkNoCachedResponse();
  }

  @Test public void noCacheStore() throws Exception {
    enqueueResponse("/ShopWithCollections.json");

    graphClient = TestUtils.createGraphClient(server.url("/"), okHttpClient, null);

    graphClient.queryGraph(Storefront.query(new ShopWithCollectionsQuery()))
      .cachePolicy(HttpCachePolicy.NETWORK_FIRST.obtain(10, TimeUnit.SECONDS))
      .execute();
    assertThat(server.getRequestCount()).isEqualTo(1);
    assertThat(lastHttResponse.cacheResponse()).isNull();
  }

  @Test public void fileSystemUnavailable() throws Exception {
    enqueueResponse("/ShopWithCollections.json");

    cacheStore.delegate = new DiskLruCacheStore(new NoFileSystem(), new File("/cache/"), Integer.MAX_VALUE);
    GraphResponse response = graphClient.queryGraph(Storefront.query(new ShopWithCollectionsQuery()))
      .cachePolicy(HttpCachePolicy.NETWORK_FIRST.obtain(10, TimeUnit.SECONDS))
      .execute();
    assertThat(response.hasErrors()).isFalse();
    assertThat(response.data()).isNotNull();

    checkNoCachedResponse();
  }

  @Test public void fileSystemWriteFailure() throws Exception {
    enqueueResponse("/ShopWithCollections.json");

    FaultyCacheStore faultyCacheStore = new FaultyCacheStore(FileSystem.SYSTEM);
    cacheStore.delegate = faultyCacheStore;

    faultyCacheStore.failStrategy(FaultyCacheStore.FailStrategy.FAIL_HEADER_WRITE);
    GraphResponse response = graphClient.queryGraph(Storefront.query(new ShopWithCollectionsQuery()))
      .cachePolicy(HttpCachePolicy.NETWORK_FIRST.obtain(10, TimeUnit.SECONDS))
      .execute();
    assertThat(server.getRequestCount()).isEqualTo(1);
    assertThat(lastHttResponse.cacheResponse()).isNull();
    assertThat(response.hasErrors()).isFalse();
    assertThat(response.data()).isNotNull();
    checkNoCachedResponse();

    enqueueResponse("/ShopWithCollections.json");
    faultyCacheStore.failStrategy(FaultyCacheStore.FailStrategy.FAIL_BODY_WRITE);
    response = graphClient.queryGraph(Storefront.query(new ShopWithCollectionsQuery()))
      .cachePolicy(HttpCachePolicy.NETWORK_FIRST.obtain(10, TimeUnit.SECONDS))
      .execute();
    assertThat(server.getRequestCount()).isEqualTo(2);
    assertThat(lastHttResponse.cacheResponse()).isNull();
    assertThat(response.hasErrors()).isFalse();
    assertThat(response.data()).isNotNull();
    checkNoCachedResponse();
  }

  @Test public void fileSystemReadFailure() throws Exception {
    FaultyCacheStore faultyCacheStore = new FaultyCacheStore(inMemoryFileSystem);
    cacheStore.delegate = faultyCacheStore;

    enqueueResponse("/ShopWithCollections.json");
    GraphResponse response = graphClient.queryGraph(Storefront.query(new ShopWithCollectionsQuery()))
      .cachePolicy(HttpCachePolicy.NETWORK_ONLY.obtain())
      .execute();
    assertThat(server.getRequestCount()).isEqualTo(1);
    assertThat(lastHttResponse.cacheResponse()).isNull();
    assertThat(response.hasErrors()).isFalse();
    assertThat(response.data()).isNotNull();
    checkCachedResponse("/ShopWithCollections.json");

    enqueueResponse("/ShopWithCollections.json");
    faultyCacheStore.failStrategy(FaultyCacheStore.FailStrategy.FAIL_HEADER_READ);
    response = graphClient.queryGraph(Storefront.query(new ShopWithCollectionsQuery()))
      .cachePolicy(HttpCachePolicy.CACHE_FIRST.obtain(10, TimeUnit.SECONDS))
      .execute();
    assertThat(server.getRequestCount()).isEqualTo(2);
    assertThat(lastHttResponse.cacheResponse()).isNull();
    assertThat(response.hasErrors()).isFalse();
    assertThat(response.data()).isNotNull();

    faultyCacheStore.failStrategy(FaultyCacheStore.FailStrategy.FAIL_BODY_READ);
    try {
      graphClient.queryGraph(Storefront.query(new ShopWithCollectionsQuery()))
        .cachePolicy(HttpCachePolicy.CACHE_ONLY.obtain())
        .execute();
      fail("Expected GraphParseError");
    } catch (GraphParseError expected) {
      // expected
    }
  }

  private void checkNoCachedResponse() throws IOException {
    String cacheKey = lastHttRequest.header(HttpCache.CACHE_KEY_HEADER);
    okhttp3.Response cachedResponse = httpCache.read(cacheKey);
    assertThat(cachedResponse).isNull();
  }

  private void checkCachedResponse(String fileName) throws IOException {
    String cacheKey = HttpCache.cacheKey(lastHttRequest.body());
    okhttp3.Response response = httpCache.read(cacheKey);
    assertThat(response).isNotNull();
    assertThat(response.body().source().readUtf8()).isEqualTo(readFileToString(getClass(), fileName));
    response.body().source().close();
  }

  private MockResponse mockResponse(String fileName) throws IOException {
    return new MockResponse().setChunkedBody(readFileToString(getClass(), fileName), 32);
  }

  private void enqueueResponse(String fileName) throws IOException {
    server.enqueue(mockResponse(fileName));
  }

  private static final class ShopWithCollectionsQuery implements Storefront.QueryRootQueryDefinition {
    @Override public void define(final Storefront.QueryRootQuery root) {
      root.shop(
        shop -> shop
          .name()
          .currencyCode()
          .description()
          .collections(10, collectionConnection -> collectionConnection
            .edges(collectionEdge -> collectionEdge
              .cursor()
              .node(Storefront.CollectionQuery::title)
            )
          )
      );
    }
  }
}
