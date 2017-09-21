package com.shopify.sample.util;

import android.os.Handler;
import android.os.Looper;

public class Executors {

  private final Handler mainThreadHandler;

  public static Executors getDefault() {
    return new Executors(new Handler(Looper.getMainLooper()));
  }

  private Executors(Handler mainThreadHandler) {
    this.mainThreadHandler = mainThreadHandler;
  }

  public Handler getMainThreadHandler() {
    return mainThreadHandler;
  }
}
