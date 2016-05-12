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

import com.shopify.buy.model.PaymentSession;
import com.shopify.buy.model.internal.CheckoutWrapper;
import com.shopify.buy.model.internal.GiftCardWrapper;
import com.shopify.buy.model.internal.PaymentSessionCheckoutWrapper;
import com.shopify.buy.model.internal.PaymentTokenWrapper;
import com.shopify.buy.model.internal.ShippingRatesWrapper;

import java.util.HashMap;

import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Url;
import rx.Observable;

interface CheckoutRetrofitService {

    @POST("api/checkouts.json")
    Observable<Response<CheckoutWrapper>> createCheckout(@Body CheckoutWrapper checkoutWrapper);

    @PATCH("api/checkouts/{token}.json")
    Observable<Response<CheckoutWrapper>> updateCheckout(@Body CheckoutWrapper checkoutWrapper, @Path("token") String token);

    @GET("api/checkouts/{token}/shipping_rates.json")
    Observable<Response<ShippingRatesWrapper>> getShippingRates(@Path("token") String token);

    @POST("api/checkouts/{token}/complete.json")
    Observable<Response<CheckoutWrapper>> completeCheckout(@Body HashMap<String, String> paymentSessionIdMap, @Path("token") String token);

    @POST("api/checkouts/{token}/complete.json")
    Observable<Response<CheckoutWrapper>> completeCheckout(@Body PaymentTokenWrapper paymentTokenWrapper, @Path("token") String token);

    @GET("api/checkouts/{token}/processing.json")
    Observable<Response<Void>> getCheckoutCompletionStatus(@Path("token") String token);

    @GET("api/checkouts/{token}.json")
    Observable<Response<CheckoutWrapper>> getCheckout(@Path("token") String token);

    @POST("api/checkouts/{token}/gift_cards.json")
    Observable<Response<GiftCardWrapper>> applyGiftCard(@Body GiftCardWrapper giftCardWrapper, @Path("token") String token);

    @DELETE("api/checkouts/{token}/gift_cards/{identifier}.json")
    Observable<Response<GiftCardWrapper>> removeGiftCard(@Path("identifier") String giftCardIdentifier, @Path("token") String token);

    @POST
    @Headers("Accept: application/json")
    Observable<Response<PaymentSession>> storeCreditCard(@Url String url, @Body PaymentSessionCheckoutWrapper body, @Header("Authorization") String authorizationHeader);

}
