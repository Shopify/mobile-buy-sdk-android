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

import android.arch.lifecycle.ViewModel;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.android.gms.wallet.MaskedWallet;
import com.shopify.buy3.pay.PayCart;
import com.shopify.buy3.pay.PayHelper;
import com.shopify.sample.domain.interactor.CheckoutCreateInteractor;
import com.shopify.sample.domain.interactor.RealCheckoutCreateInteractor;
import com.shopify.sample.domain.model.Cart;
import com.shopify.sample.domain.model.Checkout;
import com.shopify.sample.util.LifeCycleBoundCallback;
import com.shopify.sample.util.ProgressLiveData;
import com.shopify.sample.util.RequestRegister;
import com.shopify.sample.util.WeakObserver;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import timber.log.Timber;

import static com.shopify.sample.util.Util.fold;
import static com.shopify.sample.util.Util.mapItems;

@SuppressWarnings({"WeakerAccess", "FieldCanBeLocal"})
public final class CartViewModel extends ViewModel {
  public static final int CHECKOUT_TYPE_WEB = 0;
  public static final int CHECKOUT_TYPE_ANDROID_PAY = 1;

  public static final int REQUEST_ID_CREATE_WEB_CHECKOUT = 1;
  public static final int REQUEST_ID_CREATE_ANDROID_PAY_CHECKOUT = 2;
  public static final int REQUEST_ID_PREPARE_ANDROID_PAY = 3;

  private static final String STATE_KEY_CHECKOUT_ID = "checkout_id";
  private static final String STATE_KEY_PAY_CART = "pay_cart";

  private final CheckoutCreateInteractor checkoutCreateInteractor = new RealCheckoutCreateInteractor();
  private final LifeCycleBoundCallback<Checkout> webCheckoutCallback = new LifeCycleBoundCallback<>();
  private final LifeCycleBoundCallback<PayCart> androidPayStartCheckoutCallback = new LifeCycleBoundCallback<>();
  private final LifeCycleBoundCallback<AndroidPayCheckout> androidPayCheckoutCallback = new LifeCycleBoundCallback<>();
  private final ProgressLiveData progressLiveData = new ProgressLiveData();
  private final LifeCycleBoundCallback<Error> errorCallback = new LifeCycleBoundCallback<>();
  private final RequestRegister<Integer> requestRegister = new RequestRegister<>();
  private String checkoutId;
  private PayCart payCart;

  public void checkout(final int type, final Cart cart) {
    if (type == CHECKOUT_TYPE_WEB) {
      createCheckout(REQUEST_ID_CREATE_WEB_CHECKOUT, cart);
    } else {
      createCheckout(REQUEST_ID_CREATE_ANDROID_PAY_CHECKOUT, cart);
    }
  }

  public LifeCycleBoundCallback<Checkout> webCheckoutCallback() {
    return webCheckoutCallback;
  }

  public LifeCycleBoundCallback<AndroidPayCheckout> androidPayCheckoutCallback() {
    return androidPayCheckoutCallback;
  }

  public ProgressLiveData progressLiveData() {
    return progressLiveData;
  }

  public LifeCycleBoundCallback<Error> errorCallback() {
    return errorCallback;
  }

  public void cancelRequest(final int requestId) {
    requestRegister.delete(requestId);
  }

  public LifeCycleBoundCallback<PayCart> androidPayStartCheckoutCallback() {
    return androidPayStartCheckoutCallback;
  }

  public void handleMaskedWalletResponse(final int requestCode, final int resultCode, @Nullable final Intent data) {
    PayHelper.handleWalletResponse(requestCode, resultCode, data, new PayHelper.WalletResponseHandler() {
      @Override public void onWalletError(final int requestCode, final int errorCode) {
        errorCallback.notify(new Error(REQUEST_ID_PREPARE_ANDROID_PAY, new RuntimeException("Failed to fetch masked wallet, errorCode: " +
          errorCode)));
      }

      @Override public void onMaskedWallet(final MaskedWallet maskedWallet) {
        androidPayCheckoutCallback.notify(new AndroidPayCheckout(checkoutId, payCart, maskedWallet));
      }
    });
  }

  public Bundle saveState() {
    Bundle bundle = new Bundle();
    bundle.putString(STATE_KEY_CHECKOUT_ID, checkoutId);
    bundle.putParcelable(STATE_KEY_PAY_CART, payCart);
    return bundle;
  }

  public void restoreState(final Bundle bundle) {
    if (bundle == null) {
      return;
    }
    checkoutId = bundle.getString(STATE_KEY_CHECKOUT_ID);
    payCart = bundle.getParcelable(STATE_KEY_PAY_CART);
  }

  @Override protected void onCleared() {
    requestRegister.dispose();
  }

  private void createCheckout(final int requestId, final Cart cart) {
    requestRegister.delete(REQUEST_ID_CREATE_WEB_CHECKOUT);
    requestRegister.delete(REQUEST_ID_CREATE_ANDROID_PAY_CHECKOUT);

    progressLiveData.show(requestId);
    List<Checkout.LineItem> lineItems = mapItems(cart.cartItems(),
      cartItem -> new Checkout.LineItem(cartItem.productVariantId, cartItem.variantTitle, cartItem.quantity, cartItem.price));

    requestRegister.add(
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
    progressLiveData.hide(requestId);

    checkoutId = checkout.id;
    if (requestId == REQUEST_ID_CREATE_WEB_CHECKOUT) {
      webCheckoutCallback.notify(checkout);
    } else if (requestId == REQUEST_ID_CREATE_ANDROID_PAY_CHECKOUT) {
      androidPayStartCheckoutCallback.notify(payCart = checkoutPayCart(checkout));
    }
  }

  private void onCreateCheckoutError(final int requestId, final Throwable t) {
    Timber.e(t);
    progressLiveData.hide(requestId);
    errorCallback.notify(new Error(requestId, t));
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

  public static final class AndroidPayCheckout {
    public final String checkoutId;
    public final PayCart payCart;
    public final MaskedWallet maskedWallet;

    AndroidPayCheckout(final String checkoutId, final PayCart payCart, final MaskedWallet maskedWallet) {
      this.checkoutId = checkoutId;
      this.payCart = payCart;
      this.maskedWallet = maskedWallet;
    }
  }

  public static final class Error {
    public final int requestId;
    public final Throwable t;

    Error(final int requestId, final Throwable t) {
      this.requestId = requestId;
      this.t = t;
    }
  }
}
