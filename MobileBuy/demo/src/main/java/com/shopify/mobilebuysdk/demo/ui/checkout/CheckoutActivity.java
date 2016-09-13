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
import com.shopify.mobilebuysdk.demo.ui.base.BaseActivity;
import com.shopify.mobilebuysdk.demo.util.NavigationUtils;
import com.shopify.mobilebuysdk.demo.util.rx.UnsubscribeLifeCycle;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

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
  }

  @OnClick(R.id.btn_android_pay_checkout)
  protected void onAndroidPayCheckoutClick() {
    //manageSubscription(UnsubscribeLifeCycle.DESTROY_VIEW,
    //    mShopifyService.createCheckout().subscribe(checkout -> {
    //      String merchantName = getString(R.string.app_name);
    //      MaskedWalletRequest maskedWalletRequest = AndroidPayHelper.createMaskedWalletRequest(merchantName, checkout, )
    //    }, Throwable::printStackTrace));
  }

  @OnClick(R.id.btn_web_checkout)
  protected void onWebCheckoutClick() {
    manageSubscription(UnsubscribeLifeCycle.DESTROY_VIEW,
        mShopifyService.createCheckout().subscribe(checkout -> {
          Intent intent = new Intent(Intent.ACTION_VIEW);
          intent.setData(Uri.parse(checkout.getWebUrl()));
          NavigationUtils.startActivity(this, intent);
        }, Throwable::printStackTrace));
  }
}
