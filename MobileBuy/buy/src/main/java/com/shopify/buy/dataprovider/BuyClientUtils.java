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
package com.shopify.buy.dataprovider;

import android.util.Base64;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.shopify.buy.model.Checkout;
import com.shopify.buy.model.Product;
import com.shopify.buy.utils.DateUtility;

import java.nio.charset.Charset;
import java.util.Date;

public final class BuyClientUtils {

    public static Gson createDefaultGson() {
        return createDefaultGson(null);
    }

    public static Gson createDefaultGson(Class forClass) {

        GsonBuilder builder = new GsonBuilder()
                .setDateFormat(DateUtility.DEFAULT_DATE_PATTERN)
                .registerTypeAdapter(Date.class, new DateUtility.DateDeserializer());

        if (!Product.class.equals(forClass)) {
            builder.registerTypeAdapter(Product.class, new Product.ProductDeserializer());
        }

        if (!Checkout.class.equals(forClass)) {
            builder.registerTypeAdapter(Checkout.class, new Checkout.CheckoutSerializer());
            builder.registerTypeAdapter(Checkout.class, new Checkout.CheckoutDeserializer());
        }

        return builder.create();
    }

    public static String formatBasicAuthorization(final String token) {
        return String.format("Basic %s", Base64.encodeToString(token.getBytes(Charset.forName("UTF-8")), Base64.NO_WRAP));
    }

    private BuyClientUtils() {
    }
}
