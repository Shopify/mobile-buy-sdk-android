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
import com.shopify.sample.WeakObserver;
import com.shopify.sample.BaseViewPresenter;
import com.shopify.sample.application.SampleApplication;

import java.util.List;

import rx.Subscription;
import rx.functions.Action2;

public final class CustomerOrderListViewPresenter extends BaseViewPresenter<CustomerOrderListViewPresenter.View> {

    public static interface View extends BaseViewPresenter.View {

        void showOrderList(List<Order> orders);

    }

    @Override
    public void attach(final View view) {
        super.attach(view);

        final Customer customer = SampleApplication.getCustomer();
        if (customer != null) {
            fetchCustomerOrders(customer);
        }
    }

    private void fetchCustomerOrders(final Customer customer) {
        if (!attached) {
            return;
        }

        showProgress();
        final Subscription subscription = SampleApplication.getBuyClient()
                .getOrders(customer)
                .subscribe(new WeakObserver<>(
                        this,
                        new Action2<CustomerOrderListViewPresenter, List<Order>>() {
                            @Override
                            public void call(final CustomerOrderListViewPresenter target, final List<Order> orders) {
                                target.onFetchCustomerOrders(orders);
                            }
                        },
                        new Action2<CustomerOrderListViewPresenter, Throwable>() {
                            @Override
                            public void call(final CustomerOrderListViewPresenter target, final Throwable t) {
                                target.onRequestError(t);
                            }
                        }
                ));
        addSubscription(subscription);
    }

    private void onFetchCustomerOrders(final List<Order> orders) {
        hideProgress();
        if (attached) {
            view.showOrderList(orders);
        }
    }

    private void onRequestError(final Throwable t) {
        hideProgress();
        showError(t);
    }
}
