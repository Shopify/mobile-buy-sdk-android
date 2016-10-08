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
import com.shopify.buy.model.Checkout;
import com.shopify.buy.model.PaymentToken;
import com.shopify.buy.model.ShippingRate;
import com.shopify.buy.model.ShopifyObject;
import com.shopify.mobilebuysdk.demo.App;
import com.shopify.mobilebuysdk.demo.config.Constants;
import com.shopify.mobilebuysdk.demo.data.CheckoutState;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

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
    mRxSharedPreferences.register(CheckoutState.class, CheckoutState::toString, CheckoutState::from);
    mRxSharedPreferences.register(Checkout.class, ShopifyObject::toJsonString, Checkout::fromJson);
    mRxSharedPreferences.register(PaymentToken.class, gson::toJson, s -> gson.fromJson(s, PaymentToken.class));
  }

  public Observable<Cart> getCart() {
    return mRxSharedPreferences.getObject(Cart.class, Key.CART, new Cart());
  }

  public Observable<Checkout> getCheckout() {
    return mRxSharedPreferences.getObject(Checkout.class, Key.CHECKOUT, null).map(checkout -> checkout != null ? checkout.copy() : null);
  }

  public Observable<CheckoutState> getCheckoutState() {
    return mRxSharedPreferences.getObject(CheckoutState.class, Key.CHECKOUT_STATE, CheckoutState.NONE);
  }

  public Observable<CheckoutState> getLatestCheckoutState() {
    return mRxSharedPreferences.getObject(CheckoutState.class, Key.LATEST_CHECKOUT_STATE, CheckoutState.NONE);
  }

  public Observable<PaymentToken> getPaymentToken() {
    return mRxSharedPreferences.getObject(PaymentToken.class, Key.PAYMENT_TOKEN, null);
  }

  public Observable<List<ShippingRate>> getShippingRates() {
    return mRxSharedPreferences
        .getBundle(Key.SHIPPING_RATES, new Bundle())
        .map(bundle -> {
          ArrayList<String> rates = bundle.getStringArrayList(Key.SHIPPING_RATES);
          if (rates == null) {
            return null;
          }
          List<ShippingRate> shippingRates = new ArrayList<>();
          Gson gson = new Gson();
          for (String rate : rates) {
            shippingRates.add(gson.fromJson(rate, ShippingRate.class));
          }
          return shippingRates;
        });
  }

  public Observable<Cart> observeCart() {
    return mRxSharedPreferences.observeObject(Cart.class, Key.CART, new Cart());
  }

  public Observable<Checkout> observeCheckout() {
    return mRxSharedPreferences.observeObject(Checkout.class, Key.CHECKOUT, null)
        .map(checkout -> checkout != null ? checkout.copy() : null);
  }

  public Observable<CheckoutState> observeCheckoutState() {
    return mRxSharedPreferences.observeObject(CheckoutState.class, Key.CHECKOUT_STATE, CheckoutState.NONE);
  }

  public Observable<Void> setCart(Cart cart) {
    return mRxSharedPreferences.putObject(Cart.class, Key.CART, cart);
  }

  public Observable<Void> setCheckout(Checkout checkout) {
    return mRxSharedPreferences.putObject(Checkout.class, Key.CHECKOUT, checkout);
  }

  public Observable<Void> setCheckoutState(@NonNull CheckoutState state) {
    return mRxSharedPreferences.putObject(CheckoutState.class, Key.CHECKOUT_STATE, state);
  }

  public Observable<Void> setLatestCheckoutState(@NonNull CheckoutState state) {
    return getLatestCheckoutState()
        .flatMap(latestState -> {
          if (state == CheckoutState.NONE) {
            return mRxSharedPreferences.putObject(CheckoutState.class, Key.LATEST_CHECKOUT_STATE, CheckoutState.NONE);
          } else if (state.toInt() > latestState.toInt()) {
            return mRxSharedPreferences.putObject(CheckoutState.class, Key.LATEST_CHECKOUT_STATE, state);
          } else {
            return Observable.just(null);
          }
        });
  }

  public Observable<Void> setPaymentToken(PaymentToken paymentToken) {
    return mRxSharedPreferences.putObject(PaymentToken.class, Key.PAYMENT_TOKEN, paymentToken);
  }

  public Observable<Void> setShippingRates(List<ShippingRate> shippingRates) {
    return Observable.just(null)
        .flatMap(o -> {
          if (shippingRates == null) {
            return mRxSharedPreferences.putBundle(Key.SHIPPING_RATES, null);
          }
          Gson gson = new Gson();
          ArrayList<String> rates = new ArrayList<>();
          for (ShippingRate shippingRate : shippingRates) {
            rates.add(gson.toJson(shippingRate));
          }
          Bundle bundle = new Bundle();
          bundle.putStringArrayList(Key.SHIPPING_RATES, rates);
          return mRxSharedPreferences.putBundle(Key.SHIPPING_RATES, bundle);
        });
  }

  private interface Key {

    String CART = "CART";
    String CHECKOUT = "CHECKOUT";
    String CHECKOUT_STATE = "CHECKOUT_STATE";
    String LATEST_CHECKOUT_STATE = "LATEST_CHECKOUT_STATE";
    String PAYMENT_TOKEN = "PAYMENT_TOKEN";
    String SHIPPING_RATES = "SHIPPING_RATES";
  }
}
