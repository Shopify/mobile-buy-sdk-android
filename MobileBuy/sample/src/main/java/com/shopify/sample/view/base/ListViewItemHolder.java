package com.shopify.sample.view.base;

import android.view.View;

import butterknife.ButterKnife;

public abstract class ListViewItemHolder<T, MODEL extends ListItemViewModel<T>> {
  protected MODEL mListViewItemModel;

  public void bindView(final View view) {
    ButterKnife.bind(this, view);
  }

  public void bindModel(final MODEL listViewItemModel) {
    mListViewItemModel = listViewItemModel;
  }

  public MODEL getListViewItemModel() {
    return mListViewItemModel;
  }
}
