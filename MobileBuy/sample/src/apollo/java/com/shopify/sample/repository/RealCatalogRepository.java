package com.shopify.sample.repository;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.apollographql.android.ApolloCall;
import com.apollographql.android.cache.http.HttpCacheControl;
import com.apollographql.android.impl.ApolloClient;
import com.shopify.sample.SampleApplication;
import com.shopify.sample.presenter.collections.model.Collection;
import com.shopify.sample.repository.type.CollectionSortKeys;

import java.util.List;

import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

import static com.shopify.sample.util.Util.firstItem;
import static com.shopify.sample.util.Util.mapItems;

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
      .map(response -> response.data().shop().collectionConnection())
      .map(RealCatalogRepository::convertCollectionsToPresenterModels)
      .subscribeOn(Schedulers.io());
  }

  private static List<Collection> convertCollectionsToPresenterModels(CollectionsWithProducts.Data.Shop.CollectionConnection collections) {
    return mapItems(collections.edges(), collectionEdge -> {
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
}
