package com.shopify.sample.interactor.product;

import android.support.annotation.NonNull;

import com.shopify.sample.presenter.product.Product;

import io.reactivex.Single;

public interface FetchProductDetails {

  @NonNull Single<Product> call(String productId);
}
