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

package com.shopify.sample.presenter.checkout;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wallet.FullWallet;
import com.google.android.gms.wallet.FullWalletRequest;
import com.google.android.gms.wallet.MaskedWallet;
import com.google.android.gms.wallet.MaskedWalletRequest;
import com.google.android.gms.wallet.Wallet;
import com.google.android.gms.wallet.WalletConstants;
import com.shopify.buy3.pay.PayAddress;
import com.shopify.buy3.pay.PayCart;
import com.shopify.buy3.pay.PayHelper;
import com.shopify.buy3.pay.PaymentToken;
import com.shopify.sample.BuildConfig;
import com.shopify.sample.domain.model.Checkout;
import com.shopify.sample.domain.model.Payment;
import com.shopify.sample.domain.repository.CheckoutRepository;
import com.shopify.sample.mvp.BaseViewPresenter;
import com.shopify.sample.util.WeakSingleObserver;

import java.math.BigDecimal;

import io.reactivex.android.schedulers.AndroidSchedulers;

import static com.shopify.buy3.pay.PayHelper.isAndroidPayEnabledInManifest;
import static com.shopify.sample.util.Util.checkNotBlank;
import static com.shopify.sample.util.Util.checkNotNull;
import static com.shopify.sample.util.Util.fold;
import static java.util.Collections.emptyList;

public final class CheckoutViewPresenter extends BaseViewPresenter<CheckoutViewPresenter.View> {
  public static final int REQUEST_ID_UPDATE_CHECKOUT_SHIPPING_ADDRESS = 1;
  public static final int REQUEST_ID_FETCH_SHIPPING_RATES = 2;
  public static final int REQUEST_ID_APPLY_SHIPPING_RATE = 3;
  public static final int REQUEST_ID_COMPLETE_CHECKOUT = 4;

  private final CheckoutRepository checkoutRepository;
  private final String checkoutId;
  private PayCart payCart;
  private MaskedWallet maskedWallet;
  private Checkout.ShippingRates shippingRates;
  private GoogleApiClient googleApiClient;

  public CheckoutViewPresenter(@NonNull final String checkoutId, @NonNull final PayCart payCart, @NonNull final MaskedWallet maskedWallet,
    @NonNull final CheckoutRepository checkoutRepository) {
    this.checkoutId = checkNotBlank(checkoutId, "checkoutId can't be empty");
    this.payCart = checkNotNull(payCart, "payCart == null");
    this.maskedWallet = checkNotNull(maskedWallet, "maskedWallet == null");
    this.checkoutRepository = checkNotNull(checkoutRepository, "checkoutRepository == null");
  }

  public void attachView(final View view) {
    super.attachView(view);
    googleApiClient = googleApiClient();
    googleApiClient.connect();
    view.renderTotalSummary(payCart.subtotal, payCart.shippingPrice != null ? payCart.shippingPrice : BigDecimal.ZERO,
      payCart.taxPrice != null ? payCart.taxPrice : BigDecimal.ZERO, payCart.totalPrice);
    updateMaskedWallet(maskedWallet);
  }

  @Override public void detachView() {
    super.detachView();
    if (googleApiClient != null) {
      googleApiClient.disconnect();
      googleApiClient = null;
    }
  }

  public void requestMaskedWalletUpdate() {
    MaskedWalletRequest maskedWalletRequest = payCart.maskedWalletRequest(BuildConfig.ANDROID_PAY_PUBLIC_KEY);
    Wallet.Payments.loadMaskedWallet(googleApiClient, maskedWalletRequest, PayHelper.REQUEST_CODE_MASKED_WALLET);
  }

  public void updateMaskedWallet(@NonNull final MaskedWallet maskedWallet) {
    if (isViewDetached()) {
      return;
    }
    this.maskedWallet = checkNotNull(maskedWallet, "maskedWallet == null");
    invalidateShippingRates(new Checkout.ShippingRates(false, emptyList()));
    view().updateMaskedWallet(maskedWallet);

    PayAddress payAddress = PayAddress.fromUserAddress(maskedWallet.getBuyerShippingAddress());
    showProgress(REQUEST_ID_UPDATE_CHECKOUT_SHIPPING_ADDRESS);
    registerRequest(
      REQUEST_ID_UPDATE_CHECKOUT_SHIPPING_ADDRESS,
      checkoutRepository.updateShippingAddress(checkNotBlank(checkoutId, "checkoutId can't be empty"), payAddress)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribeWith(WeakSingleObserver.<CheckoutViewPresenter, Checkout>forTarget(this)
          .delegateOnSuccess(CheckoutViewPresenter::onUpdateCheckoutShippingAddress)
          .delegateOnError((presenter, t) -> presenter.onRequestError(REQUEST_ID_UPDATE_CHECKOUT_SHIPPING_ADDRESS, t))
          .create())
    );
  }

  public void applyShippingRate(@NonNull final Checkout.ShippingRate shippingRate) {
    checkNotNull(shippingRate, "shippingRate == null");

    if (isViewDetached()) {
      return;
    }

    view().showProgress(REQUEST_ID_APPLY_SHIPPING_RATE);
    registerRequest(
      REQUEST_ID_APPLY_SHIPPING_RATE,
      checkoutRepository.applyShippingRate(checkoutId, shippingRate.handle)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribeWith(WeakSingleObserver.<CheckoutViewPresenter, Checkout>forTarget(this)
          .delegateOnSuccess((presenter, checkout) -> presenter.onCheckout(checkout, REQUEST_ID_APPLY_SHIPPING_RATE))
          .delegateOnError((presenter, t) -> presenter.onRequestError(REQUEST_ID_FETCH_SHIPPING_RATES, t))
          .create()
        )
    );
  }

