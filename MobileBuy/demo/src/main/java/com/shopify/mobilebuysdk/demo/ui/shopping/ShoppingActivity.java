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

import com.shopify.mobilebuysdk.demo.R;
import com.shopify.mobilebuysdk.demo.data.Tag;
import com.shopify.mobilebuysdk.demo.service.ShopifyService;
import com.shopify.mobilebuysdk.demo.ui.base.BaseHomeActivity;
import com.shopify.mobilebuysdk.demo.widget.BottomBar;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by henrytao on 8/27/16.
 */
public class ShoppingActivity extends BaseHomeActivity {

  private final ShopifyService mShopifyService;

  @BindView(R.id.bottom_bar) BottomBar vBottomBar;

  @BindView(R.id.tab_layout) TabLayout vTabLayout;

  @BindView(R.id.toolbar) Toolbar vToolbar;

  @BindView(R.id.view_pager) ViewPager vViewPager;

  private ViewPagerAdapter mAdapter;

  public ShoppingActivity() {
    mShopifyService = ShopifyService.getInstance();
  }

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

    mAdapter = new ViewPagerAdapter(getSupportFragmentManager(), Arrays.asList(
        new Tag("new-arrival", "New Arrival"),
        new Tag("chair", "Chairs"),
        new Tag("lounge", "Lounge"),
        new Tag("table", "Table"),
        new Tag("cup", "Cup"),
        new Tag("shoes", "Shoes")
    ));
    vViewPager.setAdapter(mAdapter);

    vTabLayout.setupWithViewPager(vViewPager);
    vTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
    vTabLayout.setTabGravity(TabLayout.GRAVITY_CENTER);
  }

  private static class ViewPagerAdapter extends FragmentStatePagerAdapter {

    private final List<Tag> mTags;

    ViewPagerAdapter(FragmentManager fm, List<Tag> tags) {
      super(fm);
      mTags = tags;
    }

    @Override
    public int getCount() {
      return mTags.size();
    }

    @Override
    public Fragment getItem(int position) {
      return ShoppingListFragment.newInstance(mTags.get(position));
    }

    @Override
    public CharSequence getPageTitle(int position) {
      return mTags.get(position).description;
    }
  }
}
