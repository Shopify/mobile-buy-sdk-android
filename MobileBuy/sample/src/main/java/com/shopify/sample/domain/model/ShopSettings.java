package com.shopify.sample.domain.model;

import android.support.annotation.NonNull;

import java.util.Collections;
import java.util.List;

import static com.shopify.sample.util.Util.checkNotNull;

public final class ShopSettings {
  @NonNull public final String name;
  @NonNull public final List<String> acceptedCardBrands;
  @NonNull public final String countryCode;

  public ShopSettings(@NonNull final String name, @NonNull final List<String> acceptedCardBrands, @NonNull final String countryCode) {
    this.name = checkNotNull(name, "name can't be null");
    this.acceptedCardBrands = Collections.unmodifiableList(checkNotNull(acceptedCardBrands, "acceptedCardBrands can't be null)"));
    this.countryCode = checkNotNull(countryCode, "countryCode can't be null");
  }

  @Override public String toString() {
    return "ShopSettings{" +
      "name='" + name + '\'' +
      ", acceptedCardBrands=" + acceptedCardBrands +
      ", countryCode='" + countryCode + '\'' +
      '}';
  }
}
