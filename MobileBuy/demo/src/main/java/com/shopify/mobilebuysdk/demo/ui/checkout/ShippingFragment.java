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
import com.shopify.mobilebuysdk.demo.ui.base.BaseFragment;
import com.shopify.mobilebuysdk.demo.util.LayoutInflaterUtils;
import com.shopify.mobilebuysdk.demo.util.ProgressDialogUtils;
import com.shopify.mobilebuysdk.demo.util.ToastUtils;
import com.shopify.mobilebuysdk.demo.util.rx.UnsubscribeLifeCycle;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by henrytao on 9/14/16.
 */
public class ShippingFragment extends BaseFragment {

  public static ShippingFragment newInstance() {
    return new ShippingFragment();
  }

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

    mAdapter = new Adapter();
    vRecyclerView.setAdapter(mAdapter);
    vRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

    manageSubscription(UnsubscribeLifeCycle.DESTROY_VIEW,
        mShopifyService
            .getShippingRates()
            .compose(ProgressDialogUtils.apply(this, R.string.text_processing))
            .subscribe(mAdapter::set, throwable -> {
              throwable.printStackTrace();
              ToastUtils.showGenericErrorToast(getContext());
            })
    );
  }

  static class Adapter extends RecyclerView.Adapter<ShippingItemViewHolder> {

    private final List<ShippingRate> mData;

    Adapter() {
      mData = new ArrayList<>();
    }

    @Override
    public int getItemCount() {
      return mData.size();
    }

    @Override
    public void onBindViewHolder(ShippingItemViewHolder holder, int position) {
      holder.bind(mData.get(position));
    }

    @Override
    public ShippingItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
      return new ShippingItemViewHolder(parent);
    }

    void set(List<ShippingRate> shippingRates) {
      mData.clear();
      mData.addAll(shippingRates);
      notifyDataSetChanged();
    }
  }

  static class ShippingItemViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.checkbox) CheckBox vCheckbox;

    @BindView(R.id.subtitle) TextView vSubtitle;

    @BindView(R.id.title) TextView vTitle;

    ShippingItemViewHolder(ViewGroup parent) {
      super(LayoutInflaterUtils.inflate(parent, R.layout.view_holder_shipping_item));
      ButterKnife.bind(this, itemView);
    }

    public void bind(ShippingRate shippingRate) {
      vTitle.setText(shippingRate.getTitle());
      vSubtitle.setText(shippingRate.getPrice());
    }
  }
}
