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

import com.shopify.buy.dataprovider.cache.ProductCacheHook;
import com.shopify.buy.model.Collection;
import com.shopify.buy.model.Product;

import java.util.List;

import rx.functions.Action1;

final class ProductCacheRxHookProvider {

    private static final Action1 EMPTY_CACHE_HOOK = new Action1() {
        @Override
        public void call(Object o) {
        }
    };

    final ProductCacheHook cacheHook;

    ProductCacheRxHookProvider(final ProductCacheHook cacheHook) {
        this.cacheHook = cacheHook;
    }

    @SuppressWarnings("unchecked")
    Action1<List<Product>> getProductPageHook(final int page, final int pageSize) {
        if (cacheHook == null) {
            return EMPTY_CACHE_HOOK;
        } else {
            return new Action1<List<Product>>() {
                @Override
                public void call(final List<Product> products) {
                    try {
                        cacheHook.cacheProducts(page, pageSize, products);
                    } catch (Exception e) {

                    }
                }
            };
        }
    }

    @SuppressWarnings("unchecked")
    Action1<Product> getProductWithHandleHook(final String handle) {
        if (cacheHook == null) {
            return EMPTY_CACHE_HOOK;
        } else {
            return new Action1<Product>() {
                @Override
                public void call(final Product product) {
                    try {
                        cacheHook.cacheProductWithHandle(handle, product);
                    } catch (Exception e) {

                    }
                }
            };
        }
    }

    @SuppressWarnings("unchecked")
    Action1<Product> getProductHook() {
        if (cacheHook == null) {
            return EMPTY_CACHE_HOOK;
        } else {
            return new Action1<Product>() {
                @Override
                public void call(final Product product) {
                    try {
                        cacheHook.cacheProduct(product);
                    } catch (Exception e) {

                    }
                }
            };
        }
    }

    @SuppressWarnings("unchecked")
    Action1<List<Product>> getProductsHook() {
        if (cacheHook == null) {
            return EMPTY_CACHE_HOOK;
        } else {
            return new Action1<List<Product>>() {
                @Override
                public void call(final List<Product> products) {
                    try {
                        cacheHook.cacheProducts(products);
                    } catch (Exception e) {

                    }
                }
            };
        }
    }

    @SuppressWarnings("unchecked")
    Action1<List<Product>> getCollectionProductPageHook(final Long collectionId, final int page, final int pageSize) {
        if (cacheHook == null) {
            return EMPTY_CACHE_HOOK;
        } else {
            return new Action1<List<Product>>() {
                @Override
                public void call(final List<Product> products) {
                    try {
                        cacheHook.cacheProducts(collectionId, page, pageSize, products);
                    } catch (Exception e) {

                    }
                }
            };
        }
    }

    @SuppressWarnings("unchecked")
    Action1<List<Collection>> getCollectionPageHook(final int page, final int pageSize) {
        if (cacheHook == null) {
            return EMPTY_CACHE_HOOK;
        } else {
            return new Action1<List<Collection>>() {
                @Override
                public void call(final List<Collection> collections) {
                    try {
                        cacheHook.cacheCollections(page, pageSize, collections);
                    } catch (Exception e) {

                    }
                }
            };
        }
    }
}
