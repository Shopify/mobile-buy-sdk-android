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

import java.math.BigDecimal;
import java.util.List;

import static com.shopify.sample.util.Util.checkNotNull;
import static java.util.Collections.unmodifiableList;

public final class ProductDetail {
  @NonNull public final String id;
  @NonNull public final String title;
  @NonNull public final String description;
  @NonNull public List<String> tags;
  @NonNull public List<String> images;
  @NonNull public List<Option> options;
  @NonNull public List<Variant> variants;

  public ProductDetail(@NonNull final String id, @NonNull final String title, @NonNull final String description,
                       @NonNull final List<String> tags, @NonNull final List<String> images, @NonNull final List<Option> options,
                       @NonNull final List<Variant> variants) {
    this.id = checkNotNull(id, "id == null");
    this.title = checkNotNull(title, "title == null");
    this.description = checkNotNull(description, "description == null");
    this.tags = checkNotNull(tags, "id == null");
    this.images = unmodifiableList(checkNotNull(images, "images == null"));
    this.options = unmodifiableList(checkNotNull(options, "options == null"));
    this.variants = unmodifiableList(checkNotNull(variants, "variants == null"));
  }

  @Override public String toString() {
    return "Product{" +
      "id='" + id + '\'' +
      ", title='" + title + '\'' +
      ", description='" + description + '\'' +
      ", tags=" + tags +
      ", images=" + images +
      ", options=" + options +
      ", variants=" + variants +
      '}';
  }

  public static final class Option {
    @NonNull public final String id;
    @NonNull public final String name;
    @NonNull public final List<String> values;

    public Option(@NonNull final String id, @NonNull final String name, @NonNull final List<String> values) {
      this.id = checkNotNull(id, "id == null");
      this.name = checkNotNull(name, "name == null");
      this.values = unmodifiableList(checkNotNull(values, "values == null"));
    }

    @Override public String toString() {
      return "Option{" +
        "id='" + id + '\'' +
        ", name='" + name + '\'' +
        ", values=" + values +
        '}';
    }
  }

  public static final class SelectedOption {
    @NonNull public final String name;
    @NonNull public final String value;

    public SelectedOption(@NonNull final String name, @NonNull final String value) {
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

  public static final class Variant {
    @NonNull public final String id;
    @NonNull public final String title;
    public final boolean available;
    @NonNull public final List<SelectedOption> selectedOptions;
    @NonNull public final BigDecimal price;

    public Variant(@NonNull final String id, @NonNull final String title, final boolean available,
      @NonNull final List<SelectedOption> selectedOptions, @NonNull final BigDecimal price) {
      this.id = checkNotNull(id, "name == null");
      this.title = checkNotNull(title, "title == null");
      this.available = available;
      this.selectedOptions = unmodifiableList(checkNotNull(selectedOptions, "selectedOptions == null"));
      this.price = checkNotNull(price, "price == null");
    }

    @Override public String toString() {
      return "Variant{" +
        "id='" + id + '\'' +
        ", title='" + title + '\'' +
        ", available=" + available +
        ", selectedOptions=" + selectedOptions +
        ", price=" + price +
        '}';
    }
  }
}
