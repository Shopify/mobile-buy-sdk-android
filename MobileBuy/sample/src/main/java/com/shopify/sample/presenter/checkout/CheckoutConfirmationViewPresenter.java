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

import com.google.android.gms.wallet.MaskedWallet;
import com.shopify.buy3.pay.PayAddress;
import com.shopify.buy3.pay.PayCart;
import com.shopify.sample.interactor.checkout.UpdateCheckoutShippingAddress;
import com.shopify.sample.mvp.BaseViewPresenter;
import com.shopify.sample.util.WeakObserver;

import java.math.BigDecimal;

import io.reactivex.android.schedulers.AndroidSchedulers;

import static com.shopify.sample.util.Util.checkNotBlank;
import static com.shopify.sample.util.Util.checkNotNull;
import static com.shopify.sample.util.Util.fold;

public final class CheckoutConfirmationViewPresenter extends BaseViewPresenter<CheckoutConfirmationViewPresenter.View> {
  public static final int REQUEST_ID_UPDATE_CHECKOUT_SHIPPING_ADDRESS = 1;

  private final UpdateCheckoutShippingAddress updateCheckoutShippingAddress;
  private String checkoutId;
  private PayCart payCart;

  public CheckoutConfirmationViewPresenter(@NonNull final UpdateCheckoutShippingAddress updateCheckoutShippingAddress) {
    this.updateCheckoutShippingAddress = checkNotNull(updateCheckoutShippingAddress, "updateCheckoutShippingAddress == null");
  }

  public void attachView(final View view, @NonNull final String checkoutId, @NonNull final PayCart payCart) {
    super.attachView(view);
    this.checkoutId = checkNotBlank(checkoutId, "checkoutId can't be empty");
    this.payCart = checkNotNull(payCart, "payCart == null");

    view.renderTotalSummary(payCart.subtotal, payCart.shippingPrice != null ? payCart.shippingPrice : BigDecimal.ZERO,
      payCart.taxPrice != null ? payCart.taxPrice : BigDecimal.ZERO, payCart.totalPrice);

    updateMaskedWallet(checkNotNull(payCart.maskedWallet, "payCart.maskedWallet == null"));
  }

  public void updateMaskedWallet(@NonNull final MaskedWallet maskedWallet) {
    checkNotNull(maskedWallet, "maskedWallet == null");
    if (!isViewAttached()) {
      return;
    }
    view().updateMaskedWallet(maskedWallet);

    PayAddress payAddress = PayAddress.fromUserAddress(maskedWallet.getBuyerShippingAddress());
    showProgress(REQUEST_ID_UPDATE_CHECKOUT_SHIPPING_ADDRESS);
    registerRequest(
      REQUEST_ID_UPDATE_CHECKOUT_SHIPPING_ADDRESS,
      updateCheckoutShippingAddress.call(checkNotBlank(checkoutId, "checkoutId can't be empty"), payAddress)
        .toObservable()
        .observeOn(AndroidSchedulers.mainThread())
        .subscribeWith(WeakObserver.<CheckoutConfirmationViewPresenter, Checkout>forTarget(this)
          .delegateOnNext((presenter, checkout) -> presenter.onUpdateCheckoutShippingAddress(checkout, maskedWallet))
          .delegateOnError(CheckoutConfirmationViewPresenter::onUpdateCheckoutShippingError)
          .create())
    );
  }

  private void onUpdateCheckoutShippingAddress(final Checkout checkout, final MaskedWallet maskedWallet) {
    if (isViewAttached()) {
      view().hideProgress(REQUEST_ID_UPDATE_CHECKOUT_SHIPPING_ADDRESS);

      PayCart.Builder payCartBuilder = PayCart.builder()
        .merchantName("SampleApp")
        .currencyCode(checkout.currency)
        .phoneNumberRequired(true)
        .shippingAddressRequired(checkout.requiresShipping)
        .maskedWallet(maskedWallet);

      fold(payCartBuilder, checkout.lineItems, (accumulator, lineItem) ->
        accumulator.addLineItem(lineItem.title, lineItem.quantity, lineItem.price));

      payCart = payCartBuilder.build();

      if (isViewAttached()) {
        view().renderTotalSummary(payCart.subtotal, payCart.shippingPrice != null ? payCart.shippingPrice : BigDecimal.ZERO,
          payCart.taxPrice != null ? payCart.taxPrice : BigDecimal.ZERO, payCart.totalPrice);
      }
    }
  }

  private void onUpdateCheckoutShippingError(final Throwable t) {
    if (isViewAttached()) {
      view().hideProgress(REQUEST_ID_UPDATE_CHECKOUT_SHIPPING_ADDRESS);
      view().showError(REQUEST_ID_UPDATE_CHECKOUT_SHIPPING_ADDRESS, t);
    }
  }

  public interface View extends com.shopify.sample.mvp.View {
    void updateMaskedWallet(@NonNull MaskedWallet maskedWallet);

    void renderTotalSummary(@NonNull BigDecimal subtotal, @NonNull BigDecimal shipping, @NonNull BigDecimal tax, @NonNull BigDecimal total);
  }
}
