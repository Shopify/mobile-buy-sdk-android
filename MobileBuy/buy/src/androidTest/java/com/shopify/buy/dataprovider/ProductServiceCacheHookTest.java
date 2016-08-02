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
import com.shopify.buy.model.ProductTag;
import com.shopify.buy.model.internal.CollectionListings;
import com.shopify.buy.model.internal.ProductListings;
import com.shopify.buy.model.internal.ProductTagsWrapper;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import retrofit2.Response;
import rx.Observable;
import rx.functions.Action1;

@RunWith(AndroidJUnit4.class)
public class ProductServiceCacheHookTest extends ShopifyAndroidTestCase {

    Product product1;

    Product product2;

    List<Product> productList;

    ProductTag productTag1;

    ProductTag productTag2;

    List<ProductTag> productTagList;

    ProductTagsWrapper productTagsWrapper;

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

        productTag1 = new ProductTag() {
            {
                id = 1L;
                title = "test1";

            }
        };

        productTag2 = new ProductTag() {
            {
                id = 2L;
                title = "test2";
            }
        };

        productTagList = Arrays.asList(productTag1, productTag2);

        productTagsWrapper = new ProductTagsWrapper() {
            @Override
            public List<ProductTag> getContent() {
                return productTagList;
            }

            @Override
            public List<ProductTag> getTags() {
                return productTagList;
            }
        };

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

