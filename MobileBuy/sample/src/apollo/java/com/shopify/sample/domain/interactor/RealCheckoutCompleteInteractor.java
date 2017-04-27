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

import com.shopify.buy3.pay.PayAddress;
import com.shopify.buy3.pay.PayCart;
import com.shopify.buy3.pay.PaymentToken;
import com.shopify.sample.SampleApplication;
import com.shopify.sample.domain.CheckoutCompleteWithAndroidPayQuery;
import com.shopify.sample.domain.model.Payment;
import com.shopify.sample.domain.repository.CheckoutRepository;
import com.shopify.sample.domain.type.MailingAddressInput;
import com.shopify.sample.domain.type.TokenizedPaymentInput;

import io.reactivex.Single;

import static com.shopify.sample.util.Util.checkNotBlank;
import static com.shopify.sample.util.Util.checkNotNull;

public final class RealCheckoutCompleteInteractor implements CheckoutCompleteInteractor {
  private final CheckoutRepository repository;

  public RealCheckoutCompleteInteractor() {
    this.repository = new CheckoutRepository(SampleApplication.apolloClient());
  }

  @Override public Single<Payment> execute(@NonNull final String checkoutId, @NonNull final PayCart payCart,
    @NonNull final PaymentToken paymentToken, @NonNull final String email, @NonNull final PayAddress billingAddress) {
    checkNotBlank(checkoutId, "checkoutId can't be empty");
    checkNotNull(payCart, "payCart == null");
    checkNotNull(paymentToken, "paymentToken == null");
    checkNotBlank(email, "email can't be empty");
    checkNotNull(billingAddress, "billingAddress == null");

    MailingAddressInput mailingAddressInput = MailingAddressInput.builder()
      .address1(billingAddress.address1)
      .address2(billingAddress.address2)
      .city(billingAddress.city)
      .country(billingAddress.country)
      .firstName(billingAddress.firstName)
      .lastName(billingAddress.lastName)
      .phone(billingAddress.phone)
      .province(billingAddress.province)
      .zip(billingAddress.zip)
      .build();

    TokenizedPaymentInput paymentInput = TokenizedPaymentInput.builder()
      .amount(payCart.totalPrice)
      .idempotencyKey(paymentToken.token)
      .type("android_pay")
      .paymentData(paymentToken.token)
      .identifier(paymentToken.publicKeyHash)
      .billingAddress(mailingAddressInput)
      .build();

    CheckoutCompleteWithAndroidPayQuery query = new CheckoutCompleteWithAndroidPayQuery(checkoutId, paymentInput);
    return repository.complete(query).map(Converters::convertToPayment);
  }
}
