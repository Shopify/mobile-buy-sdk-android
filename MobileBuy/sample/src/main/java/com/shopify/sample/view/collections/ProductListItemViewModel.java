package com.shopify.sample.view.collections;

import android.support.annotation.NonNull;
import android.view.View;

import com.shopify.sample.R;
import com.shopify.sample.presenter.collections.Collection;
import com.shopify.sample.view.base.ListItemViewModel;
import com.shopify.sample.view.base.ListItemViewHolder;
import com.shopify.sample.view.widget.image.ShopifyDraweeView;

import butterknife.BindView;
import butterknife.OnClick;

final class ProductListItemViewModel extends ListItemViewModel<Collection.Product> {

  ProductListItemViewModel(final Collection.Product payload) {
    super(payload, R.layout.collection_product_list_item);
  }

  @Override public ListItemViewHolder<Collection.Product, ListItemViewModel<Collection.Product>> createViewHolder(
    final ListItemViewHolder.OnClickListener onClickListener) {
    return new ItemViewHolder(onClickListener);
  }

  static final class ItemViewHolder extends ListItemViewHolder<Collection.Product, ListItemViewModel<Collection.Product>> {
    @BindView(R.id.image) ShopifyDraweeView imageView;

    ItemViewHolder(@NonNull final OnClickListener onClickListener) {
      super(onClickListener);
    }

    @Override public void bindModel(@NonNull final ListItemViewModel<Collection.Product> listViewItemModel) {
      super.bindModel(listViewItemModel);
      imageView.loadShopifyImage(listViewItemModel.payload().imageUrl());
    }

    @SuppressWarnings("unchecked") @OnClick(R.id.image)
    void onImageClick(final View v) {
      onClickListener().onClick(itemModel());
    }
  }
}
