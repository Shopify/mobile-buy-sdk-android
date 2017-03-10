package com.shopify.sample.view.collections;

import com.shopify.sample.R;
import com.shopify.sample.presenter.collections.model.Collection;
import com.shopify.sample.view.base.ListItemViewModel;
import com.shopify.sample.view.base.ListViewItemHolder;

import java.util.List;

import butterknife.BindView;

final class ProductsListItemViewModel extends ListItemViewModel<List<Collection.Product>> {
  ProductsListItemViewModel(final List<Collection.Product> payload) {
    super(payload, R.layout.collection_products_list_item);
  }

  @Override public ListViewItemHolder<List<Collection.Product>, ListItemViewModel<List<Collection.Product>>> createViewHolder() {
    return new ProductsListItemViewModel.ViewItemHolder();
  }

  static final class ViewItemHolder extends ListViewItemHolder<List<Collection.Product>, ListItemViewModel<List<Collection.Product>>> {
    @BindView(R.id.product_list) ProductListView productListView;

    @Override public void bindModel(final ListItemViewModel<List<Collection.Product>> listViewItemModel) {
      super.bindModel(listViewItemModel);
      productListView.setItems(listViewItemModel.payload());
    }
  }
}
