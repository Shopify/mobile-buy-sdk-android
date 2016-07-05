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

import com.shopify.buy.dataprovider.cache.ProductCacheHook;
import com.shopify.buy.extensions.ShopifyAndroidTestCase;
import com.shopify.buy.model.Collection;
import com.shopify.buy.model.Product;
import com.shopify.buy.model.internal.CollectionListings;
import com.shopify.buy.model.internal.ProductListings;

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
public class ProductServiceCacheHookTest extends ShopifyAndroidTestCase {

    Product product1;

    Product product2;

    List<Product> productList;

    ProductListings productListings;

    Collection collection1;

    Collection collection2;

    List<Collection> collectionList;

    CollectionListings collectionListings;

    ProductRetrofitService productRetrofitService;

    ProductCacheHook productCacheHook;

    @Override
    public void setUp() throws Exception {
        super.setUp();

        product1 = new Product() {
            {
                productId = 1L;
            }
        };
        product2 = new Product() {
            {
                productId = 2L;
            }
        };
        productList = Arrays.asList(product1, product2);
        productListings = new ProductListings(productList);

        collection1 = new Collection();
        collection2 = new Collection();
        collectionList = Arrays.asList(collection1, collection2);

        collectionListings = new CollectionListings(collectionList);

        productRetrofitService = Mockito.mock(ProductRetrofitService.class);

        final Field retrofitServiceField = ProductServiceDefault.class.getDeclaredField("retrofitService");
        retrofitServiceField.setAccessible(true);
        retrofitServiceField.set((((BuyClientDefault) buyClient).productService), productRetrofitService);

        productCacheHook = Mockito.mock(ProductCacheHook.class);
        final ProductCacheRxHookProvider cacheRxHookProvider = new ProductCacheRxHookProvider(productCacheHook);

        final Field cacheRxHookProviderField = ProductServiceDefault.class.getDeclaredField("cacheRxHookProvider");
        cacheRxHookProviderField.setAccessible(true);
        cacheRxHookProviderField.set((((BuyClientDefault) buyClient).productService), cacheRxHookProvider);
    }

