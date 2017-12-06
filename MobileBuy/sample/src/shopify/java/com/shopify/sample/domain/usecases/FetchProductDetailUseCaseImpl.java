package com.shopify.sample.domain.usecases;

import android.support.annotation.NonNull;

import com.shopify.buy3.GraphCall;
import com.shopify.buy3.GraphClient;
import com.shopify.buy3.GraphError;
import com.shopify.buy3.GraphResponse;
import com.shopify.buy3.QueryGraphCall;
import com.shopify.buy3.Storefront;
import com.shopify.graphql.support.ID;
import com.shopify.sample.core.UseCase.Callback1;
import com.shopify.sample.core.UseCase.Cancelable;
import com.shopify.sample.data.graphql.Converter;
import com.shopify.sample.data.graphql.Query;
import com.shopify.sample.domain.model.ProductDetail;
import com.shopify.sample.util.CallbackExecutors;

public class FetchProductDetailUseCaseImpl implements FetchProductDetailUseCase {

  private final CallbackExecutors callbackExecutors;
  private final GraphClient graphClient;

  public FetchProductDetailUseCaseImpl(final CallbackExecutors callbackExecutors, final GraphClient graphClient) {
    this.callbackExecutors = callbackExecutors;
    this.graphClient = graphClient;
  }

  @Override
  public Cancelable execute(@NonNull final String productId, @NonNull final Callback1<ProductDetail> callback) {
    Storefront.QueryRootQuery query = Storefront.query(root -> root.node(new ID(productId), node -> node
      .onProduct(Query::productDetail)
    ));
    final QueryGraphCall call = graphClient.queryGraph(query);
    call.enqueue(new Callback(callback), callbackExecutors.handler());
    return call::cancel;
  }

  private class Callback implements GraphCall.Callback<Storefront.QueryRoot> {

    private final Callback1<ProductDetail> callback;

    public Callback(final Callback1<ProductDetail> callback) {
      this.callback = callback;
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public void onResponse(@NonNull final GraphResponse<Storefront.QueryRoot> response) {
      final Storefront.Product product = (Storefront.Product) response.data().getNode();
      this.callback.onResponse(Converter.convertProductDetail(product));
    }

    @Override
    public void onFailure(@NonNull final GraphError error) {
      callback.onError(error);
    }
  }
}
