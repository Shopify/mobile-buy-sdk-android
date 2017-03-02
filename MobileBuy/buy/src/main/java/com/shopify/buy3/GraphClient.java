package com.shopify.buy3;

import android.content.Context;
import android.support.annotation.NonNull;

import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;

import static com.shopify.buy3.Utils.checkNotNull;

public final class GraphClient {
  public static Builder builder(final Context context) {
    return new Builder(context);
  }

  final HttpUrl serverUrl;
  final Call.Factory httpCallFactory;

  private GraphClient(final HttpUrl serverUrl, final Call.Factory httpCallFactory) {
    this.serverUrl = serverUrl;
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
    private HttpUrl endpointUrl;
    private String apiKey;
    private String authHeader;
    private String applicationName;
    private OkHttpClient httpClient;

    private Builder(@NonNull final Context context) {
      checkNotNull(context, "context == null");
      applicationName = context.getPackageName();
    }

    /**
     * Sets store domain url (usually {store name}.myshopify.com
     *
     * @param shopDomain The domain for the shop.
     * @return a {@link GraphClient.Builder}
     */
    public Builder shopDomain(@NonNull final String shopDomain) {
      checkNotNull(shopDomain, "shopDomain == null");
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
      checkNotNull(apiKey, "apiKey == null");
      this.apiKey = apiKey;
      return this;
    }

    public Builder httpClient(@NonNull OkHttpClient httpClient) {
      checkNotNull(httpClient, "httpClient == null");
      this.httpClient = httpClient;
      return this;
    }

    Builder serverUrl(@NonNull HttpUrl endpointUrl) {
      checkNotNull(endpointUrl, "endpointUrl == null");
      this.endpointUrl = endpointUrl;
      return this;
    }

    Builder authHeader(@NonNull String authHeader) {
      checkNotNull(authHeader, "authHeader == null");
      this.authHeader = authHeader;
      return this;
    }

    public GraphClient build() {
      if (endpointUrl == null) {
        checkNotNull(shopDomain, "shopDomain == null");
        endpointUrl = HttpUrl.parse("https://" + shopDomain + "/api/graphql");
      }

      if (authHeader == null) {
        checkNotNull(apiKey, "apiKey == null");
        authHeader = Utils.formatBasicAuthorization(apiKey);
      }

      return new GraphClient(endpointUrl, buildOkHttpClient());
    }

    private OkHttpClient buildOkHttpClient() {
      if (httpClient == null) {
        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder()
          .connectTimeout(DEFAULT_HTTP_CONNECTION_TIME_OUT_MS, TimeUnit.MILLISECONDS)
          .readTimeout(DEFAULT_HTTP_READ_WRITE_TIME_OUT_MS, TimeUnit.MILLISECONDS)
          .writeTimeout(DEFAULT_HTTP_READ_WRITE_TIME_OUT_MS, TimeUnit.MILLISECONDS);
        return configureHttpClient(httpClientBuilder, authHeader, applicationName).build();
      } else {
        return configureHttpClient(httpClient, authHeader, applicationName);
      }
    }

    private OkHttpClient configureHttpClient(final OkHttpClient httpClient, final String authHeader, final String applicationName) {
      return configureHttpClient(httpClient.newBuilder(), authHeader, applicationName).build();
    }

    private static OkHttpClient.Builder configureHttpClient(final OkHttpClient.Builder httpClientBuilder, final String authHeader,
                                                            final String applicationName) {
      addAuthorizationInterceptor(httpClientBuilder, authHeader);
      addUserAgentInterceptor(httpClientBuilder, applicationName);
      return httpClientBuilder;
    }

    private static void addAuthorizationInterceptor(final OkHttpClient.Builder httpClientBuilder, final String authHeader) {
      httpClientBuilder.addInterceptor(chain -> {
        Request original = chain.request();
        Request.Builder builder = original.newBuilder().method(original.method(), original.body());
        builder.header("Authorization", authHeader);
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
