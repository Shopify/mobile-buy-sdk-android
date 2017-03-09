package com.shopify.sample.presenter.collections.model;

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
