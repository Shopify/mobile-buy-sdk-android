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

import com.shopify.buy.model.Address;
import com.shopify.buy.model.Checkout;
import com.shopify.mobilebuysdk.demo.R;
import com.shopify.mobilebuysdk.demo.data.CheckoutState;
import com.shopify.mobilebuysdk.demo.ui.base.BaseFragment;
import com.shopify.mobilebuysdk.demo.util.ProgressDialogUtils;
import com.shopify.mobilebuysdk.demo.util.ToastUtils;
import com.shopify.mobilebuysdk.demo.util.rx.Transformer;
import com.shopify.mobilebuysdk.demo.util.rx.UnsubscribeLifeCycle;

import android.os.Bundle;
import android.support.annotation.Nullable;
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
public class AddressFragment extends BaseFragment {

  public static AddressFragment newInstance() {
    return new AddressFragment();
  }

  @BindView(R.id.input_address_1)
  EditText vAddress1;

  @BindView(R.id.input_address_2)
  EditText vAddress2;

  @BindView(R.id.btn_next)
  Button vBtnNext;

  @BindView(R.id.input_city)
  EditText vCity;

  @BindView(R.id.input_country)
  EditText vCountry;

  @BindView(R.id.input_email)
  EditText vEmail;

  @BindView(R.id.input_first_name)
  EditText vFirstName;

  @BindView(R.id.input_last_name)
  EditText vLastName;

  @BindView(R.id.input_postal_code)
  EditText vPostalCode;

  private Unbinder mUnbinder;

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    mUnbinder.unbind();
  }

  @Override
  public View onInflateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_address, container, false);
    mUnbinder = ButterKnife.bind(this, view);
    return view;
  }

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    vBtnNext.setOnClickListener(this::onNextClicked);

    manageSubscription(UnsubscribeLifeCycle.DESTROY_VIEW,
        mShopifyService
            .getCheckout()
            .compose(ProgressDialogUtils.apply(this, R.string.text_creating_checkout))
            .compose(Transformer.applyIoScheduler())
            .subscribe(this::onLoad, throwable -> {
              throwable.printStackTrace();
              ToastUtils.showGenericErrorToast(getContext());
            })
    );
  }

  private String getInputValue(EditText editText, boolean required) throws IllegalArgumentException {
    String value = editText.getText().toString();
    if (TextUtils.isEmpty(value) && required) {
      throw new IllegalArgumentException("Input is required");
    }
    return value;
  }

  private void onLoad(Checkout checkout) {
    if (checkout == null) {
      return;
    }
    vEmail.setText(checkout.getEmail());
    Address address = checkout.getShippingAddress();
    if (address != null) {
      vFirstName.setText(address.getFirstName());
      vLastName.setText(address.getLastName());
      vAddress1.setText(address.getAddress1());
      vAddress2.setText(address.getAddress2());
      vCity.setText(address.getCity());
      vCountry.setText(address.getCountry());
      vPostalCode.setText(address.getZip());
    }
  }

  private void onNextClicked(View view) {
    manageSubscription(UnsubscribeLifeCycle.DESTROY_VIEW,
        Observable.just(null)
            .flatMap(o -> mShopifyService
                .getCheckout()
                .compose(Transformer.applyIoScheduler())
                .flatMap(checkout -> {
                  Address address = new Address();
                  address.setFirstName(getInputValue(vFirstName, true));
                  address.setLastName(getInputValue(vLastName, true));
                  address.setAddress1(getInputValue(vAddress1, true));
                  address.setAddress2(getInputValue(vAddress2, false));
                  address.setCity(getInputValue(vCity, true));
                  address.setCountry(getInputValue(vCountry, true));
                  address.setZip(getInputValue(vPostalCode, true));
                  checkout.setEmail(getInputValue(vEmail, true));
                  checkout.setShippingAddress(address);
                  checkout.setBillingAddress(address);
                  return mShopifyService
                      .updateCheckout(checkout)
                      .compose(Transformer.applyIoScheduler());
                })
                .flatMap(checkout -> mShopifyService
                    .setCheckoutState(CheckoutState.SHIPPING)
                    .compose(Transformer.applyIoScheduler())
                ))
            .compose(ProgressDialogUtils.apply(this, R.string.text_updating_checkout))
            .subscribe(o -> {
            }, throwable -> {
              throwable.printStackTrace();
              if (throwable instanceof IllegalArgumentException) {
                ToastUtils.showCheckRequiredFieldsToast(getContext());
              } else {
                ToastUtils.showGenericErrorToast(getContext());
              }
            }));
  }
}
