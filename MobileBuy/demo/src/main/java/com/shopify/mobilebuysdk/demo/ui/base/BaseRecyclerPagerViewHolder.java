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

import com.shopify.mobilebuysdk.demo.util.rx.UnsubscribeLifeCycle;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;

import me.henrytao.recyclerpageradapter.RecyclerPagerAdapter;
import rx.Subscription;

/**
 * Created by henrytao on 8/28/16.
 */
public abstract class BaseRecyclerPagerViewHolder extends RecyclerPagerAdapter.ViewHolder implements BaseSubscription {

  private final BaseSubscription mSubscriptionManager;

  public BaseRecyclerPagerViewHolder(@NonNull BaseSubscription subscription, View itemView) {
    super(itemView);
    mSubscriptionManager = subscription;
  }

  @Override
  public void manageSubscription(UnsubscribeLifeCycle unsubscribeLifeCycle, Subscription... subscriptions) {
    mSubscriptionManager.manageSubscription(unsubscribeLifeCycle, subscriptions);
  }

  @Override
  public void manageSubscription(String id, Subscription subscription, UnsubscribeLifeCycle unsubscribeLifeCycle) {
    mSubscriptionManager.manageSubscription(id, subscription, unsubscribeLifeCycle);
  }

  @Override
  public void manageSubscription(Subscription subscription, UnsubscribeLifeCycle unsubscribeLifeCycle) {
    mSubscriptionManager.manageSubscription(subscription, unsubscribeLifeCycle);
  }

  @Override
  public void unsubscribe(String id) {
    mSubscriptionManager.unsubscribe(id);
  }

  public Activity getActivity() {
    return (Activity) itemView.getContext();
  }

  public Context getContext() {
    return itemView.getContext();
  }
}
