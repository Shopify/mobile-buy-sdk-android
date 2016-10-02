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

package com.shopify.mobilebuysdk.demo.util;

import com.shopify.mobilebuysdk.demo.config.Constants;
import com.shopify.mobilebuysdk.demo.ui.base.BaseActivity;
import com.shopify.mobilebuysdk.demo.ui.base.BaseFragment;
import com.shopify.mobilebuysdk.demo.util.rx.Transformer;
import com.shopify.mobilebuysdk.demo.util.rx.UnsubscribeLifeCycle;

import android.app.Activity;
import android.app.ProgressDialog;
import android.support.annotation.StringRes;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.subscriptions.Subscriptions;

/**
 * Created by henrytao on 9/28/16.
 */

public class ProgressDialogUtils {

  public static <T> Observable.Transformer<T, T> apply(BaseFragment observer, @StringRes int message) {
    String ID = IDUtils.generate();
    return observable -> observable
        .doOnSubscribe(() -> observer.manageSubscription(
            ID,
            Observable.just(null)
                .delay(Constants.Timeout.SHORT, TimeUnit.MILLISECONDS)
                .flatMap(o -> showProgressDialog(observer.getActivity(), message).compose(Transformer.applyMainThreadScheduler()))
                .subscribe(aVoid -> {
                }, Throwable::printStackTrace),
            UnsubscribeLifeCycle.DESTROY_VIEW))
        .doOnCompleted(() -> observer.unsubscribe(ID))
        .doOnError(throwable -> observer.unsubscribe(ID))
        .doOnUnsubscribe(() -> observer.unsubscribe(ID));
  }

  public static <T> Observable.Transformer<T, T> apply(BaseActivity observer, @StringRes int message) {
    String ID = IDUtils.generate();
    return observable -> observable
        .doOnSubscribe(() -> observer.manageSubscription(
            ID,
            Observable.just(null)
                .delay(Constants.Timeout.SHORT, TimeUnit.MILLISECONDS)
                .flatMap(o -> showProgressDialog(observer, message).compose(Transformer.applyMainThreadScheduler()))
                .subscribe(aVoid -> {
                }, Throwable::printStackTrace),
            UnsubscribeLifeCycle.DESTROY_VIEW))
        .doOnCompleted(() -> observer.unsubscribe(ID))
        .doOnError(throwable -> observer.unsubscribe(ID))
        .doOnUnsubscribe(() -> observer.unsubscribe(ID));
  }

  public static Observable<Void> showProgressDialog(Activity activity, @StringRes int message) {
    return Observable.create(subscriber -> {
      if (activity != null) {
        ProgressDialog progressDialog = ProgressDialog.show(activity, null, activity.getString(message));
        subscriber.add(Subscriptions.create(() -> {
          if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
          }
        }));
      }
    });
  }
}
