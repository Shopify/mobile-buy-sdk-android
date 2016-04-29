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

import com.shopify.buy.model.AccountCredentials;
import com.shopify.buy.model.Customer;
import com.shopify.buy.model.CustomerToken;
import com.shopify.buy.model.internal.AccountCredentialsWrapper;
import com.shopify.buy.model.internal.CustomerTokenWrapper;
import com.shopify.buy.model.internal.CustomerWrapper;
import com.shopify.buy.model.internal.EmailWrapper;

import retrofit2.Response;
import retrofit2.Retrofit;
import rx.Observable;
import rx.Scheduler;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Default implementation of {@link CustomerService}
 */
final class CustomerServiceDefault implements CustomerService {

    private static final String EMPTY_BODY = "";

    final CustomerRetrofitService retrofitService;

    final Scheduler callbackScheduler;

    CustomerToken customerToken;

    CustomerServiceDefault(
            final Retrofit retrofit,
            final CustomerToken customerToken,
            final Scheduler callbackScheduler
    ) {
        this.retrofitService = retrofit.create(CustomerRetrofitService.class);
        this.customerToken = customerToken;
        this.callbackScheduler = callbackScheduler;
    }

    @Override
    public void setCustomerToken(CustomerToken customerToken) {
        this.customerToken = customerToken;
    }

    @Override
    public CustomerToken getCustomerToken() {
        return customerToken;
    }

    @Override
    public void createCustomer(final AccountCredentials accountCredentials, final Callback<Customer> callback) {
        createCustomer(accountCredentials).subscribe(new InternalCallbackSubscriber<>(callback));
    }

    @Override
    public Observable<Customer> createCustomer(final AccountCredentials accountCredentials) {
        if (accountCredentials == null) {
            throw new NullPointerException("accountCredentials cannot be null");
        }

        final AccountCredentialsWrapper accountCredentialsWrapper = new AccountCredentialsWrapper(accountCredentials);
        return retrofitService
                .createCustomer(accountCredentialsWrapper)
                .doOnNext(new RetrofitSuccessHttpStatusCodeHandler<>())
                .compose(new UnwrapRetrofitBodyTransformer<CustomerWrapper, Customer>())
                .observeOn(callbackScheduler);
    }

    @Deprecated
    @Override
    public void activateCustomer(final Long customerId, final String activationToken, final AccountCredentials accountCredentials, final Callback<Customer> callback) {
        activateCustomer(customerId, activationToken, accountCredentials).subscribe(new InternalCallbackSubscriber<>(callback));
    }

    @Override
    public Observable<Customer> activateCustomer(final Long customerId, final String activationToken, final AccountCredentials accountCredentials) {
        if (TextUtils.isEmpty(activationToken)) {
            throw new IllegalArgumentException("activation token cannot be empty");
        }

        if (accountCredentials == null) {
            throw new NullPointerException("accountCredentials cannot be null");
        }

        final AccountCredentialsWrapper accountCredentialsWrapper = new AccountCredentialsWrapper(accountCredentials);
        return retrofitService
                .activateCustomer(customerId, activationToken, accountCredentialsWrapper)
                .doOnNext(new RetrofitSuccessHttpStatusCodeHandler<>())
                .compose(new UnwrapRetrofitBodyTransformer<CustomerWrapper, Customer>())
                .observeOn(callbackScheduler);
    }

    @Override
    public void resetPassword(final Long customerId, final String resetToken, final AccountCredentials accountCredentials, final Callback<Customer> callback) {
        resetPassword(customerId, resetToken, accountCredentials).subscribe(new InternalCallbackSubscriber<>(callback));
    }

    @Override
    public Observable<Customer> resetPassword(final Long customerId, final String resetToken, final AccountCredentials accountCredentials) {
        if (TextUtils.isEmpty(resetToken)) {
            throw new IllegalArgumentException("reset token cannot be empty");
        }

        if (accountCredentials == null) {
            throw new NullPointerException("accountCredentials cannot be null");
        }

        final AccountCredentialsWrapper accountCredentialsWrapper = new AccountCredentialsWrapper(accountCredentials);
        return retrofitService
                .resetPassword(customerId, resetToken, accountCredentialsWrapper)
                .doOnNext(new RetrofitSuccessHttpStatusCodeHandler<>())
                .compose(new UnwrapRetrofitBodyTransformer<CustomerWrapper, Customer>())
                .observeOn(callbackScheduler);
    }

