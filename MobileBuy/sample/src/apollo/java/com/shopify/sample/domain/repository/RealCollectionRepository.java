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

import com.apollographql.apollo.ApolloClient;
import com.shopify.sample.SampleApplication;
import com.shopify.sample.domain.CollectionsWithProducts;
import com.shopify.sample.domain.model.Collection;
import com.shopify.sample.domain.model.Product;
import com.shopify.sample.domain.type.CollectionSortKeys;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

import static com.shopify.sample.RxUtil.rxApolloCall;
import static com.shopify.sample.util.Util.firstItem;
import static com.shopify.sample.util.Util.mapItems;
import static com.shopify.sample.util.Util.minItem;

public final class RealCollectionRepository implements CollectionRepository {
  private final ApolloClient apolloClient = SampleApplication.apolloClient();

  @NonNull @Override public Single<List<Collection>> fetchNextPage(@Nullable final String cursor, final int perPage) {
    CollectionsWithProducts query = CollectionsWithProducts.builder()
      .perPage(perPage)
      .nextPageCursor(TextUtils.isEmpty(cursor) ? "" : cursor)
      .collectionSortKey(CollectionSortKeys.TITLE)
      .build();

    return rxApolloCall(apolloClient.newCall(query))
      .map(response -> response.data()
        .map(it -> it.shop)
        .map(it -> it.collectionConnection)
        .map(it -> it.edges)
        .or(Collections.emptyList()))
      .map(RealCollectionRepository::map)
      .subscribeOn(Schedulers.io());
  }

  @SuppressWarnings("Convert2MethodRef")
  private static List<Collection> map(final List<CollectionsWithProducts.Data.Edge>
    collectionEdges) {
    return mapItems(collectionEdges, collectionEdge -> {
        String collectionImageUrl = collectionEdge.collection.image.transform(it -> it.src).or("");
        return new Collection(collectionEdge.collection.id, collectionEdge.collection.title,
          collectionEdge.collection.descriptionHtml, collectionImageUrl, collectionEdge.cursor,
          mapItems(collectionEdge.collection.productConnection.edges, productEdge -> {
            String productImageUrl = firstItem(productEdge.product.imageConnection.edges,
              imageEdge -> imageEdge != null ? imageEdge.image.src : null);
            List<BigDecimal> prices = mapItems(productEdge.product.variantConnection.variantEdge,
              variantEdge -> variantEdge.variant.price);
            BigDecimal minPrice = minItem(prices, BigDecimal.ZERO, BigDecimal::compareTo);
            return new Product(productEdge.product.id, productEdge.product.title, productImageUrl, minPrice, productEdge.cursor);
          }));
      }
    );
  }
}
