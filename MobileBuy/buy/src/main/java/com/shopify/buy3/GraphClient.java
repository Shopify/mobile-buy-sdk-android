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
import android.support.annotation.Nullable;

import com.shopify.buy3.cache.HttpCache;

import java.io.File;
import java.nio.charset.Charset;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okio.ByteString;

import static com.shopify.buy3.Utils.checkNotBlank;
import static com.shopify.buy3.Utils.checkNotNull;

/**
 * <p>Client for {@code GraphQL} server.</p>
 * Client is responsible for creating and preparing {@link GraphCall} calls, which can be used to send {@code GraphQL} operation http
 * requests.
 * <p>Internally client is based on {@link OkHttpClient} that means it holds its own connection pool and thread pool. It is
 * recommended to only create a single instance and use that for execution of all the {@code GraphQL} calls, as this would reduce latency
 * and would also save memory.</p>
 * <p>This client should be shared between calls to the same shop domain.</p>
 */
public final class GraphClient {
  /**
   * Instantiates new builder to construct new {@code GraphClient} instance.
   *
   * @param context android context
   * @return {@link GraphClient.Builder}
   */
  public static Builder builder(final Context context) {
    return new Builder(context);
  }

  final HttpUrl serverUrl;
  final Call.Factory httpCallFactory;
  final HttpCachePolicy.Policy defaultHttpCachePolicy;
  final HttpCache httpCache;
  final ScheduledExecutorService dispatcher;

  private GraphClient(@NonNull final HttpUrl serverUrl, @NonNull final Call.Factory httpCallFactory,
    @NonNull final HttpCachePolicy.Policy defaultHttpCachePolicy, @Nullable final HttpCache httpCache) {
    this.serverUrl = checkNotNull(serverUrl, "serverUrl == null");
    this.httpCallFactory = checkNotNull(httpCallFactory, "httpCallFactory == null");
    this.defaultHttpCachePolicy = checkNotNull(defaultHttpCachePolicy, "defaultCachePolicy == null");
    this.httpCache = httpCache;

    ScheduledThreadPoolExecutor threadPoolExecutor = new ScheduledThreadPoolExecutor(2,
      runnable -> new Thread(runnable, "GraphClient Dispatcher"));
    threadPoolExecutor.setKeepAliveTime(1, TimeUnit.SECONDS);
    threadPoolExecutor.allowCoreThreadTimeOut(true);
    this.dispatcher = threadPoolExecutor;
  }

  /**
   * <p>Creates call to execute {@code GraphQL} query operation.</p>
   * Creates and prepares {@link QueryGraphCall} that represents {@code GraphQL} query operation to be executed at some point in the future.
   *
   * @param query {@link Storefront.QueryRootQuery} to be executed
   * @return prepared {@link QueryGraphCall} call for later execution
   */
  public QueryGraphCall queryGraph(final Storefront.QueryRootQuery query) {
    return new RealQueryGraphCall(query, serverUrl, httpCallFactory, dispatcher, defaultHttpCachePolicy, httpCache);
  }

  /**
   * <p>Creates call to execute {@code GraphQL} mutation operation.</p>
   * Creates and prepares {@link MutationGraphCall} that represents {@code GraphQL} mutation operation to be executed at some point in the
   * future.
   *
   * @param query {@link Storefront.MutationQuery} to be executed
   * @return prepared {@link MutationGraphCall} call for later execution
   */
  public MutationGraphCall mutateGraph(final Storefront.MutationQuery query) {
    return new RealMutationGraphCall(query, serverUrl, httpCallFactory, dispatcher, HttpCachePolicy.NETWORK_ONLY, httpCache);
  }

  /**
   * Returns reference to the {@link HttpCache} used by this client.
   *
   * @return {@link HttpCache}
   */
  @Nullable public HttpCache httpCache() {
    return httpCache;
  }

  /**
   * Builds new {@code GraphClient} instance.
   */
  public static final class Builder {
    private static final long DEFAULT_HTTP_CONNECTION_TIME_OUT_MS = TimeUnit.SECONDS.toMillis(10);
    private static final long DEFAULT_HTTP_READ_WRITE_TIME_OUT_MS = TimeUnit.SECONDS.toMillis(20);

    private final String applicationName;
    private String shopDomain;
    private HttpUrl endpointUrl;
    private String accessToken;
    private OkHttpClient httpClient;
    private HttpCachePolicy.Policy defaultHttpCachePolicy = HttpCachePolicy.NETWORK_ONLY;
    private File httpCacheFolder;
    private long httpCacheMaxSize;
    private HttpCache httpCache;

