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
import android.text.TextUtils;

import com.apollographql.apollo.ApolloClient;
import com.shopify.sample.SampleApplication;
import com.shopify.sample.domain.CollectionProducts;
import com.shopify.sample.domain.ProductDetailsQuery;
import com.shopify.sample.domain.model.Product;
import com.shopify.sample.domain.model.ProductDetails;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

import static com.shopify.sample.RxUtil.rxApolloCall;
import static com.shopify.sample.util.Util.firstItem;
import static com.shopify.sample.util.Util.mapItems;
import static com.shopify.sample.util.Util.minItem;

public final class RealProductRepository implements ProductRepository {
  private final ApolloClient apolloClient = SampleApplication.apolloClient();

  @NonNull @Override public Single<List<Product>> fetchNextPage(final String collectionId, final String cursor, final int perPage) {
    CollectionProducts query = CollectionProducts.builder()
      .perPage(perPage)
      .nextPageCursor(TextUtils.isEmpty(cursor) ? null : cursor)
      .collectionId(collectionId)
      .build();
    return rxApolloCall(apolloClient.newCall(query))
      .map(response -> response.data()
        .flatMap(it -> it.collection)
        .flatMap(it -> it.asCollection)
        .map(it -> it.productConnection)
        .map(it -> it.productEdges)
        .or(Collections.emptyList()))
      .map(RealProductRepository::map)
      .subscribeOn(Schedulers.io());
  }

  @NonNull @Override public Single<ProductDetails> fetchDetails(final String productId) {
    ProductDetailsQuery query = new ProductDetailsQuery(productId);
    return rxApolloCall(apolloClient.newCall(query))
      .map(response -> response.data()
        .flatMap(it -> it.node)
        .flatMap(it -> it.asProduct)
        .get())
      .map(RealProductRepository::map)
      .subscribeOn(Schedulers.io());
  }

  private static List<Product> map(final List<CollectionProducts.Data.ProductEdge>
    productEdges) {
    return mapItems(productEdges, productEdge -> {
      String productImageUrl = firstItem(productEdge.product.imageConnection.imageEdges,
        imageEdge -> imageEdge != null ? imageEdge.image.src : null);
      List<BigDecimal> prices = mapItems(productEdge.product.variantConnection.variantEdges,
        variantEdge -> variantEdge.variant.price);
      BigDecimal minPrice = minItem(prices, BigDecimal.ZERO, BigDecimal::compareTo);
      return new Product(productEdge.product.id, productEdge.product.title, productImageUrl, minPrice, productEdge.cursor);
    });
  }

  private static ProductDetails map(ProductDetailsQuery.Data.AsProduct product) {
    List<String> images = mapItems(product.imageConnection.imageEdge, imageEdge -> imageEdge.image.src);
    List<ProductDetails.Option> options = mapItems(product.options, option -> new ProductDetails.Option(option.id, option.name,
      option.values));
    List<ProductDetails.Variant> variants = mapItems(product.variantConnection.variantEdge,
      variantEdge -> {
        List<ProductDetails.SelectedOption> selectedOptions = mapItems(variantEdge.variant.selectedOptions, selectedOption ->
          new ProductDetails.SelectedOption(selectedOption.name, selectedOption.value));
        return new ProductDetails.Variant(variantEdge.variant.id, variantEdge.variant.title, variantEdge.variant.available.or(true),
          selectedOptions, variantEdge.variant.price);
      });
    return new ProductDetails(product.id, product.title, product.descriptionHtml, product.tags, images, options, variants);
  }
}
