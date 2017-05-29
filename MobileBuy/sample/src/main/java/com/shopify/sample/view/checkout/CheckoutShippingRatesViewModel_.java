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

import com.shopify.sample.domain.interactor.CheckoutShippingRatesInteractor;
import com.shopify.sample.domain.interactor.RealCheckoutShippingRatesInteractor;
import com.shopify.sample.domain.model.Checkout;
import com.shopify.sample.util.WeakSingleObserver;
import com.shopify.sample.view.BaseViewModel;

import io.reactivex.android.schedulers.AndroidSchedulers;

import static com.shopify.sample.util.Util.checkNotBlank;
import static com.shopify.sample.util.Util.firstItem;
import static java.util.Collections.emptyList;

@SuppressWarnings("WeakerAccess")
public final class CheckoutShippingRatesViewModel_ extends BaseViewModel {
  public static final int REQUEST_ID_FETCH_SHIPPING_RATES = CheckoutShippingRatesInteractor.class.hashCode();

  private final CheckoutShippingRatesInteractor checkoutShippingRatesInteractor = new RealCheckoutShippingRatesInteractor();

  private final MutableLiveData<Checkout.ShippingRate> selectedShippingRateLiveData = new MutableLiveData<>();
  private final MutableLiveData<Checkout.ShippingRates> shippingRatesLiveData = new MutableLiveData<>();

  public void invalidateShippingRates(@NonNull final String checkoutId) {
    checkNotBlank(checkoutId, "checkoutId can't be empty");

    selectedShippingRateLiveData.setValue(null);
    shippingRatesLiveData.setValue(new Checkout.ShippingRates(false, emptyList()));

    showProgress(REQUEST_ID_FETCH_SHIPPING_RATES);
    registerRequest(
      REQUEST_ID_FETCH_SHIPPING_RATES,
      checkoutShippingRatesInteractor.execute(checkoutId)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribeWith(WeakSingleObserver.<CheckoutShippingRatesViewModel_, Checkout.ShippingRates>forTarget(this)
          .delegateOnSuccess(CheckoutShippingRatesViewModel_::onShippingRates)
          .delegateOnError((presenter, t) -> presenter.onRequestError(REQUEST_ID_FETCH_SHIPPING_RATES, t))
          .create()
        )
    );
  }

  public void selectShippingRate(final Checkout.ShippingRate shippingRate) {
    selectedShippingRateLiveData.setValue(shippingRate);
  }

  public LiveData<Checkout.ShippingRate> selectedShippingRateLiveData() {
    return selectedShippingRateLiveData;
  }

  public LiveData<Checkout.ShippingRates> shippingRatesLiveData() {
    return shippingRatesLiveData;
  }

  private void onShippingRates(final Checkout.ShippingRates shippingRates) {
    hideProgress(REQUEST_ID_FETCH_SHIPPING_RATES);
    shippingRatesLiveData.setValue(shippingRates != null ? shippingRates : new Checkout.ShippingRates(false, emptyList()));
    selectedShippingRateLiveData.setValue(shippingRates != null ? firstItem(shippingRates.shippingRates) : null);
  }

  private void onRequestError(final int requestId, final Throwable t) {
    hideProgress(requestId);
    notifyUserError(requestId, t);
  }
}
