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

import android.text.TextUtils;

import com.shopify.buy.model.Collection;
import com.shopify.buy.model.Product;
import com.shopify.buy.model.internal.CollectionListings;
import com.shopify.buy.model.internal.ProductListings;

import java.util.List;

import retrofit2.Retrofit;
import rx.Observable;
import rx.Scheduler;

/**
 * Default implementation of {@link ProductService}
 */
final class ProductServiceDefault implements ProductService {

    final ProductRetrofitService retrofitService;

    final String appId;

    final int pageSize;

    final NetworkRetryPolicyProvider networkRetryPolicyProvider;

    final Scheduler callbackScheduler;

    ProductServiceDefault(
        final Retrofit retrofit,
        final String appId,
        final int pageSize,
        final NetworkRetryPolicyProvider networkRetryPolicyProvider,
        final Scheduler callbackScheduler
    ) {
        this.retrofitService = retrofit.create(ProductRetrofitService.class);
        this.appId = appId;
        this.pageSize = pageSize;
        this.networkRetryPolicyProvider = networkRetryPolicyProvider;
        this.callbackScheduler = callbackScheduler;
    }

    @Override
    public int getProductPageSize() {
        return pageSize;
    }

    @Override
    public CancellableTask getProducts(final int page, final Callback<List<Product>> callback) {
        return new CancellableTaskSubscriptionWrapper(getProducts(page).subscribe(new InternalCallbackSubscriber<>(callback)));
    }

    @Override
    public Observable<List<Product>> getProducts(final int page) {
        if (page < 1) {
            throw new IllegalArgumentException("page is a 1-based index, value cannot be less than 1");
        }

        return retrofitService
            .getProductPage(appId, page, pageSize)
            .retryWhen(networkRetryPolicyProvider.provide())
            .doOnNext(new RetrofitSuccessHttpStatusCodeHandler<>())
            .compose(new UnwrapRetrofitBodyTransformer<ProductListings, List<Product>>())
            .onErrorResumeNext(new BuyClientExceptionHandler<List<Product>>())
            .observeOn(callbackScheduler);
    }

    @Override
    public CancellableTask getProductByHandle(final String handle, final Callback<Product> callback) {
        return new CancellableTaskSubscriptionWrapper(getProductByHandle(handle).subscribe(new InternalCallbackSubscriber<>(callback)));
    }

    @Override
    public Observable<Product> getProductByHandle(final String handle) {
        if (handle == null) {
            throw new NullPointerException("handle cannot be null");
        }

        if (TextUtils.isEmpty(handle)) {
            throw new IllegalArgumentException("handle cannot be empty");
        }

        return retrofitService
            .getProductWithHandle(appId, handle)
            .retryWhen(networkRetryPolicyProvider.provide())
            .doOnNext(new RetrofitSuccessHttpStatusCodeHandler<>())
            .compose(new UnwrapRetrofitBodyTransformer<ProductListings, List<Product>>())
            .compose(new FirstListItemOrDefaultTransformer<Product>())
            .onErrorResumeNext(new BuyClientExceptionHandler<Product>())
            .observeOn(callbackScheduler);
    }

    @Override
    public CancellableTask getProduct(final Long productId, final Callback<Product> callback) {
        return new CancellableTaskSubscriptionWrapper(getProduct(productId).subscribe(new InternalCallbackSubscriber<>(callback)));
    }

    @Override
    public Observable<Product> getProduct(final Long productId) {
        if (productId == null) {
            throw new NullPointerException("productId cannot be null");
        }

        return retrofitService
            .getProducts(appId, String.valueOf(productId))
            .retryWhen(networkRetryPolicyProvider.provide())
            .doOnNext(new RetrofitSuccessHttpStatusCodeHandler<>())
            .compose(new UnwrapRetrofitBodyTransformer<ProductListings, List<Product>>())
            .compose(new FirstListItemOrDefaultTransformer<Product>())
            .onErrorResumeNext(new BuyClientExceptionHandler<Product>())
            .observeOn(callbackScheduler);
    }

