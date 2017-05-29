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

package com.shopify.sample.view.checkout;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
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
import com.shopify.sample.domain.interactor.CartClearInteractor;
import com.shopify.sample.domain.interactor.CheckoutCompleteInteractor;
import com.shopify.sample.domain.interactor.CheckoutShippingAddressUpdateInteractor;
import com.shopify.sample.domain.interactor.CheckoutShippingLineUpdateInteractor;
import com.shopify.sample.domain.interactor.CheckoutShippingRatesInteractor;
import com.shopify.sample.domain.interactor.RealCartClearInteractor;
import com.shopify.sample.domain.interactor.RealCheckoutCompleteInteractor;
import com.shopify.sample.domain.interactor.RealCheckoutShippingAddressUpdateInteractor;
import com.shopify.sample.domain.interactor.RealCheckoutShippingLineUpdateInteractor;
import com.shopify.sample.domain.interactor.RealCheckoutShippingRatesInteractor;
import com.shopify.sample.domain.model.Checkout;
import com.shopify.sample.domain.model.Payment;
import com.shopify.sample.util.WeakSingleObserver;
import com.shopify.sample.view.BaseViewModel;
import com.shopify.sample.view.LifeCycleBoundCallback;

import io.reactivex.android.schedulers.AndroidSchedulers;
import timber.log.Timber;

import static com.shopify.sample.util.Util.checkNotBlank;
import static com.shopify.sample.util.Util.checkNotNull;
import static com.shopify.sample.util.Util.firstItem;
import static java.util.Collections.emptyList;

@SuppressWarnings("WeakerAccess")
public class RealCheckoutViewModel extends BaseViewModel implements CheckoutViewModel, CheckoutShippingRatesViewModel {
  private final CheckoutShippingRatesInteractor checkoutShippingRatesInteractor = new RealCheckoutShippingRatesInteractor();
  private final CheckoutShippingAddressUpdateInteractor checkoutShippingAddressUpdateInteractor =
    new RealCheckoutShippingAddressUpdateInteractor();
  private final CheckoutShippingLineUpdateInteractor checkoutShippingLineUpdateInteractor = new RealCheckoutShippingLineUpdateInteractor();
  private final CheckoutCompleteInteractor checkoutCompleteInteractor = new RealCheckoutCompleteInteractor();
  private final CartClearInteractor cartClearInteractor = new RealCartClearInteractor();

  private final MutableLiveData<PayCart> payCartLiveData = new MutableLiveData<>();
  private final MutableLiveData<MaskedWallet> maskedWalletLiveData = new MutableLiveData<>();
  private final MutableLiveData<Checkout.ShippingRate> pendingSelectShippingRateLiveData = new MutableLiveData<>();
  private final MutableLiveData<Checkout.ShippingRate> selectedShippingRateLiveData = new MutableLiveData<>();
  private final MutableLiveData<Checkout.ShippingRates> shippingRatesLiveData = new MutableLiveData<>();
  private final LifeCycleBoundCallback<Payment> successPaymentLiveData = new LifeCycleBoundCallback<>();

  private final String checkoutId;
  private boolean newMaskedWalletRequired;

  public RealCheckoutViewModel(@NonNull final String checkoutId, @NonNull final PayCart payCart, @NonNull final MaskedWallet maskedWallet) {
    this.checkoutId = checkNotBlank(checkoutId, "checkoutId can't be empty");

    payCartLiveData.setValue(checkNotNull(payCart, "payCart == null"));
    maskedWalletLiveData.setValue(checkNotNull(maskedWallet, "maskedWallet == null"));

    pendingSelectShippingRateLiveData.observeForever(it -> {
      cancelAllRequests();
      selectedShippingRateLiveData.setValue(null);
      if (it != null) {
        applyShippingRate(it);
      }
    });
    maskedWalletLiveData.observeForever(it -> {
      cancelAllRequests();
      if (it != null) {
        updateShippingAddress(PayAddress.fromUserAddress(it.getBuyerShippingAddress()));
      }
    });
    successPaymentLiveData.observeForever(it -> {
      if (it != null) {
        cartClearInteractor.execute();
      }
    });
  }

  @Override public void fetchShippingRates() {
    MaskedWallet maskedWallet = maskedWalletLiveData().getValue();
    if (maskedWallet == null) {
      return;
    }
    updateShippingAddress(PayAddress.fromUserAddress(maskedWallet.getBuyerShippingAddress()));
  }

