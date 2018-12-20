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

import com.shopify.sample.SampleApplication;
import com.shopify.sample.domain.CheckoutShippingAddressUpdateQuery;
import com.shopify.sample.domain.model.Address;
import com.shopify.sample.domain.model.Checkout;
import com.shopify.sample.domain.model.UserMessageError;
import com.shopify.sample.domain.repository.CheckoutRepository;
import com.shopify.sample.domain.repository.UserError;
import com.shopify.sample.domain.type.MailingAddressInput;

import io.reactivex.Single;

import static com.shopify.sample.util.Util.checkNotBlank;
import static com.shopify.sample.util.Util.checkNotNull;

public final class RealCheckoutShippingAddressUpdateInteractor implements CheckoutShippingAddressUpdateInteractor {
  private final CheckoutRepository repository;

  public RealCheckoutShippingAddressUpdateInteractor() {
    this.repository = new CheckoutRepository(SampleApplication.apolloClient());
  }

  @Override public Single<Checkout> execute(@NonNull final String checkoutId, @NonNull final Address payAddress) {
    checkNotBlank(checkoutId, "checkoutId can't be empty");
    checkNotNull(payAddress, "payAddress == null");

    MailingAddressInput mailingAddressInput = MailingAddressInput.builder()
      .address1(payAddress.address1)
      .address2(payAddress.address2)
      .city(payAddress.city)
      .country(payAddress.country)
      .firstName(payAddress.firstName)
      .lastName(payAddress.lastName)
      .phone(payAddress.phone)
      .province(payAddress.province)
      .zip(payAddress.zip)
      .build();

    CheckoutShippingAddressUpdateQuery query = new CheckoutShippingAddressUpdateQuery(checkoutId, mailingAddressInput);

    return repository.updateShippingAddress(query)
      .map(Converters::convertToCheckout)
      .onErrorResumeNext(t -> Single.error( (t instanceof UserError) ? new UserMessageError(t.getMessage()) : t));
  }
}
