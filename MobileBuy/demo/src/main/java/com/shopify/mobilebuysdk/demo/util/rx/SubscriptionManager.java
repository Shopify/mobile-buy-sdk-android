/*
 * Copyright 2016 "Henry Tao <hi@henrytao.me>"
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.shopify.mobilebuysdk.demo.util.rx;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import rx.Subscription;

/**
 * Created by henrytao on 11/13/15.
 */
public class SubscriptionManager implements ISubscriptionManager {

  private Map<UnsubscribeLifeCycle, Map<String, Subscription>> mSubscriptions;

  public SubscriptionManager() {
    mSubscriptions = new HashMap<>();
  }

  @Override
  public void manageSubscription(Subscription subscription, UnsubscribeLifeCycle unsubscribeLifeCycle) {
    manageSubscription(UUID.randomUUID().toString(), subscription, unsubscribeLifeCycle);
  }

  @Override
  public void manageSubscription(String id, Subscription subscription, UnsubscribeLifeCycle unsubscribeLifeCycle) {
    Map<String, Subscription> subscriptions = getSubscriptions(unsubscribeLifeCycle);
    if (subscriptions.containsKey(id)) {
      unsubscribe(id);
    }
    subscriptions.put(id, subscription);
  }

  @Override
  public void unsubscribe(UnsubscribeLifeCycle unsubscribeLifeCycle) {
    unsubscribe(getSubscriptions(unsubscribeLifeCycle));
  }

  @Override
  public void unsubscribe() {
    for (Map.Entry<UnsubscribeLifeCycle, Map<String, Subscription>> entry : mSubscriptions.entrySet()) {
      unsubscribe(entry.getValue());
    }
    mSubscriptions.clear();
  }

  @Override
  public void unsubscribe(String id) {
    for (Map.Entry<UnsubscribeLifeCycle, Map<String, Subscription>> entry : mSubscriptions.entrySet()) {
      unsubscribe(entry.getValue().get(id));
    }
  }

  private Map<String, Subscription> getSubscriptions(UnsubscribeLifeCycle unsubscribeLifeCycle) {
    Map<String, Subscription> subscriptions = mSubscriptions.get(unsubscribeLifeCycle);
    if (subscriptions == null) {
      subscriptions = new HashMap<>();
      mSubscriptions.put(unsubscribeLifeCycle, subscriptions);
    }
    return subscriptions;
  }

  private void unsubscribe(Map<String, Subscription> subscriptions) {
    for (Map.Entry<String, Subscription> entry : subscriptions.entrySet()) {
      unsubscribe(entry.getValue());
    }
    subscriptions.clear();
  }

  private void unsubscribe(Subscription subscription) {
    if (subscription != null && !subscription.isUnsubscribed()) {
      subscription.unsubscribe();
    }
  }
}
