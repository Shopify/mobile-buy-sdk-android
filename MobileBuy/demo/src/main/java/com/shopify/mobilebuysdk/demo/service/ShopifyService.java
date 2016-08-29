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
import com.shopify.buy.model.Product;
import com.shopify.buy.model.ProductVariant;
import com.shopify.buy.model.Shop;
import com.shopify.mobilebuysdk.demo.App;
import com.shopify.mobilebuysdk.demo.BuildConfig;
import com.shopify.mobilebuysdk.demo.util.StringUtils;

import android.app.Application;

import java.util.List;

import rx.Observable;
import rx.subjects.BehaviorSubject;

/**
 * Created by henrytao on 8/27/16.
 */
public class ShopifyService {

  private static ShopifyService sInstance;

  // TODO: consider using DI or not?
  public static ShopifyService getInstance() {
    if (sInstance == null) {
      synchronized (ShopifyService.class) {
        if (sInstance == null) {
          sInstance = new ShopifyService(App.getInstance());
        }
      }
    }
    return sInstance;
  }

  private final BuyClient mBuyClient;

  private final Cart mCart;

  private final BehaviorSubject<Integer> mCartQuantitySubject;

  private ShopifyService(Application application) {
    if (StringUtils.isEmpty(BuildConfig.SHOP_DOMAIN) ||
        StringUtils.isEmpty(BuildConfig.API_KEY) ||
        StringUtils.isEmpty(BuildConfig.APP_ID)) {
      throw new IllegalStateException("You must add 'SHOP_DOMAIN', 'API_KEY', 'APP_ID' entries in app/shop.properties");
    }
    mBuyClient = new BuyClientBuilder()
        .shopDomain(BuildConfig.SHOP_DOMAIN)
        .apiKey(BuildConfig.API_KEY)
        .appId(BuildConfig.APP_ID)
        .applicationName(application.getPackageName())
        .build();

    mCart = new Cart();
    mCartQuantitySubject = BehaviorSubject.create(0);
  }

  public void addToCart(ProductVariant productVariant) {
    mCart.addVariant(productVariant);
    mCartQuantitySubject.onNext(mCart.getTotalQuantity());
  }

  public Observable<List<Product>> getProducts() {
    return mBuyClient.getProducts(1);
  }

  public Observable<Shop> getShop() {
    return mBuyClient.getShop();
  }

  public Observable<Integer> observeCartQuantity() {
    return mCartQuantitySubject;
  }
}
