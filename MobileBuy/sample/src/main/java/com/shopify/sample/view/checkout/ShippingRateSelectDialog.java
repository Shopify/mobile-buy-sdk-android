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

package com.shopify.sample.view.checkout;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.shopify.sample.R;
import com.shopify.sample.domain.model.Checkout.ShippingRate;
import com.shopify.sample.domain.model.Checkout.ShippingRates;
import com.shopify.sample.view.base.ListItemViewHolder;
import com.shopify.sample.view.base.ListItemViewModel;
import com.shopify.sample.view.base.RecyclerViewAdapter;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.shopify.sample.util.Util.checkNotNull;

public final class ShippingRateSelectDialog extends BottomSheetDialog implements RecyclerViewAdapter.OnItemClickListener {
  @BindView(R.id.toolbar) Toolbar toolbarView;
  @BindView(R.id.list) RecyclerView listView;

  private final RecyclerViewAdapter listViewAdapter = new RecyclerViewAdapter(this);
  private OnShippingRateSelectListener onShippingRateSelectListener;

  public ShippingRateSelectDialog(@NonNull final Context context) {
    super(context);
    init();
  }

  public ShippingRateSelectDialog(@NonNull final Context context, @StyleRes final int theme) {
    super(context, theme);
    init();
  }

  public ShippingRateSelectDialog(@NonNull final Context context, final boolean cancelable, final OnCancelListener cancelListener) {
    super(context, cancelable, cancelListener);
    init();
  }

  public void show(@NonNull final ShippingRates shippingRates, @Nullable final OnShippingRateSelectListener onShippingRateSelectListener) {
    checkNotNull(shippingRates, "shippingRates == null");
    this.onShippingRateSelectListener = onShippingRateSelectListener;

    List<ListItemViewModel> models = new ArrayList<>();
    for (ShippingRate shippingRate : shippingRates.shippingRates) {

      models.add(new ShippingRateListItemModel(shippingRate));
    }
    listViewAdapter.swapItemsAndNotify(models, new RecyclerViewAdapter.ItemComparator() {
      @Override public boolean equalsById(final ListItemViewModel oldItem, final ListItemViewModel newItem) {
        return ((ShippingRate) oldItem.payload()).handle.equals(((ShippingRate) newItem.payload()).handle);
      }

      @Override public boolean equalsByContent(final ListItemViewModel oldItem, final ListItemViewModel newItem) {
        return oldItem.payload().equals(newItem);
      }
    });
    show();
  }

  @Override public void onItemClick(@NonNull final ListItemViewModel itemViewModel) {
    if (onShippingRateSelectListener != null) {
      dismiss();
      onShippingRateSelectListener.onShippingRateSelected((ShippingRate) itemViewModel.payload());
    }
  }

  private void init() {
    final View contentView = LayoutInflater.from(getContext()).inflate(R.layout.shipping_rate_list, null);
    setContentView(contentView);
    ButterKnife.bind(this, contentView);

    toolbarView.setTitle(R.string.checkout_shipping_method_select_title);
    toolbarView.setNavigationIcon(R.drawable.ic_close);
    toolbarView.setNavigationOnClickListener(v -> dismiss());
    listView.setAdapter(listViewAdapter);
  }

  public interface OnShippingRateSelectListener {
    void onShippingRateSelected(ShippingRate shippingRate);
  }

  private static final class ShippingRateListItemModel extends ListItemViewModel<ShippingRate> {
    ShippingRateListItemModel(final ShippingRate shippingRate) {
      super(shippingRate, R.layout.shipping_rate_list_item);
    }

    @Override public ListItemViewHolder<ShippingRate, ListItemViewModel<ShippingRate>> createViewHolder(
      final ListItemViewHolder.OnClickListener onClickListener) {
      return new ItemViewHolder(onClickListener);
    }
  }

  static final class ItemViewHolder extends ListItemViewHolder<ShippingRate, ListItemViewModel<ShippingRate>> {
    private static final NumberFormat CURRENCY_FORMAT = NumberFormat.getCurrencyInstance();

    @BindView(R.id.title) TextView titleView;
    @BindView(R.id.price) TextView priceView;

    ItemViewHolder(@NonNull final OnClickListener onClickListener) {
      super(onClickListener);
    }

    @Override public void bindModel(@NonNull final ListItemViewModel<ShippingRate> listViewItemModel, final int position) {
      super.bindModel(listViewItemModel, position);
      titleView.setText(listViewItemModel.payload().title);
      priceView.setText(CURRENCY_FORMAT.format(listViewItemModel.payload().price));
    }

    @OnClick(R.id.root)
    void onRootViewClick() {
      notifyOnClickListener();
    }
  }
}
