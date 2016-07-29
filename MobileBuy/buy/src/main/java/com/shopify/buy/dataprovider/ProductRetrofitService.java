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

import com.shopify.buy.model.internal.CollectionListings;
import com.shopify.buy.model.internal.ProductListings;
import com.shopify.buy.model.internal.ProductTagsWrapper;

import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

interface ProductRetrofitService {

    @GET("api/apps/{appId}/product_listings.json")
    Observable<Response<ProductListings>> getProducts(@Path("appId") String appId, @Query("product_ids") String productId);

    @GET("api/apps/{appId}/product_listings.json")
    Observable<Response<ProductListings>> getProductByHandle(@Path("appId") String appId, @Query("handle") String handle);

    @GET("api/apps/{appId}/collection_listings.json")
    Observable<Response<CollectionListings>> getCollectionPage(@Path("appId") String appId, @Query("page") int page, @Query("limit") int pageSize);

    @GET("api/apps/{appId}/collection_listings.json")
    Observable<Response<CollectionListings>> getCollections(@Path("appId") String appId, @Query("collection_ids") String collectionIds);

    @GET("api/apps/{appId}/collection_listings.json")
    Observable<Response<CollectionListings>> getCollectionByHandle(@Path("appId") String appId, @Query("handle") String handle);

    @GET("api/apps/{appId}/product_listings/tags.json")
    Observable<Response<ProductTagsWrapper>> getProductTagPage(@Path("appId") String appId, @Query("page") int page, @Query("limit") int pageSize);

    @GET("api/apps/{appId}/product_listings.json")
    Observable<Response<ProductListings>> getProducts(@Path("appId") String appId, @Query("collection_id") Long collectionId, @Query("tag") String tags, @Query("sort_by") String sortOrder, @Query("page") int page, @Query("limit") int pageSize);
}
