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

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;

import static com.shopify.buy3.Utils.checkNotBlank;
import static com.shopify.buy3.Utils.checkNotNull;

public final class GraphClient {
  public static Builder builder(final Context context) {
    return new Builder(context);
  }

  final HttpUrl serverUrl;
  final Call.Factory httpCallFactory;
  final ScheduledExecutorService dispatcher;

  private GraphClient(final Builder builder) {
    this.serverUrl = builder.endpointUrl;
    this.httpCallFactory = builder.httpClient;

    if (builder.dispatcher == null) {
      ScheduledThreadPoolExecutor threadPoolExecutor = new ScheduledThreadPoolExecutor(2,
        runnable -> new Thread(runnable, "GraphClient Dispatcher"));
      threadPoolExecutor.setKeepAliveTime(1, TimeUnit.SECONDS);
      threadPoolExecutor.allowCoreThreadTimeOut(true);
      this.dispatcher = threadPoolExecutor;
    } else {
      this.dispatcher = builder.dispatcher;
    }
  }

  public GraphCall<Storefront.QueryRoot> queryGraph(final Storefront.QueryRootQuery query) {
    return new RealGraphCall<>(query, serverUrl, httpCallFactory, response -> new Storefront.QueryRoot(response.getData()), dispatcher);
  }

  public GraphCall<Storefront.Mutation> mutateGraph(final Storefront.MutationQuery query) {
    return new RealGraphCall<>(query, serverUrl, httpCallFactory, response -> new Storefront.Mutation(response.getData()), dispatcher);
  }

  public static final class Builder {
    private static final long DEFAULT_HTTP_CONNECTION_TIME_OUT_MS = TimeUnit.SECONDS.toMillis(10);
    private static final long DEFAULT_HTTP_READ_WRITE_TIME_OUT_MS = TimeUnit.SECONDS.toMillis(20);

    private final String applicationName;
    private String shopDomain;
    private HttpUrl endpointUrl;
    private String accessToken;
    private OkHttpClient httpClient;
    private ScheduledExecutorService dispatcher;
    private Interceptor sdkHeaderInterceptor;

    private Builder(@NonNull final Context context) {
      applicationName = checkNotNull(context, "context == null").getPackageName();
    }

    /**
     * Sets store domain url (usually {store name}.myshopify.com
     *
     * @param shopDomain The domain for the shop.
     * @return a {@link GraphClient.Builder}
     */
    public Builder shopDomain(@NonNull final String shopDomain) {
      this.shopDomain = checkNotBlank(shopDomain, "shopDomain == null");
      return this;
    }

    /**
     * Sets Shopify store access token
     *
     * @param accessToken store access token
     * @return a {@link GraphClient.Builder}
     */
    public Builder accessToken(@NonNull final String accessToken) {
      this.accessToken = checkNotBlank(accessToken, "accessToken == null");
      return this;
    }

    public Builder httpClient(@NonNull OkHttpClient httpClient) {
      this.httpClient = checkNotNull(httpClient, "httpClient == null");
      return this;
    }

    Builder serverUrl(@NonNull HttpUrl endpointUrl) {
      this.endpointUrl = checkNotNull(endpointUrl, "endpointUrl == null");
      return this;
    }

    Builder dispatcher(@NonNull final ScheduledExecutorService dispatcher) {
      this.dispatcher = checkNotNull(dispatcher, "dispatcher == null");
      return this;
    }

    public GraphClient build() {
      if (endpointUrl == null) {
        checkNotBlank(shopDomain, "shopDomain == null");
        endpointUrl = HttpUrl.parse("https://" + shopDomain + "/api/graphql");
      }

      checkNotBlank(accessToken, "apiKey == null");

      if (sdkHeaderInterceptor == null) {
        sdkHeaderInterceptor = sdkHeaderInterceptor(applicationName, accessToken);
      }

      if (httpClient == null) {
        httpClient = defaultOkHttpClient();
      }

      if (!httpClient.interceptors().contains(sdkHeaderInterceptor)) {
        httpClient = httpClient.newBuilder().addInterceptor(sdkHeaderInterceptor).build();
      }

      return new GraphClient(this);
    }

    OkHttpClient defaultOkHttpClient() {
      return new OkHttpClient.Builder()
        .connectTimeout(DEFAULT_HTTP_CONNECTION_TIME_OUT_MS, TimeUnit.MILLISECONDS)
        .readTimeout(DEFAULT_HTTP_READ_WRITE_TIME_OUT_MS, TimeUnit.MILLISECONDS)
        .writeTimeout(DEFAULT_HTTP_READ_WRITE_TIME_OUT_MS, TimeUnit.MILLISECONDS)
        .build();
    }

    private static Interceptor sdkHeaderInterceptor(final String applicationName, final String accessToken) {
      return chain -> {
        Request original = chain.request();
        Request.Builder builder = original.newBuilder().method(original.method(), original.body());
        builder.header("User-Agent", "Mobile Buy SDK Android/" + BuildConfig.VERSION_NAME + "/" + applicationName);
        builder.header("X-SDK-Version", BuildConfig.VERSION_NAME);
        builder.header("X-SDK-Variant", "android");
        builder.header("X-Shopify-Storefront-Access-Token", accessToken);
        return chain.proceed(builder.build());
      };
    }
  }
}
