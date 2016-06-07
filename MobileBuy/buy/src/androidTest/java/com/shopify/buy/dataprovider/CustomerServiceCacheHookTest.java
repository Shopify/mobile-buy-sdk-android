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

import com.shopify.buy.dataprovider.cache.CustomerCacheHook;
import com.shopify.buy.extensions.ShopifyAndroidTestCase;
import com.shopify.buy.model.AccountCredentials;
import com.shopify.buy.model.Customer;
import com.shopify.buy.model.CustomerToken;
import com.shopify.buy.model.internal.AccountCredentialsWrapper;
import com.shopify.buy.model.internal.CustomerTokenWrapper;
import com.shopify.buy.model.internal.CustomerWrapper;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import java.lang.reflect.Field;

import retrofit2.Response;
import rx.Observable;
import rx.functions.Action1;

@RunWith(AndroidJUnit4.class)
public class CustomerServiceCacheHookTest extends ShopifyAndroidTestCase {

    Customer customer;

    CustomerWrapper customerWrapper;

    CustomerToken customerToken;

    CustomerTokenWrapper customerTokenWrapper;

    CustomerRetrofitService customerRetrofitService;

    CustomerCacheHook customerCacheHook;

    @Override
    public void setUp() throws Exception {
        super.setUp();

        customer = new Customer() {
            {
                id = 100L;
                setEmail("test@test.com");
            }
        };

        customerWrapper = new CustomerWrapper(customer);

        customerToken = new CustomerToken();

        customerTokenWrapper = new CustomerTokenWrapper(customerToken);

        customerRetrofitService = Mockito.mock(CustomerRetrofitService.class);

        final Field retrofitServiceField = CustomerServiceDefault.class.getDeclaredField("retrofitService");
        retrofitServiceField.setAccessible(true);
        retrofitServiceField.set((((BuyClientDefault) buyClient).customerService), customerRetrofitService);

        customerCacheHook = Mockito.mock(CustomerCacheHook.class);
        final CustomerCacheRxHookProvider cacheRxHookProvider = new CustomerCacheRxHookProvider(customerCacheHook);

        final Field cacheRxHookProviderField = CustomerServiceDefault.class.getDeclaredField("cacheRxHookProvider");
        cacheRxHookProviderField.setAccessible(true);
        cacheRxHookProviderField.set((((BuyClientDefault) buyClient).customerService), cacheRxHookProvider);
    }

