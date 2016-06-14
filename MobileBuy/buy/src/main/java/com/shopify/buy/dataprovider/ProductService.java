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

import com.shopify.buy.model.Collection;
import com.shopify.buy.model.Product;

import java.util.List;

import rx.Observable;

/**
 * Service that provides Product API endpoints.
 */
public interface ProductService {

    /**
     * Returns the page size used for paged product API queries.
     *
     * @return page size
     */
    int getProductPageSize();

    /**
     * Fetch a page of products
     *
     * @param page     the 1-based page index. The page size is set by {@link BuyClientBuilder#productPageSize} configuration.
     * @param callback the {@link Callback} that will be used to indicate the response from the asynchronous network operation, not null
     * @return cancelable task
     */
    CancellableTask getProducts(int page, Callback<List<Product>> callback);

    /**
     * Fetch a page of products
     *
     * @param page the 1-based page index. The page size is set by {@link BuyClientBuilder#productPageSize} configuration.
     * @return cold observable that emits the requested list of products
     */
    Observable<List<Product>> getProducts(int page);

    /**
     * Fetch the product with the specified handle
     *
     * @param handle   the handle for the product to fetch
     * @param callback the {@link Callback} that will be used to indicate the response from the asynchronous network operation, not null
     * @return cancelable task
     */
    CancellableTask getProductByHandle(String handle, Callback<Product> callback);

    /**
     * Fetch the product with the specified handle
     *
     * @param handle the handle for the product to fetch
     * @return cold observable that emits requested product with the specified handle
     */
    Observable<Product> getProductByHandle(String handle);

    /**
     * Fetch a single Product
     *
     * @param productId the productId for the product to fetch
     * @param callback  the {@link Callback} that will be used to indicate the response from the asynchronous network operation, not null
     * @return cancelable task
     */
    CancellableTask getProduct(Long productId, Callback<Product> callback);

    /**
     * Fetch a single Product
     *
     * @param productId the productId for the product to fetch
     * @return cold observable that emits requested single product
     */
    Observable<Product> getProduct(Long productId);

    /**
     * Fetch a list of Products
     *
     * @param productIds a List of the productIds to fetch
     * @param callback   the {@link Callback} that will be used to indicate the response from the asynchronous network operation, not null
     * @return cancelable task
     */
    CancellableTask getProducts(List<Long> productIds, Callback<List<Product>> callback);

    /**
     * Fetch a list of Products
     *
     * @param productIds a List of the productIds to fetch
     * @return cold observable that emits requested list of products
     */
    Observable<List<Product>> getProducts(List<Long> productIds);

    /**
     * Fetch the list of Products in a Collection using the sort order defined in the shop admin
     *
     * @param page         the 1-based page index. The page size is set by {@link BuyClientBuilder#productPageSize} configuration.
     * @param collectionId the collectionId that we want to fetch products for
     * @param callback     the {@link Callback} that will be used to indicate the response from the asynchronous network operation, not null
     * @return cancelable task
     */
    CancellableTask getProducts(int page, Long collectionId, Callback<List<Product>> callback);

    /**
     * Fetch the list of Products in a Collection using the sort order defined in the shop admin
     *
     * @param page         the 1-based page index. The page size is set by {@link BuyClientBuilder#productPageSize} configuration.
     * @param collectionId the collectionId that we want to fetch products for
     * @return cold observable that emits requested list of products
     */
    Observable<List<Product>> getProducts(int page, Long collectionId);

    /**
     * Fetch the list of Products in a Collection
     *
     * @param page         the 1-based page index. The page size is set by {@link BuyClientBuilder#productPageSize} configuration.
     * @param collectionId the collectionId that we want to fetch products for
     * @param sortOrder    the sort order for the collection.
     * @param callback     the {@link Callback} that will be used to indicate the response from the asynchronous network operation, not null
     * @return cancelable task
     */
    CancellableTask getProducts(int page, Long collectionId, Collection.SortOrder sortOrder, Callback<List<Product>> callback);

    /**
     * Fetch the list of Products in a Collection
     *
     * @param page         the 1-based page index. The page size is set by {@link BuyClientBuilder#productPageSize} configuration.
     * @param collectionId the collectionId that we want to fetch products for
     * @param sortOrder    the sort order for the collection.
     * @return cold observable that emits requested list of products
     */
    Observable<List<Product>> getProducts(int page, Long collectionId, Collection.SortOrder sortOrder);

    /**
     * Fetch a page of collections
     *
     * @param page     the 1-based page index. The page size is set by {@link BuyClientBuilder#productPageSize} configuration.
     * @param callback the {@link Callback} that will be used to indicate the response from the asynchronous network operation, not null
     * @return cancelable task
     */
    CancellableTask getCollections(int page, Callback<List<Collection>> callback);

    /**
     * Fetch a page of collections
     *
     * @param page the 1-based page index. The page size is set by {@link BuyClientBuilder#productPageSize} configuration.
     * @return cold observable that emits requested list of collections
     */
    Observable<List<Collection>> getCollections(int page);
}
