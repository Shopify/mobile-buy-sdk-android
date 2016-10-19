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

import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.view.DraweeTransition;
import com.shopify.buy.model.Product;
import com.shopify.mobilebuysdk.demo.R;
import com.shopify.mobilebuysdk.demo.config.Constants;
import com.shopify.mobilebuysdk.demo.ui.base.BaseHomeActivity;
import com.shopify.mobilebuysdk.demo.ui.base.BaseSubscription;
import com.shopify.mobilebuysdk.demo.widget.BottomBar;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.ViewGroup;
import android.view.Window;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.henrytao.recyclerpageradapter.RecyclerPagerAdapter;

/**
 * Created by henrytao on 8/27/16.
 */
public class ShoppingActivity extends BaseHomeActivity {

  @BindView(R.id.bottom_bar) BottomBar vBottomBar;

  @BindView(R.id.tab_layout) TabLayout vTabLayout;

  @BindView(R.id.toolbar) Toolbar vToolbar;

  @BindView(R.id.view_pager) ViewPager vViewPager;

  private ViewPagerAdapter mAdapter;

  @Override
  public void onSetContentView(@Nullable Bundle savedInstanceState) {
    setContentView(R.layout.activity_shopping);
    ButterKnife.bind(this);
  }

  @Override
  protected int getBottomBarIndex() {
    return INDEX_SHOPPING;
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

    // TODO: need to load category dynamically
    mAdapter = new ViewPagerAdapter(this, Arrays.asList(
        "New Arrivals",
        Constants.Tag.ALL,
        "G-Knit",
        "Rosen",
        "Royale",
        "Wilson",
        "Wooster"
    ));
    vViewPager.setAdapter(mAdapter);

    vTabLayout.setupWithViewPager(vViewPager);
    vTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
    vTabLayout.setTabGravity(TabLayout.GRAVITY_CENTER);
  }

  @TargetApi(Build.VERSION_CODES.LOLLIPOP)
  @Override
  protected void onSharedElementConfig(Window window) {
    super.onSharedElementConfig(window);
    window.requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
    window.setSharedElementExitTransition(
        DraweeTransition.createTransitionSet(ScalingUtils.ScaleType.FIT_CENTER, ScalingUtils.ScaleType.FIT_CENTER));
    window.setSharedElementReenterTransition(
        DraweeTransition.createTransitionSet(ScalingUtils.ScaleType.FIT_CENTER, ScalingUtils.ScaleType.FIT_CENTER));
  }

  private static class ViewPagerAdapter extends RecyclerPagerAdapter<ShoppingListViewHolder> {

    private final Map<String, List<Product>> mCaches = new HashMap<>();

    private final BaseSubscription mSubscription;

    private final List<String> mTags;

    ViewPagerAdapter(BaseSubscription subscription, List<String> tags) {
      mSubscription = subscription;
      mTags = tags;
    }

    @Override
    public int getItemCount() {
      return mTags.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
      return mTags.get(position);
    }

    @Override
    public void onBindViewHolder(ShoppingListViewHolder holder, int position) {
      String tag = mTags.get(position);
      if (!mCaches.containsKey(tag)) {
        mCaches.put(tag, new ArrayList<>());
      }
      holder.bind(tag, mCaches.get(tag));
    }

    @Override
    public ShoppingListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
      return new ShoppingListViewHolder(mSubscription, parent);
    }
  }
}
