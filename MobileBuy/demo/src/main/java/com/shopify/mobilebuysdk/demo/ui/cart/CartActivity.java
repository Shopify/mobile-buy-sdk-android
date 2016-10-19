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

import com.shopify.buy.model.CartLineItem;
import com.shopify.buy.model.ProductVariant;
import com.shopify.mobilebuysdk.demo.R;
import com.shopify.mobilebuysdk.demo.data.CartItemInfo;
import com.shopify.mobilebuysdk.demo.ui.base.BaseHomeActivity;
import com.shopify.mobilebuysdk.demo.ui.base.DialogHelper;
import com.shopify.mobilebuysdk.demo.ui.base.RecyclerViewLoadingEmptyErrorWrapperAdapter;
import com.shopify.mobilebuysdk.demo.ui.checkout.CheckoutActivity;
import com.shopify.mobilebuysdk.demo.ui.product.ProductActivity;
import com.shopify.mobilebuysdk.demo.util.ExceptionUtils;
import com.shopify.mobilebuysdk.demo.util.NavigationUtils;
import com.shopify.mobilebuysdk.demo.util.ProgressDialogUtils;
import com.shopify.mobilebuysdk.demo.util.ToastUtils;
import com.shopify.mobilebuysdk.demo.util.rx.Transformer;
import com.shopify.mobilebuysdk.demo.util.rx.UnsubscribeLifeCycle;
import com.shopify.mobilebuysdk.demo.widget.BottomBar;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by henrytao on 8/27/16.
 */
