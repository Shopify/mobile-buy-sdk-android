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

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.ApolloClient;
import com.apollographql.apollo.api.internal.Optional;
import com.apollographql.apollo.cache.http.HttpCacheControl;
import com.shopify.buy3.pay.PayAddress;
import com.shopify.buy3.pay.PayCart;
import com.shopify.buy3.pay.PaymentToken;
import com.shopify.sample.RxUtil;
import com.shopify.sample.SampleApplication;
import com.shopify.sample.domain.CheckoutShippingLineUpdateQuery;
import com.shopify.sample.domain.CheckoutUpdateEmailQuery;
import com.shopify.sample.domain.CompleteCheckoutQuery;
import com.shopify.sample.domain.CreateCheckoutQuery;
import com.shopify.sample.domain.FetchCheckoutQuery;
import com.shopify.sample.domain.FetchCheckoutShippingRatesQuery;
import com.shopify.sample.domain.UpdateCheckoutShippingAddressQuery;
import com.shopify.sample.domain.fragment.CheckoutFragment;
import com.shopify.sample.domain.fragment.CheckoutShippingRatesFragment;
import com.shopify.sample.domain.fragment.PaymentFragment;
import com.shopify.sample.domain.model.Checkout;
import com.shopify.sample.domain.model.Payment;
import com.shopify.sample.domain.type.CheckoutCompleteWithTokenizedPaymentInput;
import com.shopify.sample.domain.type.CheckoutCreateInput;
import com.shopify.sample.domain.type.CheckoutEmailUpdateInput;
import com.shopify.sample.domain.type.CheckoutShippingAddressUpdateInput;
import com.shopify.sample.domain.type.CheckoutShippingLineUpdateInput;
import com.shopify.sample.domain.type.LineItemInput;
import com.shopify.sample.domain.type.MailingAddressInput;
import com.shopify.sample.util.NotReadyException;
import com.shopify.sample.util.RxRetryHandler;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