  @Override public void selectShippingRate(final Checkout.ShippingRate shippingRate) {
    pendingSelectShippingRateLiveData.setValue(shippingRate);
  }

  @Override public LiveData<Checkout.ShippingRate> selectedShippingRateLiveData() {
    return selectedShippingRateLiveData;
  }

  @Override public LiveData<Checkout.ShippingRates> shippingRatesLiveData() {
    return shippingRatesLiveData;
  }

  @Override public LiveData<PayCart> payCartLiveData() {
    return payCartLiveData;
  }

  @Override public LiveData<MaskedWallet> maskedWalletLiveData() {
    return maskedWalletLiveData;
  }

  @Override public LifeCycleBoundCallback<Payment> successPaymentLiveData() {
    return successPaymentLiveData;
  }

  @Override public void confirmCheckout(@NonNull final GoogleApiClient googleApiClient) {
    checkNotNull(googleApiClient, "googleApiClient == null");

    Checkout.ShippingRate shippingRate = selectedShippingRateLiveData().getValue();
    if (shippingRate == null) {
      notifyUserError(REQUEST_ID_CONFIRM_CHECKOUT, new ShippingRateMissingException());
      return;
    }

    payCartLiveData.setValue(payCartLiveData.getValue().toBuilder()
      .shippingPrice(shippingRate.price)
      .build());

    if (newMaskedWalletRequired) {
      PayHelper.newMaskedWallet(googleApiClient, maskedWalletLiveData.getValue());
    } else {
      PayHelper.requestFullWallet(googleApiClient, payCartLiveData.getValue(), maskedWalletLiveData.getValue());
    }
  }

  @Override public void handleWalletResponse(final int requestCode, final int resultCode, @Nullable final Intent data,
    @NonNull final GoogleApiClient googleApiClient) {
    PayHelper.handleWalletResponse(requestCode, resultCode, data, new PayHelper.WalletResponseHandler() {
      @Override public void onWalletError(final int requestCode, final int errorCode) {
        if (errorCode == WalletConstants.ERROR_CODE_INVALID_TRANSACTION) {
          PayHelper.requestMaskedWallet(googleApiClient, payCartLiveData.getValue(), BuildConfig.ANDROID_PAY_PUBLIC_KEY);
        } else {
          notifyUserError(-1, new RuntimeException("Failed wallet request, errorCode: " + errorCode));
        }
      }

      @Override public void onMaskedWallet(final MaskedWallet maskedWallet) {
        newMaskedWalletRequired = false;
        maskedWalletLiveData.setValue(maskedWallet);
      }

      @Override public void onFullWallet(final FullWallet fullWallet) {
        completeCheckout(fullWallet);
      }
    });
  }

  private void applyShippingRate(final Checkout.ShippingRate shippingRate) {
    showProgress(REQUEST_ID_APPLY_SHIPPING_RATE);
    registerRequest(
      REQUEST_ID_APPLY_SHIPPING_RATE,
      checkoutShippingLineUpdateInteractor.execute(checkoutId, shippingRate.handle)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribeWith(WeakSingleObserver.<RealCheckoutViewModel, Checkout>forTarget(this)
          .delegateOnSuccess((viewModel, checkout) -> viewModel.onApplyShippingRate(checkout, REQUEST_ID_APPLY_SHIPPING_RATE))
          .delegateOnError((viewModel, t) -> viewModel.onRequestError(REQUEST_ID_APPLY_SHIPPING_RATE, t))
          .create()
        )
    );
  }

  private void onApplyShippingRate(final Checkout checkout, final int requestId) {
    hideProgress(requestId);
    selectedShippingRateLiveData.setValue(checkout.shippingLine);
    payCartLiveData.setValue(payCartLiveData.getValue().toBuilder()
      .shippingPrice(checkout.shippingLine != null ? checkout.shippingLine.price : null)
      .totalPrice(checkout.totalPrice)
      .taxPrice(checkout.taxPrice)
      .build());
  }

  private void onShippingRates(final Checkout.ShippingRates shippingRates) {
    hideProgress(REQUEST_ID_FETCH_SHIPPING_RATES);
    shippingRatesLiveData.setValue(shippingRates != null ? shippingRates : new Checkout.ShippingRates(false, emptyList()));
    pendingSelectShippingRateLiveData.setValue(shippingRates != null ? firstItem(shippingRates.shippingRates) : null);
  }

