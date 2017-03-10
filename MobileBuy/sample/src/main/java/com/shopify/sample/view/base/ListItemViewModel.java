package com.shopify.sample.view.base;

import android.support.annotation.LayoutRes;

public abstract class ListItemViewModel<T> {
  private final T payload;
  private final int viewType;

  public ListItemViewModel(final T payload, final @LayoutRes int viewType) {
    this.payload = payload;
    this.viewType = viewType;
  }

  public abstract ListViewItemHolder<T, ListItemViewModel<T>> createViewHolder();

  public void bindView(final ListViewItemHolder<T, ListItemViewModel<T>> viewHolder, final int position) {
    viewHolder.bindModel(this);
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
