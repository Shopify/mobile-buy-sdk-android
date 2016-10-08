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
import com.shopify.buy.model.ShippingRate;
import com.shopify.mobilebuysdk.demo.R;
import com.shopify.mobilebuysdk.demo.util.LayoutInflaterUtils;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by henrytao on 10/7/16.
 */
public class SummaryShippingViewHolder extends RecyclerView.ViewHolder {

  @BindView(R.id.title) TextView vTitle;

  public SummaryShippingViewHolder(ViewGroup parent) {
    super(LayoutInflaterUtils.inflate(parent, R.layout.view_holder_summary_shipping));
    ButterKnife.bind(this, itemView);
  }

  public void bind(Checkout checkout) {
    ShippingRate rate = checkout.getShippingRate();
    vTitle.setText(String.format(Locale.US, "%s - %s", rate.getTitle(), getContext().getString(R.string.currency_format, rate.getPrice())));
  }

  private Context getContext() {
    return itemView.getContext();
  }
}