    @Test
    public void cacheWithoutHook() {
        Observable
            .just(customerToken)
            .doOnNext(new CustomerCacheRxHookProvider(null).getCustomerTokenCacheHook())
            .subscribe(new Action1<CustomerToken>() {
                @Override
                public void call(CustomerToken response) {
                    Assert.assertEquals(customerToken, response);
                }
            }, new Action1<Throwable>() {
                @Override
                public void call(Throwable throwable) {
                    Assert.fail();
                }
            });

        Observable
            .just(customer)
            .doOnNext(new CustomerCacheRxHookProvider(null).getCustomerCacheHook())
            .subscribe(new Action1<Customer>() {
                @Override
                public void call(Customer response) {
                    Assert.assertEquals(customer, response);
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
        final CustomerCacheHook cacheHook = Mockito.mock(CustomerCacheHook.class);
        Mockito.doThrow(new RuntimeException()).when(cacheHook).cacheCustomerToken(customerToken);
        Mockito.doThrow(new RuntimeException()).when(cacheHook).cacheCustomer(customer);

        Observable
            .just(customerToken)
            .doOnNext(new CustomerCacheRxHookProvider(cacheHook).getCustomerTokenCacheHook())
            .subscribe(new Action1<CustomerToken>() {
                @Override
                public void call(CustomerToken response) {
                    Assert.assertEquals(customerToken, response);
                }
            }, new Action1<Throwable>() {
                @Override
                public void call(Throwable throwable) {
                    Assert.fail();
                }
            });

        Observable
            .just(customer)
            .doOnNext(new CustomerCacheRxHookProvider(cacheHook).getCustomerCacheHook())
            .subscribe(new Action1<Customer>() {
                @Override
                public void call(Customer response) {
                    Assert.assertEquals(customer, response);
                }
            }, new Action1<Throwable>() {
                @Override
                public void call(Throwable throwable) {
                    Assert.fail();
                }
            });
    }

    @Test
    public void cacheCreateCustomer() {
        final Response<CustomerWrapper> response = Response.success(customerWrapper);
        final Observable<Response<CustomerWrapper>> responseObservable = Observable.just(response);
        final Observable<Response<CustomerTokenWrapper>> responseCustomerTokenObservable = Observable.just(Response.success(customerTokenWrapper));
        Mockito.when(customerRetrofitService.createCustomer(Mockito.any(AccountCredentialsWrapper.class))).thenReturn(responseObservable);
        Mockito.when(customerRetrofitService.getCustomerToken(Mockito.any(AccountCredentialsWrapper.class))).thenReturn(responseCustomerTokenObservable);
        buyClient.createCustomer(new AccountCredentials("test@test.com", "123"), new Callback<Customer>() {
            @Override
            public void success(Customer response) {
                Assert.assertEquals(customer, response);
            }

            @Override
            public void failure(BuyClientError error) {
                Assert.fail();
            }
        });
        Mockito.verify(customerCacheHook, Mockito.times(1)).cacheCustomer(customer);
    }

    @Test
    public void cacheActivateCustomer() {
        final Response<CustomerWrapper> response = Response.success(customerWrapper);
        final Observable<Response<CustomerWrapper>> responseObservable = Observable.just(response);
        Mockito.when(customerRetrofitService.activateCustomer(Mockito.anyLong(), Mockito.anyString(), Mockito.any(AccountCredentialsWrapper.class))).thenReturn(responseObservable);
        buyClient.activateCustomer(100L, "token", new AccountCredentials("test@test.com", "123"), new Callback<Customer>() {
            @Override
            public void success(Customer response) {
                Assert.assertEquals(customer, response);
            }

            @Override
            public void failure(BuyClientError error) {
                Assert.fail();
            }
        });
        Mockito.verify(customerCacheHook, Mockito.times(1)).cacheCustomer(customer);
    }

    @Test
    public void cacheResetPassword() {
        final Response<CustomerWrapper> response = Response.success(customerWrapper);
        final Observable<Response<CustomerWrapper>> responseObservable = Observable.just(response);
        Mockito.when(customerRetrofitService.resetPassword(Mockito.anyLong(), Mockito.anyString(), Mockito.any(AccountCredentialsWrapper.class))).thenReturn(responseObservable);
        buyClient.resetPassword(100L, "token", new AccountCredentials("test@test.com", "123"), new Callback<Customer>() {
            @Override
            public void success(Customer response) {
                Assert.assertEquals(customer, response);
            }

            @Override
            public void failure(BuyClientError error) {
                Assert.fail();
            }
        });
        Mockito.verify(customerCacheHook, Mockito.times(1)).cacheCustomer(customer);
    }

    @Test
    public void cacheLoginCustomer() {
        final Response<CustomerTokenWrapper> response = Response.success(customerTokenWrapper);
        final Observable<Response<CustomerTokenWrapper>> responseObservable = Observable.just(response);
        Mockito.when(customerRetrofitService.getCustomerToken(Mockito.any(AccountCredentialsWrapper.class))).thenReturn(responseObservable);
        buyClient.loginCustomer(new AccountCredentials("test@test.com", "123"), new Callback<CustomerToken>() {
            @Override
            public void success(CustomerToken response) {
                Assert.assertEquals(customerToken, response);
            }

            @Override
            public void failure(BuyClientError error) {
                Assert.fail();
            }
        });
        Mockito.verify(customerCacheHook, Mockito.times(1)).cacheCustomerToken(customerToken);
    }

    @Test
    public void cacheUpdateCustomer() {
        final Response<CustomerWrapper> response = Response.success(customerWrapper);
        final Observable<Response<CustomerWrapper>> responseObservable = Observable.just(response);
        Mockito.when(customerRetrofitService.updateCustomer(Mockito.anyLong(), Mockito.any(CustomerWrapper.class))).thenReturn(responseObservable);
        buyClient.updateCustomer(customer, new Callback<Customer>() {
            @Override
            public void success(Customer response) {
                Assert.assertEquals(customer, response);
            }

            @Override
            public void failure(BuyClientError error) {
                Assert.fail();
            }
        });
        Mockito.verify(customerCacheHook, Mockito.times(1)).cacheCustomer(customer);
    }

    @Test
    public void cacheGetCustomer() {
        final Response<CustomerWrapper> response = Response.success(customerWrapper);
        final Observable<Response<CustomerWrapper>> responseObservable = Observable.just(response);
        Mockito.when(customerRetrofitService.getCustomer(Mockito.anyLong())).thenReturn(responseObservable);
        buyClient.getCustomer(100L, new Callback<Customer>() {
            @Override
            public void success(Customer response) {
                Assert.assertEquals(customer, response);
            }

            @Override
            public void failure(BuyClientError error) {
                Assert.fail();
            }
        });
        Mockito.verify(customerCacheHook, Mockito.times(1)).cacheCustomer(customer);
    }

    @Test
    public void cacheRenewCustomer() {
        cacheLoginCustomer();

        Mockito.reset(customerCacheHook);

        final Response<CustomerTokenWrapper> response = Response.success(customerTokenWrapper);
        final Observable<Response<CustomerTokenWrapper>> responseObservable = Observable.just(response);
        Mockito.when(customerRetrofitService.renewCustomerToken(Mockito.anyString(), Mockito.anyLong())).thenReturn(responseObservable);
        buyClient.renewCustomer(new Callback<CustomerToken>() {
            @Override
            public void success(CustomerToken response) {
                Assert.assertEquals(customerToken, response);
            }

            @Override
            public void failure(BuyClientError error) {
                Assert.fail();
            }
        });
        Mockito.verify(customerCacheHook, Mockito.times(1)).cacheCustomerToken(customerToken);
    }
}
