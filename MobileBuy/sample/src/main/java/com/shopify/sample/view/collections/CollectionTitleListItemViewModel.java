package com.shopify.sample.view.collections;

import android.support.annotation.NonNull;
import android.widget.TextView;

import com.shopify.sample.R;
import com.shopify.sample.presenter.collections.Collection;
import com.shopify.sample.view.base.ListItemViewModel;
import com.shopify.sample.view.base.ListItemViewHolder;

import butterknife.BindView;

final class CollectionTitleListItemViewModel extends ListItemViewModel<Collection> {

  CollectionTitleListItemViewModel(final Collection payload) {
    super(payload, R.layout.collection_title_list_item);
  }

  @Override public ListItemViewHolder<Collection, ListItemViewModel<Collection>> createViewHolder(
    final ListItemViewHolder.OnClickListener onClickListener) {
    return new ItemViewHolder(onClickListener);
  }

  static final class ItemViewHolder extends ListItemViewHolder<Collection, ListItemViewModel<Collection>> {
    @BindView(R.id.title) TextView titleView;

    ItemViewHolder(@NonNull final OnClickListener onClickListener) {
      super(onClickListener);
    }

    @Override public void bindModel(@NonNull final ListItemViewModel<Collection> listViewItemModel) {
      super.bindModel(listViewItemModel);
      titleView.setText(listViewItemModel.payload().title());
    }
  }
}
