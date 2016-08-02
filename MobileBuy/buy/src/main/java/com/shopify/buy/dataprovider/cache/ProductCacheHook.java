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
package com.shopify.buy.dataprovider.cache;

import com.shopify.buy.model.Collection;
import com.shopify.buy.model.Product;
import com.shopify.buy.model.ProductTag;

import java.util.List;
import java.util.Set;

/**
 * Cache hook that will be triggered by {@link com.shopify.buy.dataprovider.ProductService}. By default all caching operates
 * on the background thread.
 */
public interface ProductCacheHook {

    /**
     * Caches product
     *
     * @param product product to cache
     */
    void cacheProduct(Product product);

    /**
     * Caches product requested by handle
     *
     * @param handle  product handle
     * @param product product to cache
     */
    void cacheProduct(String handle, Product product);

    /**
     * Caches products
     *
     * @param productIds product ids
     * @param products   products to cache
     */
    void cacheProducts(List<Long> productIds, List<Product> products);

    /**
     * Caches products
     *
     * @param page         page index
     * @param collectionId collection id
     * @param tags         filter tags
     * @param sortOrder    collection product sort order
     * @param products     products to cache
     */
    void cacheProducts(int page, Long collectionId, Set<String> tags, Collection.SortOrder sortOrder, List<Product> products);

    /**
     * Caches product tags
     *
     * @param page page index
     * @param tags filter tags
     */
    void cacheProductTags(int page, List<ProductTag> tags);

    /**
     * Caches collections
     *
     * @param page page index
     */
    void cacheCollections(int page, List<Collection> collections);

    /**
     * Caches collections
     *
     * @param collectionIds collection ids
     * @param collections   collections to cache
     */
    void cacheCollections(List<Long> collectionIds, List<Collection> collections);

    /**
     * Caches collection
     *
     * @param collection collection to cache
     */
    void cacheCollection(Collection collection);

    /**
     * Caches collection requested by handle
     *
     * @param handle     collection handle
     * @param collection collection to cache
     */
    void cacheCollection(String handle, Collection collection);

}
