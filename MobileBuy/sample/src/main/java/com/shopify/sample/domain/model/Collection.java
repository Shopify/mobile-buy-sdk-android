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

import java.util.List;

import static com.shopify.sample.util.Util.checkNotNull;
import static java.util.Collections.unmodifiableList;

public final class Collection {
  @NonNull public final String id;
  @NonNull public final String title;
  @NonNull public final String description;
  @Nullable public final String image;
  @NonNull public final String cursor;
  @NonNull public final List<Product> products;

  public Collection(@NonNull final String id, @NonNull final String title, @NonNull final String description,
    @Nullable final String image, @NonNull final String cursor, @NonNull final List<Product> products) {
    this.id = checkNotNull(id, "id == null");
    this.title = checkNotNull(title, "title == null");
    this.description = checkNotNull(description, "description == null");
    this.image = image;
    this.cursor = checkNotNull(cursor, "cursor == null");
    this.products = unmodifiableList(checkNotNull(products, "products == null"));
  }

  @Override public String toString() {
    return "Collection{" +
      "id='" + id + '\'' +
      ", title='" + title + '\'' +
      ", description='" + description + '\'' +
      ", image='" + image + '\'' +
      ", cursor='" + cursor + '\'' +
      ", products=" + products +
      '}';
  }

  public boolean equalsById(@NonNull final Collection other) {
    return id.equals(other.id);
  }

  @Override public boolean equals(final Object o) {
    if (this == o) return true;
    if (!(o instanceof Collection)) return false;

    final Collection that = (Collection) o;

    if (!id.equals(that.id)) return false;
    if (!title.equals(that.title)) return false;
    if (!description.equals(that.description)) return false;
    if (image != null ? !image.equals(that.image) : that.image != null) return false;
    if (!cursor.equals(that.cursor)) return false;
    return products.equals(that.products);
  }

  @Override public int hashCode() {
    int result = id.hashCode();
    result = 31 * result + title.hashCode();
    result = 31 * result + description.hashCode();
    result = 31 * result + (image != null ? image.hashCode() : 0);
    result = 31 * result + cursor.hashCode();
    result = 31 * result + products.hashCode();
    return result;
  }
}
