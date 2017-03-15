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
