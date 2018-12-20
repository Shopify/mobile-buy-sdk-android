package com.shopify.sample.domain.usecases;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.shopify.buy3.GraphCallResult;
import com.shopify.buy3.GraphClient;
import com.shopify.buy3.QueryGraphCall;
import com.shopify.buy3.Storefront;
import com.shopify.graphql.support.ID;
import com.shopify.sample.core.UseCase.Callback1;
import com.shopify.sample.core.UseCase.Cancelable;
import com.shopify.sample.data.graphql.Converter;
import com.shopify.sample.data.graphql.Query;
import com.shopify.sample.domain.model.Product;
import com.shopify.sample.util.CallbackExecutors;
import com.shopify.sample.view.Constant;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;

import java.util.List;

public final class FetchProductsUseCaseImpl implements FetchProductsUseCase {

  private final CallbackExecutors callbackExectors;
  private final GraphClient graphClient;

  public FetchProductsUseCaseImpl(final CallbackExecutors callbackExecutors, final GraphClient graphClient) {
    this.callbackExectors = callbackExecutors;
    this.graphClient = graphClient;
  }

  @Override
  public Cancelable execute(@NonNull final String collectionId, @Nullable final String cursor, final int perPage, @NonNull final Callback1<List<Product>> callback) {
    Storefront.QueryRootQuery query = Storefront.query(root -> root.node(new ID(collectionId), node -> node
        .onCollection(collection -> collection
            .products(args -> args
                .first(Constant.PAGE_SIZE)
                .after(cursor), Query::products
            )
        )
    ));
    final QueryGraphCall call = graphClient.queryGraph(query)
        .enqueue(callbackExectors.handler(), result -> {
          if (result instanceof GraphCallResult.Success) {
            final Storefront.Collection collection = (Storefront.Collection) ((GraphCallResult.Success<Storefront.QueryRoot>) result).getResponse().getData().getNode();
            callback.onResponse(Converter.convertProducts(collection.getProducts()));
          } else {
            callback.onError(((GraphCallResult.Failure) result).getError());
          }
          return Unit.INSTANCE;
        });
    return call::cancel;
  }
}
