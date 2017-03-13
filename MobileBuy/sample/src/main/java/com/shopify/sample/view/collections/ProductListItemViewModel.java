package com.shopify.sample.view.collections;

import android.widget.TextView;

import com.shopify.sample.R;
import com.shopify.sample.presenter.collections.Collection;
import com.shopify.sample.view.base.ListItemViewModel;
import com.shopify.sample.view.base.ListViewItemHolder;
import com.shopify.sample.view.base.ShopifyDraweeView;

import butterknife.BindView;

final class ProductListItemViewModel extends ListItemViewModel<Collection.Product> {

  ProductListItemViewModel(final Collection.Product payload) {
    super(payload, R.layout.collection_product_list_item);
  }

  @Override public ListViewItemHolder<Collection.Product, ListItemViewModel<Collection.Product>> createViewHolder() {
    return new ProductListItemViewModel.ViewItemHolder();
  }

  static final class ViewItemHolder extends ListViewItemHolder<Collection.Product, ListItemViewModel<Collection.Product>> {
    @BindView(R.id.image) ShopifyDraweeView imageView;
    @BindView(R.id.title) TextView titleView;

    @Override public void bindModel(final ListItemViewModel<Collection.Product> listViewItemModel) {
      super.bindModel(listViewItemModel);
      imageView.loadShopifyImage(listViewItemModel.payload().imageUrl());
      titleView.setText(listViewItemModel.payload().title());
    }
  }
}
