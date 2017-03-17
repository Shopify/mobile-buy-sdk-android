package com.shopify.sample.presenter.collections;

import com.shopify.sample.interactor.collections.FetchCollectionNextPage;
import com.shopify.sample.mvp.BasePageListViewPresenter;
import com.shopify.sample.mvp.PageListViewPresenter;

import java.util.List;

import io.reactivex.ObservableTransformer;
import io.reactivex.schedulers.Schedulers;

public final class CollectionListViewPresenter extends BasePageListViewPresenter<Collection, PageListViewPresenter.View<Collection>> {
  private final FetchCollectionNextPage fetchCollectionNextPage;

  public CollectionListViewPresenter(final FetchCollectionNextPage fetchCollectionNextPage) {
    this.fetchCollectionNextPage = fetchCollectionNextPage;
  }

  @Override protected ObservableTransformer<String, List<Collection>> nextPageRequestComposer() {
    return upstream -> upstream.flatMapSingle(
      cursor -> fetchCollectionNextPage.call(cursor, PER_PAGE)
        .subscribeOn(Schedulers.io())
    );
  }
}
