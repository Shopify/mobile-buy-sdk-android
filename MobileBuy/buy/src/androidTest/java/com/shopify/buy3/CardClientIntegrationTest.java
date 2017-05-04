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
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;

import static com.google.common.truth.Truth.assertThat;
import static junit.framework.Assert.fail;

@RunWith(AndroidJUnit4.class)
public class CardClientIntegrationTest {
  private CardClient cardClient = new CardClient();
  @Rule public MockWebServer server = new MockWebServer();

  @Test public void realCardVaultCallInstantiatedCorrectly() {
    CreditCard creditCard = provideCreditCard();
    RealCreditCardVaultCall call = (RealCreditCardVaultCall) cardClient.vault(creditCard, "http://google.com");

    assertThat(call.creditCard).isEqualTo(creditCard);
    assertThat(call.endpointUrl.toString()).isEqualTo("http://google.com/");
    assertThat(call.httpCallFactory).isEqualTo(cardClient.httpCallFactory);
  }

  @Test public void realCardVaultCallClone() {
    CreditCard creditCard = provideCreditCard();
    RealCreditCardVaultCall call = (RealCreditCardVaultCall) cardClient.vault(creditCard, "http://google.com");
    RealCreditCardVaultCall clonedCall = (RealCreditCardVaultCall) call.clone();

    assertThat(clonedCall).isNotEqualTo(call);
    assertThat(clonedCall.creditCard).isEqualTo(call.creditCard);
    assertThat(clonedCall.endpointUrl).isEqualTo(call.endpointUrl);
    assertThat(clonedCall.httpCallFactory).isEqualTo(call.httpCallFactory);
  }

  @Test public void vaultCreditCardSuccess() throws Exception {
    server.enqueue(new MockResponse().setResponseCode(200).setBody("{\"id\": \"83hru3obno3hu434b3u\"}"));

    CreditCard creditCard = provideCreditCard();
    String token = cardClient.vault(creditCard, server.url("/").toString()).execute();

    RecordedRequest recordedRequest = server.takeRequest();
    assertThat(recordedRequest.getHeader("Accept")).isEqualTo(RealCreditCardVaultCall.ACCEPT_HEADER);
    assertThat(recordedRequest.getHeader("Content-Type")).isEqualTo(RealCreditCardVaultCall.CONTENT_MEDIA_TYPE.toString());
    assertThat(recordedRequest.getBody().readUtf8()).isEqualTo("{\"credit_card\":{\"number\":\"1\",\"first_name\":\"John\","
      + "\"last_name\":\"Smith\",\"month\":\"06\",\"year\":\"2017\",\"verification_value\":\"111\"}}");
    assertThat(token).isEqualTo("83hru3obno3hu434b3u");
  }

  @Test public void vaultCreditCardSuccessAsync() throws Exception {
    server.enqueue(new MockResponse().setResponseCode(200).setBody("{\"id\": \"83hru3obno3hu434b3u\"}"));

    CreditCard creditCard = provideCreditCard();

    AtomicBoolean executedInCallbackHandler = new AtomicBoolean();
    NamedCountDownLatch countDownLatch = new NamedCountDownLatch("invalidResponseErrorAsync", 1);
    AtomicReference<String> tokenRef = new AtomicReference<>();
    cardClient.vault(creditCard, server.url("/").toString()).enqueue(new CreditCardVaultCall.Callback() {
      @Override public void onResponse(@NonNull final String token) {
        tokenRef.set(token);
        countDownLatch.countDown();
      }

      @Override public void onFailure(@NonNull final IOException error) {
        countDownLatch.countDown();
      }
    }, mockCallbackHandler(executedInCallbackHandler));

    countDownLatch.await(2, TimeUnit.SECONDS);
    assertThat(tokenRef.get()).isEqualTo("83hru3obno3hu434b3u");
  }

  @Test public void vaultCreditCardFail() throws Exception {
    server.enqueue(new MockResponse().setResponseCode(400));

    CreditCard creditCard = provideCreditCard();

    try {
      cardClient.vault(creditCard, server.url("/").toString()).execute();
      fail("Expected exception");
    } catch (Exception expected) {
      // expected
    }

    server.enqueue(new MockResponse().setResponseCode(200).setBody(""));
    try {
      cardClient.vault(creditCard, server.url("/").toString()).execute();
      fail("Expected exception");
    } catch (Exception expected) {
      // expected
    }
  }

  @Test public void vaultCreditCardFailAsync() throws Exception {
    server.enqueue(new MockResponse().setResponseCode(500));

    CreditCard creditCard = provideCreditCard();

    AtomicBoolean executedInCallbackHandler = new AtomicBoolean();
    NamedCountDownLatch countDownLatch = new NamedCountDownLatch("invalidResponseErrorAsync", 1);
    AtomicReference<IOException> errorRef = new AtomicReference<>();
    cardClient.vault(creditCard, server.url("/").toString()).enqueue(new CreditCardVaultCall.Callback() {
      @Override public void onResponse(@NonNull final String token) {
        countDownLatch.countDown();
      }

      @Override public void onFailure(@NonNull final IOException error) {
        errorRef.set(error);
        countDownLatch.countDown();
      }
    }, mockCallbackHandler(executedInCallbackHandler));

    countDownLatch.await(2, TimeUnit.SECONDS);
    assertThat(errorRef.get()).isNotNull();
  }


  private static Looper createBackgroundLooper() throws Exception {
    final AtomicReference<Looper> looperRef = new AtomicReference<>();
    new Thread("CallbackThread") {
      @Override public void run() {
        Looper.prepare();
        synchronized (this) {
          looperRef.set(Looper.myLooper());
          notifyAll();
        }
        Looper.loop();
      }
    }.start();
    Thread.sleep(200);
    return looperRef.get();
  }

  private static Handler mockCallbackHandler(final AtomicBoolean invokeTracker) throws Exception {
    return new Handler(createBackgroundLooper()) {
      @Override public boolean sendMessageAtTime(Message msg, long uptimeMillis) {
        invokeTracker.set(true);
        msg.getCallback().run();
        return true;
      }
    };
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
