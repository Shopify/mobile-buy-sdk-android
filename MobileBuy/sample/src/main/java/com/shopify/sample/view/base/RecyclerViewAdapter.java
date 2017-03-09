package com.shopify.sample.view.base;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shopify.sample.util.Util;

import java.util.ArrayList;
import java.util.List;

public final class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewItemHolder> {
  private final List<ListItemViewModel> items = new ArrayList<>();

  @Override public RecyclerViewItemHolder onCreateViewHolder(final ViewGroup parentView, final int layoutId) {
    final LayoutInflater layoutInflater = LayoutInflater.from(parentView.getContext());
    final View view = layoutInflater.inflate(layoutId, parentView, false);
    return new RecyclerViewItemHolder(view);
  }

  @Override public void onBindViewHolder(final RecyclerViewItemHolder viewHolder, final int position) {
    final ListItemViewModel item = itemAt(position);
    viewHolder.bindModel(item, position);
  }

  @Override public int getItemCount() {
    return items.size();
  }

  @Override public int getItemViewType(final int position) {
    return itemAt(position).getViewType();
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
    Util.checkNotNull(newItems, "newItems == null");
    int prevSize = items.size();
    items.addAll(newItems);
    notifyItemRangeInserted(prevSize, newItems.size());
  }

  public void clearItems() {
    int prevSize = items.size();
    items.clear();
    notifyItemRangeRemoved(0, prevSize);
  }
}
