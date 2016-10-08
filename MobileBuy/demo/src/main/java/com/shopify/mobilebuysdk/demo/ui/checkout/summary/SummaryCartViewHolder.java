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

package com.shopify.mobilebuysdk.demo.ui.checkout.summary;

import com.shopify.buy.model.Checkout;
import com.shopify.buy.model.LineItem;
import com.shopify.mobilebuysdk.demo.R;
import com.shopify.mobilebuysdk.demo.util.LayoutInflaterUtils;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by henrytao on 10/7/16.
 */
public class SummaryCartViewHolder extends RecyclerView.ViewHolder {

  private final Adapter mAdapter;

  @BindView(R.id.list) RecyclerView vRecyclerView;

  public SummaryCartViewHolder(ViewGroup parent) {
    super(LayoutInflaterUtils.inflate(parent, R.layout.view_holder_summary_cart));
    ButterKnife.bind(this, itemView);
    mAdapter = new Adapter();
    vRecyclerView.setNestedScrollingEnabled(false);
    vRecyclerView.setLayoutManager(new LinearLayoutManager(itemView.getContext()));
    vRecyclerView.setAdapter(mAdapter);
  }

  public void bind(Checkout checkout) {
    mAdapter.set(checkout.getLineItems());
  }

  private static class Adapter extends RecyclerView.Adapter<SummaryCartItemViewHolder> {

    private final List<LineItem> mData;

    public Adapter() {
      mData = new ArrayList<>();
    }

    @Override
    public int getItemCount() {
      return mData.size();
    }

    @Override
    public void onBindViewHolder(SummaryCartItemViewHolder holder, int position) {
      holder.bind(mData.get(position));
    }

    @Override
    public SummaryCartItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
      return new SummaryCartItemViewHolder(parent);
    }

    public void set(List<LineItem> lineItems) {
      mData.clear();
      mData.addAll(lineItems);
      notifyDataSetChanged();
    }
  }
}
