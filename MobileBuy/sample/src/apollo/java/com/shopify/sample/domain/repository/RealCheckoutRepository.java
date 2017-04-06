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
import com.apollographql.apollo.api.internal.Optional;
import com.apollographql.apollo.cache.http.HttpCacheControl;
import com.shopify.buy3.pay.PayAddress;
import com.shopify.sample.SampleApplication;
import com.shopify.sample.domain.CreateCheckoutQuery;
import com.shopify.sample.domain.FetchCheckoutQuery;
import com.shopify.sample.domain.FetchCheckoutShippingRatesQuery;
import com.shopify.sample.domain.UpdateCheckoutShippingAddressQuery;
import com.shopify.sample.domain.fragment.CheckoutFragment;
import com.shopify.sample.domain.fragment.CheckoutShippingRatesFragment;
import com.shopify.sample.domain.model.Checkout;
import com.shopify.sample.domain.type.CheckoutCreateInput;
import com.shopify.sample.domain.type.CheckoutShippingAddressUpdateInput;
import com.shopify.sample.domain.type.LineItemInput;
import com.shopify.sample.domain.type.MailingAddressInput;
import com.shopify.sample.util.NotReadyException;
import com.shopify.sample.util.RxRetryHandler;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

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
        .transform(it -> it.checkoutCreate.get())
        .transform(it -> it.checkout.get())
        .transform(it -> it.fragments.checkoutFragment.get())
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
        .transform(it -> it.checkoutShippingAddressUpdate.get())
        .transform(it -> it.checkout.fragments.checkoutFragment.get())
        .get())
      .map(RealCheckoutRepository::map)
      .subscribeOn(Schedulers.io());
  }

  @Override public Single<Checkout> fetch(@NonNull final String checkoutId) {
    checkNotBlank(checkoutId, "checkoutId can't be empty");
    FetchCheckoutQuery query = new FetchCheckoutQuery(checkoutId);
    return rxApolloCall(apolloClient.newCall(query).httpCacheControl(HttpCacheControl.NETWORK_ONLY))
      .map(response -> response.data()
        .transform(it -> it.node.get())
        .transform(it -> it.fragments.checkoutFragment.get())
        .get())
      .map(RealCheckoutRepository::map)
      .subscribeOn(Schedulers.io());
  }

  @Override public Single<Checkout.ShippingRates> fetchShippingRates(@NonNull final String checkoutId) {
    checkNotBlank(checkoutId, "checkoutId can't be empty");
    FetchCheckoutShippingRatesQuery query = new FetchCheckoutShippingRatesQuery(checkoutId);
    return rxApolloCall(apolloClient.newCall(query).httpCacheControl(HttpCacheControl.NETWORK_ONLY))
      .map(response -> response.data()
        .transform(it -> it.node.get())
        .transform(it -> it.asCheckout.get())
        .transform(it -> it.availableShippingRates.get())
        .transform(it -> it.fragments.checkoutShippingRatesFragment)
        .get())
      .map(RealCheckoutRepository::map)
      .doAfterSuccess(shippingRates -> {
        if (!shippingRates.ready) throw new NotReadyException("Shipping rates not available yet");
      })
      .subscribeOn(Schedulers.io())
      .retryWhen(RxRetryHandler.exponentialBackoff(1, TimeUnit.SECONDS, 1.5f)
        .maxRetries(5)
        .when(t -> t instanceof NotReadyException)
        .build());
  }

  private static Checkout map(final CheckoutFragment checkout) {
    List<Checkout.LineItem> lineItems = mapItems(checkout.lineItemConnection.lineItemEdges, it ->
      new Checkout.LineItem(it.lineItem.variant.get().id, it.lineItem.title, it.lineItem.quantity, it.lineItem.variant.get().price));
    return new Checkout(checkout.id, checkout.webUrl, checkout.currencyCode.toString(), checkout.requiresShipping, lineItems,
      map(checkout.availableShippingRates.transform(it -> it.fragments.checkoutShippingRatesFragment.get())));
  }

  private static Checkout.ShippingRates map(final Optional<CheckoutShippingRatesFragment> availableShippingRates) {
    List<Checkout.ShippingRate> shippingRates = mapItems(availableShippingRates.transform(it -> it.shippingRates.get())
      .or(Collections.emptyList()), it -> new Checkout.ShippingRate(it.handle, it.price, it.title));
    return new Checkout.ShippingRates(availableShippingRates.transform(it -> it.ready).or(false), shippingRates);
  }
}
