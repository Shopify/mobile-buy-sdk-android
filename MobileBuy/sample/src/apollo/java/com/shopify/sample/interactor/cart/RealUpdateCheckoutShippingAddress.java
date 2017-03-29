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
import com.shopify.buy3.pay.PayAddress;
import com.shopify.sample.SampleApplication;
import com.shopify.sample.domain.UpdateCheckoutShippingAddressQuery;
import com.shopify.sample.domain.type.CheckoutShippingAddressUpdateInput;
import com.shopify.sample.domain.type.MailingAddressInput;
import com.shopify.sample.presenter.cart.Checkout;

import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

import static com.shopify.sample.RxUtil.rxApolloCall;
import static com.shopify.sample.util.Util.checkNotBlank;
import static com.shopify.sample.util.Util.checkNotNull;
import static com.shopify.sample.util.Util.mapItems;

public final class RealUpdateCheckoutShippingAddress implements UpdateCheckoutShippingAddress {
  private final ApolloClient apolloClient = SampleApplication.apolloClient();

  @Override public Single<Checkout> call(@NonNull final String checkoutId, @NonNull final PayAddress address) {
    checkNotBlank(checkoutId, "checkoutId can't be empty");
    checkNotNull(address, "address == null");

    MailingAddressInput mailingAddressInput = MailingAddressInput.builder()
      .address1(address.address1)
      .address2(address.address2)
      .city(address.city)
      .country(address.country)
      .firstName(address.firstName)
      .lastName(address.lastName)
      .phone(address.phone)
      .province(address.province)
      .zip(address.zip)
      .build();

    CheckoutShippingAddressUpdateInput input = CheckoutShippingAddressUpdateInput
      .builder()
      .checkoutId(checkoutId)
      .shippingAddress(mailingAddressInput)
      .build();

    UpdateCheckoutShippingAddressQuery query = UpdateCheckoutShippingAddressQuery.builder().input(input).build();
    return rxApolloCall(apolloClient.newCall(query).httpCacheControl(HttpCacheControl.NETWORK_ONLY))
      .map(response -> response.data()
        .transform(it -> it.checkoutShippingAddressUpdate().orNull())
        .transform(it -> it.checkout())
        .get())
      .map(RealUpdateCheckoutShippingAddress::map)
      .subscribeOn(Schedulers.io());
  }

  private static Checkout map(final UpdateCheckoutShippingAddressQuery.Data.CheckoutShippingAddressUpdate.Checkout checkout) {
    return new Checkout(checkout.id(), checkout.webUrl(), checkout.currencyCode().toString(),
      checkout.requiresShipping(), mapItems(checkout.lineItemConnection().lineItemEdges(), lineItemEdge ->
      new Checkout.LineItem(lineItemEdge.lineItem().variant().get().id(), lineItemEdge.lineItem().title(),
        lineItemEdge.lineItem().quantity(), lineItemEdge.lineItem().variant().get().price())));
  }
}
