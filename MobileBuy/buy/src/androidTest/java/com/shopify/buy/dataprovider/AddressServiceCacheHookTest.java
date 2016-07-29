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

import com.shopify.buy.dataprovider.cache.AddressCacheHook;
import com.shopify.buy.extensions.ShopifyAndroidTestCase;
import com.shopify.buy.model.Address;
import com.shopify.buy.model.Customer;
import com.shopify.buy.model.CustomerToken;
import com.shopify.buy.model.internal.AddressWrapper;
import com.shopify.buy.model.internal.AddressesWrapper;

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
public class AddressServiceCacheHookTest extends ShopifyAndroidTestCase {

    Address address1;

    AddressWrapper addressWrapper1;

    Address address2;

    List<Address> addressList;

    AddressesWrapper addressesWrapper;

    Customer customer;

    AddressRetrofitService addressRetrofitService;

    AddressCacheHook addressCacheHook;

    CustomerService customerService;

    @Override
    public void setUp() throws Exception {
        super.setUp();

        address1 = new Address() {
            {
                id = 1L;
            }
        };
        address1.setAddress1("address1");

        addressWrapper1 = new AddressWrapper(address1);

        address2 = new Address(){
            {
                id = 1L;
            }
        };
        address2.setAddress1("address2");

        addressList = Arrays.asList(address1, address2);

        addressesWrapper = new AddressesWrapper(addressList);

        customer = new Customer() {
            {
                id = 100L;
            }
        };

        addressRetrofitService = Mockito.mock(AddressRetrofitService.class);

        final Field retrofitServiceField = AddressServiceDefault.class.getDeclaredField("retrofitService");
        retrofitServiceField.setAccessible(true);
        retrofitServiceField.set((((BuyClientDefault) buyClient).addressService), addressRetrofitService);

        addressCacheHook = Mockito.mock(AddressCacheHook.class);
        final AddressCacheRxHookProvider cacheRxHookProvider = new AddressCacheRxHookProvider(addressCacheHook);

        final Field cacheRxHookProviderField = AddressServiceDefault.class.getDeclaredField("cacheRxHookProvider");
        cacheRxHookProviderField.setAccessible(true);
        cacheRxHookProviderField.set((((BuyClientDefault) buyClient).addressService), cacheRxHookProvider);

        customerService = Mockito.mock(CustomerService.class);
        Mockito.when(customerService.getCustomerToken()).thenReturn(new CustomerToken() {
            @Override
            public Long getCustomerId() {
                return customer.getId();
            }
        });

        final Field customerServiceField = AddressServiceDefault.class.getDeclaredField("customerService");
        customerServiceField.setAccessible(true);
        customerServiceField.set((((BuyClientDefault) buyClient).addressService), customerService);
    }

