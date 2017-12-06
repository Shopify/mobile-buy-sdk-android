package com.shopify.sample.domain.usecases;

import android.os.Handler;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.ApolloClient;
import com.apollographql.apollo.ApolloQueryCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.api.internal.Optional;
import com.apollographql.apollo.exception.ApolloException;
import com.shopify.sample.core.UseCase;
import com.shopify.sample.core.UseCase.Callback1;
import com.shopify.sample.data.apollo.Converter;
import com.shopify.sample.domain.ProductByIdQuery;
import com.shopify.sample.domain.model.ProductDetail;
import com.shopify.sample.util.CallbackExecutors;

import javax.annotation.Nonnull;

/**
 * Created by henrytao on 11/1/17.
 */

public final class FetchProductDetailUseCaseImpl implements FetchProductDetailUseCase {

  private final CallbackExecutors callbackExecutors;
  private final ApolloClient apolloClient;

  public FetchProductDetailUseCaseImpl(final CallbackExecutors callbackExecutors, final ApolloClient apolloClient) {
    this.callbackExecutors = callbackExecutors;
    this.apolloClient = apolloClient;
  }

  @Override
  public UseCase.Cancelable execute(final String productId, final Callback1<ProductDetail> callback) {
    ProductByIdQuery query = ProductByIdQuery.builder()
      .id(productId)
      .build();
    final ApolloQueryCall<Optional<ProductByIdQuery.Data>> call = apolloClient.query(query);
    call.enqueue(new Callback(callback, callbackExecutors.handler()));
    return call::cancel;
  }

  private class Callback extends ApolloCall.Callback<Optional<ProductByIdQuery.Data>> {

    private final Callback1<ProductDetail> callback;
    private final Handler handler;

    public Callback(final Callback1<ProductDetail> callback, final Handler handler) {
      this.callback = callback;
      this.handler = handler;
    }

    @Override
    public void onResponse(@Nonnull final Response<Optional<ProductByIdQuery.Data>> response) {
      handler.post(() -> callback.onResponse(Converter.convertProductDetail(response.data().get().node.get().asProduct.get())));
    }

    @Override
    public void onFailure(@Nonnull final ApolloException error) {
      handler.post(() -> callback.onError(error));
    }
  }
}