import static com.shopify.sample.RxUtil.queryResponseDataTransformer;
import static com.shopify.sample.RxUtil.rxApolloCall;
import static com.shopify.sample.util.Util.checkNotBlank;
import static com.shopify.sample.util.Util.checkNotEmpty;
import static com.shopify.sample.util.Util.checkNotNull;
import static com.shopify.sample.util.Util.fold;
import static com.shopify.sample.util.Util.mapItems;
import static java.util.Collections.emptyList;

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
        .flatMap(it -> it.checkoutCreate)
        .flatMap(it -> it.checkout)
        .flatMap(it -> it.fragments.checkoutFragment)
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
        .flatMap(it -> it.checkoutShippingAddressUpdate)
        .flatMap(it -> it.checkout.fragments.checkoutFragment)
        .get())
      .map(RealCheckoutRepository::map)
      .subscribeOn(Schedulers.io());
  }

  @Override public Single<Checkout> fetch(@NonNull final String checkoutId) {
    checkNotBlank(checkoutId, "checkoutId can't be empty");
    FetchCheckoutQuery query = new FetchCheckoutQuery(checkoutId);
    return rxApolloCall(apolloClient.newCall(query).httpCacheControl(HttpCacheControl.NETWORK_ONLY))
      .map(response -> response.data()
        .flatMap(it -> it.node)
        .map(it -> it.fragments.checkoutFragment)
        .get())
      .map(RealCheckoutRepository::map)
      .subscribeOn(Schedulers.io());
  }

  @Override public Single<Checkout.ShippingRates> fetchShippingRates(@NonNull final String checkoutId) {
    checkNotBlank(checkoutId, "checkoutId can't be empty");
    FetchCheckoutShippingRatesQuery query = new FetchCheckoutShippingRatesQuery(checkoutId);
    ApolloCall<Optional<FetchCheckoutShippingRatesQuery.Data>> call = apolloClient.newCall(query)
      .httpCacheControl(HttpCacheControl.NETWORK_ONLY);
    return Single.fromCallable(call::clone)
      .flatMap(RxUtil::rxApolloCall)
      .map(response -> response.data()
        .flatMap(it -> it.node)
        .flatMap(it -> it.asCheckout)
        .flatMap(it -> it.availableShippingRates)
        .map(it -> it.fragments.checkoutShippingRatesFragment)
        .get())
      .map(RealCheckoutRepository::map)
      .flatMap(shippingRates ->
        shippingRates.ready ? Single.just(shippingRates) : Single.error(new NotReadyException("Shipping rates not available yet")))
      .retryWhen(RxRetryHandler.exponentialBackoff(500, TimeUnit.MILLISECONDS, 1.2f)
        .maxRetries(10)
        .when(t -> t instanceof NotReadyException)
        .build())
      .subscribeOn(Schedulers.io());
  }

  @Override public Single<Checkout> applyShippingRate(@NonNull final String checkoutId, @NonNull final String shippingRateHandle) {
    CheckoutShippingLineUpdateInput input = CheckoutShippingLineUpdateInput.builder()
      .checkoutId(checkNotBlank(checkoutId, "checkoutId can't be empty"))
      .shippingRateHandle(checkNotBlank(shippingRateHandle, "shippingRateHandle can't be empty"))
      .build();

    CheckoutShippingLineUpdateQuery query = new CheckoutShippingLineUpdateQuery(input);
    return rxApolloCall(apolloClient.newCall(query).httpCacheControl(HttpCacheControl.NETWORK_ONLY))
      .map(response -> response.data()
        .flatMap(it -> it.checkoutShippingLineUpdate)
        .flatMap(it -> it.checkout)
        .flatMap(it -> it.fragments.checkoutFragment)
        .get())
      .map(RealCheckoutRepository::map)
      .subscribeOn(Schedulers.io());
  }

  @Override public Single<Checkout> updateEmail(@NonNull final String checkoutId, @NonNull final String email) {
    checkNotBlank(checkoutId, "checkoutId can't be empty");
    checkNotBlank(email, "email can't be empty");

    CheckoutEmailUpdateInput input = CheckoutEmailUpdateInput.builder()
      .checkoutId(checkoutId)
      .email(email)
      .build();

    CheckoutUpdateEmailQuery query = new CheckoutUpdateEmailQuery(input);
    return rxApolloCall(apolloClient.newCall(query).httpCacheControl(HttpCacheControl.NETWORK_ONLY))
      .compose(queryResponseDataTransformer())
      .map(data -> data
        .flatMap(it -> it.checkoutEmailUpdate)
        .map(it -> it.checkout)
        .map(it -> it.fragments)
        .flatMap(it -> it.checkoutFragment)
        .get())
      .map(RealCheckoutRepository::map)
      .subscribeOn(Schedulers.io());
  }

  @Override public Single<Payment> completeCheckout(@NonNull final String checkoutId, @NonNull final PayCart payCart,
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

    CheckoutCompleteWithTokenizedPaymentInput input = CheckoutCompleteWithTokenizedPaymentInput.builder()
      .checkoutId(checkoutId)
      .amount(payCart.totalPrice)
      .idempotencyKey(paymentToken.token)
      .type("android_pay")
      .paymentData(paymentToken.token)
      .identifier(paymentToken.publicKeyHash)
      .billingAddress(mailingAddressInput)
      .build();

    CompleteCheckoutQuery query = new CompleteCheckoutQuery(input);

    return updateEmail(checkoutId, email)
      .flatMap(it -> rxApolloCall(apolloClient.newCall(query).httpCacheControl(HttpCacheControl.NETWORK_ONLY)))
      .compose(queryResponseDataTransformer())
      .map(data -> data.flatMap(it -> it.checkoutCompleteWithTokenizedPayment).get())
      .flatMap(it -> {
        if (it.userErrors.isEmpty()) {
          return Single.just(it);
        } else {
          String errorMessage = fold(new StringBuilder(), it.userErrors,
            (builder, error) -> builder.append(error.field).append(" : ").append(error.message).append("\n")).toString();
          return Single.error(new RuntimeException(errorMessage));
        }
      })
      .map(data -> data.payment
        .map(it -> it.fragments)
        .flatMap(it -> it.paymentFragment)
        .get())
      .map(RealCheckoutRepository::map)
      .subscribeOn(Schedulers.io());
  }

  private static Checkout map(final CheckoutFragment checkout) {
    List<Checkout.LineItem> lineItems = mapItems(checkout.lineItemConnection.lineItemEdges, it ->
      new Checkout.LineItem(it.lineItem.variant.get().id, it.lineItem.title, it.lineItem.quantity, it.lineItem.variant.get().price));
    Checkout.ShippingRate shippingLine = checkout.shippingLine.transform(it -> new Checkout.ShippingRate(it.handle, it.price, it.title))
      .orNull();
    return new Checkout(checkout.id, checkout.webUrl, checkout.currencyCode.toString(), checkout.requiresShipping, lineItems,
      map(checkout.availableShippingRates.transform(it -> it.fragments.checkoutShippingRatesFragment.orNull())), shippingLine,
      checkout.totalTax, checkout.subtotalPrice, checkout.totalPrice);
  }

  private static Checkout.ShippingRates map(final Optional<CheckoutShippingRatesFragment> availableShippingRates) {
    List<Checkout.ShippingRate> shippingRates = mapItems(availableShippingRates.transform(it -> it.shippingRates.or(emptyList()))
      .or(emptyList()), it -> new Checkout.ShippingRate(it.handle, it.price, it.title));
    return new Checkout.ShippingRates(availableShippingRates.transform(it -> it.ready).or(false), shippingRates);
  }

  private static Payment map(final PaymentFragment payment) {
    return new Payment(payment.id, payment.errorMessage.orNull(), payment.ready,
      payment.transaction.transform(it -> new Payment.Transaction(it.status.name())).orNull());
  }
}
