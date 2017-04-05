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

import android.support.annotation.LayoutRes;

public abstract class ListItemViewModel<T> {
  private final T payload;
  private final int viewType;
  private int position;

  public ListItemViewModel(final T payload, @LayoutRes final int viewType) {
    this.payload = payload;
    this.viewType = viewType;
  }

  public abstract ListItemViewHolder<T, ListItemViewModel<T>> createViewHolder(final ListItemViewHolder.OnClickListener onClickListener);

  public void bindView(final ListItemViewHolder<T, ListItemViewModel<T>> viewHolder, final int position) {
    this.position = position;
    viewHolder.bindModel(this, position);
  }

  public long itemId() {
    if (payload == null) {
      return 0;
    } else {
      return payload().hashCode();
    }
  }

  public T payload() {
    return payload;
  }

  @LayoutRes
  public int viewType() {
    return viewType;
  }

  public int position() {
    return position;
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) return true;
    if (!(o instanceof ListItemViewModel)) return false;

    final ListItemViewModel that = (ListItemViewModel) o;

    if (viewType != that.viewType) return false;
    return payload != null ? payload.equals(that.payload) : that.payload == null;
  }

  @Override
  public int hashCode() {
    int result = payload != null ? payload.hashCode() : 0;
    result = 31 * result + viewType;
    return result;
  }
}
