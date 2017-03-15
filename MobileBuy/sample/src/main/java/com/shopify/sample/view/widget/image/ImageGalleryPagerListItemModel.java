package com.shopify.sample.view.widget.image;

import android.support.annotation.NonNull;

import com.shopify.sample.R;
import com.shopify.sample.util.Util;
import com.shopify.sample.view.base.ListItemViewModel;
import com.shopify.sample.view.base.ListItemViewHolder;

import butterknife.BindView;

final class ImageGalleryPagerListItemModel extends ListItemViewModel<String> {

  ImageGalleryPagerListItemModel(@NonNull final String image) {
    super(image, R.layout.image_gallery_pager_list_item);
    Util.checkNotNull(image, "image == null");
  }

  @Override public ListItemViewHolder<String, ListItemViewModel<String>> createViewHolder(
    final ListItemViewHolder.OnClickListener onClickListener) {
    return new ItemViewHolder(onClickListener);
  }

  static final class ItemViewHolder extends ListItemViewHolder<String, ListItemViewModel<String>> {
    @BindView(R.id.image) ShopifyDraweeView imageView;

    ItemViewHolder(@NonNull final OnClickListener onClickListener) {
      super(onClickListener);
    }

    @Override public void bindModel(@NonNull final ListItemViewModel<String> listViewItemModel) {
      super.bindModel(listViewItemModel);
      imageView.loadShopifyImage(listViewItemModel.payload());
    }
  }
}
