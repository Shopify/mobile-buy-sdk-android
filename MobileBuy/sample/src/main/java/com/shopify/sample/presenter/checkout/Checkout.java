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

package com.shopify.sample.presenter.checkout;

import android.support.annotation.NonNull;

import java.math.BigDecimal;
import java.util.List;

import static com.shopify.sample.util.Util.checkNotEmpty;
import static com.shopify.sample.util.Util.checkNotNull;

@SuppressWarnings("WeakerAccess")
public final class Checkout {
  @NonNull public final String id;
  @NonNull public final String webUrl;
  @NonNull public final String currency;
  public final boolean requiresShipping;
  @NonNull public final List<LineItem> lineItems;

  public Checkout(@NonNull final String id, @NonNull final String webUrl, @NonNull final String currency, final boolean requiresShipping,
    @NonNull final List<LineItem> lineItems) {
    this.id = checkNotNull(id, "id == null");
    this.webUrl = checkNotNull(webUrl, "webUrl == null");
    this.currency = checkNotNull(currency, "currency == null");
    this.requiresShipping = requiresShipping;
    this.lineItems = checkNotEmpty(lineItems, "lineItems can't be empty");
  }

  @Override public String toString() {
    return "Checkout{" +
      "id='" + id + '\'' +
      ", webUrl='" + webUrl + '\'' +
      ", currency='" + currency + '\'' +
      ", requiresShipping=" + requiresShipping +
      ", lineItems=" + lineItems +
      '}';
  }

  @SuppressWarnings("WeakerAccess")
  public static final class LineItem {
    @NonNull public final String variantId;
    @NonNull public final String title;
    public final int quantity;
    @NonNull public final BigDecimal price;

    public LineItem(@NonNull final String variantId, @NonNull final String title, final int quantity, @NonNull final BigDecimal price) {
      this.variantId = checkNotNull(variantId, "variantId == null");
      this.title = checkNotNull(title, "title == null");
      this.quantity = quantity;
      this.price = checkNotNull(price, "price == null");
    }

    @Override public String toString() {
      return "LineItem{" +
        "variantId='" + variantId + '\'' +
        ", title='" + title + '\'' +
        ", quantity=" + quantity +
        ", price=" + price +
        '}';
    }
  }
}
