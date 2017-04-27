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
import com.shopify.sample.domain.CheckoutCreateQuery;
import com.shopify.sample.domain.model.Checkout;
import com.shopify.sample.domain.repository.CheckoutRepository;
import com.shopify.sample.domain.type.CheckoutCreateInput;
import com.shopify.sample.domain.type.CheckoutLineItemInput;

import java.util.List;

import io.reactivex.Single;

import static com.shopify.sample.util.Util.checkNotEmpty;
import static com.shopify.sample.util.Util.mapItems;

public final class RealCheckoutCreateInteractor implements CheckoutCreateInteractor {
  private final CheckoutRepository repository;

  public RealCheckoutCreateInteractor() {
    this.repository = new CheckoutRepository(SampleApplication.apolloClient());
  }

  @Override public Single<Checkout> execute(@NonNull final List<Checkout.LineItem> lineItems) {
    checkNotEmpty(lineItems, "lineItems can't be empty");

    List<CheckoutLineItemInput> lineItemInputs = mapItems(lineItems, lineItem ->
      CheckoutLineItemInput.builder()
        .variantId(lineItem.variantId)
        .quantity(lineItem.quantity)
        .build());
    CheckoutCreateInput input = CheckoutCreateInput.builder().lineItems(lineItemInputs).build();
    CheckoutCreateQuery query = CheckoutCreateQuery.builder().input(input).build();
    return repository.create(query).map(Converters::convertToCheckout);
  }
}
