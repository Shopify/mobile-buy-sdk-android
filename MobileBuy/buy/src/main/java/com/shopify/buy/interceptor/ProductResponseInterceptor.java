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
package com.shopify.buy.interceptor;

import com.shopify.buy.model.Collection;
import com.shopify.buy.model.Product;

import java.util.List;
import java.util.Set;

import rx.Observable;

/**
 * Represents response interceptor for Product API calls {@link com.shopify.buy.dataprovider.ProductService}
 */
public interface ProductResponseInterceptor {

    /**
     * Intercept responses returned by:
     * {@link com.shopify.buy.dataprovider.ProductService#getProducts(int)}
     * {@link com.shopify.buy.dataprovider.ProductService#getProducts(int, Set)}
     * {@link com.shopify.buy.dataprovider.ProductService#getProducts(int, Long, Set, Collection.SortOrder)}
     *
     * @param page               the 1-based page index
     * @param collectionId       the collectionId that we want to fetch products for
     * @param tags               set of tags which each product must contain
     * @param sortOrder          the sort order of products for the specified collection
     * @param originalObservable original request observable
     * @return modified or the same version of original request observable
     */
    Observable<List<Product>> getProducts(int page, Long collectionId, Set<String> tags, Collection.SortOrder sortOrder, Observable<List<Product>> originalObservable);

    /**
     * Intercept responses returned by {@link com.shopify.buy.dataprovider.ProductService#getProductByHandle(String)}
     *
     * @param handle             the handle for the product to fetch
     * @param originalObservable original request observable
     * @return modified or the same version of original request observable
     */
    Observable<Product> getProductByHandle(String handle, Observable<Product> originalObservable);

    /**
     * Intercept responses returned by {@link com.shopify.buy.dataprovider.ProductService#getProduct(Long)}
     *
     * @param productId          the productId for the product to fetch
     * @param originalObservable original request observable
     * @return modified or the same version of original request observable
     */
    Observable<Product> getProduct(Long productId, Observable<Product> originalObservable);

    /**
     * Intercept responses returned by {@link com.shopify.buy.dataprovider.ProductService#getProducts(List)}
     *
     * @param productIds         a List of the productIds to fetch
     * @param originalObservable original request observable
     * @return modified or the same version of original request observable
     */
    Observable<List<Product>> getProducts(List<Long> productIds, Observable<List<Product>> originalObservable);

    /**
     * Intercept responses returned by {@link com.shopify.buy.dataprovider.ProductService#getCollectionByHandle(String)}
     *
     * @param handle             the handle for the collection to fetch
     * @param originalObservable original request observable
     * @return modified or the same version of original request observable
     */
    Observable<Collection> getCollectionByHandle(String handle, Observable<Collection> originalObservable);

    /**
     * Intercept responses returned by {@link com.shopify.buy.dataprovider.ProductService#getCollections(int)}
     *
     * @param page               the 1-based page index
     * @param originalObservable original request observable
     * @return modified or the same version of original request observable
     */
    Observable<List<Collection>> getCollections(int page, Observable<List<Collection>> originalObservable);

    /**
     * Intercept responses returned by {@link com.shopify.buy.dataprovider.ProductService#getCollections(List)}
     *
     * @param collectionIds      a List of the ids to fetch
     * @param originalObservable original request observable
     * @return modified or the same version of original request observable
     */
    Observable<List<Collection>> getCollections(List<Long> collectionIds, Observable<List<Collection>> originalObservable);
}
