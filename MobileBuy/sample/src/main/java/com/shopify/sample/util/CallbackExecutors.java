package com.shopify.sample.util;

import android.os.Handler;
import android.os.Looper;

public final class CallbackExecutors {

  private final Handler handler;

  public static CallbackExecutors getDefault() {
    return new CallbackExecutors(new Handler(Looper.getMainLooper()));
  }

  private CallbackExecutors(Handler handler) {
    this.handler = handler;
  }

  public Handler getHandler() {
    return handler;
  }
}
