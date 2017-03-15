package com.shopify.sample.view.collections;

import android.support.annotation.NonNull;
import android.view.View;

import com.shopify.sample.R;
import com.shopify.sample.presenter.collections.Collection;
import com.shopify.sample.view.base.ListItemViewHolder;
import com.shopify.sample.view.base.ListItemViewModel;
import com.shopify.sample.view.widget.image.ShopifyDraweeView;

import butterknife.BindView;
import butterknife.OnClick;

final class CollectionImageListItemViewModel extends ListItemViewModel<Collection> {

  CollectionImageListItemViewModel(final Collection payload) {
    super(payload, R.layout.collection_image_list_item);
  }

  @Override public ListItemViewHolder<Collection, ListItemViewModel<Collection>> createViewHolder(
    final ListItemViewHolder.OnClickListener onClickListener) {
    return new ItemViewHolder(onClickListener);
  }

  static final class ItemViewHolder extends ListItemViewHolder<Collection, ListItemViewModel<Collection>> {
    @BindView(R.id.image) ShopifyDraweeView imageView;

    ItemViewHolder(@NonNull final ListItemViewHolder.OnClickListener onClickListener) {
      super(onClickListener);
    }

    @Override public void bindModel(@NonNull final ListItemViewModel<Collection> listViewItemModel) {
      super.bindModel(listViewItemModel);
      imageView.loadShopifyImage(listViewItemModel.payload().imageUrl());
    }

    @SuppressWarnings("unchecked") @OnClick(R.id.image)
    void onImageClick() {
      onClickListener().onClick(itemModel());
    }
  }
}
