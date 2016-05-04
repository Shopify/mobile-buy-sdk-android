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

import com.shopify.buy.model.Customer;
import com.shopify.buy.model.Order;
import com.shopify.sample.application.SampleApplication;

import java.lang.ref.WeakReference;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public final class CustomerViewPresenter {

    public static interface View {

        void showOrderList(List<Order> orders);

        void showError(RetrofitError error);

        void showProgress();

        void hideProgress();

    }

    private View view;

    public void attach(final View view) {
        this.view = view;

        final Customer customer = SampleApplication.getCustomer();
        if (customer != null) {
            fetchCustomerOrders(customer);
        }
    }

    public void detach() {
        view = null;
    }

    private void fetchCustomerOrders(final Customer customer) {
        view.showProgress();
        SampleApplication.getBuyClient().getOrders(customer, new BaseBuyCallback<List<Order>>(this) {
            @Override
            public void success(final List<Order> orders, final Response response) {
                final CustomerViewPresenter presenter = presenterRef.get();
                if (presenter != null) {
                    presenter.onFetchCustomerOrders(orders);
                }
            }
        });
    }

    private void onFetchCustomerOrders(final List<Order> orders) {
        if (view != null) {
            view.hideProgress();
            view.showOrderList(orders);
        }
    }

    private void onRequestError(final RetrofitError error) {
        if (view != null) {
            view.hideProgress();
            view.showError(error);
        }
    }

    private static abstract class BaseBuyCallback<T> implements Callback<T> {

        final WeakReference<CustomerViewPresenter> presenterRef;

        BaseBuyCallback(final CustomerViewPresenter presenter) {
            presenterRef = new WeakReference<>(presenter);
        }

        @Override
        public void failure(RetrofitError error) {
            final CustomerViewPresenter presenter = presenterRef.get();
            if (presenter != null) {
                presenter.onRequestError(error);
            }
        }
    }
}
