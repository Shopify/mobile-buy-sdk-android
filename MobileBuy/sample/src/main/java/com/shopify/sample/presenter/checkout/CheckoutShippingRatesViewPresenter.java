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

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.shopify.sample.domain.interactor.CheckoutShippingRatesInteractor;
import com.shopify.sample.domain.model.Checkout;
import com.shopify.sample.mvp.BaseViewPresenter;
import com.shopify.sample.util.WeakSingleObserver;

import io.reactivex.android.schedulers.AndroidSchedulers;

import static com.shopify.sample.util.Util.checkNotBlank;
import static com.shopify.sample.util.Util.checkNotNull;
import static com.shopify.sample.util.Util.firstItem;
import static java.util.Collections.emptyList;

public final class CheckoutShippingRatesViewPresenter extends BaseViewPresenter<CheckoutShippingRatesViewPresenter.View> {
  public static final int REQUEST_ID_FETCH_SHIPPING_RATES = 1;

  private final String checkoutId;
  private Checkout.ShippingRate selectedShippingRate;
  private final CheckoutShippingRatesInteractor checkoutShippingRatesInteractor;
  private Checkout.ShippingRates shippingRates = new Checkout.ShippingRates(false, emptyList());

  public CheckoutShippingRatesViewPresenter(@NonNull final String checkoutId,
    @NonNull final CheckoutShippingRatesInteractor checkoutShippingRatesInteractor) {
    this.checkoutId = checkNotBlank(checkoutId, "checkoutId can't be empty");
    this.checkoutShippingRatesInteractor = checkNotNull(checkoutShippingRatesInteractor, "checkoutShippingRatesInteractor == null");
  }

  public void invalidateShippingRates() {
    if (isViewDetached()) {
      return;
    }
    selectedShippingRate = null;

    this.shippingRates = new Checkout.ShippingRates(false, emptyList());
    view().onShippingRateSelected(selectedShippingRate);
    view().showProgress(REQUEST_ID_FETCH_SHIPPING_RATES);

    registerRequest(
      REQUEST_ID_FETCH_SHIPPING_RATES,
      checkoutShippingRatesInteractor.execute(checkoutId)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribeWith(WeakSingleObserver.<CheckoutShippingRatesViewPresenter, Checkout.ShippingRates>forTarget(this)
          .delegateOnSuccess(CheckoutShippingRatesViewPresenter::onShippingRates)
          .delegateOnError((presenter, t) -> presenter.onRequestError(REQUEST_ID_FETCH_SHIPPING_RATES, t))
          .create()
        )
    );
  }

  @NonNull public Checkout.ShippingRates shippingRates() {
    return shippingRates;
  }

  public void setSelectedShippingRate(@Nullable final Checkout.ShippingRate selectedShippingRate) {
    this.selectedShippingRate = selectedShippingRate;
    view().onShippingRateSelected(selectedShippingRate);
  }

  @Nullable public Checkout.ShippingRate selectedShippingRate() {
    return selectedShippingRate;
  }

  private void onShippingRates(final Checkout.ShippingRates shippingRates) {
    if (isViewDetached()) {
      return;
    }
    hideProgress(REQUEST_ID_FETCH_SHIPPING_RATES);

    this.shippingRates = shippingRates != null ? shippingRates : new Checkout.ShippingRates(false, emptyList());
    selectedShippingRate = firstItem(this.shippingRates.shippingRates);

    view().onShippingRateSelected(selectedShippingRate);
  }

  public interface View extends com.shopify.sample.mvp.View {
    void onShippingRateSelected(@Nullable Checkout.ShippingRate shippingRate);
  }
}
