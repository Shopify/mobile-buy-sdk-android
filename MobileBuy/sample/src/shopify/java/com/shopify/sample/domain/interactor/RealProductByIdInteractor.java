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

import com.shopify.buy3.Storefront;
import com.shopify.sample.SampleApplication;
import com.shopify.sample.domain.model.ProductDetails;
import com.shopify.sample.domain.repository.ProductRepository;

import io.reactivex.Single;

import static com.shopify.sample.util.Util.checkNotNull;

public final class RealProductByIdInteractor implements ProductByIdInteractor {
  private final ProductRepository repository;

  public RealProductByIdInteractor() {
    repository = new ProductRepository(SampleApplication.graphClient());
  }

  @NonNull @Override public Single<ProductDetails> execute(@NonNull final String productId) {
    checkNotNull(productId, "productId == null");
    Storefront.ProductQueryDefinition query = it -> it
      .title()
      .descriptionHtml()
      .tags()
      .images(250, imageConnection -> imageConnection
        .edges(imageEdge -> imageEdge
          .node(Storefront.ImageQuery::src)
        )
      )
      .options(option -> option
        .name()
        .values()
      )
      .variants(250, variantConnection -> variantConnection
        .edges(variantEdge -> variantEdge
          .node(variant -> variant
            .title()
            .available()
            .selectedOptions(selectedOption -> selectedOption
              .name()
              .value()
            )
            .price()
          )
        )
      );
    return repository.product(productId, query).map(Converters::convertToProductDetails);
  }
}
