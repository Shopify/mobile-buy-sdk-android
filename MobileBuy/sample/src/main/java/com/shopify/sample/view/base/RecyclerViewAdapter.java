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
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import static com.shopify.sample.util.Util.checkNotNull;

public final class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewItemHolder> {
  private final List<ListItemViewModel> items = new ArrayList<>();
  private final RecyclerViewItemHolder.OnClickListener itemClickListener;

  public RecyclerViewAdapter() {
    setHasStableIds(true);
    this.itemClickListener = itemModel -> {
    };
  }

  public RecyclerViewAdapter(@NonNull final OnItemClickListener itemClickListener) {
    checkNotNull(itemClickListener, "itemClickListener == null");
    this.itemClickListener = itemClickListener::onItemClick;
    setHasStableIds(true);
  }

  @Override public RecyclerViewItemHolder onCreateViewHolder(final ViewGroup parentView, final int layoutId) {
    final LayoutInflater layoutInflater = LayoutInflater.from(parentView.getContext());
    final View view = layoutInflater.inflate(layoutId, parentView, false);
    return new RecyclerViewItemHolder(view, itemClickListener);
  }

  @Override public void onBindViewHolder(final RecyclerViewItemHolder viewHolder, final int position) {
    viewHolder.bindModel(items.get(position), position);
  }

  @Override public int getItemCount() {
    return items.size();
  }

  @Override public int getItemViewType(final int position) {
    return items.get(position).viewType();
  }

  @Override public long getItemId(final int position) {
    long hash = 1;
    hash *= 1000003;
    hash ^= getItemViewType(position);
    hash *= 1000003;
    hash ^= items.get(position).hashCode();
    return hash;
  }

  @SuppressWarnings("unchecked") @Nullable public <T> ListItemViewModel<T> itemAt(final int position) {
    if (position < 0 || position >= items.size()) {
      return null;
    }
    return items.get(position);
  }

  @SuppressWarnings("unchecked") @Nullable public <T> ListItemViewModel<T> lastItem() {
    if (items.size() > 0) {
      return items.get(items.size() - 1);
    }
    return null;
  }

  public void addItems(@NonNull final List<ListItemViewModel> newItems) {
    checkNotNull(newItems, "newItems == null");
    int prevSize = items.size();
    items.addAll(newItems);
    notifyItemRangeInserted(prevSize, newItems.size());
  }

  public void clearItems() {
    int prevSize = items.size();
    items.clear();
    notifyItemRangeRemoved(0, prevSize);
  }

  public int itemPosition(ListItemViewModel item) {
    return items.indexOf(item);
  }

  public interface OnItemClickListener {
    void onItemClick(@NonNull ListItemViewModel itemViewModel);
  }
}
