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
import java.util.Set;

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
     * @param handle   the handle for the product to fetch, not null or empty
     * @param callback the {@link Callback} that will be used to indicate the response from the asynchronous network operation, not null
     * @return cancelable task
     */
    CancellableTask getProductByHandle(String handle, Callback<Product> callback);

    /**
     * Fetch the product with the specified handle
     *
     * @param handle the handle for the product to fetch, not null or empty
     * @return cold observable that emits requested product with the specified handle
     */
    Observable<Product> getProductByHandle(String handle);

    /**
     * Fetch a single Product
     *
     * @param productId the productId for the product to fetch, not null
     * @param callback  the {@link Callback} that will be used to indicate the response from the asynchronous network operation, not null
     * @return cancelable task
     */
    CancellableTask getProduct(Long productId, Callback<Product> callback);

    /**
     * Fetch a single Product
     *
     * @param productId the productId for the product to fetch, not null
     * @return cold observable that emits requested single product
     */
    Observable<Product> getProduct(Long productId);

    /**
     * Fetch a list of Products
     *
     * @param productIds a List of the productIds to fetch, not null or empty
     * @param callback   the {@link Callback} that will be used to indicate the response from the asynchronous network operation, not null
     * @return cancelable task
     */
    CancellableTask getProducts(List<Long> productIds, Callback<List<Product>> callback);

    /**
     * Fetch a list of Products
     *
     * @param productIds a List of the productIds to fetch, not null or empty
     * @return cold observable that emits requested list of products
     */
    Observable<List<Product>> getProducts(List<Long> productIds);

    /**
     * Fetch the collection with the specified handle
     *
     * @param handle   the handle for the collection to fetch, not null or empty
     * @param callback the {@link Callback} that will be used to indicate the response from the asynchronous network operation, not null
     * @return cancelable task
     */
    CancellableTask getCollectionByHandle(String handle, Callback<Collection> callback);

    /**
     * Fetch the collection with the specified handle
     *
     * @param handle the handle for the collection to fetch, not null or empty
     * @return cold observable that emits requested product with the specified handle
     */
    Observable<Collection> getCollectionByHandle(String handle);

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

    /**
     * Fetch collections by ids
     *
     * @param collectionIds a list of the ids to fetch, not null or empty
     * @param callback      the {@link Callback} that will be used to indicate the response from the asynchronous network operation, not null
     * @return cancelable task
     */
    CancellableTask getCollections(List<Long> collectionIds, Callback<List<Collection>> callback);

    /**
     * Fetch a page of collections
     *
     * @param collectionIds a List of the ids to fetch, not null or empty
     * @return cold observable that emits requested list of collections
     */
    Observable<List<Collection>> getCollections(List<Long> collectionIds);

    /**
     * Fetch a page of product tags
     *
     * @param page     the 1-based page index. The page size is set by {@link BuyClientBuilder#productPageSize} configuration.
     * @param callback the {@link Callback} that will be used to indicate the response from the asynchronous network operation, not null
     * @return cancelable task
     */
    CancellableTask getProductTags(int page, Callback<List<String>> callback);

    /**
     * Fetch a page of product tags
     *
     * @param page the 1-based page index. The page size is set by {@link BuyClientBuilder#productPageSize} configuration.
     * @return cold observable that emits requested set of tags
     */
    Observable<List<String>> getProductTags(int page);


    /**
     * Fetch the products filtered by specified tags
     *
     * @param page     the 1-based page index. The page size is set by {@link BuyClientBuilder#productPageSize} configuration.
     * @param tags     set of tags which each product must contain, can be null
     * @param callback the {@link Callback} that will be used to indicate the response from the asynchronous network operation, not null
     * @return cancelable task
     */
    CancellableTask getProducts(int page, Set<String> tags, Callback<List<Product>> callback);

    /**
     * Fetch the products filtered by specified tags
     *
     * @param page the 1-based page index. The page size is set by {@link BuyClientBuilder#productPageSize} configuration.
     * @param tags set of tags which each product must contain, can be null
     * @return cold observable that emits requested list of product
     */
    Observable<List<Product>> getProducts(int page, Set<String> tags);

    /**
     * Fetch the products filtered by specified collection and optional tags
     *
     * @param page         the 1-based page index. The page size is set by {@link BuyClientBuilder#productPageSize} configuration.
     * @param collectionId the collectionId that we want to fetch products for, can't be null
     * @param tags         set of tags which each product must contain, can be null
     * @param sortOrder    the sort order of products for the specified collection,
     *                     in case of {@code null} value {@link Collection.SortOrder#COLLECTION_DEFAULT} will be used as default,
     *                     will be ignored if specified collection id is {@code null}
     * @param callback     the {@link Callback} that will be used to indicate the response from the asynchronous network operation, not null
     * @return cancelable task
     */
    CancellableTask getProducts(int page, Long collectionId, Set<String> tags, Collection.SortOrder sortOrder, Callback<List<Product>> callback);

    /**
     * Fetch the products filtered by specified collection and optional tags
     *
     * @param page         the 1-based page index. The page size is set by {@link BuyClientBuilder#productPageSize} configuration.
     * @param collectionId the collectionId that we want to fetch products for, can't be null
     * @param tags         set of tags which each product must contain, can be null
     * @param sortOrder    the sort order of products for the specified collection,
     *                     in case of {@code null} value {@link Collection.SortOrder#COLLECTION_DEFAULT} will be used as default,
     *                     will be ignored if specified collection id is {@code null}
     * @return cold observable that emits requested list of product
     */
    Observable<List<Product>> getProducts(int page, Long collectionId, Set<String> tags, Collection.SortOrder sortOrder);
}