  private void onRequestError(final int requestId, final Throwable t) {
    Timber.e(t);

    if (requestId == REQUEST_ID_UPDATE_CHECKOUT_SHIPPING_ADDRESS) {
      newMaskedWalletRequired = true;
    }

    hideProgress(requestId);
    notifyUserError(requestId, t);
  }

  private void updateShippingAddress(final PayAddress payAddress) {
    pendingSelectShippingRateLiveData.setValue(null);
    selectedShippingRateLiveData.setValue(null);
    shippingRatesLiveData.setValue(new Checkout.ShippingRates(false, emptyList()));

    showProgress(REQUEST_ID_UPDATE_CHECKOUT_SHIPPING_ADDRESS);
    registerRequest(
      REQUEST_ID_UPDATE_CHECKOUT_SHIPPING_ADDRESS,
      checkoutShippingAddressUpdateInteractor.execute(checkNotBlank(checkoutId, "checkoutId can't be empty"), payAddress)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribeWith(WeakSingleObserver.<RealCheckoutViewModel, Checkout>forTarget(this)
          .delegateOnSuccess(RealCheckoutViewModel::onUpdateCheckoutShippingAddress)
          .delegateOnError((viewModel, t) -> viewModel.onRequestError(REQUEST_ID_UPDATE_CHECKOUT_SHIPPING_ADDRESS, t))
          .create())
    );
  }

  private void onUpdateCheckoutShippingAddress(final Checkout checkout) {
    payCartLiveData.setValue(payCartLiveData.getValue().toBuilder()
      .shippingPrice(checkout.shippingLine != null ? checkout.shippingLine.price : null)
      .totalPrice(checkout.totalPrice)
      .taxPrice(checkout.taxPrice)
      .subtotal(checkout.subtotalPrice)
      .build());

    invalidateShippingRates();
  }

  private void invalidateShippingRates() {
    checkNotBlank(checkoutId, "checkoutId can't be empty");

    showProgress(REQUEST_ID_FETCH_SHIPPING_RATES);
    registerRequest(
      REQUEST_ID_FETCH_SHIPPING_RATES,
      checkoutShippingRatesInteractor.execute(checkoutId)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribeWith(WeakSingleObserver.<RealCheckoutViewModel, Checkout.ShippingRates>forTarget(this)
          .delegateOnSuccess(RealCheckoutViewModel::onShippingRates)
          .delegateOnError((viewModel, t) -> viewModel.onRequestError(REQUEST_ID_FETCH_SHIPPING_RATES, t))
          .create()
        )
    );
  }

  private void completeCheckout(final FullWallet fullWallet) {
    newMaskedWalletRequired = true;

    String androidPayPublicKey = BuildConfig.ANDROID_PAY_PUBLIC_KEY;
    PaymentToken paymentToken = PayHelper.extractPaymentToken(fullWallet, androidPayPublicKey);
    PayAddress billingAddress = PayAddress.fromUserAddress(fullWallet.getBuyerBillingAddress());

    if (paymentToken == null) {
      notifyUserError(-1, new RuntimeException("Failed to extract Android payment token"));
      return;
    }

    showProgress(REQUEST_ID_COMPLETE_CHECKOUT);
    registerRequest(
      REQUEST_ID_COMPLETE_CHECKOUT,
      checkoutCompleteInteractor.execute(checkoutId, payCartLiveData().getValue(), paymentToken, fullWallet.getEmail(), billingAddress)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribeWith(WeakSingleObserver.<RealCheckoutViewModel, Payment>forTarget(this)
          .delegateOnSuccess(RealCheckoutViewModel::onCompleteCheckout)
          .delegateOnError((viewModel, t) -> viewModel.onRequestError(REQUEST_ID_COMPLETE_CHECKOUT, t))
          .create()
        )
    );
  }

  private void onCompleteCheckout(final Payment payment) {
    hideProgress(REQUEST_ID_COMPLETE_CHECKOUT);
    if (!payment.ready) {
      Timber.e("Payment transaction has not been finished yet");
      notifyUserError(REQUEST_ID_COMPLETE_CHECKOUT, new RuntimeException("Payment transaction has not been finished yet"));
      return;
    }

    if (payment.errorMessage != null) {
      Timber.e("Payment transaction failed");
      notifyUserError(REQUEST_ID_COMPLETE_CHECKOUT, new RuntimeException("Payment transaction failed"), payment.errorMessage);
      return;
    }

    successPaymentLiveData.notify(payment);
  }
}
