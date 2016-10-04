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
import com.shopify.mobilebuysdk.demo.ui.base.BaseFragment;
import com.shopify.mobilebuysdk.demo.util.ToastUtils;
import com.shopify.mobilebuysdk.demo.widget.MaskedEditText;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by henrytao on 9/14/16.
 */
public class PaymentFragment extends BaseFragment {

  public static PaymentFragment newInstance() {
    return new PaymentFragment();
  }

  @BindView(R.id.btn_android_pay) Button vBtnAndroidPay;

  @BindView(R.id.btn_next) Button vBtnNext;

  @BindView(R.id.input_cvv_code) EditText vInputCVVCode;

  @BindView(R.id.input_card_number) EditText vInputCardNumber;

  @BindView(R.id.input_expires) MaskedEditText vInputExpires;

  @BindView(R.id.til_cvv_code) TextInputLayout vTilCVVCode;

  @BindView(R.id.til_card_number) TextInputLayout vTilCardNumber;

  @BindView(R.id.til_expires) TextInputLayout vTilExpires;

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

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    vBtnAndroidPay.setOnClickListener(this::onAndroidPayClicked);

    vInputExpires.setText("01-04");

    //manageSubscription(UnsubscribeLifeCycle.DESTROY_VIEW,
    //    mShopifyService
    //
    //);
  }

  private void onAndroidPayClicked(View view) {
    ToastUtils.showAndroidPaySetupMessage(getContext());
  }

  //@OnClick(R.id.btn_android_pay_checkout)
  //protected void onAndroidPayCheckoutClick() {
  //  //manageSubscription(UnsubscribeLifeCycle.DESTROY_VIEW,
  //  //    mShopifyService.getCheckout().subscribe(checkout -> {
  //  //      String merchantName = getString(R.string.app_name);
  //  //      MaskedWalletRequest maskedWalletRequest = AndroidPayHelper.createMaskedWalletRequest(merchantName, checkout, )
  //  //    }, Throwable::printStackTrace));
  //}
  //
  //@OnClick(R.id.btn_native_checkout)
  //protected void onNativeCheckoutClick() {
  //  manageSubscription(UnsubscribeLifeCycle.DESTROY_VIEW,
  //      mShopifyService
  //          .setCheckoutState(CheckoutState.ADDRESS)
  //          .compose(Transformer.applyComputationScheduler())
  //          .subscribe()
  //  );
  //}
  //
  //@OnClick(R.id.btn_web_checkout)
  //protected void onWebCheckoutClick() {
  //  manageSubscription(UnsubscribeLifeCycle.DESTROY_VIEW,
  //      mShopifyService
  //          .getCheckout()
  //          .compose(Transformer.applyIoScheduler())
  //          .subscribe(checkout -> {
  //            Intent intent = new Intent(Intent.ACTION_VIEW);
  //            intent.setData(Uri.parse(checkout.getWebUrl()));
  //            NavigationUtils.startActivity(getActivity(), intent);
  //          }, Throwable::printStackTrace));
  //}
}
