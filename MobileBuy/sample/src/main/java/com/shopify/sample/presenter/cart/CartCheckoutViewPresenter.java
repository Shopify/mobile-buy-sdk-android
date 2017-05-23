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

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.OnLifecycleEvent;
import android.arch.lifecycle.Transformations;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wallet.MaskedWallet;
import com.shopify.buy3.pay.PayCart;
import com.shopify.buy3.pay.PayHelper;
import com.shopify.sample.BuildConfig;
import com.shopify.sample.domain.interactor.CheckoutCreateInteractor;
import com.shopify.sample.domain.model.Cart;
import com.shopify.sample.domain.model.Checkout;
import com.shopify.sample.domain.repository.CartRepository;
import com.shopify.sample.mvp.BaseViewPresenter;
import com.shopify.sample.util.WeakObserver;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;

import static com.shopify.sample.util.Util.checkNotNull;
import static com.shopify.sample.util.Util.fold;
import static com.shopify.sample.util.Util.mapItems;

@SuppressWarnings("WeakerAccess")
public final class CartCheckoutViewPresenter extends BaseViewPresenter<CartCheckoutViewPresenter.View> {
  public static final int REQUEST_ID_UPDATE_CART = 1;
  public static final int REQUEST_ID_CREATE_ANDROID_PAY_CHECKOUT = 2;
  public static final int REQUEST_ID_CREATE_WEB_CHECKOUT = 3;
  public static final int REQUEST_ID_PREPARE_ANDROID_PAY = 4;
  private static final NumberFormat CURRENCY_FORMAT = NumberFormat.getCurrencyInstance();
  private static final String STATE_KEY_CHECKOUT_ID = "checkout_id";
  private static final String STATE_KEY_PAY_CART = "pay_cart";

  private final CheckoutCreateInteractor checkoutCreateInteractor;
  private final CartRepository cartRepository;
  private String checkoutId;
  private PayCart payCart;
  private final MutableLiveData<BigDecimal> totalData = new MutableLiveData<>();
  private final MutableLiveData<Checkout> webCheckoutData = new MutableLiveData<>();

  public CartCheckoutViewPresenter(@NonNull final CheckoutCreateInteractor checkoutCreateInteractor,
    @NonNull final CartRepository cartRepository, @NonNull final View view) {
    this.checkoutCreateInteractor = checkNotNull(checkoutCreateInteractor, "checkoutCreateInteractor == null");
    this.cartRepository = checkNotNull(cartRepository, "cartRepository == null");
    attachView(view);

    view.getLifecycle().addObserver(new LifecycleObserver() {
      @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
      public void onCreate() {
        CartCheckoutViewPresenter.this.onCreate();
      }

      @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
      public void onDestroy() {
        CartCheckoutViewPresenter.this.onDestroy();
      }
    });
  }

