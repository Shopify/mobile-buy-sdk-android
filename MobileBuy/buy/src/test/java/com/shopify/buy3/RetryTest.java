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
import android.support.annotation.NonNull;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import okhttp3.OkHttpClient;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;

import static com.google.common.truth.Truth.assertThat;
import static junit.framework.Assert.fail;

@RunWith(MockitoJUnitRunner.class)
public class RetryTest {
  @Mock public Context mockContext;
  @Rule public MockWebServer server = new MockWebServer();
  private GraphClient graphClient;

  @Before public void setUp() {
    OkHttpClient httpClient = new OkHttpClient.Builder()
      .build();

    graphClient = GraphClient.builder(mockContext)
      .httpClient(httpClient)
      .serverUrl(server.url("/"))
      .build();
  }

  @Test public void noRetryNetworkError() throws Exception {
    server.enqueue(new MockResponse().setResponseCode(500).setBody(""));
    server.enqueue(new MockResponse().setResponseCode(500).setBody(""));

    AtomicReference<GraphError> graphError = new AtomicReference<>();
    NamedCountDownLatch latch = new NamedCountDownLatch("noRetryNetworkError", 1);
    graphClient
      .queryGraph(Storefront.query(root -> root.shop(Storefront.ShopQuery::name)))
      .enqueue(new GraphCall.Callback<Storefront.QueryRoot>() {
        @Override public void onResponse(@NonNull final GraphResponse<Storefront.QueryRoot> response) {
          latch.countDown();
        }

        @Override public void onFailure(@NonNull final GraphError error) {
          graphError.set(error);
          latch.countDown();
        }
      });

    latch.awaitOrThrowWithTimeout(3, TimeUnit.SECONDS);

    GraphError error = graphError.get();
    if (error == null) {
      fail("expected GraphError");
    }

    assertThat(server.getRequestCount()).isEqualTo(1);
    assertThat(error.type()).isEqualTo(GraphError.Type.NETWORK);
  }

  @Test public void retryNoConditionNetworkError() throws Exception {
    server.enqueue(new MockResponse().setResponseCode(500).setBody(""));
    server.enqueue(new MockResponse().setResponseCode(500).setBody(""));
    server.enqueue(new MockResponse().setResponseCode(500).setBody(""));
    server.enqueue(new MockResponse().setResponseCode(500).setBody(""));
    server.enqueue(new MockResponse().setResponseCode(500).setBody(""));

    AtomicReference<GraphError> graphError = new AtomicReference<>();
    NamedCountDownLatch latch = new NamedCountDownLatch("retryNoConditionNetworkError", 1);
    graphClient
      .queryGraph(Storefront.query(root -> root.shop(Storefront.ShopQuery::name)))
      .enqueue(new GraphCall.Callback<Storefront.QueryRoot>() {
        @Override public void onResponse(@NonNull final GraphResponse<Storefront.QueryRoot> response) {
          latch.countDown();
        }

        @Override public void onFailure(@NonNull final GraphError error) {
          graphError.set(error);
          latch.countDown();
        }
      }, null, RetryHandler.simple(3, 100, TimeUnit.MILLISECONDS, 1));

    latch.awaitOrThrowWithTimeout(10, TimeUnit.SECONDS);

    GraphError error = graphError.get();
    if (error == null) {
      fail("expected GraphError");
    }

    assertThat(server.getRequestCount()).isEqualTo(4);
    assertThat(error.type()).isEqualTo(GraphError.Type.NETWORK);
  }

  @Test public void retryWihConditionNetworkError() throws Exception {
    server.enqueue(new MockResponse().setResponseCode(500).setBody(""));
    server.enqueue(new MockResponse().setResponseCode(500).setBody(""));
    server.enqueue(new MockResponse().setResponseCode(500).setBody(""));
    server.enqueue(new MockResponse().setResponseCode(200).setBody(""));
    server.enqueue(new MockResponse().setResponseCode(500).setBody(""));

    AtomicReference<GraphError> graphError = new AtomicReference<>();
    NamedCountDownLatch latch = new NamedCountDownLatch("retryWihConditionNetworkError", 1);
    graphClient
      .queryGraph(Storefront.query(root -> root.shop(Storefront.ShopQuery::name)))
      .enqueue(new GraphCall.Callback<Storefront.QueryRoot>() {
        @Override public void onResponse(@NonNull final GraphResponse<Storefront.QueryRoot> response) {
          latch.countDown();
        }

        @Override public void onFailure(@NonNull final GraphError error) {
          graphError.set(error);
          latch.countDown();
        }
      }, null, RetryHandler.builder()
        .maxCount(4)
        .delayBetweenRetries(100, TimeUnit.MILLISECONDS)
        .backoffMultiplier(1)
        .errorRetryCondition(error -> error.type() == GraphError.Type.NETWORK)
        .build());

    latch.awaitOrThrowWithTimeout(10, TimeUnit.SECONDS);

    GraphError error = graphError.get();
    if (error == null) {
      fail("expected GraphError");
    }

    assertThat(server.getRequestCount()).isEqualTo(4);
    assertThat(error.type()).isEqualTo(GraphError.Type.PARSE);
  }

