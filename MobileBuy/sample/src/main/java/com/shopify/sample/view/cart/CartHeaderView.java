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

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LifecycleRegistry;
import android.arch.lifecycle.Transformations;
import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.shopify.sample.R;

import java.text.NumberFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public final class CartHeaderView extends FrameLayout implements LifecycleOwner {
  private static final NumberFormat CURRENCY_FORMAT = NumberFormat.getCurrencyInstance();

  @BindView(R.id.android_pay_checkout) View androidPayCheckoutView;
  @BindView(R.id.subtotal) TextView subtotalView;

  private final LifecycleRegistry lifecycleRegistry = new LifecycleRegistry(this);

  private CartHeaderViewModel viewModel;

  public CartHeaderView(@NonNull final Context context) {
    super(context);
    lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_CREATE);
  }

  public CartHeaderView(@NonNull final Context context, @Nullable final AttributeSet attrs) {
    super(context, attrs);
    lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_CREATE);
  }

  public CartHeaderView(@NonNull final Context context, @Nullable final AttributeSet attrs, @AttrRes final int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_CREATE);
  }

  public void bindViewModel(final CartHeaderViewModel viewModel) {
    if (this.viewModel != null) {
      throw new IllegalStateException("Already bound");
    }
    this.viewModel = viewModel;

    Transformations.map(viewModel.cartTotalLiveData(), CURRENCY_FORMAT::format)
      .observe(this, (total) -> subtotalView.setText(total));

//    viewModel.googleApiClientConnectionData().observe(this, connected ->
//      androidPayCheckoutView.setVisibility(connected == Boolean.TRUE ? VISIBLE : GONE));
    viewModel.isReadyToPayRequest().observe(this, isReadyToPayRequest ->
      androidPayCheckoutView.setVisibility(isReadyToPayRequest == Boolean.TRUE ? VISIBLE : GONE));
  }

  @Override public LifecycleRegistry getLifecycle() {
    return lifecycleRegistry;
  }

  @Override protected void onFinishInflate() {
    super.onFinishInflate();
    ButterKnife.bind(this);
    subtotalView.setText(CURRENCY_FORMAT.format(0));
  }

  @Override protected void onAttachedToWindow() {
    super.onAttachedToWindow();
    lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_START);
  }

  @Override protected void onDetachedFromWindow() {
    lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_DESTROY);
    super.onDetachedFromWindow();
  }

  @OnClick(R.id.web_checkout) void onWebCheckoutClick() {
    viewModel.webCheckout();
  }

  @OnClick(R.id.android_pay_checkout) void onAndroidPayCheckoutClick() {
    viewModel.androidPayCheckout();
  }
}
