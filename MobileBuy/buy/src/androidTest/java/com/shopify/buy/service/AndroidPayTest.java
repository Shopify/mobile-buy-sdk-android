/*
 *
 * The MIT License (MIT)
 *
 * Copyright (c) 2016 Shopify Inc.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 *
 */

package com.shopify.buy.service;


import com.shopify.buy.extensions.ShopifyAndroidTestCase;
import com.shopify.buy.model.Checkout;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class AndroidPayTest extends ShopifyAndroidTestCase {


    public void testEnableAndroidPay() throws UnsupportedEncodingException, NoSuchAlgorithmException {
        assertEquals(false, buyClient.androidPayIsEnabled());

        buyClient.enableAndroidPay("somepublickey");
        assertEquals(true, buyClient.androidPayIsEnabled());

        buyClient.disableAndroidPay();
        assertEquals(false, buyClient.androidPayIsEnabled());

        try {
            buyClient.enableAndroidPay(null);
            fail("Should have Thrown Illegal Argument Exception");
        } catch (IllegalArgumentException i) {
            assertEquals(false, buyClient.androidPayIsEnabled());
        }

        try {
            buyClient.enableAndroidPay("");
            fail("Should have Thrown Illegal Argument Exception");
        } catch (IllegalArgumentException i) {
            assertEquals(false, buyClient.androidPayIsEnabled());
        }
    }

    public void testCompleteCheckout() throws UnsupportedEncodingException{
        assertEquals(false, buyClient.androidPayIsEnabled());

        String androidPayToken = "sometoken";
        String androidPayPublicKey = "somekey";

        Checkout checkout = new Checkout();
        Callback<Checkout> callback = new Callback<Checkout>() {
            @Override
            public void success(Checkout checkout, Response response) {

            }

            @Override
            public void failure(RetrofitError error) {

            }
        };

        try {
            buyClient.completeCheckout(androidPayToken, checkout, callback);
            fail("Should have thrown an UnsupportedOperationException");
        } catch (Exception e) {}

        buyClient.enableAndroidPay(androidPayPublicKey);

        try {
            buyClient.completeCheckout(null, checkout, callback);
            fail("Should have thrown an IllegalArgumentException");
        } catch (IllegalArgumentException e) {}

        try {
            buyClient.completeCheckout("", checkout, callback);
            fail("Should have thrown an IllegalArgumentException");
        } catch (IllegalArgumentException e) {}

        try {
            buyClient.completeCheckout(androidPayToken, null, callback);
            fail("Should have thrown a NullPointerException");
        } catch (NullPointerException e) {}

    }
}
