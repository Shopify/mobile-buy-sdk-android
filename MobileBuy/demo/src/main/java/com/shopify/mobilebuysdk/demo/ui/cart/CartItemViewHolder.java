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

package com.shopify.mobilebuysdk.demo.ui.cart;

import com.facebook.drawee.view.SimpleDraweeView;
import com.shopify.buy.model.ProductVariant;
import com.shopify.mobilebuysdk.demo.R;
import com.shopify.mobilebuysdk.demo.data.CartItemInfo;
import com.shopify.mobilebuysdk.demo.util.LayoutInflaterUtils;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by henrytao on 8/29/16.
 */
public class CartItemViewHolder extends RecyclerView.ViewHolder {

  @BindView(R.id.btn_add) ImageView vBtnAdd;

  @BindView(R.id.btn_remove) ImageView vBtnRemove;

  @BindView(R.id.price) TextView vPrice;

  @BindView(R.id.quantity) TextView vQuantity;

  @BindView(R.id.thumbnail) SimpleDraweeView vThumbnail;

  @BindView(R.id.title) TextView vTitle;

  private CartItemInfo mCartLineItem;

  public CartItemViewHolder(ViewGroup parent, OnCartItemAddClickListener onCartItemAddClickListener,
      OnCartItemRemoveClickListener onCartItemRemoveClickListener, OnCartItemThumbnailClickListener onCartItemThumbnailClickListener) {
    super(LayoutInflaterUtils.inflate(parent, R.layout.view_holder_cart_item));
    ButterKnife.bind(this, itemView);
    vBtnAdd.setOnClickListener(view -> {
      if (onCartItemAddClickListener != null && mCartLineItem != null) {
        onCartItemAddClickListener.onCartItemAddClick(mCartLineItem.getProductVariant());
      }
    });
    vBtnRemove.setOnClickListener(view -> {
      if (onCartItemRemoveClickListener != null && mCartLineItem != null) {
        onCartItemRemoveClickListener.onCartItemRemoveClick(mCartLineItem.getProductVariant());
      }
    });
    vThumbnail.setOnClickListener(view -> {
      if (onCartItemThumbnailClickListener != null && mCartLineItem != null) {
        onCartItemThumbnailClickListener.onCartItemThumbnailClick(view, mCartLineItem.getProductVariant());
      }
    });
  }

  public void bind(CartItemInfo cartItemInfo) {
    mCartLineItem = cartItemInfo;

    ProductVariant productVariant = mCartLineItem.getProductVariant();
    vThumbnail.setImageURI(productVariant.getImageUrl());
    vTitle.setText(productVariant.getProductTitle());
    vPrice.setText(itemView.getContext().getString(R.string.currency_format, productVariant.getPrice()));
    vQuantity.setText(String.valueOf(cartItemInfo.getQuantity()));
  }

  public interface OnCartItemAddClickListener {

    void onCartItemAddClick(ProductVariant productVariant);
  }

  public interface OnCartItemRemoveClickListener {

    void onCartItemRemoveClick(ProductVariant productVariant);
  }

  public interface OnCartItemThumbnailClickListener {

    void onCartItemThumbnailClick(View view, ProductVariant productVariant);
  }
}
