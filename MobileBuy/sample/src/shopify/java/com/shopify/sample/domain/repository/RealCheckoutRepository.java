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
import com.shopify.buy3.Storefront;
import com.shopify.buy3.pay.PayAddress;
import com.shopify.buy3.pay.PayCart;
import com.shopify.buy3.pay.PaymentToken;
import com.shopify.graphql.support.ID;
import com.shopify.sample.RxUtil;
import com.shopify.sample.SampleApplication;
import com.shopify.sample.domain.model.Checkout;
import com.shopify.sample.domain.model.Payment;
import com.shopify.sample.util.NotReadyException;
import com.shopify.sample.util.RxRetryHandler;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

import static com.shopify.sample.RxUtil.rxGraphMutationCall;
import static com.shopify.sample.RxUtil.rxGraphQueryCall;
import static com.shopify.sample.util.Util.checkNotBlank;
import static com.shopify.sample.util.Util.checkNotEmpty;
import static com.shopify.sample.util.Util.checkNotNull;
import static com.shopify.sample.util.Util.fold;
import static com.shopify.sample.util.Util.mapItems;
import static java.util.Collections.emptyList;

public final class RealCheckoutRepository implements CheckoutRepository {
  private final GraphClient graphClient = SampleApplication.graphClient();

  @Override public Single<Checkout> create(@NonNull final List<Checkout.LineItem> lineItems) {
    checkNotEmpty(lineItems, "lineItems can't be empty");

    List<Storefront.LineItemInput> storefrontLineItems = mapItems(lineItems, lineItem ->
      new Storefront.LineItemInput(lineItem.quantity, new ID(lineItem.variantId)));
    Storefront.CheckoutCreateInput input = new Storefront.CheckoutCreateInput().setLineItems(storefrontLineItems);

    GraphCall<Storefront.Mutation> call = graphClient.mutateGraph(
      Storefront.mutation(
        root -> root.checkoutCreate(input, query -> query.checkout(new CheckoutFragment()))
      )
    );
    return rxGraphMutationCall(call)
      .map(GraphResponse::data)
      .map(Storefront.Mutation::getCheckoutCreate)
      .map(Storefront.CheckoutCreatePayload::getCheckout)
      .map(RealCheckoutRepository::map)
      .subscribeOn(Schedulers.io());
  }

  @Override public Single<Checkout> updateShippingAddress(@NonNull final String checkoutId, @NonNull final PayAddress address) {
    checkNotBlank(checkoutId, "checkoutId can't be empty");
    checkNotNull(address, "address == null");

    Storefront.MailingAddressInput mailingAddressInput = new Storefront.MailingAddressInput()
      .setAddress1(address.address1)
      .setAddress2(address.address2)
      .setCity(address.city)
      .setCountry(address.country)
      .setFirstName(address.firstName)
      .setLastName(address.lastName)
      .setPhone(address.phone)
      .setProvince(address.province)
      .setZip(address.zip);

    Storefront.CheckoutShippingAddressUpdateInput input = new Storefront.CheckoutShippingAddressUpdateInput(new ID(checkoutId),
      mailingAddressInput);

    GraphCall<Storefront.Mutation> call = graphClient.mutateGraph(
      Storefront.mutation(
        root -> root.checkoutShippingAddressUpdate(input, query -> query.checkout(new CheckoutFragment()))
      )
    );

    return rxGraphMutationCall(call)
      .map(GraphResponse::data)
      .map(Storefront.Mutation::getCheckoutShippingAddressUpdate)
      .map(Storefront.CheckoutShippingAddressUpdatePayload::getCheckout)
      .map(RealCheckoutRepository::map)
      .subscribeOn(Schedulers.io());
  }

  @Override public Single<Checkout> fetch(@NonNull final String checkoutId) {
    checkNotBlank(checkoutId, "checkoutId can't be empty");

    GraphCall<Storefront.QueryRoot> call = graphClient.queryGraph(
      Storefront.query(
        root -> root.node(new ID(checkoutId), query -> query.onCheckout(new CheckoutFragment()))
      )
    );
    return rxGraphQueryCall(call)
      .map(GraphResponse::data)
      .map(it -> (Storefront.Checkout) it.getNode())
      .map(RealCheckoutRepository::map)
      .subscribeOn(Schedulers.io());
  }

  @Override public Single<Checkout.ShippingRates> fetchShippingRates(@NonNull final String checkoutId) {
    checkNotBlank(checkoutId, "checkoutId can't be empty");

    GraphCall<Storefront.QueryRoot> call = graphClient.queryGraph(
      Storefront.query(
        root -> root.node(new ID(checkoutId), query -> query.onCheckout(
          checkout -> checkout.availableShippingRates(new CheckoutShippingRatesFragment())
        ))
      )
    );
    return Single.fromCallable(call::clone)
      .flatMap(RxUtil::rxGraphQueryCall)
      .map(GraphResponse::data)
      .map(it -> (Storefront.Checkout) it.getNode())
      .map(Storefront.Checkout::getAvailableShippingRates)
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
    Storefront.CheckoutShippingLineUpdateInput input = new Storefront.CheckoutShippingLineUpdateInput(
      new ID(checkNotBlank(checkoutId, "checkoutId can't be empty")),
      checkNotBlank(shippingRateHandle, "shippingRateHandle can't be empty")
    );

    GraphCall<Storefront.Mutation> call = graphClient.mutateGraph(
      Storefront.mutation(
        root -> root.checkoutShippingLineUpdate(input, query -> query.checkout(new CheckoutFragment()))
      )
    );
    return rxGraphMutationCall(call)
      .map(GraphResponse::data)
      .map(Storefront.Mutation::getCheckoutShippingLineUpdate)
      .map(Storefront.CheckoutShippingLineUpdatePayload::getCheckout)
      .map(RealCheckoutRepository::map)
      .subscribeOn(Schedulers.io());
  }

