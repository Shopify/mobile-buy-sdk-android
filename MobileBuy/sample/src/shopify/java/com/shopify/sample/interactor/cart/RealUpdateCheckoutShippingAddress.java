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

import com.shopify.buy3.GraphCall;
import com.shopify.buy3.GraphClient;
import com.shopify.buy3.Storefront;
import com.shopify.buy3.pay.PayAddress;
import com.shopify.graphql.support.ID;
import com.shopify.sample.SampleApplication;
import com.shopify.sample.presenter.cart.Checkout;

import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

import static com.shopify.sample.RxUtil.rxGraphMutationCall;
import static com.shopify.sample.util.Util.checkNotBlank;
import static com.shopify.sample.util.Util.checkNotNull;
import static com.shopify.sample.util.Util.mapItems;

@SuppressWarnings("WeakerAccess")
public final class RealUpdateCheckoutShippingAddress implements UpdateCheckoutShippingAddress {
  private final GraphClient graphClient = SampleApplication.graphClient();

  @Override public Single<Checkout> call(@NonNull final String checkoutId, @NonNull final PayAddress address) {
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

    GraphCall<Storefront.Mutation> call = graphClient.mutateGraph(Storefront.mutation(
      root -> root.checkoutShippingAddressUpdate(input,
        query -> query.checkout(
          checkout -> checkout
            .webUrl()
            .requiresShipping()
            .currencyCode()
            .lineItems(250,
              lineItemConnection -> lineItemConnection.edges(
                lineItemEdge -> lineItemEdge.node(
                  lineItemNode -> lineItemNode
                    .variant(variant -> variant.price())
                    .quantity()
                    .title()
                )
              )
            )
        )
      ))
    );

    return rxGraphMutationCall(call)
      .map(it -> it.data().getCheckoutShippingAddressUpdate().getCheckout())
      .map(RealUpdateCheckoutShippingAddress::map)
      .subscribeOn(Schedulers.io());
  }

  private static Checkout map(final Storefront.Checkout checkout) {
    return new Checkout(checkout.getId().toString(), checkout.getWebUrl(), checkout.getCurrencyCode().toString(),
      checkout.getRequiresShipping(), mapItems(checkout.getLineItems().getEdges(), lineItemEdge -> {
      Storefront.LineItem lineItem = lineItemEdge.getNode();
      return new Checkout.LineItem(lineItem.getVariant().getId().toString(), lineItem.getTitle(), lineItem.getQuantity(),
        lineItem.getVariant().getPrice());
    }));
  }
}
