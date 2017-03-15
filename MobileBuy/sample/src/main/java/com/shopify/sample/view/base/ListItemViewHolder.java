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

  void bindView(@NonNull final View view) {
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
