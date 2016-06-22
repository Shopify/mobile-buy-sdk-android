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
package com.shopify.sample.customer;

import com.shopify.buy.dataprovider.BuyClientError;
import com.shopify.buy.dataprovider.Callback;
import com.shopify.buy.dataprovider.CancellableTask;
import com.shopify.buy.model.AccountCredentials;
import com.shopify.buy.model.Customer;
import com.shopify.buy.model.CustomerToken;
import com.shopify.sample.BaseViewPresenter;
import com.shopify.sample.BuildConfig;
import com.shopify.sample.WeakObserver;
import com.shopify.sample.application.SampleApplication;

import java.lang.ref.WeakReference;

import rx.Subscription;
import rx.functions.Action2;

public final class CustomerLoginViewPresenter extends BaseViewPresenter<CustomerLoginViewPresenter.View> {

    public static interface View extends BaseViewPresenter.View {

        void onLoginCustomerSuccess();
    }

    private CancellableTask loginCustomerTask;

    private CancellableTask fetchCustomerTask;

    @Override
    public void detach() {
        super.detach();

        if (loginCustomerTask != null) {
            loginCustomerTask.cancel();
            loginCustomerTask = null;
        }

        if (fetchCustomerTask != null) {
            fetchCustomerTask.cancel();
            fetchCustomerTask = null;
        }
    }

    public void loginCustomer(final String email, final String password) {
        if (BuildConfig.USE_RX_API) {
            loginCustomerWithRx(email, password);
        } else {
            loginCustomerWithCallback(email, password);
        }
    }

    public void loginCustomerWithRx(final String email, final String password) {
        if (!attached) {
            return;
        }

        showProgress();
        final AccountCredentials credentials = new AccountCredentials(email, password);
        final Subscription subscription = SampleApplication.getBuyClient()
            .loginCustomer(credentials)
            .subscribe(new WeakObserver<>(
                this,
                new Action2<CustomerLoginViewPresenter, Customer>() {
                    @Override
                    public void call(final CustomerLoginViewPresenter target, final Customer customer) {
                        target.onFetchCustomerSuccess(customer);
                    }
                },
                new Action2<CustomerLoginViewPresenter, Throwable>() {
                    @Override
                    public void call(final CustomerLoginViewPresenter target, final Throwable t) {
                        target.onRequestError(t);
                    }
                }
            ));
        addSubscription(subscription);
    }

    public void loginCustomerWithCallback(final String email, final String password) {
        if (!attached) {
            return;
        }

        showProgress();

        final WeakReference<CustomerLoginViewPresenter> presenterRef = new WeakReference<>(this);
        final AccountCredentials credentials = new AccountCredentials(email, password);
        loginCustomerTask = SampleApplication.getBuyClient().loginCustomer(credentials, new Callback<Customer>() {
            @Override
            public void success(final Customer token) {
                final CustomerLoginViewPresenter presenter = presenterRef.get();
                if (presenter != null) {
                    presenter.onFetchCustomerToken(SampleApplication.getBuyClient().getCustomerToken());
                }
            }

            @Override
            public void failure(final BuyClientError error) {
                final CustomerLoginViewPresenter presenter = presenterRef.get();
                if (presenter != null) {
                    presenter.onRequestError(error);
                }
            }
        });
    }

    public void createCustomer(final String email, final String password, final String firstName, final String lastName) {
        if (!attached) {
            return;
        }

        showProgress();
        final AccountCredentials credentials = new AccountCredentials(email, password, firstName, lastName);
        final Subscription subscription = SampleApplication.getBuyClient()
            .createCustomer(credentials)
            .subscribe(new WeakObserver<>(
                this,
                new Action2<CustomerLoginViewPresenter, Customer>() {
                    @Override
                    public void call(final CustomerLoginViewPresenter target, final Customer customer) {
                        target.onFetchCustomerSuccess(customer);
                    }
                },
                new Action2<CustomerLoginViewPresenter, Throwable>() {
                    @Override
                    public void call(final CustomerLoginViewPresenter target, final Throwable t) {
                        target.onRequestError(t);
                    }
                }
            ));
        addSubscription(subscription);
    }

    private void onFetchCustomerToken(final CustomerToken customerToken) {
        if (!attached) {
            return;
        }

        final WeakReference<CustomerLoginViewPresenter> presenterRef = new WeakReference<>(this);
        fetchCustomerTask = SampleApplication.getBuyClient().getCustomer(customerToken.getCustomerId(), new Callback<Customer>() {
            @Override
            public void success(final Customer customer) {
                final CustomerLoginViewPresenter presenter = presenterRef.get();
                if (presenter != null) {
                    presenter.onFetchCustomerSuccess(customer);
                }
            }

            @Override
            public void failure(final BuyClientError error) {
                final CustomerLoginViewPresenter presenter = presenterRef.get();
                if (presenter != null) {
                    presenter.onRequestError(error);
                }
            }
        });
    }

    private void onFetchCustomerSuccess(final Customer customer) {
        SampleApplication.setCustomer(customer);
        hideProgress();
        if (attached) {
            view.onLoginCustomerSuccess();
        }
    }

    private void onRequestError(final Throwable t) {
        hideProgress();
        showError(t);
    }
}