    private Builder(@NonNull final Context context) {
      applicationName = checkNotNull(context, "context == null").getPackageName();
    }

    /**
     * Sets Shopify store domain URL (usually {@code {store name}.myshopify.com}).
     *
     * @param shopDomain domain for the shop
     * @return {@link GraphClient.Builder} to be used for chaining method calls
     * @throws NullPointerException     when {@code shopDomain} is null
     * @throws IllegalArgumentException when {@code shopDomain} is empty
     */
    public Builder shopDomain(@NonNull final String shopDomain) {
      this.shopDomain = checkNotBlank(shopDomain, "shopDomain == null");
      return this;
    }

    /**
     * Sets Shopify store access token obtained on your shop's admin page.
     *
     * @param accessToken store access token
     * @return {@link GraphClient.Builder} to be used for chaining method calls
     * @throws NullPointerException     when {@code accessToken} is null
     * @throws IllegalArgumentException when {@code accessToken} is empty
     */
    public Builder accessToken(@NonNull final String accessToken) {
      this.accessToken = checkNotBlank(accessToken, "accessToken == null");
      return this;
    }

    /**
     * Sets the {@link OkHttpClient} to be used as network layer for making HTTP requests.
     *
     * @param httpClient {@link OkHttpClient} client to be used
     * @return {@link GraphClient.Builder} to be used for chaining method calls
     * @throws NullPointerException when {@code httpClient} is null
     */
    public Builder httpClient(@NonNull OkHttpClient httpClient) {
      this.httpClient = checkNotNull(httpClient, "httpClient == null");
      return this;
    }

    /**
     * Sets the {@link HttpCachePolicy.Policy} to be used as default for all {@link QueryGraphCall} calls.
     *
     * @param httpCachePolicy default {@link HttpCachePolicy.Policy}
     * @return {@link GraphClient.Builder} to be used for chaining method calls
     */
    public Builder defaultHttpCachePolicy(@NonNull final HttpCachePolicy.Policy httpCachePolicy) {
      this.defaultHttpCachePolicy = checkNotNull(httpCachePolicy, "cachePolicy == null");
      return this;
    }

    /**
     * Enables http cache with provided storage settings.
     *
     * @param folder  a writable cache directory
     * @param maxSize the maximum number of bytes this cache should use to store
     * @return {@link GraphClient.Builder} to be used for chaining method calls
     * @see HttpCache
     */
    public Builder httpCache(@NonNull final File folder, long maxSize) {
      this.httpCacheFolder = checkNotNull(folder, "folder == null");
      this.httpCacheMaxSize = maxSize;
      return this;
    }

    Builder httpCache(@Nullable final HttpCache httpCache) {
      this.httpCache = httpCache;
      return this;
    }

    Builder serverUrl(@NonNull HttpUrl endpointUrl) {
      this.endpointUrl = checkNotNull(endpointUrl, "endpointUrl == null");
      return this;
    }

    /**
     * Builds the {@link GraphClient} instance with provided configuration options.
     *
     * @return configured {@link GraphClient}
     */
    public GraphClient build() {
      if (endpointUrl == null) {
        checkNotBlank(shopDomain, "shopDomain == null");
        endpointUrl = HttpUrl.parse("https://" + shopDomain + "/api/graphql");
      }

      checkNotBlank(accessToken, "apiKey == null");

      HttpCache httpCache = this.httpCache;
      if (httpCache == null && httpCacheFolder != null) {
        byte[] tmp = (endpointUrl.toString() + "/" + accessToken).getBytes(Charset.forName("UTF-8"));
        File cacheFolder = new File(httpCacheFolder, ByteString.of(tmp).md5().hex());
        httpCache = new HttpCache(cacheFolder, httpCacheMaxSize);
      }

      OkHttpClient httpClient = this.httpClient;
      if (httpClient == null) {
        httpClient = defaultOkHttpClient();
      }
      httpClient = httpClient.newBuilder().addInterceptor(sdkHeaderInterceptor(applicationName, accessToken)).build();
      if (httpCache != null) {
        httpClient = httpClient.newBuilder().addInterceptor(httpCache.httpInterceptor()).build();
      }

      return new GraphClient(endpointUrl, httpClient, defaultHttpCachePolicy, httpCache);
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
