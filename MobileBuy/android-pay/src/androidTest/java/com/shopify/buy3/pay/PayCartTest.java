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

import com.google.android.gms.identity.intents.model.UserAddress;
import com.google.android.gms.wallet.FullWalletRequest;
import com.google.android.gms.wallet.LineItem;
import com.google.android.gms.wallet.MaskedWallet;
import com.google.android.gms.wallet.MaskedWalletRequest;
import com.google.android.gms.wallet.PaymentMethodTokenizationType;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.lang.reflect.Constructor;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;

import static com.google.common.truth.Truth.assertThat;
import static junit.framework.Assert.fail;

@RunWith(AndroidJUnit4.class)
public class PayCartTest {

  @Test public void preconditions() throws Exception {
    final PayCart.Builder builder = PayCart.builder();
    testNullPointerException(() -> builder.merchantName(null));
    testIllegalArgumentException(() -> builder.merchantName(""));
    testNullPointerException(() -> builder.currencyCode(null));
    testIllegalArgumentException(() -> builder.currencyCode(""));
    testNullPointerException(() -> builder.addLineItem(null, 1, BigDecimal.ZERO));
    testIllegalArgumentException(() -> builder.addLineItem("", 1, BigDecimal.ZERO));
    testIllegalArgumentException(() -> builder.addLineItem("title", -1, BigDecimal.ZERO));
    testIllegalArgumentException(() -> builder.addLineItem("title", 0, BigDecimal.ZERO));
    testNullPointerException(() -> builder.addLineItem("title", 1, null));
    testNullPointerException(() -> builder.subtotal(null));
    testNullPointerException(() -> builder.totalPrice(null));

    testNullPointerException(() -> PayCart.builder().build());
    testNullPointerException(() -> PayCart.builder().merchantName("merchantName").build());
  }

  @Test public void successBuild() throws Exception {
    PayCart payCart = PayCart.builder()
      .merchantName("merchantName")
      .currencyCode("UAH")
      .shippingAddressRequired(true)
      .addLineItem("lineItem1", 10, BigDecimal.valueOf(1.09))
      .addLineItem("lineItem2", 20, BigDecimal.valueOf(2.99))
      .phoneNumberRequired(true)
      .shipsToCountries(Arrays.asList("US, CA, UA"))
      .shippingPrice(BigDecimal.valueOf(3.01))
      .taxPrice(BigDecimal.valueOf(2.01))
      .subtotal(BigDecimal.valueOf(29.01))
      .totalPrice(BigDecimal.valueOf(100.99))
      .build();

    assertThat(payCart.merchantName).isEqualTo("merchantName");
    assertThat(payCart.currencyCode).isEqualTo("UAH");
    assertThat(payCart.shippingAddressRequired).isTrue();
    assertThat(payCart.lineItems).hasSize(2);

    assertThat(payCart.lineItems.get(0).getDescription()).isEqualTo("lineItem1");
    assertThat(payCart.lineItems.get(0).getQuantity()).isEqualTo("10");
    assertThat(payCart.lineItems.get(0).getUnitPrice()).isEqualTo("1.09");
    assertThat(payCart.lineItems.get(0).getTotalPrice()).isEqualTo("10.90");
    assertThat(payCart.lineItems.get(0).getCurrencyCode()).isEqualTo("UAH");
    assertThat(payCart.lineItems.get(0).getRole()).isEqualTo(LineItem.Role.REGULAR);

    assertThat(payCart.lineItems.get(1).getDescription()).isEqualTo("lineItem2");
    assertThat(payCart.lineItems.get(1).getQuantity()).isEqualTo("20");
    assertThat(payCart.lineItems.get(1).getUnitPrice()).isEqualTo("2.99");
    assertThat(payCart.lineItems.get(1).getTotalPrice()).isEqualTo("59.80");
    assertThat(payCart.lineItems.get(1).getCurrencyCode()).isEqualTo("UAH");
    assertThat(payCart.lineItems.get(1).getRole()).isEqualTo(LineItem.Role.REGULAR);

    assertThat(payCart.phoneNumberRequired).isTrue();
    assertThat(payCart.shipsToCountries).isEqualTo(Arrays.asList("US, CA, UA"));
    assertThat(payCart.shippingPrice).isEqualTo(BigDecimal.valueOf(3.01));
    assertThat(payCart.taxPrice).isEqualTo(BigDecimal.valueOf(2.01));
    assertThat(payCart.subtotal).isEqualTo(BigDecimal.valueOf(29.01));
    assertThat(payCart.totalPrice).isEqualTo(BigDecimal.valueOf(100.99));
  }

