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

import com.shopify.buy.model.CreditCard;
import com.shopify.mobilebuysdk.demo.R;
import com.shopify.mobilebuysdk.demo.data.CheckoutState;
import com.shopify.mobilebuysdk.demo.ui.base.BaseFragment;
import com.shopify.mobilebuysdk.demo.util.EditTextUtils;
import com.shopify.mobilebuysdk.demo.util.ProgressDialogUtils;
import com.shopify.mobilebuysdk.demo.util.ToastUtils;
import com.shopify.mobilebuysdk.demo.util.rx.Transformer;
import com.shopify.mobilebuysdk.demo.util.rx.UnsubscribeLifeCycle;
import com.shopify.mobilebuysdk.demo.widget.MaskedEditText;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import rx.Observable;

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

  @BindView(R.id.input_first_name) EditText vInputFirstName;

  @BindView(R.id.input_last_name) EditText vInputLastName;

  @BindView(R.id.til_cvv_code) TextInputLayout vTilCVVCode;

  @BindView(R.id.til_card_number) TextInputLayout vTilCardNumber;

  @BindView(R.id.til_expires) TextInputLayout vTilExpires;

  @BindView(R.id.til_first_name) TextInputLayout vTilFirstName;

  @BindView(R.id.til_last_name) TextInputLayout vTilLastName;

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
    vBtnNext.setOnClickListener(this::onNextClicked);
  }

  private CreditCard getCreditCardFromInput() throws IllegalArgumentException {
    CreditCard creditCard = new CreditCard();
    String expires = vInputExpires.getUnmaskText();
    if (TextUtils.isEmpty(expires) || expires.length() != 4) {
      throw new IllegalArgumentException("Invalid expires");
    }
    creditCard.setFirstName(EditTextUtils.getText(vInputFirstName, true));
    creditCard.setLastName(EditTextUtils.getText(vInputLastName, true));
    creditCard.setNumber(EditTextUtils.getText(vInputCardNumber, true));
    creditCard.setMonth(expires.substring(0, 1));
    creditCard.setYear(expires.substring(2, 3));
    creditCard.setVerificationValue(EditTextUtils.getText(vInputCVVCode, true));
    return creditCard;
  }

  private void onAndroidPayClicked(View view) {
    ToastUtils.showAndroidPaySetupMessage(getContext());
  }

  private void onNextClicked(View view) {
    manageSubscription(UnsubscribeLifeCycle.DESTROY_VIEW,
        Observable.defer(() -> Observable.concat(
            mShopifyService.setCreditCard(getCreditCardFromInput()),
            mShopifyService.setCheckoutState(CheckoutState.SUMMARY, true))
            .toList().map(voids -> null))
            .compose(ProgressDialogUtils.apply(this, R.string.text_updating_checkout))
            .compose(Transformer.applyIoScheduler())
            .subscribe(o -> {
            }, throwable -> {
              throwable.printStackTrace();
              if (throwable instanceof IllegalArgumentException) {
                ToastUtils.showCheckRequiredFieldsToast(getContext());
              } else {
                ToastUtils.showGenericErrorToast(getContext());
              }
            })
    );
  }
}
