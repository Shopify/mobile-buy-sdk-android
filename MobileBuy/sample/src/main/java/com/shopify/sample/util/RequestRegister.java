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

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

import io.reactivex.disposables.Disposable;

public final class RequestRegister<T> implements Disposable {
  private final Map<T, Disposable> disposablePool = new ConcurrentHashMap<>();
  private final AtomicBoolean disposed = new AtomicBoolean();

  @Override public void dispose() {
    if (disposed.compareAndSet(false, true)) {
      synchronized (disposablePool) {
        for (Disposable disposable : disposablePool.values()) {
          disposable.dispose();
        }
        disposablePool.clear();
      }
    }
  }

  @Override public boolean isDisposed() {
    return disposed.get();
  }

  public void add(final T id, final Disposable disposable) {
    if (disposed.get()) {
      disposable.dispose();
    } else {
      Disposable prevDisposable = disposablePool.put(id, disposable);
      if (prevDisposable != null) {
        prevDisposable.dispose();
      }
    }
  }

  public void delete(final T id) {
    Disposable disposable = disposablePool.remove(id);
    if (disposable != null) {
      disposable.dispose();
    }
  }

  public void deleteAll() {
    synchronized (disposablePool) {
      for (Disposable disposable : disposablePool.values()) {
        disposable.dispose();
      }
      disposablePool.clear();
    }
  }
}
