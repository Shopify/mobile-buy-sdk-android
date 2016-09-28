/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2016 Shopify Inc.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 *
 */

package com.shopify.mobilebuysdk.demo.util.rx;

import android.os.Build;
import android.os.Looper;

import rx.Observable;
import rx.Scheduler;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by henrytao on 4/23/16.
 */
public class Transformer {

  private static SchedulerManager sComputationScheduler = new SchedulerManager(Schedulers::computation);

  private static SchedulerManager sIoScheduler = new SchedulerManager(Schedulers::io);

  private static SchedulerManager sMainThreadScheduler = new SchedulerManager(AndroidSchedulers::mainThread);

  private static SchedulerManager sNewThreadScheduler = new SchedulerManager(Schedulers::newThread);

  public static <T> Observable.Transformer<T, T> applyComputationScheduler() {
    return observable -> observable
        .subscribeOn(sComputationScheduler.get())
        .observeOn(sMainThreadScheduler.get());
  }

  public static <T> Observable.Transformer<T, T> applyIoScheduler() {
    return observable -> observable
        .subscribeOn(sIoScheduler.get())
        .observeOn(sMainThreadScheduler.get());
  }

  public static <T> Observable.Transformer<T, T> applyMainThreadScheduler() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
      if (Looper.getMainLooper().isCurrentThread()) {
        return observable -> observable;
      }
    } else {
      if (Thread.currentThread() == Looper.getMainLooper().getThread()) {
        return observable -> observable;
      }
    }
    return observable -> observable
        .subscribeOn(sMainThreadScheduler.get())
        .observeOn(sMainThreadScheduler.get());
  }

  public static <T> Observable.Transformer<T, T> applyNewThreadScheduler() {
    return observable -> observable
        .subscribeOn(sNewThreadScheduler.get())
        .observeOn(sMainThreadScheduler.get());
  }

  public static Scheduler getComputationScheduler() {
    return sComputationScheduler.get();
  }

  public static Scheduler getIoScheduler() {
    return sIoScheduler.get();
  }

  public static Scheduler getMainThreadScheduler() {
    return sMainThreadScheduler.get();
  }

  public static Scheduler getNewThreadScheduler() {
    return sNewThreadScheduler.get();
  }

  public static void overrideComputationScheduler(Scheduler scheduler) {
    sComputationScheduler.set(scheduler);
  }

  public static void overrideIoScheduler(Scheduler scheduler) {
    sIoScheduler.set(scheduler);
  }

  public static void overrideMainThreadScheduler(Scheduler scheduler) {
    sMainThreadScheduler.set(scheduler);
  }

  public static void overrideNewThreadScheduler(Scheduler scheduler) {
    sNewThreadScheduler.set(scheduler);
  }

  public static void resetComputationScheduler() {
    sComputationScheduler.reset();
  }

  public static void resetIoScheduler() {
    sIoScheduler.reset();
  }

  public static void resetMainThreadScheduler() {
    sMainThreadScheduler.reset();
  }

  public static void resetNewThreadScheduler() {
    sNewThreadScheduler.reset();
  }

  private static class SchedulerManager {

    private Callback mDefaultSchedulerCallback;

    private Scheduler mScheduler;

    public SchedulerManager(Callback defaultSchedulerCallback) {
      mDefaultSchedulerCallback = defaultSchedulerCallback;
    }

    public Scheduler get() {
      return mScheduler != null ? mScheduler : mDefaultSchedulerCallback.get();
    }

    public void reset() {
      set(null);
    }

    public void set(Scheduler scheduler) {
      mScheduler = scheduler;
    }

    private interface Callback {

      Scheduler get();
    }
  }
}
