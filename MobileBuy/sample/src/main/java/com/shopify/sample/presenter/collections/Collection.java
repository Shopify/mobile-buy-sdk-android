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

package com.shopify.sample.presenter.collections;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.Collections;
import java.util.List;

import static com.shopify.sample.util.Util.checkNotNull;

public final class Collection {
  @NonNull private final String id;
  @NonNull private final String title;
  @Nullable private final String imageUrl;
  @NonNull private final String cursor;
  @NonNull private final List<Product> products;

  public Collection(@NonNull final String id, @NonNull final String title, @Nullable final String imageUrl, @NonNull final String cursor,
                    @NonNull final List<Product> products) {
    this.id = checkNotNull(id, "id == null");
    this.title = checkNotNull(title, "title == null");
    this.imageUrl = imageUrl;
    this.cursor = checkNotNull(cursor, "cursor == null");
    this.products = Collections.unmodifiableList(checkNotNull(products, "products == null"));
  }

  @NonNull public String id() {
    return id;
  }

  @NonNull public String title() {
    return title;
  }

  @Nullable public String imageUrl() {
    return imageUrl;
  }

  @NonNull public String cursor() {
    return cursor;
  }

  @NonNull public List<Product> products() {
    return products;
  }

  @Override public String toString() {
    return "Collection{" +
      "id='" + id + '\'' +
      ", title='" + title + '\'' +
      ", imageUrl='" + imageUrl + '\'' +
      ", cursor='" + cursor + '\'' +
      ", products=" + products +
      '}';
  }

  public static final class Product {
    @NonNull private final String id;
    @NonNull private final String title;
    @Nullable private final String imageUrl;
    @NonNull private final String cursor;

    public Product(@NonNull final String id, @NonNull final String title, @Nullable final String imageUrl, @NonNull final String cursor) {
      this.id = checkNotNull(id, "id == null");
      this.title = checkNotNull(title, "title == null");
      this.imageUrl = imageUrl;
      this.cursor = checkNotNull(cursor, "cursor == null");
    }

    @NonNull public String id() {
      return id;
    }

    @NonNull public String title() {
      return title;
    }

    @Nullable public String imageUrl() {
      return imageUrl;
    }

    @NonNull public String cursor() {
      return cursor;
    }

    @Override public String toString() {
      return "Product{" +
        "id='" + id + '\'' +
        ", title='" + title + '\'' +
        ", imageUrl='" + imageUrl + '\'' +
        ", cursor='" + cursor + '\'' +
        '}';
    }
  }
}