  @Test public void shipsToCountries() throws Exception {
    PayCart payCart = PayCart.builder()
      .merchantName("merchantName")
      .currencyCode("UAH")
      .build();

    assertThat(payCart.shipsToCountries).hasSize(1);
    assertThat(payCart.shipsToCountries.get(0)).isEqualTo("*");

    payCart = PayCart.builder()
      .merchantName("merchantName")
      .currencyCode("UAH")
      .shipsToCountries(null)
      .build();

    assertThat(payCart.shipsToCountries).hasSize(1);
    assertThat(payCart.shipsToCountries.get(0)).isEqualTo("*");

    payCart = PayCart.builder()
      .merchantName("merchantName")
      .currencyCode("UAH")
      .shipsToCountries(Collections.emptyList())
      .build();

    assertThat(payCart.shipsToCountries).hasSize(1);
    assertThat(payCart.shipsToCountries.get(0)).isEqualTo("*");
  }

  @Test public void subtotal() throws Exception {
    PayCart payCart = PayCart.builder()
      .merchantName("merchantName")
      .currencyCode("UAH")
      .addLineItem("lineItem1", 10, BigDecimal.valueOf(1.09))
      .addLineItem("lineItem2", 15, BigDecimal.valueOf(2.91))
      .addLineItem("lineItem3", 30, BigDecimal.valueOf(3.09))
      .addLineItem("lineItem4", 40, BigDecimal.valueOf(2.01))
      .shippingPrice(BigDecimal.valueOf(29.01))
      .taxPrice(BigDecimal.valueOf(10.05))
      .build();

    assertThat(payCart.lineItems).hasSize(4);
    assertThat(payCart.subtotal).isEqualTo(BigDecimal.valueOf(227.65));
    assertThat(payCart.totalPrice).isEqualTo(BigDecimal.valueOf(266.71));
  }

