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

package com.shopify.sample.domain.repository;

import android.support.annotation.NonNull;

import com.apollographql.apollo.ApolloClient;
import com.apollographql.apollo.cache.http.HttpCacheControl;
import com.shopify.buy3.pay.PayAddress;
import com.shopify.sample.SampleApplication;
import com.shopify.sample.domain.CreateCheckoutQuery;
import com.shopify.sample.domain.UpdateCheckoutShippingAddressQuery;
import com.shopify.sample.domain.model.Checkout;
import com.shopify.sample.domain.type.CheckoutCreateInput;
import com.shopify.sample.domain.type.CheckoutShippingAddressUpdateInput;
import com.shopify.sample.domain.type.LineItemInput;
import com.shopify.sample.domain.type.MailingAddressInput;

import java.util.List;

import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

import static com.shopify.sample.RxUtil.rxApolloCall;
import static com.shopify.sample.util.Util.checkNotBlank;
import static com.shopify.sample.util.Util.checkNotEmpty;
import static com.shopify.sample.util.Util.checkNotNull;
import static com.shopify.sample.util.Util.mapItems;

public final class RealCheckoutRepository implements CheckoutRepository {
  private final ApolloClient apolloClient = SampleApplication.apolloClient();

  @Override public Single<Checkout> create(@NonNull final List<Checkout.LineItem> lineItems) {
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
        .transform(it -> it.checkoutCreate.orNull())
        .transform(it -> it.checkout.orNull())
        .get())
      .map(RealCheckoutRepository::map)
      .subscribeOn(Schedulers.io());
  }

  @Override public Single<Checkout> updateShippingAddress(@NonNull final String checkoutId, @NonNull final PayAddress payAddress) {
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

    CheckoutShippingAddressUpdateInput input = CheckoutShippingAddressUpdateInput
      .builder()
      .checkoutId(checkoutId)
      .shippingAddress(mailingAddressInput)
      .build();

    UpdateCheckoutShippingAddressQuery query = UpdateCheckoutShippingAddressQuery.builder().input(input).build();
    return rxApolloCall(apolloClient.newCall(query).httpCacheControl(HttpCacheControl.NETWORK_ONLY))
      .map(response -> response.data()
        .transform(it -> it.checkoutShippingAddressUpdate.orNull())
        .transform(it -> it.checkout)
        .get())
      .map(RealCheckoutRepository::map)
      .subscribeOn(Schedulers.io());
  }

  private static Checkout map(final CreateCheckoutQuery.Data.CheckoutCreate.Checkout checkout) {
    return new Checkout(checkout.id, checkout.webUrl, checkout.currencyCode.toString(),
      checkout.requiresShipping, mapItems(checkout.lineItemConnection.lineItemEdges, lineItemEdge ->
      new Checkout.LineItem(lineItemEdge.lineItem.variant.get().id, lineItemEdge.lineItem.title,
        lineItemEdge.lineItem.quantity, lineItemEdge.lineItem.variant.get().price)));
  }

  private static Checkout map(final UpdateCheckoutShippingAddressQuery.Data.CheckoutShippingAddressUpdate.Checkout checkout) {
    return new Checkout(checkout.id, checkout.webUrl, checkout.currencyCode.toString(),
      checkout.requiresShipping, mapItems(checkout.lineItemConnection.lineItemEdges, lineItemEdge ->
      new Checkout.LineItem(lineItemEdge.lineItem.variant.get().id, lineItemEdge.lineItem.title,
        lineItemEdge.lineItem.quantity, lineItemEdge.lineItem.variant.get().price)));
  }
}
