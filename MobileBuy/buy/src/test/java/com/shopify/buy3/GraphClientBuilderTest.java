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

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

import static com.google.common.truth.Truth.assertThat;
import static com.shopify.buy3.TestUtils.checkForNullPointerException;
import static com.shopify.buy3.TestUtils.checkIllegalArgumentException;
import static junit.framework.Assert.fail;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class GraphClientBuilderTest {
  private static final String PACKAGE_NAME = "com.shopify.buy3.test";
  private static final String SHOP_DOMAIN = "shopDomain";
  private static final String ACCESS_TOKEN = "access_token";
  private static final HttpUrl ENDPOINT_URL = HttpUrl.parse(String.format("https://%s/api/graphql", SHOP_DOMAIN));

  @Mock public Context mockContext;

  @Before public void setUp() {
    when(mockContext.getPackageName()).thenReturn(PACKAGE_NAME);
  }

  @Test public void buildSuccessWithCustomClient() {
    Interceptor networkInterceptor = new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC);
    OkHttpClient httpClient = new OkHttpClient.Builder()
      .addNetworkInterceptor(networkInterceptor)
      .build();

    GraphClient graphClient = GraphClient.builder(mockContext)
      .shopDomain(SHOP_DOMAIN)
      .httpClient(httpClient)
      .accessToken(ACCESS_TOKEN)
      .build();

    assertThat(graphClient.serverUrl).isEqualTo(ENDPOINT_URL);
    assertThat(graphClient.httpCallFactory).isNotNull();
    assertThat(graphClient.httpCallFactory).isInstanceOf(OkHttpClient.class);
    assertThat(((OkHttpClient) graphClient.httpCallFactory).networkInterceptors()).contains(networkInterceptor);
  }

  @Test public void buildSuccessWithDefaultClient() {
    GraphClient graphClient = GraphClient.builder(mockContext)
      .shopDomain(SHOP_DOMAIN)
      .accessToken(ACCESS_TOKEN)
      .build();

    assertThat(graphClient.serverUrl).isEqualTo(ENDPOINT_URL);
    assertThat(graphClient.httpCallFactory).isNotNull();
    assertThat(graphClient.httpCallFactory).isInstanceOf(OkHttpClient.class);
  }

  @SuppressWarnings("ConstantConditions") @Test public void buildFailWithPreconditions() {
    checkForNullPointerException(() -> GraphClient.builder(null));

    checkForNullPointerException(() -> GraphClient.builder(mockContext).shopDomain(null));
    checkForNullPointerException(() -> GraphClient.builder(mockContext).accessToken(null));
    checkForNullPointerException(() -> GraphClient.builder(mockContext).httpClient(null));

    checkIllegalArgumentException(() -> GraphClient.builder(mockContext).shopDomain(""));
    checkIllegalArgumentException(() -> GraphClient.builder(mockContext).accessToken(""));

    checkForNullPointerException(() -> GraphClient.builder(mockContext).build());
    checkForNullPointerException(() -> GraphClient.builder(mockContext).shopDomain(SHOP_DOMAIN).build());

    checkForNullPointerException(() -> GraphClient.builder(mockContext).defaultHttpCachePolicy(null));
  }
}
