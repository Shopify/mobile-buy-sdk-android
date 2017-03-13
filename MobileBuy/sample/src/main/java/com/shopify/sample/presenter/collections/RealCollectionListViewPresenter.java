package com.shopify.sample.presenter.collections;

import com.shopify.sample.mvp.BasePageListViewPresenter;
import com.shopify.sample.mvp.PageListViewPresenter;
import com.shopify.sample.repository.CatalogRepository;

import java.util.List;

import io.reactivex.ObservableTransformer;
import io.reactivex.schedulers.Schedulers;

public final class RealCollectionListViewPresenter extends BasePageListViewPresenter<Collection, PageListViewPresenter.View<Collection>> {
  private final CatalogRepository catalogRepository;

  public RealCollectionListViewPresenter(final CatalogRepository catalogRepository) {
    this.catalogRepository = catalogRepository;
  }

  @Override protected ObservableTransformer<String, List<Collection>> nextPageRequestComposer() {
    return upstream -> upstream.flatMapSingle(
      cursor -> catalogRepository.browseNextCollectionPage(cursor, PER_PAGE)
        .subscribeOn(Schedulers.io())
    );
  }
}
