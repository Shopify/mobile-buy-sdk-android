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

package com.shopify.mobilebuysdk.demo.ui.checkout;

import com.shopify.mobilebuysdk.demo.R;
import com.shopify.mobilebuysdk.demo.data.CheckoutState;
import com.shopify.mobilebuysdk.demo.ui.base.BaseActivity;
import com.shopify.mobilebuysdk.demo.util.rx.Transformer;
import com.shopify.mobilebuysdk.demo.util.rx.UnsubscribeLifeCycle;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by henrytao on 8/30/16.
 */
public class CheckoutActivity extends BaseActivity {

  public static Intent newIntent(Context context) {
    Intent intent = new Intent(context, CheckoutActivity.class);
    return intent;
  }

  @BindView(R.id.toolbar) Toolbar vToolbar;

  @Override
  public void onBackPressed() {
    super.onBackPressed();
    if (!isTaskRoot()) {
      overridePendingTransition(R.anim.enter_ltr, R.anim.exit_ltr);
    }
  }

  @Override
  public void onSetContentView(Bundle savedInstanceState) {
    setContentView(R.layout.activity_checkout);
    ButterKnife.bind(this);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setSupportActionBar(vToolbar);
    vToolbar.setNavigationOnClickListener(view -> onBackPressed());

    manageSubscription(UnsubscribeLifeCycle.DESTROY,
        mShopifyService
            .observeCheckoutState()
            .compose(Transformer.applyComputationScheduler())
            .subscribe(this::onCheckoutStateChanged, Throwable::printStackTrace)
    );
  }

  private void onCheckoutStateChanged(CheckoutState state) {
    Fragment fragment;
    switch (state) {
      case PAYMENT_METHOD:
        fragment = PaymentMethodFragment.newInstance();
        break;
      //case SHIPPING_ADDRESS:
      //  break;
      //case SHIPPING_RATES:
      //  break;
      //case SUMMARY_BEFORE_PAYMENT:
      //  break;
      //case PROCESSING:
      //  break;
      //case PAYMENT_SUCCESS:
      //  break;
      default:
        fragment = PaymentMethodFragment.newInstance();
        break;
    }
    getSupportFragmentManager()
        .beginTransaction()
        .replace(R.id.fragment, fragment)
        .commit();
  }
}