  @Override public Single<Checkout> updateEmail(@NonNull final String checkoutId, @NonNull final String email) {
    checkNotBlank(checkoutId, "checkoutId can't be empty");
    checkNotBlank(email, "email can't be empty");

    Storefront.CheckoutEmailUpdateInput input = new Storefront.CheckoutEmailUpdateInput(new ID(checkoutId), email);
    GraphCall<Storefront.Mutation> call = graphClient.mutateGraph(
      Storefront.mutation(
        root -> root.checkoutEmailUpdate(input, query -> query.checkout(new CheckoutFragment()))
      )
    );
    return rxGraphMutationCall(call)
      .map(GraphResponse::data)
      .map(Storefront.Mutation::getCheckoutEmailUpdate)
      .map(Storefront.CheckoutEmailUpdatePayload::getCheckout)
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

    Storefront.MailingAddressInput mailingAddressInput = new Storefront.MailingAddressInput()
      .setAddress1(billingAddress.address1)
      .setAddress2(billingAddress.address2)
      .setCity(billingAddress.city)
      .setCountry(billingAddress.country)
      .setFirstName(billingAddress.firstName)
      .setLastName(billingAddress.lastName)
      .setPhone(billingAddress.phone)
      .setProvince(billingAddress.province)
      .setZip(billingAddress.zip);

    Storefront.CheckoutCompleteWithTokenizedPaymentInput input = new Storefront.CheckoutCompleteWithTokenizedPaymentInput(
      payCart.totalPrice, mailingAddressInput, new ID(checkoutId), paymentToken.token, paymentToken.token, "android_pay")
      .setIdentifier(paymentToken.publicKeyHash);

    GraphCall<Storefront.Mutation> call = graphClient.mutateGraph(
      Storefront.mutation(
        root -> root.checkoutCompleteWithTokenizedPayment(input, query ->
          query
            .payment(new PaymentFragment())
            .userErrors(userError -> userError.field().message())))
    );
    return updateEmail(checkoutId, email)
      .flatMap(it -> rxGraphMutationCall(call))
      .map(GraphResponse::data)
      .map(Storefront.Mutation::getCheckoutCompleteWithTokenizedPayment)
      .flatMap(it -> {
        if (it.getUserErrors().isEmpty()) {
          return Single.just(it);
        } else {
          String errorMessage = fold(new StringBuilder(), it.getUserErrors(),
            (builder, error) -> builder.append(error.getField()).append(" : ").append(error.getMessage()).append("\n")).toString();
          return Single.error(new RuntimeException(errorMessage));
        }
      })
      .map(Storefront.CheckoutCompleteWithTokenizedPaymentPayload::getPayment)
      .map(RealCheckoutRepository::map)
      .subscribeOn(Schedulers.io());
  }

  private static Checkout map(final Storefront.Checkout checkout) {
    List<Checkout.LineItem> lineItems = mapItems(checkout.getLineItems().getEdges(), it ->
      new Checkout.LineItem(it.getNode().getVariant().getId().toString(), it.getNode().getTitle(), it.getNode().getQuantity(),
        it.getNode().getVariant().getPrice()));
    Checkout.ShippingRate shippingLine = checkout.getShippingLine() != null ? map(checkout.getShippingLine()) : null;
    return new Checkout(checkout.getId().toString(), checkout.getWebUrl(), checkout.getCurrencyCode().toString(),
      checkout.getRequiresShipping(), lineItems, map(checkout.getAvailableShippingRates()), shippingLine, checkout.getTotalTax(),
      checkout.getSubtotalPrice(), checkout.getTotalPrice());
  }

  private static Checkout.ShippingRates map(final Storefront.AvailableShippingRates availableShippingRates) {
    if (availableShippingRates == null) {
      return new Checkout.ShippingRates(false, emptyList());
    }
    List<Checkout.ShippingRate> shippingRates = mapItems(
      availableShippingRates.getShippingRates() != null ? availableShippingRates.getShippingRates() : emptyList(),
      it -> new Checkout.ShippingRate(it.getHandle(), it.getPrice(), it.getTitle()));
    return new Checkout.ShippingRates(availableShippingRates.getReady(), shippingRates);
  }

  private static Checkout.ShippingRate map(final Storefront.ShippingRate shippingRate) {
    return new Checkout.ShippingRate(shippingRate.getHandle(), shippingRate.getPrice(), shippingRate.getTitle());
  }

  private static Payment map(final Storefront.Payment payment) {
    return new Payment(payment.getId().toString(), payment.getErrorMessage(), payment.getReady(),
      payment.getTransaction() != null ? new Payment.Transaction(payment.getTransaction().getStatus().name()) : null);
  }
}
