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

package com.shopify.buy3.pay;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.android.gms.identity.intents.model.CountrySpecification;
import com.google.android.gms.wallet.Cart;
import com.google.android.gms.wallet.FullWalletRequest;
import com.google.android.gms.wallet.LineItem;
import com.google.android.gms.wallet.LineItem.Role;
import com.google.android.gms.wallet.MaskedWallet;
import com.google.android.gms.wallet.MaskedWalletRequest;
import com.google.android.gms.wallet.PaymentMethodTokenizationParameters;
import com.google.android.gms.wallet.PaymentMethodTokenizationType;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import static com.shopify.buy3.pay.PayHelper.UNSUPPORTED_SHIPPING_COUNTRIES;
import static com.shopify.buy3.pay.Util.checkNotEmpty;
import static com.shopify.buy3.pay.Util.checkNotNull;

@SuppressWarnings({"WeakerAccess", "unused"})
public final class PayCart implements Parcelable {
  @NonNull public final String currencyCode;
  @NonNull public final String merchantName;
  @NonNull public final List<String> shipsToCountries;
  public final boolean shippingAddressRequired;
  public final boolean phoneNumberRequired;
  @NonNull public final List<LineItem> lineItems;
  @NonNull public final BigDecimal subtotal;
  @Nullable public final BigDecimal taxPrice;
  @Nullable public final BigDecimal shippingPrice;
  @NonNull public final BigDecimal totalPrice;
  @Nullable public final MaskedWallet maskedWallet;

  protected PayCart(Parcel in) {
    currencyCode = in.readString();
    merchantName = in.readString();
    shipsToCountries = in.createStringArrayList();
    shippingAddressRequired = in.readByte() != 0;
    phoneNumberRequired = in.readByte() != 0;
    lineItems = in.createTypedArrayList(LineItem.CREATOR);
    subtotal = BigDecimal.valueOf(in.readDouble());
    if (in.readByte() == 1) {
      taxPrice = BigDecimal.valueOf(in.readDouble());
    } else {
      taxPrice = null;
    }
    if (in.readByte() == 1) {
      shippingPrice = BigDecimal.valueOf(in.readDouble());
    } else {
      shippingPrice = null;
    }
    totalPrice = BigDecimal.valueOf(in.readDouble());
    maskedWallet = in.readParcelable(MaskedWallet.class.getClassLoader());
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(currencyCode);
    dest.writeString(merchantName);
    dest.writeStringList(shipsToCountries);
    dest.writeByte((byte) (shippingAddressRequired ? 1 : 0));
    dest.writeByte((byte) (phoneNumberRequired ? 1 : 0));
    dest.writeTypedList(lineItems);
    dest.writeDouble(subtotal.doubleValue());
    dest.writeByte((byte) (taxPrice != null ? 1 : 0));
    if (taxPrice != null) {
      dest.writeDouble(taxPrice.doubleValue());
    }
    dest.writeByte((byte) (shippingPrice != null ? 1 : 0));
    if (shippingPrice != null) {
      dest.writeDouble(shippingPrice.doubleValue());
    }
    dest.writeDouble(totalPrice.doubleValue());
    dest.writeParcelable(maskedWallet, flags);
  }

  @Override
  public int describeContents() {
    return 0;
  }

  public static final Creator<PayCart> CREATOR = new Creator<PayCart>() {
    @Override
    public PayCart createFromParcel(Parcel in) {
      return new PayCart(in);
    }

    @Override
    public PayCart[] newArray(int size) {
      return new PayCart[size];
    }
  };

  public static Builder builder() {
    return new Builder();
  }

  PayCart(Builder builder) {
    this.currencyCode = checkNotEmpty(builder.currencyCode, "currencyCode can't be empty");
    this.merchantName = checkNotEmpty(builder.merchantName, "merchantName can't be empty");
    this.shipsToCountries = builder.shipsToCountries;
    this.shippingAddressRequired = builder.shippingAddressRequired;
    this.phoneNumberRequired = builder.phoneNumberRequired;
    this.lineItems = builder.lineItems;
    this.taxPrice = builder.taxPrice;
    this.shippingPrice = builder.shippingPrice;
    this.subtotal = builder.subtotal;
    this.totalPrice = (builder.totalPrice == null) ? builder.subtotal : builder.totalPrice;
    this.maskedWallet = builder.maskedWallet;
  }

  public MaskedWalletRequest maskedWalletRequest(@NonNull final String androidPayPublicKey) {
    PaymentMethodTokenizationParameters parameters =
      PaymentMethodTokenizationParameters.newBuilder()
        .setPaymentMethodTokenizationType(PaymentMethodTokenizationType.NETWORK_TOKEN)
        .addParameter("publicKey", checkNotEmpty(androidPayPublicKey, "androidPayPublicKey can't be empty"))
        .build();

    MaskedWalletRequest.Builder builder = MaskedWalletRequest.newBuilder()
      .setMerchantName(merchantName)
      .setPhoneNumberRequired(phoneNumberRequired)
      .setShippingAddressRequired(shippingAddressRequired)
      .setCurrencyCode(currencyCode)
      .setEstimatedTotalPrice(totalPrice.toString())
      .setPaymentMethodTokenizationParameters(parameters)
      .setCart(cartBuilder().build());

    if (!shipsToCountries.isEmpty()) {
      Collection<CountrySpecification> shippingCountrySpecifications = shippingCountrySpecifications(shipsToCountries);
      builder.addAllowedCountrySpecificationsForShipping(shippingCountrySpecifications);
    }

    return builder.build();
  }