  @Test public void maskedWalletRequest() {
    PayCart payCart = PayCart.builder()
      .merchantName("merchantName")
      .currencyCode("UAH")
      .shippingAddressRequired(true)
      .addLineItem("lineItem1", 10, BigDecimal.valueOf(1.09))
      .addLineItem("lineItem2", 20, BigDecimal.valueOf(2.99))
      .phoneNumberRequired(true)
      .shipsToCountries(Arrays.asList("US, CA, UA"))
      .shippingPrice(BigDecimal.valueOf(3.01))
      .taxPrice(BigDecimal.valueOf(2.01))
      .subtotal(BigDecimal.valueOf(29.01))
      .totalPrice(BigDecimal.valueOf(100.99))
      .build();

    MaskedWalletRequest maskedWalletRequest = payCart.maskedWalletRequest("androidPublicKey");
    assertThat(maskedWalletRequest.getMerchantName()).isEqualTo("merchantName");
    assertThat(maskedWalletRequest.isPhoneNumberRequired()).isTrue();
    assertThat(maskedWalletRequest.isShippingAddressRequired()).isTrue();
    assertThat(maskedWalletRequest.getCurrencyCode()).isEqualTo("UAH");
    assertThat(maskedWalletRequest.getEstimatedTotalPrice()).isEqualTo("100.99");
    assertThat(maskedWalletRequest.getPaymentMethodTokenizationParameters().getPaymentMethodTokenizationType())
      .isEqualTo(PaymentMethodTokenizationType.NETWORK_TOKEN);
    assertThat(maskedWalletRequest.getPaymentMethodTokenizationParameters().getParameters().getString("publicKey"))
      .isEqualTo("androidPublicKey");

    assertThat(maskedWalletRequest.getAllowedCountrySpecificationsForShipping().get(0).getCountryCode()).isEqualTo("US, CA, UA");
    assertThat(maskedWalletRequest.getCart().getCurrencyCode()).isEqualTo("UAH");
    assertThat(maskedWalletRequest.getCart().getTotalPrice()).isEqualTo("100.99");
    assertThat(maskedWalletRequest.getCart().getLineItems()).hasSize(4);

    assertThat(maskedWalletRequest.getCart().getLineItems().get(0).getDescription()).isEqualTo("lineItem1");
    assertThat(maskedWalletRequest.getCart().getLineItems().get(0).getQuantity()).isEqualTo("10");
    assertThat(maskedWalletRequest.getCart().getLineItems().get(0).getUnitPrice()).isEqualTo("1.09");
    assertThat(maskedWalletRequest.getCart().getLineItems().get(0).getTotalPrice()).isEqualTo("10.90");
    assertThat(maskedWalletRequest.getCart().getLineItems().get(0).getCurrencyCode()).isEqualTo("UAH");
    assertThat(maskedWalletRequest.getCart().getLineItems().get(0).getRole()).isEqualTo(LineItem.Role.REGULAR);

    assertThat(maskedWalletRequest.getCart().getLineItems().get(1).getDescription()).isEqualTo("lineItem2");
    assertThat(maskedWalletRequest.getCart().getLineItems().get(1).getQuantity()).isEqualTo("20");
    assertThat(maskedWalletRequest.getCart().getLineItems().get(1).getUnitPrice()).isEqualTo("2.99");
    assertThat(maskedWalletRequest.getCart().getLineItems().get(1).getTotalPrice()).isEqualTo("59.80");
    assertThat(maskedWalletRequest.getCart().getLineItems().get(1).getCurrencyCode()).isEqualTo("UAH");
    assertThat(maskedWalletRequest.getCart().getLineItems().get(1).getRole()).isEqualTo(LineItem.Role.REGULAR);

    assertThat(maskedWalletRequest.getCart().getLineItems().get(2).getTotalPrice()).isEqualTo("2.01");
    assertThat(maskedWalletRequest.getCart().getLineItems().get(2).getCurrencyCode()).isEqualTo("UAH");
    assertThat(maskedWalletRequest.getCart().getLineItems().get(2).getRole()).isEqualTo(LineItem.Role.TAX);

    assertThat(maskedWalletRequest.getCart().getLineItems().get(3).getTotalPrice()).isEqualTo("3.01");
    assertThat(maskedWalletRequest.getCart().getLineItems().get(3).getCurrencyCode()).isEqualTo("UAH");
    assertThat(maskedWalletRequest.getCart().getLineItems().get(3).getRole()).isEqualTo(LineItem.Role.SHIPPING);
  }

