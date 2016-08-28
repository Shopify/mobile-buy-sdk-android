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

package com.shopify.mobilebuysdk.demo.ui.shopping;

import com.facebook.drawee.view.SimpleDraweeView;
import com.shopify.buy.model.Product;
import com.shopify.mobilebuysdk.demo.R;
import com.shopify.mobilebuysdk.demo.ui.base.BaseViewHolder;
import com.shopify.mobilebuysdk.demo.util.LayoutInflaterUtils;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by henrytao on 8/27/16.
 */
public class ShoppingListItemViewHolder extends BaseViewHolder {

  @BindView(R.id.container) View vContainer;

  @BindView(R.id.price) TextView vPrice;

  @BindView(R.id.thumbnail) SimpleDraweeView vThumbnail;

  @BindView(R.id.title) TextView vTitle;

  private Product mData;

  public ShoppingListItemViewHolder(ViewGroup parent, OnItemClickListener onItemClickListener) {
    super(LayoutInflaterUtils.inflate(parent, R.layout.item_shopping_list));
    ButterKnife.bind(this, itemView);
    vContainer.setOnClickListener(view -> {
      if (onItemClickListener != null && mData != null) {
        onItemClickListener.onItemClick(view, mData);
      }
    });
  }

  public void bind(Product data) {
    mData = data;
    vThumbnail.setImageURI(data.getFirstImageUrl());
    vTitle.setText(data.getTitle());
    // TODO: need to extract to resource
    vPrice.setText(String.format(Locale.US, "$%s", data.getMinimumPrice()));
  }

  public interface OnItemClickListener {

    void onItemClick(View view, Product data);
  }
}
