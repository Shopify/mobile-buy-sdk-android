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
import com.shopify.mobilebuysdk.demo.ui.base.BlankViewHolder;
import com.shopify.mobilebuysdk.demo.ui.checkout.summary.SummaryAddressViewHolder;
import com.shopify.mobilebuysdk.demo.ui.checkout.summary.SummaryCartViewHolder;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by henrytao on 9/15/16.
 */
public class SummaryFragment extends BaseFragment {

  public static SummaryFragment newInstance() {
    return new SummaryFragment();
  }

  @BindView(R.id.btn_purchase) Button vBtnPurchase;

  @BindView(R.id.list) RecyclerView vRecyclerView;

  private Unbinder mUnbinder;

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    mUnbinder.unbind();
  }

  @Override
  public View onInflateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_summary, container, false);
    mUnbinder = ButterKnife.bind(this, view);
    return view;
  }

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    vRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    vRecyclerView.setAdapter(new Adapter());

    vBtnPurchase.setOnClickListener(this::onPurchaseClicked);
  }

  private void onPurchaseClicked(View view) {

  }

  private static class Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int ADDRESS_INDEX = 1;

    private static final int CART_INDEX = 0;

    private static final int COUNT = 4;

    private static final int PAYMENT_INDEX = 3;

    private static final int SHIPPING_INDEX = 2;

    @Override
    public int getItemCount() {
      return COUNT;
    }

    @Override
    public int getItemViewType(int position) {
      return position;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
      switch (viewType) {
        case CART_INDEX:
          return new SummaryCartViewHolder(parent);
        case ADDRESS_INDEX:
          return new SummaryAddressViewHolder(parent);
        case SHIPPING_INDEX:
          break;
        case PAYMENT_INDEX:
          break;
      }
      return new BlankViewHolder(parent);
    }
  }
}
