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

import rx.Subscriber;

/**
 * Created by henrytao on 4/15/16.
 */
public class SubscriptionUtils {

  public static <T> void onComplete(Subscriber<T> subscriber) {
    if (subscriber != null && !subscriber.isUnsubscribed()) {
      subscriber.onCompleted();
    }
  }

  public static <T> void onError(Subscriber<T> subscriber, Throwable throwable) {
    if (subscriber != null && !subscriber.isUnsubscribed()) {
      subscriber.onError(throwable);
    }
  }

  public static <T> void onNext(Subscriber<T> subscriber, T data) {
    if (subscriber != null && !subscriber.isUnsubscribed()) {
      subscriber.onNext(data);
    }
  }

  public static <T> void onNext(Subscriber<T> subscriber) {
    onNext(subscriber, null);
  }

  public static <T> void onNextAndComplete(Subscriber<T> subscriber, T data) {
    onNext(subscriber, data);
    onComplete(subscriber);
  }

  public static <T> void onNextAndComplete(Subscriber<T> subscriber) {
    onNextAndComplete(subscriber, null);
  }
}
