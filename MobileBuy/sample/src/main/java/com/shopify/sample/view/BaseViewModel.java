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

package com.shopify.sample.view;

import android.support.annotation.Nullable;

import com.shopify.sample.domain.model.UserMessageError;
import com.shopify.sample.util.RequestRegister;

import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

import static com.shopify.sample.util.Util.checkNotNull;

@SuppressWarnings("WeakerAccess")
public abstract class BaseViewModel extends android.arch.lifecycle.ViewModel implements ViewModel {
  private final ProgressLiveData progressLiveData = new ProgressLiveData();
  private final UserErrorCallback errorCallback = new UserErrorCallback();
  private final RequestRegister<Integer> requestRegister = new RequestRegister<>();

  @Override protected void onCleared() {
    requestRegister.dispose();
  }

  @Override public void cancelAllRequests() {
    requestRegister.deleteAll();
  }

  @Override public void cancelRequest(final int requestId) {
    requestRegister.delete(requestId);
  }

  @Override public ProgressLiveData progressLiveData() {
    return progressLiveData;
  }

  @Override public UserErrorCallback errorErrorCallback() {
    return errorCallback;
  }

  protected void registerRequest(final int requestId, @NonNull final Disposable disposable) {
    cancelRequest(requestId);
    requestRegister.add(requestId, checkNotNull(disposable, "disposable == null"));
  }

  protected void notifyUserError(final int requestId, @Nullable final Throwable t, @Nullable final String message) {
    if (message == null && t instanceof UserMessageError) {
      errorCallback.notify(requestId, t, t.getMessage());
    } else {
      errorCallback.notify(requestId, t, message);
    }
  }

  protected void notifyUserError(final int requestId, @Nullable final Throwable t) {
    errorCallback.notify(requestId, t, null);
  }

  protected void showProgress(final int requestId) {
    progressLiveData.show(requestId);
  }

  protected void hideProgress(final int requestId) {
    progressLiveData.hide(requestId);
  }

}
