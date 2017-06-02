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

package com.shopify.sample.view.cart;

import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.TextView;

import com.shopify.sample.R;
import com.shopify.sample.domain.model.Cart;
import com.shopify.sample.view.base.ListItemViewHolder;
import com.shopify.sample.view.base.ListItemViewModel;

import java.text.NumberFormat;

import butterknife.BindView;

final class CartSubtotalListItemViewModel extends ListItemViewModel<Cart> {

  CartSubtotalListItemViewModel(final Cart cart) {
    super(cart, R.layout.cart_subtotal_list_item);
  }

  @Override public ListItemViewHolder<Cart, ListItemViewModel<Cart>> createViewHolder(
    @NonNull final ListItemViewHolder.OnClickListener onClickListener) {
    return new CartSubtotalListItemViewModel.ItemViewHolder(onClickListener);
  }

  static final class ItemViewHolder extends ListItemViewHolder<Cart, ListItemViewModel<Cart>> {
    static final NumberFormat CURRENCY_FORMAT = NumberFormat.getCurrencyInstance();
    @BindView(R.id.subtotal) TextView subtotalView;
    private int colorAccent;

    ItemViewHolder(@NonNull final OnClickListener onClickListener) {
      super(onClickListener);
    }

    @Override protected void bindView(@NonNull final View view) {
      super.bindView(view);

      TypedArray typedArray = view.getContext().obtainStyledAttributes(new int[]{R.attr.colorAccent});
      colorAccent = typedArray.getColor(0, 0);
      typedArray.recycle();
    }

    @Override public void bindModel(@NonNull final ListItemViewModel<Cart> listViewItemModel, final int position) {
      super.bindModel(listViewItemModel, position);

      String price = CURRENCY_FORMAT.format(listViewItemModel.payload().totalPrice());
      String text = subtotalView.getResources().getString(R.string.cart_subtotal_price, price);
      SpannableStringBuilder textBuilder = new SpannableStringBuilder(text);
      textBuilder.setSpan(new ForegroundColorSpan(colorAccent), text.length() - price.length(), text.length(),
        Spanned.SPAN_INCLUSIVE_INCLUSIVE);
      subtotalView.setText(textBuilder);
    }
  }
}
