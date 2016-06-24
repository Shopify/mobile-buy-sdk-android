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

import com.shopify.buy.model.internal.AccountCredentialsWrapper;
import com.shopify.buy.model.internal.CustomerTokenWrapper;
import com.shopify.buy.model.internal.CustomerWrapper;
import com.shopify.buy.model.internal.EmailWrapper;

import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

interface CustomerRetrofitService {

    @POST("api/customers.json")
    Observable<Response<CustomerWrapper>> createCustomer(@Body AccountCredentialsWrapper accountCredentialsWrapper);

    @PUT("api/customers/{customerId}/activate.json")
    Observable<Response<CustomerWrapper>> activateCustomer(@Path("customerId") Long customerId, @Query("token") String activationToken, @Body AccountCredentialsWrapper accountCredentialsWrapper);

    @PUT("api/customers/{customerId}/reset.json")
    Observable<Response<CustomerWrapper>> resetPassword(@Path("customerId") Long customerId, @Query("token") String resetToken, @Body AccountCredentialsWrapper accountCredentialsWrapper);

    @POST("api/customers/recover.json")
    Observable<Response<Void>> recoverCustomer(@Body EmailWrapper emailWrapper);

    @GET("api/customers.json")
    Observable<Response<CustomerWrapper>> getCustomer();

    @GET("api/customers/{customerId}.json")
    Observable<Response<CustomerWrapper>> getCustomer(@Path("customerId") Long customerId);

    @PUT("api/customers/{customerId}.json")
    Observable<Response<CustomerWrapper>> updateCustomer(@Path("customerId") Long customerId, @Body CustomerWrapper customer);

    @POST("api/customers/customer_token.json")
    Observable<Response<CustomerTokenWrapper>> getCustomerToken(@Body AccountCredentialsWrapper accountCredentialsWrapper);

    @DELETE("api/customers/{customerId}/customer_token.json")
    Observable<Response<Void>> removeCustomerToken(@Path("customerId") Long customerId);

    @PUT("api/customers/{customerId}/customer_token/renew.json")
    Observable<Response<CustomerTokenWrapper>> renewCustomerToken(@Body String empty, @Path("customerId") Long customerId);

}
