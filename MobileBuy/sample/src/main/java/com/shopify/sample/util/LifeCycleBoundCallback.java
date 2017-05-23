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

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.OnLifecycleEvent;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import static android.arch.lifecycle.Lifecycle.State.DESTROYED;
import static android.arch.lifecycle.Lifecycle.State.STARTED;

public final class LifeCycleBoundCallback<T> {
  private final List<LifecycleBoundObserver> observers = new LinkedList<>();
  private final ReadWriteLock lock = new ReentrantReadWriteLock();

  public void observe(final Lifecycle lifecycle, final Observer<T> observer) {
    if (lifecycle.getCurrentState() == DESTROYED) {
      return;
    }

    lock.writeLock().lock();
    try {
      observers.add(new LifecycleBoundObserver(lifecycle, observer));
    } finally {
      lock.writeLock().unlock();
    }
  }

  public void notify(final T data) {
    lock.readLock().lock();
    try {
      for (final LifecycleBoundObserver observer : observers) {
        if (observer.currentState.isAtLeast(STARTED)) {
          observer.observer.onChanged(data);
        }
      }
    } finally {
      lock.readLock().unlock();
    }
  }

  @SuppressWarnings("WeakerAccess")
  class LifecycleBoundObserver implements LifecycleObserver {
    Lifecycle.State currentState;
    final Observer<T> observer;

    LifecycleBoundObserver(Lifecycle lifecycle, Observer<T> observer) {
      this.observer = observer;
      lifecycle.addObserver(this);
    }

    @SuppressWarnings("unused")
    @OnLifecycleEvent(Lifecycle.Event.ON_ANY)
    void onStateChange(final LifecycleOwner lifecycleOwner, final Lifecycle.Event event) {
      currentState = lifecycleOwner.getLifecycle().getCurrentState();
      if (currentState == DESTROYED) {
        lock.writeLock().lock();
        try {
          observers.remove(this);
        } finally {
          lock.writeLock().unlock();
        }
      }
    }
  }
}
