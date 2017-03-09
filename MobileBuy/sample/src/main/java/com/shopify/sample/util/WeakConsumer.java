package com.shopify.sample.util;

import java.lang.ref.WeakReference;

import io.reactivex.annotations.NonNull;
import io.reactivex.annotations.Nullable;
import io.reactivex.functions.Consumer;

public class WeakConsumer<TARGET, RESPONSE> {
  final WeakReference<TARGET> targetRef;
  AcceptDelegate<TARGET, RESPONSE> acceptDelegate;

  public static <TARGET, RESPONSE> WeakConsumer<TARGET, RESPONSE> forTarget(@Nullable final TARGET target) {
    return new WeakConsumer<>(target);
  }

  private WeakConsumer(@Nullable final TARGET target) {
    this.targetRef = new WeakReference<>(target);
  }

  public WeakConsumer<TARGET, RESPONSE> delegateAccept(@Nullable final AcceptDelegate<TARGET, RESPONSE> to) {
    this.acceptDelegate = to;
    return this;
  }

  public Consumer<RESPONSE> create() {
    return new Consumer<RESPONSE>() {
      @Override public void accept(@NonNull final RESPONSE response) throws Exception {
        final TARGET target = targetRef.get();
        if (target != null && acceptDelegate != null) {
          acceptDelegate.accept(target, response);
        }
      }
    };
  }

  public static interface AcceptDelegate<TARGET, RESPONSE> {
    void accept(TARGET target, RESPONSE response);
  }
}
