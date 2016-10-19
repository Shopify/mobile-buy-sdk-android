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

import android.annotation.TargetApi;
import android.os.Build;
import android.support.annotation.NonNull;
import android.transition.Transition;

import java.lang.ref.WeakReference;

import rx.functions.Action1;

/**
 * Created by henrytao on 8/28/16.
 */

public class TransitionUtils {

  public static <T> void addOnTransitionEndListener(Transition transition, T object, @NonNull Action1<T> callback) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
      WeakReference<Transition> transitionWeakReference = new WeakReference<>(transition);
      WeakReference<T> objectWeakReference = new WeakReference<>(object);
      transition.addListener(new Transition.TransitionListener() {
        @Override
        public void onTransitionCancel(Transition transition) {

        }

        @TargetApi(Build.VERSION_CODES.KITKAT)
        @Override
        public void onTransitionEnd(Transition transition) {
          Transition t = transitionWeakReference.get();
          T o = objectWeakReference.get();
          if (t != null) {
            t.removeListener(this);
          }
          if (o != null) {
            callback.call(o);
          }
        }

        @Override
        public void onTransitionPause(Transition transition) {

        }

        @Override
        public void onTransitionResume(Transition transition) {

        }

        @Override
        public void onTransitionStart(Transition transition) {

        }
      });
    }
  }
}
