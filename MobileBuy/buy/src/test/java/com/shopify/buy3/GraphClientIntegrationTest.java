package com.shopify.buy3;

import android.content.Context;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.concurrent.atomic.AtomicReference;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class GraphClientIntegrationTest {
  private static final String PACKAGE_NAME = "com.shopify.buy3.test";
  private static final String AUTH_HEADER = "Basic YXBpS2V5";

  @Mock public Context mockContext;
  @Rule public MockWebServer server = new MockWebServer();
  private GraphClient graphClient;
  private final AtomicReference<Response> lastHttResponse = new AtomicReference<>();

  @Before public void setUp() {
    when(mockContext.getPackageName()).thenReturn(PACKAGE_NAME);

    OkHttpClient httpClient = new OkHttpClient.Builder()
      .addInterceptor(chain -> {
        Request request = chain.request();
        okhttp3.Response response = chain.proceed(request);
        lastHttResponse.set(response);
        return response;
      })
      .build();

    graphClient = GraphClient.builder(mockContext)
      .authHeader(AUTH_HEADER)
      .httpClient(httpClient)
      .serverUrl(server.url("/"))
      .build();
  }

  @Test public void headerInterceptors() throws InterruptedException {
    server.enqueue(new MockResponse().setResponseCode(500).setBody(""));
    try {
      graphClient.queryGraph(APISchema.query(root -> root.shop(APISchema.ShopQuery::name))).execute();
      Assert.fail("expected exception to be thrown");
    } catch (GraphError error) {
      // ignore
    }

    assertThat(lastHttResponse.get().request().header("Authorization")).isEqualTo(AUTH_HEADER);
    assertThat(lastHttResponse.get().request().header("User-Agent")).isEqualTo("Mobile Buy SDK Android/" + BuildConfig.VERSION_NAME + "/" +
      PACKAGE_NAME);
  }
}
