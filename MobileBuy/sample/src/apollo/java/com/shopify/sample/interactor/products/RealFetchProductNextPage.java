package com.shopify.sample.interactor.products;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.apollographql.android.ApolloCall;
import com.apollographql.android.api.graphql.internal.Optional;
import com.apollographql.android.cache.http.HttpCacheControl;
import com.apollographql.android.impl.ApolloClient;
import com.shopify.sample.SampleApplication;
import com.shopify.sample.domain.CollectionProducts;
import com.shopify.sample.presenter.products.Product;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

import static com.shopify.sample.util.Util.firstItem;
import static com.shopify.sample.util.Util.mapItems;
import static com.shopify.sample.util.Util.minItem;

public final class RealFetchProductNextPage implements FetchProductNextPage {
  private final ApolloClient apolloClient = SampleApplication.apolloClient();

  @SuppressWarnings({"Convert2MethodRef", "ConstantConditions"})
  @Override public @NonNull Single<List<Product>> call(final String collectionId, final String cursor, final int perPage) {
    CollectionProducts query = new CollectionProducts(CollectionProducts.Variables.builder()
      .perPage(perPage)
      .nextPageCursor(TextUtils.isEmpty(cursor) ? null : cursor)
      .collectionId(collectionId)
      .build());
    ApolloCall<CollectionProducts.Data> call = apolloClient.newCall(query).httpCacheControl(HttpCacheControl.CACHE_FIRST);
    return Single.fromCallable(call::execute)
      .map(response -> Optional.fromNullable(response.data()))
      .map(data -> data
        .transform(it -> it.collection().orNull())
        .transform(it -> it.asCollection().orNull())
        .transform(it -> it.productConnection())
        .transform(it -> it.productEdges())
        .or(Collections.emptyList()))
      .map(RealFetchProductNextPage::convert)
      .subscribeOn(Schedulers.io());
  }

  private static List<Product> convert(final List<CollectionProducts.Data.Collection.AsCollection.ProductConnection.ProductEdge>
    productEdges) {
    return mapItems(productEdges, productEdge -> {
      String productImageUrl = firstItem(productEdge.product().imageConnection().imageEdges(),
        imageEdge -> imageEdge != null ? imageEdge.image().src() : null);
      List<BigDecimal> prices = mapItems(productEdge.product().variantConnection().variantEdges(),
        variantEdge -> variantEdge.variant().price());
      BigDecimal minPrice = minItem(prices, BigDecimal.ZERO, BigDecimal::compareTo);
      return new Product(productEdge.product().id(), productEdge.product().title(), productImageUrl, minPrice, productEdge.cursor());
    });
  }

}
