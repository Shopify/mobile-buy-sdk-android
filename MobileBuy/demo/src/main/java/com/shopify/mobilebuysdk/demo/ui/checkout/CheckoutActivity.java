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

    manageSubscription(UnsubscribeLifeCycle.DESTROY,
        mShopifyService
            .observeCheckoutState()
            .compose(Transformer.applyComputationScheduler())
            .subscribe(this::onCheckoutStateChanged, Throwable::printStackTrace)
    );
  }

  @OnClick(R.id.btn_payment_method)
  protected void onPaymentMethodClicked() {
    manageSubscription(UnsubscribeLifeCycle.DESTROY_VIEW,
        mShopifyService
            .setCheckoutState(CheckoutState.PAYMENT_METHOD)
            .compose(Transformer.applyComputationScheduler())
            .subscribe(aVoid -> {
            }, Throwable::printStackTrace));
  }

  @OnClick(R.id.btn_shipping_address)
  protected void onShippingAddressClicked() {
    manageSubscription(UnsubscribeLifeCycle.DESTROY_VIEW,
        mShopifyService
            .setCheckoutState(CheckoutState.SHIPPING_ADDRESS)
            .compose(Transformer.applyComputationScheduler())
            .subscribe(aVoid -> {
            }, Throwable::printStackTrace));
  }

  @OnClick(R.id.btn_shipping_rates)
  protected void onShippingRatesClicked() {
    manageSubscription(UnsubscribeLifeCycle.DESTROY_VIEW,
        mShopifyService
            .setCheckoutState(CheckoutState.SHIPPING_RATES)
            .compose(Transformer.applyComputationScheduler())
            .subscribe(aVoid -> {
            }, Throwable::printStackTrace));
  }

  @OnClick(R.id.btn_summary)
  protected void onSummaryClicked() {
    manageSubscription(UnsubscribeLifeCycle.DESTROY_VIEW,
        mShopifyService
            .setCheckoutState(CheckoutState.SUMMARY_BEFORE_PAYMENT)
            .compose(Transformer.applyComputationScheduler())
            .subscribe(aVoid -> {
            }, Throwable::printStackTrace));
  }

  private void onCheckoutStateChanged(CheckoutState state) {
    Fragment fragment;
    switch (state) {
      case PAYMENT_METHOD:
        fragment = PaymentMethodFragment.newInstance();
        break;
      case SHIPPING_ADDRESS:
        fragment = ShippingAddressFragment.newInstance();
        break;
      case SHIPPING_RATES:
        fragment = ShippingRatesFragment.newInstance();
        break;
      case SUMMARY_BEFORE_PAYMENT:
        fragment = SummaryFragment.newInstance();
        break;
      case PROCESSING:
        fragment = ProcessingFragment.newInstance();
        break;
      case PAYMENT_SUCCESS:
        fragment = PurchaseSuccessFragment.newInstance();
        break;
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
