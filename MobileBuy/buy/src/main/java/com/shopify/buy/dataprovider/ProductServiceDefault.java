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

import com.shopify.buy.interceptor.RequestInterceptorResponse;
import com.shopify.buy.interceptor.ProductRequestInterceptor;
import com.shopify.buy.interceptor.ProductResponseInterceptor;
import com.shopify.buy.model.Collection;
import com.shopify.buy.model.Product;
import com.shopify.buy.model.ProductTag;
import com.shopify.buy.model.internal.CollectionListings;
import com.shopify.buy.model.internal.ProductListings;
import com.shopify.buy.model.internal.ProductTagsWrapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import retrofit2.Retrofit;
import rx.Observable;
import rx.Scheduler;
import rx.functions.Func1;
import rx.functions.Func2;

/**
 * Default implementation of {@link ProductService}
 */
final class ProductServiceDefault implements ProductService {

    final ProductRetrofitService retrofitService;

    final String appId;

    final int pageSize;

    final NetworkRetryPolicyProvider networkRetryPolicyProvider;

    final Scheduler callbackScheduler;

    final ProductRequestInterceptor requestInterceptor;

    final ProductResponseInterceptor responseInterceptor;

    ProductServiceDefault(
        final Retrofit retrofit,
        final String appId,
        final int pageSize,
        final NetworkRetryPolicyProvider networkRetryPolicyProvider,
        final Scheduler callbackScheduler,
        final ProductRequestInterceptor requestInterceptor,
        final ProductResponseInterceptor responseInterceptor
    ) {
        this.retrofitService = retrofit.create(ProductRetrofitService.class);
        this.appId = appId;
        this.pageSize = pageSize;
        this.networkRetryPolicyProvider = networkRetryPolicyProvider;
        this.callbackScheduler = callbackScheduler;
        this.requestInterceptor = requestInterceptor;
        this.responseInterceptor = responseInterceptor;
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
        return getProducts(page, (Set<String>) null);
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

        final Observable<Product> apiRequest = retrofitService
            .getProductByHandle(appId, handle)
            .retryWhen(networkRetryPolicyProvider.provide())
            .doOnNext(new RetrofitSuccessHttpStatusCodeHandler<>())
            .compose(new UnwrapRetrofitBodyTransformer<ProductListings, List<Product>>())
            .compose(new FirstListItemOrDefaultTransformer<Product>())
            .onErrorResumeNext(new BuyClientExceptionHandler<Product>());

        return ApiCallInterceptWrapper.wrap(
            apiRequest,
            new ApiCallInterceptWrapper.RequestInterceptor<>(requestInterceptor, new Func1<ProductRequestInterceptor, Observable<RequestInterceptorResponse<Product>>>() {
                @Override
                public Observable<RequestInterceptorResponse<Product>> call(ProductRequestInterceptor interceptor) {
                    return interceptor.getProductByHandle(handle);
                }
            }),
            new ApiCallInterceptWrapper.ResponseInterceptor<>(responseInterceptor, new Func2<ProductResponseInterceptor, Observable<Product>, Observable<Product>>() {
                @Override
                public Observable<Product> call(ProductResponseInterceptor interceptor, Observable<Product> observable) {
                    return interceptor.getProductByHandle(handle, observable);
                }
            })
        ).observeOn(callbackScheduler);
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

        final Observable<Product> apiRequest = retrofitService
            .getProducts(appId, String.valueOf(productId))
            .retryWhen(networkRetryPolicyProvider.provide())
            .doOnNext(new RetrofitSuccessHttpStatusCodeHandler<>())
            .compose(new UnwrapRetrofitBodyTransformer<ProductListings, List<Product>>())
            .compose(new FirstListItemOrDefaultTransformer<Product>())
            .onErrorResumeNext(new BuyClientExceptionHandler<Product>());

        return ApiCallInterceptWrapper.wrap(
            apiRequest,
            new ApiCallInterceptWrapper.RequestInterceptor<>(requestInterceptor, new Func1<ProductRequestInterceptor, Observable<RequestInterceptorResponse<Product>>>() {
                @Override
                public Observable<RequestInterceptorResponse<Product>> call(ProductRequestInterceptor interceptor) {
                    return interceptor.getProduct(productId);
                }
            }),
            new ApiCallInterceptWrapper.ResponseInterceptor<>(responseInterceptor, new Func2<ProductResponseInterceptor, Observable<Product>, Observable<Product>>() {
                @Override
                public Observable<Product> call(ProductResponseInterceptor interceptor, Observable<Product> observable) {
                    return interceptor.getProduct(productId, observable);
                }
            })
        ).observeOn(callbackScheduler);
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
        final String queryString = formatQueryString(productIds);
        final Observable<List<Product>> apiRequest = retrofitService
            .getProducts(appId, queryString)
            .retryWhen(networkRetryPolicyProvider.provide())
            .doOnNext(new RetrofitSuccessHttpStatusCodeHandler<>())
            .compose(new UnwrapRetrofitBodyTransformer<ProductListings, List<Product>>())
            .onErrorResumeNext(new BuyClientExceptionHandler<List<Product>>());

        return ApiCallInterceptWrapper.wrap(
            apiRequest,
            new ApiCallInterceptWrapper.RequestInterceptor<>(requestInterceptor, new Func1<ProductRequestInterceptor, Observable<RequestInterceptorResponse<List<Product>>>>() {
                @Override
                public Observable<RequestInterceptorResponse<List<Product>>> call(ProductRequestInterceptor interceptor) {
                    return interceptor.getProducts(productIds);
                }
            }),
            new ApiCallInterceptWrapper.ResponseInterceptor<>(responseInterceptor, new Func2<ProductResponseInterceptor, Observable<List<Product>>, Observable<List<Product>>>() {
                @Override
                public Observable<List<Product>> call(ProductResponseInterceptor interceptor, Observable<List<Product>> observable) {
                    return interceptor.getProducts(productIds, observable);
                }
            })
        ).observeOn(callbackScheduler);
    }

