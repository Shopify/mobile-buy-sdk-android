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

import org.junit.Test;

import okhttp3.OkHttpClient;

import static com.google.common.truth.Truth.assertThat;
import static com.shopify.buy3.TestUtils.checkForNullPointerException;

public class CardClientTest {

  @Test public void instantiateWithNullOkHttpClient() {
    checkForNullPointerException(() -> new CardClient(null));
  }

  @Test public void instantiateWithDefaultOkHttpClient() {
    CardClient cardClient = new CardClient();
    assertThat(cardClient.httpCallFactory).isNotNull();

    CreditCard creditCard = provideCreditCard();
    RealCreditCardVaultCall call = (RealCreditCardVaultCall) cardClient.vault(creditCard, "http://google.com");
    assertThat(call.httpCallFactory).isEqualTo(cardClient.httpCallFactory);
  }

  @Test public void instantiateWithOkHttpClient() {
    OkHttpClient okHttpClient = new OkHttpClient.Builder().build();

    CardClient cardClient = new CardClient(okHttpClient);
    assertThat(cardClient.httpCallFactory).isEqualTo(okHttpClient);

    CreditCard creditCard = provideCreditCard();
    RealCreditCardVaultCall call = (RealCreditCardVaultCall) cardClient.vault(creditCard, "http://google.com");
    assertThat(call.httpCallFactory).isEqualTo(okHttpClient);
  }

  private CreditCard provideCreditCard() {
    return CreditCard.builder()
      .firstName("John")
      .lastName("Smith")
      .number("1")
      .expireMonth("06")
      .expireYear("2017")
      .verificationCode("111")
      .build();
  }
}
