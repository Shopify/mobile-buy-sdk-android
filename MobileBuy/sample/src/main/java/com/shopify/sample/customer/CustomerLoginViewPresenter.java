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

import com.shopify.buy.model.AccountCredentials;
import com.shopify.buy.model.Customer;
import com.shopify.buy.model.CustomerToken;
import com.shopify.sample.application.SampleApplication;

import java.lang.ref.WeakReference;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public final class CustomerLoginViewPresenter {

    public static interface View {

        void showError(RetrofitError error);

        void showProgress();

        void hideProgress();

        void onLoginCustomerSuccess();

    }

    private View view;

    public void attach(final View view) {
        this.view = view;
    }

    public void detach() {
        view = null;
    }

    public void loginCustomer(final String email, final String password) {
        view.showProgress();

        final AccountCredentials credentials = new AccountCredentials(email, password);
        SampleApplication.getBuyClient().loginCustomer(credentials, new BaseBuyCallback<CustomerToken>(this) {
            @Override
            public void success(final CustomerToken customerToken, final Response response) {
                final CustomerLoginViewPresenter presenter = presenterRef.get();
                if (presenter != null) {
                    presenter.onLoginCustomerSuccess(customerToken);
                }
            }
        });
    }

    public void createCustomer(final String email, final String password, final String firstName, final String lastName) {
        view.showProgress();

        final AccountCredentials credentials = new AccountCredentials(email, password, firstName, lastName);
        SampleApplication.getBuyClient().createCustomer(credentials, new BaseBuyCallback<Customer>(this) {
            @Override
            public void success(final Customer customer, final Response response) {
                final CustomerLoginViewPresenter presenter = presenterRef.get();
                if (presenter != null) {
                    presenter.onFetchCustomerSuccess(customer);
                }
            }
        });
    }

    private void onLoginCustomerSuccess(final CustomerToken customerToken) {
        if (view != null) {
            SampleApplication.getBuyClient().getCustomer(customerToken.getCustomerId(), new BaseBuyCallback<Customer>(this) {
                @Override
                public void success(final Customer customer, final Response response) {
                    final CustomerLoginViewPresenter presenter = presenterRef.get();
                    if (presenter != null) {
                        presenter.onFetchCustomerSuccess(customer);
                    }
                }
            });
        }
    }

    private void onFetchCustomerSuccess(final Customer customer) {
        SampleApplication.setCustomer(customer);
        if (view != null) {
            view.hideProgress();
            view.onLoginCustomerSuccess();
        }
    }

    private void onRequestError(final RetrofitError error) {
        if (view != null) {
            view.hideProgress();
            view.showError(error);
        }
    }

    private static abstract class BaseBuyCallback<T> implements Callback<T> {

        final WeakReference<CustomerLoginViewPresenter> presenterRef;

        BaseBuyCallback(final CustomerLoginViewPresenter presenter) {
            presenterRef = new WeakReference<>(presenter);
        }

        @Override
        public void failure(RetrofitError error) {
            final CustomerLoginViewPresenter presenter = presenterRef.get();
            if (presenter != null) {
                presenter.onRequestError(error);
            }
        }
    }
}
