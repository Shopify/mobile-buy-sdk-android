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

package com.shopify.sample.view.products;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.shopify.sample.R;
import com.shopify.sample.presenter.products.Product;
import com.shopify.sample.view.base.ListItemViewModel;
import com.shopify.sample.view.base.ListItemViewHolder;
import com.shopify.sample.view.widget.image.ShopifyDraweeView;

import java.text.NumberFormat;

import butterknife.BindView;
import butterknife.OnClick;

final class ProductListItemViewModel extends ListItemViewModel<Product> {

  ProductListItemViewModel(final Product payload) {
    super(payload, R.layout.product_list_item);
  }

  @Override public ListItemViewHolder<Product, ListItemViewModel<Product>> createViewHolder(
    final ListItemViewHolder.OnClickListener onClickListener) {
    return new ItemViewHolder(onClickListener);
  }

  static final class ItemViewHolder extends ListItemViewHolder<Product, ListItemViewModel<Product>> {
    static final NumberFormat CURRENCY_FORMAT = NumberFormat.getCurrencyInstance();
    @BindView(R.id.image) ShopifyDraweeView imageView;
    @BindView(R.id.title) TextView titleView;
    @BindView(R.id.price) TextView priceView;

    ItemViewHolder(@NonNull final OnClickListener onClickListener) {
      super(onClickListener);
    }

    @Override public void bindModel(@NonNull final ListItemViewModel<Product> listViewItemModel) {
      super.bindModel(listViewItemModel);
      imageView.loadShopifyImage(listViewItemModel.payload().imageUrl());
      titleView.setText(listViewItemModel.payload().title());
      priceView.setText(CURRENCY_FORMAT.format(listViewItemModel.payload().minPrice()));
    }

    @SuppressWarnings("unchecked") @OnClick({R.id.image, R.id.title, R.id.price})
    void onClick() {
      onClickListener().onClick(itemModel());
    }
  }
}
