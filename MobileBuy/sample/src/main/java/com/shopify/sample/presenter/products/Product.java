package com.shopify.sample.presenter.products;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.math.BigDecimal;

import static com.shopify.sample.util.Util.checkNotNull;

public final class Product {
  @NonNull private final String id;
  @NonNull private final String title;
  @Nullable private final String imageUrl;
  @NonNull private final BigDecimal minPrice;
  @NonNull private final String cursor;

  public Product(@NonNull final String id, @NonNull final String title, @Nullable final String imageUrl,
    final @NonNull BigDecimal minPrice, @NonNull final String cursor) {
    this.id = checkNotNull(id, "id == null");
    this.title = checkNotNull(title, "title == null");
    this.imageUrl = imageUrl;
    this.minPrice = minPrice;
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

  @NonNull public BigDecimal minPrice() {
    return minPrice;
  }

  @NonNull public String cursor() {
    return cursor;
  }

  @Override public String toString() {
    return "Product{" +
      "id='" + id + '\'' +
      ", title='" + title + '\'' +
      ", imageUrl='" + imageUrl + '\'' +
      ", minPrice='" + minPrice + '\'' +
      ", cursor='" + cursor + '\'' +
      '}';
  }
}
