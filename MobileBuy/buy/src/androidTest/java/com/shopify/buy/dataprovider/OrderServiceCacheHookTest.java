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

import com.shopify.buy.dataprovider.cache.OrderCacheHook;
import com.shopify.buy.extensions.ShopifyAndroidTestCase;
import com.shopify.buy.model.Customer;
import com.shopify.buy.model.Order;
import com.shopify.buy.model.internal.OrderWrapper;
import com.shopify.buy.model.internal.OrdersWrapper;

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
public class OrderServiceCacheHookTest extends ShopifyAndroidTestCase {

    Order order1;

    OrderWrapper orderWrapper1;

    Order order2;

    List<Order> orderList;

    OrdersWrapper ordersWrapper;

    Customer customer;

    OrderRetrofitService orderRetrofitService;

    OrderCacheHook orderCacheHook;

    @Override
    public void setUp() throws Exception {
        super.setUp();

        order1 = new Order() {
            {
                id = 1L;
                name = "Order1";
                orderNumber = "Order1";
            }
        };

        orderWrapper1 = new OrderWrapper(order1);

        order2 = new Order() {
            {
                id = 2L;
                name = "Order2";
                orderNumber = "Order2";
            }
        };

        orderList = Arrays.asList(order1, order2);
        ordersWrapper = new OrdersWrapper(orderList);

        customer = new Customer() {
            {
                id = 100L;
            }
        };

        orderRetrofitService = Mockito.mock(OrderRetrofitService.class);

        final Field retrofitServiceField = OrderServiceDefault.class.getDeclaredField("retrofitService");
        retrofitServiceField.setAccessible(true);
        retrofitServiceField.set((((BuyClientDefault) buyClient).orderService), orderRetrofitService);

        orderCacheHook = Mockito.mock(OrderCacheHook.class);
        final OrderCacheRxHookProvider cacheRxHookProvider = new OrderCacheRxHookProvider(orderCacheHook);

        final Field cacheRxHookProviderField = OrderServiceDefault.class.getDeclaredField("cacheRxHookProvider");
        cacheRxHookProviderField.setAccessible(true);
        cacheRxHookProviderField.set((((BuyClientDefault) buyClient).orderService), cacheRxHookProvider);
    }

    @Test
    public void cacheWithoutHook() {
        Observable
            .just(order1)
            .doOnNext(new OrderCacheRxHookProvider(null).getOrderCacheHook(customer))
            .subscribe(new Action1<Order>() {
                @Override
                public void call(Order response) {
                    Assert.assertEquals(order1, response);
                }
            }, new Action1<Throwable>() {
                @Override
                public void call(Throwable throwable) {
                    Assert.fail();
                }
            });

        Observable
            .just(orderList)
            .doOnNext(new OrderCacheRxHookProvider(null).getOrdersCacheHook(customer))
            .subscribe(new Action1<List<Order>>() {
                @Override
                public void call(List<Order> response) {
                    Assert.assertEquals(orderList.get(0), response.get(0));
                    Assert.assertEquals(orderList.get(1), response.get(1));
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
        final OrderCacheHook cacheHook = Mockito.mock(OrderCacheHook.class);
        Mockito.doThrow(new RuntimeException()).when(cacheHook).cacheOrder(customer, order1);
        Mockito.doThrow(new RuntimeException()).when(cacheHook).cacheOrders(customer, orderList);

        Observable
            .just(order1)
            .doOnNext(new OrderCacheRxHookProvider(cacheHook).getOrderCacheHook(customer))
            .subscribe(new Action1<Order>() {
                @Override
                public void call(Order response) {
                    Assert.assertEquals(order1, response);
                }
            }, new Action1<Throwable>() {
                @Override
                public void call(Throwable throwable) {
                    Assert.fail();
                }
            });

        Observable
            .just(orderList)
            .doOnNext(new OrderCacheRxHookProvider(cacheHook).getOrdersCacheHook(customer))
            .subscribe(new Action1<List<Order>>() {
                @Override
                public void call(List<Order> response) {
                    Assert.assertEquals(orderList.get(0), response.get(0));
                    Assert.assertEquals(orderList.get(1), response.get(1));
                }
            }, new Action1<Throwable>() {
                @Override
                public void call(Throwable throwable) {
                    Assert.fail();
                }
            });
    }

    @Test
    public void cacheGetOrder() {
        final Response<OrderWrapper> response = Response.success(orderWrapper1);
        final Observable<Response<OrderWrapper>> responseObservable = Observable.just(response);
        Mockito.when(orderRetrofitService.getOrder(Mockito.anyLong(), Mockito.anyString())).thenReturn(responseObservable);
        buyClient.getOrder(customer, "test", new Callback<Order>() {
            @Override
            public void success(Order response) {
                Assert.assertEquals(order1, response);
            }

            @Override
            public void failure(RetrofitError error) {
                Assert.fail();
            }
        });
        Mockito.verify(orderCacheHook, Mockito.times(1)).cacheOrder(customer, order1);
    }

    @Test
    public void cacheGetOrders() {
        final Response<OrdersWrapper> response = Response.success(ordersWrapper);
        final Observable<Response<OrdersWrapper>> responseObservable = Observable.just(response);
        Mockito.when(orderRetrofitService.getOrders(Mockito.anyLong())).thenReturn(responseObservable);
        buyClient.getOrders(customer, new Callback<List<Order>>() {
            @Override
            public void success(List<Order> response) {
                Assert.assertEquals(orderList.get(0), response.get(0));
                Assert.assertEquals(orderList.get(1), response.get(1));
            }

            @Override
            public void failure(RetrofitError error) {
                Assert.fail();
            }
        });
        Mockito.verify(orderCacheHook, Mockito.times(1)).cacheOrders(customer, orderList);
    }
}
