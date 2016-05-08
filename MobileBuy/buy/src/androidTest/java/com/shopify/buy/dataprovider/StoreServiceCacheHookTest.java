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

import android.support.test.runner.AndroidJUnit4;

import com.shopify.buy.dataprovider.cache.StoreCacheHook;
import com.shopify.buy.extensions.ShopifyAndroidTestCase;
import com.shopify.buy.model.Shop;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import java.lang.reflect.Field;

import retrofit2.Response;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;

@RunWith(AndroidJUnit4.class)
public class StoreServiceCacheHookTest extends ShopifyAndroidTestCase {

    Shop shop;

    StoreRetrofitService storeRetrofitService;

    StoreCacheHook storeCacheHook;

    @Override
    public void setUp() throws Exception {
        super.setUp();

        shop = new Shop() {
            {
                name = "Mocked Shop";
            }
        };

        storeRetrofitService = Mockito.mock(StoreRetrofitService.class);

        final Field retrofitServiceField = StoreServiceDefault.class.getDeclaredField("retrofitService");
        retrofitServiceField.setAccessible(true);
        retrofitServiceField.set(((BuyClientDefault) buyClient).storeService, storeRetrofitService);

        storeCacheHook = Mockito.mock(StoreCacheHook.class);
        final StoreCacheRxHookProvider storeCacheRxHookProvider = new StoreCacheRxHookProvider(storeCacheHook);

        final Field cacheRxHookProviderField = StoreServiceDefault.class.getDeclaredField("cacheRxHookProvider");
        cacheRxHookProviderField.setAccessible(true);
        cacheRxHookProviderField.set(((BuyClientDefault) buyClient).storeService, storeCacheRxHookProvider);
    }

    @Test
    public void cacheWithoutHook() {
        Observable
            .just(shop)
            .doOnNext(new StoreCacheRxHookProvider(null).getStoreHook())
            .subscribe(new Action1<Shop>() {
                @Override
                public void call(Shop shop) {
                    Assert.assertEquals(StoreServiceCacheHookTest.this.shop, shop);
                }
            }, new Action1<Throwable>() {
                @Override
                public void call(Throwable throwable) {
                    Assert.fail();
                }
            });
    }

    @Test
    public void cacheWithHookException() {
        final StoreCacheHook storeCacheHook = Mockito.mock(StoreCacheHook.class);
        Mockito.doThrow(new RuntimeException()).when(storeCacheHook).cacheStore(shop);

        Observable
            .just(shop)
            .doOnNext(new StoreCacheRxHookProvider(storeCacheHook).getStoreHook())
            .subscribe(new Action1<Shop>() {
                @Override
                public void call(Shop shop) {
                    Assert.assertEquals(StoreServiceCacheHookTest.this.shop, shop);
                }
            }, new Action1<Throwable>() {
                @Override
                public void call(Throwable throwable) {
                    Assert.fail();
                }
            });
    }

    @Test
    public void cacheStoreWithHook() {
        final Response<Shop> response = Response.success(shop);
        final Observable<Response<Shop>> shopResponseObservable = Observable.just(response);
        Mockito.when(storeRetrofitService.getShop()).thenReturn(shopResponseObservable);

        buyClient.getShop(new Callback<Shop>() {
            @Override
            public void success(Shop body) {
                Assert.assertEquals(shop, body);
            }

            @Override
            public void failure(RetrofitError error) {
                Assert.fail();
            }
        });

        Mockito.verify(storeCacheHook, Mockito.times(1)).cacheStore(shop);
    }
}
