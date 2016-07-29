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

import com.shopify.buy.dataprovider.cache.CheckoutCacheHook;
import com.shopify.buy.extensions.ShopifyAndroidTestCase;
import com.shopify.buy.model.Checkout;
import com.shopify.buy.model.CustomerToken;
import com.shopify.buy.model.GiftCard;
import com.shopify.buy.model.ShippingRate;
import com.shopify.buy.model.internal.CheckoutWrapper;
import com.shopify.buy.model.internal.GiftCardWrapper;
import com.shopify.buy.model.internal.ShippingRatesWrapper;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

import retrofit2.Response;
import rx.Observable;
import rx.functions.Action1;

@RunWith(AndroidJUnit4.class)
public class CheckoutServiceCacheHookTest extends ShopifyAndroidTestCase {

    Checkout checkout;

    CheckoutWrapper checkoutWrapper;

    ShippingRate shippingRate1;

    ShippingRate shippingRate2;

    List<ShippingRate> shippingRateList;

    ShippingRatesWrapper shippingRatesWrapper;

    GiftCard giftCard;

    GiftCardWrapper giftCardWrapper;

    CheckoutRetrofitService checkoutRetrofitService;

    CheckoutCacheHook checkoutCacheHook;

    CustomerService customerService;

    @Override
    public void setUp() throws Exception {
        super.setUp();

        checkout = Checkout.fromJson("{\"id\":100, \"channel\":\"mobile_app\",\"payment_due\":\"payment_due\",\"token\":\"checkout_token\"}");
        checkoutWrapper = new CheckoutWrapper(checkout);

        shippingRate1 = new ShippingRate();
        shippingRate2 = new ShippingRate();
        shippingRateList = Arrays.asList(shippingRate1, shippingRate2);

        shippingRatesWrapper = new ShippingRatesWrapper(shippingRateList);

        giftCard = new GiftCard("test") {
            {
                id = 1L;
                checkout = new Checkout();
            }
        };
        giftCardWrapper = new GiftCardWrapper(giftCard);

        checkoutRetrofitService = Mockito.mock(CheckoutRetrofitService.class);

        final Field retrofitServiceField = CheckoutServiceDefault.class.getDeclaredField("retrofitService");
        retrofitServiceField.setAccessible(true);
        retrofitServiceField.set((((BuyClientDefault) buyClient).checkoutService), checkoutRetrofitService);

        checkoutCacheHook = Mockito.mock(CheckoutCacheHook.class);
        final CheckoutCacheRxHookProvider cacheRxHookProvider = new CheckoutCacheRxHookProvider(checkoutCacheHook);

        final Field cacheRxHookProviderField = CheckoutServiceDefault.class.getDeclaredField("cacheRxHookProvider");
        cacheRxHookProviderField.setAccessible(true);
        cacheRxHookProviderField.set((((BuyClientDefault) buyClient).checkoutService), cacheRxHookProvider);

        customerService = Mockito.mock(CustomerService.class);
        Mockito.when(customerService.getCustomerToken()).thenReturn(new CustomerToken() {
            @Override
            public Long getCustomerId() {
                return 1L;
            }
        });
    }

