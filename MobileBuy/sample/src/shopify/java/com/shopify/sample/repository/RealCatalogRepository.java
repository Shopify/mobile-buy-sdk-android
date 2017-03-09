package com.shopify.sample.repository;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.shopify.buy3.APISchema;
import com.shopify.buy3.GraphCall;
import com.shopify.buy3.GraphClient;
import com.shopify.sample.SampleApplication;
import com.shopify.sample.presenter.collections.model.Collection;

import java.util.List;

import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

import static com.shopify.sample.util.Util.firstItem;
import static com.shopify.sample.util.Util.mapItems;

public final class RealCatalogRepository implements CatalogRepository {
  private final GraphClient graphClient = SampleApplication.graphClient();

  @Override public @NonNull Single<List<Collection>> browseNextCollectionPage(@Nullable final String cursor, final int perPage) {
    GraphCall<APISchema.QueryRoot> call = graphClient.queryGraph(APISchema.query(
      root -> root.shop(
        shop -> shop.collections(
          perPage,
          args -> args.after(cursor == null || cursor.isEmpty() ? null : cursor).sortKey(APISchema.CollectionSortKeys.TITLE),
          collectionConnection -> collectionConnection.edges(
            collectionEdge -> collectionEdge
              .cursor()
              .node(collection -> collection
                .title()
                .image(APISchema.ImageQuery::src)
                .products(
                  perPage,
                  productConnection -> productConnection.edges(
                    productEdge -> productEdge
                      .cursor()
                      .node(product -> product
                        .title()
                        .images(1, imageConnection -> imageConnection.edges(imageEdge -> imageEdge.node(APISchema.ImageQuery::src)))
                      )
                  )
                )
              )
          )
        )
      )
    ));
    return Single.fromCallable(call::execute)
      .map(queryRoot -> queryRoot.data().getShop().getCollections().getEdges())
      .map(this::convertCollectionsToPresenterModels)
      .subscribeOn(Schedulers.io());
  }

  private List<Collection> convertCollectionsToPresenterModels(final List<APISchema.CollectionEdge> edges) {
    return mapItems(edges, collectionEdge -> {
      APISchema.Collection collection = collectionEdge.getNode();
      String collectionImageUrl = collection.getImage() != null ? collection.getImage().getSrc() : null;
      return new Collection(collection.getId().toString(), collection.getTitle(), collectionImageUrl, collectionEdge.getCursor(),
        mapItems(collection.getProducts().getEdges(), productEdge -> {
          APISchema.Product product = productEdge.getNode();
          APISchema.ImageEdge productImage = firstItem(product.getImages() != null ? product.getImages().getEdges() : null);
          String productImageUrl = productImage != null ? productImage.getNode().getSrc() : null;
          return new Collection.Product(product.getId().toString(), product.getTitle(), productImageUrl, productEdge.getCursor());
        }));
    });
  }
}
