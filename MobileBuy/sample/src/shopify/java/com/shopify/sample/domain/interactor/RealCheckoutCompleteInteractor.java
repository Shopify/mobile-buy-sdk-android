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

package com.shopify.sample.domain.interactor;

import android.support.annotation.NonNull;

import com.shopify.buy3.GraphHttpError;
import com.shopify.buy3.GraphNetworkError;
import com.shopify.buy3.Storefront;
import com.shopify.buy3.pay.PayAddress;
import com.shopify.buy3.pay.PayCart;
import com.shopify.buy3.pay.PaymentToken;
import com.shopify.sample.SampleApplication;
import com.shopify.sample.domain.model.Payment;
import com.shopify.sample.domain.model.UserMessageError;
import com.shopify.sample.domain.repository.CheckoutRepository;
import com.shopify.sample.domain.repository.UserError;
import com.shopify.sample.util.NotReadyException;
import com.shopify.sample.util.RxRetryHandler;

import java.util.concurrent.TimeUnit;

import io.reactivex.Single;

import static com.shopify.sample.util.Util.checkNotBlank;
import static com.shopify.sample.util.Util.checkNotNull;

public final class RealCheckoutCompleteInteractor implements CheckoutCompleteInteractor {
  private final CheckoutRepository repository;

  public RealCheckoutCompleteInteractor() {
    repository = new CheckoutRepository(SampleApplication.graphClient());
  }

  @Override public Single<Payment> execute(@NonNull final String checkoutId, @NonNull final PayCart payCart,
    @NonNull final PaymentToken paymentToken, @NonNull final String email, @NonNull final PayAddress billingAddress) {
    checkNotBlank(checkoutId, "checkoutId can't be empty");
    checkNotNull(payCart, "payCart == null");
    checkNotNull(paymentToken, "paymentToken == null");
    checkNotBlank(email, "email can't be empty");
    checkNotNull(billingAddress, "billingAddress == null");

    Storefront.MailingAddressInput mailingAddressInput = new Storefront.MailingAddressInput()
      .setAddress1(billingAddress.address1)
      .setAddress2(billingAddress.address2)
      .setCity(billingAddress.city)
      .setCountry(billingAddress.country)
      .setFirstName(billingAddress.firstName)
      .setLastName(billingAddress.lastName)
      .setPhone(billingAddress.phone)
      .setProvince(billingAddress.province)
      .setZip(billingAddress.zip);

    Storefront.TokenizedPaymentInput paymentInput = new Storefront.TokenizedPaymentInput(payCart.totalPrice, paymentToken.token,
      mailingAddressInput, "android_pay", paymentToken.token).setIdentifier(paymentToken.publicKeyHash);

    return repository
      .updateEmail(checkoutId, email, it -> it.checkout(new CheckoutFragment()))
      .flatMap(it -> repository
        .complete(checkoutId, paymentInput, q -> q.payment(new PaymentFragment())))
      .flatMap(it -> {
        if (it.getReady()) {
          return Single.just(it);
        } else {
          return repository
            .paymentById(it.getId().toString(), q -> q.onPayment(new PaymentFragment()))
            .flatMap(payment -> payment.getReady() ? Single.just(payment)
              : Single.error(new NotReadyException("Payment transaction is not finished")))
            .retryWhen(RxRetryHandler
              .exponentialBackoff(500, TimeUnit.MILLISECONDS, 1.2f)
              .maxRetries(10)
              .when(t -> t instanceof NotReadyException || t instanceof GraphHttpError || t instanceof GraphNetworkError)
              .build());
        }
      })
      .map(Converters::convertToPayment)
      .onErrorResumeNext(t -> Single.error((t instanceof UserError) ? new UserMessageError(t.getMessage()) : t));
  }
}
