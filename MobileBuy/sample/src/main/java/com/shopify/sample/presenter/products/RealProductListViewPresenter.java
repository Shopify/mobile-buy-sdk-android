package com.shopify.sample.presenter.products;

import com.shopify.sample.mvp.BasePageListViewPresenter;
import com.shopify.sample.mvp.PageListViewPresenter;
import com.shopify.sample.repository.CatalogRepository;

import java.util.List;

import io.reactivex.ObservableTransformer;
import io.reactivex.schedulers.Schedulers;

public final class RealProductListViewPresenter extends BasePageListViewPresenter<Product, PageListViewPresenter.View<Product>> {
  private final String collectionId;
  private final CatalogRepository catalogRepository;

  public RealProductListViewPresenter(final String collectionId, final CatalogRepository catalogRepository) {
    this.collectionId = collectionId;
    this.catalogRepository = catalogRepository;
  }

  @Override protected ObservableTransformer<String, List<Product>> nextPageRequestComposer() {
    return upstream -> upstream.flatMapSingle(
      cursor -> catalogRepository.browseNextProductPage(collectionId, cursor, PER_PAGE)
        .subscribeOn(Schedulers.io()));
  }
}