  @Test public void retryNoConditionSuccess() throws Exception {
    server.enqueue(new MockResponse().setResponseCode(500).setBody(""));
    server.enqueue(new MockResponse().setResponseCode(500).setBody(""));
    server.enqueue(new MockResponse().setResponseCode(200).setBody("{\n" +
      "  \"data\": {\n" +
      "    \"shop\": {\n" +
      "      \"name\": \"Greats Clone\"\n" +
      "    }\n" +
      "  }\n" +
      "}"));

    AtomicReference<GraphResponse<Storefront.QueryRoot>> graphResponse = new AtomicReference<>();
    AtomicReference<GraphError> graphError = new AtomicReference<>();
    NamedCountDownLatch latch = new NamedCountDownLatch("retryNoConditionSuccess", 1);
    graphClient
      .queryGraph(Storefront.query(root -> root.shop(Storefront.ShopQuery::name)))
      .enqueue(new GraphCall.Callback<Storefront.QueryRoot>() {
        @Override public void onResponse(@NonNull final GraphResponse<Storefront.QueryRoot> response) {
          graphResponse.set(response);
          latch.countDown();
        }

        @Override public void onFailure(@NonNull final GraphError error) {
          graphError.set(error);
          latch.countDown();
        }
      }, null, RetryHandler.simple(3, 100, TimeUnit.MILLISECONDS, 1));

    latch.awaitOrThrowWithTimeout(5, TimeUnit.SECONDS);

    GraphError error = graphError.get();
    if (error != null) {
      throw error;
    }

    assertThat(graphResponse.get().data().getShop().getName()).isEqualTo("Greats Clone");
  }

  @Test public void retryWithConditionSuccess() throws Exception {
    server.enqueue(new MockResponse().setResponseCode(500).setBody(""));
    server.enqueue(new MockResponse().setResponseCode(200).setBody("{\n" +
      "  \"data\": {\n" +
      "    \"shop\": {\n" +
      "      \"name\": \"Empty\"\n" +
      "    }\n" +
      "  }\n" +
      "}"));
    server.enqueue(new MockResponse().setResponseCode(500).setBody(""));
    server.enqueue(new MockResponse().setResponseCode(200).setBody("{\n" +
      "  \"data\": {\n" +
      "    \"shop\": {\n" +
      "      \"name\": \"Greats Clone\"\n" +
      "    }\n" +
      "  }\n" +
      "}"));

    AtomicReference<GraphResponse<Storefront.QueryRoot>> graphResponse = new AtomicReference<>();
    AtomicReference<GraphError> graphError = new AtomicReference<>();
    NamedCountDownLatch latch = new NamedCountDownLatch("retryWithConditionSuccess", 1);
    graphClient
      .queryGraph(Storefront.query(root -> root.shop(Storefront.ShopQuery::name)))
      .enqueue(new GraphCall.Callback<Storefront.QueryRoot>() {
        @Override public void onResponse(@NonNull final GraphResponse<Storefront.QueryRoot> response) {
          graphResponse.set(response);
          latch.countDown();
        }

        @Override public void onFailure(@NonNull final GraphError error) {
          graphError.set(error);
          latch.countDown();
        }
      }, null, RetryHandler.builder()
        .maxCount(3)
        .delayBetweenRetries(100, TimeUnit.MILLISECONDS)
        .backoffMultiplier(1)
        .<Storefront.QueryRoot>responseRetryCondition(response -> response.data().getShop().getName().equals("Empty"))
        .build());

    latch.awaitOrThrowWithTimeout(5, TimeUnit.SECONDS);

    GraphError error = graphError.get();
    if (error != null) {
      throw error;
    }

    assertThat(graphResponse.get().data().getShop().getName()).isEqualTo("Greats Clone");
  }

  @Test public void retryAndCancel() throws Exception {
    server.enqueue(new MockResponse().setResponseCode(500).setBody(""));
    server.enqueue(new MockResponse().setResponseCode(200).setBody("{\n" +
      "  \"data\": {\n" +
      "    \"shop\": {\n" +
      "      \"name\": \"Greats Clone\"\n" +
      "    }\n" +
      "  }\n" +
      "}"));

    AtomicReference<GraphResponse<Storefront.QueryRoot>> graphResponse = new AtomicReference<>();
    AtomicReference<GraphError> graphError = new AtomicReference<>();
    NamedCountDownLatch latch = new NamedCountDownLatch("retryAndCancel", 1);
    GraphCall<Storefront.QueryRoot> call = graphClient
      .queryGraph(Storefront.query(root -> root.shop(Storefront.ShopQuery::name)))
      .enqueue(new GraphCall.Callback<Storefront.QueryRoot>() {
        @Override public void onResponse(@NonNull final GraphResponse<Storefront.QueryRoot> response) {
          graphResponse.set(response);
          latch.countDown();
        }

        @Override public void onFailure(@NonNull final GraphError error) {
          graphError.set(error);
          latch.countDown();
        }
      }, null, RetryHandler.builder()
        .maxCount(3)
        .delayBetweenRetries(3, TimeUnit.SECONDS)
        .backoffMultiplier(1)
        .<Storefront.QueryRoot>responseRetryCondition(response -> response.data().getShop().getName().equals("Empty"))
        .build());

    Thread.sleep(TimeUnit.SECONDS.toMillis(1));
    call.cancel();

    latch.awaitOrThrowWithTimeout(10, TimeUnit.SECONDS);

    if (graphError.get() != null || graphResponse.get() != null) {
      fail("Expected to cancel");
    }
  }
}
