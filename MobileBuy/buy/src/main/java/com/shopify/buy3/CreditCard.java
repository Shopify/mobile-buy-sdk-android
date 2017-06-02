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

package com.shopify.buy3;

import static com.shopify.buy3.Utils.checkNotBlank;

/**
 * <p>Credit Card information</p>
 * Abstraction that represents credit card information.
 */
@SuppressWarnings("WeakerAccess")
public final class CreditCard {
  public static CreditCard.Builder builder() {
    return new CreditCard.Builder();
  }

  public final String number;
  public final String firstName;
  public final String lastName;
  public final String expireMonth;
  public final String expireYear;
  public final String verificationCode;

  private CreditCard(final String number, final String firstName, final String lastName, final String expireMonth, final String expireYear,
    final String verificationCode) {
    this.number = checkNotBlank(number, "number can't be empty");
    this.firstName = checkNotBlank(firstName, "firstName can't be empty");
    this.lastName = checkNotBlank(lastName, "lastName can't be empty");
    this.expireMonth = checkNotBlank(expireMonth, "expireMonth can't be empty");
    this.expireYear = checkNotBlank(expireYear, "expireYear can't be empty");
    this.verificationCode = verificationCode;
  }

  /**
   * Builds new {@link CreditCard} instance.
   */
  public static final class Builder {
    private String number;
    private String firstName;
    private String lastName;
    private String expireMonth;
    private String expireYear;
    private String verificationCode;

    Builder() {
    }

    public Builder number(final String number) {
      this.number = checkNotBlank(number, "number can't be empty");
      return this;
    }

    public Builder firstName(final String firstName) {
      this.firstName = checkNotBlank(firstName, "firstName can't be empty");
      return this;
    }

    public Builder lastName(final String lastName) {
      this.lastName = checkNotBlank(lastName, "lastName can't be empty");
      return this;
    }

    public Builder expireMonth(final String expireMonth) {
      this.expireMonth = checkNotBlank(expireMonth, "expireMonth can't be empty");
      return this;
    }

    public Builder expireYear(final String expireYear) {
      this.expireYear = checkNotBlank(expireYear, "expireYear can't be empty");
      return this;
    }

    public Builder verificationCode(final String verificationCode) {
      this.verificationCode = verificationCode;
      return this;
    }

    /**
     * Builds new {@link CreditCard} instance.
     */
    public CreditCard build() {
      return new CreditCard(number, firstName, lastName, expireMonth, expireYear, verificationCode);
    }
  }
}
