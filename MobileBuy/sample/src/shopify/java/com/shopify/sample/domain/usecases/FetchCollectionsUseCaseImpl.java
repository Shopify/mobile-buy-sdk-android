package com.shopify.sample.domain.usecases;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.shopify.buy3.GraphCallResult;
import com.shopify.buy3.GraphClient;
import com.shopify.buy3.QueryGraphCall;
import com.shopify.buy3.Storefront;
import com.shopify.sample.data.graphql.Converter;
import com.shopify.sample.data.graphql.Query;
import com.shopify.sample.domain.model.Collection;
import com.shopify.sample.util.CallbackExecutors;
import com.shopify.sample.view.Constant;
import kotlin.Unit;

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
    final QueryGraphCall call = graphClient.queryGraph(query)
        .enqueue(callbackExecutors.handler(), result -> {
          if (result instanceof GraphCallResult.Success) {
            final Storefront.Shop shop = ((GraphCallResult.Success<Storefront.QueryRoot>) result).getResponse().getData().getShop();
            callback.onResponse(Converter.convertCollections(shop.getCollections()));
          } else {
            callback.onError(((GraphCallResult.Failure) result).getError());
          }
          return Unit.INSTANCE;
        });
    return call::cancel;
  }
}
