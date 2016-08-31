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

package com.shopify.mobilebuysdk.demo.data;

import com.shopify.buy.model.CartLineItem;
import com.shopify.buy.model.ProductVariant;

/**
 * Created by henrytao on 8/30/16.
 */
public class CartItemInfo {

  private ProductVariant mProductVariant;

  private long mQuantity;

  public CartItemInfo(CartLineItem cartLineItem) {
    mProductVariant = cartLineItem.getVariant();
    mQuantity = cartLineItem.getQuantity();
  }

  public ProductVariant getProductVariant() {
    return mProductVariant;
  }

  public long getQuantity() {
    return mQuantity;
  }

  public boolean setQuantity(long quantity) {
    if (quantity != mQuantity) {
      mQuantity = quantity;
      return true;
    }
    return false;
  }
}
