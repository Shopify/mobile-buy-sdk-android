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

import com.shopify.buy.model.ShippingRate;
import com.shopify.mobilebuysdk.demo.R;
import com.shopify.mobilebuysdk.demo.data.CheckoutState;
import com.shopify.mobilebuysdk.demo.ui.base.BaseFragment;
import com.shopify.mobilebuysdk.demo.util.LayoutInflaterUtils;
import com.shopify.mobilebuysdk.demo.util.ProgressDialogUtils;
import com.shopify.mobilebuysdk.demo.util.StringUtils;
import com.shopify.mobilebuysdk.demo.util.ToastUtils;
import com.shopify.mobilebuysdk.demo.util.rx.Transformer;
import com.shopify.mobilebuysdk.demo.util.rx.UnsubscribeLifeCycle;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import rx.Observable;

/**
 * Created by henrytao on 9/14/16.
 */
public class ShippingFragment extends BaseFragment {

  public static ShippingFragment newInstance() {
    return new ShippingFragment();
  }

  @BindView(R.id.btn_next) Button vBtnNext;

  @BindView(R.id.list) RecyclerView vRecyclerView;

  private Adapter mAdapter;

  private Unbinder mUnBinder;

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    mUnBinder.unbind();
  }

  @Override
  public View onInflateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_shipping, container, false);
    mUnBinder = ButterKnife.bind(this, view);
    return view;
  }

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    vBtnNext.setOnClickListener(this::onNextClicked);

    mAdapter = new Adapter();
    vRecyclerView.setAdapter(mAdapter);
    vRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

    manageSubscription(UnsubscribeLifeCycle.DESTROY_VIEW,
        Observable.merge(
            mShopifyService
                .getCheckout()
                .compose(Transformer.applyIoScheduler())
                .doOnNext(checkout -> mAdapter.setSelectedShippingRate(checkout.getShippingRate())),
            mShopifyService
                .getShippingRates()
                .compose(Transformer.applyIoScheduler())
                .doOnNext(mAdapter::set))
            .compose(ProgressDialogUtils.apply(this, R.string.text_processing))
            .subscribe(o -> {
            }, throwable -> {
              throwable.printStackTrace();
              ToastUtils.showGenericErrorToast(getContext());
            })
    );
  }

  private void onNextClicked(View view) {
    manageSubscription(UnsubscribeLifeCycle.DESTROY_VIEW,
        Observable.defer(() -> Observable.concat(
            mShopifyService.setShippingRate(mAdapter.getSelectedShippingRate()),
            mShopifyService.setCheckoutState(CheckoutState.PAYMENT)))
            .compose(ProgressDialogUtils.apply(this, R.string.text_updating_checkout))
            .compose(Transformer.applyIoScheduler())
            .subscribe(aVoid -> {
            }, throwable -> {
              throwable.printStackTrace();
              ToastUtils.showGenericErrorToast(getContext());
            })
    );
  }

  interface OnCheckedChangeListener {

    void onCheckChanged(ShippingRate shippingRate);
  }

  static class Adapter extends RecyclerView.Adapter<ShippingItemViewHolder> {

    private final List<ShippingRate> mData;

    private ShippingRate mSelectedShippingRate;

    Adapter() {
      mData = new ArrayList<>();
    }

    @Override
    public int getItemCount() {
      return mData.size();
    }

    @Override
    public void onBindViewHolder(ShippingItemViewHolder holder, int position) {
      ShippingRate shippingRate = mData.get(position);
      holder.bind(shippingRate,
          StringUtils.equals(shippingRate.getId(), mSelectedShippingRate != null ? mSelectedShippingRate.getId() : null));
    }

    @Override
    public ShippingItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
      return new ShippingItemViewHolder(parent, shippingRate -> {
        mSelectedShippingRate = shippingRate;
        int index = mData.indexOf(shippingRate);
        notifyItemRangeChanged(0, index);
        notifyItemRangeChanged(index + 1, getItemCount() - (index + 1));
      });
    }

    public ShippingRate getSelectedShippingRate() {
      return mSelectedShippingRate;
    }

    public void setSelectedShippingRate(ShippingRate shippingRate) {
      mSelectedShippingRate = shippingRate;
      notifyDataSetChanged();
    }

    void set(List<ShippingRate> shippingRates) {
      mData.clear();
      mData.addAll(shippingRates);
      notifyDataSetChanged();
    }
  }

  static class ShippingItemViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.radio_button) RadioButton vRadioButton;

    @BindView(R.id.subtitle) TextView vSubtitle;

    @BindView(R.id.title) TextView vTitle;

    private ShippingRate mShippingRate;

    ShippingItemViewHolder(ViewGroup parent, OnCheckedChangeListener onCheckedChangeListener) {
      super(LayoutInflaterUtils.inflate(parent, R.layout.view_holder_shipping_item));
      ButterKnife.bind(this, itemView);
      itemView.setOnClickListener(view -> {
        vRadioButton.setChecked(true);
        if (onCheckedChangeListener != null) {
          onCheckedChangeListener.onCheckChanged(mShippingRate);
        }
      });
    }

    public void bind(ShippingRate shippingRate, boolean isChecked) {
      mShippingRate = shippingRate;
      vTitle.setText(shippingRate.getTitle());
      vSubtitle.setText(shippingRate.getPrice());
      vRadioButton.setChecked(isChecked);
    }
  }
}
