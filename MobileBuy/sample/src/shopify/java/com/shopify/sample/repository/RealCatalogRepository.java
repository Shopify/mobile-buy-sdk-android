package com.shopify.sample.repository;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.shopify.buy3.APISchema;
import com.shopify.buy3.GraphCall;
import com.shopify.buy3.GraphClient;
import com.shopify.graphql.support.ID;
import com.shopify.sample.SampleApplication;
import com.shopify.sample.presenter.collections.Collection;
import com.shopify.sample.presenter.products.Product;

import java.math.BigDecimal;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

import static com.shopify.sample.util.Util.firstItem;
import static com.shopify.sample.util.Util.mapItems;
import static com.shopify.sample.util.Util.minItem;

public final class RealCatalogRepository implements CatalogRepository {
  private final GraphClient graphClient = SampleApplication.graphClient();

  @Override @NonNull public Single<List<Collection>> browseNextCollectionPage(@Nullable final String cursor, final int perPage) {
    GraphCall<APISchema.QueryRoot> call = graphClient.queryGraph(APISchema.query(
      root -> root.shop(
        shop -> shop.collections(
          perPage,
          args -> args.after(TextUtils.isEmpty(cursor) ? null : cursor).sortKey(APISchema.CollectionSortKeys.TITLE),
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
      .map(RealCatalogRepository::convertCollections)
      .subscribeOn(Schedulers.io());
  }

  @Override public Single<List<Product>> browseNextProductPage(final String collectionId, final String cursor, final int perPage) {
    GraphCall<APISchema.QueryRoot> call = graphClient.queryGraph(APISchema.query(
      root -> root.node(new ID(collectionId), node ->
        node.onCollection(collectionConnection -> collectionConnection.products(
          perPage,
          args -> args.after(TextUtils.isEmpty(cursor) ? null : cursor),
          productConnection -> productConnection.edges(productEdge -> productEdge
            .cursor()
            .node(product -> product
              .title()
              .images(1, imageConnection -> imageConnection.edges(imageEdge -> imageEdge.node(APISchema.ImageQuery::src)))
              .variants(250, variantConnection -> variantConnection
                .edges(variantEdge -> variantEdge.node(APISchema.ProductVariantQuery::price))
              )
            )
          )
          )
        )
      )
    ));
    return Single.fromCallable(call::execute)
      .map(queryRoot -> ((APISchema.Collection) queryRoot.data().getNode()).getProducts().getEdges())
      .map(RealCatalogRepository::convertProducts)
      .subscribeOn(Schedulers.io());
  }

  private static List<Collection> convertCollections(final List<APISchema.CollectionEdge> edges) {
    return mapItems(edges, collectionEdge -> {
      APISchema.Collection collection = collectionEdge.getNode();
      String collectionImageUrl = collection.getImage() != null ? collection.getImage().getSrc() : null;
      return new Collection(collection.getId().toString(), collection.getTitle(), collectionImageUrl, collectionEdge.getCursor(),
        mapItems(collection.getProducts().getEdges(), productEdge -> {
          APISchema.Product product = productEdge.getNode();
          String productImageUrl = firstItem(product.getImages() != null ? product.getImages().getEdges() : null,
            image -> image != null ? image.getNode().getSrc() : null);
          return new Collection.Product(product.getId().toString(), product.getTitle(), productImageUrl, productEdge.getCursor());
        }));
    });
  }

  private static List<Product> convertProducts(final List<APISchema.ProductEdge> edges) {
    return mapItems(edges, productEdge -> {
      APISchema.Product product = productEdge.getNode();
      String productImageUrl = firstItem(product.getImages() != null ? product.getImages().getEdges() : null,
        image -> image != null ? image.getNode().getSrc() : null);
      List<BigDecimal> prices = mapItems(product.getVariants().getEdges(), variantEdge -> variantEdge.getNode().getPrice());
      BigDecimal minPrice = minItem(prices, BigDecimal.ZERO, BigDecimal::compareTo);
      return new Product(product.getId().toString(), product.getTitle(), productImageUrl, minPrice, productEdge.getCursor());
    });
  }
}
