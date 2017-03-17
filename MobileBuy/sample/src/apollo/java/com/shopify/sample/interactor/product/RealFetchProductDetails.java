package com.shopify.sample.interactor.product;

import android.support.annotation.NonNull;

import com.apollographql.android.ApolloCall;
import com.apollographql.android.api.graphql.internal.Optional;
import com.apollographql.android.cache.http.HttpCacheControl;
import com.apollographql.android.impl.ApolloClient;
import com.shopify.sample.SampleApplication;
import com.shopify.sample.domain.ProductDetailsQuery;
import com.shopify.sample.presenter.product.Product;

import java.util.List;

import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

import static com.shopify.sample.util.Util.mapItems;

public final class RealFetchProductDetails implements FetchProductDetails {

  private final ApolloClient apolloClient = SampleApplication.apolloClient();

  @NonNull @Override public Single<Product> call(final String productId) {
    ProductDetailsQuery query = new ProductDetailsQuery(ProductDetailsQuery.Variables.builder()
      .id(productId)
      .build());

    ApolloCall<ProductDetailsQuery.Data> call = apolloClient.newCall(query).httpCacheControl(HttpCacheControl.CACHE_FIRST);
    return Single.fromCallable(call::execute)
      .map(response -> Optional.fromNullable(response.data()))
      .map(data -> data
        .transform(it -> it.node().orNull())
        .transform(it -> it.asProduct().orNull())
        .get()
      ).map(this::convert)
      .subscribeOn(Schedulers.io());
  }

  private Product convert(ProductDetailsQuery.Data.Node.AsProduct product) {
    List<String> images = mapItems(product.imageConnection().imageEdge(), imageEdge -> imageEdge.image().src());
    List<Product.Option> options = mapItems(product.options(), option -> new Product.Option(option.id(), option.name(), option.values()));
    List<Product.Variant> variants = mapItems(product.variantConnection().variantEdge(),
      variantEdge -> {
        List<Product.SelectedOption> selectedOptions = mapItems(variantEdge.variant().selectedOptions(), selectedOption ->
          new Product.SelectedOption(selectedOption.name(), selectedOption.value()));
        return new Product.Variant(variantEdge.variant().id(), variantEdge.variant().title(), variantEdge.variant().available().or(true),
          selectedOptions, variantEdge.variant().price());
      });
    return new Product(product.id(), product.title(), product.descriptionHtml(), product.tags(), images, options, variants);
  }
}
