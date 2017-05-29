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

package com.shopify.sample.domain.interactor;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.shopify.buy3.Storefront;
import com.shopify.sample.SampleApplication;
import com.shopify.sample.domain.model.Collection;
import com.shopify.sample.domain.repository.CollectionRepository;

import java.util.List;

import io.reactivex.Single;

public final class RealCollectionNextPageInteractor implements CollectionNextPageInteractor {
  private final CollectionRepository repository;

  public RealCollectionNextPageInteractor() {
    repository = new CollectionRepository(SampleApplication.graphClient());
  }

  @NonNull @Override public Single<List<Collection>> execute(@Nullable final String cursor, final int perPage) {
    Storefront.CollectionConnectionQueryDefinition query = q -> q
      .edges(collectionEdge -> collectionEdge
        .cursor()
        .node(collection -> collection
          .title()
          .description()
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
      );
    return repository
      .nextPage(cursor, perPage, query)
      .map(Converters::convertToCollections);
  }
}
