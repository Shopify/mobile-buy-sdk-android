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

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;

import com.shopify.sample.domain.interactor.CartWatchInteractor;
import com.shopify.sample.domain.interactor.RealCartWatchInteractor;
import com.shopify.sample.domain.model.Cart;
import com.shopify.sample.util.LifeCycleBoundCallback;
import com.shopify.sample.util.RequestRegister;
import com.shopify.sample.util.WeakObserver;

import java.math.BigDecimal;

import io.reactivex.android.schedulers.AndroidSchedulers;

@SuppressWarnings({"FieldCanBeLocal", "WeakerAccess"})
public final class CartHeaderViewModel extends ViewModel {
  public static final int REQUEST_ID_UPDATE_CART = 1;

  private final CartWatchInteractor cartWatchInteractor = new RealCartWatchInteractor();
  private final MutableLiveData<Cart> cartLiveData = new MutableLiveData<>();
  private final MutableLiveData<Boolean> googleApiClientConnectionData = new MutableLiveData<>();
  private final RequestRegister<Integer> requestRegister = new RequestRegister<>();
  private final LifeCycleBoundCallback<CheckoutRequest> checkoutCallback = new LifeCycleBoundCallback<>();

  public CartHeaderViewModel() {
    requestRegister.add(
      REQUEST_ID_UPDATE_CART,
      cartWatchInteractor.execute()
        .subscribeOn(AndroidSchedulers.mainThread())
        .subscribeWith(WeakObserver.<CartHeaderViewModel, Cart>forTarget(this)
          .delegateOnNext(CartHeaderViewModel::onCartUpdated)
          .create())
    );
  }

  public LiveData<Boolean> googleApiClientConnectionData() {
    return googleApiClientConnectionData;
  }

  public void onGoogleApiClientConnectionChanged(final boolean connected) {
    googleApiClientConnectionData.setValue(connected);
  }

  public LiveData<BigDecimal> cartTotalLiveData() {
    return Transformations.map(cartLiveData, cart -> cart != null ? cart.totalPrice() : BigDecimal.ZERO);
  }

  public void webCheckout() {
    checkoutCallback.notify(CheckoutRequest.web(cartLiveData.getValue()));
  }

  public void androidPayCheckout() {
    checkoutCallback.notify(CheckoutRequest.androidPay(cartLiveData.getValue()));
  }

  public LifeCycleBoundCallback<CheckoutRequest> checkoutCallback() {
    return checkoutCallback;
  }

  private void onCartUpdated(final Cart cart) {
    cartLiveData.setValue(cart);
  }

  public static final class CheckoutRequest {
    public static final int CHECKOUT_TYPE_WEB = 0;
    public static final int CHECKOUT_TYPE_ANDROID_PAY = 1;

    public final int checkoutType;
    public final Cart cart;

    static CheckoutRequest web(final Cart cart) {
      return new CheckoutRequest(CHECKOUT_TYPE_WEB, cart);
    }

    static CheckoutRequest androidPay(final Cart cart) {
      return new CheckoutRequest(CHECKOUT_TYPE_ANDROID_PAY, cart);
    }

    private CheckoutRequest(final int checkoutType, final Cart cart) {
      this.checkoutType = checkoutType;
      this.cart = cart;
    }
  }
}
