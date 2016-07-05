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

import java.util.List;

/**
 * Cache hook that will be triggered by {@link com.shopify.buy.dataprovider.ProductService}. By default all caching operates
 * on background thread.
 */
public interface ProductCacheHook {

    /**
     * Caches product page list
     */
    void cacheProducts(int page, int pageSize, List<Product> products);

    /**
     * Caches product by handle
     */
    void cacheProductWithHandle(String handle, Product product);

    /**
     * Caches product
     */
    void cacheProduct(Product product);

    /**
     * Caches list of products
     */
    void cacheProducts(List<Product> products);

    /**
     * Caches product page list for specified collection id
     */
    void cacheProducts(Long collectionId, int page, int pageSize, List<Product> products);

    /**
     * Caches collection page list
     */
    void cacheCollections(int page, int pageSize, List<Collection> collections);
}
