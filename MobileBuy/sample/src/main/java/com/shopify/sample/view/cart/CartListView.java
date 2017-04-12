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

package com.shopify.sample.view.cart;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.shopify.sample.R;
import com.shopify.sample.domain.model.Cart;
import com.shopify.sample.domain.model.CartItem;
import com.shopify.sample.domain.repository.RealCartRepository;
import com.shopify.sample.mvp.PageListViewPresenter;
import com.shopify.sample.presenter.cart.CartLisViewPresenter;
import com.shopify.sample.view.base.ListItemViewModel;
import com.shopify.sample.view.base.RecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;

public final class CartListView extends FrameLayout implements PageListViewPresenter.View<CartItem>,
  CartListItemViewModel.OnChangeQuantityClickListener {
  @BindView(R.id.list) RecyclerView listView;
  private final RecyclerViewAdapter listViewAdapter = new RecyclerViewAdapter();
  private final CartLisViewPresenter presenter = new CartLisViewPresenter(new RealCartRepository());
  private final RecyclerViewAdapter.ItemComparator itemComparator = new RecyclerViewAdapter.ItemComparator() {
    @Override public boolean equalsById(final ListItemViewModel oldItem, final ListItemViewModel newItem) {
      if (oldItem.payload() instanceof CartItem && newItem.payload() instanceof CartItem) {
        return oldItem.equals(newItem);
      } else {
        return oldItem.payload() instanceof Cart && newItem.payload() instanceof Cart;
      }
    }

    @Override public boolean equalsByContent(final ListItemViewModel oldItem, final ListItemViewModel newItem) {
      if (oldItem.payload() instanceof CartItem && newItem.payload() instanceof CartItem) {
        CartItem oldCartItem = (CartItem) oldItem.payload();
        CartItem newCartItem = (CartItem) newItem.payload();
        return oldCartItem.quantity == newCartItem.quantity
          && oldCartItem.productTitle.equals(newCartItem.productTitle)
          && oldCartItem.variantTitle.equals(newCartItem.variantTitle)
          && oldCartItem.price.equals(newCartItem.price)
          && oldCartItem.options.equals(newCartItem.options)
          && (oldCartItem.image != null ? oldCartItem.image.equals(newCartItem.image) : newCartItem.image == null);
      } else {
        return false;
      }
    }
  };

  public CartListView(final Context context) {
    super(context);
  }

  public CartListView(final Context context, final AttributeSet attrs) {
    super(context, attrs);
  }

  @Override public void showProgress(final int requestId) {
  }

  @Override public void hideProgress(final int requestId) {
  }

  @Override public void showError(final long requestId, final Throwable t) {
    Snackbar.make(this, R.string.default_error, Snackbar.LENGTH_LONG).show();
  }

  @Override public Observable<String> nextPageObservable() {
    return presenter.cartObservable().map(cart -> String.valueOf(System.currentTimeMillis()));
  }

  @Override public void addItems(final List<CartItem> cartItems) {
    List<ListItemViewModel> viewModels = new ArrayList<>();
    for (CartItem item : cartItems) {
      viewModels.add(new CartListItemViewModel(item, this));
    }
    if (!cartItems.isEmpty()) {
      viewModels.add(new CartSubtotalListItemViewModel(presenter.cart()));
    }
    listViewAdapter.swapItemsAndNotify(viewModels, itemComparator);
  }

  @Override public void clearItems() {
  }

  public void refresh() {
    presenter.reset();
  }

  @Override public void onAddCartItemClick(final CartItem cartItem) {
    presenter.addCartItem(cartItem);
  }

  @Override public void onRemoveCartItemClick(final CartItem cartItem) {
    presenter.removeCartItem(cartItem);
  }

  @Override protected void onFinishInflate() {
    super.onFinishInflate();
    ButterKnife.bind(this);

    listView.setHasFixedSize(true);
    listView.setAdapter(listViewAdapter);
    ((SimpleItemAnimator) listView.getItemAnimator()).setSupportsChangeAnimations(false);
  }

  @Override protected void onAttachedToWindow() {
    super.onAttachedToWindow();
    if (isInEditMode()) return;
    presenter.attachView(this);
    refresh();
  }

  @Override protected void onDetachedFromWindow() {
    super.onDetachedFromWindow();
    if (isInEditMode()) return;
    presenter.detachView();
  }
}
