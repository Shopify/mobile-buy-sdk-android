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
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.widget.TextView;

import com.shopify.sample.R;
import com.shopify.sample.domain.interactor.RealCheckoutShippingRatesInteractor;
import com.shopify.sample.domain.model.Checkout.ShippingRate;
import com.shopify.sample.presenter.checkout.CheckoutShippingRatesViewPresenter;

import java.text.NumberFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.shopify.sample.presenter.checkout.CheckoutShippingRatesViewPresenter.REQUEST_ID_FETCH_SHIPPING_RATES;

public final class ShippingRatesView extends ConstraintLayout implements CheckoutShippingRatesViewPresenter.View {
  private static final NumberFormat CURRENCY_FORMAT = NumberFormat.getCurrencyInstance();

  @BindView(R.id.shipping_line) TextView shippingLineView;
  @BindView(R.id.price) TextView priceView;

  private CheckoutShippingRatesViewPresenter presenter;
  private OnProgressListener onProgressListener;
  private OnShippingRateSelectListener onShippingRateSelectListener;

  public ShippingRatesView(final Context context) {
    super(context);
  }

  public ShippingRatesView(final Context context, final AttributeSet attrs) {
    super(context, attrs);
  }

  public ShippingRatesView(final Context context, final AttributeSet attrs, final int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  public void init(@NonNull final String checkoutId) {
    if (presenter != null) throw new IllegalStateException("Already initialized");
    presenter = new CheckoutShippingRatesViewPresenter(checkoutId, new RealCheckoutShippingRatesInteractor());
  }

  public void invalidateShippingRates() {
    if (presenter != null) {
      presenter.invalidateShippingRates();
    }
  }

  @Override public void showProgress(final int requestId) {
    if (onProgressListener == null) {
      return;
    }

    final String message;
    if (requestId == REQUEST_ID_FETCH_SHIPPING_RATES) {
      message = getResources().getString(R.string.checkout_fetch_shipping_rates_progress);
    } else {
      message = getResources().getString(R.string.progress_loading);
    }
    onProgressListener.onShowProgress(message);
  }

  @Override public void hideProgress(final int requestId) {
    if (onProgressListener == null) {
      return;
    }
    onProgressListener.onHideProgress();
  }

  @Override public void showError(final int requestId, final Throwable t) {
    if (onShippingRateSelectListener != null) {
      onShippingRateSelectListener.onError(t);
    }
  }

  @Override public void onShippingRateSelected(@Nullable final ShippingRate shippingRate) {
    shippingLineView.setText(shippingRate != null ? shippingRate.title
      : getResources().getString(R.string.checkout_shipping_method_not_available));
    priceView.setText(shippingRate != null ? CURRENCY_FORMAT.format(shippingRate.price) : "");

    if (onShippingRateSelectListener != null) {
      onShippingRateSelectListener.onShippingRateSelected(shippingRate);
    }
  }

  public void setOnProgressListener(@Nullable final OnProgressListener onProgressListener) {
    this.onProgressListener = onProgressListener;
  }

  public void setOnShippingRateSelectListener(@Nullable final OnShippingRateSelectListener onShippingRateSelectListener) {
    this.onShippingRateSelectListener = onShippingRateSelectListener;
  }

  @Nullable public ShippingRate selectedShippingRate() {
    return presenter != null ? presenter.selectedShippingRate() : null;
  }

  @OnClick(R.id.change)
  void onChangeClick() {
    if (presenter == null) {
      return;
    }

    new ShippingRateSelectDialog(getContext()).show(presenter.shippingRates(), presenter::setSelectedShippingRate);
  }

  @Override protected void onFinishInflate() {
    super.onFinishInflate();
    ButterKnife.bind(this);
  }

  @Override protected void onAttachedToWindow() {
    super.onAttachedToWindow();
    if (presenter != null) {
      presenter.attachView(this);
    }
  }

  @Override protected void onDetachedFromWindow() {
    super.onDetachedFromWindow();
    if (presenter != null) {
      presenter.detachView();
    }
  }

  public interface OnProgressListener {
    void onShowProgress(@NonNull String message);

    void onHideProgress();
  }

  public interface OnShippingRateSelectListener {
    void onShippingRateSelected(@Nullable ShippingRate shippingRate);

    void onError(@NonNull Throwable t);
  }
}
