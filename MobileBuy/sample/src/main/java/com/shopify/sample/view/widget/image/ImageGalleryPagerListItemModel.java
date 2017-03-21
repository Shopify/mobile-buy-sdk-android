/*
 *   The MIT License (MIT)
 *
 *   Copyright (c) 2015 Shopify Inc.
 *
 *   Permission is hereby granted, free of charge, to any person obtaining a copy
 *   of this software and associated documentation files (the "Software"), to deal
 *   in the Software without restriction, including without limitation the rights
 *   to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *   copies of the Software, and to permit persons to whom the Software is
 *   furnished to do so, subject to the following conditions:
 *
 *   The above copyright notice and this permission notice shall be included in
 *   all copies or substantial portions of the Software.
 *
 *   THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *   IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *   FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *   AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *   LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *   OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 *   THE SOFTWARE.
 */

package com.shopify.sample.view.widget.image;

import android.support.annotation.NonNull;

import com.shopify.sample.R;
import com.shopify.sample.util.Util;
import com.shopify.sample.view.base.ListItemViewModel;
import com.shopify.sample.view.base.ListItemViewHolder;

import butterknife.BindView;
import butterknife.OnClick;

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

    @Override public void bindModel(@NonNull final ListItemViewModel<String> listViewItemModel, final int position) {
      super.bindModel(listViewItemModel, position);
      imageView.loadShopifyImage(listViewItemModel.payload());
    }

    @SuppressWarnings("unchecked") @OnClick(R.id.image)
    void onImageClick() {
      onClickListener().onClick(itemModel());
    }
  }
}
