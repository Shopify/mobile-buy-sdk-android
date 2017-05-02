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

import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

import okhttp3.Call;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.shopify.buy3.Utils.checkNotNull;

final class RealCreditCardVaultCall implements CreditCardVaultCall {
  static final String ACCEPT_HEADER = "application/json";
  static final MediaType CONTENT_MEDIA_TYPE = MediaType.parse("application/json; charset=utf-8");

  final CreditCard creditCard;
  final HttpUrl endpointUrl;
  final Call.Factory httpCallFactory;
  private final AtomicBoolean executed = new AtomicBoolean();
  private volatile Call httpCall;
  private volatile boolean canceled;

  RealCreditCardVaultCall(@NonNull final CreditCard creditCard, @NonNull final HttpUrl endpointUrl, @NonNull final Call.Factory httpCallFactory) {
    this.creditCard = checkNotNull(creditCard, "creditCard == null");
    this.endpointUrl = checkNotNull(endpointUrl, "endpointUrl == null");
    this.httpCallFactory = checkNotNull(httpCallFactory, "httpCallFactory == null");
  }

  @Override public void cancel() {
    canceled = true;

    Call httpCall = this.httpCall;
    if (httpCall != null) {
      httpCall.cancel();
    }
  }

  @Override public boolean isCanceled() {
    return canceled;
  }

  @NonNull @Override public CreditCardVaultCall clone() {
    return new RealCreditCardVaultCall(creditCard, endpointUrl, httpCallFactory);
  }

  @NonNull @Override public String execute() throws IOException {
    if (!executed.compareAndSet(false, true)) {
      throw new IllegalStateException("Already Executed");
    }
    checkIfCanceled();

    String payload;
    try {
      payload = toJson(creditCard);
    } catch (JSONException e) {
      throw new IOException("Failed to serialize credit card info", e);
    }

    httpCall = httpCall(payload);
    return parseResponse(httpCall.execute());
  }

  @NonNull @Override public CreditCardVaultCall enqueue(@NonNull final Callback callback) {
    return enqueue(callback, null);
  }

  @NonNull @Override public CreditCardVaultCall enqueue(@NonNull final Callback callback, @Nullable final Handler callbackHandler) {
    checkNotNull(callback, "callback == null");

    if (checkIfCanceled(callback)) {
      return this;
    }

    String payload;
    try {
      payload = toJson(creditCard);
    } catch (JSONException e) {
      callback.onFailure(new IOException("Failed to serialize credit card info", e));
      return this;
    }

    httpCall = httpCall(payload);
    httpCall.enqueue(new OkHttpCallback(callback));

    return this;
  }

  private Call httpCall(final String payload) {
    RequestBody body = RequestBody.create(CONTENT_MEDIA_TYPE, payload);
    Request request = new Request.Builder()
      .url(endpointUrl)
      .post(body)
      .header("Accept", ACCEPT_HEADER)
      .build();
    return httpCallFactory.newCall(request);
  }

  String toJson(final CreditCard creditCard) throws JSONException {
    return new JSONObject()
      .put("credit_card", new JSONObject()
        .put("number", creditCard.number)
        .put("first_name", creditCard.firstName)
        .put("last_name", creditCard.lastName)
        .put("month", creditCard.expireMonth)
        .put("year", creditCard.expireYear)
        .put("verification_value", creditCard.verificationCode)
      ).toString();
  }

  private void checkIfCanceled() throws IOException {
    if (canceled) {
      throw new IOException("Canceled");
    }
  }

  private boolean checkIfCanceled(final Callback callback) {
    if (canceled) {
      callback.onFailure(new IOException("Canceled"));
      return true;
    } else {
      return false;
    }
  }

  private String parseResponse(final Response response) throws IOException {
    checkIfCanceled();
    return extractToken(response);
  }

  private final class OkHttpCallback implements okhttp3.Callback {
    final Callback callback;

    OkHttpCallback(final Callback callback) {
      this.callback = callback;
    }

    @Override public void onFailure(final Call call, final IOException e) {
      if (checkIfCanceled(callback)) {
        return;
      }
      callback.onFailure(e);
    }

    @Override public void onResponse(final Call call, final Response response) throws IOException {
      try {
        callback.onResponse(parseResponse(response));
      } catch (IOException e) {
        callback.onFailure(e);
      }
    }
  }

  private static String formatMessage(Response response) {
    return "HTTP " + response.code() + " " + response.message();
  }

  private String extractToken(final Response response) throws IOException {
    if (response.isSuccessful()) {
      try {
        JSONObject responseJsonObject = new JSONObject(response.body().string());
        return responseJsonObject.getString("id");
      } catch (Exception e) {
        throw new IOException("Failed to parse vault response", e);
      }
    } else {
      throw new IOException("Failed to vault credit card, " + formatMessage(response));
    }
  }
}
