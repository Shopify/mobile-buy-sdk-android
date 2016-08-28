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
import com.shopify.mobilebuysdk.demo.ui.base.BaseRecyclerPagerViewHolder;
import com.shopify.mobilebuysdk.demo.ui.base.BaseSubscription;
import com.shopify.mobilebuysdk.demo.ui.base.RecyclerViewEndlessWrapperAdapter;
import com.shopify.mobilebuysdk.demo.ui.product.ProductActivity;
import com.shopify.mobilebuysdk.demo.util.LayoutInflaterUtils;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.Transition;
import android.view.View;
import android.view.ViewGroup;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by henrytao on 8/27/16.
 */
public class ShoppingListViewHolder extends BaseRecyclerPagerViewHolder implements ShoppingListItemViewHolder.OnItemClickListener {

  private final Adapter mAdapter;

  private final RecyclerViewEndlessWrapperAdapter mEndlessWrapperAdapter;

  @BindView(R.id.list) RecyclerView vRecyclerView;

  public ShoppingListViewHolder(BaseSubscription subscription, ViewGroup parent) {
    super(subscription, LayoutInflaterUtils.inflate(parent, R.layout.view_shopping_list));
    ButterKnife.bind(this, itemView);

    mAdapter = new Adapter(this);
    mEndlessWrapperAdapter = new RecyclerViewEndlessWrapperAdapter(mAdapter, null);
    vRecyclerView.setAdapter(mEndlessWrapperAdapter);
    vRecyclerView.setLayoutManager(new LinearLayoutManager(itemView.getContext()));
  }

  @Override
  public void onItemClick(View view, Product data) {
    Intent intent = ProductActivity.newIntent(getActivity());
    Activity activity = getActivity();
    WeakReference<View> viewWeakReference = new WeakReference<View>(view);
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

      getActivity().getWindow().getSharedElementReenterTransition().addListener(new Transition.TransitionListener() {
        @Override
        public void onTransitionCancel(Transition transition) {

        }

        @Override
        public void onTransitionEnd(Transition transition) {
          View v = viewWeakReference.get();
          if (v != null) {
            v.requestLayout();
          }
        }

        @Override
        public void onTransitionPause(Transition transition) {

        }

        @Override
        public void onTransitionResume(Transition transition) {

        }

        @Override
        public void onTransitionStart(Transition transition) {

        }
      });

      ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
          getActivity(), view.findViewById(R.id.thumbnail), getContext().getString(R.string.transition_product_thumbnail));
      activity.startActivity(intent, options.toBundle());
    } else {
      activity.startActivity(intent);
    }
  }

  public void bind(@NonNull List<Product> products) {
    mAdapter.bind(products);
  }

  private static class Adapter extends RecyclerView.Adapter<ShoppingListItemViewHolder> {

    private final ShoppingListItemViewHolder.OnItemClickListener mOnItemClickListener;

    private List<Product> mData = new ArrayList<>();

    public Adapter(ShoppingListItemViewHolder.OnItemClickListener onItemClickListener) {
      mOnItemClickListener = onItemClickListener;
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
      return new ShoppingListItemViewHolder(parent, mOnItemClickListener);
    }

    public void bind(@NonNull List<Product> products) {
      mData = products;
      notifyDataSetChanged();
    }
  }
}
