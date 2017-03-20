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

package com.shopify.sample.view.base;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import static com.shopify.sample.util.Util.checkNotNull;

final class RecyclerViewItemHolder extends RecyclerView.ViewHolder {
  private ListItemViewHolder viewItemHolder;
  private final ListItemViewHolder.OnClickListener onListItemClickListener;

  @SuppressWarnings("unchecked") RecyclerViewItemHolder(@NonNull final View itemView, @NonNull final OnClickListener onClickListener) {
    super(itemView);
    checkNotNull(onClickListener, "onClickListener == null");
    onListItemClickListener = onClickListener::onClick;
  }

  @SuppressWarnings("unchecked") void bindModel(@NonNull final ListItemViewModel listItem, final int position) {
    if (viewItemHolder == null) {
      viewItemHolder = listItem.createViewHolder(onListItemClickListener);
      viewItemHolder.bindView(itemView);
    }
    listItem.bindView(viewItemHolder, position);
  }

  public ListItemViewHolder viewItemHolder() {
    return viewItemHolder;
  }

  interface OnClickListener<T, MODEL extends ListItemViewModel<T>> {
    void onClick(@NonNull MODEL itemModel);
  }
}
