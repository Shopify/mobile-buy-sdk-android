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

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.google.android.gms.wallet.MaskedWallet;
import com.shopify.buy3.pay.PayCart;
import com.shopify.sample.R;
import com.shopify.sample.domain.model.Checkout;
import com.shopify.sample.domain.repository.RealCartRepository;
import com.shopify.sample.domain.repository.RealCheckoutRepository;
import com.shopify.sample.presenter.cart.CartCheckoutViewPresenter;
import com.shopify.sample.view.ProgressDialogHelper;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.shopify.sample.util.Util.checkNotNull;

public final class CartCheckoutView extends FrameLayout implements CartCheckoutViewPresenter.View {
  @BindView(R.id.android_pay_checkout) View androidPayCheckoutView;
  @BindView(R.id.subtotal) TextView subtotalView;
  private final CartCheckoutViewPresenter presenter = new CartCheckoutViewPresenter(new RealCheckoutRepository(), new RealCartRepository());
  private ProgressDialogHelper progressDialogHelper;
  private OnConfirmAndroidPayListener onConfirmAndroidPayListener;

  public CartCheckoutView(@NonNull final Context context) {
    super(context);
  }

  public CartCheckoutView(@NonNull final Context context, @Nullable final AttributeSet attrs) {
    super(context, attrs);
  }

  public CartCheckoutView(@NonNull final Context context, @Nullable final AttributeSet attrs, @AttrRes final int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  @Override public void showProgress(final int requestId) {
    if (requestId == CartCheckoutViewPresenter.REQUEST_ID_CREATE_WEB_CHECKOUT) {
      progressDialogHelper.show(requestId, null, getResources().getString(R.string.progress_loading),
        () -> presenter.cancelRequest(CartCheckoutViewPresenter.REQUEST_ID_CREATE_WEB_CHECKOUT));
    }
  }

  @Override public void hideProgress(final int requestId) {
    if (requestId == CartCheckoutViewPresenter.REQUEST_ID_CREATE_WEB_CHECKOUT) {
      progressDialogHelper.dismiss(requestId);
    }
  }

  @Override public void showError(final long requestId, final Throwable t) {
    //TODO show error message
  }

  @Override public void renderTotal(@NonNull final String total) {
    subtotalView.setText(checkNotNull(total, "total == null"));
  }

  @Override public Context context() {
    return getContext();
  }

  @Override public void showAndroidPayCheckout() {
    androidPayCheckoutView.setVisibility(VISIBLE);
  }

  @Override public void showWebCheckout(@NonNull final Checkout checkout) {
    checkNotNull(checkout, "checkout == null");
    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(checkout.webUrl));
    getContext().startActivity(intent);
  }

  @Override public void showAndroidPayConfirmation(@NonNull final String checkoutId, @NonNull final PayCart payCart,
    @NonNull final MaskedWallet maskedWallet) {
    if (onConfirmAndroidPayListener != null) {
      onConfirmAndroidPayListener.onConfirmAndroidPay(checkoutId, payCart, maskedWallet);
    }
  }

  public void handleMaskedWalletResponse(final int resultCode, @Nullable final Bundle data) {
    presenter.handleMaskedWalletResponse(resultCode, data);
  }

  public void setOnConfirmAndroidPayListener(final OnConfirmAndroidPayListener onConfirmAndroidPayListener) {
    this.onConfirmAndroidPayListener = onConfirmAndroidPayListener;
  }

  @Override protected void onFinishInflate() {
    super.onFinishInflate();
    ButterKnife.bind(this);
  }

  @Override protected void onAttachedToWindow() {
    super.onAttachedToWindow();
    progressDialogHelper = new ProgressDialogHelper(getContext());
    presenter.attachView(this);
  }

  @Override protected void onDetachedFromWindow() {
    super.onDetachedFromWindow();
    progressDialogHelper.dismiss();
    presenter.detachView();
  }

  @OnClick(R.id.web_checkout) void onWebCheckoutClick() {
    presenter.createWebCheckout();
  }

  @OnClick(R.id.android_pay_checkout) void onAndroidPayCheckoutClick() {
    presenter.createAndroidPayCheckout();
  }

  public interface OnConfirmAndroidPayListener {
    void onConfirmAndroidPay(@NonNull String checkoutId, @NonNull PayCart payCart, @NonNull MaskedWallet maskedWallet);
  }
}
