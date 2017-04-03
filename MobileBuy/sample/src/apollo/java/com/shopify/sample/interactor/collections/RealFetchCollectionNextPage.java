package com.shopify.sample.interactor.collections;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;


import com.apollographql.apollo.ApolloClient;
import com.shopify.sample.SampleApplication;
import com.shopify.sample.domain.CollectionsWithProducts;
import com.shopify.sample.domain.type.CollectionSortKeys;
import com.shopify.sample.presenter.collections.Collection;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

import static com.shopify.sample.RxUtil.rxApolloCall;
import static com.shopify.sample.util.Util.firstItem;
import static com.shopify.sample.util.Util.mapItems;
import static com.shopify.sample.util.Util.minItem;

public final class RealFetchCollectionNextPage implements FetchCollectionNextPage {
  private final ApolloClient apolloClient = SampleApplication.apolloClient();

  @SuppressWarnings("Convert2MethodRef")
  @Override @NonNull public Single<List<Collection>> call(@Nullable final String cursor, final int perPage) {
    CollectionsWithProducts query = CollectionsWithProducts.builder()
      .perPage(perPage)
      .nextPageCursor(TextUtils.isEmpty(cursor) ? null : cursor)
      .collectionSortKey(CollectionSortKeys.TITLE)
      .build();

    return rxApolloCall(apolloClient.newCall(query))
      .map(response -> response.data()
        .transform(it -> it.shop)
        .transform(it -> it.collectionConnection)
        .transform(it -> it.edges)
        .or(Collections.emptyList()))
      .map(RealFetchCollectionNextPage::convert)
      .subscribeOn(Schedulers.io());
  }

  @SuppressWarnings("Convert2MethodRef")
  private static List<Collection> convert(final List<CollectionsWithProducts.Data.Shop.CollectionConnection.Edge>
    collectionEdges) {
    return mapItems(collectionEdges, collectionEdge -> {
        String collectionImageUrl = collectionEdge.collection.image.transform(it -> it.src).or("");
        return new Collection(collectionEdge.collection.id, collectionEdge.collection.title,
          collectionEdge.collection.descriptionPlainSummary, collectionImageUrl, collectionEdge.cursor,
          mapItems(collectionEdge.collection.productConnection.edges, productEdge -> {
            String productImageUrl = firstItem(productEdge.product.imageConnection.edges,
              imageEdge -> imageEdge != null ? imageEdge.image.src : null);
            List<BigDecimal> prices = mapItems(productEdge.product.variantConnection.variantEdge,
              variantEdge -> variantEdge.variant.price);
            BigDecimal minPrice = minItem(prices, BigDecimal.ZERO, BigDecimal::compareTo);
            return new Collection.Product(productEdge.product.id, productEdge.product.title, productImageUrl, minPrice, productEdge
              .cursor);
          }));
      }
    );
  }
}
