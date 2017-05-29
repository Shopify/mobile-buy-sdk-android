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
import com.shopify.sample.RxUtil;
import com.shopify.sample.domain.CheckoutByIdQuery;
import com.shopify.sample.domain.CheckoutCompleteWithAndroidPayQuery;
import com.shopify.sample.domain.CheckoutCreateQuery;
import com.shopify.sample.domain.CheckoutEmailUpdateQuery;
import com.shopify.sample.domain.CheckoutShippingAddressUpdateQuery;
import com.shopify.sample.domain.CheckoutShippingLineUpdateQuery;
import com.shopify.sample.domain.CheckoutShippingRatesQuery;
import com.shopify.sample.domain.PaymentByIdQuery;
import com.shopify.sample.domain.fragment.CheckoutCreateFragment;
import com.shopify.sample.domain.fragment.CheckoutFragment;
import com.shopify.sample.domain.fragment.CheckoutShippingRatesFragment;
import com.shopify.sample.domain.fragment.PaymentFragment;

import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

import static com.shopify.sample.RxUtil.rxApolloCall;
import static com.shopify.sample.util.Util.checkNotNull;
import static com.shopify.sample.util.Util.mapItems;

public final class CheckoutRepository {
  private final ApolloClient apolloClient;

  public CheckoutRepository(@NonNull final ApolloClient apolloClient) {
    this.apolloClient = checkNotNull(apolloClient, "apolloClient == null");
  }

  public Single<CheckoutCreateFragment> create(@NonNull final CheckoutCreateQuery query) {
    checkNotNull(query, "query == null");
    return rxApolloCall(apolloClient.newCall(query)
      .httpCacheControl(HttpCacheControl.NETWORK_ONLY))
      .map(Optional::get)
      .map(it -> it.checkoutCreate.get())
      .flatMap(it -> {
        if (it.userErrors.isEmpty()) {
          return Single.just(it);
        } else {
          return Single.error(new UserError(mapItems(it.userErrors, error -> error.message)));
        }
      })
      .map(it -> it.checkout.get())
      .map(it -> it.fragments)
      .map(it -> it.checkoutCreateFragment.get())
      .subscribeOn(Schedulers.io());
  }

  public Single<CheckoutFragment> updateShippingAddress(@NonNull final CheckoutShippingAddressUpdateQuery query) {
    checkNotNull(query, "query == null");
    return rxApolloCall(apolloClient.newCall(query)
      .httpCacheControl(HttpCacheControl.NETWORK_ONLY))
      .map(Optional::get)
      .map(it -> it.checkoutShippingAddressUpdate.get())
      .flatMap(it -> {
        if (it.userErrors.isEmpty()) {
          return Single.just(it);
        } else {
          return Single.error(new UserError(mapItems(it.userErrors, error -> error.message)));
        }
      })
      .map(it -> it.checkout)
      .map(it -> it.fragments)
      .map(it -> it.checkoutFragment.get())
      .subscribeOn(Schedulers.io());
  }

  public Single<CheckoutFragment> checkout(@NonNull final CheckoutByIdQuery query) {
    checkNotNull(query, "query == null");
    return rxApolloCall(apolloClient.newCall(query)
      .httpCacheControl(HttpCacheControl.NETWORK_ONLY))
      .map(Optional::get)
      .map(it -> it.node.get())
      .map(it -> it.fragments.checkoutFragment)
      .subscribeOn(Schedulers.io());
  }

  public Single<CheckoutShippingRatesFragment> shippingRates(@NonNull final CheckoutShippingRatesQuery query) {
    checkNotNull(query, "query == null");
    ApolloCall<Optional<CheckoutShippingRatesQuery.Data>> call = apolloClient.newCall(query)
      .httpCacheControl(HttpCacheControl.NETWORK_ONLY);
    return Single.fromCallable(call::clone)
      .flatMap(RxUtil::rxApolloCall)
      .map(Optional::get)
      .map(it -> it.node.get())
      .map(it -> it.asCheckout.get())
      .map(it -> it.availableShippingRates.get())
      .map(it -> it.fragments.checkoutShippingRatesFragment)
      .map(it -> it.or(new CheckoutShippingRatesFragment(false, null)))
      .subscribeOn(Schedulers.io());
  }

  public Single<CheckoutFragment> updateShippingLine(@NonNull final CheckoutShippingLineUpdateQuery query) {
    checkNotNull(query, "query == null");
    return rxApolloCall(apolloClient.newCall(query)
      .httpCacheControl(HttpCacheControl.NETWORK_ONLY))
      .map(Optional::get)
      .map(it -> it.checkoutShippingLineUpdate.get())
      .flatMap(it -> {
        if (it.userErrors.isEmpty()) {
          return Single.just(it);
        } else {
          return Single.error(new UserError(mapItems(it.userErrors, error -> error.message)));
        }
      })
      .map(it -> it.checkout.get())
      .map(it -> it.fragments.checkoutFragment.get())
      .subscribeOn(Schedulers.io());
  }

  public Single<CheckoutFragment> updateEmail(@NonNull final CheckoutEmailUpdateQuery query) {
    checkNotNull(query, "query == null");
    return rxApolloCall(apolloClient.newCall(query)
      .httpCacheControl(HttpCacheControl.NETWORK_ONLY))
      .map(Optional::get)
      .map(it -> it.checkoutEmailUpdate.get())
      .flatMap(it -> {
        if (it.userErrors.isEmpty()) {
          return Single.just(it);
        } else {
          return Single.error(new UserError(mapItems(it.userErrors, error -> error.message)));
        }
      })
      .map(it -> it.checkout)
      .map(it -> it.fragments)
      .map(it -> it.checkoutFragment.get())
      .subscribeOn(Schedulers.io());
  }

  public Single<PaymentFragment> complete(@NonNull final CheckoutCompleteWithAndroidPayQuery query) {
    checkNotNull(query, "query == null");
    return rxApolloCall(apolloClient.newCall(query)
      .httpCacheControl(HttpCacheControl.NETWORK_ONLY))
      .map(Optional::get)
      .map(it -> it.checkoutCompleteWithTokenizedPayment.get())
      .flatMap(it -> {
        if (it.userErrors.isEmpty()) {
          return Single.just(it);
        } else {
          return Single.error(new UserError(mapItems(it.userErrors, error -> error.message)));
        }
      })
      .map(it -> it.payment.get())
      .map(it -> it.fragments)
      .map(it -> it.paymentFragment.get())
      .subscribeOn(Schedulers.io());
  }

  public Single<PaymentFragment> paymentById(@NonNull final PaymentByIdQuery query) {
    checkNotNull(query, "query == null");
    ApolloCall<Optional<PaymentByIdQuery.Data>> call = apolloClient.newCall(query)
      .httpCacheControl(HttpCacheControl.NETWORK_ONLY);
    return Single.fromCallable(call::clone)
      .flatMap(RxUtil::rxApolloCall)
      .map(Optional::get)
      .map(it -> it.node.get())
      .map(it -> it.fragments)
      .map(it -> it.paymentFragment)
      .subscribeOn(Schedulers.io());
  }
}
