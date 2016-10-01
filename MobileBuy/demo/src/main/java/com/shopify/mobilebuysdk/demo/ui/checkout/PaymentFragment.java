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
import com.shopify.mobilebuysdk.demo.ui.base.BaseFragment;
import com.shopify.mobilebuysdk.demo.util.NavigationUtils;
import com.shopify.mobilebuysdk.demo.util.rx.Transformer;
import com.shopify.mobilebuysdk.demo.util.rx.UnsubscribeLifeCycle;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by henrytao on 9/14/16.
 */
public class PaymentFragment extends BaseFragment {

  public static PaymentFragment newInstance() {
    return new PaymentFragment();
  }

  private Unbinder mUnbinder;

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    mUnbinder.unbind();
  }

  @Override
  public View onInflateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_payment, container, false);
    mUnbinder = ButterKnife.bind(this, view);
    return view;
  }

  @OnClick(R.id.btn_android_pay_checkout)
  protected void onAndroidPayCheckoutClick() {
    //manageSubscription(UnsubscribeLifeCycle.DESTROY_VIEW,
    //    mShopifyService.getCheckout().subscribe(checkout -> {
    //      String merchantName = getString(R.string.app_name);
    //      MaskedWalletRequest maskedWalletRequest = AndroidPayHelper.createMaskedWalletRequest(merchantName, checkout, )
    //    }, Throwable::printStackTrace));
  }

  @OnClick(R.id.btn_native_checkout)
  protected void onNativeCheckoutClick() {
    manageSubscription(UnsubscribeLifeCycle.DESTROY_VIEW,
        mShopifyService
            .setCheckoutState(CheckoutState.ADDRESS)
            .compose(Transformer.applyComputationScheduler())
            .subscribe()
    );
  }

  @OnClick(R.id.btn_web_checkout)
  protected void onWebCheckoutClick() {
    manageSubscription(UnsubscribeLifeCycle.DESTROY_VIEW,
        mShopifyService
            .getCheckout()
            .compose(Transformer.applyIoScheduler())
            .subscribe(checkout -> {
              Intent intent = new Intent(Intent.ACTION_VIEW);
              intent.setData(Uri.parse(checkout.getWebUrl()));
              NavigationUtils.startActivity(getActivity(), intent);
            }, Throwable::printStackTrace));
  }
}
