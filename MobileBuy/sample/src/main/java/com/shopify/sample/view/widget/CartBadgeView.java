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

package com.shopify.sample.view.widget;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.shopify.sample.R;
import com.shopify.sample.domain.model.Cart;
import com.shopify.sample.domain.repository.CartRepository;
import com.shopify.sample.domain.repository.RealCartRepository;
import com.shopify.sample.util.WeakObserver;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

public final class CartBadgeView extends FrameLayout {
  @BindView(R.id.count) TextView countView;

  private CartRepository cartRepository = new RealCartRepository();
  private Disposable cartSubscription;

  public CartBadgeView(@NonNull final Context context) {
    super(context);
  }

  public CartBadgeView(@NonNull final Context context, @Nullable final AttributeSet attrs) {
    super(context, attrs);
  }

  public CartBadgeView(@NonNull final Context context, @Nullable final AttributeSet attrs, @AttrRes final int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  @Override protected void onFinishInflate() {
    super.onFinishInflate();
    ButterKnife.bind(this);
  }

  @Override protected void onAttachedToWindow() {
    super.onAttachedToWindow();
    cartSubscription = cartRepository.watch()
      .subscribeOn(AndroidSchedulers.mainThread())
      .subscribeWith(WeakObserver.<CartBadgeView, Cart>forTarget(this)
        .delegateOnNext(CartBadgeView::onCartUpdate)
        .create());
  }

  @Override protected void onDetachedFromWindow() {
    super.onDetachedFromWindow();
    if (cartSubscription != null) {
      cartSubscription.dispose();
      cartSubscription = null;
    }
  }

  private void onCartUpdate(final Cart cart) {
    final int totalQuantity = cart.totalQuantity();
    countView.setVisibility(totalQuantity == 0 ? View.INVISIBLE : VISIBLE);
    countView.setText(String.valueOf(totalQuantity));
  }
}
