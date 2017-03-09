package com.shopify.sample.repository;

import com.shopify.sample.presenter.collections.model.Collection;

import java.util.List;

import io.reactivex.Single;

public interface CatalogRepository {
  Single<List<Collection>> browseNextCollectionPage(final String cursor, final int perPage);
}
