/*
 *   The MIT License (MIT)
 *
 *   Copyright (c) 2015 Shopify Inc.
 *
 *   Permission is hereby granted, free of charge, to any person obtaining a copy
 *   of this software and associated documentation files (the "Software"), to deal
 *   in the Software without restriction, including without limitation the rights
 *   to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *   copies of the Software, and to permit persons to whom the Software is
 *   furnished to do so, subject to the following conditions:
 *
 *   The above copyright notice and this permission notice shall be included in
 *   all copies or substantial portions of the Software.
 *
 *   THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *   IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *   FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *   AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *   LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *   OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 *   THE SOFTWARE.
 */

package com.shopify.sample.domain.repository;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.shopify.buy3.GraphCall;
import com.shopify.buy3.GraphClient;
import com.shopify.buy3.Storefront;
import com.shopify.sample.SampleApplication;
import com.shopify.sample.domain.model.Collection;
import com.shopify.sample.domain.model.Product;

import java.math.BigDecimal;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

import static com.shopify.sample.RxUtil.rxGraphQueryCall;
import static com.shopify.sample.util.Util.firstItem;
import static com.shopify.sample.util.Util.mapItems;
import static com.shopify.sample.util.Util.minItem;

public final class RealCollectionRepository implements CollectionRepository {
  private final GraphClient graphClient = SampleApplication.graphClient();

  @NonNull @Override public Single<List<Collection>> fetchNextPage(@Nullable final String cursor, final int perPage) {
    GraphCall<Storefront.QueryRoot> call = graphClient.queryGraph(Storefront.query(
      root -> root.shop(
        shop -> shop.collections(
          perPage,
          args -> args
            .after(TextUtils.isEmpty(cursor) ? null : cursor)
            .sortKey(Storefront.CollectionSortKeys.TITLE),
          collectionConnection -> collectionConnection
            .edges(collectionEdge -> collectionEdge
              .cursor()
              .node(collection -> collection
                .title()
                .descriptionPlainSummary()
                .image(Storefront.ImageQuery::src)
                .products(perPage, productConnection -> productConnection
                  .edges(productEdge -> productEdge
                    .cursor()
                    .node(product -> product
                      .title()
                      .images(1, imageConnection -> imageConnection
                        .edges(imageEdge -> imageEdge
                          .node(Storefront.ImageQuery::src)))
                      .variants(250, variantConnection -> variantConnection
                        .edges(variantEdge -> variantEdge
                          .node(Storefront.ProductVariantQuery::price)))
                    )
                  )
                )
              )
            )
        )
      )
    ));
    return rxGraphQueryCall(call)
      .map(queryRoot -> queryRoot.data().getShop().getCollections().getEdges())
      .map(RealCollectionRepository::map)
      .subscribeOn(Schedulers.io());
  }

  private static List<Collection> map(final List<Storefront.CollectionEdge> edges) {
    return mapItems(edges, collectionEdge -> {
      Storefront.Collection collection = collectionEdge.getNode();
      String collectionImageUrl = collection.getImage() != null ? collection.getImage().getSrc() : null;
      return new Collection(collection.getId().toString(), collection.getTitle(), collection.getDescriptionPlainSummary(),
        collectionImageUrl, collectionEdge.getCursor(), mapItems(collection.getProducts().getEdges(), productEdge -> {
        Storefront.Product product = productEdge.getNode();
        String productImageUrl = firstItem(product.getImages() != null ? product.getImages().getEdges() : null,
          image -> image != null ? image.getNode().getSrc() : null);
        List<BigDecimal> prices = mapItems(product.getVariants().getEdges(), variantEdge -> variantEdge.getNode().getPrice());
        BigDecimal minPrice = minItem(prices, BigDecimal.ZERO, BigDecimal::compareTo);
        return new Product(product.getId().toString(), product.getTitle(), productImageUrl, minPrice, productEdge.getCursor());
      }));
    });
  }
}