    @Test
    public void cacheWithoutHook() {
        Observable
            .just(checkout)
            .doOnNext(new CheckoutCacheRxHookProvider(null).getCheckoutCacheHook())
            .subscribe(new Action1<Checkout>() {
                @Override
                public void call(Checkout response) {
                    Assert.assertEquals(checkout, response);
                }
            }, new Action1<Throwable>() {
                @Override
                public void call(Throwable throwable) {
                    Assert.fail();
                }
            });

        Observable
            .just(shippingRateList)
            .doOnNext(new CheckoutCacheRxHookProvider(null).getShippingRatesCacheHook(""))
            .subscribe(new Action1<List<ShippingRate>>() {
                @Override
                public void call(List<ShippingRate> response) {
                    Assert.assertEquals(shippingRateList.get(0), response.get(0));
                    Assert.assertEquals(shippingRateList.get(1), response.get(1));
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
        final CheckoutCacheHook cacheHook = Mockito.mock(CheckoutCacheHook.class);
        Mockito.doThrow(new RuntimeException()).when(cacheHook).cacheCheckout(Mockito.any(Checkout.class));
        Mockito.doThrow(new RuntimeException()).when(cacheHook).cacheShippingRates(Mockito.anyString(), Mockito.anyList());

        Observable
            .just(checkout)
            .doOnNext(new CheckoutCacheRxHookProvider(cacheHook).getCheckoutCacheHook())
            .subscribe(new Action1<Checkout>() {
                @Override
                public void call(Checkout response) {
                    Assert.assertEquals(checkout, response);
                }
            }, new Action1<Throwable>() {
                @Override
                public void call(Throwable throwable) {
                    Assert.fail();
                }
            });

        Observable
            .just(shippingRateList)
            .doOnNext(new CheckoutCacheRxHookProvider(cacheHook).getShippingRatesCacheHook(""))
            .subscribe(new Action1<List<ShippingRate>>() {
                @Override
                public void call(List<ShippingRate> response) {
                    Assert.assertEquals(shippingRateList.get(0), response.get(0));
                    Assert.assertEquals(shippingRateList.get(1), response.get(1));
                }
            }, new Action1<Throwable>() {
                @Override
                public void call(Throwable throwable) {
                    Assert.fail();
                }
            });
    }

    @Test
    public void cacheCreateCheckout() {
        final Response<CheckoutWrapper> response = Response.success(checkoutWrapper);
        final Observable<Response<CheckoutWrapper>> responseObservable = Observable.just(response);
        Mockito.when(checkoutRetrofitService.createCheckout(Mockito.any(CheckoutWrapper.class))).thenReturn(responseObservable);
        buyClient.createCheckout(checkout, new Callback<Checkout>() {
            @Override
            public void success(Checkout response) {
                Assert.assertEquals(checkout, response);
            }

            @Override
            public void failure(BuyClientError error) {
                Assert.fail();
            }
        });
        Mockito.verify(checkoutCacheHook, Mockito.times(1)).cacheCheckout(checkout);
    }

    @Test
    public void cacheUpdateCheckout() {
        final Response<CheckoutWrapper> response = Response.success(checkoutWrapper);
        final Observable<Response<CheckoutWrapper>> responseObservable = Observable.just(response);
        Mockito.when(checkoutRetrofitService.updateCheckout(Mockito.any(CheckoutWrapper.class), Mockito.anyString())).thenReturn(responseObservable);
        buyClient.updateCheckout(checkout, new Callback<Checkout>() {
            @Override
            public void success(Checkout response) {
                Assert.assertEquals(checkout, response);
            }

            @Override
            public void failure(BuyClientError error) {
                Assert.fail();
            }
        });
        Mockito.verify(checkoutCacheHook, Mockito.times(1)).cacheCheckout(checkout);
    }

    @Test
    public void cacheGetShippingRates() {
        final Response<ShippingRatesWrapper> response = Response.success(shippingRatesWrapper);
        final Observable<Response<ShippingRatesWrapper>> responseObservable = Observable.just(response);
        Mockito.when(checkoutRetrofitService.getShippingRates(Mockito.anyString())).thenReturn(responseObservable);
        buyClient.getShippingRates("test", new Callback<List<ShippingRate>>() {
            @Override
            public void success(List<ShippingRate> response) {
                Assert.assertEquals(shippingRateList.get(0), response.get(0));
                Assert.assertEquals(shippingRateList.get(1), response.get(1));
            }

            @Override
            public void failure(BuyClientError error) {
                Assert.fail();
            }
        });
        Mockito.verify(checkoutCacheHook, Mockito.times(1)).cacheShippingRates("test", shippingRateList);
    }

    @Test
    public void cacheGetCheckout() {
        final Response<CheckoutWrapper> response = Response.success(checkoutWrapper);
        final Observable<Response<CheckoutWrapper>> responseObservable = Observable.just(response);
        Mockito.when(checkoutRetrofitService.getCheckout(Mockito.anyString())).thenReturn(responseObservable);
        buyClient.getCheckout(checkout.getToken(), new Callback<Checkout>() {
            @Override
            public void success(Checkout response) {
                Assert.assertEquals(checkout, response);
            }

            @Override
            public void failure(BuyClientError error) {
                Assert.fail();
            }
        });
        Mockito.verify(checkoutCacheHook, Mockito.times(1)).cacheCheckout(checkout);
    }

    @Test
    public void cacheApplyGiftCard() {
        final Response<GiftCardWrapper> response = Response.success(giftCardWrapper);
        final Observable<Response<GiftCardWrapper>> responseObservable = Observable.just(response);
        Mockito.when(checkoutRetrofitService.applyGiftCard(Mockito.any(GiftCardWrapper.class), Mockito.anyString())).thenReturn(responseObservable);
        buyClient.applyGiftCard(giftCard.getCode(), checkout, new Callback<Checkout>() {
            @Override
            public void success(Checkout response) {
                Assert.assertEquals(checkout, response);
            }

            @Override
            public void failure(BuyClientError error) {
                Assert.fail();
            }
        });
        Mockito.verify(checkoutCacheHook, Mockito.times(1)).cacheCheckout(checkout);
    }

    @Test
    public void cacheRemoveGiftCard() {
        final Response<GiftCardWrapper> response = Response.success(giftCardWrapper);
        final Observable<Response<GiftCardWrapper>> responseObservable = Observable.just(response);
        Mockito.when(checkoutRetrofitService.removeGiftCard(Mockito.anyLong(), Mockito.anyString())).thenReturn(responseObservable);
        buyClient.removeGiftCard(giftCard.getId(), checkout, new Callback<Checkout>() {
            @Override
            public void success(Checkout response) {
                Assert.assertEquals(checkout, response);
            }

            @Override
            public void failure(BuyClientError error) {
                Assert.fail();
            }
        });
        Mockito.verify(checkoutCacheHook, Mockito.times(1)).cacheCheckout(checkout);
    }
}
