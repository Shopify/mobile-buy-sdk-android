package com.shopify.sample.view.collections;

import android.support.annotation.NonNull;

import com.shopify.sample.R;
import com.shopify.sample.presenter.collections.Collection;
import com.shopify.sample.view.base.ListItemViewHolder;
import com.shopify.sample.view.base.ListItemViewModel;

import java.util.List;

import butterknife.BindView;

final class ProductsListItemViewModel extends ListItemViewModel<List<Collection.Product>> {
  ProductsListItemViewModel(final List<Collection.Product> payload) {
    super(payload, R.layout.collection_products_list_item);
  }

  @Override public ListItemViewHolder<List<Collection.Product>, ListItemViewModel<List<Collection.Product>>> createViewHolder(
    final ListItemViewHolder.OnClickListener onClickListener) {
    return new ItemViewHolder(onClickListener);
  }

  static final class ItemViewHolder extends ListItemViewHolder<List<Collection.Product>, ListItemViewModel<List<Collection.Product>>> {
    @BindView(R.id.product_list) ProductListView productListView;

    ItemViewHolder(@NonNull final OnClickListener onClickListener) {
      super(onClickListener);
    }

    @Override public void bindModel(@NonNull final ListItemViewModel<List<Collection.Product>> listViewItemModel) {
      super.bindModel(listViewItemModel);
      productListView.setItems(listViewItemModel.payload());
    }
  }
}
