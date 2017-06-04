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

package com.shopify.sample.domain.model;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.math.BigDecimal;

import static com.shopify.sample.util.Util.checkNotNull;

public final class Product {
  @NonNull public final String id;
  @NonNull public final String title;
  @Nullable public final String image;
  @NonNull public final BigDecimal price;
  @NonNull public final String cursor;

  public Product(@NonNull final String id, @NonNull final String title, @Nullable final String image,
    @NonNull final BigDecimal price, @NonNull final String cursor) {
    this.id = checkNotNull(id, "id == null");
    this.title = checkNotNull(title, "title == null");
    this.image = image;
    this.price = price;
    this.cursor = checkNotNull(cursor, "cursor == null");
  }

  @Override public String toString() {
    return "Product{" +
      "id='" + id + '\'' +
      ", title='" + title + '\'' +
      ", image='" + image + '\'' +
      ", price='" + price + '\'' +
      ", cursor='" + cursor + '\'' +
      '}';
  }

  public boolean equalsById(@NonNull final Product other) {
    return id.equals(other.id);
  }

  @Override public boolean equals(final Object o) {
    if (this == o) return true;
    if (!(o instanceof Product)) return false;

    final Product product = (Product) o;

    if (!id.equals(product.id)) return false;
    if (!title.equals(product.title)) return false;
    if (image != null ? !image.equals(product.image) : product.image != null) return false;
    if (!price.equals(product.price)) return false;
    return cursor.equals(product.cursor);

  }

  @Override public int hashCode() {
    int result = id.hashCode();
    result = 31 * result + title.hashCode();
    result = 31 * result + (image != null ? image.hashCode() : 0);
    result = 31 * result + price.hashCode();
    result = 31 * result + cursor.hashCode();
    return result;
  }
}
