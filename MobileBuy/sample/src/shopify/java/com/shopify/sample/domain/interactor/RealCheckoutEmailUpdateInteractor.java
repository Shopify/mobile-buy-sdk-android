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

import com.shopify.buy3.Storefront;
import com.shopify.graphql.support.ID;
import com.shopify.sample.SampleApplication;
import com.shopify.sample.domain.model.Checkout;
import com.shopify.sample.domain.repository.CheckoutRepository;

import io.reactivex.Single;

import static com.shopify.sample.util.Util.checkNotBlank;

public final class RealCheckoutEmailUpdateInteractor implements CheckoutEmailUpdateInteractor {
  private final CheckoutRepository repository;

  public RealCheckoutEmailUpdateInteractor() {
    repository = new CheckoutRepository(SampleApplication.graphClient());
  }

  @Override public Single<Checkout> execute(@NonNull final String checkoutId, @NonNull final String email) {
    checkNotBlank(checkoutId, "checkoutId can't be empty");
    checkNotBlank(email, "email can't be empty");

    Storefront.CheckoutEmailUpdatePayloadQueryDefinition query = it -> it.checkout(new CheckoutFragment());
    return repository.updateEmail(checkoutId, email, query).map(Converters::convertToCheckout);
  }
}
