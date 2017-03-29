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

package com.shopify.sample.interactor.cart;

import android.support.annotation.NonNull;

import com.apollographql.android.cache.http.HttpCacheControl;
import com.apollographql.android.impl.ApolloClient;
import com.shopify.sample.SampleApplication;
import com.shopify.sample.domain.CreateCheckoutQuery;
import com.shopify.sample.domain.type.CheckoutCreateInput;
import com.shopify.sample.domain.type.LineItemInput;
import com.shopify.sample.presenter.cart.Checkout;

import java.util.List;

import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

import static com.shopify.sample.RxUtil.rxApolloCall;
import static com.shopify.sample.util.Util.checkNotEmpty;
import static com.shopify.sample.util.Util.mapItems;

public final class RealCreateCheckout implements CreateCheckout {
  private final ApolloClient apolloClient = SampleApplication.apolloClient();

  @SuppressWarnings("ConstantConditions") @Override public Single<Checkout> call(@NonNull final List<LineItem> lineItems) {
    checkNotEmpty(lineItems, "lineItems can't be empty");
    List<LineItemInput> lineItemInputs = mapItems(lineItems, lineItem ->
      LineItemInput.builder()
        .variantId(lineItem.variantId)
        .quantity(lineItem.quantity)
        .build());
    CheckoutCreateInput input = CheckoutCreateInput.builder().lineItems(lineItemInputs).build();
    CreateCheckoutQuery query = CreateCheckoutQuery.builder().input(input).build();

    return rxApolloCall(apolloClient.newCall(query).httpCacheControl(HttpCacheControl.NETWORK_ONLY))
      .map(response -> response.data()
        .transform(it -> it.checkoutCreate().orNull())
        .transform(it -> it.checkout().orNull())
        .get())
      .map(it -> new Checkout(it.id(), it.webUrl(), it.requiresShipping()))
      .subscribeOn(Schedulers.io());
  }
}
