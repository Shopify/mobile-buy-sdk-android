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

import android.support.test.runner.AndroidJUnit4;
import android.util.Base64;

import com.google.android.gms.identity.intents.model.UserAddress;
import com.google.android.gms.wallet.FullWallet;
import com.google.android.gms.wallet.InstrumentInfo;
import com.google.android.gms.wallet.PaymentMethodToken;
import com.google.android.gms.wallet.ProxyCard;
import com.google.android.gms.wallet.WalletConstants;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.lang.reflect.Constructor;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import static com.google.common.truth.Truth.assertThat;
import static junit.framework.Assert.fail;

@RunWith(AndroidJUnit4.class)
public class PayHelperTest {
  private static final String PAYMENT_TOKEN = "paymentToken";
  private static final String PUBLIC_KEY = "androidPayPublicKey";

  @Test
  public void androidPaymentToken() throws Exception {
    final Constructor<PaymentMethodToken> paymentMethodTokenConstructor = PaymentMethodToken.class.getDeclaredConstructor(int.class,
      int.class, String.class);
    paymentMethodTokenConstructor.setAccessible(true);
    PaymentMethodToken paymentMethodToken = paymentMethodTokenConstructor.newInstance(1, 1, PAYMENT_TOKEN);

    final Constructor<FullWallet> fullWalletConstructor = FullWallet.class.getDeclaredConstructor(int.class, String.class, String.class,
      ProxyCard.class, String.class, com.google.android.gms.wallet.Address.class, com.google.android.gms.wallet.Address.class,
      String[].class, UserAddress.class, UserAddress.class, InstrumentInfo[].class, PaymentMethodToken.class);
    fullWalletConstructor.setAccessible(true);
    final FullWallet fullWallet = fullWalletConstructor.newInstance(1, null, null, null, null, null, null, null, null, null, null,
      paymentMethodToken);

    PaymentToken paymentToken = PayHelper.extractPaymentToken(fullWallet, PUBLIC_KEY);

    final MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
    final byte[] digest = messageDigest.digest(PUBLIC_KEY.getBytes("UTF-8"));
    final String androidPayPublicKeyHash = Base64.encodeToString(digest, Base64.DEFAULT);

    assertThat(paymentToken.token).isEqualTo(PAYMENT_TOKEN);
    assertThat(paymentToken.publicKeyHash).isEqualTo(androidPayPublicKeyHash);
  }

  @Test
  public void convertSupportedCardNetworks() {
    try {
      PayHelper.convertSupportedCardNetworks(null);
      fail("expected NullPointerException");
    } catch (NullPointerException expected) {
    }

    Set<Integer> cardNetworks = PayHelper.convertSupportedCardNetworks(
      new LinkedHashSet<>(Arrays.asList(CardNetworkType.VISA, CardNetworkType.MASTERCARD, CardNetworkType.DISCOVER,
        CardNetworkType.AMERICAN_EXPRESS, CardNetworkType.JCB)
      )
    );

    assertThat(new ArrayList<>(cardNetworks)).isEqualTo(Arrays.asList(WalletConstants.CardNetwork.VISA,
      WalletConstants.CardNetwork.MASTERCARD, WalletConstants.CardNetwork.DISCOVER, WalletConstants.CardNetwork.AMEX,
      WalletConstants.CardNetwork.JCB));
  }
}
