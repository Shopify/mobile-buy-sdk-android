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

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wallet.FullWallet;
import com.google.android.gms.wallet.MaskedWallet;
import com.google.android.gms.wallet.WalletConstants;
import com.shopify.buy3.pay.PayAddress;
import com.shopify.buy3.pay.PayCart;
import com.shopify.buy3.pay.PayHelper;
import com.shopify.buy3.pay.PaymentToken;
import com.shopify.sample.BuildConfig;
import com.shopify.sample.domain.interactor.CheckoutCompleteInteractor;
import com.shopify.sample.domain.interactor.CheckoutShippingAddressUpdateInteractor;
import com.shopify.sample.domain.interactor.CheckoutShippingLineUpdateInteractor;
import com.shopify.sample.domain.model.Checkout;
import com.shopify.sample.domain.model.Payment;
import com.shopify.sample.mvp.BaseViewPresenter;
import com.shopify.sample.util.WeakSingleObserver;

import java.math.BigDecimal;

import io.reactivex.android.schedulers.AndroidSchedulers;

import static com.shopify.sample.util.Util.checkNotBlank;
import static com.shopify.sample.util.Util.checkNotNull;

public final class CheckoutViewPresenter extends BaseViewPresenter<CheckoutViewPresenter.View> {
  public static final int REQUEST_ID_UPDATE_CHECKOUT_SHIPPING_ADDRESS = 1;
  public static final int REQUEST_ID_APPLY_SHIPPING_RATE = 2;
  public static final int REQUEST_ID_COMPLETE_CHECKOUT = 3;

  private final CheckoutShippingAddressUpdateInteractor checkoutShippingAddressUpdateInteractor;
  private final CheckoutShippingLineUpdateInteractor checkoutShippingLineUpdateInteractor;
  private final CheckoutCompleteInteractor checkoutCompleteInteractor;
  private final String checkoutId;
  private PayCart payCart;
  private MaskedWallet maskedWallet;
  private boolean newMaskedWalletRequired;
  private Checkout.ShippingRate pendingCheckoutShippingRate;

  public CheckoutViewPresenter(@NonNull final String checkoutId, @NonNull final PayCart payCart, @NonNull final MaskedWallet maskedWallet,
    @NonNull final CheckoutShippingAddressUpdateInteractor checkoutShippingAddressUpdateInteractor,
    @NonNull final CheckoutShippingLineUpdateInteractor checkoutShippingLineUpdateInteractor,
    @NonNull final CheckoutCompleteInteractor checkoutCompleteInteractor) {
    this.checkoutId = checkNotBlank(checkoutId, "checkoutId can't be empty");
    this.payCart = checkNotNull(payCart, "payCart == null");
    this.maskedWallet = checkNotNull(maskedWallet, "maskedWallet == null");
    this.checkoutShippingAddressUpdateInteractor = checkNotNull(checkoutShippingAddressUpdateInteractor,
      "checkoutShippingAddressUpdateInteractor == null");
    this.checkoutShippingLineUpdateInteractor = checkNotNull(checkoutShippingLineUpdateInteractor,
      "checkoutShippingLineUpdateInteractor == null");
    this.checkoutCompleteInteractor = checkNotNull(checkoutCompleteInteractor, "checkoutCompleteInteractor == null");
  }

  public void attachView(final View view) {
    super.attachView(view);
    view.renderTotalSummary(payCart.subtotal, payCart.shippingPrice != null ? payCart.shippingPrice : BigDecimal.ZERO,
      payCart.taxPrice != null ? payCart.taxPrice : BigDecimal.ZERO, payCart.totalPrice);
    updateMaskedWallet(maskedWallet);
  }

  public void applyShippingRate(@NonNull final Checkout.ShippingRate shippingRate) {
    checkNotNull(shippingRate, "shippingRate == null");

    if (isViewDetached()) {
      return;
    }

    view().showProgress(REQUEST_ID_APPLY_SHIPPING_RATE);
    registerRequest(
      REQUEST_ID_APPLY_SHIPPING_RATE,
      checkoutShippingLineUpdateInteractor.execute(checkoutId, shippingRate.handle)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribeWith(WeakSingleObserver.<CheckoutViewPresenter, Checkout>forTarget(this)
          .delegateOnSuccess((presenter, checkout) -> presenter.onApplyShippingRate(checkout, REQUEST_ID_APPLY_SHIPPING_RATE))
          .delegateOnError((presenter, t) -> presenter.onRequestError(REQUEST_ID_APPLY_SHIPPING_RATE, t))
          .create()
        )
    );
  }

  public void confirmCheckout(@NonNull final GoogleApiClient googleApiClient, @NonNull final Checkout.ShippingRate shippingRate) {
    checkNotNull(googleApiClient, "googleApiClient == null");
    pendingCheckoutShippingRate = checkNotNull(shippingRate, "shippingRate == null");
    payCart = payCart.toBuilder()
      .shippingPrice(pendingCheckoutShippingRate.price)
      .build();
    if (newMaskedWalletRequired) {
      newMaskedWalletRequired = false;
      PayHelper.newMaskedWallet(googleApiClient, maskedWallet);
    } else {
      PayHelper.requestFullWallet(googleApiClient, payCart, maskedWallet);
    }
  }

