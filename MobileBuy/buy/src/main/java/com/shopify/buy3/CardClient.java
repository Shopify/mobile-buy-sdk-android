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

import android.support.annotation.NonNull;

import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;

import static com.shopify.buy3.Utils.checkNotNull;

/**
 * <p>Client for card server.</p>
 * Factory for network calls to the card server.
 * Should be shared and reused for all calls to the card server.
 */
@SuppressWarnings("WeakerAccess")
public final class CardClient {
  private static final long DEFAULT_HTTP_CONNECTION_TIME_OUT_MS = TimeUnit.SECONDS.toMillis(10);
  private static final long DEFAULT_HTTP_READ_WRITE_TIME_OUT_MS = TimeUnit.SECONDS.toMillis(20);

  final Call.Factory httpCallFactory;

  /**
   * Creates client with default {@link OkHttpClient} network layer.
   */
  public CardClient() {
    this(defaultOkHttpClient());
  }

  /**
   * Creates client with provided {@link OkHttpClient}.
   *
   * @param httpCallFactory {@link Call.Factory} to be used as network layer
   */
  public CardClient(@NonNull final Call.Factory httpCallFactory) {
    this.httpCallFactory = checkNotNull(httpCallFactory, "httpCallFactory == null");
  }

  /**
   * <p>Creates a call to vault credit card on the server.</p>
   * Credit cards cannot be sent to the checkout API directly. They must be sent to the card vault which in response will return an token.
   * This token should be used for completion checkout with credit card.
   *
   * @param creditCard     {@link CreditCard} credit card info
   * @param vaultServerUrl endpoint of card vault returned in {@link Storefront.Shop#getCardVaultUrl()}
   * @return {@link CreditCardVaultCall}
   */
  public CreditCardVaultCall vault(@NonNull final CreditCard creditCard, @NonNull final String vaultServerUrl) {
    return new RealCreditCardVaultCall(creditCard, HttpUrl.parse(vaultServerUrl), httpCallFactory);
  }

  private static OkHttpClient defaultOkHttpClient() {
    return new OkHttpClient.Builder()
      .connectTimeout(DEFAULT_HTTP_CONNECTION_TIME_OUT_MS, TimeUnit.MILLISECONDS)
      .readTimeout(DEFAULT_HTTP_READ_WRITE_TIME_OUT_MS, TimeUnit.MILLISECONDS)
      .writeTimeout(DEFAULT_HTTP_READ_WRITE_TIME_OUT_MS, TimeUnit.MILLISECONDS)
      .build();
  }
}
