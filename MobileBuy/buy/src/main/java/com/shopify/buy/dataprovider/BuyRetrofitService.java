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
import com.shopify.buy.model.internal.AccountCredentialsWrapper;
import com.shopify.buy.model.internal.AddressWrapper;
import com.shopify.buy.model.internal.AddressesWrapper;
import com.shopify.buy.model.internal.CheckoutWrapper;
import com.shopify.buy.model.internal.CollectionPublication;
import com.shopify.buy.model.internal.CustomerTokenWrapper;
import com.shopify.buy.model.internal.CustomerWrapper;
import com.shopify.buy.model.internal.EmailWrapper;
import com.shopify.buy.model.internal.GiftCardWrapper;
import com.shopify.buy.model.internal.OrderWrapper;
import com.shopify.buy.model.internal.OrdersWrapper;
import com.shopify.buy.model.internal.ProductPublication;
import com.shopify.buy.model.internal.ShippingRatesWrapper;

import java.util.HashMap;

import retrofit.Callback;
import retrofit.ResponseCallback;
import retrofit.RestAdapter;
import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.GET;
import retrofit.http.PATCH;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Path;
import retrofit.http.Query;

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
    void getProductWithHandle(@Path("channel") String channelId, @Query("handle") String handle, Callback<ProductPublication> callback);

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
     * Customer API
     */

    @POST("/api/customers.json")
    void createCustomer(@Body AccountCredentialsWrapper accountCredentialsWrapper, Callback<CustomerWrapper> callback);

    @PUT("/api/customers/{customerId}/activate.json")
    void activateCustomer(@Query("token") String activationToken, @Body AccountCredentialsWrapper accountCredentialsWrapper, @Path("customerId") Long customerId, Callback<CustomerWrapper> callback);

    @PUT("/api/customers/{customerId}/reset.json")
    void resetPassword(@Query("token") String resetToken, @Body AccountCredentialsWrapper accountCredentialsWrapper, @Path("customerId") Long customerId, Callback<CustomerWrapper> callback);

    @POST("/api/customers/recover.json")
    void recoverCustomer(@Body EmailWrapper emailWrapper, Callback<Void> callback);

    @GET("/api/customers/{customerId}.json")
    void getCustomer(@Path("customerId") Long customerId, Callback<CustomerWrapper> callback);

    @PUT("/api/customers/{customerId}.json")
    void updateCustomer(@Path("customerId") Long customerId, @Body CustomerWrapper customer, Callback<CustomerWrapper> callback);

    /*
     * Customer Token API
     */

    @POST("/api/customers/customer_token.json")
    void getCustomerToken(@Body AccountCredentialsWrapper accountCredentialsWrapper, Callback<CustomerTokenWrapper> callback);

    @DELETE("/api/customers/{customerId}/customer_token.json")
    void removeCustomerToken(@Path("customerId") Long customerId, Callback<Void> callback);

    @PUT("/api/customers/{customerId}/customer_token/renew.json")
    void renewCustomerToken(@Body String empty, @Path("customerId") Long customerId, Callback<CustomerTokenWrapper> callback);

    /*
     * Customer Orders API
     */


    @GET("/api/customers/{customerId}/orders.json")
    void getOrders(@Path("customerId") Long customerId, Callback<OrdersWrapper> callback);

    @GET("/api/customers/{customerId}/orders/{orderId}")
    void getOrder(@Path("customerId") Long customerId, @Path("orderId") String orderId, Callback<OrderWrapper> callback);


    /*
     * Customer Address API
     */

    @GET("/api/customers/{customerId}/addresses")
    void getAddresses(@Path("customerId") Long customerId, Callback<AddressesWrapper> callback);

    @POST("/api/customers/{customerId}/addresses")
    void createAddress(@Path("customerId") Long customerId, @Body AddressWrapper address, Callback<AddressWrapper> callback);

    @GET("/api/customers/{customerId}/addresses/{addressId}")
    void getAddress(@Path("customerId") Long customerId, @Path("addressId") String addressId, Callback<AddressWrapper> callback);

    @PATCH("/api/customers/{customerId}/addresses/{addressId}")
    void updateAddress(@Path("customerId") Long customerId, @Body AddressWrapper address, @Path("addressId") String addressId, Callback<AddressWrapper> callback);


    /*
     * Testing Integration
     */

    // http://SHOP_DOMAIN/mobile_app/verify?api_key=API_KEY&channel_id=CHANNEL_ID
    @GET("/mobile_app/verify")
    void testIntegration(@Query("api_key") String apiKey, @Query("channel_id") String channelId, Callback<Void> callback);
}
