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
import com.shopify.buy.model.internal.CollectionListings;
import com.shopify.buy.model.internal.CustomerTokenWrapper;
import com.shopify.buy.model.internal.CustomerWrapper;
import com.shopify.buy.model.internal.EmailWrapper;
import com.shopify.buy.model.internal.GiftCardWrapper;
import com.shopify.buy.model.internal.OrderWrapper;
import com.shopify.buy.model.internal.OrdersWrapper;
import com.shopify.buy.model.internal.PaymentRequestWrapper;
import com.shopify.buy.model.internal.PaymentWrapper;
import com.shopify.buy.model.internal.ProductListings;
import com.shopify.buy.model.internal.ShippingRatesWrapper;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Provides the interface for {@link Retrofit} describing the endpoints and responses for the Mobile Buy endpoints
 */
interface BuyRetrofitService {

    /*
     * Storefront API
     */

    @GET("meta.json")
    Observable<Response<Shop>> getShop();

    @GET("api/apps/{appId}/product_listings.json")
    Observable<Response<ProductListings>> getProductPage(@Path("appId") String appId, @Query("page") int page, @Query("limit") int pageSize);

    @GET("api/apps/{appId}/product_listings.json")
    Observable<Response<ProductListings>>  getProducts(@Path("appId") String appId, @Query("product_ids") String productId);

    @GET("api/apps/{appId}/product_listings.json")
    Observable<Response<ProductListings>>  getProductWithHandle(@Path("appId") String appId, @Query("handle") String handle);

    @GET("api/apps/{appId}/product_listings.json")
    Observable<Response<ProductListings>>  getProducts(@Path("appId") String appId, @Query("collection_id") String collectionId, @Query("limit") int pageSize, @Query("page") int page, @Query("sort_by") String sortOrder);

    @GET("api/apps/{appId}/collection_listings.json")
    Observable<Response<CollectionListings>> getCollections(@Path("appId") String appId);

    @GET("api/apps/{appId}/collection_listings.json")
    Observable<Response<CollectionListings>> getCollectionPage(@Path("appId") String appId, @Query("page") int page, @Query("limit") int pageSize);

    /*
     * Checkout API
     */

    @POST("api/checkouts.json")
    Observable<Response<CheckoutWrapper>> createCheckout(@Body CheckoutWrapper checkoutWrapper);

    @PATCH("api/checkouts/{token}.json")
    Observable<Response<CheckoutWrapper>> updateCheckout(@Body CheckoutWrapper checkoutWrapper, @Path("token") String token);

    @GET("api/checkouts/{token}/shipping_rates.json")
    Observable<Response<ShippingRatesWrapper>> getShippingRates(@Path("token") String token);

    @POST("api/checkouts/{token}/payments.json")
    Observable<Response<PaymentWrapper>> completeCheckout(@Body PaymentRequestWrapper paymentRequestWrapper, @Path("token") String token);

    @GET("api/checkouts/{token}.json")
    Observable<Response<CheckoutWrapper>> getCheckout(@Path("token") String token);

    @POST("api/checkouts/{token}/gift_cards.json")
    Observable<Response<GiftCardWrapper>> applyGiftCard(@Body GiftCardWrapper giftCardWrapper, @Path("token") String token);

    @DELETE("api/checkouts/{token}/gift_cards/{identifier}.json")
    Observable<Response<GiftCardWrapper>> removeGiftCard(@Path("identifier") String giftCardIdentifier, @Path("token") String token);

    /*
     * Customer API
     */

    @POST("api/customers.json")
    Observable<Response<CustomerWrapper>> createCustomer(@Body AccountCredentialsWrapper accountCredentialsWrapper);

    @PUT("api/customers/{customerId}/activate.json")
    Observable<Response<CustomerWrapper>> activateCustomer(@Query("token") String activationToken, @Body AccountCredentialsWrapper accountCredentialsWrapper, @Path("customerId") Long customerId);

    @PUT("api/customers/{customerId}/reset.json")
    Observable<Response<CustomerWrapper>> resetPassword(@Query("token") String resetToken, @Body AccountCredentialsWrapper accountCredentialsWrapper, @Path("customerId") Long customerId);

    @POST("api/customers/login.json")
    Observable<Response<CustomerWrapper>> loginCustomer(@Body CustomerWrapper customerWrapper);

    @POST("api/customers/logout.json")
    Observable<Response<Void>> logoutCustomer(@Body String empty);

    @POST("api/customers/recover.json")
    Observable<Response<Void>> recoverCustomer(@Body EmailWrapper emailWrapper);

    @PUT("api/customers/renew.json")
    Observable<Response<CustomerWrapper>> renewCustomer(@Body String empty);

    @GET("api/customers.json")
    Observable<Response<CustomerWrapper>> getCustomer();

    @PUT("api/customers.json")
    Observable<Response<CustomerWrapper>> updateCustomer(@Body CustomerWrapper customer);

    @GET("api/customers/{customerId}.json")
    Observable<Response<CustomerWrapper>> getCustomer(@Path("customerId") Long customerId);

    @PUT("api/customers/{customerId}.json")
    Observable<Response<CustomerWrapper>> updateCustomer(@Path("customerId") Long customerId, @Body CustomerWrapper customer);

    /*
     * Customer Token API
     */

    @POST("api/customers/customer_token.json")
    Observable<Response<CustomerTokenWrapper>> getCustomerToken(@Body AccountCredentialsWrapper accountCredentialsWrapper);

    @DELETE("api/customers/{customerId}/customer_token.json")
    Observable<Response<Void>> removeCustomerToken(@Path("customerId") Long customerId);

    @PUT("api/customers/{customerId}/customer_token/renew.json")
    Observable<Response<CustomerTokenWrapper>> renewCustomerToken(@Body String empty, @Path("customerId") Long customerId);

    /*
     * Customer Orders API
     */

    @GET("api/customers/{customerId}/orders.json")
    Observable<Response<OrdersWrapper>> getOrders(@Path("customerId") Long customerId);

    @GET("api/customers/{customerId}/orders/{orderId}")
    Observable<Response<OrderWrapper>> getOrder(@Path("customerId") Long customerId, @Path("orderId") String orderId);


    /*
     * Customer Address API
     */

    @GET("api/customers/{customerId}/addresses")
    Observable<Response<AddressesWrapper>> getAddresses(@Path("customerId") Long customerId);

    @POST("api/customers/{customerId}/addresses")
    Observable<Response<AddressWrapper>> createAddress(@Path("customerId") Long customerId, @Body AddressWrapper address);

    @GET("api/customers/{customerId}/addresses/{addressId}")
    Observable<Response<AddressWrapper>> getAddress(@Path("customerId") Long customerId, @Path("addressId") String addressId);

    @PATCH("api/customers/{customerId}/addresses/{addressId}")
    Observable<Response<AddressWrapper>> updateAddress(@Path("customerId") Long customerId, @Body AddressWrapper address, @Path("addressId") String addressId);

}
