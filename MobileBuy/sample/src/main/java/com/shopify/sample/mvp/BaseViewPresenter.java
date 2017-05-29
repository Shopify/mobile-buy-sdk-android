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

package com.shopify.sample.mvp;

import com.shopify.sample.util.RequestRegister;

import java.lang.ref.WeakReference;

import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import timber.log.Timber;

import static com.shopify.sample.util.Util.checkNotNull;

public abstract class BaseViewPresenter<V extends View> implements ViewPresenter<V> {
  private boolean viewAttached;
  private WeakReference<V> viewRef;
  private RequestRegister<Integer> requestRegister;

  @Override
  public void attachView(final V view) {
    viewRef = new WeakReference<>(view);

    if (requestRegister != null) {
      requestRegister.dispose();
    }
    requestRegister = new RequestRegister<>();

    viewAttached = true;
  }

  @Override
  public void detachView() {
    viewAttached = false;
    viewRef.clear();

    if (requestRegister != null) {
      requestRegister.dispose();
      requestRegister = null;
    }
  }

  @Override
  public boolean isViewAttached() {
    return viewAttached;
  }

  @Override public boolean isViewDetached() {
    return !viewAttached;
  }

  protected V view() {
    return viewRef.get();
  }

  protected void registerRequest(final int requestId, @NonNull final Disposable disposable) {
    if (isViewAttached()) {
      cancelRequest(requestId);
      requestRegister.add(requestId, checkNotNull(disposable, "disposable == null"));
    }
  }

  public void cancelRequest(final int requestId) {
    if (requestRegister != null) {
      requestRegister.delete(requestId);
    }
  }

  public void cancelAllRequests() {
    if (requestRegister != null) {
      requestRegister.deleteAll();
    }
  }

  protected void showProgress(final int requestId) {
    if (isViewAttached()) {
      final V view = view();
      if (view != null) {
        view.showProgress(requestId);
      }
    }
  }

  protected void onRequestError(final int requestId, final Throwable t) {
    hideProgress(requestId);
    showError(requestId, t);
  }

  protected void hideProgress(final int requestId) {
    if (isViewAttached()) {
      final V view = view();
      if (view != null) {
        view.hideProgress(requestId);
      }
    }
  }

  protected void showError(final int requestId, final Throwable t) {
    Timber.e(t);

    if (isViewAttached()) {
      final V view = view();
      if (view != null) {
        view.showError(requestId, t);
      }
    }
  }
}
