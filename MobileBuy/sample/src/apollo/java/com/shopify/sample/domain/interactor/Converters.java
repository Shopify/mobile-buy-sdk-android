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

import com.shopify.sample.domain.CollectionPageWithProductsQuery;
import com.shopify.sample.domain.CollectionProductPageQuery;
import com.shopify.sample.domain.ProductByIdQuery;
import com.shopify.sample.domain.fragment.CheckoutCreateFragment;
import com.shopify.sample.domain.fragment.CheckoutFragment;
import com.shopify.sample.domain.fragment.CheckoutShippingRatesFragment;
import com.shopify.sample.domain.fragment.PaymentFragment;
import com.shopify.sample.domain.model.Checkout;
import com.shopify.sample.domain.model.Collection;
import com.shopify.sample.domain.model.Payment;
import com.shopify.sample.domain.model.Product;
import com.shopify.sample.domain.model.ProductDetails;

import java.math.BigDecimal;
import java.util.List;

import static com.shopify.sample.util.Util.firstItem;
import static com.shopify.sample.util.Util.mapItems;
import static com.shopify.sample.util.Util.minItem;
import static java.util.Collections.emptyList;

final class Converters {

  static List<Product> convertToProducts(final List<CollectionProductPageQuery.Data.ProductEdge> productEdges) {
    return mapItems(productEdges, productEdge -> {
      String productImageUrl = firstItem(productEdge.product.imageConnection.imageEdges,
        imageEdge -> imageEdge != null ? imageEdge.image.src : null);
      List<BigDecimal> prices = mapItems(productEdge.product.variantConnection.variantEdges,
        variantEdge -> variantEdge.variant.price);
      BigDecimal minPrice = minItem(prices, BigDecimal.ZERO, BigDecimal::compareTo);
      return new Product(productEdge.product.id, productEdge.product.title, productImageUrl, minPrice, productEdge.cursor);
    });
  }

  static ProductDetails convertToProductDetails(ProductByIdQuery.Data.AsProduct product) {
    List<String> images = mapItems(product.imageConnection.imageEdge, imageEdge -> imageEdge.image.src);
    List<ProductDetails.Option> options = mapItems(product.options, option -> new ProductDetails.Option(option.id, option.name,
      option.values));
    List<ProductDetails.Variant> variants = mapItems(product.variantConnection.variantEdge,
      variantEdge -> {
        List<ProductDetails.SelectedOption> selectedOptions = mapItems(variantEdge.variant.selectedOptions, selectedOption ->
          new ProductDetails.SelectedOption(selectedOption.name, selectedOption.value));
        return new ProductDetails.Variant(variantEdge.variant.id, variantEdge.variant.title, variantEdge.variant.available.or(true),
          selectedOptions, variantEdge.variant.price);
      });
    return new ProductDetails(product.id, product.title, product.descriptionHtml, product.tags, images, options, variants);
  }

  @SuppressWarnings("Convert2MethodRef") static List<Collection> convertToCollections(
    final List<CollectionPageWithProductsQuery.Data.Edge> collectionEdges) {
    return mapItems(collectionEdges, collectionEdge -> {
        String collectionImageUrl = collectionEdge.collection.image.transform(it -> it.src).or("");
        return new Collection(collectionEdge.collection.id, collectionEdge.collection.title,
          collectionEdge.collection.descriptionHtml, collectionImageUrl, collectionEdge.cursor,
          mapItems(collectionEdge.collection.productConnection.edges, productEdge -> {
            String productImageUrl = firstItem(productEdge.product.imageConnection.edges,
              imageEdge -> imageEdge != null ? imageEdge.image.src : null);
            List<BigDecimal> prices = mapItems(productEdge.product.variantConnection.variantEdge,
              variantEdge -> variantEdge.variant.price);
            BigDecimal minPrice = minItem(prices, BigDecimal.ZERO, BigDecimal::compareTo);
            return new Product(productEdge.product.id, productEdge.product.title, productImageUrl, minPrice, productEdge.cursor);
          }));
      }
    );
  }

  @SuppressWarnings("ConstantConditions") static Checkout convertToCheckout(final CheckoutFragment checkout) {
    List<Checkout.LineItem> lineItems = mapItems(checkout.lineItemConnection.lineItemEdges, it ->
      new Checkout.LineItem(it.lineItem.variant.get().id, it.lineItem.title, it.lineItem.quantity, it.lineItem.variant.get().price));
    Checkout.ShippingRate shippingLine = checkout.shippingLine.transform(it -> new Checkout.ShippingRate(it.handle, it.price, it.title))
      .orNull();
    Checkout.ShippingRates shippingRates = checkout.availableShippingRates.flatMap(it -> it.fragments.checkoutShippingRatesFragment)
      .transform(Converters::convertToShippingRates).or(new Checkout.ShippingRates(false, emptyList()));
    return new Checkout(checkout.id, checkout.webUrl, checkout.currencyCode.toString(), checkout.requiresShipping, lineItems, shippingRates,
      shippingLine, checkout.totalTax, checkout.subtotalPrice, checkout.paymentDue);
  }

  @SuppressWarnings("ConstantConditions") static Checkout convertToCheckout(final CheckoutCreateFragment checkout) {
    List<Checkout.LineItem> lineItems = mapItems(checkout.lineItemConnection.lineItemEdges, it ->
      new Checkout.LineItem(it.lineItem.variant.get().id, it.lineItem.title, it.lineItem.quantity, it.lineItem.variant.get().price));
    return new Checkout(checkout.id, checkout.webUrl, checkout.currencyCode.toString(), checkout.requiresShipping, lineItems,
      new Checkout.ShippingRates(false, emptyList()), null, checkout.totalTax, checkout.subtotalPrice, checkout.paymentDue);
  }

  static Checkout.ShippingRates convertToShippingRates(final CheckoutShippingRatesFragment availableShippingRates) {
    List<Checkout.ShippingRate> shippingRates = mapItems(availableShippingRates.shippingRates.or(emptyList()),
      it -> new Checkout.ShippingRate(it.handle, it.price, it.title));
    return new Checkout.ShippingRates(availableShippingRates.ready, shippingRates);
  }

  static Payment convertToPayment(final PaymentFragment payment) {
    return new Payment(payment.id, payment.errorMessage.orNull(), payment.ready);
  }

  private Converters() {
  }
}
