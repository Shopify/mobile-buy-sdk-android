package com.shopify.buy3;

import android.content.Context;

import junit.framework.Assert;

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
import static junit.framework.Assert.fail;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class GraphClientBuilderTest {
  private static final String PACKAGE_NAME = "com.shopify.buy3.test";
  private static final String SHOP_DOMAIN = "shopDomain";
  private static final HttpUrl ENDPOINT_URL = HttpUrl.parse(String.format("https://%s/api/graphql", SHOP_DOMAIN));

  @Mock public Context mockContext;

  @Before public void setUp() {
    when(mockContext.getPackageName()).thenReturn(PACKAGE_NAME);
  }

  @Test public void buildSuccessWithCustomClient() {
    Interceptor networkInterceptor = new HttpLoggingInterceptor().setLevel(BuildConfig.OKHTTP_LOG_LEVEL);
    OkHttpClient httpClient = new OkHttpClient.Builder()
      .addNetworkInterceptor(networkInterceptor)
      .build();

    GraphClient graphClient = GraphClient.builder(mockContext)
      .shopDomain(SHOP_DOMAIN)
      .httpClient(httpClient)
      .authHeader("authHeader")
      .build();

    assertThat(graphClient.serverUrl).isEqualTo(ENDPOINT_URL);
    assertThat(graphClient.httpCallFactory).isNotNull();
    assertThat(graphClient.httpCallFactory).isInstanceOf(OkHttpClient.class);
    assertThat(((OkHttpClient) graphClient.httpCallFactory).networkInterceptors()).contains(networkInterceptor);
  }

  @Test public void buildSuccessWithDefaultClient() {
    GraphClient graphClient = GraphClient.builder(mockContext)
      .shopDomain(SHOP_DOMAIN)
      .authHeader("authHeader")
      .build();

    assertThat(graphClient.serverUrl).isEqualTo(ENDPOINT_URL);
    assertThat(graphClient.httpCallFactory).isNotNull();
    assertThat(graphClient.httpCallFactory).isInstanceOf(OkHttpClient.class);
  }

  @SuppressWarnings("ConstantConditions") @Test public void buildFailWithPreconditions() {
    checkForNullPointerException(() -> GraphClient.builder(null));

    GraphClient.Builder builder = GraphClient.builder(mockContext);
    checkForNullPointerException(() -> builder.shopDomain(null));
    checkForNullPointerException(() -> builder.apiKey(null));
    checkForNullPointerException(() -> builder.httpClient(null));

    checkForNullPointerException(builder::build);
    builder.shopDomain("");
    checkForNullPointerException(builder::build);
    builder.apiKey("");
    builder.build();
  }

  private void checkForNullPointerException(final Runnable action) {
    try {
      action.run();
      fail("expected NullPointerException");
    } catch (NullPointerException expected) {
    }
  }
}
