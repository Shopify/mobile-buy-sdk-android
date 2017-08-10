package com.shopify.buy3.pay;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import static com.shopify.buy3.pay.Util.checkNotNull;

/**
 * Card network type, such as Visa or Mastercard, which can be used for payments.
 */
public enum CardNetworkType {
  VISA,
  MASTERCARD,
  DISCOVER,
  AMERICAN_EXPRESS,
  JCB;

  /**
   * Find card network type by it's name.
   *
   * @param name of the card network type to be found
   * @return found {@link CardNetworkType} or {@code null} otherwise
   */
  @Nullable public static CardNetworkType findByName(@NonNull final String name) {
    checkNotNull(name, "name is null");

    for (final CardNetworkType type : CardNetworkType.values()) {
      if (type.name().equalsIgnoreCase(name.trim())) {
        return type;
      }
    }

    if ("AMEX".equalsIgnoreCase(name.trim())) {
      return AMERICAN_EXPRESS;
    }

    return null;
  }
}
