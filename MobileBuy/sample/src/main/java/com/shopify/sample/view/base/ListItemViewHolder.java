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
import android.view.View;

import butterknife.ButterKnife;

import static com.shopify.sample.util.Util.checkNotNull;

public abstract class ListItemViewHolder<T, MODEL extends ListItemViewModel<T>> {
  private final OnClickListener onClickListener;
  private MODEL itemModel;

  public ListItemViewHolder(@NonNull final OnClickListener onClickListener) {
    checkNotNull(onClickListener, "clickListener == null");
    this.onClickListener = onClickListener;
  }

  protected void bindView(@NonNull final View view) {
    ButterKnife.bind(this, view);
  }

  public void bindModel(@NonNull final MODEL listViewItemModel) {
    itemModel = listViewItemModel;
  }

  @SuppressWarnings("WeakerAccess") public MODEL itemModel() {
    return itemModel;
  }

  @NonNull protected OnClickListener onClickListener() {
    return onClickListener;
  }

  public interface OnClickListener<T, MODEL extends ListItemViewModel<T>> {
    void onClick(@NonNull MODEL itemModel);
  }
}
