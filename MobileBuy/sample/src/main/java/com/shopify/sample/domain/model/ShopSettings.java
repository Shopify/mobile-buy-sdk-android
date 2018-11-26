package com.shopify.sample.domain.model;

import android.support.annotation.NonNull;

import static com.shopify.sample.util.Util.checkNotNull;

public final class ShopSettings {
  @NonNull
  public final String name;
  @NonNull
  public final String countryCode;

  public ShopSettings(@NonNull final String name, @NonNull final String countryCode) {
    this.name = checkNotNull(name, "name can't be null");
    this.countryCode = checkNotNull(countryCode, "countryCode can't be null");
  }

  @Override
  public String toString() {
    return "ShopSettings{" +
        "name='" + name + '\'' +
        ", countryCode='" + countryCode + '\'' +
        '}';
  }
}
