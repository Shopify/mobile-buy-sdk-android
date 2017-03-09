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
