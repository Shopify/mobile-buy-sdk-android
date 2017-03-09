package com.shopify.sample.presenter.collections;

import com.shopify.sample.mvp.ViewPresenter;
import com.shopify.sample.view.base.ListItemViewModel;

import java.util.List;

import io.reactivex.Observable;

public interface CollectionListViewPresenter extends ViewPresenter<CollectionListViewPresenter.View> {
  int REQUEST_ID_NEXT_PAGE = 1;
  int PER_PAGE = 10;

  void reset();

  interface View extends com.shopify.sample.mvp.View {
    Observable<String> nextPageObservable();

    void addItems(List<ListItemViewModel> items);

    void clearItems();
  }
}
