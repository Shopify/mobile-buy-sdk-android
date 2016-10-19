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

import com.shopify.mobilebuysdk.demo.R;

import android.content.Context;
import android.support.v7.app.AlertDialog;

import me.henrytao.rxsharedpreferences.util.SubscriptionUtils;
import rx.Observable;
import rx.subscriptions.Subscriptions;

/**
 * Created by henrytao on 9/22/16.
 */
public class DialogHelper {

  public static Observable<PaymentMethodResult> showOtherPaymentMethodsDialog(Context context) {
    return Observable.create(subscriber -> {
      CharSequence[] options = new CharSequence[]{
          context.getString(R.string.text_web_checkout),
          context.getString(R.string.text_android_pay_checkout)
      };
      AlertDialog.Builder builder = new AlertDialog.Builder(context)
          .setTitle(context.getString(R.string.text_select_other_payment_methods))
          .setOnDismissListener(dialogInterface -> {
            SubscriptionUtils.onNextAndComplete(subscriber, new PaymentMethodResult(Action.NEGATIVE, PaymentMethod.NONE));
          })
          .setItems(options, (dialogInterface, i) -> {
            SubscriptionUtils.onNextAndComplete(subscriber, new PaymentMethodResult(Action.POSITIVE,
                i == 0 ? PaymentMethod.WEB : (i == 1 ? PaymentMethod.ANDROID_PAY : PaymentMethod.NATIVE)
            ));
          });
      AlertDialog dialog = builder.show();
      subscriber.add(Subscriptions.create(dialog::dismiss));
    });
  }

  public enum Action {
    POSITIVE, NEGATIVE
  }

  public enum PaymentMethod {
    NONE, WEB, ANDROID_PAY, NATIVE
  }

  public static class PaymentMethodResult extends Result {

    public final PaymentMethod paymentMethod;

    public PaymentMethodResult(Action action, PaymentMethod paymentMethod) {
      super(action);
      this.paymentMethod = paymentMethod;
    }
  }

  public static class Result {

    public final Action action;

    public Result(Action action) {
      this.action = action;
    }
  }
}
