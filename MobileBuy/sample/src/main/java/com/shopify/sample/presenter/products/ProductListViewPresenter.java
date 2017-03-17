package com.shopify.sample.presenter.products;

import com.shopify.sample.interactor.products.FetchProductNextPage;
import com.shopify.sample.mvp.BasePageListViewPresenter;
import com.shopify.sample.mvp.PageListViewPresenter;

import java.util.List;

import io.reactivex.ObservableTransformer;
import io.reactivex.schedulers.Schedulers;

public final class ProductListViewPresenter extends BasePageListViewPresenter<Product, PageListViewPresenter.View<Product>> {
  private final String collectionId;
  private final FetchProductNextPage fetchProductNextPage;

  public ProductListViewPresenter(final String collectionId, final FetchProductNextPage fetchProductNextPage) {
    this.collectionId = collectionId;
    this.fetchProductNextPage = fetchProductNextPage;
  }

  @Override protected ObservableTransformer<String, List<Product>> nextPageRequestComposer() {
    return upstream -> upstream.flatMapSingle(
      cursor -> fetchProductNextPage.call(collectionId, cursor, PER_PAGE * 2)
        .subscribeOn(Schedulers.io()));
  }
}
