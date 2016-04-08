/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 Shopify Inc.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.shopify.buy.dataprovider;

import com.shopify.buy.model.Shop;
import com.shopify.buy.model.internal.CheckoutWrapper;
import com.shopify.buy.model.internal.CollectionPublication;
import com.shopify.buy.model.internal.GiftCardWrapper;
import com.shopify.buy.model.internal.ProductPublication;
import com.shopify.buy.model.internal.ShippingRatesWrapper;

import java.util.HashMap;

import retrofit.Callback;
import retrofit.ResponseCallback;
import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.GET;
import retrofit.http.PATCH;
import retrofit.http.POST;
import retrofit.http.Path;
import retrofit.http.Query;
import retrofit.RestAdapter;

/**
 * Provides the interface for {@link RestAdapter} describing the endpoints and responses for the Mobile Buy endpoints
 */
interface BuyRetrofitService {

    /*
     * Storefront API
     */

    @GET("/meta.json")
    void getShop(Callback<Shop> callback);

    @GET("/api/channels/{channel}/product_publications.json")
    void getProductPage(@Path("channel") String channelId, @Query("page") int page, @Query("limit") int pageSize, Callback<ProductPublication> callback);

    @GET("/api/channels/{channel}/product_publications.json")
    void getProducts(@Path("channel") String channelId, @Query("product_ids") String productId, Callback<ProductPublication> callback);

    @GET("/api/channels/{channel}/product_publications.json")
    void getProducts(@Path("channel") String channelId, @Query("collection_id") String collectionId, @Query("limit") int pageSize, @Query("page") int page, @Query("sort_by") String sortOrder, Callback<ProductPublication> callback);

    @GET("/api/channels/{channel}/collection_publications.json")
    void getCollections(@Path("channel") String channelId, Callback<CollectionPublication> callback);

    @GET("/api/channels/{channel}/collection_publications.json")
    void getCollectionPage(@Path("channel") String channelId, @Query("page") int page, @Query("limit") int pageSize, Callback<CollectionPublication> callback);

    /*
     * Checkout API
     */

    @POST("/api/checkouts.json")
    void createCheckout(@Body CheckoutWrapper checkoutWrapper, Callback<CheckoutWrapper> callback);

    @PATCH("/api/checkouts/{token}.json")
    void updateCheckout(@Body CheckoutWrapper checkoutWrapper, @Path("token") String token, Callback<CheckoutWrapper> callback);

    @GET("/api/checkouts/{token}/shipping_rates.json")
    void getShippingRates(@Path("token") String token, Callback<ShippingRatesWrapper> callback);

    @POST("/api/checkouts/{token}/complete.json")
    void completeCheckout(@Body HashMap<String, String> paymentSessionIdMap, @Path("token") String token, Callback<CheckoutWrapper> callback);

    @GET("/api/checkouts/{token}/processing.json")
    void getCheckoutCompletionStatus(@Path("token") String token, ResponseCallback callback);

    @GET("/api/checkouts/{token}.json")
    void getCheckout(@Path("token") String token, Callback<CheckoutWrapper> callback);

    @POST("/api/checkouts/{token}/gift_cards.json")
    void applyGiftCard(@Body GiftCardWrapper giftCardWrapper, @Path("token") String token, Callback<GiftCardWrapper> callback);

    @DELETE("/api/checkouts/{token}/gift_cards/{identifier}.json")
    void removeGiftCard(@Path("identifier") String giftCardIdentifier, @Path("token") String token, Callback<GiftCardWrapper> callback);

    /*
     * Testing Integration
     */

    // http://SHOP_DOMAIN/mobile_app/verify?api_key=API_KEY&channel_id=CHANNEL_ID
    @GET("/mobile_app/verify")
    void testIntegration(@Query("api_key") String apiKey, @Query("channel_id") String channelId, Callback<Void> callback);
}
