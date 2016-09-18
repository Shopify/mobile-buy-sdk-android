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

package com.shopify.mobilebuysdk.demo.service;

import com.google.gson.Gson;

import com.shopify.buy.model.Cart;
import com.shopify.mobilebuysdk.demo.App;
import com.shopify.mobilebuysdk.demo.config.Constants;

import android.content.Context;

import me.henrytao.rxsharedpreferences.RxSharedPreferences;
import rx.Observable;

/**
 * Created by henrytao on 9/16/16.
 */
public class StorageService {

  private static StorageService sInstance;

  // TODO: should use DI instead
  public static StorageService getInstance() {
    if (sInstance == null) {
      synchronized (StorageService.class) {
        if (sInstance == null) {
          sInstance = new StorageService(App.getInstance());
        }
      }
    }
    return sInstance;
  }

  private final RxSharedPreferences mRxSharedPreferences;

  private StorageService(Context context) {
    Gson gson = new Gson();
    mRxSharedPreferences = new RxSharedPreferences(context.getSharedPreferences(Constants.Preferences.KEY, Constants.Preferences.MODE));
    mRxSharedPreferences.register(Cart.class, gson::toJson, s -> gson.fromJson(s, Cart.class));
  }

  public Observable<Cart> getCart() {
    return mRxSharedPreferences.getObject(Cart.class, Key.CART, new Cart());
  }

  public Observable<Cart> observeCart() {
    return mRxSharedPreferences.observeObject(Cart.class, Key.CART, new Cart());
  }

  public Observable<Void> setCart(Cart cart) {
    return mRxSharedPreferences.putObject(Cart.class, Key.CART, cart);
  }

  private interface Key {

    String CART = "CART";
  }
}
