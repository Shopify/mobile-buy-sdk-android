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

package com.shopify.sample.domain.model;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Cart {
  private final Map<String, CartItem> cartItems = new LinkedHashMap<>();
  private ReadWriteLock lock = new ReentrantReadWriteLock();

  protected Cart() {
  }

  @Nullable public CartItem findByProductVariantId(final String productVariantId) {
    return cartItems.get(productVariantId);
  }

  @NonNull public List<CartItem> cartItems() {
    lock.readLock().lock();
    try {
      return new ArrayList<>(cartItems.values());
    } finally {
      lock.readLock().unlock();
    }
  }

  public double totalPrice() {
    lock.readLock().lock();
    try {
      double total = 0;
      for (CartItem cartItem : cartItems.values()) {
        total += cartItem.price.doubleValue() * cartItem.quantity;
      }
      return total;
    } finally {
      lock.readLock().unlock();
    }
  }

  public int totalQuantity() {
    lock.readLock().lock();
    try {
      int total = 0;
      for (CartItem cartItem : cartItems.values()) {
        total += cartItem.quantity;
      }
      return total;
    } finally {
      lock.readLock().unlock();
    }
  }

  protected void add(final CartItem cartItem) {
    lock.writeLock().lock();
    try {
      CartItem existing = cartItems.get(cartItem.productVariantId);
      if (existing == null) {
        cartItems.put(cartItem.productVariantId, cartItem);
      } else {
        cartItems.put(cartItem.productVariantId, existing.incrementQuantity(1));
      }
    } finally {
      lock.writeLock().unlock();
    }
  }

  protected void remove(final CartItem cartItem) {
    lock.writeLock().lock();
    try {
      CartItem existing = cartItems.get(cartItem.productVariantId);
      if (existing != null) {
        cartItems.put(existing.productVariantId, existing = existing.decrementQuantity(1));
        if (existing.quantity == 0) {
          cartItems.remove(cartItem.productVariantId);
        }
      }
    } finally {
      lock.writeLock().unlock();
    }
  }

  protected void clear() {
    lock.writeLock().lock();
    try {
      cartItems.clear();
    } finally {
      lock.writeLock().unlock();
    }
  }
}
