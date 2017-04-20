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

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.net.SocketTimeoutException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import okhttp3.OkHttpClient;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;

import static com.google.common.truth.Truth.assertThat;
import static junit.framework.Assert.fail;

@RunWith(AndroidJUnit4.class)
public class GraphClientIntegrationTest {
  private static final String SHOP_DOMAIN = "myshop.shopify.com";

  @Rule public MockWebServer server = new MockWebServer();
  private GraphClient graphClient;
  private final Storefront.QueryRootQuery shopNameQuery = Storefront.query(root -> root.shop(Storefront.ShopQuery::name));

  @Before public void setUp() {
    graphClient = GraphClient.builder(InstrumentationRegistry.getContext())
      .accessToken("access_token")
      .serverUrl(server.url(SHOP_DOMAIN))
      .httpClient(new OkHttpClient.Builder()
        .connectTimeout(3, TimeUnit.SECONDS)
        .readTimeout(3, TimeUnit.SECONDS)
        .build())
      .build();
  }

  @Test public void graphResponse() throws Exception {
    server.enqueue(new MockResponse().setResponseCode(200).setBody("{"
      + "  \"data\": {"
      + "    \"shop\": {"
      + "      \"name\": \"MyShop\""
      + "    }"
      + "  }"
      + "}"));
    AtomicBoolean executedInCallbackHandler = new AtomicBoolean();
    NamedCountDownLatch countDownLatch = new NamedCountDownLatch("invalidResponseErrorAsync", 1);
    AtomicReference<GraphResponse<Storefront.QueryRoot>> responseRef = new AtomicReference<>();
    graphClient.queryGraph(shopNameQuery).enqueue(new GraphCall.Callback<Storefront.QueryRoot>() {
      @Override public void onResponse(@NonNull final GraphResponse<Storefront.QueryRoot> response) {
        responseRef.set(response);
        countDownLatch.countDown();
      }

      @Override public void onFailure(@NonNull final GraphError error) {
        fail("expected onResponse");
      }
    }, mockCallbackHandler(executedInCallbackHandler));
    countDownLatch.await(2, TimeUnit.SECONDS);
    assertThat(executedInCallbackHandler.get()).isTrue();
    GraphResponse<Storefront.QueryRoot> response = responseRef.get();
    assertThat(response.data()).isNotNull();
    assertThat(response.data().getShop()).isNotNull();
    assertThat(response.data().getShop().getName()).isEqualTo("MyShop");
  }

  @Test public void graphResponseErrors() throws Exception {
    server.enqueue(new MockResponse().setResponseCode(200).setBody("{"
      + "  \"errors\": ["
      + "    {"
      + "      \"message\": \"Field 'names' doesn't exist on type 'Shop'\","
      + "      \"locations\": ["
      + "        {"
      + "          \"line\": 1,"
      + "          \"column\": 7"
      + "        }"
      + "      ],"
      + "      \"fields\": ["
      + "        \"query\","
      + "        \"shop\","
      + "        \"names\""
      + "      ]"
      + "    }"
      + "  ]"
      + "}"));
    AtomicBoolean executedInCallbackHandler = new AtomicBoolean();
    NamedCountDownLatch countDownLatch = new NamedCountDownLatch("invalidResponseErrorAsync", 1);
    AtomicReference<GraphResponse<Storefront.QueryRoot>> responseRef = new AtomicReference<>();
    graphClient.queryGraph(shopNameQuery).enqueue(new GraphCall.Callback<Storefront.QueryRoot>() {
      @Override public void onResponse(@NonNull final GraphResponse<Storefront.QueryRoot> response) {
        responseRef.set(response);
        countDownLatch.countDown();
      }

      @Override public void onFailure(@NonNull final GraphError error) {
        fail("expected onResponse");
      }
    }, mockCallbackHandler(executedInCallbackHandler));
    countDownLatch.await(2, TimeUnit.SECONDS);
    assertThat(executedInCallbackHandler.get()).isTrue();
    GraphResponse<Storefront.QueryRoot> response = responseRef.get();
    assertThat(response.data()).isNull();
    assertThat(response.hasErrors()).isTrue();
    assertThat(response.errors()).isNotEmpty();
    assertThat(response.formatErrorMessage()).isEqualTo("Field 'names' doesn't exist on type 'Shop'");
  }

  @Test public void invalidResponseError() throws Exception {
    server.enqueue(new MockResponse().setResponseCode(401).setBody("Unauthorized request!"));
    AtomicBoolean executedInCallbackHandler = new AtomicBoolean();
    NamedCountDownLatch countDownLatch = new NamedCountDownLatch("invalidResponseErrorAsync", 1);
    AtomicReference<GraphHttpError> errorRef = new AtomicReference<>();
    graphClient.queryGraph(shopNameQuery).enqueue(new GraphCall.Callback<Storefront.QueryRoot>() {
      @Override public void onResponse(@NonNull final GraphResponse<Storefront.QueryRoot> response) {
        fail("expected GraphInvalidResponseError");
      }

      @Override public void onFailure(@NonNull final GraphError error) {
        fail("expected GraphInvalidResponseError");
      }

      @Override public void onHttpError(@NonNull final GraphHttpError error) {
        errorRef.set(error);
        countDownLatch.countDown();
      }
    }, mockCallbackHandler(executedInCallbackHandler));
    countDownLatch.await(2, TimeUnit.SECONDS);
    assertThat(executedInCallbackHandler.get()).isTrue();
    GraphHttpError error = errorRef.get();
    assertThat(error.code()).isEqualTo(401);
    assertThat(error.message()).isEqualTo("Client Error");
    assertThat(error.rawResponse().body().string()).isEqualTo("Unauthorized request!");
    assertThat(error.getMessage()).isEqualTo("HTTP 401 Client Error");
    error.dispose();
  }

