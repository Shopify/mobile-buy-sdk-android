package com.shopify.sample.interactor.products;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.shopify.buy3.APISchema;
import com.shopify.buy3.GraphCall;
import com.shopify.buy3.GraphClient;
import com.shopify.graphql.support.ID;
import com.shopify.sample.SampleApplication;
import com.shopify.sample.presenter.products.Product;

import java.math.BigDecimal;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

import static com.shopify.sample.util.Util.firstItem;
import static com.shopify.sample.util.Util.mapItems;
import static com.shopify.sample.util.Util.minItem;

public final class RealFetchProductNextPage implements FetchProductNextPage {
  private final GraphClient graphClient = SampleApplication.graphClient();

  @NonNull @Override public Single<List<Product>> call(final String collectionId, final String cursor, final int perPage) {
    GraphCall<APISchema.QueryRoot> call = graphClient.queryGraph(APISchema.query(
      root -> root
        .node(new ID(collectionId), node -> node
          .onCollection(collectionConnection -> collectionConnection
            .products(
              perPage,
              args -> args.after(TextUtils.isEmpty(cursor) ? null : cursor),
              productConnection -> productConnection
                .edges(productEdge -> productEdge
                  .cursor()
                  .node(product -> product
                    .title()
                    .images(1, imageConnection -> imageConnection
                      .edges(imageEdge -> imageEdge
                        .node(APISchema.ImageQuery::src)
                      )
                    )
                    .variants(250, variantConnection -> variantConnection
                      .edges(variantEdge -> variantEdge
                        .node(APISchema.ProductVariantQuery::price)
                      )
                    )
                  )
                )
            )
          )
        )
    ));
    return Single.fromCallable(call::execute)
      .map(queryRoot -> ((APISchema.Collection) queryRoot.data().getNode()).getProducts().getEdges())
      .map(RealFetchProductNextPage::convert)
      .subscribeOn(Schedulers.io());
  }

  private static List<Product> convert(final List<APISchema.ProductEdge> edges) {
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
