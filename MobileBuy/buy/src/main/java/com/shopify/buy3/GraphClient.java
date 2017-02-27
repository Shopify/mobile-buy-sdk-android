package com.shopify.buy3;

import android.content.Context;
import android.support.annotation.NonNull;

import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public final class GraphClient {
  public static Builder builder(final Context context) {
    return new Builder(context);
  }

  private final HttpUrl serverUrl;
  private final Call.Factory httpCallFactory;

  private GraphClient(final String shopDomain, final Call.Factory httpCallFactory) {
    this.serverUrl = HttpUrl.parse("https://" + shopDomain + "/api/graphql");
    this.httpCallFactory = httpCallFactory;
  }

  public GraphCall<APISchema.QueryRoot> queryGraph(final APISchema.QueryRootQuery query) {
    return new RealGraphCall<>(query, serverUrl, httpCallFactory, response -> new APISchema.QueryRoot(response.getData()));
  }

  public GraphCall<APISchema.Mutation> mutateGraph(final APISchema.MutationQuery query) {
    return new RealGraphCall<>(query, serverUrl, httpCallFactory, response -> new APISchema.Mutation(response.getData()));
  }

  public static final class Builder {
    public static final long DEFAULT_HTTP_CONNECTION_TIME_OUT_MS = TimeUnit.SECONDS.toMillis(30);
    public static final long DEFAULT_HTTP_READ_WRITE_TIME_OUT_MS = TimeUnit.SECONDS.toMillis(60);

    private String shopDomain;
    private String apiKey;
    private String applicationName;
    private OkHttpClient httpClient;

    private Builder(final Context context) {
      applicationName = context.getPackageName();
    }

    /**
     * Sets store domain url (usually {store name}.myshopify.com
     *
     * @param shopDomain The domain for the shop.
     * @return a {@link GraphClient.Builder}
     */
    public Builder shopDomain(@NonNull final String shopDomain) {
      this.shopDomain = shopDomain;
      return this;
    }

    /**
     * Sets Shopify store api key
     *
     * @param apiKey The Api Key.
     * @return a {@link GraphClient.Builder}
     */
    public Builder apiKey(@NonNull final String apiKey) {
      this.apiKey = apiKey;
      return this;
    }

    public Builder httpClient(@NonNull OkHttpClient httpClient) {
      this.httpClient = httpClient;
      return this;
    }

    public GraphClient build() {
      Call.Factory httpCallFactory;
      if (httpClient == null) {
        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder()
          .connectTimeout(DEFAULT_HTTP_CONNECTION_TIME_OUT_MS, TimeUnit.MILLISECONDS)
          .readTimeout(DEFAULT_HTTP_READ_WRITE_TIME_OUT_MS, TimeUnit.MILLISECONDS)
          .writeTimeout(DEFAULT_HTTP_READ_WRITE_TIME_OUT_MS, TimeUnit.MILLISECONDS);
        httpCallFactory = configureHttpClient(httpClientBuilder, apiKey, applicationName).build();
      } else {
        httpCallFactory = configureHttpClient(httpClient, apiKey, applicationName);
      }
      return new GraphClient(shopDomain, httpCallFactory);
    }

    private OkHttpClient configureHttpClient(final OkHttpClient httpClient, final String apiKey, final String applicationName) {
      return configureHttpClient(httpClient.newBuilder(), apiKey, applicationName).build();
    }

    private static OkHttpClient.Builder configureHttpClient(final OkHttpClient.Builder httpClientBuilder, final String apiKey,
                                                            final String applicationName) {
      addAuthorizationInterceptor(httpClientBuilder, apiKey);
      addUserAgentInterceptor(httpClientBuilder, applicationName);
      return httpClientBuilder;
    }

    private static void addAuthorizationInterceptor(final OkHttpClient.Builder httpClientBuilder, final String apiKey) {
      httpClientBuilder.addInterceptor(chain -> {
        Request original = chain.request();
        Request.Builder builder = original.newBuilder().method(original.method(), original.body());
        builder.header("Authorization", Utils.formatBasicAuthorization(apiKey));
        return chain.proceed(builder.build());
      });
    }

    private static void addUserAgentInterceptor(final OkHttpClient.Builder httpClientBuilder, final String applicationName) {
      httpClientBuilder.addInterceptor(chain -> {
        Request original = chain.request();
        Request.Builder builder = original.newBuilder().method(original.method(), original.body());
        builder.header("User-Agent", "Mobile Buy SDK Android/" + BuildConfig.VERSION_NAME + "/" + applicationName);
        return chain.proceed(builder.build());
      });
    }
  }
}
