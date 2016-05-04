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
package com.shopify.sample;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

public abstract class BaseViewPresenter<V extends BaseViewPresenter.View> {

    public static interface View {

        void showError(Throwable t);

        void showProgress();

        void hideProgress();

    }

    protected boolean attached;

    protected V view;

    protected CompositeSubscription requestSubscriptions;

    public void attach(final V view) {
        this.view = view;
        this.requestSubscriptions = new CompositeSubscription();
        this.attached = true;
    }

    public void detach() {
        attached = false;
        if (requestSubscriptions != null) {
            requestSubscriptions.unsubscribe();
            requestSubscriptions = null;
        }
        view = null;
    }

    protected void showProgress() {
        if (attached) {
            view.showProgress();
        }
    }

    protected void hideProgress() {
        if (attached) {
            view.hideProgress();
        }
    }

    protected void showError(final Throwable t) {
        if (attached) {
            view.showError(t);
        }
    }

    protected void addSubscription(final Subscription subscription) {
        if (attached) {
            requestSubscriptions.add(subscription);
        }
    }
}