    @Override
    public CancellableTask getCollectionByHandle(final String handle, final Callback<Collection> callback) {
        return new CancellableTaskSubscriptionWrapper(getCollectionByHandle(handle).subscribe(new InternalCallbackSubscriber<>(callback)));
    }

    @Override
    public Observable<Collection> getCollectionByHandle(final String handle) {
        if (handle == null) {
            throw new NullPointerException("handle cannot be null");
        }

        if (TextUtils.isEmpty(handle)) {
            throw new IllegalArgumentException("handle cannot be empty");
        }

        final Observable<Collection> apiRequest = retrofitService
            .getCollectionByHandle(appId, handle)
            .retryWhen(networkRetryPolicyProvider.provide())
            .doOnNext(new RetrofitSuccessHttpStatusCodeHandler<>())
            .compose(new UnwrapRetrofitBodyTransformer<CollectionListings, List<Collection>>())
            .compose(new FirstListItemOrDefaultTransformer<Collection>())
            .onErrorResumeNext(new BuyClientExceptionHandler<Collection>());

        return ApiCallInterceptWrapper.wrap(
            apiRequest,
            new ApiCallInterceptWrapper.RequestInterceptor<>(requestInterceptor, new Func1<ProductRequestInterceptor, Observable<RequestInterceptorResponse<Collection>>>() {
                @Override
                public Observable<RequestInterceptorResponse<Collection>> call(ProductRequestInterceptor interceptor) {
                    return interceptor.getCollectionByHandle(handle);
                }
            }),
            new ApiCallInterceptWrapper.ResponseInterceptor<>(responseInterceptor, new Func2<ProductResponseInterceptor, Observable<Collection>, Observable<Collection>>() {
                @Override
                public Observable<Collection> call(ProductResponseInterceptor interceptor, Observable<Collection> observable) {
                    return interceptor.getCollectionByHandle(handle, observable);
                }
            })
        ).observeOn(callbackScheduler);

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
        final Observable<List<Collection>> apiRequest = retrofitService
            .getCollectionPage(appId, page, pageSize)
            .retryWhen(networkRetryPolicyProvider.provide())
            .doOnNext(new RetrofitSuccessHttpStatusCodeHandler<>())
            .compose(new UnwrapRetrofitBodyTransformer<CollectionListings, List<Collection>>())
            .onErrorResumeNext(new BuyClientExceptionHandler<List<Collection>>());

        return ApiCallInterceptWrapper.wrap(
            apiRequest,
            new ApiCallInterceptWrapper.RequestInterceptor<>(requestInterceptor, new Func1<ProductRequestInterceptor, Observable<RequestInterceptorResponse<List<Collection>>>>() {
                @Override
                public Observable<RequestInterceptorResponse<List<Collection>>> call(ProductRequestInterceptor interceptor) {
                    return interceptor.getCollections(page);
                }
            }),
            new ApiCallInterceptWrapper.ResponseInterceptor<>(responseInterceptor, new Func2<ProductResponseInterceptor, Observable<List<Collection>>, Observable<List<Collection>>>() {
                @Override
                public Observable<List<Collection>> call(ProductResponseInterceptor interceptor, Observable<List<Collection>> observable) {
                    return interceptor.getCollections(page, observable);
                }
            })
        ).observeOn(callbackScheduler);
    }

