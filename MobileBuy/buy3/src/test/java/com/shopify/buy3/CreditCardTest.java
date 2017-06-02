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

import com.google.common.truth.Truth;

import org.junit.Test;

import static com.shopify.buy3.TestUtils.checkForNullPointerException;
import static com.shopify.buy3.TestUtils.checkIllegalArgumentException;

public class CreditCardTest {

  @Test public void checkBuilderPrecondition() {
    checkForNullPointerException(() -> CreditCard.builder().firstName(null));
    checkForNullPointerException(() -> CreditCard.builder().lastName(null));
    checkForNullPointerException(() -> CreditCard.builder().number(null));
    checkForNullPointerException(() -> CreditCard.builder().expireMonth(null));
    checkForNullPointerException(() -> CreditCard.builder().expireYear(null));

    checkIllegalArgumentException(() -> CreditCard.builder().firstName(""));
    checkIllegalArgumentException(() -> CreditCard.builder().lastName(""));
    checkIllegalArgumentException(() -> CreditCard.builder().number(""));
    checkIllegalArgumentException(() -> CreditCard.builder().expireMonth(""));
    checkIllegalArgumentException(() -> CreditCard.builder().expireYear(""));
  }

  @Test public void checkBuildPrecondition() {
    checkForNullPointerException(() -> CreditCard.builder().build());
    checkForNullPointerException(() -> CreditCard.builder().firstName("John").build());
    checkForNullPointerException(() -> CreditCard.builder().firstName("John").lastName("Smith").build());
    checkForNullPointerException(() -> CreditCard.builder().firstName("John").lastName("Smith").number("1").build());
    checkForNullPointerException(() -> CreditCard.builder().firstName("John").lastName("Smith").number("1").expireMonth("06").build());
  }

  @Test public void checkBuildSuccess() {
    Truth.assertThat(CreditCard.builder()
      .firstName("John")
      .lastName("Smith")
      .number("1")
      .expireMonth("06")
      .expireYear("2017")
      .build()).isNotNull();

    Truth.assertThat(CreditCard.builder()
      .firstName("John")
      .lastName("Smith")
      .number("1")
      .expireMonth("06")
      .expireYear("2017")
      .verificationCode("111")
      .build()).isNotNull();
  }
}
