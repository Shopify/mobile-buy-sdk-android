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
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.widget.TextView;

import com.shopify.sample.R;
import com.shopify.sample.domain.model.Checkout.ShippingRate;
import com.shopify.sample.domain.model.Checkout.ShippingRates;

import java.text.NumberFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static java.util.Collections.emptyList;

public final class ShippingMethodView extends ConstraintLayout {
  private static final NumberFormat CURRENCY_FORMAT = NumberFormat.getCurrencyInstance();

  @BindView(R.id.shipping_line) TextView shippingLineView;
  @BindView(R.id.price) TextView priceView;

  private ShippingRates shippingRates = new ShippingRates(false, emptyList());
  private OnShippingRateSelectListener onShippingRateSelectListener;

  public ShippingMethodView(final Context context) {
    super(context);
  }

  public ShippingMethodView(final Context context, final AttributeSet attrs) {
    super(context, attrs);
  }

  public ShippingMethodView(final Context context, final AttributeSet attrs, final int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  @Override protected void onFinishInflate() {
    super.onFinishInflate();
    ButterKnife.bind(this);
  }

  public void render(@Nullable final ShippingRate shippingLine, @Nullable final ShippingRates shippingRates) {
    shippingLineView.setText(shippingLine != null ? shippingLine.title
      : getResources().getString(R.string.checkout_shipping_method_not_available));
    priceView.setText(shippingLine != null ? CURRENCY_FORMAT.format(shippingLine.price) : "");
    this.shippingRates = shippingRates != null ? shippingRates : new ShippingRates(false, emptyList());
  }

  public void onShippingRateSelectListener(final OnShippingRateSelectListener onShippingRateSelectListener) {
    this.onShippingRateSelectListener = onShippingRateSelectListener;
  }

  @OnClick(R.id.change)
  void onChangeClick() {
    new ShippingRateSelectDialog(getContext()).show(shippingRates, shippingRate -> {
      if (onShippingRateSelectListener != null) {
        onShippingRateSelectListener.onShippingRateSelected(shippingRate);
      }
    });
  }

  public interface OnShippingRateSelectListener {
    void onShippingRateSelected(ShippingRate shippingRate);
  }
}