    @Override
    public CancellableTask getCollections(final List<Long> collectionIds, final Callback<List<Collection>> callback) {
        return new CancellableTaskSubscriptionWrapper(getCollections(collectionIds).subscribe(new InternalCallbackSubscriber<>(callback)));
    }

    @Override
    public Observable<List<Collection>> getCollections(final List<Long> collectionIds) {
        if (collectionIds == null) {
            throw new NullPointerException("collectionIds List cannot be null");
        }

        if (collectionIds.isEmpty()) {
            throw new IllegalArgumentException("collectionIds List cannot be empty");
        }

        final String queryString = TextUtils.join(",", collectionIds.toArray());
        final Observable<List<Collection>> apiRequest = retrofitService
            .getCollections(appId, queryString)
            .retryWhen(networkRetryPolicyProvider.provide())
            .doOnNext(new RetrofitSuccessHttpStatusCodeHandler<>())
            .compose(new UnwrapRetrofitBodyTransformer<CollectionListings, List<Collection>>())
            .onErrorResumeNext(new BuyClientExceptionHandler<List<Collection>>());

        return ApiCallInterceptWrapper.wrap(
            apiRequest,
            new ApiCallInterceptWrapper.RequestInterceptor<>(requestInterceptor, new Func1<ProductRequestInterceptor, Observable<RequestInterceptorResponse<List<Collection>>>>() {
                @Override
                public Observable<RequestInterceptorResponse<List<Collection>>> call(ProductRequestInterceptor interceptor) {
                    return interceptor.getCollections(collectionIds);
                }
            }),
            new ApiCallInterceptWrapper.ResponseInterceptor<>(responseInterceptor, new Func2<ProductResponseInterceptor, Observable<List<Collection>>, Observable<List<Collection>>>() {
                @Override
                public Observable<List<Collection>> call(ProductResponseInterceptor interceptor, Observable<List<Collection>> observable) {
                    return interceptor.getCollections(collectionIds, observable);
                }
            })
        ).observeOn(callbackScheduler);
    }

    @Override
    public CancellableTask getProductTags(int page, Callback<List<String>> callback) {
        return new CancellableTaskSubscriptionWrapper(getProductTags(page).subscribe(new InternalCallbackSubscriber<>(callback)));
    }

    @Override
    public Observable<List<String>> getProductTags(final int page) {
        if (page < 1) {
            throw new IllegalArgumentException("page is a 1-based index, value cannot be less than 1");
        }

        return retrofitService
            .getProductTagPage(appId, page, pageSize)
            .retryWhen(networkRetryPolicyProvider.provide())
            .doOnNext(new RetrofitSuccessHttpStatusCodeHandler<>())
            .compose(new UnwrapRetrofitBodyTransformer<ProductTagsWrapper, List<ProductTag>>())
            .map(unwrapProductTags())
            .onErrorResumeNext(new BuyClientExceptionHandler<List<String>>())
            .observeOn(callbackScheduler);
    }

    @Override
    public CancellableTask getProducts(final int page, final Set<String> tags, final Callback<List<Product>> callback) {
        return new CancellableTaskSubscriptionWrapper(getProducts(page, tags).subscribe(new InternalCallbackSubscriber<>(callback)));
    }

