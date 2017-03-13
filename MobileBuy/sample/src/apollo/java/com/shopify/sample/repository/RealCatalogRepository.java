package com.shopify.sample.repository;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.apollographql.android.ApolloCall;
import com.apollographql.android.cache.http.HttpCacheControl;
import com.apollographql.android.impl.ApolloClient;
import com.shopify.sample.SampleApplication;
import com.shopify.sample.presenter.collections.Collection;
import com.shopify.sample.presenter.products.Product;
import com.shopify.sample.repository.type.CollectionSortKeys;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

import static com.shopify.sample.util.Util.firstItem;
import static com.shopify.sample.util.Util.mapItems;
import static com.shopify.sample.util.Util.minItem;

public final class RealCatalogRepository implements CatalogRepository {
  private final ApolloClient apolloClient = SampleApplication.apolloClient();

  @Override @NonNull public Single<List<Collection>> browseNextCollectionPage(@Nullable final String cursor, final int perPage) {
    CollectionsWithProducts query = new CollectionsWithProducts(CollectionsWithProducts.Variables.builder()
      .perPage(perPage)
      .nextPageCursor(TextUtils.isEmpty(cursor) ? null : cursor)
      .collectionSortKey(CollectionSortKeys.TITLE)
      .build());
    ApolloCall<CollectionsWithProducts.Data> call = apolloClient.newCall(query).httpCacheControl(HttpCacheControl.CACHE_FIRST);
    return Single.fromCallable(call::execute)
      .map(response -> response.data() != null
        ? response.data().shop().collectionConnection().edges()
        : Collections.<CollectionsWithProducts.Data.Shop.CollectionConnection.Edge>emptyList())
      .map(RealCatalogRepository::convertCollections)
      .subscribeOn(Schedulers.io());
  }

  @Override public Single<List<Product>> browseNextProductPage(final String collectionId, final String cursor, final int perPage) {
    CollectionProducts query = new CollectionProducts(CollectionProducts.Variables.builder()
      .perPage(perPage)
      .nextPageCursor(TextUtils.isEmpty(cursor) ? null : cursor)
      .collectionId(collectionId)
      .build());
    ApolloCall<CollectionProducts.Data> call = apolloClient.newCall(query).httpCacheControl(HttpCacheControl.CACHE_FIRST);
    return Single.fromCallable(call::execute)
      .map(response -> response.data() != null && response.data().collection().asCollection() != null
        ? response.data().collection().asCollection().productConnection().productEdges()
        : Collections.<CollectionProducts.Data.Collection.AsCollection.ProductConnection.ProductEdge>emptyList())
      .map(RealCatalogRepository::convertProducts)
      .subscribeOn(Schedulers.io());
  }

  private static List<Collection> convertCollections(final List<CollectionsWithProducts.Data.Shop.CollectionConnection.Edge>
    collectionEdges) {
    return mapItems(collectionEdges, collectionEdge -> {
        String collectionImageUrl = collectionEdge.collection().image() != null ? collectionEdge.collection().image().src() : null;
        return new Collection(collectionEdge.collection().id(), collectionEdge.collection().title(), collectionImageUrl,
          collectionEdge.cursor(), mapItems(collectionEdge.collection().productConnection().edges(), productEdge -> {
          String productImageUrl = firstItem(productEdge.product().imageConnection().edges(),
            imageEdge -> imageEdge != null ? imageEdge.image().src() : null);
          return new Collection.Product(productEdge.product().id(), productEdge.product().title(), productImageUrl, productEdge.cursor());
        }));
      }
    );
  }

  private static List<Product> convertProducts(final List<CollectionProducts.Data.Collection.AsCollection.ProductConnection.ProductEdge>
    productEdges) {
    return mapItems(productEdges, productEdge -> {
      String productImageUrl = firstItem(productEdge.product().imageConnection().imageEdges(),
        imageEdge -> imageEdge != null ? imageEdge.image().src() : null);
      List<BigDecimal> prices = mapItems(productEdge.product().variantConnection().variantEdges(),
        variantEdge -> variantEdge.variant().price());
      BigDecimal minPrice = minItem(prices, BigDecimal.ZERO, BigDecimal::compareTo);
      return new Product(productEdge.product().id(), productEdge.product().title(), productImageUrl, minPrice, productEdge.cursor());
    });
  }
}
