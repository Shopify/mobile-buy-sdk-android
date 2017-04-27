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

import android.content.Intent;
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

  private final CheckoutCreateInteractor checkoutCreateInteractor;
  private final CartRepository cartRepository;
  private String currentCheckoutId;
  private PayCart payCart;

  public CartCheckoutViewPresenter(@NonNull final CheckoutCreateInteractor checkoutCreateInteractor, @NonNull final CartRepository cartRepository) {
    this.checkoutCreateInteractor = checkNotNull(checkoutCreateInteractor, "checkoutCreateInteractor == null");
    this.cartRepository = checkNotNull(cartRepository, "cartRepository == null");
  }

  @Override public void attachView(final View view) {
    super.attachView(view);
    registerRequest(
      REQUEST_ID_UPDATE_CART,
      cartRepository.watch()
        .map(Cart::totalPrice)
        .subscribeOn(AndroidSchedulers.mainThread())
        .subscribeWith(WeakObserver.<CartCheckoutViewPresenter, Double>forTarget(this)
          .delegateOnNext(CartCheckoutViewPresenter::onTotalPriceUpdated)
          .create())
    );
  }

  public void createWebCheckout() {
    createCheckout(REQUEST_ID_CREATE_WEB_CHECKOUT, null);
  }

  public void createAndroidPayCheckout(@NonNull final GoogleApiClient googleApiClient) {
    createCheckout(REQUEST_ID_CREATE_ANDROID_PAY_CHECKOUT, checkNotNull(googleApiClient, "googleApiClient == null"));
  }

  public boolean handleMaskedWalletResponse(final int requestCode, final int resultCode, @Nullable final Intent data) {
    return PayHelper.handleWalletResponse(requestCode, resultCode, data, new PayHelper.WalletResponseHandler() {
      @Override public void onWalletError(final int requestCode, final int errorCode) {
        showError(REQUEST_ID_PREPARE_ANDROID_PAY, new RuntimeException("Failed to fetch masked wallet, errorCode: " + errorCode));
      }

      @Override public void onMaskedWallet(final MaskedWallet maskedWallet) {
        if (isViewAttached()) {
          view().showAndroidPayConfirmation(currentCheckoutId, payCart, maskedWallet);
        }
      }
    });
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

  private void onTotalPriceUpdated(final double total) {
    if (isViewAttached()) {
      view().renderTotal(CURRENCY_FORMAT.format(total));
    }
  }

  private void onCreateCheckout(final int requestId, @NonNull final Checkout checkout, final GoogleApiClient googleApiClient) {
    if (isViewAttached()) {
      view().hideProgress(requestId);
      this.currentCheckoutId = checkout.id;
      if (requestId == REQUEST_ID_CREATE_WEB_CHECKOUT) {
        view().showWebCheckout(checkout);
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

  public interface View extends com.shopify.sample.mvp.View {

    void renderTotal(@NonNull String total);

    void showWebCheckout(@NonNull Checkout checkout);

    void showAndroidPayConfirmation(@NonNull String checkoutId, @NonNull PayCart payCart, @NonNull MaskedWallet maskedWallet);
  }
}
