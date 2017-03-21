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

    @Override public void bindModel(@NonNull final ListItemViewModel<List<Collection.Product>> listViewItemModel, final int position) {
      super.bindModel(listViewItemModel, position);
      productListView.setItems(listViewItemModel.payload());
    }
  }
}
