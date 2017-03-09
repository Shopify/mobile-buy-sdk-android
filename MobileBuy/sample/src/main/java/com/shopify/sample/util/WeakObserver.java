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

import java.lang.ref.WeakReference;

import io.reactivex.Observer;
import io.reactivex.annotations.Nullable;
import io.reactivex.observers.DisposableObserver;

/**
 * Rx observer implementation that delegates calls {@link Observer#onNext(Object)}, {@link Observer#onError(Throwable)}, {@link Observer#onCompleted()}
 * to specified actions. The purpose of this class is to avoid memory leaks in such way that anonymous action implementations,
 * shouldn't keep implicit reference to the outer object but have access to it as parameter. This class keeps weak reference
 * to the target object and provides it as a parameter during delegation calls. Delegated actions will be invoked only in
 * case if there is reference to target object.
 *
 * @param <TARGET>   class of target weak referenced object that will be passed to actions as a parameter
 * @param <RESPONSE> class that will be emitted by observable
 */
@SuppressWarnings("WeakerAccess")
public final class WeakObserver<TARGET, RESPONSE> {
  final WeakReference<TARGET> targetRef;
  OnNextDelegate<TARGET, ? super RESPONSE> onNextDelegate;
  OnErrorDelegate<TARGET> onErrorDelegate;
  OnCompleteDelegate<TARGET> onCompleteDelegate;

  public static <TARGET, RESPONSE> WeakObserver<TARGET, RESPONSE> forTarget(@Nullable final TARGET target) {
    return new WeakObserver<>(target);
  }

  public WeakObserver(@Nullable final TARGET target) {
    this.targetRef = new WeakReference<>(target);
  }

  public WeakObserver<TARGET, ? super RESPONSE> delegateOnNext(@Nullable final OnNextDelegate<TARGET, ? super RESPONSE> to) {
    this.onNextDelegate = to;
    return this;
  }

  public WeakObserver<TARGET, RESPONSE> delegateOnError(@Nullable final OnErrorDelegate<TARGET> to) {
    this.onErrorDelegate = to;
    return this;
  }

  public WeakObserver<TARGET, RESPONSE> delegateOnCompleteDelegate(@Nullable final OnCompleteDelegate<TARGET> to) {
    this.onCompleteDelegate = to;
    return this;
  }

  public DisposableObserver<? super RESPONSE> create() {
    return new DisposableObserver<RESPONSE>() {
      @Override public void onNext(final RESPONSE response) {
        final TARGET target = targetRef.get();
        if (target != null && onNextDelegate != null) {
          onNextDelegate.onNext(target, response);
        }
      }

      @Override public void onError(final Throwable t) {
        final TARGET target = targetRef.get();
        if (target != null && onErrorDelegate != null) {
          onErrorDelegate.onError(target, t);
        }
      }

      @Override public void onComplete() {
        final TARGET target = targetRef.get();
        if (target != null && onCompleteDelegate != null) {
          onCompleteDelegate.onComplete(target);
        }
      }
    };
  }

  public interface OnNextDelegate<TARGET, RESPONSE> {
    void onNext(TARGET target, RESPONSE response);
  }

  public interface OnErrorDelegate<TARGET> {
    void onError(TARGET target, Throwable throwable);
  }

  public interface OnCompleteDelegate<TARGET> {
    void onComplete(TARGET target);
  }
}