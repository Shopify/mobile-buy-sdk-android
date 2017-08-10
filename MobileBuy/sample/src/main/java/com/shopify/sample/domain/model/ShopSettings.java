package com.shopify.sample.domain.model;

import android.support.annotation.NonNull;

import com.shopify.buy3.pay.CardNetworkType;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.shopify.sample.util.Util.checkNotNull;

public final class ShopSettings {
  @NonNull public final String name;
  @NonNull public final Set<CardNetworkType> acceptedCardBrands;
  @NonNull public final String countryCode;

  public ShopSettings(@NonNull final String name, @NonNull final Set<CardNetworkType> acceptedCardBrands, @NonNull final String countryCode) {
    this.name = checkNotNull(name, "name can't be null");
    this.acceptedCardBrands = Collections.unmodifiableSet(checkNotNull(acceptedCardBrands, "acceptedCardBrands can't be null)"));
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
