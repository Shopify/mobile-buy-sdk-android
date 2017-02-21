package com.shopify.buy3;

import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public final class GraphClient implements GraphCallFactory {
  private final HttpUrl serverUrl;
  private final Call.Factory httpCallFactory;

  private GraphClient(final String shopDomain, final String apiKey, final String applicationName, final long httpConnectionTimeoutMs,
                      final long httpReadWriteTimeoutMs, final Interceptor[] httpInterceptors) {
    this.serverUrl = HttpUrl.parse("https://" + shopDomain + "/api/graphql");
    this.httpCallFactory = buildHttpCallFactory(apiKey, applicationName, httpConnectionTimeoutMs, httpReadWriteTimeoutMs, httpInterceptors);
  }

  @Override public GraphCall<APISchema.QueryRoot> queryGraph(final APISchema.QueryRootQuery query) {
    return new RealGraphCall<>(query, serverUrl, httpCallFactory, response -> new APISchema.QueryRoot(response.getData()));
  }

  @Override public GraphCall<APISchema.Mutation> mutateGraph(final APISchema.MutationQuery query) {
    return new RealGraphCall<>(query, serverUrl, httpCallFactory, response -> new APISchema.Mutation(response.getData()));
  }

  private OkHttpClient buildHttpCallFactory(final String apiKey, final String applicationName, final long httpConnectionTimeoutMs,
                                            final long httpReadWriteTimeoutMs, final Interceptor[] httpInterceptors) {
    final Interceptor requestInterceptor = chain -> {
      final Request original = chain.request();
      final Request.Builder builder = original.newBuilder().method(original.method(), original.body());
      builder.header("Authorization", Utils.formatBasicAuthorization(apiKey));

      // Using the full package name for BuildConfig here as a work around for Javadoc.
      builder.header("User-Agent", "Mobile Buy SDK Android/" + BuildConfig.VERSION_NAME + "/" + applicationName);

      return chain.proceed(builder.build());
    };

    final OkHttpClient.Builder builder = new OkHttpClient.Builder()
      .connectTimeout(httpConnectionTimeoutMs, TimeUnit.MILLISECONDS)
      .readTimeout(httpReadWriteTimeoutMs, TimeUnit.MILLISECONDS)
      .writeTimeout(httpReadWriteTimeoutMs, TimeUnit.MILLISECONDS)
      .addInterceptor(requestInterceptor);

    if (httpInterceptors != null) {
      for (Interceptor interceptor : httpInterceptors) {
        builder.addInterceptor(interceptor);
      }
    }

    return builder.build();
  }

  public static final class Builder {
    public static final long DEFAULT_HTTP_CONNECTION_TIME_OUT_MS = TimeUnit.SECONDS.toMillis(30);
    public static final long DEFAULT_HTTP_READ_WRITE_TIME_OUT_MS = TimeUnit.SECONDS.toMillis(60);

    private String shopDomain;
    private String apiKey;
    private String applicationName;
    private Interceptor[] httpInterceptors;
    private long httpConnectionTimeoutMs = DEFAULT_HTTP_CONNECTION_TIME_OUT_MS;
    private long httpReadWriteTimeoutMs = DEFAULT_HTTP_READ_WRITE_TIME_OUT_MS;

    /**
     * Sets store domain url (usually {store name}.myshopify.com
     *
     * @param shopDomain The domain for the shop.
     * @return a {@link BuyClientBuilder}
     */
    public Builder shopDomain(final String shopDomain) {
      this.shopDomain = shopDomain;
      return this;
    }

    /**
     * Sets Shopify store api key
     *
     * @param apiKey The Api Key.
     * @return a {@link BuyClientBuilder}
     */
    public Builder apiKey(final String apiKey) {
      this.apiKey = apiKey;
      return this;
    }

    /**
     * Sets Shopify store application name
     *
     * @param applicationName The application name.
     * @return a {@link BuyClientBuilder}
     */
    public Builder applicationName(final String applicationName) {
      this.applicationName = applicationName;
      return this;
    }

    /**
     * Sets custom OkHttp httpInterceptors
     *
     * @param httpInterceptors Interceptors to add to the OkHttp client.
     * @return a {@link BuyClientBuilder}
     */
    public Builder httpInterceptors(final Interceptor... httpInterceptors) {
      this.httpInterceptors = httpInterceptors;
      return this;
    }

    /**
     * Sets the default http timeouts for new connections.
     * A value of 0 means no timeout, otherwise values must be between 1 and Long.MAX_VALUE.
     *
     * @param httpConnectionTimeoutMs default connect timeout for new connections in milliseconds
     * @param httpReadWriteTimeoutMs  default read/write timeout for new connections in milliseconds
     * @return {@link BuyClientBuilder}
     */
    public Builder httpTimeout(final long httpConnectionTimeoutMs, final long httpReadWriteTimeoutMs) {
      this.httpConnectionTimeoutMs = httpConnectionTimeoutMs;
      this.httpReadWriteTimeoutMs = httpReadWriteTimeoutMs;
      return this;
    }

    public GraphClient build() {
      return new GraphClient(
        shopDomain,
        apiKey,
        applicationName,
        httpConnectionTimeoutMs,
        httpReadWriteTimeoutMs,
        httpInterceptors
      );
    }
  }
}
