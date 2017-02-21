package com.shopify.buy3;

import android.support.annotation.NonNull;

import com.shopify.graphql.support.Query;

import okhttp3.Call;
import okhttp3.HttpUrl;

final class RealQueryGraphCall extends RealGraphCall<APISchema.QueryRoot> implements QueryGraphCall {
  RealQueryGraphCall(final Query query, final HttpUrl serverUrl, final Call.Factory httpCallFactory) {
    super(query, serverUrl, httpCallFactory);
  }

  @NonNull @Override ResponseConverter<APISchema.QueryRoot> responseConverter() {
    return response -> new APISchema.QueryRoot(response.getData());
  }

  @NonNull @Override public GraphCall<APISchema.QueryRoot> clone() {
    return new RealQueryGraphCall(query, serverUrl, httpCallFactory);
  }
}
