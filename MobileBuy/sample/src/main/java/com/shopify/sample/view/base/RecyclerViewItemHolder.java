package com.shopify.sample.view.base;

import android.support.v7.widget.RecyclerView;
import android.view.View;

public class RecyclerViewItemHolder extends RecyclerView.ViewHolder {
  private ListViewItemHolder viewItemHolder;

  public RecyclerViewItemHolder(final View itemView) {
    super(itemView);
  }

  @SuppressWarnings("unchecked") public void bindModel(final ListItemViewModel listItem, final int position) {
    if (viewItemHolder == null) {
      viewItemHolder = listItem.createViewHolder();
      viewItemHolder.bindView(itemView);
    }
    listItem.bindView(viewItemHolder, position);
  }

  public ListViewItemHolder viewItemHolder() {
    return viewItemHolder;
  }
}