  public void handleWalletResponse(final int requestCode, final int resultCode, @Nullable final Intent data,
    @NonNull final GoogleApiClient googleApiClient) {
    PayHelper.handleWalletResponse(requestCode, resultCode, data, new PayHelper.WalletResponseHandler() {
      @Override public void onWalletError(final int requestCode, final int errorCode) {
        if (errorCode == WalletConstants.ERROR_CODE_INVALID_TRANSACTION) {
          requestMaskedWallet(googleApiClient);
        } else {
          showError(-1, new RuntimeException("Failed wallet request, errorCode: " + errorCode));
        }
      }

      @Override public void onMaskedWallet(final MaskedWallet maskedWallet) {
        updateMaskedWallet(maskedWallet);
      }

      @Override public void onFullWallet(final FullWallet fullWallet) {
        completeCheckout(fullWallet);
      }
    });
  }

  private void requestMaskedWallet(@NonNull final GoogleApiClient googleApiClient) {
    checkNotNull(googleApiClient, "googleApiClient == null");
    PayHelper.requestMaskedWallet(googleApiClient, payCart, BuildConfig.ANDROID_PAY_PUBLIC_KEY);
  }

  private void completeCheckout(final FullWallet fullWallet) {
    if (isViewDetached()) {
      return;
    }

    String androidPayPublicKey = BuildConfig.ANDROID_PAY_PUBLIC_KEY;
    PaymentToken paymentToken = PayHelper.extractPaymentToken(fullWallet, androidPayPublicKey);
    PayAddress billingAddress = PayAddress.fromUserAddress(fullWallet.getBuyerBillingAddress());

    if (paymentToken == null) {
      showError(-1, new RuntimeException("Failed to extract Android payment token"));
      return;
    }

    view().showProgress(REQUEST_ID_COMPLETE_CHECKOUT);
    registerRequest(
      REQUEST_ID_COMPLETE_CHECKOUT,
      checkoutCompleteInteractor.execute(checkoutId, payCart, paymentToken, fullWallet.getEmail(), billingAddress,
        pendingCheckoutShippingRate.handle)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribeWith(WeakSingleObserver.<CheckoutViewPresenter, Payment>forTarget(this)
          .delegateOnSuccess(CheckoutViewPresenter::onCompleteCheckout)
          .delegateOnError(CheckoutViewPresenter::onCompleteCheckoutError)
          .create()
        )
    );
  }

  private void updateMaskedWallet(@NonNull final MaskedWallet maskedWallet) {
    if (isViewDetached()) {
      return;
    }
    this.maskedWallet = checkNotNull(maskedWallet, "maskedWallet == null");
    view().updateMaskedWallet(maskedWallet);

    PayAddress payAddress = PayAddress.fromUserAddress(maskedWallet.getBuyerShippingAddress());
    showProgress(REQUEST_ID_UPDATE_CHECKOUT_SHIPPING_ADDRESS);
    registerRequest(
      REQUEST_ID_UPDATE_CHECKOUT_SHIPPING_ADDRESS,
      checkoutShippingAddressUpdateInteractor.execute(checkNotBlank(checkoutId, "checkoutId can't be empty"), payAddress)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribeWith(WeakSingleObserver.<CheckoutViewPresenter, Checkout>forTarget(this)
          .delegateOnSuccess(CheckoutViewPresenter::onUpdateCheckoutShippingAddress)
          .delegateOnError((presenter, t) -> presenter.onRequestError(REQUEST_ID_UPDATE_CHECKOUT_SHIPPING_ADDRESS, t))
          .create())
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

    if (isViewAttached()) {
      view().invalidateShippingRates();
    }
  }

  private void onApplyShippingRate(final Checkout checkout, final int requestId) {
    hideProgress(requestId);

    if (isViewDetached()) {
      return;
    }

    payCart = payCart.toBuilder()
      .shippingPrice(checkout.shippingLine != null ? checkout.shippingLine.price : null)
      .totalPrice(checkout.totalPrice)
      .taxPrice(checkout.taxPrice)
      .build();

    renderTotalSummary(payCart);
  }

  private void renderTotalSummary(final PayCart payCart) {
    if (isViewAttached()) {
      view().renderTotalSummary(payCart.subtotal, payCart.shippingPrice != null ? payCart.shippingPrice : BigDecimal.ZERO,
        payCart.taxPrice != null ? payCart.taxPrice : BigDecimal.ZERO, payCart.totalPrice);
    }
  }

  private void onCompleteCheckout(final Payment payment) {
    hideProgress(REQUEST_ID_COMPLETE_CHECKOUT);
    newMaskedWalletRequired = true;
    if (isViewDetached()) {
      return;
    }
  }

  private void onCompleteCheckoutError(Throwable t) {
    newMaskedWalletRequired = true;
    onRequestError(REQUEST_ID_COMPLETE_CHECKOUT, t);
  }

  public interface View extends com.shopify.sample.mvp.View {
    void updateMaskedWallet(@NonNull MaskedWallet maskedWallet);

    void renderTotalSummary(@NonNull BigDecimal subtotal, @NonNull BigDecimal shipping, @NonNull BigDecimal tax, @NonNull BigDecimal total);

    void invalidateShippingRates();
  }
}
