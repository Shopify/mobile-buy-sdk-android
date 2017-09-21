package com.shopify.sample.data.graphql;

import com.shopify.buy3.Storefront;

public class Query {

  private static void collection(final Storefront.CollectionQuery collection) {
    collection
      .title()
      .description()
      .image(Storefront.ImageQuery::src);
  }

  public static void collections(final Storefront.CollectionConnectionQuery collections) {
    collections
      .edges(collectionEdge -> collectionEdge
        .cursor()
        .node(Query::collection))
      .pageInfo(Storefront.PageInfoQuery::hasNextPage);
  }

  private static void product(final Storefront.ProductQuery product) {
    product
      .title()
      .images(args -> args.first(1), imageConnection -> imageConnection
        .edges(imageEdge -> imageEdge
          .node(Storefront.ImageQuery::src)
        )
      )
      .variants(args -> args.first(1), variantConnection -> variantConnection
        .edges(variantEdge -> variantEdge
          .node(Storefront.ProductVariantQuery::price)
        )
      );
  }

  public static void products(final Storefront.ProductConnectionQuery products) {
    products
      .edges(productEdge -> productEdge
        .cursor()
        .node(Query::product))
      .pageInfo(Storefront.PageInfoQuery::hasNextPage);
  }

  public static void shop(final Storefront.ShopQuery shop) {
    shop
      .name()
      .paymentSettings(paymentSettings -> paymentSettings
        .countryCode()
        .acceptedCardBrands()
      );
  }
}
