/*
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

package com.shopify.mobilebuysdk.demo.data;

import android.util.SparseArray;

/**
 * Created by henrytao on 9/18/16.
 */
public enum CheckoutState {
  NONE(0),
  PAYMENT_METHOD(1),
  SHIPPING_ADDRESS(2),
  SHIPPING_RATES(3),
  SUMMARY_BEFORE_PAYMENT(4),
  PROCESSING(5),
  PAYMENT_SUCCESS(6);

  private static final SparseArray<CheckoutState> sCaches = new SparseArray<>();

  static {
    for (CheckoutState state : CheckoutState.values()) {
      sCaches.put(state.value, state);
    }
  }

  public static CheckoutState from(String value) {
    try {
      return sCaches.get(Integer.valueOf(value), NONE);
    } catch (Exception ignore) {
      return NONE;
    }
  }

  private final int value;

  CheckoutState(int value) {
    this.value = value;
  }

  @Override
  public String toString() {
    return String.valueOf(value);
  }
}