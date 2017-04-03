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

package com.shopify.sample.view.cart;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.shopify.buy3.pay.PayCart;
import com.shopify.sample.view.ScreenActionEvent;

import static com.shopify.sample.util.Util.checkNotBlank;
import static com.shopify.sample.util.Util.checkNotNull;

@SuppressWarnings("WeakerAccess")
public final class AndroidPayConfirmationClickActionEvent extends ScreenActionEvent implements Parcelable {
  public static final Creator<AndroidPayConfirmationClickActionEvent> CREATOR = new Creator<AndroidPayConfirmationClickActionEvent>() {
    @Override
    public AndroidPayConfirmationClickActionEvent createFromParcel(Parcel in) {
      return new AndroidPayConfirmationClickActionEvent(in);
    }

    @Override
    public AndroidPayConfirmationClickActionEvent[] newArray(int size) {
      return new AndroidPayConfirmationClickActionEvent[size];
    }
  };

  public static final String ACTION = AndroidPayConfirmationClickActionEvent.class.getSimpleName();
  public static final String EXTRAS_CHECKOUT_ID = "checkout_id";
  public static final String EXTRAS_PAY_CART = "pay_cart";

  public AndroidPayConfirmationClickActionEvent(@NonNull final String checkoutId, @NonNull final PayCart payCart) {
    super(ACTION);
    payload.putString(EXTRAS_CHECKOUT_ID, checkNotBlank(checkoutId, "checkoutId can't be blank"));
    payload.putParcelable(EXTRAS_PAY_CART, checkNotNull(payCart, "payCart == null"));
  }

  @SuppressWarnings("WeakerAccess") AndroidPayConfirmationClickActionEvent(Parcel in) {
    super(in);
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    super.writeToParcel(dest, flags);
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @SuppressWarnings("ConstantConditions") @NonNull public String checkoutId() {
    return payload().getString(EXTRAS_CHECKOUT_ID);
  }

  @SuppressWarnings("ConstantConditions") @NonNull public PayCart payCart() {
    return payload().getParcelable(EXTRAS_PAY_CART);
  }
}