    private void testCacheHook(final ProductCacheHook cacheHook) {
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
            .just(product1)
            .doOnNext(new ProductCacheRxHookProvider(cacheHook).getProductCacheHook("test handle"))
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
            .doOnNext(new ProductCacheRxHookProvider(cacheHook).getProductsCacheHook(Arrays.asList(1L, 2L)))
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
            .just(productList)
            .doOnNext(new ProductCacheRxHookProvider(cacheHook).getProductsCacheHook(1, 1L, null, null))
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
            .just(productTagList)
            .doOnNext(new ProductCacheRxHookProvider(cacheHook).getProductTagsCacheHook(1))
            .subscribe(new Action1<List<ProductTag>>() {
                @Override
                public void call(List<ProductTag> response) {
                    Assert.assertEquals(productTagList.get(0), response.get(0));
                    Assert.assertEquals(productTagList.get(1), response.get(1));
                }
            }, new Action1<Throwable>() {
                @Override
                public void call(Throwable throwable) {
                    Assert.fail();
                }
            });

        Observable
            .just(collection1)
            .doOnNext(new ProductCacheRxHookProvider(cacheHook).getCollectionCacheHook())
            .subscribe(new Action1<Collection>() {
                @Override
                public void call(Collection response) {
                    Assert.assertEquals(collection1, response);
                }
            }, new Action1<Throwable>() {
                @Override
                public void call(Throwable throwable) {
                    Assert.fail();
                }
            });

        Observable
            .just(collection1)
            .doOnNext(new ProductCacheRxHookProvider(cacheHook).getCollectionCacheHook("test handle"))
            .subscribe(new Action1<Collection>() {
                @Override
                public void call(Collection response) {
                    Assert.assertEquals(collection1, response);
                }
            }, new Action1<Throwable>() {
                @Override
                public void call(Throwable throwable) {
                    Assert.fail();
                }
            });

        Observable
            .just(collectionList)
            .doOnNext(new ProductCacheRxHookProvider(cacheHook).getCollectionsCacheHook(1))
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

        Observable
            .just(collectionList)
            .doOnNext(new ProductCacheRxHookProvider(cacheHook).getCollectionsCacheHook(Arrays.asList(1L, 2L)))
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
    public void cacheWithoutHook() {
        testCacheHook(null);
    }

    @Test
    public void cacheWithHookException() {
        final ProductCacheHook cacheHook = Mockito.mock(ProductCacheHook.class);
        Mockito.doThrow(new RuntimeException()).when(cacheHook).cacheProduct(Mockito.any(Product.class));
        Mockito.doThrow(new RuntimeException()).when(cacheHook).cacheProduct(Mockito.anyString(), Mockito.any(Product.class));
        Mockito.doThrow(new RuntimeException()).when(cacheHook).cacheProducts(Mockito.anyList(), Mockito.anyList());
        Mockito.doThrow(new RuntimeException()).when(cacheHook).cacheProducts(Mockito.anyInt(), Mockito.anyLong(), Mockito.anySet(), Mockito.any(Collection.SortOrder.class), Mockito.anyList());
        Mockito.doThrow(new RuntimeException()).when(cacheHook).cacheProductTags(Mockito.anyInt(), Mockito.anyList());
        Mockito.doThrow(new RuntimeException()).when(cacheHook).cacheCollection(Mockito.any(Collection.class));
        Mockito.doThrow(new RuntimeException()).when(cacheHook).cacheCollection(Mockito.anyString(), Mockito.any(Collection.class));
        Mockito.doThrow(new RuntimeException()).when(cacheHook).cacheCollections(Mockito.anyInt(), Mockito.anyList());
        Mockito.doThrow(new RuntimeException()).when(cacheHook).cacheCollections(Mockito.anyList(), Mockito.anyList());
        testCacheHook(cacheHook);
    }

    @Test
    public void cacheGetProducts() {
        final Observable<Response<ProductListings>> responseObservable = Observable.just(Response.success(productListings));
        Mockito.when(productRetrofitService.getProducts(Mockito.anyString(), Mockito.anyLong(), Mockito.anyString(), Mockito.anyString(), Mockito.anyInt(), Mockito.anyInt())).thenReturn(responseObservable);
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
        Mockito.verify(productCacheHook, Mockito.times(1)).cacheProducts(1, null, null, null, productList);
    }

    @Test
    public void testGetProductByHandle() {
        final Observable<Response<ProductListings>> responseObservable = Observable.just(Response.success(productListings));
        Mockito.when(productRetrofitService.getProductByHandle(Mockito.anyString(), Mockito.anyString())).thenReturn(responseObservable);
        buyClient.getProductByHandle("test", new Callback<Product>() {
            @Override
            public void success(Product response) {
                Assert.assertEquals(product1, response);
            }

            @Override
            public void failure(BuyClientError error) {
                Assert.fail();
            }
        });
        Mockito.verify(productCacheHook, Mockito.times(1)).cacheProduct("test", product1);
    }

    @Test
    public void cacheGetProduct() {
        final Observable<Response<ProductListings>> responseObservable = Observable.just(Response.success(productListings));
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
        final Observable<Response<ProductListings>> responseObservable = Observable.just(Response.success(productListings));
        Mockito.when(productRetrofitService.getProducts(Mockito.anyString(), Mockito.anyString())).thenReturn(responseObservable);
        final List<Long> productIds = Arrays.asList(1L, 2L);
        buyClient.getProducts(productIds, new Callback<List<Product>>() {
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
        Mockito.verify(productCacheHook, Mockito.times(1)).cacheProducts(productIds, productList);
    }

    @Test
    public void cacheGetCollectionByHandle() {
        final Observable<Response<CollectionListings>> responseObservable = Observable.just(Response.success(collectionListings));
        Mockito.when(productRetrofitService.getCollectionByHandle(Mockito.anyString(), Mockito.anyString())).thenReturn(responseObservable);
        buyClient.getCollectionByHandle("test", new Callback<Collection>() {
            @Override
            public void success(Collection response) {
                Assert.assertEquals(collection1, response);
            }

            @Override
            public void failure(BuyClientError error) {
                Assert.fail();
            }
        });
        Mockito.verify(productCacheHook, Mockito.times(1)).cacheCollection("test", collection1);
    }

    @Test
    public void cacheGetCollections() {
        final Observable<Response<CollectionListings>> responseObservable = Observable.just(Response.success(collectionListings));
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
        Mockito.verify(productCacheHook, Mockito.times(1)).cacheCollections(1, collectionList);
    }

    @Test
    public void cacheGetCollectionsByIds() {
        final Observable<Response<CollectionListings>> responseObservable = Observable.just(Response.success(collectionListings));
        Mockito.when(productRetrofitService.getCollections(Mockito.anyString(), Mockito.anyString())).thenReturn(responseObservable);
        final List<Long> collectionIds = Arrays.asList(1L, 2L);
        buyClient.getCollections(collectionIds, new Callback<List<Collection>>() {
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
        Mockito.verify(productCacheHook, Mockito.times(1)).cacheCollections(collectionIds, collectionList);
    }

    @Test
    public void cacheProductTags() {
        final Observable<Response<ProductTagsWrapper>> responseObservable = Observable.just(Response.success(productTagsWrapper));
        Mockito.when(productRetrofitService.getProductTagPage(Mockito.anyString(), Mockito.anyInt(), Mockito.anyInt())).thenReturn(responseObservable);
        buyClient.getProductTags(1, new Callback<List<String>>() {
            @Override
            public void success(List<String> response) {
                Assert.assertEquals(productTagList.get(0).getTitle(), response.get(0));
                Assert.assertEquals(productTagList.get(1).getTitle(), response.get(1));
            }

            @Override
            public void failure(BuyClientError error) {
                Assert.fail();
            }
        });
        Mockito.verify(productCacheHook, Mockito.times(1)).cacheProductTags(1, productTagList);
    }

    @Test
    public void cacheGetProductsByTags() {
        final Response<ProductListings> response = Response.success(productListings);
        final Observable<Response<ProductListings>> responseObservable = Observable.just(response);
        Mockito.when(productRetrofitService.getProducts(Mockito.anyString(), Mockito.anyLong(), Mockito.anyString(), Mockito.anyString(), Mockito.anyInt(), Mockito.anyInt())).thenReturn(responseObservable);
        final Set<String> productTags = new HashSet<>(Arrays.asList("test1", "test2"));
        buyClient.getProducts(1, productTags, new Callback<List<Product>>() {
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
        Mockito.verify(productCacheHook, Mockito.times(1)).cacheProducts(1, null, productTags, null, productList);
    }

    @Test
    public void cacheGetCollectionProducts() {
        final Response<ProductListings> response = Response.success(productListings);
        final Observable<Response<ProductListings>> responseObservable = Observable.just(response);
        Mockito.when(productRetrofitService.getProducts(Mockito.anyString(), Mockito.anyLong(), Mockito.anyString(), Mockito.anyString(), Mockito.anyInt(), Mockito.anyInt())).thenReturn(responseObservable);
        final Set<String> productTags = new HashSet<>(Arrays.asList("test1", "test2"));
        buyClient.getProducts(1, 1L, productTags, Collection.SortOrder.BEST_SELLING, new Callback<List<Product>>() {
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
        Mockito.verify(productCacheHook, Mockito.times(1)).cacheProducts(1, 1L, productTags, Collection.SortOrder.BEST_SELLING, productList);
    }
}
