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
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.android.gms.wallet.MaskedWallet;
import com.shopify.buy3.pay.PayCart;
import com.shopify.buy3.pay.PayHelper;
import com.shopify.sample.domain.interactor.CartWatchInteractor;
import com.shopify.sample.domain.interactor.CheckoutCreateInteractor;
import com.shopify.sample.domain.interactor.RealCartWatchInteractor;
import com.shopify.sample.domain.interactor.RealCheckoutCreateInteractor;
import com.shopify.sample.domain.model.Cart;
import com.shopify.sample.domain.model.Checkout;
import com.shopify.sample.view.LifeCycleBoundCallback;
import com.shopify.sample.util.WeakObserver;
import com.shopify.sample.view.BaseViewModel;

import java.math.BigDecimal;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import timber.log.Timber;

import static com.shopify.sample.util.Util.fold;
import static com.shopify.sample.util.Util.mapItems;

@SuppressWarnings({"WeakerAccess", "FieldCanBeLocal"})
public final class CartViewModel extends BaseViewModel implements CartDetailsViewModel, CartHeaderViewModel {
  private static final String STATE_KEY_CHECKOUT_ID = "checkout_id";
  private static final String STATE_KEY_PAY_CART = "pay_cart";

  private final CartWatchInteractor cartWatchInteractor = new RealCartWatchInteractor();
  private final CheckoutCreateInteractor checkoutCreateInteractor = new RealCheckoutCreateInteractor();
  private final LifeCycleBoundCallback<Checkout> webCheckoutCallback = new LifeCycleBoundCallback<>();
  private final LifeCycleBoundCallback<PayCart> androidPayStartCheckoutCallback = new LifeCycleBoundCallback<>();
  private final LifeCycleBoundCallback<AndroidPayCheckout> androidPayCheckoutCallback = new LifeCycleBoundCallback<>();
  private final MutableLiveData<Cart> cartLiveData = new MutableLiveData<>();
  private final MutableLiveData<Boolean> googleApiClientConnectionData = new MutableLiveData<>();

  private String checkoutId;
  private PayCart payCart;

  public CartViewModel() {
    registerRequest(
      REQUEST_ID_UPDATE_CART,
      cartWatchInteractor.execute()
        .observeOn(AndroidSchedulers.mainThread())
        .subscribeWith(WeakObserver.<CartViewModel, Cart>forTarget(this)
          .delegateOnNext(CartViewModel::onCartUpdated)
          .create())
    );
  }

  @Override public void webCheckout() {
    createCheckout(REQUEST_ID_CREATE_WEB_CHECKOUT, cartLiveData.getValue());
  }

  @Override public void androidPayCheckout() {
    createCheckout(REQUEST_ID_CREATE_ANDROID_PAY_CHECKOUT, cartLiveData.getValue());
  }

  @Override public LiveData<Boolean> googleApiClientConnectionData() {
    return googleApiClientConnectionData;
  }

  @Override public void onGoogleApiClientConnectionChanged(final boolean connected) {
    googleApiClientConnectionData.setValue(connected);
  }

  @Override public LiveData<BigDecimal> cartTotalLiveData() {
    return Transformations.map(cartLiveData, cart -> cart != null ? cart.totalPrice() : BigDecimal.ZERO);
  }

  @Override public LifeCycleBoundCallback<Checkout> webCheckoutCallback() {
    return webCheckoutCallback;
  }

  @Override public LifeCycleBoundCallback<AndroidPayCheckout> androidPayCheckoutCallback() {
    return androidPayCheckoutCallback;
  }

  @Override public LifeCycleBoundCallback<PayCart> androidPayStartCheckoutCallback() {
    return androidPayStartCheckoutCallback;
  }

  @Override public void handleMaskedWalletResponse(final int requestCode, final int resultCode, @Nullable final Intent data) {
    PayHelper.handleWalletResponse(requestCode, resultCode, data, new PayHelper.WalletResponseHandler() {
      @Override public void onWalletError(final int requestCode, final int errorCode) {
        notifyUserError(REQUEST_ID_PREPARE_ANDROID_PAY, new RuntimeException("Failed to fetch masked wallet, errorCode: " +
          errorCode), null);
      }

      @Override public void onMaskedWallet(final MaskedWallet maskedWallet) {
        androidPayCheckoutCallback.notify(new AndroidPayCheckout(checkoutId, payCart, maskedWallet));
      }
    });
  }

  @Override public Bundle saveState() {
    Bundle bundle = new Bundle();
    bundle.putString(STATE_KEY_CHECKOUT_ID, checkoutId);
    bundle.putParcelable(STATE_KEY_PAY_CART, payCart);
    return bundle;
  }

  @Override public void restoreState(final Bundle bundle) {
    if (bundle == null) {
      return;
    }
    checkoutId = bundle.getString(STATE_KEY_CHECKOUT_ID);
    payCart = bundle.getParcelable(STATE_KEY_PAY_CART);
  }

  private void onCartUpdated(final Cart cart) {
    cartLiveData.setValue(cart);
  }

  private void createCheckout(final int requestId, final Cart cart) {
    cancelRequest(REQUEST_ID_CREATE_WEB_CHECKOUT);
    cancelRequest(REQUEST_ID_CREATE_ANDROID_PAY_CHECKOUT);

    if (cart == null) return;

    showProgress(requestId);
    List<Checkout.LineItem> lineItems = mapItems(cart.cartItems(),
      cartItem -> new Checkout.LineItem(cartItem.productVariantId, cartItem.variantTitle, cartItem.quantity, cartItem.price));

    registerRequest(
      requestId,
      checkoutCreateInteractor.execute(lineItems)
        .toObservable()
        .observeOn(AndroidSchedulers.mainThread())
        .subscribeWith(WeakObserver.<CartViewModel, Checkout>forTarget(this)
          .delegateOnNext((presenter, checkout) -> presenter.onCreateCheckout(requestId, checkout))
          .delegateOnError((presenter, t) -> presenter.onCreateCheckoutError(requestId, t))
          .create())
    );
  }

  private void onCreateCheckout(final int requestId, @NonNull final Checkout checkout) {
    hideProgress(requestId);

    checkoutId = checkout.id;
    if (requestId == REQUEST_ID_CREATE_WEB_CHECKOUT) {
      webCheckoutCallback.notify(checkout);
    } else if (requestId == REQUEST_ID_CREATE_ANDROID_PAY_CHECKOUT) {
      androidPayStartCheckoutCallback.notify(payCart = checkoutPayCart(checkout));
    }
  }

  private void onCreateCheckoutError(final int requestId, final Throwable t) {
    Timber.e(t);

    hideProgress(requestId);
    notifyUserError(requestId, t, null);
  }

  private PayCart checkoutPayCart(final Checkout checkout) {
    PayCart.Builder payCartBuilder = PayCart.builder()
      .merchantName("SampleApp")
      .currencyCode(checkout.currency)
      .phoneNumberRequired(true)
      .shippingAddressRequired(checkout.requiresShipping);

    fold(payCartBuilder, checkout.lineItems, (accumulator, lineItem) ->
      accumulator.addLineItem(lineItem.title, lineItem.quantity, lineItem.price));

    return payCartBuilder.build();
  }
}
