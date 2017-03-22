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

package com.shopify.sample.presenter.cart;

import com.shopify.sample.model.cart.Cart;
import com.shopify.sample.model.cart.CartManager;
import com.shopify.sample.mvp.BaseViewPresenter;
import com.shopify.sample.util.WeakObserver;

import java.text.NumberFormat;

import io.reactivex.android.schedulers.AndroidSchedulers;

public final class CartHeaderViewPresenter extends BaseViewPresenter<CartHeaderViewPresenter.View> {
  static final NumberFormat CURRENCY_FORMAT = NumberFormat.getCurrencyInstance();

  @Override public void attachView(final View view) {
    super.attachView(view);
    registerRequest(
      0,
      CartManager.instance().cartObservable()
        .map(Cart::totalPrice)
        .subscribeOn(AndroidSchedulers.mainThread())
        .subscribeWith(WeakObserver.<CartHeaderViewPresenter, Double>forTarget(this)
          .delegateOnNext(CartHeaderViewPresenter::onTotalPriceUpdated)
          .create())
    );
  }

  private void onTotalPriceUpdated(final double total) {
    if (isViewAttached()) {
      view().renderTotal(CURRENCY_FORMAT.format(total));
    }
  }

  public interface View extends com.shopify.sample.mvp.View {
    void renderTotal(String total);
  }
}
