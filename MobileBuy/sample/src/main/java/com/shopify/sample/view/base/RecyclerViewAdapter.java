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
