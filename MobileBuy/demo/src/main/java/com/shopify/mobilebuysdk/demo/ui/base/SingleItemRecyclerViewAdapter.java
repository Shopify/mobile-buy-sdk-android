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

package com.shopify.mobilebuysdk.demo.ui.base;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by henrytao on 10/2/16.
 */
public abstract class SingleItemRecyclerViewAdapter<VH extends RecyclerView.ViewHolder, D> extends RecyclerView.Adapter<VH> {

  public abstract void onBindViewHolder(VH holder);

  public abstract VH onCreateViewHolder(ViewGroup parent);

  private final List<D> mData;

  public SingleItemRecyclerViewAdapter() {
    mData = new ArrayList<>();
  }

  @Override
  public int getItemCount() {
    return mData.size();
  }

  @Override
  public void onBindViewHolder(VH holder, int position) {
    onBindViewHolder(holder);
  }

  @Override
  public VH onCreateViewHolder(ViewGroup parent, int viewType) {
    return onCreateViewHolder(parent);
  }

  public void clear() {
    mData.clear();
    notifyItemRemoved(0);
  }

  public void setData(D data) {
    if (mData.size() == 0) {
      mData.add(data);
      notifyItemInserted(0);
    } else {
      mData.clear();
      mData.add(data);
      notifyItemChanged(0);
    }
  }
}
