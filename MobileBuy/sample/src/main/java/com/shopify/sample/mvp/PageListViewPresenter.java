package com.shopify.sample.mvp;

import java.util.List;

import io.reactivex.Observable;

public interface PageListViewPresenter<ITEM, VIEW extends PageListViewPresenter.View<ITEM>> extends ViewPresenter<VIEW> {
  int REQUEST_ID_NEXT_PAGE = 1;
  int PER_PAGE = 10;

  void reset();

  interface View<ITEM> extends com.shopify.sample.mvp.View {
    Observable<String> nextPageObservable();

    void addItems(List<ITEM> items);

    void clearItems();
  }
}