    @Test
    public void cacheWithoutHook() {
        Observable
            .just(productList)
            .doOnNext(new ProductCacheRxHookProvider(null).getProductPageCacheHook(1, 1))
            .subscribe(new Action1<List<Product>>() {
                @Override
                public void call(List<Product> response) {
                    Assert.assertEquals(productList.get(0), response.get(0));
                    Assert.assertEquals(productList.get(1), response.get(1));
                }
            }, new Action1<Throwable>() {
                @Override
                public void call(Throwable throwable) {
                    Assert.fail();
                }
            });

        Observable
            .just(product1)
            .doOnNext(new ProductCacheRxHookProvider(null).getProductCacheHook())
            .subscribe(new Action1<Product>() {
                @Override
                public void call(Product response) {
                    Assert.assertEquals(product1, response);
                }
            }, new Action1<Throwable>() {
                @Override
                public void call(Throwable throwable) {
                    Assert.fail();
                }
            });

        Observable
            .just(productList)
            .doOnNext(new ProductCacheRxHookProvider(null).getCollectionProductPageCacheHook(1L, 1, 1))
            .subscribe(new Action1<List<Product>>() {
                @Override
                public void call(List<Product> response) {
                    Assert.assertEquals(productList.get(0), response.get(0));
                    Assert.assertEquals(productList.get(1), response.get(1));
                }
            }, new Action1<Throwable>() {
                @Override
                public void call(Throwable throwable) {
                    Assert.fail();
                }
            });

        Observable
            .just(collectionList)
            .doOnNext(new ProductCacheRxHookProvider(null).getCollectionPageCacheHook(1, 1))
            .subscribe(new Action1<List<Collection>>() {
                @Override
                public void call(List<Collection> response) {
                    Assert.assertEquals(collectionList.get(0), response.get(0));
                    Assert.assertEquals(collectionList.get(1), response.get(1));
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
        final ProductCacheHook cacheHook = Mockito.mock(ProductCacheHook.class);
        Mockito.doThrow(new RuntimeException()).when(cacheHook).cacheProducts(Mockito.anyInt(), Mockito.anyInt(), Mockito.anyList());
        Mockito.doThrow(new RuntimeException()).when(cacheHook).cacheProduct(Mockito.any(Product.class));
        Mockito.doThrow(new RuntimeException()).when(cacheHook).cacheProducts(Mockito.anyList());
        Mockito.doThrow(new RuntimeException()).when(cacheHook).cacheProduct(Mockito.any(Product.class));
        Mockito.doThrow(new RuntimeException()).when(cacheHook).cacheProducts(Mockito.anyList());
        Mockito.doThrow(new RuntimeException()).when(cacheHook).cacheProducts(Mockito.anyLong(), Mockito.anyInt(), Mockito.anyInt(), Mockito.anyList());
        Mockito.doThrow(new RuntimeException()).when(cacheHook).cacheCollections(Mockito.anyInt(), Mockito.anyInt(), Mockito.anyList());

        Observable
            .just(productList)
            .doOnNext(new ProductCacheRxHookProvider(cacheHook).getProductPageCacheHook(1, 1))
            .subscribe(new Action1<List<Product>>() {
                @Override
                public void call(List<Product> response) {
                    Assert.assertEquals(productList.get(0), response.get(0));
                    Assert.assertEquals(productList.get(1), response.get(1));
                }
            }, new Action1<Throwable>() {
                @Override
                public void call(Throwable throwable) {
                    Assert.fail();
                }
            });

        Observable
            .just(product1)
            .doOnNext(new ProductCacheRxHookProvider(cacheHook).getProductCacheHook())
            .subscribe(new Action1<Product>() {
                @Override
                public void call(Product response) {
                    Assert.assertEquals(product1, response);
                }
            }, new Action1<Throwable>() {
                @Override
                public void call(Throwable throwable) {
                    Assert.fail();
                }
            });

        Observable
            .just(productList)
            .doOnNext(new ProductCacheRxHookProvider(cacheHook).getCollectionProductPageCacheHook(1L, 1, 1))
            .subscribe(new Action1<List<Product>>() {
                @Override
                public void call(List<Product> response) {
                    Assert.assertEquals(productList.get(0), response.get(0));
                    Assert.assertEquals(productList.get(1), response.get(1));
                }
            }, new Action1<Throwable>() {
                @Override
                public void call(Throwable throwable) {
                    Assert.fail();
                }
            });

        Observable
            .just(collectionList)
            .doOnNext(new ProductCacheRxHookProvider(cacheHook).getCollectionPageCacheHook(1, 1))
            .subscribe(new Action1<List<Collection>>() {
                @Override
                public void call(List<Collection> response) {
                    Assert.assertEquals(collectionList.get(0), response.get(0));
                    Assert.assertEquals(collectionList.get(1), response.get(1));
                }
            }, new Action1<Throwable>() {
                @Override
                public void call(Throwable throwable) {
                    Assert.fail();
                }
            });
    }

    @Test
    public void cacheGetProductPage() {
        final Response<ProductListings> response = Response.success(productListings);
        final Observable<Response<ProductListings>> responseObservable = Observable.just(response);
        Mockito.when(productRetrofitService.getProductPage(Mockito.anyString(), Mockito.anyInt(), Mockito.anyInt())).thenReturn(responseObservable);
        buyClient.getProducts(1, new Callback<List<Product>>() {
            @Override
            public void success(List<Product> response) {
                Assert.assertEquals(productList.get(0), response.get(0));
                Assert.assertEquals(productList.get(1), response.get(1));
            }

            @Override
            public void failure(BuyClientError error) {
                Assert.fail();
            }
        });
        Mockito.verify(productCacheHook, Mockito.times(1)).cacheProducts(1, productPageSize, productList);
    }

    @Test
    public void cacheGetProduct() {
        final Response<ProductListings> response = Response.success(productListings);
        final Observable<Response<ProductListings>> responseObservable = Observable.just(response);
        Mockito.when(productRetrofitService.getProducts(Mockito.anyString(), Mockito.anyString())).thenReturn(responseObservable);
        buyClient.getProduct(1L, new Callback<Product>() {
            @Override
            public void success(Product response) {
                Assert.assertEquals(product1, response);
            }

            @Override
            public void failure(BuyClientError error) {
                Assert.fail();
            }
        });
        Mockito.verify(productCacheHook, Mockito.times(1)).cacheProduct(product1);
    }

    @Test
    public void cacheGetProductsByIds() {
        final Response<ProductListings> response = Response.success(productListings);
        final Observable<Response<ProductListings>> responseObservable = Observable.just(response);
        Mockito.when(productRetrofitService.getProducts(Mockito.anyString(), Mockito.anyString())).thenReturn(responseObservable);
        buyClient.getProducts(Arrays.asList(1L, 2L), new Callback<List<Product>>() {
            @Override
            public void success(List<Product> response) {
                Assert.assertEquals(productList.get(0), response.get(0));
                Assert.assertEquals(productList.get(1), response.get(1));
            }

            @Override
            public void failure(BuyClientError error) {
                Assert.fail();
            }
        });
        Mockito.verify(productCacheHook, Mockito.times(1)).cacheProducts(productList);
    }

    @Test
    public void cacheGetCollectionProducts() {
        final Response<ProductListings> response = Response.success(productListings);
        final Observable<Response<ProductListings>> responseObservable = Observable.just(response);
        Mockito.when(productRetrofitService.getProducts(Mockito.anyString(), Mockito.anyLong(), Mockito.anyInt(), Mockito.anyInt(), Mockito.anyString())).thenReturn(responseObservable);
        buyClient.getProducts(1, 1L, Collection.SortOrder.BEST_SELLING, new Callback<List<Product>>() {
            @Override
            public void success(List<Product> response) {
                Assert.assertEquals(productList.get(0), response.get(0));
                Assert.assertEquals(productList.get(1), response.get(1));
            }

            @Override
            public void failure(BuyClientError error) {
                Assert.fail();
            }
        });
        Mockito.verify(productCacheHook, Mockito.times(1)).cacheProducts(1L, 1, productPageSize, productList);
    }

    @Test
    public void cacheGetCollections() {
        final Response<CollectionListings> response = Response.success(collectionListings);
        final Observable<Response<CollectionListings>> responseObservable = Observable.just(response);
        Mockito.when(productRetrofitService.getCollectionPage(Mockito.anyString(), Mockito.anyInt(), Mockito.anyInt())).thenReturn(responseObservable);
        buyClient.getCollections(1, new Callback<List<Collection>>() {
            @Override
            public void success(List<Collection> response) {
                Assert.assertEquals(collectionList.get(0), response.get(0));
                Assert.assertEquals(collectionList.get(1), response.get(1));
            }

            @Override
            public void failure(BuyClientError error) {
                Assert.fail();
            }
        });
        Mockito.verify(productCacheHook, Mockito.times(1)).cacheCollections(1, productPageSize, collectionList);
    }
}
