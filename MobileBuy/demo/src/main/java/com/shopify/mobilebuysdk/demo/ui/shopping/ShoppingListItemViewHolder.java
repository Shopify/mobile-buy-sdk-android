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
import com.shopify.mobilebuysdk.demo.util.LayoutInflaterUtils;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by henrytao on 8/27/16.
 */
public class ShoppingListItemViewHolder extends RecyclerView.ViewHolder {

  @BindView(R.id.btn_add_to_cart) Button vBtnAddToCart;

  @BindView(R.id.container) View vContainer;

  @BindView(R.id.price) TextView vPrice;

  @BindView(R.id.thumbnail) SimpleDraweeView vThumbnail;

  @BindView(R.id.title) TextView vTitle;

  private Product mProduct;

  public ShoppingListItemViewHolder(ViewGroup parent, OnThumbnailClickListener onThumbnailClickListener,
      OnAddToCartClickListener onAddToCartClickListener) {
    super(LayoutInflaterUtils.inflate(parent, R.layout.view_holder_shopping_list_item));
    ButterKnife.bind(this, itemView);
    vThumbnail.setOnClickListener(view -> {
      if (onThumbnailClickListener != null && mProduct != null) {
        onThumbnailClickListener.onThumbnailClick(view, mProduct);
      }
    });
    vBtnAddToCart.setOnClickListener(view -> {
      if (onAddToCartClickListener != null && mProduct != null) {
        onAddToCartClickListener.onAddToCartClick(view, mProduct);
      }
    });
  }

  public void bind(Product product) {
    mProduct = product;
    vThumbnail.setImageURI(product.getFirstImageUrl());
    vTitle.setText(product.getTitle());
    vPrice.setText(itemView.getContext().getString(R.string.currency_format, product.getMinimumPrice()));
  }

  public interface OnAddToCartClickListener {

    void onAddToCartClick(View view, Product product);
  }

  public interface OnThumbnailClickListener {

    void onThumbnailClick(View view, Product product);
  }
}
