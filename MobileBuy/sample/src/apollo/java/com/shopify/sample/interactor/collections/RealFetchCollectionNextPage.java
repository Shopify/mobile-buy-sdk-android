package com.shopify.sample.interactor.collections;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.apollographql.android.ApolloCall;
import com.apollographql.android.api.graphql.internal.Optional;
import com.apollographql.android.cache.http.HttpCacheControl;
import com.apollographql.android.impl.ApolloClient;
import com.shopify.sample.SampleApplication;
import com.shopify.sample.domain.CollectionsWithProducts;
import com.shopify.sample.domain.type.CollectionSortKeys;
import com.shopify.sample.presenter.collections.Collection;

import java.util.Collections;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

import static com.shopify.sample.util.Util.firstItem;
import static com.shopify.sample.util.Util.mapItems;

public final class RealFetchCollectionNextPage implements FetchCollectionNextPage {
  private final ApolloClient apolloClient = SampleApplication.apolloClient();

  @SuppressWarnings("Convert2MethodRef")
  @Override @NonNull public Single<List<Collection>> call(@Nullable final String cursor, final int perPage) {
    CollectionsWithProducts query = new CollectionsWithProducts(CollectionsWithProducts.Variables.builder()
      .perPage(perPage)
      .nextPageCursor(TextUtils.isEmpty(cursor) ? null : cursor)
      .collectionSortKey(CollectionSortKeys.TITLE)
      .build());
    ApolloCall<CollectionsWithProducts.Data> call = apolloClient.newCall(query).httpCacheControl(HttpCacheControl.CACHE_FIRST);
    return Single.fromCallable(call::execute)
      .map(response -> Optional.fromNullable(response.data()))
      .map(data -> data
        .transform(it -> it.shop())
        .transform(it -> it.collectionConnection())
        .transform(it -> it.edges())
        .or(Collections.emptyList()))
      .map(RealFetchCollectionNextPage::convert)
      .subscribeOn(Schedulers.io());
  }

  @SuppressWarnings("Convert2MethodRef")
  private static List<Collection> convert(final List<CollectionsWithProducts.Data.Shop.CollectionConnection.Edge>
    collectionEdges) {
    return mapItems(collectionEdges, collectionEdge -> {
        String collectionImageUrl = collectionEdge.collection().image().transform(it -> it.src()).or("");
        return new Collection(collectionEdge.collection().id(), collectionEdge.collection().title(), collectionImageUrl,
          collectionEdge.cursor(), mapItems(collectionEdge.collection().productConnection().edges(), productEdge -> {
          String productImageUrl = firstItem(productEdge.product().imageConnection().edges(),
            imageEdge -> imageEdge != null ? imageEdge.image().src() : null);
          return new Collection.Product(productEdge.product().id(), productEdge.product().title(), productImageUrl, productEdge.cursor());
        }));
      }
    );
  }
}