    @Override
    public void loginCustomer(final AccountCredentials accountCredentials, final Callback<CustomerToken> callback) {
        loginCustomer(accountCredentials).subscribe(new InternalCallbackSubscriber<>(callback));
    }

    @Override
    public Observable<CustomerToken> loginCustomer(final AccountCredentials accountCredentials) {
        if (accountCredentials == null) {
            throw new NullPointerException("accountCredentials cannot be null");
        }

        final AccountCredentialsWrapper accountCredentialsWrapper = new AccountCredentialsWrapper(accountCredentials);
        return retrofitService
                .getCustomerToken(accountCredentialsWrapper)
                .doOnNext(new RetrofitSuccessHttpStatusCodeHandler<>())
                .compose(new UnwrapRetrofitBodyTransformer<CustomerTokenWrapper, CustomerToken>())
                .doOnNext(new Action1<CustomerToken>() {
                    @Override
                    public void call(CustomerToken token) {
                        customerToken = token;
                    }
                })
                .observeOn(callbackScheduler);
    }

    @Override
    public void logoutCustomer(final Callback<Void> callback) {
        logoutCustomer().subscribe(new InternalCallbackSubscriber<>(callback));
    }

    @Override
    public Observable<Void> logoutCustomer() {
        if (customerToken == null) {
            return Observable.just(null);
        }

        return retrofitService
                .removeCustomerToken(customerToken.getCustomerId())
                .doOnNext(new RetrofitSuccessHttpStatusCodeHandler<>())
                .map(new Func1<Response<Void>, Void>() {
                    @Override
                    public Void call(Response<Void> response) {
                        return response.body();
                    }
                })
                .observeOn(callbackScheduler)
                .doOnNext(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        customerToken = null;
                    }
                });
    }

    @Override
    public void updateCustomer(final Customer customer, final Callback<Customer> callback) {
        updateCustomer(customer).subscribe(new InternalCallbackSubscriber<>(callback));
    }

    @Override
    public Observable<Customer> updateCustomer(final Customer customer) {
        if (customer == null) {
            throw new NullPointerException("customer cannot be null");
        }

        return retrofitService
                .updateCustomer(customer.getId(), new CustomerWrapper(customer))
                .doOnNext(new RetrofitSuccessHttpStatusCodeHandler<>())
                .compose(new UnwrapRetrofitBodyTransformer<CustomerWrapper, Customer>())
                .observeOn(callbackScheduler);
    }

    @Override
    public void getCustomer(final Long customerId, final Callback<Customer> callback) {
        getCustomer(customerId).subscribe(new InternalCallbackSubscriber<>(callback));
    }

    @Override
    public Observable<Customer> getCustomer(final Long customerId) {
        if (customerId == null) {
            throw new NullPointerException("customer Id cannot be null");
        }

        return retrofitService
                .getCustomer(customerId)
                .doOnNext(new RetrofitSuccessHttpStatusCodeHandler<>())
                .compose(new UnwrapRetrofitBodyTransformer<CustomerWrapper, Customer>())
                .observeOn(callbackScheduler);
    }

    @Override
    public void renewCustomer(final Callback<CustomerToken> callback) {
        renewCustomer().subscribe(new InternalCallbackSubscriber<>(callback));
    }

    @Override
    public Observable<CustomerToken> renewCustomer() {
        if (customerToken == null) {
            return Observable.just(null);
        }

        return retrofitService
                .renewCustomerToken(EMPTY_BODY, customerToken.getCustomerId())
                .doOnNext(new RetrofitSuccessHttpStatusCodeHandler<>())
                .compose(new UnwrapRetrofitBodyTransformer<CustomerTokenWrapper, CustomerToken>())
                .observeOn(callbackScheduler)
                .doOnNext(new Action1<CustomerToken>() {
                    @Override
                    public void call(CustomerToken token) {
                        customerToken = token;
                    }
                });
    }

    @Override
    public void recoverPassword(final String email, final Callback<Void> callback) {
        recoverPassword(email).subscribe(new InternalCallbackSubscriber<>(callback));
    }

    @Override
    public Observable<Void> recoverPassword(final String email) {
        if (TextUtils.isEmpty(email)) {
            throw new IllegalArgumentException("email cannot be empty");
        }

        return retrofitService
                .recoverCustomer(new EmailWrapper(email))
                .doOnNext(new RetrofitSuccessHttpStatusCodeHandler<>())
                .map(new Func1<Response<Void>, Void>() {
                    @Override
                    public Void call(Response<Void> response) {
                        return response.body();
                    }
                })
                .observeOn(callbackScheduler);
    }
}
