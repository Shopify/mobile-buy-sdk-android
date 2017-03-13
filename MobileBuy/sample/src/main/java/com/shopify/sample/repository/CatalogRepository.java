package com.shopify.sample.repository;

import com.shopify.sample.presenter.collections.Collection;
import com.shopify.sample.presenter.products.Product;

import java.util.List;

import io.reactivex.Single;

public interface CatalogRepository {

  Single<List<Collection>> browseNextCollectionPage(String cursor, int perPage);

  Single<List<Product>> browseNextProductPage(String collectionId, String cursor, int perPage);
}
