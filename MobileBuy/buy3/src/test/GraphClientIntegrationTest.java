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

import com.google.common.base.Charsets;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.net.SocketTimeoutException;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;

import static com.google.common.truth.Truth.assertThat;
import static junit.framework.Assert.fail;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class GraphClientIntegrationTest {

  @Test
  public void foo() {

  }

//  private static final String PACKAGE_NAME = "com.shopify.buy3.test";
//  private static final String SHOP_DOMAIN = "myshop.shopify.com";
//  private static final String ACCESS_TOKEN = "access_token";
//
//  @Mock public Context mockContext;
//  @Rule public MockWebServer server = new MockWebServer();
//  private GraphClient graphClient;
//  private final Storefront.QueryRootQuery shopNameQuery = Storefront.query(root -> root.shop(Storefront.ShopQuery::name));
//
//  @Before public void setUp() {
//    when(mockContext.getPackageName()).thenReturn(PACKAGE_NAME);
//    graphClient = GraphClient.builder(mockContext)
//      .accessToken(ACCESS_TOKEN)
//      .serverUrl(server.url(SHOP_DOMAIN))
//      .httpClient(new OkHttpClient.Builder()
//        .connectTimeout(3, TimeUnit.SECONDS)
//        .readTimeout(3, TimeUnit.SECONDS)
//        .build())
//      .build();
//  }
//
//  @Test public void httpHeaders() throws Exception {
//    server.enqueue(new MockResponse().setResponseCode(200).setBody(""));
//    try {
//      graphClient.queryGraph(shopNameQuery).execute();
//      fail("expected exception to be thrown");
//    } catch (GraphError error) {
//      // ignore
//    }
//
//    RecordedRequest recordedRequest = server.takeRequest();
//    assertThat(recordedRequest.getHeader("X-Shopify-Storefront-Access-Token")).isEqualTo(ACCESS_TOKEN);
//    assertThat(recordedRequest.getHeader("X-SDK-Version")).isEqualTo(BuildConfig.VERSION_NAME);
//    assertThat(recordedRequest.getHeader("X-SDK-Variant")).isEqualTo("android");
//    assertThat(recordedRequest.getHeader("User-Agent")).isEqualTo("Mobile Buy SDK Android/" + BuildConfig.VERSION_NAME + "/" + PACKAGE_NAME);
//    assertThat(recordedRequest.getHeader("Accept")).isEqualTo(RealGraphCall.ACCEPT_HEADER);
//    assertThat(recordedRequest.getHeader("Content-Type")).isEqualTo(RealGraphCall.GRAPHQL_MEDIA_TYPE.toString());
//  }
//
//  @Test public void httpRequest() throws Exception {
//    server.enqueue(new MockResponse().setResponseCode(200).setBody(""));
//    try {
//      graphClient.queryGraph(shopNameQuery).execute();
//      fail("expected exception to be thrown");
//    } catch (GraphError error) {
//      // ignore
//    }
//
//    RecordedRequest recordedRequest = server.takeRequest();
//    assertThat(recordedRequest.getMethod()).isEqualTo("POST");
//    assertThat(recordedRequest.getBody().readString(Charsets.UTF_8)).isEqualTo("{shop{name}}");
//  }
//
//  @Test public void graphResponse() throws Exception {
//    server.enqueue(new MockResponse().setResponseCode(200).setBody("{"
//      + "  \"data\": {"
//      + "    \"shop\": {"
//      + "      \"name\": \"MyShop\""
//      + "    }"
//      + "  }"
//      + "}"));
//    GraphResponse<Storefront.QueryRoot> response = graphClient.queryGraph(shopNameQuery).execute();
//    assertThat(response.data()).isNotNull();
//    assertThat(response.data().getShop()).isNotNull();
//    assertThat(response.data().getShop().getName()).isEqualTo("MyShop");
//  }
//
//  @Test public void graphResponseErrors() throws Exception {
//    server.enqueue(new MockResponse().setResponseCode(200).setBody("{"
//      + "  \"errors\": ["
//      + "    {"
//      + "      \"message\": \"Field 'names' doesn't exist on type 'Shop'\","
//      + "      \"locations\": ["
//      + "        {"
//      + "          \"line\": 1,"
//      + "          \"column\": 7"
//      + "        }"
//      + "      ],"
//      + "      \"fields\": ["
//      + "        \"query\","
//      + "        \"shop\","
//      + "        \"names\""
//      + "      ]"
//      + "    }"
//      + "  ]"
//      + "}"));
//    GraphResponse<Storefront.QueryRoot> response = graphClient.queryGraph(shopNameQuery).execute();
//    assertThat(response.data()).isNull();
//    assertThat(response.hasErrors()).isTrue();
//    assertThat(response.errors()).isNotEmpty();
//    assertThat(response.formatErrorMessage()).isEqualTo("Field 'names' doesn't exist on type 'Shop'");
//  }
//
//  @Test public void invalidResponseError() throws Exception {
//    server.enqueue(new MockResponse().setResponseCode(401).setBody("Unauthorized request!"));
//    try {
//      graphClient.queryGraph(shopNameQuery).execute();
//      fail("expected GraphInvalidResponseError");
//    } catch (GraphHttpError e) {
//      assertThat(e.code()).isEqualTo(401);
//      assertThat(e.message()).isEqualTo("Client Error");
//      assertThat(e.getMessage()).isEqualTo("HTTP 401 Client Error");
//    }
//  }
//
//  @Test public void networkError() throws Exception {
//    try {
//      graphClient.queryGraph(shopNameQuery).execute();
//      fail("expected GraphNetworkError");
//    } catch (GraphNetworkError e) {
//      assertThat(e.getMessage()).isEqualTo("Failed to execute GraphQL http request");
//      assertThat(e.getCause().getClass()).isEqualTo(SocketTimeoutException.class);
//    }
//  }
//
//  @Test public void parseError() throws Exception {
//    server.enqueue(new MockResponse().setBody("Noise"));
//    try {
//      graphClient.queryGraph(shopNameQuery).execute();
//      fail("Expected GraphParseError");
//    } catch (GraphParseError e) {
//      assertThat(e.getMessage()).isEqualTo("Failed to parse GraphQL http response");
//    }
//  }
//
//  @Test public void canceledErrorBeforeExecute() throws Exception {
//    GraphCall call = graphClient.queryGraph(shopNameQuery);
//    call.cancel();
//    try {
//      call.execute();
//      fail("Expected GraphCallCanceledError");
//    } catch (GraphCallCanceledError expected) {
//      // expected
//    }
//  }
//
//  @Test public void canceledErrorDuringExecute() throws Exception {
//    final NamedCountDownLatch countDownLatch = new NamedCountDownLatch("canceledErrorDuringExecute", 1);
//    final GraphCall call = graphClient.queryGraph(shopNameQuery);
//
//    new Thread(() -> {
//      try {
//        Thread.sleep(TimeUnit.SECONDS.toMillis(1));
//      } catch (InterruptedException ignore) {
//        // ignore
//      }
//      call.cancel();
//      countDownLatch.countDown();
//    }).start();
//
//    try {
//      call.execute();
//      fail("Expected GraphCallCanceledError");
//    } catch (GraphCallCanceledError expected) {
//      // expected
//    }
//    countDownLatch.await(3, TimeUnit.SECONDS);
//  }
}
