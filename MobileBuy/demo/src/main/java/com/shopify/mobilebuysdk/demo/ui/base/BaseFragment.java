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

import com.shopify.mobilebuysdk.demo.service.ShopifyService;
import com.shopify.mobilebuysdk.demo.util.rx.SubscriptionManager;
import com.shopify.mobilebuysdk.demo.util.rx.UnsubscribeLifeCycle;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import rx.Subscription;

/**
 * Created by henrytao on 8/26/16.
 */
public abstract class BaseFragment extends Fragment implements BaseLifeCycle, BaseSubscription {

  public abstract View onInflateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState);

  protected final ShopifyService mShopifyService;

  private boolean mIsDestroy;

  private boolean mIsDestroyView;

  private boolean mIsPause;

  private boolean mIsStop;

  private SubscriptionManager mSubscriptionManager = new SubscriptionManager();

  public BaseFragment() {
    mShopifyService = ShopifyService.getInstance();
  }

  @Override
  public void manageSubscription(UnsubscribeLifeCycle unsubscribeLifeCycle, Subscription... subscriptions) {
    for (Subscription subscription : subscriptions) {
      manageSubscription(subscription, unsubscribeLifeCycle);
    }
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
  public void onCreate() {
    mIsDestroy = false;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    Bundle bundle = getArguments();
    onInitializeBundle(bundle != null ? bundle : new Bundle(), savedInstanceState != null ? savedInstanceState : new Bundle());
    super.onCreate(savedInstanceState);
    onCreate();
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    return onInflateView(inflater, container, savedInstanceState);
  }

  public void onCreateView() {
    mIsDestroyView = false;
  }

  @Override
  public void onDestroy() {
    mIsDestroy = true;
    mSubscriptionManager.unsubscribe(UnsubscribeLifeCycle.DESTROY);
    mSubscriptionManager.unsubscribe();
    super.onDestroy();
  }

  @Override
  public void onDestroyView() {
    mIsDestroyView = true;
    mSubscriptionManager.unsubscribe(UnsubscribeLifeCycle.DESTROY_VIEW);
    super.onDestroyView();
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
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    onCreateView();
  }

  @Override
  public void unsubscribe(String id) {
    mSubscriptionManager.unsubscribe(id);
  }

  public void onInitializeBundle(@NonNull Bundle bundle, @NonNull Bundle savedInstanceState) {
  }
}