    @Override
    public Observable<List<Product>> getProducts(final int page, final Set<String> tags) {
        if (page < 1) {
            throw new IllegalArgumentException("page is a 1-based index, value cannot be less than 1");
        }

        final String tagsQueryStr = formatQueryString(tags);

        final Observable<List<Product>> apiRequest = retrofitService
            .getProducts(appId, null, tagsQueryStr, null, page, pageSize)
            .retryWhen(networkRetryPolicyProvider.provide())
            .doOnNext(new RetrofitSuccessHttpStatusCodeHandler<>())
            .compose(new UnwrapRetrofitBodyTransformer<ProductListings, List<Product>>())
            .onErrorResumeNext(new BuyClientExceptionHandler<List<Product>>());

        return ApiCallInterceptWrapper.wrap(
            apiRequest,
            new ApiCallInterceptWrapper.RequestInterceptor<>(requestInterceptor, new Func1<ProductRequestInterceptor, Observable<RequestInterceptorResponse<List<Product>>>>() {
                @Override
                public Observable<RequestInterceptorResponse<List<Product>>> call(ProductRequestInterceptor interceptor) {
                    return interceptor.getProducts(page, null, tags, null);
                }
            }),
            new ApiCallInterceptWrapper.ResponseInterceptor<>(responseInterceptor, new Func2<ProductResponseInterceptor, Observable<List<Product>>, Observable<List<Product>>>() {
                @Override
                public Observable<List<Product>> call(ProductResponseInterceptor interceptor, Observable<List<Product>> observable) {
                    return interceptor.getProducts(page, null, tags, null, observable);
                }
            })
        ).observeOn(callbackScheduler);
    }

    @Override
    public CancellableTask getProducts(final int page, final Long collectionId, Set<String> tags, final Collection.SortOrder sortOrder, final Callback<List<Product>> callback) {
        return new CancellableTaskSubscriptionWrapper(getProducts(page, collectionId, tags, sortOrder).subscribe(new InternalCallbackSubscriber<>(callback)));
    }

    @Override
    public Observable<List<Product>> getProducts(final int page, final Long collectionId, final Set<String> tags, final Collection.SortOrder sortOrder) {
        if (page < 1) {
            throw new IllegalArgumentException("page is a 1-based index, value cannot be less than 1");
        }

        if (collectionId == null) {
            throw new NullPointerException("collectionId cannot be null");
        }

        final String sortOrderStr = sortOrder != null ? sortOrder.toString() : Collection.SortOrder.COLLECTION_DEFAULT.toString();
        final String tagsQueryStr = formatQueryString(tags);
        final Observable<List<Product>> apiRequest = retrofitService
            .getProducts(appId, collectionId, tagsQueryStr, sortOrderStr, page, pageSize)
            .retryWhen(networkRetryPolicyProvider.provide())
            .doOnNext(new RetrofitSuccessHttpStatusCodeHandler<>())
            .compose(new UnwrapRetrofitBodyTransformer<ProductListings, List<Product>>())
            .onErrorResumeNext(new BuyClientExceptionHandler<List<Product>>());

        return ApiCallInterceptWrapper.wrap(
            apiRequest,
            new ApiCallInterceptWrapper.RequestInterceptor<>(requestInterceptor, new Func1<ProductRequestInterceptor, Observable<RequestInterceptorResponse<List<Product>>>>() {
                @Override
                public Observable<RequestInterceptorResponse<List<Product>>> call(ProductRequestInterceptor interceptor) {
                    return interceptor.getProducts(page, collectionId, tags, sortOrder);
                }
            }),
            new ApiCallInterceptWrapper.ResponseInterceptor<>(responseInterceptor, new Func2<ProductResponseInterceptor, Observable<List<Product>>, Observable<List<Product>>>() {
                @Override
                public Observable<List<Product>> call(ProductResponseInterceptor interceptor, Observable<List<Product>> observable) {
                    return interceptor.getProducts(page, collectionId, tags, sortOrder, observable);
                }
            })
        ).observeOn(callbackScheduler);
    }

    private Func1<List<ProductTag>, List<String>> unwrapProductTags() {
        return new Func1<List<ProductTag>, List<String>>() {
            @Override
            public List<String> call(List<ProductTag> productTags) {
                final List<String> tags = new ArrayList<>();
                if (productTags != null) {
                    for (ProductTag productTag : productTags) {
                        if (!TextUtils.isEmpty(productTag.getTitle())) {
                            tags.add(productTag.getTitle());
                        }
                    }
                }
                return tags;
            }
        };
    }

    private String formatQueryString(final java.util.Collection items) {
        if (items != null && !items.isEmpty()) {
            return TextUtils.join(",", items.toArray());
        } else {
            return null;
        }
    }
}