  @Test public void fullWalletRequest() throws Exception {
    PayCart payCart = PayCart.builder()
      .merchantName("merchantName")
      .currencyCode("UAH")
      .shippingAddressRequired(true)
      .addLineItem("lineItem1", 10, BigDecimal.valueOf(1.09))
      .addLineItem("lineItem2", 20, BigDecimal.valueOf(2.99))
      .phoneNumberRequired(true)
      .shipsToCountries(Arrays.asList("US, CA, UA"))
      .shippingPrice(BigDecimal.valueOf(3.01))
      .taxPrice(BigDecimal.valueOf(2.01))
      .subtotal(BigDecimal.valueOf(29.01))
      .totalPrice(BigDecimal.valueOf(100.99))
      .build();

    MaskedWallet maskedWallet = createMaskedWallet(createWalletUserAddress(), createWalletUserAddress(), "test@test.com",
      "googleTransactionId");
    FullWalletRequest fullWalletRequest = payCart.fullWalletRequest(maskedWallet);

    assertThat(fullWalletRequest.getGoogleTransactionId()).isEqualTo("googleTransactionId");

    assertThat(fullWalletRequest.getCart().getCurrencyCode()).isEqualTo("UAH");
    assertThat(fullWalletRequest.getCart().getTotalPrice()).isEqualTo("100.99");
    assertThat(fullWalletRequest.getCart().getLineItems()).hasSize(4);

    assertThat(fullWalletRequest.getCart().getLineItems().get(0).getDescription()).isEqualTo("lineItem1");
    assertThat(fullWalletRequest.getCart().getLineItems().get(0).getQuantity()).isEqualTo("10");
    assertThat(fullWalletRequest.getCart().getLineItems().get(0).getUnitPrice()).isEqualTo("1.09");
    assertThat(fullWalletRequest.getCart().getLineItems().get(0).getTotalPrice()).isEqualTo("10.90");
    assertThat(fullWalletRequest.getCart().getLineItems().get(0).getCurrencyCode()).isEqualTo("UAH");
    assertThat(fullWalletRequest.getCart().getLineItems().get(0).getRole()).isEqualTo(LineItem.Role.REGULAR);

    assertThat(fullWalletRequest.getCart().getLineItems().get(1).getDescription()).isEqualTo("lineItem2");
    assertThat(fullWalletRequest.getCart().getLineItems().get(1).getQuantity()).isEqualTo("20");
    assertThat(fullWalletRequest.getCart().getLineItems().get(1).getUnitPrice()).isEqualTo("2.99");
    assertThat(fullWalletRequest.getCart().getLineItems().get(1).getTotalPrice()).isEqualTo("59.80");
    assertThat(fullWalletRequest.getCart().getLineItems().get(1).getCurrencyCode()).isEqualTo("UAH");
    assertThat(fullWalletRequest.getCart().getLineItems().get(1).getRole()).isEqualTo(LineItem.Role.REGULAR);

    assertThat(fullWalletRequest.getCart().getLineItems().get(2).getTotalPrice()).isEqualTo("2.01");
    assertThat(fullWalletRequest.getCart().getLineItems().get(2).getCurrencyCode()).isEqualTo("UAH");
    assertThat(fullWalletRequest.getCart().getLineItems().get(2).getRole()).isEqualTo(LineItem.Role.TAX);

    assertThat(fullWalletRequest.getCart().getLineItems().get(3).getTotalPrice()).isEqualTo("3.01");
    assertThat(fullWalletRequest.getCart().getLineItems().get(3).getCurrencyCode()).isEqualTo("UAH");
    assertThat(fullWalletRequest.getCart().getLineItems().get(3).getRole()).isEqualTo(LineItem.Role.SHIPPING);
  }

  private static void testNullPointerException(Runnable runnable) throws Exception {
    try {
      runnable.run();
      fail("expected NullPointerException");
    } catch (NullPointerException expected) {
      // expected
    }
  }

  private static void testIllegalArgumentException(Runnable runnable) throws Exception {
    try {
      runnable.run();
      fail("expected IllegalArgumentException");
    } catch (IllegalArgumentException expected) {
      // expected
    }
  }

  private static MaskedWallet createMaskedWallet(final UserAddress shippingAddress, final UserAddress billingAddress, final String email,
    final String googleTransactionId) throws Exception {
    final Constructor<MaskedWallet> maskedWalletConstructor = MaskedWallet.class.getDeclaredConstructor();
    maskedWalletConstructor.setAccessible(true);

    final Constructor<MaskedWallet.Builder> maskedWalletBuilderConstructor =
      (Constructor<MaskedWallet.Builder>) Class.forName("com.google.android.gms.wallet.MaskedWallet$Builder")
        .getDeclaredConstructor(MaskedWallet.class);
    maskedWalletBuilderConstructor.setAccessible(true);

    final MaskedWallet.Builder maskedWalletBuilder = maskedWalletBuilderConstructor.newInstance(maskedWalletConstructor.newInstance());
    maskedWalletBuilder.setBuyerShippingAddress(shippingAddress);
    maskedWalletBuilder.setBuyerBillingAddress(billingAddress);
    maskedWalletBuilder.setEmail(email);
    maskedWalletBuilder.setGoogleTransactionId(googleTransactionId);

    return maskedWalletBuilder.build();
  }

  private UserAddress createWalletUserAddress() throws Exception {
    final Constructor<UserAddress> constructor = UserAddress.class.getDeclaredConstructor(int.class, String.class, String.class,
      String.class, String.class, String.class, String.class, String.class, String.class, String.class, String.class, String.class,
      String.class, boolean.class, String.class, String.class);
    constructor.setAccessible(true);
    return constructor.newInstance(1, "firstName lastName", "address1", "address2", "address3", "address4", "address5",
      "administrativeArea", "locality", "countryCode", "postalCode", "sortingCode", "phoneNumber", false, "companyName", "emailAddress");
  }
}
