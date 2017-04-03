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

package com.shopify.sample.interactor.product;

import android.support.annotation.NonNull;

import com.shopify.buy3.GraphCall;
import com.shopify.buy3.GraphClient;
import com.shopify.buy3.Storefront;
import com.shopify.graphql.support.ID;
import com.shopify.sample.SampleApplication;
import com.shopify.sample.presenter.product.Product;

import java.util.List;

import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

import static com.shopify.sample.RxUtil.rxGraphQueryCall;
import static com.shopify.sample.util.Util.mapItems;

public final class RealFetchProductDetails implements FetchProductDetails {
  private final GraphClient graphClient = SampleApplication.graphClient();

  @NonNull @Override public Single<Product> call(final String productId) {
    GraphCall<Storefront.QueryRoot> call = graphClient.queryGraph(Storefront.query(
      root -> root
        .node(new ID(productId), node -> node
          .onProduct(product -> product
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
            )
          )
        )
    ));
    return rxGraphQueryCall(call)
      .map(queryRoot -> ((Storefront.Product) queryRoot.data().getNode()))
      .map(RealFetchProductDetails::convert)
      .subscribeOn(Schedulers.io());
  }

  private static Product convert(final Storefront.Product product) {
    List<String> images = mapItems(product.getImages().getEdges(), imageEdge -> imageEdge.getNode().getSrc());
    List<Product.Option> options = mapItems(product.getOptions(), option -> new Product.Option(option.getId().toString(), option.getName(),
      option.getValues()));
    List<Product.Variant> variants = mapItems(product.getVariants().getEdges(),
      variantEdge -> {
        List<Product.SelectedOption> selectedOptions = mapItems(variantEdge.getNode().getSelectedOptions(), selectedOption ->
          new Product.SelectedOption(selectedOption.getName(), selectedOption.getValue()));
        return new Product.Variant(variantEdge.getNode().getId().toString(), variantEdge.getNode().getTitle(),
          (variantEdge.getNode().getAvailable() == null || Boolean.TRUE.equals(variantEdge.getNode().getAvailable())),
          selectedOptions, variantEdge.getNode().getPrice());
      });
    return new Product(product.getId().toString(), product.getTitle(), product.getDescriptionHtml(), product.getTags(), images, options,
      variants);
  }
}
