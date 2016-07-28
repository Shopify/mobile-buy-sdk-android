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

import java.util.concurrent.atomic.AtomicReference;

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

    final NetworkRetryPolicyProvider networkRetryPolicyProvider;

    final Scheduler callbackScheduler;

    final AtomicReference<CustomerToken> customerTokenRef = new AtomicReference<>();

    CustomerServiceDefault(
        final Retrofit retrofit,
        final CustomerToken customerToken,
        final NetworkRetryPolicyProvider networkRetryPolicyProvider,
        final Scheduler callbackScheduler
    ) {
        this.retrofitService = retrofit.create(CustomerRetrofitService.class);
        this.customerTokenRef.set(customerToken);
        this.networkRetryPolicyProvider = networkRetryPolicyProvider;
        this.callbackScheduler = callbackScheduler;
    }

    @Override
    public CustomerToken getCustomerToken() {
        return customerTokenRef.get();
    }

    @Override
    public void setCustomerToken(final CustomerToken customerToken) {
        customerTokenRef.set(customerToken);
    }

    @Override
    public CancellableTask createCustomer(final AccountCredentials accountCredentials, final Callback<Customer> callback) {
        return new CancellableTaskSubscriptionWrapper(createCustomer(accountCredentials).subscribe(new InternalCallbackSubscriber<>(callback)));
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
            .onErrorResumeNext(new BuyClientExceptionHandler<Customer>())
            .flatMap(new Func1<Customer, Observable<Customer>>() {
                @Override
                public Observable<Customer> call(final Customer customer) {
                    return loginCustomer(accountCredentials);
                }
            })
            .observeOn(callbackScheduler);
    }

    @Deprecated
    @Override
    public CancellableTask activateCustomer(final Long customerId, final String activationToken, final AccountCredentials accountCredentials, final Callback<Customer> callback) {
        return new CancellableTaskSubscriptionWrapper(activateCustomer(customerId, activationToken, accountCredentials).subscribe(new InternalCallbackSubscriber<>(callback)));
    }

    @Override
    public Observable<Customer> activateCustomer(final Long customerId, final String activationToken, final AccountCredentials accountCredentials) {
        if (customerId == null) {
            throw new NullPointerException("customerId cannot be null");
        }
        if (activationToken == null) {
            throw new NullPointerException("activationToken cannot be null");
        }
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
            .onErrorResumeNext(new BuyClientExceptionHandler<Customer>())
            .observeOn(callbackScheduler);
    }

    @Override
    public CancellableTask resetPassword(final Long customerId, final String resetToken, final AccountCredentials accountCredentials, final Callback<Customer> callback) {
        return new CancellableTaskSubscriptionWrapper(resetPassword(customerId, resetToken, accountCredentials).subscribe(new InternalCallbackSubscriber<>(callback)));
    }

    @Override
    public Observable<Customer> resetPassword(final Long customerId, final String resetToken, final AccountCredentials accountCredentials) {
        if (customerId == null) {
            throw new NullPointerException("customerId cannot be null");
        }
        if (resetToken == null) {
            throw new NullPointerException("resetToken cannot be null");
        }
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
            .onErrorResumeNext(new BuyClientExceptionHandler<Customer>())
            .observeOn(callbackScheduler);
    }

    @Override
    public CancellableTask loginCustomer(final AccountCredentials accountCredentials, final Callback<Customer> callback) {
        return new CancellableTaskSubscriptionWrapper(loginCustomer(accountCredentials).subscribe(new InternalCallbackSubscriber<>(callback)));
    }

    @Override
    public Observable<Customer> loginCustomer(final AccountCredentials accountCredentials) {
        if (accountCredentials == null) {
            throw new NullPointerException("accountCredentials cannot be null");
        }

        final AccountCredentialsWrapper accountCredentialsWrapper = new AccountCredentialsWrapper(accountCredentials);
        return retrofitService
            .getCustomerToken(accountCredentialsWrapper)
            .retryWhen(networkRetryPolicyProvider.provide())
            .doOnNext(new RetrofitSuccessHttpStatusCodeHandler<>())
            .compose(new UnwrapRetrofitBodyTransformer<CustomerTokenWrapper, CustomerToken>())
            .onErrorResumeNext(new BuyClientExceptionHandler<CustomerToken>())
            .doOnNext(new Action1<CustomerToken>() {
                @Override
                public void call(CustomerToken token) {
                    customerTokenRef.set(token);
                }
            })
            .flatMap(new Func1<CustomerToken, Observable<Customer>>() {
                @Override
                public Observable<Customer> call(CustomerToken customerToken) {
                    return getCustomer();
                }
            })
            .observeOn(callbackScheduler);
    }

    @Override
    public CancellableTask logoutCustomer(final Callback<Void> callback) {
        return new CancellableTaskSubscriptionWrapper(logoutCustomer().subscribe(new InternalCallbackSubscriber<>(callback)));
    }

    @Override
    public Observable<Void> logoutCustomer() {
        CustomerToken customerToken = getCustomerToken();

        if (customerToken == null) {
            return Observable.error(new BuyClientError(new IllegalStateException("customer must be logged in")));
        }

        return retrofitService
            .removeCustomerToken(customerToken.getCustomerId())
            .retryWhen(networkRetryPolicyProvider.provide())
            .doOnNext(new RetrofitSuccessHttpStatusCodeHandler<>())
            .map(new Func1<Response<Void>, Void>() {
                @Override
                public Void call(Response<Void> response) {
                    return response.body();
                }
            })
            .onErrorResumeNext(new BuyClientExceptionHandler<Void>())
            .observeOn(callbackScheduler)
            .doOnNext(new Action1<Void>() {
                @Override
                public void call(Void aVoid) {
                    customerTokenRef.set(null);
                }
            });
    }

    @Override
    public CancellableTask updateCustomer(final Customer customer, final Callback<Customer> callback) {
        return new CancellableTaskSubscriptionWrapper(updateCustomer(customer).subscribe(new InternalCallbackSubscriber<>(callback)));
    }

    @Override
    public Observable<Customer> updateCustomer(final Customer customer) {
        if (customer == null) {
            throw new NullPointerException("customer cannot be null");
        }

        CustomerToken customerToken = getCustomerToken();

        if (customerToken == null) {
            return Observable.error(new BuyClientError(new IllegalStateException("customer must be logged in")));
        }

        return retrofitService
            .updateCustomer(customerToken.getCustomerId(), new CustomerWrapper(customer))
            .doOnNext(new RetrofitSuccessHttpStatusCodeHandler<>())
            .compose(new UnwrapRetrofitBodyTransformer<CustomerWrapper, Customer>())
            .onErrorResumeNext(new BuyClientExceptionHandler<Customer>())
            .observeOn(callbackScheduler);
    }

    @Override
    public CancellableTask getCustomer(final Callback<Customer> callback) {
        return new CancellableTaskSubscriptionWrapper(getCustomer().subscribe(new InternalCallbackSubscriber<>(callback)));
    }

    @Override
    public Observable<Customer> getCustomer() {
        CustomerToken customerToken = getCustomerToken();

        if (customerToken == null) {
            return Observable.error(new BuyClientError(new IllegalStateException("customer must be logged in")));
        }

        return retrofitService
            .getCustomer(customerToken.getCustomerId())
            .retryWhen(networkRetryPolicyProvider.provide())
            .doOnNext(new RetrofitSuccessHttpStatusCodeHandler<>())
            .compose(new UnwrapRetrofitBodyTransformer<CustomerWrapper, Customer>())
            .onErrorResumeNext(new BuyClientExceptionHandler<Customer>())
            .observeOn(callbackScheduler);
    }

    @Override
    public CancellableTask renewCustomer(final Callback<CustomerToken> callback) {
        return new CancellableTaskSubscriptionWrapper(renewCustomer().subscribe(new InternalCallbackSubscriber<>(callback)));
    }

    @Override
    public Observable<CustomerToken> renewCustomer() {
        CustomerToken customerToken = getCustomerToken();

        if (customerToken == null) {
            return Observable.error(new BuyClientError(new IllegalStateException("customer must be logged in")));
        }

        return retrofitService
            .renewCustomerToken(EMPTY_BODY, customerToken.getCustomerId())
            .doOnNext(new RetrofitSuccessHttpStatusCodeHandler<>())
            .compose(new UnwrapRetrofitBodyTransformer<CustomerTokenWrapper, CustomerToken>())
            .onErrorResumeNext(new BuyClientExceptionHandler<CustomerToken>())
            .observeOn(callbackScheduler)
            .doOnNext(new Action1<CustomerToken>() {
                @Override
                public void call(CustomerToken token) {
                    customerTokenRef.set(token);
                }
            });
    }

    @Override
    public CancellableTask recoverPassword(final String email, final Callback<Void> callback) {
        return new CancellableTaskSubscriptionWrapper(recoverPassword(email).subscribe(new InternalCallbackSubscriber<>(callback)));
    }

    @Override
    public Observable<Void> recoverPassword(final String email) {
        if (email == null) {
            throw new NullPointerException("email cannot be null");
        }
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
            .onErrorResumeNext(new BuyClientExceptionHandler<Void>())
            .observeOn(callbackScheduler);
    }
}