  @Test public void networkError() throws Exception {
    AtomicBoolean executedInCallbackHandler = new AtomicBoolean();
    NamedCountDownLatch countDownLatch = new NamedCountDownLatch("testNetworkErrorAsync", 1);
    AtomicReference<GraphNetworkError> errorRef = new AtomicReference<>();
    graphClient.queryGraph(shopNameQuery).enqueue(new GraphCall.Callback<Storefront.QueryRoot>() {
      @Override public void onResponse(@NonNull final GraphResponse<Storefront.QueryRoot> response) {
        fail("expected ApolloNetworkException");
      }

      @Override public void onFailure(@NonNull final GraphError error) {
        fail("expected ApolloNetworkException");
      }

      @Override public void onNetworkError(@NonNull final GraphNetworkError error) {
        errorRef.set(error);
        countDownLatch.countDown();
      }
    }, mockCallbackHandler(executedInCallbackHandler));
    countDownLatch.await(4, TimeUnit.SECONDS);
    assertThat(executedInCallbackHandler.get()).isTrue();
    GraphNetworkError error = errorRef.get();
    assertThat(error.getMessage()).isEqualTo("Failed to execute GraphQL http request");
    assertThat(error.getCause().getClass()).isEqualTo(SocketTimeoutException.class);
  }

  @Test public void parseError() throws Exception {
    server.enqueue(new MockResponse().setBody("Noise"));
    AtomicBoolean executedInCallbackHandler = new AtomicBoolean();
    NamedCountDownLatch countDownLatch = new NamedCountDownLatch("testParseErrorAsync", 1);
    AtomicReference<GraphParseError> errorRef = new AtomicReference<>();
    graphClient.queryGraph(shopNameQuery).enqueue(new GraphCall.Callback<Storefront.QueryRoot>() {
      @Override public void onResponse(@NonNull final GraphResponse<Storefront.QueryRoot> response) {
        fail("expected ApolloNetworkException");
      }

      @Override public void onFailure(@NonNull final GraphError error) {
        fail("expected ApolloNetworkException");
      }

      @Override public void onParseError(@NonNull final GraphParseError error) {
        errorRef.set(error);
        countDownLatch.countDown();
      }
    }, mockCallbackHandler(executedInCallbackHandler));
    countDownLatch.await(1, TimeUnit.SECONDS);
    assertThat(executedInCallbackHandler.get()).isTrue();
    GraphParseError error = errorRef.get();
    assertThat(error.getMessage()).isEqualTo("Failed to parse GraphQL http response");
  }

  @Test public void cancel() throws Exception {
    server.enqueue(new MockResponse().setResponseCode(200).setBody("{"
      + "  \"data\": {"
      + "    \"shop\": {"
      + "      \"name\": \"MyShop\""
      + "    }"
      + "  }"
      + "}").setBodyDelay(3, TimeUnit.SECONDS));
    AtomicBoolean executedInCallbackHandler = new AtomicBoolean();
    CountDownLatch countDownLatch = new CountDownLatch(1);
    AtomicBoolean callbackIssued = new AtomicBoolean();
    GraphCall<Storefront.QueryRoot> call = graphClient.queryGraph(shopNameQuery).enqueue(new GraphCall.Callback<Storefront.QueryRoot>() {
      @Override public void onResponse(@NonNull final GraphResponse<Storefront.QueryRoot> response) {
        callbackIssued.set(true);
        countDownLatch.countDown();
      }

      @Override public void onFailure(@NonNull final GraphError error) {
        callbackIssued.set(true);
        countDownLatch.countDown();
      }
    }, mockCallbackHandler(executedInCallbackHandler));
    countDownLatch.await(2, TimeUnit.SECONDS);
    call.cancel();
    countDownLatch.await(5, TimeUnit.SECONDS);
    if (callbackIssued.get()) {
      fail("expected canceled");
    }
    assertThat(call.isCanceled()).isTrue();
    assertThat(executedInCallbackHandler.get()).isFalse();
  }

  private static Looper createBackgroundLooper() throws Exception {
    final AtomicReference<Looper> looperRef = new AtomicReference<>();
    new Thread("CallbackThread") {
      @Override public void run() {
        Looper.prepare();
        synchronized (this) {
          looperRef.set(Looper.myLooper());
          notifyAll();
        }
        Looper.loop();
      }
    }.start();
    Thread.sleep(200);
    return looperRef.get();
  }

  private static Handler mockCallbackHandler(final AtomicBoolean invokeTracker) throws Exception {
    return new Handler(createBackgroundLooper()) {
      @Override public boolean sendMessageAtTime(Message msg, long uptimeMillis) {
        invokeTracker.set(true);
        msg.getCallback().run();
        return true;
      }
    };
  }
}