  public FullWalletRequest fullWalletRequest(@NonNull final String androidPayTransactionId) {
    checkNotEmpty(androidPayTransactionId, "androidPayTransactionId can't be empty");
    return FullWalletRequest.newBuilder()
      .setGoogleTransactionId(androidPayTransactionId)
      .setCart(cartBuilder().build())
      .build();
  }

  public Builder toBuilder() {
    Builder builder = new Builder();
    builder.currencyCode = currencyCode;
    builder.merchantName = merchantName;
    builder.shipsToCountries = shipsToCountries;
    builder.shippingAddressRequired = shippingAddressRequired;
    builder.phoneNumberRequired = phoneNumberRequired;
    builder.lineItems = new ArrayList<>(lineItems);
    builder.subtotal = subtotal;
    builder.taxPrice = taxPrice;
    builder.shippingPrice = shippingPrice;
    builder.totalPrice = totalPrice;
    builder.maskedWallet = maskedWallet;
    return builder;
  }

  Cart.Builder cartBuilder() {
    Cart.Builder builder = Cart.newBuilder()
      .setCurrencyCode(currencyCode)
      .setTotalPrice(totalPrice.toString())
      .setLineItems(lineItems);

    if (taxPrice != null) {
      builder.addLineItem(LineItem.newBuilder()
        .setRole(LineItem.Role.TAX)
        .setTotalPrice(taxPrice.toString())
        .build());
    }

    if (shippingPrice != null) {
      builder.addLineItem(LineItem.newBuilder()
        .setRole(LineItem.Role.SHIPPING)
        .setTotalPrice(shippingPrice.toString())
        .build());
    }

    return builder;
  }

  private static Collection<CountrySpecification> shippingCountrySpecifications(Collection<String> shipsToCountries) {
    Set<String> countryCodes = new LinkedHashSet<>(shipsToCountries);

    String wildcard = "*";
    if (countryCodes.remove(wildcard)) {
      List<String> wildCardCodes = new ArrayList<>(Arrays.asList(Locale.getISOCountries()));
      wildCardCodes.removeAll(Arrays.asList(UNSUPPORTED_SHIPPING_COUNTRIES));
      countryCodes.addAll(wildCardCodes);
    }

    List<CountrySpecification> countrySpecifications = new ArrayList<>(countryCodes.size());
    for (String countryCode : countryCodes) {
      countrySpecifications.add(new CountrySpecification(countryCode));
    }

    return countrySpecifications;
  }

  public static final class Builder {
    String currencyCode;
    String merchantName;
    boolean shippingAddressRequired;
    List<String> shipsToCountries = Collections.emptyList();
    boolean phoneNumberRequired;
    List<LineItem> lineItems = new ArrayList<>();
    BigDecimal subtotal = BigDecimal.ZERO;
    BigDecimal taxPrice;
    BigDecimal shippingPrice;
    BigDecimal totalPrice;
    MaskedWallet maskedWallet;

    Builder() {
    }

    public Builder merchantName(@NonNull final String merchantName) {
      this.merchantName = checkNotEmpty(merchantName, "merchantName can't be empty");
      return this;
    }

    public Builder currencyCode(@NonNull final String currencyCode) {
      this.currencyCode = checkNotEmpty(currencyCode, "currencyCode can't be empty");
      return this;
    }

    public Builder shippingAddressRequired(final boolean shippingAddressRequired) {
      this.shippingAddressRequired = shippingAddressRequired;
      return this;
    }

    public Builder addLineItem(@NonNull final String title, final int quantity, @NonNull final BigDecimal price) {
      if (quantity <= 0) {
        throw new IllegalArgumentException("quantity can't be less than 1");
      }

      LineItem lineItem = LineItem.newBuilder()
        .setQuantity(Integer.toString(quantity))
        .setUnitPrice(price.toString())
        .setTotalPrice(price.multiply(BigDecimal.valueOf(quantity)).toString())
        .setDescription(checkNotEmpty(title, "title can't be empty"))
        .setCurrencyCode(currencyCode)
        .setRole(Role.REGULAR)
        .build();
      lineItems.add(lineItem);

      subtotal = subtotal.add(price.multiply(BigDecimal.valueOf(quantity)));

      return this;
    }

    Builder lineItems(@NonNull final List<LineItem> lineItems) {
      this.lineItems = checkNotNull(lineItems, "lineItems == null");
      return this;
    }

    public Builder phoneNumberRequired(final boolean phoneNumberRequired) {
      this.phoneNumberRequired = phoneNumberRequired;
      return this;
    }

    public Builder shipsToCountries(final Collection<String> shipsToCountries) {
      if (shipsToCountries != null) {
        this.shipsToCountries = new ArrayList<>(shipsToCountries);
      } else {
        this.shipsToCountries = Collections.singletonList("*");
      }
      return this;
    }

    public Builder taxPrice(@NonNull final BigDecimal taxPrice) {
      this.taxPrice = checkNotNull(taxPrice, "taxPrice can't be null");
      return this;
    }

    public Builder shippingPrice(@NonNull final BigDecimal shippingPrice) {
      this.shippingPrice = checkNotNull(shippingPrice, "shippingPrice can't be null");
      return this;
    }

    public Builder totalPrice(@NonNull final BigDecimal totalPrice) {
      this.totalPrice = checkNotNull(totalPrice, "totalPrice == null");
      return this;
    }

    public Builder maskedWallet(@Nullable final MaskedWallet maskedWallet) {
      this.maskedWallet = maskedWallet;
      return this;
    }

    public PayCart build() {
      return new PayCart(this);
    }
  }
}
