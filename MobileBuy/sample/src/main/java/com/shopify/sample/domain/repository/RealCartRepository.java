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

package com.shopify.sample.domain.repository;

import com.jakewharton.rxrelay2.BehaviorRelay;
import com.shopify.sample.domain.model.Cart;
import com.shopify.sample.domain.model.CartItem;

import io.reactivex.Observable;

public final class RealCartRepository implements CartRepository {
  @Override public Cart cart() {
    return CartManager.INSTANCE.cart();
  }

  @Override public void addCartItem(final CartItem cartItem) {
    CartManager.INSTANCE.addCartItem(cartItem);
  }

  @Override public void removeCartItem(final CartItem cartItem) {
    CartManager.INSTANCE.removeCartItem(cartItem);
  }

  @Override public Observable<Cart> watch() {
    return CartManager.INSTANCE.cartObservable();
  }

  @Override public void clear() {
    CartManager.INSTANCE.clear();
  }

  private static final class CartManager {
    static final CartManager INSTANCE = new CartManager();
    final RealCart cart = new RealCart();
    final BehaviorRelay<Cart> updateCartSubject = BehaviorRelay.create();

    Cart cart() {
      return cart;
    }

    void addCartItem(final CartItem cartItem) {
      cart.add(cartItem);
      updateCartSubject.accept(cart);
    }

    void removeCartItem(final CartItem cartItem) {
      cart.remove(cartItem);
      updateCartSubject.accept(cart);
    }

    void clear() {
      cart.clear();
      updateCartSubject.accept(cart);
    }

    Observable<Cart> cartObservable() {
      return updateCartSubject;
    }
  }

  private static final class RealCart extends Cart {
    @Override protected void add(final CartItem cartItem) {
      super.add(cartItem);
    }

    @Override protected void remove(final CartItem cartItem) {
      super.remove(cartItem);
    }

    @Override protected void clear() {
      super.clear();
    }
  }
}