  public void createWebCheckout() {
    if (isViewAttached() && view().getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.CREATED)) {
      createCheckout(REQUEST_ID_CREATE_WEB_CHECKOUT, null);
    }
  }

  public void createAndroidPayCheckout(@NonNull final GoogleApiClient googleApiClient) {
    if (isViewAttached() && view().getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.CREATED)) {
      createCheckout(REQUEST_ID_CREATE_ANDROID_PAY_CHECKOUT, checkNotNull(googleApiClient, "googleApiClient == null"));
    }
  }

  public boolean handleMaskedWalletResponse(final int requestCode, final int resultCode, @Nullable final Intent data) {
    if (isViewAttached() && view().getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.CREATED)) {
      return PayHelper.handleWalletResponse(requestCode, resultCode, data, new PayHelper.WalletResponseHandler() {
        @Override public void onWalletError(final int requestCode, final int errorCode) {
          showError(REQUEST_ID_PREPARE_ANDROID_PAY, new RuntimeException("Failed to fetch masked wallet, errorCode: " + errorCode));
        }

        @Override public void onMaskedWallet(final MaskedWallet maskedWallet) {
          if (isViewAttached()) {
            view().showAndroidPayConfirmation(checkoutId, payCart, maskedWallet);
          }
        }
      });
    }
    return false;
  }

  @NonNull public Bundle saveState() {
    Bundle bundle = new Bundle();
    bundle.putString(STATE_KEY_CHECKOUT_ID, checkoutId);
    bundle.putParcelable(STATE_KEY_PAY_CART, payCart);
    return bundle;
  }

  public void restoreState(@NonNull final Bundle bundle) {
    checkNotNull(bundle, "bundle == null");
    checkoutId = bundle.getString(STATE_KEY_CHECKOUT_ID);
    payCart = bundle.getParcelable(STATE_KEY_PAY_CART);
  }

  public LiveData<String> totalData() {
    return Transformations.map(totalData, CURRENCY_FORMAT::format);
  }

  public LiveData<Checkout> webCheckoutData() {
    return webCheckoutData;
  }

  private void onCreate() {
    registerRequest(
      REQUEST_ID_UPDATE_CART,
      cartRepository.watch()
        .map(Cart::totalPrice)
        .subscribeOn(AndroidSchedulers.mainThread())
        .subscribeWith(WeakObserver.<CartCheckoutViewPresenter, BigDecimal>forTarget(this)
          .delegateOnNext(CartCheckoutViewPresenter::onTotalUpdated)
          .create())
    );
  }

  private void onDestroy() {
//    webCheckoutData.setValue(null);
  }

  private void createCheckout(final int requestId, final GoogleApiClient googleApiClient) {
    cancelRequest(REQUEST_ID_CREATE_WEB_CHECKOUT);
    cancelRequest(REQUEST_ID_CREATE_ANDROID_PAY_CHECKOUT);

    showProgress(requestId);
    List<Checkout.LineItem> lineItems = mapItems(cartRepository.cart().cartItems(),
      cartItem -> new Checkout.LineItem(cartItem.productVariantId, cartItem.variantTitle, cartItem.quantity, cartItem.price));
    registerRequest(
      requestId,
      checkoutCreateInteractor.execute(lineItems)
        .toObservable()
        .observeOn(AndroidSchedulers.mainThread())
        .subscribeWith(WeakObserver.<CartCheckoutViewPresenter, Checkout>forTarget(this)
          .delegateOnNext((presenter, checkout) -> presenter.onCreateCheckout(requestId, checkout, googleApiClient))
          .delegateOnError((presenter, t) -> presenter.onCreateCheckoutError(requestId, t))
          .create())
    );
  }

  private void onTotalUpdated(final BigDecimal total) {
    if (isViewAttached() && view().getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.CREATED)) {
      totalData.postValue(total);
    }
  }

  private void onCreateCheckout(final int requestId, @NonNull final Checkout checkout, final GoogleApiClient googleApiClient) {
    if (isViewAttached()) {
      view().hideProgress(requestId);
      this.checkoutId = checkout.id;
      if (requestId == REQUEST_ID_CREATE_WEB_CHECKOUT) {
        webCheckoutData.postValue(checkout);
//        view().showWebCheckoutConfirmation(checkout);
      } else if (requestId == REQUEST_ID_CREATE_ANDROID_PAY_CHECKOUT) {
        prepareForAndroidPayCheckout(checkout, googleApiClient);
      }
    }
  }

  private void onCreateCheckoutError(final int requestId, final Throwable t) {
    if (isViewAttached()) {
      view().hideProgress(requestId);
      view().showError(requestId, t);
    }
  }

  private void prepareForAndroidPayCheckout(final Checkout checkout, final GoogleApiClient googleApiClient) {
    PayCart.Builder payCartBuilder = PayCart.builder()
      .merchantName("SampleApp")
      .currencyCode(checkout.currency)
      .phoneNumberRequired(true)
      .shippingAddressRequired(checkout.requiresShipping);

    fold(payCartBuilder, checkout.lineItems, (accumulator, lineItem) ->
      accumulator.addLineItem(lineItem.title, lineItem.quantity, lineItem.price));

    payCart = payCartBuilder.build();
    PayHelper.requestMaskedWallet(googleApiClient, payCart, BuildConfig.ANDROID_PAY_PUBLIC_KEY);
  }

  public interface View extends com.shopify.sample.mvp.View, LifecycleOwner {

//    void showWebCheckoutConfirmation(@NonNull Checkout checkout);

    void showAndroidPayConfirmation(@NonNull String checkoutId, @NonNull PayCart payCart, @NonNull MaskedWallet maskedWallet);
  }
}
