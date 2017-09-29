package com.shopify.sample.domain.usecases;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.shopify.buy3.GraphCall;
import com.shopify.buy3.GraphClient;
import com.shopify.buy3.GraphError;
import com.shopify.buy3.GraphResponse;
import com.shopify.buy3.QueryGraphCall;
import com.shopify.buy3.Storefront;
import com.shopify.sample.view.Constant;
import com.shopify.sample.data.graphql.Converter;
import com.shopify.sample.data.graphql.Query;
import com.shopify.sample.domain.model.Collection;
import com.shopify.sample.util.CallbackExecutors;

import java.util.List;

public final class FetchCollectionsUseCaseImpl implements FetchCollectionsUseCase {

  private final CallbackExecutors callbackExecutors;
  private final GraphClient graphClient;

  public FetchCollectionsUseCaseImpl(final CallbackExecutors callbackExecutors, final GraphClient graphClient) {
    this.callbackExecutors = callbackExecutors;
    this.graphClient = graphClient;
  }

  @Override
  public Cancelable execute(@Nullable final String cursor, final int perPage, @NonNull final Callback1<List<Collection>> callback) {
    Storefront.QueryRootQuery query = Storefront.query(root -> root
      .shop(shop -> shop
        .collections(
          args -> args
            .first(Constant.PAGE_SIZE)
            .after(cursor)
            .sortKey(Storefront.CollectionSortKeys.TITLE),
          collections -> {
            Query.collections(collections);
            collections
              .edges(collectionEdge -> collectionEdge
                .node(collection -> collection
                  .products(args -> args.first(Constant.PAGE_SIZE), Query::products)
                )
              );
          }
        )
      )
    );
    final QueryGraphCall call = graphClient.queryGraph(query);
    call.enqueue(new Callback(callback), callbackExecutors.handler());
    return call::cancel;
  }

  private static class Callback implements GraphCall.Callback<Storefront.QueryRoot> {

    private final Callback1<List<Collection>> callback;

    public Callback(final Callback1<List<Collection>> callback) {
      this.callback = callback;
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public void onResponse(@NonNull final GraphResponse<Storefront.QueryRoot> response) {
      final Storefront.Shop shop = response.data().getShop();
      this.callback.onResponse(Converter.convertCollections(shop.getCollections()));
    }

    @Override
    public void onFailure(@NonNull final GraphError error) {
      callback.onError(error);
    }
  }
}
