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

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.google.android.gms.identity.intents.model.UserAddress;

import static com.shopify.buy3.pay.Util.checkNotNull;

/**
 * Convenient class that represents a Shopify-compliant address.
 */
public final class PayAddress {
  private static final int FIRST_NAME_PART = 0;
  private static final int LAST_NAME_PART = 1;

  @Nullable public final String address1;
  @Nullable public final String address2;
  @Nullable public final String city;
  @Nullable public final String country;
  @Nullable public final String firstName;
  @Nullable public final String lastName;
  @Nullable public final String phone;
  @Nullable public final String province;
  @Nullable public final String zip;

  /**
   * Converts Android Pay {@link UserAddress} to Shopify address format.
   *
   * @param userAddress android pay user address
   * @return Shopify compliant address
   */
  public static PayAddress fromUserAddress(@NonNull final UserAddress userAddress) {
    checkNotNull(userAddress, "userAddress == null");

    String[] nameParts = extractFirstAndLastNames(userAddress.getName());

    StringBuilder address2 = new StringBuilder();
    if (!TextUtils.isEmpty(userAddress.getAddress2())) {
      address2.append(userAddress.getAddress2());
      address2.append(", ");
    }
    if (!TextUtils.isEmpty(userAddress.getAddress3())) {
      address2.append(userAddress.getAddress3());
      address2.append(", ");
    }
    if (!TextUtils.isEmpty(userAddress.getAddress4())) {
      address2.append(userAddress.getAddress4());
      address2.append(", ");
    }
    if (!TextUtils.isEmpty(userAddress.getAddress5())) {
      address2.append(userAddress.getAddress5());
    }

    return new PayAddress(userAddress.getAddress1(), address2.toString(), userAddress.getLocality(), userAddress.getCountryCode(),
      nameParts[FIRST_NAME_PART], nameParts[LAST_NAME_PART], userAddress.getPhoneNumber(), userAddress.getAdministrativeArea(),
      userAddress.getPostalCode());
  }

  private PayAddress(@Nullable final String address1, @Nullable final String address2, @Nullable final String city,
    @Nullable final String country, @Nullable final String firstName, @Nullable final String lastName, @Nullable final String phone,
    @Nullable final String province, @Nullable final String zip) {
    this.address1 = address1;
    this.address2 = address2;
    this.city = city;
    this.country = country;
    this.firstName = firstName;
    this.lastName = lastName;
    this.phone = phone;
    this.province = province;
    this.zip = zip;
  }

  @Override public String toString() {
    return "PayAddress{" +
      "address1='" + address1 + '\'' +
      ", address2='" + address2 + '\'' +
      ", city='" + city + '\'' +
      ", country='" + country + '\'' +
      ", firstName='" + firstName + '\'' +
      ", lastName='" + lastName + '\'' +
      ", phone='" + phone + '\'' +
      ", province='" + province + '\'' +
      ", zip='" + zip + '\'' +
      '}';
  }

  private static String[] extractFirstAndLastNames(String name) {
    String[] firstAndLastName = new String[2];
    String[] nameParts = name.split(" ");
    if (nameParts.length > 0) {
      firstAndLastName[FIRST_NAME_PART] = (nameParts[FIRST_NAME_PART]);
    }
    int nameIndex = 1;
    String lastName = "";
    while (nameParts.length > nameIndex) {
      lastName += nameParts[nameIndex++];
    }
    firstAndLastName[LAST_NAME_PART] = lastName.trim();
    return firstAndLastName;
  }
}
