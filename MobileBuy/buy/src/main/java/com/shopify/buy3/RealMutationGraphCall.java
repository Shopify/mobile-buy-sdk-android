package com.shopify.buy3;

import android.support.annotation.NonNull;

import com.shopify.graphql.support.Query;

import okhttp3.Call;
import okhttp3.HttpUrl;

final class RealMutationGraphCall extends RealGraphCall<APISchema.Mutation> implements MutationGraphCall {
  RealMutationGraphCall(final Query query, final HttpUrl serverUrl, final Call.Factory httpCallFactory) {
    super(query, serverUrl, httpCallFactory);
  }

  @NonNull @Override ResponseConverter<APISchema.Mutation> responseConverter() {
    return response -> new APISchema.Mutation(response.getData());
  }

  @NonNull @Override public GraphCall<APISchema.Mutation> clone() {
    return new RealMutationGraphCall(query, serverUrl, httpCallFactory);
  }
}
