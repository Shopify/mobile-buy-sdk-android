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

import com.shopify.buy3.GraphCall;
import com.shopify.buy3.GraphClient;
import com.shopify.buy3.GraphResponse;
import com.shopify.buy3.HttpCachePolicy;
import com.shopify.buy3.Storefront;
import com.shopify.graphql.support.ID;
import com.shopify.sample.RxUtil;

import java.util.concurrent.TimeUnit;

import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

import static com.shopify.sample.RxUtil.rxGraphMutationCall;
import static com.shopify.sample.RxUtil.rxGraphQueryCall;
import static com.shopify.sample.util.Util.checkNotBlank;
import static com.shopify.sample.util.Util.checkNotNull;

public final class CheckoutRepository {
  private final GraphClient graphClient;

  public CheckoutRepository(@NonNull final GraphClient graphClient) {
    this.graphClient = checkNotNull(graphClient, "graphClient == null");
  }

  public Single<Storefront.Checkout> create(@NonNull final Storefront.CheckoutCreateInput input,
    @NonNull final Storefront.CheckoutCreatePayloadQueryDefinition query) {
    checkNotNull(input, "input == null");
    checkNotNull(query, "query == null");
    GraphCall<Storefront.Mutation> call = graphClient.mutateGraph(Storefront.mutation(
      root -> root.checkoutCreate(input, query)
    ));
    return rxGraphMutationCall(call)
      .map(GraphResponse::data)
      .map(Storefront.Mutation::getCheckoutCreate)
      .map(Storefront.CheckoutCreatePayload::getCheckout)
      .subscribeOn(Schedulers.io());
  }

  public Single<Storefront.Checkout> updateShippingAddress(@NonNull final String checkoutId,
    @NonNull final Storefront.MailingAddressInput input,
    @NonNull final Storefront.CheckoutShippingAddressUpdatePayloadQueryDefinition query) {
    checkNotBlank(checkoutId, "checkoutId can't be empty");
    checkNotNull(input, "input == null");
    checkNotNull(query, "query == null");
    GraphCall<Storefront.Mutation> call = graphClient.mutateGraph(Storefront.mutation(
      root -> root.checkoutShippingAddressUpdate(input, new ID(checkoutId), query)
    ));
    return rxGraphMutationCall(call)
      .map(GraphResponse::data)
      .map(Storefront.Mutation::getCheckoutShippingAddressUpdate)
      .map(Storefront.CheckoutShippingAddressUpdatePayload::getCheckout)
      .subscribeOn(Schedulers.io());
  }

  public Single<Storefront.Checkout> checkout(@NonNull final String checkoutId, @NonNull final Storefront.NodeQueryDefinition nodeQuery) {
    checkNotBlank(checkoutId, "checkoutId can't be empty");
    checkNotNull(nodeQuery, "query == null");
    GraphCall<Storefront.QueryRoot> call = graphClient
      .queryGraph(Storefront.query(
        root -> root.node(new ID(checkoutId), nodeQuery)
      ))
      .cachePolicy(HttpCachePolicy.NETWORK_FIRST.expireAfter(5, TimeUnit.MINUTES));
    return rxGraphQueryCall(call)
      .map(GraphResponse::data)
      .map(it -> (Storefront.Checkout) it.getNode())
      .subscribeOn(Schedulers.io());
  }

  public Single<Storefront.AvailableShippingRates> shippingRates(@NonNull final String checkoutId,
    @NonNull final Storefront.CheckoutQueryDefinition query) {
    checkNotBlank(checkoutId, "checkoutId can't be empty");
    checkNotNull(query, "query == null");
    GraphCall<Storefront.QueryRoot> call = graphClient
      .queryGraph(Storefront.query(
        root -> root.node(new ID(checkoutId), q -> q.onCheckout(query))
      ))
      .cachePolicy(HttpCachePolicy.NETWORK_ONLY);
    return Single.fromCallable(call::clone)
      .flatMap(RxUtil::rxGraphQueryCall)
      .map(GraphResponse::data)
      .map(it -> (Storefront.Checkout) it.getNode())
      .map(Storefront.Checkout::getAvailableShippingRates)
      .subscribeOn(Schedulers.io());
  }

  public Single<Storefront.Checkout> updateShippingLine(@NonNull final String checkoutId, @NonNull final String shippingRateHandle,
    @NonNull final Storefront.CheckoutShippingLineUpdatePayloadQueryDefinition query) {
    checkNotNull(query, "query == null");
    GraphCall<Storefront.Mutation> call = graphClient.mutateGraph(Storefront.mutation(
      root -> root.checkoutShippingLineUpdate(new ID(checkoutId), shippingRateHandle, query)
    ));
    return rxGraphMutationCall(call)
      .map(GraphResponse::data)
      .map(Storefront.Mutation::getCheckoutShippingLineUpdate)
      .map(Storefront.CheckoutShippingLineUpdatePayload::getCheckout)
      .subscribeOn(Schedulers.io());
  }

  public Single<Storefront.Checkout> updateEmail(@NonNull final String checkoutId, @NonNull final String email,
    @NonNull final Storefront.CheckoutEmailUpdatePayloadQueryDefinition query) {
    checkNotBlank(checkoutId, "checkoutId can't be empty");
    checkNotBlank(email, "email can't be empty");
    checkNotNull(query, "query == null");
    GraphCall<Storefront.Mutation> call = graphClient.mutateGraph(Storefront.mutation(
      root -> root.checkoutEmailUpdate(new ID(checkoutId), email, query)
    ));
    return rxGraphMutationCall(call)
      .map(GraphResponse::data)
      .map(Storefront.Mutation::getCheckoutEmailUpdate)
      .map(Storefront.CheckoutEmailUpdatePayload::getCheckout)
      .subscribeOn(Schedulers.io());
  }

  public Single<Storefront.Payment> complete(@NonNull final String checkoutId,
    @NonNull final Storefront.TokenizedPaymentInput paymentInput,
    @NonNull final Storefront.CheckoutCompleteWithTokenizedPaymentPayloadQueryDefinition query) {
    checkNotBlank(checkoutId, "checkoutId can't be empty");
    checkNotNull(paymentInput, "paymentInput == null");
    checkNotNull(query, "query == null");
    GraphCall<Storefront.Mutation> call = graphClient.mutateGraph(Storefront.mutation(
      root -> root.checkoutCompleteWithTokenizedPayment(new ID(checkoutId), paymentInput, query)
    ));
    return rxGraphMutationCall(call)
      .map(GraphResponse::data)
      .map(Storefront.Mutation::getCheckoutCompleteWithTokenizedPayment)
      .map(Storefront.CheckoutCompleteWithTokenizedPaymentPayload::getPayment)
      .subscribeOn(Schedulers.io());
  }
}