public class CartActivity extends BaseHomeActivity implements CartItemViewHolder.OnCartItemAddClickListener,
    CartItemViewHolder.OnCartItemRemoveClickListener, CartItemViewHolder.OnCartItemThumbnailClickListener {

  @BindView(R.id.bottom_bar) BottomBar vBottomBar;

  @BindView(R.id.btn_checkout) Button vBtnCheckout;

  @BindView(R.id.btn_checkout_options) Button vBtnCheckoutOptions;

  @BindView(R.id.recycler_view) RecyclerView vRecyclerView;

  @BindView(R.id.subtotal) TextView vSubtotal;

  @BindView(R.id.toolbar) Toolbar vToolbar;

  private Adapter mAdapter;

  private RecyclerViewLoadingEmptyErrorWrapperAdapter mLoadingEmptyErrorWrapperAdapter;

  @Override
  public void onCartItemAddClick(ProductVariant productVariant) {
    manageSubscription(UnsubscribeLifeCycle.DESTROY_VIEW, mShopifyService
        .addToCart(productVariant)
        .compose(Transformer.applyIoScheduler())
        .subscribe(aVoid -> {
        }, ExceptionUtils::onError));
  }

  @Override
  public void onCartItemRemoveClick(ProductVariant productVariant) {
    manageSubscription(UnsubscribeLifeCycle.DESTROY_VIEW, mShopifyService
        .removeFromCart(productVariant)
        .compose(Transformer.applyIoScheduler())
        .subscribe(aVoid -> {
        }, ExceptionUtils::onError));
  }

  @Override
  public void onCartItemThumbnailClick(View view, ProductVariant productVariant) {
    manageSubscription(UnsubscribeLifeCycle.DESTROY_VIEW,
        mShopifyService
            .getProduct(productVariant.getProductId())
            .compose(ProgressDialogUtils.apply(this, R.string.text_loading))
            .compose(Transformer.applyIoScheduler())
            .subscribe(product -> {
              ProductActivity.startActivityWithAnimation(this, product, view);
            }, throwable -> {
              ExceptionUtils.onError(throwable);
              ToastUtils.showGenericErrorToast(this);
            })
    );
  }

  @Override
  public void onSetContentView(@Nullable Bundle savedInstanceState) {
    setContentView(R.layout.activity_cart);
    ButterKnife.bind(this);
  }

  @Override
  protected int getBottomBarIndex() {
    return INDEX_CART;
  }

  @NonNull
  @Override
  protected BottomBar getBottomBarView() {
    return vBottomBar;
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setSupportActionBar(vToolbar);

    mAdapter = new Adapter(this, this, this);
    mLoadingEmptyErrorWrapperAdapter = new RecyclerViewLoadingEmptyErrorWrapperAdapter(mAdapter);
    mLoadingEmptyErrorWrapperAdapter.setEmptyText(getString(R.string.text_cart_is_empty));
    vRecyclerView.setAdapter(mLoadingEmptyErrorWrapperAdapter);
    vRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

    mLoadingEmptyErrorWrapperAdapter.showEmptyView();
    vBtnCheckout.setEnabled(false);
    vBtnCheckoutOptions.setEnabled(false);

    vBtnCheckout.setOnClickListener(view -> onCheckoutClick());
    vBtnCheckoutOptions.setOnClickListener(view -> onCheckoutOptionsClick());

    manageSubscription(UnsubscribeLifeCycle.DESTROY,
        mShopifyService
            .observeCartChange()
            .compose(Transformer.applyComputationScheduler())
            .subscribe(cart -> {
              mAdapter.addOrSetToZero(cart.getLineItems());
              if (mAdapter.mData.size() > 0) {
                mLoadingEmptyErrorWrapperAdapter.hide();
              } else {
                mLoadingEmptyErrorWrapperAdapter.showEmptyView();
              }
            }, ExceptionUtils::onError),
        mShopifyService
            .observeCartQuantity()
            .compose(Transformer.applyComputationScheduler())
            .subscribe(quantity -> {
              vBtnCheckout.setEnabled(quantity > 0);
              vBtnCheckoutOptions.setEnabled(quantity > 0);
            }, ExceptionUtils::onError),
        mShopifyService
            .observeCartSubtotal()
            .compose(Transformer.applyComputationScheduler())
            .subscribe(subtotal -> {
              DecimalFormat format = new DecimalFormat(getString(R.string.decimal_format));
              vSubtotal.setText(getString(R.string.currency_format, format.format(subtotal)));
            }, ExceptionUtils::onError)
    );
  }

  private void onCheckoutClick() {
    manageSubscription(UnsubscribeLifeCycle.DESTROY_VIEW,
        mShopifyService
            .getCheckout()
            .compose(ProgressDialogUtils.apply(this, R.string.text_creating_checkout))
            .compose(Transformer.applyIoScheduler())
            .subscribe(checkout -> {
              NavigationUtils.startActivity(this, CheckoutActivity.newIntent(this));
            }, throwable -> {
              ExceptionUtils.onError(throwable);
              ToastUtils.showGenericErrorToast(this);
            }));
  }

  private void onCheckoutOptionsClick() {
    manageSubscription(UnsubscribeLifeCycle.DESTROY_VIEW,
        DialogHelper
            .showOtherPaymentMethodsDialog(this)
            .filter(result -> result.action == DialogHelper.Action.POSITIVE)
            .zipWith(mShopifyService
                .getCheckout()
                .compose(ProgressDialogUtils.apply(this, R.string.text_creating_checkout))
                .compose(Transformer.applyIoScheduler()), (result, checkout) -> {
              switch (result.paymentMethod) {
                case WEB:
                  Intent intent = new Intent(Intent.ACTION_VIEW);
                  intent.setData(Uri.parse(checkout.getWebUrl()));
                  NavigationUtils.startActivity(this, intent);
                  break;
                case ANDROID_PAY:
                  // TODO: setup Android Pay
                  break;
              }
              return null;
            })
            .subscribe(o -> {
            }, throwable -> {
              ExceptionUtils.onError(throwable);
              ToastUtils.showGenericErrorToast(this);
            })
    );
  }

  static class Adapter extends RecyclerView.Adapter<CartItemViewHolder> {

    private final List<CartItemInfo> mData;

    private final CartItemViewHolder.OnCartItemThumbnailClickListener mOnCarItemThumbnailClickListener;

    private final CartItemViewHolder.OnCartItemAddClickListener mOnCartItemAddClickListener;

    private final CartItemViewHolder.OnCartItemRemoveClickListener mOnCartItemRemoveClickListener;

    Adapter(CartItemViewHolder.OnCartItemAddClickListener onCartItemAddClickListener,
        CartItemViewHolder.OnCartItemRemoveClickListener onCartItemRemoveClickListener,
        CartItemViewHolder.OnCartItemThumbnailClickListener onCartItemThumbnailClickListener) {
      mOnCartItemAddClickListener = onCartItemAddClickListener;
      mOnCartItemRemoveClickListener = onCartItemRemoveClickListener;
      mOnCarItemThumbnailClickListener = onCartItemThumbnailClickListener;
      mData = new ArrayList<>();
    }

    @Override
    public int getItemCount() {
      return mData.size();
    }

    @Override
    public void onBindViewHolder(CartItemViewHolder holder, int position) {
      holder.bind(mData.get(position));
    }

    @Override
    public CartItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
      return new CartItemViewHolder(parent, mOnCartItemAddClickListener, mOnCartItemRemoveClickListener, mOnCarItemThumbnailClickListener);
    }

    void addOrSetToZero(List<CartLineItem> cartLineItems) {
      // TODO: this is work around to support 0 count in cart items.
      Map<ProductVariant, Boolean> caches = new HashMap<>();
      Map<ProductVariant, Integer> indexes = new HashMap<>();
      int n = mData.size();
      for (int i = 0; i < n; i++) {
        caches.put(mData.get(i).getProductVariant(), false);
        indexes.put(mData.get(i).getProductVariant(), i);
      }
      for (CartLineItem cartLineItem : cartLineItems) {
        ProductVariant productVariant = cartLineItem.getVariant();
        if (caches.containsKey(productVariant)) {
          caches.put(productVariant, true);
          if (mData.get(indexes.get(productVariant)).setQuantity(cartLineItem.getQuantity())) {
            notifyItemChanged(indexes.get(productVariant));
          }
        } else {
          mData.add(new CartItemInfo(cartLineItem));
          notifyItemInserted(mData.size() - 1);
        }
      }
      for (Map.Entry<ProductVariant, Boolean> entry : caches.entrySet()) {
        if (!entry.getValue()) {
          if (mData.get(indexes.get(entry.getKey())).setQuantity(0)) {
            notifyItemChanged(indexes.get(entry.getKey()));
          }
        }
      }
    }
  }
}