    @Test
    public void cacheWithoutHook() {
        Observable
            .just(address1)
            .doOnNext(new AddressCacheRxHookProvider(null).getAddressCacheHook(customer.getId()))
            .subscribe(new Action1<Address>() {
                @Override
                public void call(Address response) {
                    Assert.assertEquals(address1, response);
                }
            }, new Action1<Throwable>() {
                @Override
                public void call(Throwable throwable) {
                    Assert.fail();
                }
            });

        Observable
            .just(addressList)
            .doOnNext(new AddressCacheRxHookProvider(null).getAddressesCacheHook(customer.getId()))
            .subscribe(new Action1<List<Address>>() {
                @Override
                public void call(List<Address> response) {
                    Assert.assertEquals(addressList.get(0), response.get(0));
                    Assert.assertEquals(addressList.get(1), response.get(1));
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
        final AddressCacheHook addressCacheHook = Mockito.mock(AddressCacheHook.class);

        Mockito.doThrow(new RuntimeException()).when(addressCacheHook).cacheAddress(customer.getId(), address1);
        Mockito.doThrow(new RuntimeException()).when(addressCacheHook).cacheAddresses(customer.getId(), addressList);

        Observable
            .just(address1)
            .doOnNext(new AddressCacheRxHookProvider(addressCacheHook).getAddressCacheHook(customer.getId()))
            .subscribe(new Action1<Address>() {
                @Override
                public void call(Address response) {
                    Assert.assertEquals(address1, response);
                }
            }, new Action1<Throwable>() {
                @Override
                public void call(Throwable throwable) {
                    Assert.fail();
                }
            });

        Observable
            .just(addressList)
            .doOnNext(new AddressCacheRxHookProvider(addressCacheHook).getAddressesCacheHook(customer.getId()))
            .subscribe(new Action1<List<Address>>() {
                @Override
                public void call(List<Address> response) {
                    Assert.assertEquals(addressList.get(0), response.get(0));
                    Assert.assertEquals(addressList.get(1), response.get(1));
                }
            }, new Action1<Throwable>() {
                @Override
                public void call(Throwable throwable) {
                    Assert.fail();
                }
            });
    }

    @Test
    public void cacheCreateAddress() {
        final Response<AddressWrapper> response = Response.success(addressWrapper1);
        final Observable<Response<AddressWrapper>> responseObservable = Observable.just(response);
        Mockito.when(addressRetrofitService.createAddress(Mockito.anyLong(), (AddressWrapper) Mockito.any())).thenReturn(responseObservable);
        buyClient.createAddress(address1, new Callback<Address>() {
            @Override
            public void success(Address response) {
                Assert.assertEquals(address1, response);
            }

            @Override
            public void failure(BuyClientError error) {
                Assert.fail();
            }
        });
        Mockito.verify(addressCacheHook, Mockito.times(1)).cacheAddress(customer.getId(), address1);
    }

    @Test
    public void cacheGetAddresses() {
        final Response<AddressesWrapper> response = Response.success(addressesWrapper);
        final Observable<Response<AddressesWrapper>> responseObservable = Observable.just(response);
        Mockito.when(addressRetrofitService.getAddresses(Mockito.anyLong())).thenReturn(responseObservable);
        buyClient.getAddresses(new Callback<List<Address>>() {
            @Override
            public void success(List<Address> response) {
                Assert.assertEquals(addressList.get(0), response.get(0));
                Assert.assertEquals(addressList.get(1), response.get(1));
            }

            @Override
            public void failure(BuyClientError error) {
                Assert.fail();
            }
        });
        Mockito.verify(addressCacheHook, Mockito.times(1)).cacheAddresses(customer.getId(), addressList);
    }

    @Test
    public void cacheGetAddress() {
        final Response<AddressWrapper> response = Response.success(addressWrapper1);
        final Observable<Response<AddressWrapper>> responseObservable = Observable.just(response);
        Mockito.when(addressRetrofitService.getAddress(Mockito.anyLong(), Mockito.anyLong())).thenReturn(responseObservable);
        buyClient.getAddress(address1.getId(), new Callback<Address>() {
            @Override
            public void success(Address response) {
                Assert.assertEquals(address1, response);
            }

            @Override
            public void failure(BuyClientError error) {
                Assert.fail();
            }
        });
        Mockito.verify(addressCacheHook, Mockito.times(1)).cacheAddress(customer.getId(), address1);
    }

    @Test
    public void cacheUpdateAddress() {
        final Response<AddressWrapper> response = Response.success(addressWrapper1);
        final Observable<Response<AddressWrapper>> responseObservable = Observable.just(response);
        Mockito.when(addressRetrofitService.updateAddress(Mockito.anyLong(), (AddressWrapper) Mockito.any(), Mockito.anyLong())).thenReturn(responseObservable);
        buyClient.updateAddress(address1, new Callback<Address>() {
            @Override
            public void success(Address response) {
                Assert.assertEquals(address1, response);
            }

            @Override
            public void failure(BuyClientError error) {
                Assert.fail();
            }
        });
        Mockito.verify(addressCacheHook, Mockito.times(1)).cacheAddress(customer.getId(), address1);
    }
}
