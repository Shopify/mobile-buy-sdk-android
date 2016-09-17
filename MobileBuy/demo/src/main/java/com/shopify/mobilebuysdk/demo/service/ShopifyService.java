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

import com.shopify.buy.dataprovider.BuyClient;
import com.shopify.buy.dataprovider.BuyClientBuilder;
import com.shopify.buy.model.Cart;
import com.shopify.buy.model.Checkout;
import com.shopify.buy.model.Product;
import com.shopify.buy.model.ProductVariant;
import com.shopify.buy.model.Shop;
import com.shopify.mobilebuysdk.demo.App;
import com.shopify.mobilebuysdk.demo.BuildConfig;
import com.shopify.mobilebuysdk.demo.R;
import com.shopify.mobilebuysdk.demo.util.StringUtils;

import android.annotation.SuppressLint;
import android.content.Context;

import java.util.List;
import java.util.Locale;

import rx.Observable;
import rx.subjects.BehaviorSubject;
import rx.subjects.PublishSubject;

/**
 * Created by henrytao on 8/27/16.
 */
public class ShopifyService {

  @SuppressLint("StaticFieldLeak")
  private static ShopifyService sInstance;

  // TODO: should use DI instead
  public static ShopifyService getInstance() {
    if (sInstance == null) {
      synchronized (ShopifyService.class) {
        if (sInstance == null) {
          sInstance = new ShopifyService(App.getInstance(), StorageService.getInstance());
        }
      }
    }
    return sInstance;
  }

  private final BuyClient mBuyClient;

  private final Cart mCart;

  private final PublishSubject<Void> mCartChangeSubject = PublishSubject.create();

  private final BehaviorSubject<Integer> mCartQuantitySubject = BehaviorSubject.create(0);

  private final BehaviorSubject<Double> mCartSubtotalSubject = BehaviorSubject.create(0d);

  private final Context mContext;

  private final String mPackageName;

  private final StorageService mStorageService;

  private Checkout mCheckout;

  private ShopifyService(Context context, StorageService storageService) {
    mStorageService = storageService;
    mPackageName = context.getPackageName();
    mContext = context.getApplicationContext();
    if (StringUtils.isEmpty(BuildConfig.SHOP_DOMAIN) ||
        StringUtils.isEmpty(BuildConfig.API_KEY) ||
        StringUtils.isEmpty(BuildConfig.APP_ID)) {
      throw new IllegalStateException("You must add 'SHOP_DOMAIN', 'API_KEY', 'APP_ID' entries in app/shop.properties");
    }
    mBuyClient = new BuyClientBuilder()
        .shopDomain(BuildConfig.SHOP_DOMAIN)
        .apiKey(BuildConfig.API_KEY)
        .appId(BuildConfig.APP_ID)
        .applicationName(mPackageName)
        .build();
    mCart = mStorageService.getCart().toBlocking().first();
  }

  public void addToCart(ProductVariant productVariant) {
    mCart.addVariant(productVariant);
    mCartQuantitySubject.onNext(mCart.getTotalQuantity());
    mCartChangeSubject.onNext(null);
    mCartSubtotalSubject.onNext(mCart.getSubtotal());
    mStorageService.setCart(mCart);
  }

  public Observable<Checkout> createCheckout() {
    if (mCheckout != null) {
      return Observable.just(mCheckout);
    }
    Checkout checkout = new Checkout(mCart);
    checkout.setWebReturnToUrl(String.format(Locale.US, "%s://%s%s",
        mContext.getString(R.string.appLink_scheme),
        mPackageName,
        mContext.getString(R.string.appLink_path_callback)));
    checkout.setWebReturnToLabel(mContext.getString(R.string.text_return_to_app));
    return mBuyClient.createCheckout(checkout).doOnNext(co -> mCheckout = co);
  }

  public Cart getCart() {
    return mCart;
  }

  public Observable<List<Product>> getProducts() {
    return mBuyClient.getProducts(1);
  }

  public Observable<Shop> getShop() {
    return mBuyClient.getShop();
  }

  public Observable<Void> observeCartChange() {
    return mCartChangeSubject;
  }

  public Observable<Integer> observeCartQuantity() {
    return mCartQuantitySubject;
  }

  public Observable<Double> observeCartSubtotal() {
    return mCartSubtotalSubject;
  }

  public void removeFromCart(ProductVariant productVariant) {
    mCart.decrementVariant(productVariant);
    mCartQuantitySubject.onNext(mCart.getTotalQuantity());
    mCartChangeSubject.onNext(null);
    mCartSubtotalSubject.onNext(mCart.getSubtotal());
  }

  public enum CheckoutState {
    PAYMENT_METHOD,
    SHIPPING_ADDRESS,
    SHIPPING_RATES,
    SUMMARY_BEFORE_PAYMENT,
    PROCESSING,
    PAYMENT_SUCCESS
  }
}
