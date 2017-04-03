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

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wallet.MaskedWallet;
import com.google.android.gms.wallet.MaskedWalletRequest;
import com.google.android.gms.wallet.Wallet;
import com.google.android.gms.wallet.WalletConstants;
import com.shopify.buy3.pay.PayCart;
import com.shopify.buy3.pay.PayHelper;
import com.shopify.sample.BuildConfig;
import com.shopify.sample.interactor.cart.CreateCheckout;
import com.shopify.sample.model.cart.Cart;
import com.shopify.sample.model.cart.CartManager;
import com.shopify.sample.mvp.BaseViewPresenter;
import com.shopify.sample.util.WeakObserver;

import java.text.NumberFormat;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;

import static com.shopify.buy3.pay.PayHelper.androidPayIsAvailable;
import static com.shopify.buy3.pay.PayHelper.isAndroidPayEnabledInManifest;
import static com.shopify.sample.util.Util.checkNotNull;

import static com.shopify.sample.util.Util.fold;
import static com.shopify.sample.util.Util.mapItems;

@SuppressWarnings("WeakerAccess")
public final class CartHeaderViewPresenter extends BaseViewPresenter<CartHeaderViewPresenter.View>
  implements GoogleApiClient.ConnectionCallbacks {
  public static final int REQUEST_ID_UPDATE_CART = 1;
  public static final int REQUEST_ID_CREATE_ANDROID_PAY_CHECKOUT = 2;
  public static final int REQUEST_ID_CREATE_WEB_CHECKOUT = 3;
  public static final int REQUEST_ID_PREPARE_ANDROID_PAY = 4;
  private static final NumberFormat CURRENCY_FORMAT = NumberFormat.getCurrencyInstance();

  private final CreateCheckout createCheckout;
  private GoogleApiClient googleApiClient;
  private String currentCheckoutId;
  private PayCart payCart;

  public CartHeaderViewPresenter(@NonNull final CreateCheckout createCheckout) {
    this.createCheckout = checkNotNull(createCheckout, "createCheckout == null");
  }

  @Override public void attachView(final View view) {
    super.attachView(view);
    registerRequest(
      REQUEST_ID_UPDATE_CART,
      CartManager.instance()
        .cartObservable()
        .map(Cart::totalPrice)
        .subscribeOn(AndroidSchedulers.mainThread())
        .subscribeWith(WeakObserver.<CartHeaderViewPresenter, Double>forTarget(this)
          .delegateOnNext(CartHeaderViewPresenter::onTotalPriceUpdated)
          .create())
    );
    connectAndroidPayGoogleApiClient();
  }

  @Override public void detachView() {
    super.detachView();
    if (googleApiClient != null) {
      googleApiClient.disconnect();
      googleApiClient = null;
    }
  }

  @Override
  public void onConnected(final @Nullable Bundle bundle) {
    if (isViewAttached()) {
      androidPayIsAvailable(view().context(), googleApiClient, result -> {
          if (isViewAttached()) {
            view().showAndroidPayCheckout();
          }
        }
      );
    }
  }

  @Override
  public void onConnectionSuspended(final int i) {
    // nothing
  }

  public void createWebCheckout() {
    createCheckout(REQUEST_ID_CREATE_WEB_CHECKOUT);
  }

  public void createAndroidPayCheckout() {
    createCheckout(REQUEST_ID_CREATE_ANDROID_PAY_CHECKOUT);
  }

  public void handleMaskedWalletResponse(final int resultCode, @Nullable final Bundle data) {
    int errorCode = data != null ? data.getInt(WalletConstants.EXTRA_ERROR_CODE, -1) : -1;
    if (errorCode != -1 || data == null) {
      showError(REQUEST_ID_PREPARE_ANDROID_PAY, new RuntimeException("Failed to fetch masked wallet, errorCode: " + errorCode));
      return;
    }

    if (resultCode != Activity.RESULT_OK) {
      return;
    }

    MaskedWallet maskedWallet = data.getParcelable(WalletConstants.EXTRA_MASKED_WALLET);
    if (maskedWallet == null) {
      showError(REQUEST_ID_PREPARE_ANDROID_PAY, new RuntimeException("Failed to extract masked wallet, empty"));
      return;
    }

    if (isViewAttached()) {
      view().showAndroidPayConfirmation(currentCheckoutId, payCart.toBuilder().maskedWallet(maskedWallet).build());
    }
  }

  private void createCheckout(final int requestId) {
    cancelRequest(REQUEST_ID_CREATE_WEB_CHECKOUT);
    cancelRequest(REQUEST_ID_CREATE_ANDROID_PAY_CHECKOUT);

    showProgress(requestId);
    List<CreateCheckout.LineItem> lineItems = mapItems(CartManager.instance().cart().cartItems(),
      cartItem -> new CreateCheckout.LineItem(cartItem.productVariantId, cartItem.quantity));
    registerRequest(
      requestId,
      createCheckout.call(lineItems)
        .toObservable()
        .observeOn(AndroidSchedulers.mainThread())
        .subscribeWith(WeakObserver.<CartHeaderViewPresenter, Checkout>forTarget(this)
          .delegateOnNext((presenter, checkout) -> presenter.onCreateCheckout(requestId, checkout))
          .delegateOnError((presenter, t) -> presenter.onCreateCheckoutError(requestId, t))
          .create())
    );
  }

  private void onTotalPriceUpdated(final double total) {
    if (isViewAttached()) {
      view().renderTotal(CURRENCY_FORMAT.format(total));
    }
  }


  private void onCreateCheckout(final int requestId, final Checkout checkout) {
    if (isViewAttached()) {
      view().hideProgress(requestId);
      this.currentCheckoutId = checkout.id;
      if (requestId == REQUEST_ID_CREATE_WEB_CHECKOUT) {
        view().showWebCheckout(checkout);
      } else if (requestId == REQUEST_ID_CREATE_ANDROID_PAY_CHECKOUT) {
        prepareForAndroidPayCheckout(checkout);
      }
    }
  }

  private void onCreateCheckoutError(final int requestId, final Throwable t) {
    if (isViewAttached()) {
      view().hideProgress(requestId);
      view().showError(requestId, t);
    }
  }

  private void connectAndroidPayGoogleApiClient() {
    if (isAndroidPayEnabledInManifest(view().context())) {
      googleApiClient = new GoogleApiClient.Builder(view().context())
        .addApi(
          Wallet.API,
          new Wallet.WalletOptions.Builder()
            .setEnvironment(BuildConfig.ANDROID_PAY_ENVIRONMENT)
            .setTheme(WalletConstants.THEME_LIGHT)
            .build())
        .addConnectionCallbacks(this)
        .build();
      googleApiClient.connect();
    }
  }

  private void prepareForAndroidPayCheckout(final Checkout checkout) {
    PayCart.Builder payCartBuilder = PayCart.builder()
      .merchantName("SampleApp")
      .currencyCode(checkout.currency)
      .phoneNumberRequired(true)
      .shippingAddressRequired(checkout.requiresShipping);

    fold(payCartBuilder, checkout.lineItems, (accumulator, lineItem) ->
      accumulator.addLineItem(lineItem.title, lineItem.quantity, lineItem.price));

    payCart = payCartBuilder.build();
    MaskedWalletRequest maskedWalletRequest = payCart.maskedWalletRequest(BuildConfig.ANDROID_PAY_PUBLIC_KEY);
    Wallet.Payments.loadMaskedWallet(googleApiClient, maskedWalletRequest, PayHelper.REQUEST_CODE_MASKED_WALLET);
  }

  public interface View extends com.shopify.sample.mvp.View {

    Context context();

    void renderTotal(@NonNull String total);

    void showAndroidPayCheckout();

    void showWebCheckout(@NonNull Checkout checkout);

    void showAndroidPayConfirmation(@NonNull String checkoutId, @NonNull PayCart payCart);
  }
}
