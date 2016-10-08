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

package com.shopify.mobilebuysdk.demo.ui.callback;

import com.shopify.mobilebuysdk.demo.R;
import com.shopify.mobilebuysdk.demo.ui.MainActivity;
import com.shopify.mobilebuysdk.demo.ui.base.BaseActivity;
import com.shopify.mobilebuysdk.demo.ui.checkout.CompleteCheckoutActivity;
import com.shopify.mobilebuysdk.demo.util.NavigationUtils;
import com.shopify.mobilebuysdk.demo.util.rx.Transformer;
import com.shopify.mobilebuysdk.demo.util.rx.UnsubscribeLifeCycle;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import butterknife.ButterKnife;

/**
 * Created by henrytao on 9/10/16.
 */
public class CallbackActivity extends BaseActivity {

  public static Intent newIntent(Context context) {
    return new Intent(context, CallbackActivity.class);
  }

  @Override
  public void onSetContentView(Bundle savedInstanceState) {
    setContentView(R.layout.activity_callback);
    ButterKnife.bind(this);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    manageSubscription(UnsubscribeLifeCycle.DESTROY,
        mShopifyService
            .getCheckoutFromServer()
            .compose(Transformer.applyIoScheduler())
            .subscribe(checkout -> {
              if (checkout.hasOrderId()) {
                NavigationUtils.startActivityAndFinishWithoutAnimation(this, CompleteCheckoutActivity.newIntent(this));
              } else {
                NavigationUtils.startActivityAndFinishWithoutAnimation(this, MainActivity.newIntent(this));
              }
            }, throwable -> {
              throwable.printStackTrace();
              NavigationUtils.startActivityAndFinishWithoutAnimation(this, MainActivity.newIntent(this));
            })
    );
  }
}