  public void confirmCheckout() {
    FullWalletRequest fullWalletRequest = payCart.fullWalletRequest(maskedWallet);
    Wallet.Payments.loadFullWallet(googleApiClient, fullWalletRequest, PayHelper.REQUEST_CODE_FULL_WALLET);
  }

  public void completeCheckout(final FullWallet fullWallet) {
    if (isViewDetached()) {
      return;
    }

    String androidPayPublicKey = BuildConfig.ANDROID_PAY_PUBLIC_KEY;
    PaymentToken paymentToken = PayHelper.extractPaymentToken(fullWallet, androidPayPublicKey);
    PayAddress billingAddress = PayAddress.fromUserAddress(fullWallet.getBuyerBillingAddress());

    view().showProgress(REQUEST_ID_COMPLETE_CHECKOUT);
    registerRequest(
      REQUEST_ID_COMPLETE_CHECKOUT,
      checkoutRepository.completeCheckout(checkoutId, payCart, paymentToken, fullWallet.getEmail(), billingAddress)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribeWith(WeakSingleObserver.<CheckoutViewPresenter, Payment>forTarget(this)
          .delegateOnSuccess(CheckoutViewPresenter::onCompleteCheckout)
          .delegateOnError((presenter, t) -> presenter.onRequestError(REQUEST_ID_COMPLETE_CHECKOUT, t))
          .create()
        )
    );
  }

  private void onUpdateCheckoutShippingAddress(final Checkout checkout) {
    payCart = payCart.toBuilder()
      .shippingPrice(checkout.shippingLine != null ? checkout.shippingLine.price : null)
      .totalPrice(checkout.totalPrice)
      .taxPrice(checkout.taxPrice)
      .subtotal(checkout.subtotalPrice)
      .build();
    renderTotalSummary(payCart);

    requestShippingRates();
  }

  private void requestShippingRates() {
    if (isViewDetached()) {
      return;
    }
    this.shippingRates = new Checkout.ShippingRates(false, emptyList());
    renderShippingRates(shippingRates, null);

    view().showProgress(REQUEST_ID_FETCH_SHIPPING_RATES);
    registerRequest(
      REQUEST_ID_FETCH_SHIPPING_RATES,
      checkoutRepository.fetchShippingRates(checkoutId)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribeWith(WeakSingleObserver.<CheckoutViewPresenter, Checkout.ShippingRates>forTarget(this)
          .delegateOnSuccess(CheckoutViewPresenter::invalidateShippingRates)
          .delegateOnError((presenter, t) -> presenter.onRequestError(REQUEST_ID_FETCH_SHIPPING_RATES, t))
          .create()
        )
    );
  }

  private void invalidateShippingRates(final Checkout.ShippingRates shippingRates) {
    if (isViewDetached()) {
      return;
    }

    this.shippingRates = shippingRates;
    if (!shippingRates.ready || shippingRates.shippingRates.isEmpty()) {
      hideProgress(REQUEST_ID_FETCH_SHIPPING_RATES);
      return;
    }

    applyShippingRate(shippingRates.shippingRates.get(0));
  }

  private void onCheckout(final Checkout checkout, final int requestId) {
    hideProgress(requestId);

    if (isViewDetached()) {
      return;
    }

    this.shippingRates = checkout.shippingRates;

    PayCart.Builder payCartBuilder = payCart.toBuilder()
      .shippingPrice(checkout.shippingLine != null ? checkout.shippingLine.price : null)
      .totalPrice(checkout.totalPrice)
      .taxPrice(checkout.taxPrice)
      .subtotal(checkout.subtotalPrice);
    fold(payCartBuilder, checkout.lineItems, (accumulator, lineItem) ->
      accumulator.addLineItem(lineItem.title, lineItem.quantity, lineItem.price));

    payCart = payCartBuilder.build();

    renderTotalSummary(payCart);
    renderShippingRates(shippingRates, checkout.shippingLine);
  }

  private void renderTotalSummary(final PayCart payCart) {
    if (isViewAttached()) {
      view().renderTotalSummary(payCart.subtotal, payCart.shippingPrice != null ? payCart.shippingPrice : BigDecimal.ZERO,
        payCart.taxPrice != null ? payCart.taxPrice : BigDecimal.ZERO, payCart.totalPrice);
    }
  }

  private void renderShippingRates(final Checkout.ShippingRates shippingRates, final Checkout.ShippingRate shippingLine) {
    if (isViewAttached()) {
      view().renderShippingRates(shippingRates, shippingLine);
    }
  }

  private GoogleApiClient googleApiClient() {
    if (isAndroidPayEnabledInManifest(view().context())) {
      return new GoogleApiClient.Builder(view().context())
        .addApi(Wallet.API, new Wallet.WalletOptions.Builder()
          .setEnvironment(BuildConfig.ANDROID_PAY_ENVIRONMENT)
          .setTheme(WalletConstants.THEME_LIGHT)
          .build())
        .build();
    }
    return null;
  }

  private void onCompleteCheckout(final Payment payment) {
    hideProgress(REQUEST_ID_COMPLETE_CHECKOUT);
    if (isViewDetached()) {
      return;
    }
  }

  public interface View extends com.shopify.sample.mvp.View {
    Context context();

    void updateMaskedWallet(@NonNull MaskedWallet maskedWallet);

    void renderTotalSummary(@NonNull BigDecimal subtotal, @NonNull BigDecimal shipping, @NonNull BigDecimal tax, @NonNull BigDecimal total);

    void renderShippingRates(@Nullable Checkout.ShippingRates shippingRates, @Nullable Checkout.ShippingRate shippingLine);
  }
}
