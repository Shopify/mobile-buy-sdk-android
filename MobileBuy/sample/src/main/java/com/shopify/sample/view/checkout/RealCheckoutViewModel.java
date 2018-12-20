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
import android.support.annotation.NonNull;
import com.shopify.sample.domain.interactor.*;
import com.shopify.sample.domain.model.Checkout;
import com.shopify.sample.domain.model.Payment;
import com.shopify.sample.util.WeakSingleObserver;
import com.shopify.sample.view.BaseViewModel;
import com.shopify.sample.view.LifeCycleBoundCallback;
import io.reactivex.android.schedulers.AndroidSchedulers;
import timber.log.Timber;

import static com.shopify.sample.util.Util.checkNotBlank;
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

  private final MutableLiveData<Checkout.ShippingRate> pendingSelectShippingRateLiveData = new MutableLiveData<>();
  private final MutableLiveData<Checkout.ShippingRate> selectedShippingRateLiveData = new MutableLiveData<>();
  private final MutableLiveData<Checkout.ShippingRates> shippingRatesLiveData = new MutableLiveData<>();
  private final LifeCycleBoundCallback<Payment> successPaymentLiveData = new LifeCycleBoundCallback<>();

  private final String checkoutId;

  public RealCheckoutViewModel(@NonNull final String checkoutId) {
    this.checkoutId = checkNotBlank(checkoutId, "checkoutId can't be empty");

    pendingSelectShippingRateLiveData.observeForever(it -> {
      cancelAllRequests();
      selectedShippingRateLiveData.setValue(null);
      if (it != null) {
        applyShippingRate(it);
      }
    });
    successPaymentLiveData.observeForever(it -> {
      if (it != null) {
        cartClearInteractor.execute();
      }
    });
  }

  @Override public void fetchShippingRates() {
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

  @Override public LifeCycleBoundCallback<Payment> successPaymentLiveData() {
    return successPaymentLiveData;
  }

  @Override public void confirmCheckout() {
    Checkout.ShippingRate shippingRate = selectedShippingRateLiveData().getValue();
    if (shippingRate == null) {
      notifyUserError(REQUEST_ID_CONFIRM_CHECKOUT, new ShippingRateMissingException());
      return;
    }
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
  }

  private void onShippingRates(final Checkout.ShippingRates shippingRates) {
    hideProgress(REQUEST_ID_FETCH_SHIPPING_RATES);
    shippingRatesLiveData.setValue(shippingRates != null ? shippingRates : new Checkout.ShippingRates(false, emptyList()));
    pendingSelectShippingRateLiveData.setValue(shippingRates != null ? firstItem(shippingRates.shippingRates) : null);
  }

  private void onRequestError(final int requestId, final Throwable t) {
    Timber.e(t);

    if (requestId == REQUEST_ID_UPDATE_CHECKOUT_SHIPPING_ADDRESS) {
    }

    hideProgress(requestId);
    notifyUserError(requestId, t);
  }

  private void updateShippingAddress() {
    pendingSelectShippingRateLiveData.setValue(null);
    selectedShippingRateLiveData.setValue(null);
    shippingRatesLiveData.setValue(new Checkout.ShippingRates(false, emptyList()));

//    showProgress(REQUEST_ID_UPDATE_CHECKOUT_SHIPPING_ADDRESS);
//    registerRequest(
//      REQUEST_ID_UPDATE_CHECKOUT_SHIPPING_ADDRESS,
//      checkoutShippingAddressUpdateInteractor.execute(checkNotBlank(checkoutId, "checkoutId can't be empty"), payAddress)
//        .observeOn(AndroidSchedulers.mainThread())
//        .subscribeWith(WeakSingleObserver.<RealCheckoutViewModel, Checkout>forTarget(this)
//          .delegateOnSuccess(RealCheckoutViewModel::onUpdateCheckoutShippingAddress)
//          .delegateOnError((viewModel, t) -> viewModel.onRequestError(REQUEST_ID_UPDATE_CHECKOUT_SHIPPING_ADDRESS, t))
//          .create())
//    );
  }

  private void onUpdateCheckoutShippingAddress(final Checkout checkout) {
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

  private void completeCheckout() {
//    showProgress(REQUEST_ID_COMPLETE_CHECKOUT);
//    registerRequest(
//      REQUEST_ID_COMPLETE_CHECKOUT,
//      checkoutCompleteInteractor.execute(checkoutId, payCartLiveData().getValue(), paymentToken, fullWallet.getEmail(), billingAddress)
//        .observeOn(AndroidSchedulers.mainThread())
//        .subscribeWith(WeakSingleObserver.<RealCheckoutViewModel, Payment>forTarget(this)
//          .delegateOnSuccess(RealCheckoutViewModel::onCompleteCheckout)
//          .delegateOnError((viewModel, t) -> viewModel.onRequestError(REQUEST_ID_COMPLETE_CHECKOUT, t))
//          .create()
//        )
//    );
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
