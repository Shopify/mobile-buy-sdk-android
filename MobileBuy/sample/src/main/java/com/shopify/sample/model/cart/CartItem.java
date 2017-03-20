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

package com.shopify.sample.model.cart;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static com.shopify.sample.util.Util.checkEmpty;
import static com.shopify.sample.util.Util.checkNotNull;
import static java.util.Collections.unmodifiableList;

public final class CartItem {
  @NonNull public final String productId;
  @NonNull public final String productVariantId;
  @NonNull public final String title;
  @NonNull public final BigDecimal price;
  @NonNull public final List<Option> options;
  @Nullable public final String image;

  public CartItem(@NonNull final String productId, @NonNull final String productVariantId, @NonNull final String title,
    @NonNull final BigDecimal price, @NonNull final List<Option> options, @Nullable final String image) {
    this.productId = checkNotNull(productId, "productId == null");
    this.productVariantId = checkNotNull(productVariantId, "productVariantId == null");
    this.title = checkNotNull(title, "title == null");
    this.price = checkNotNull(price, "price == null");
    this.options = unmodifiableList(checkEmpty(options, "options is empty"));
    this.image = image;
  }

  public static final class Option {
    @NonNull public final String name;
    @NonNull public final String value;

    public Option(@NonNull final String name, @NonNull final String value) {
      this.name = checkNotNull(name, "name == null");
      this.value = checkNotNull(value, "value == null");
    }

    @Override public String toString() {
      return "SelectedOption{" +
        "name='" + name + '\'' +
        ", value='" + value + '\'' +
        '}';
    }
  }
}
