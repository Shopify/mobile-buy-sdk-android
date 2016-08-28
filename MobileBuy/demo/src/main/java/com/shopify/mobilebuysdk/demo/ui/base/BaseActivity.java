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

package com.shopify.mobilebuysdk.demo.ui.base;

import com.shopify.mobilebuysdk.demo.util.rx.SubscriptionManager;
import com.shopify.mobilebuysdk.demo.util.rx.UnsubscribeLifeCycle;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;

import rx.Subscription;

/**
 * Created by henrytao on 8/26/16.
 */
public abstract class BaseActivity extends AppCompatActivity implements BaseLifeCycle, BaseSubscription {

  public abstract void onSetContentView(Bundle savedInstanceState);

  private boolean mIsDestroy;

  private boolean mIsDestroyView;

  private boolean mIsPause;

  private boolean mIsStop;

  private SubscriptionManager mSubscriptionManager = new SubscriptionManager();

  @Override
  public void manageSubscription(UnsubscribeLifeCycle unsubscribeLifeCycle, Subscription... subscriptions) {
    for (Subscription subscription : subscriptions) {
      manageSubscription(subscription, unsubscribeLifeCycle);
    }
  }

  @Override
  public void manageSubscription(Subscription subscription, UnsubscribeLifeCycle unsubscribeLifeCycle) {
    if ((unsubscribeLifeCycle == UnsubscribeLifeCycle.DESTROY && mIsDestroy) ||
        (unsubscribeLifeCycle == UnsubscribeLifeCycle.DESTROY_VIEW && mIsDestroyView) ||
        (unsubscribeLifeCycle == UnsubscribeLifeCycle.STOP && mIsStop) ||
        (unsubscribeLifeCycle == UnsubscribeLifeCycle.PAUSE && mIsPause)) {
      if (subscription != null && !subscription.isUnsubscribed()) {
        subscription.unsubscribe();
      }
      return;
    }
    mSubscriptionManager.manageSubscription(subscription, unsubscribeLifeCycle);
  }

  @Override
  public void manageSubscription(String id, Subscription subscription, UnsubscribeLifeCycle unsubscribeLifeCycle) {
    if ((unsubscribeLifeCycle == UnsubscribeLifeCycle.DESTROY && mIsDestroy) ||
        (unsubscribeLifeCycle == UnsubscribeLifeCycle.DESTROY_VIEW && mIsDestroyView) ||
        (unsubscribeLifeCycle == UnsubscribeLifeCycle.STOP && mIsStop) ||
        (unsubscribeLifeCycle == UnsubscribeLifeCycle.PAUSE && mIsPause)) {
      if (subscription != null && !subscription.isUnsubscribed()) {
        subscription.unsubscribe();
      }
      return;
    }
    mSubscriptionManager.manageSubscription(id, subscription, unsubscribeLifeCycle);
  }

  @Override
  public void onCreate() {
    mIsDestroy = false;
  }

  @Override
  public void onCreateView() {
    mIsDestroyView = false;
  }

  @Override
  public void onDestroy() {
    onDestroyView();
    mIsDestroy = true;
    mSubscriptionManager.unsubscribe(UnsubscribeLifeCycle.DESTROY);
    mSubscriptionManager.unsubscribe();
    super.onDestroy();
  }

  @Override
  public void onDestroyView() {
    mIsDestroyView = true;
    mSubscriptionManager.unsubscribe(UnsubscribeLifeCycle.DESTROY_VIEW);
  }

  @Override
  public void onPause() {
    mIsPause = true;
    mSubscriptionManager.unsubscribe(UnsubscribeLifeCycle.PAUSE);
    super.onPause();
  }

  @Override
  public void onResume() {
    mIsPause = false;
    super.onResume();
  }

  @Override
  public void onStart() {
    mIsStop = false;
    super.onStart();
  }

  @Override
  public void onStop() {
    mIsStop = true;
    mSubscriptionManager.unsubscribe(UnsubscribeLifeCycle.STOP);
    super.onStop();
  }

  @Override
  public void unsubscribe(String id) {
    mSubscriptionManager.unsubscribe(id);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
      onSharedElementConfig(getWindow());
    }
    onCreate();
    onSetContentView(savedInstanceState);
    onCreateView();
  }

  @TargetApi(Build.VERSION_CODES.LOLLIPOP)
  protected void onSharedElementConfig(Window window) {

  }
}
