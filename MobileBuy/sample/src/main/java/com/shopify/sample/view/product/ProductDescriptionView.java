/*
 *   The MIT License (MIT)
 *
 *   Copyright (c) 2015 Shopify Inc.
 *
 *   Permission is hereby granted, free of charge, to any person obtaining a copy
 *   of this software and associated documentation files (the "Software"), to deal
 *   in the Software without restriction, including without limitation the rights
 *   to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *   copies of the Software, and to permit persons to whom the Software is
 *   furnished to do so, subject to the following conditions:
 *
 *   The above copyright notice and this permission notice shall be included in
 *   all copies or substantial portions of the Software.
 *
 *   THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *   IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *   FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *   AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *   LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *   OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 *   THE SOFTWARE.
 */

package com.shopify.sample.view.product;

import android.content.Context;
import android.support.v4.widget.NestedScrollView;
import android.text.Html;
import android.util.AttributeSet;
import android.widget.TextView;

import com.shopify.sample.R;
import com.shopify.sample.domain.model.ProductDetails;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.shopify.sample.util.Util.mapItems;
import static com.shopify.sample.util.Util.minItem;

public final class ProductDescriptionView extends NestedScrollView {
  static final NumberFormat CURRENCY_FORMAT = NumberFormat.getCurrencyInstance();

  @BindView(R.id.title) TextView titleView;
  @BindView(R.id.price) TextView priceView;
  @BindView(R.id.description) TextView descriptionView;
  private OnAddToCartClickListener onAddToCartClickListener;

  public ProductDescriptionView(final Context context) {
    super(context);
  }

  public ProductDescriptionView(final Context context, final AttributeSet attrs) {
    super(context, attrs);
  }

  public ProductDescriptionView(final Context context, final AttributeSet attrs, final int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  @Override protected void onFinishInflate() {
    super.onFinishInflate();
    ButterKnife.bind(this);
  }

  public void renderProduct(final String title, final double price) {
    titleView.setText(title);
    priceView.setText(getResources().getString(R.string.price_from, CURRENCY_FORMAT.format(price)));
  }

  public void renderProduct(final ProductDetails product) {
    titleView.setText(product.title);
    priceView.setText(getResources().getString(R.string.price_from, formatMinPrice(product)));
    descriptionView.setText(Html.fromHtml(product.description));
  }

  public void setOnAddToCartClickListener(final OnAddToCartClickListener onAddToCartClickListener) {
    this.onAddToCartClickListener = onAddToCartClickListener;
  }

  @OnClick(R.id.price)
  void onAddToCartClick() {
    if (onAddToCartClickListener != null) {
      onAddToCartClickListener.onAddToCartClick();
    }
  }

  private String formatMinPrice(final ProductDetails product) {
    List<BigDecimal> prices = mapItems(product.variants, variant -> variant.price);
    BigDecimal minPrice = minItem(prices, BigDecimal.ZERO, BigDecimal::compareTo);
    return CURRENCY_FORMAT.format(minPrice);
  }

  public interface OnAddToCartClickListener {
    void onAddToCartClick();
  }
}
