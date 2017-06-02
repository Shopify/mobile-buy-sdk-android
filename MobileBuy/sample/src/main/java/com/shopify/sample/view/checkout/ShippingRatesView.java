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

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LifecycleRegistry;
import android.arch.lifecycle.Transformations;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.widget.TextView;

import com.shopify.sample.R;
import com.shopify.sample.domain.model.Checkout;

import java.text.NumberFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public final class ShippingRatesView extends ConstraintLayout implements LifecycleOwner {
  private static final NumberFormat CURRENCY_FORMAT = NumberFormat.getCurrencyInstance();

  @BindView(R.id.shipping_line) TextView shippingLineView;
  @BindView(R.id.price) TextView priceView;

  private final LifecycleRegistry lifecycleRegistry = new LifecycleRegistry(this);
  private CheckoutShippingRatesViewModel viewModel;

  public ShippingRatesView(final Context context) {
    super(context);
    lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_CREATE);
  }

  public ShippingRatesView(final Context context, final AttributeSet attrs) {
    super(context, attrs);
    lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_CREATE);
  }

  public ShippingRatesView(final Context context, final AttributeSet attrs, final int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_CREATE);
  }

  public void bindViewModel(@NonNull final CheckoutShippingRatesViewModel viewModel) {
    if (this.viewModel != null) {
      throw new IllegalStateException("Already bound");
    }
    this.viewModel = viewModel;

    Transformations.map(viewModel.selectedShippingRateLiveData(), shippingRate ->
      shippingRate != null ? shippingRate.title : null)
      .observe(this, title -> {
        if (title != null) {
          shippingLineView.setText(title);
        } else {
          shippingLineView.setText(R.string.checkout_shipping_method_not_selected);
        }
      });

    Transformations.map(viewModel.selectedShippingRateLiveData(), shippingRate ->
      shippingRate != null ? CURRENCY_FORMAT.format(shippingRate.price)
        : getResources().getString(R.string.checkout_shipping_method_price_not_available))
      .observe(this, price -> priceView.setText(price));
  }

  @Override public Lifecycle getLifecycle() {
    return lifecycleRegistry;
  }

  @OnClick(R.id.change)
  void onChangeClick() {
    Checkout.ShippingRates shippingRates = viewModel.shippingRatesLiveData().getValue();
    if (shippingRates == null || shippingRates.shippingRates.isEmpty()) {
      viewModel.fetchShippingRates();
      return;
    }

    new ShippingRateSelectDialog(getContext()).show(shippingRates, shippingRate ->
      viewModel.selectShippingRate(shippingRate));
  }

  @Override protected void onFinishInflate() {
    super.onFinishInflate();
    ButterKnife.bind(this);
  }

  @Override protected void onAttachedToWindow() {
    super.onAttachedToWindow();
    lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_START);
  }

  @Override protected void onDetachedFromWindow() {
    lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_DESTROY);
    super.onDetachedFromWindow();
  }
}