    @Override
    public CancellableTask getProducts(final List<Long> productIds, final Callback<List<Product>> callback) {
        return new CancellableTaskSubscriptionWrapper(getProducts(productIds).subscribe(new InternalCallbackSubscriber<>(callback)));
    }

    @Override
    public Observable<List<Product>> getProducts(final List<Long> productIds) {
        if (productIds == null) {
            throw new NullPointerException("productIds List cannot be null");
        }
        if (productIds.size() < 1) {
            throw new IllegalArgumentException("productIds List cannot be empty");
        }

        // All product responses from the server are wrapped in a ProductListings object
        // The same endpoint is used for single and multiple product queries.
        // For this call we will query with multiple ids.
        // The returned product array will contain products for each id found.
        // If no ids were found, the array will be empty
        final String queryString = TextUtils.join(",", productIds.toArray());
        return retrofitService
            .getProducts(appId, queryString)
            .retryWhen(networkRetryPolicyProvider.provide())
            .doOnNext(new RetrofitSuccessHttpStatusCodeHandler<>())
            .compose(new UnwrapRetrofitBodyTransformer<ProductListings, List<Product>>())
            .onErrorResumeNext(new BuyClientExceptionHandler<List<Product>>())
            .observeOn(callbackScheduler);
    }

    @Override
    public CancellableTask getProducts(int page, Long collectionId, final Callback<List<Product>> callback) {
        return getProducts(page, collectionId, Collection.SortOrder.COLLECTION_DEFAULT, callback);
    }

    @Override
    public Observable<List<Product>> getProducts(final int page, final Long collectionId) {
        return getProducts(page, collectionId, Collection.SortOrder.COLLECTION_DEFAULT);
    }

    @Override
    public CancellableTask getProducts(final int page, final Long collectionId, final Collection.SortOrder sortOrder, final Callback<List<Product>> callback) {
        return new CancellableTaskSubscriptionWrapper(getProducts(page, collectionId, sortOrder).subscribe(new InternalCallbackSubscriber<>(callback)));
    }

    @Override
    public Observable<List<Product>> getProducts(final int page, final Long collectionId, final Collection.SortOrder sortOrder) {
        if (page < 1) {
            throw new IllegalArgumentException("page is a 1-based index, value cannot be less than 1");
        }
        if (collectionId == null) {
            throw new NullPointerException("collectionId cannot be null");
        }
        if (sortOrder == null) {
            throw new NullPointerException("sortOrder cannot be null");
        }

        return retrofitService
            .getProducts(appId, collectionId, pageSize, page, sortOrder.toString())
            .retryWhen(networkRetryPolicyProvider.provide())
            .doOnNext(new RetrofitSuccessHttpStatusCodeHandler<>())
            .compose(new UnwrapRetrofitBodyTransformer<ProductListings, List<Product>>())
            .onErrorResumeNext(new BuyClientExceptionHandler<List<Product>>())
            .observeOn(callbackScheduler);
    }

    @Override
    public CancellableTask getCollections(final int page, final Callback<List<Collection>> callback) {
        return new CancellableTaskSubscriptionWrapper(getCollections(page).subscribe(new InternalCallbackSubscriber<>(callback)));
    }

    @Override
    public Observable<List<Collection>> getCollections(final int page) {
        if (page < 1) {
            throw new IllegalArgumentException("page is a 1-based index, value cannot be less than 1");
        }

        // All collection responses from the server are wrapped in a CollectionListings object which contains and array of collections
        // For this call, we will clamp the size of the collection array returned to the page size
        return retrofitService
            .getCollectionPage(appId, page, pageSize)
            .retryWhen(networkRetryPolicyProvider.provide())
            .doOnNext(new RetrofitSuccessHttpStatusCodeHandler<>())
            .compose(new UnwrapRetrofitBodyTransformer<CollectionListings, List<Collection>>())
            .onErrorResumeNext(new BuyClientExceptionHandler<List<Collection>>())
            .observeOn(callbackScheduler);
    }
}
