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

import com.shopify.buy.model.Product;
import com.shopify.mobilebuysdk.demo.R;
import com.shopify.mobilebuysdk.demo.service.ShopifyService;
import com.shopify.mobilebuysdk.demo.ui.base.BaseRecyclerPagerViewHolder;
import com.shopify.mobilebuysdk.demo.ui.base.BaseSubscription;
import com.shopify.mobilebuysdk.demo.ui.base.RecyclerViewEndlessWrapperAdapter;
import com.shopify.mobilebuysdk.demo.ui.base.RecyclerViewLoadingEmptyErrorWrapperAdapter;
import com.shopify.mobilebuysdk.demo.ui.product.ProductActivity;
import com.shopify.mobilebuysdk.demo.util.ExceptionUtils;
import com.shopify.mobilebuysdk.demo.util.LayoutInflaterUtils;
import com.shopify.mobilebuysdk.demo.util.rx.Transformer;
import com.shopify.mobilebuysdk.demo.util.rx.UnsubscribeLifeCycle;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by henrytao on 8/27/16.
 */
public class ShoppingListViewHolder extends BaseRecyclerPagerViewHolder implements ShoppingListItemViewHolder.OnThumbnailClickListener,
    ShoppingListItemViewHolder.OnAddToCartClickListener {

  private final Adapter mAdapter;

  private final RecyclerViewEndlessWrapperAdapter mEndlessWrapperAdapter;

  private final RecyclerViewLoadingEmptyErrorWrapperAdapter mLoadingEmptyErrorWrapperAdapter;

  private final ShopifyService mShopifyService;

  @BindView(R.id.recycler_view) RecyclerView vRecyclerView;

  private String mTag;

  public ShoppingListViewHolder(BaseSubscription subscription, ViewGroup parent) {
    super(subscription, LayoutInflaterUtils.inflate(parent, R.layout.view_holder_shopping_list));
    mShopifyService = ShopifyService.getInstance();
    ButterKnife.bind(this, itemView);

    Context context = itemView.getContext();

    mAdapter = new Adapter(this, this);
    mEndlessWrapperAdapter = new RecyclerViewEndlessWrapperAdapter(mAdapter, null);
    mLoadingEmptyErrorWrapperAdapter = new RecyclerViewLoadingEmptyErrorWrapperAdapter(mEndlessWrapperAdapter, this::onLoadNewData);
    mLoadingEmptyErrorWrapperAdapter.setLoadingText(context.getString(R.string.text_loading_store_items));
    mLoadingEmptyErrorWrapperAdapter.setEmptyText(context.getString(R.string.text_store_is_empty));
    vRecyclerView.setAdapter(mLoadingEmptyErrorWrapperAdapter);
    vRecyclerView.setLayoutManager(new LinearLayoutManager(itemView.getContext()));
  }

  @Override
  public void onAddToCartClick(View view, Product product) {
    manageSubscription(UnsubscribeLifeCycle.DESTROY_VIEW, mShopifyService
        .addToCart(product.getVariants().get(0))
        .compose(Transformer.applyIoScheduler())
        .subscribe(aVoid -> {
        }, ExceptionUtils::onError));
  }

  @Override
  public void onThumbnailClick(View view, Product product) {
    ProductActivity.startActivityWithAnimation(getActivity(), product, view);
  }

  public void bind(String tag, @NonNull List<Product> products) {
    mTag = tag;
    mAdapter.bind(products);
    if (products.size() == 0) {
      onLoadNewData();
    } else {
      // TODO: either do endless query or just show the list
    }
  }

  private void onLoadNewData() {
    mLoadingEmptyErrorWrapperAdapter.showLoadingView();
    manageSubscription(UnsubscribeLifeCycle.DESTROY_VIEW,
        mShopifyService
            .getProducts()
            .compose(Transformer.applyIoScheduler())
            .subscribe(data -> {
              if (data.size() == 0) {
                mLoadingEmptyErrorWrapperAdapter.showEmptyView();
              } else {
                mLoadingEmptyErrorWrapperAdapter.hide();
                mAdapter.add(data);
              }
            }, throwable -> {
              ExceptionUtils.onError(throwable);
              mLoadingEmptyErrorWrapperAdapter.showErrorView();
            })
    );
  }

  private static class Adapter extends RecyclerView.Adapter<ShoppingListItemViewHolder> {

    private final ShoppingListItemViewHolder.OnAddToCartClickListener mOnAddToCartClickListener;

    private final ShoppingListItemViewHolder.OnThumbnailClickListener mOnThumbnailClickListener;

    private List<Product> mData = new ArrayList<>();

    public Adapter(ShoppingListItemViewHolder.OnThumbnailClickListener onThumbnailClickListener,
        ShoppingListItemViewHolder.OnAddToCartClickListener onAddToCartClickListener) {
      mOnThumbnailClickListener = onThumbnailClickListener;
      mOnAddToCartClickListener = onAddToCartClickListener;
    }

    @Override
    public int getItemCount() {
      return mData.size();
    }

    @Override
    public void onBindViewHolder(ShoppingListItemViewHolder holder, int position) {
      holder.bind(mData.get(position));
    }

    @Override
    public ShoppingListItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
      return new ShoppingListItemViewHolder(parent, mOnThumbnailClickListener, mOnAddToCartClickListener);
    }

    public void add(List<Product> products) {
      int count = mData.size();
      mData.addAll(products);
      notifyItemRangeInserted(count, products.size());
    }

    public void bind(@NonNull List<Product> products) {
      mData = products;
      notifyDataSetChanged();
    }
  }
}
