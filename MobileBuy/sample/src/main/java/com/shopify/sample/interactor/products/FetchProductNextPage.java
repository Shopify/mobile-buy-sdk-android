package com.shopify.sample.interactor.products;

import android.support.annotation.NonNull;

import com.shopify.sample.presenter.products.Product;

import java.util.List;

import io.reactivex.Single;

public interface FetchProductNextPage {

  @NonNull Single<List<Product>> call(String collectionId, String cursor, int perPage);
}
