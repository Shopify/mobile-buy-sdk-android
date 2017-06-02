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

package com.google.android.gms.identity.intents.model;

import android.support.test.runner.AndroidJUnit4;

import com.shopify.buy3.pay.PayAddress;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.lang.reflect.Constructor;

import static com.google.common.truth.Truth.assertThat;

@RunWith(AndroidJUnit4.class)
public class PayAddressTest {

  @Test
  public void userAddressToPayAddress() throws Exception {
    final Constructor<UserAddress> constructor = UserAddress.class.getDeclaredConstructor(int.class, String.class, String.class,
      String.class, String.class, String.class, String.class, String.class, String.class, String.class, String.class, String.class,
      String.class, boolean.class, String.class, String.class);
    constructor.setAccessible(true);
    UserAddress userAddress = constructor.newInstance(1, "firstName lastName", "address1", "address2", "address3", "address4", "address5",
      "administrativeArea", "locality", "countryCode", "postalCode", "sortingCode", "phoneNumber", false, "companyName", "emailAddress");

    PayAddress payAddress = PayAddress.fromUserAddress(userAddress);
    assertThat(payAddress.address1).isEqualTo("address1");
    assertThat(payAddress.address2).isEqualTo("address2, address3, address4, address5");
    assertThat(payAddress.city).isEqualTo("locality");
    assertThat(payAddress.country).isEqualTo("countryCode");
    assertThat(payAddress.firstName).isEqualTo("firstName");
    assertThat(payAddress.lastName).isEqualTo("lastName");
    assertThat(payAddress.phone).isEqualTo("phoneNumber");
    assertThat(payAddress.province).isEqualTo("administrativeArea");
    assertThat(payAddress.zip).isEqualTo("postalCode");
  }
}
