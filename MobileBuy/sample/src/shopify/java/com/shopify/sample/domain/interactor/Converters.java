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

import com.shopify.buy3.Storefront;
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
  static List<Product> convertToProducts(final List<Storefront.ProductEdge> edges) {
    return mapItems(edges, productEdge -> {
      Storefront.Product product = productEdge.getNode();
      String productImageUrl = firstItem(product.getImages() != null ? product.getImages().getEdges() : null,
        image -> image != null ? image.getNode().getSrc() : null);
      List<BigDecimal> prices = mapItems(product.getVariants().getEdges(), variantEdge -> variantEdge.getNode().getPrice());
      BigDecimal minPrice = minItem(prices, BigDecimal.ZERO, BigDecimal::compareTo);
      return new Product(product.getId().toString(), product.getTitle(), productImageUrl, minPrice, productEdge.getCursor());
    });
  }

  static ProductDetails convertToProductDetails(final Storefront.Product product) {
    List<String> images = mapItems(product.getImages().getEdges(), imageEdge -> imageEdge.getNode().getSrc());
    List<ProductDetails.Option> options = mapItems(product.getOptions(), option ->
      new ProductDetails.Option(option.getId().toString(), option.getName(), option.getValues()));
    List<ProductDetails.Variant> variants = mapItems(product.getVariants().getEdges(),
      variantEdge -> {
        List<ProductDetails.SelectedOption> selectedOptions = mapItems(variantEdge.getNode().getSelectedOptions(), selectedOption ->
          new ProductDetails.SelectedOption(selectedOption.getName(), selectedOption.getValue()));
        return new ProductDetails.Variant(variantEdge.getNode().getId().toString(), variantEdge.getNode().getTitle(),
          (variantEdge.getNode().getAvailableForSale() == null || Boolean.TRUE.equals(variantEdge.getNode().getAvailableForSale())),
          selectedOptions, variantEdge.getNode().getPrice());
      });
    return new ProductDetails(product.getId().toString(), product.getTitle(), product.getDescriptionHtml(), product.getTags(), images, options,
      variants);
  }

  static List<Collection> convertToCollections(final List<Storefront.CollectionEdge> edges) {
    return mapItems(edges, collectionEdge -> {
      Storefront.Collection collection = collectionEdge.getNode();
      String collectionImageUrl = collection.getImage() != null ? collection.getImage().getSrc() : null;
      return new Collection(collection.getId().toString(), collection.getTitle(), collection.getDescription(),
        collectionImageUrl, collectionEdge.getCursor(), mapItems(collection.getProducts().getEdges(), productEdge -> {
        Storefront.Product product = productEdge.getNode();
        String productImageUrl = firstItem(product.getImages() != null ? product.getImages().getEdges() : null,
          image -> image != null ? image.getNode().getSrc() : null);
        List<BigDecimal> prices = mapItems(product.getVariants().getEdges(), variantEdge -> variantEdge.getNode().getPrice());
        BigDecimal minPrice = minItem(prices, BigDecimal.ZERO, BigDecimal::compareTo);
        return new Product(product.getId().toString(), product.getTitle(), productImageUrl, minPrice, productEdge.getCursor());
      }));
    });
  }

  static Checkout convertToCheckout(final Storefront.Checkout checkout) {
    List<Checkout.LineItem> lineItems = mapItems(checkout.getLineItems().getEdges(), it ->
      new Checkout.LineItem(it.getNode().getVariant().getId().toString(), it.getNode().getTitle(), it.getNode().getQuantity(),
        it.getNode().getVariant().getPrice()));
    Checkout.ShippingRate shippingLine = checkout.getShippingLine() != null ? convertToShippingRate(checkout.getShippingLine()) : null;
    return new Checkout(checkout.getId().toString(), checkout.getWebUrl(), checkout.getCurrencyCode().toString(),
      checkout.getRequiresShipping(), lineItems, convertToShippingRates(checkout.getAvailableShippingRates()),
      shippingLine, checkout.getTotalTax(), checkout.getSubtotalPrice(), checkout.getTotalPrice());
  }

  static Checkout.ShippingRates convertToShippingRates(final Storefront.AvailableShippingRates availableShippingRates) {
    if (availableShippingRates == null) {
      return new Checkout.ShippingRates(false, emptyList());
    }
    List<Checkout.ShippingRate> shippingRates = mapItems(
      availableShippingRates.getShippingRates() != null ? availableShippingRates.getShippingRates() : emptyList(),
      it -> new Checkout.ShippingRate(it.getHandle(), it.getPrice(), it.getTitle()));
    return new Checkout.ShippingRates(availableShippingRates.getReady(), shippingRates);
  }

  static Checkout.ShippingRate convertToShippingRate(final Storefront.ShippingRate shippingRate) {
    return new Checkout.ShippingRate(shippingRate.getHandle(), shippingRate.getPrice(), shippingRate.getTitle());
  }

  static Payment convertToPayment(final Storefront.Payment payment) {
    return new Payment(payment.getId().toString(), payment.getErrorMessage(), payment.getReady());
  }

  private Converters() {
  }
}
