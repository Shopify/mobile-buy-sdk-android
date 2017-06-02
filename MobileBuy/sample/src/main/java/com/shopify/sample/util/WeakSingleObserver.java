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

package com.shopify.sample.util;

import android.support.annotation.Nullable;

import java.lang.ref.WeakReference;

import io.reactivex.observers.DisposableSingleObserver;

@SuppressWarnings("WeakerAccess")
public class WeakSingleObserver<TARGET, RESPONSE> {
  final WeakReference<TARGET> targetRef;
  OnSuccessDelegate<TARGET, ? super RESPONSE> onNextDelegate;
  OnErrorDelegate<TARGET> onErrorDelegate;

  public static <TARGET, RESPONSE> WeakSingleObserver<TARGET, RESPONSE> forTarget(@Nullable final TARGET target) {
    return new WeakSingleObserver<>(target);
  }

  public WeakSingleObserver(@Nullable final TARGET target) {
    this.targetRef = new WeakReference<>(target);
  }

  public WeakSingleObserver<TARGET, ? super RESPONSE> delegateOnSuccess(@Nullable final OnSuccessDelegate<TARGET, ? super RESPONSE> to) {
    this.onNextDelegate = to;
    return this;
  }

  public WeakSingleObserver<TARGET, RESPONSE> delegateOnError(@Nullable final OnErrorDelegate<TARGET> to) {
    this.onErrorDelegate = to;
    return this;
  }

  public DisposableSingleObserver<? super RESPONSE> create() {
    return new DisposableSingleObserver<RESPONSE>() {
      @Override public void onSuccess(final RESPONSE response) {
        final TARGET target = targetRef.get();
        if (target != null && onNextDelegate != null) {
          onNextDelegate.onSuccess(target, response);
        }
      }

      @Override public void onError(final Throwable t) {
        final TARGET target = targetRef.get();
        if (target != null && onErrorDelegate != null) {
          onErrorDelegate.onError(target, t);
        }
      }
    };
  }

  public interface OnSuccessDelegate<TARGET, RESPONSE> {
    void onSuccess(TARGET target, RESPONSE response);
  }

  public interface OnErrorDelegate<TARGET> {
    void onError(TARGET target, Throwable throwable);
  }
}
