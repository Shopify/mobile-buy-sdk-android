package com.shopify.sample.view.collections;

import android.widget.TextView;

import com.shopify.sample.R;
import com.shopify.sample.presenter.collections.Collection;
import com.shopify.sample.view.base.ListItemViewModel;
import com.shopify.sample.view.base.ListViewItemHolder;

import butterknife.BindView;

final class CollectionTitleListItemViewModel extends ListItemViewModel<Collection> {

  CollectionTitleListItemViewModel(final Collection payload) {
    super(payload, R.layout.collection_title_list_item);
  }

  @Override public ListViewItemHolder<Collection, ListItemViewModel<Collection>> createViewHolder() {
    return new ViewItemHolder();
  }

  static final class ViewItemHolder extends ListViewItemHolder<Collection, ListItemViewModel<Collection>> {
    @BindView(R.id.title) TextView titleView;

    @Override public void bindModel(final ListItemViewModel<Collection> listViewItemModel) {
      super.bindModel(listViewItemModel);
      titleView.setText(listViewItemModel.payload().title());
    }
  }
}
