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

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import com.shopify.sample.domain.model.Cart;
import com.shopify.sample.domain.model.Checkout;
import com.shopify.sample.view.LifeCycleBoundCallback;
import com.shopify.sample.view.ViewModel;

import java.util.UUID;

@SuppressWarnings("WeakerAccess")
public interface CartDetailsViewModel extends ViewModel {
  int REQUEST_ID_UPDATE_CART = UUID.randomUUID().hashCode();
  int REQUEST_ID_CREATE_WEB_CHECKOUT = UUID.randomUUID().hashCode();
  int REQUEST_ID_CREATE_ANDROID_PAY_CHECKOUT = UUID.randomUUID().hashCode();
  int REQUEST_ID_PREPARE_ANDROID_PAY = UUID.randomUUID().hashCode();

  void onGoogleApiClientConnectionChanged(boolean connected);

  LifeCycleBoundCallback<Checkout> webCheckoutCallback();

  LifeCycleBoundCallback<CartDetailsViewModel.AndroidPayCheckout> androidPayCheckoutCallback();

  LifeCycleBoundCallback<Cart> androidPayStartCheckoutCallback();

//  void handleMaskedWalletResponse(int requestCode, int resultCode, @Nullable Intent data);

  Bundle saveState();

  void restoreState(Bundle bundle);

  final class AndroidPayCheckout {
    public final String checkoutId;
    public final Cart payCart;

    AndroidPayCheckout(final String checkoutId, final Cart payCart) {
      this.checkoutId = checkoutId;
      this.payCart = payCart;
    }
  }
}
