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

package com.shopify.mobilebuysdk.demo.ui.base;

import com.shopify.mobilebuysdk.demo.R;
import com.shopify.mobilebuysdk.demo.ui.cart.CartActivity;
import com.shopify.mobilebuysdk.demo.ui.shopping.ShoppingActivity;
import com.shopify.mobilebuysdk.demo.util.NavigationUtils;
import com.shopify.mobilebuysdk.demo.util.rx.Transformer;
import com.shopify.mobilebuysdk.demo.util.rx.UnsubscribeLifeCycle;
import com.shopify.mobilebuysdk.demo.widget.BottomBar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.DrawableRes;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by henrytao on 8/27/16.
 */
public abstract class BaseHomeActivity extends BaseActivity {

  @Index
  protected abstract int getBottomBarIndex();

  @NonNull
  protected abstract BottomBar getBottomBarView();

  public static final int INDEX_CART = 1;

  public static final int INDEX_SHOPPING = 0;

  private static final Map<Integer, Class<? extends BaseHomeActivity>> sActivities = new HashMap<>();

  private static final List<BottomBarItem> sBottomBarItems = Arrays.asList(
      new BottomBarItem(INDEX_SHOPPING, "chat", R.drawable.ic_apps_background, R.string.text_shopping),
      new BottomBarItem(INDEX_CART, "contact", R.drawable.ic_shopping_cart_background, R.string.text_cart)
  );

  private static int DEFAULT_INDEX = INDEX_SHOPPING;

  static {
    sActivities.put(INDEX_SHOPPING, ShoppingActivity.class);
    sActivities.put(INDEX_CART, CartActivity.class);
  }

  public static Intent newIntent(Activity activity) {
    return newIntent(activity, DEFAULT_INDEX);
  }

  public static Intent newIntent(Activity activity, @Index int index) {
    DEFAULT_INDEX = index;
    return new Intent(activity, sActivities.get(index));
  }

  private int mCurrentIndex;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    for (BottomBarItem item : sBottomBarItems) {
      getBottomBarView().addView(item.icon, item.title);
    }
    getBottomBarView().setOnItemEnabledListener((view, index) -> onBottomBarItemChanged(sBottomBarItems.get(index)));
    setBottomBarItemEnabled(getBottomBarIndex());

    manageSubscription(UnsubscribeLifeCycle.DESTROY,
        mShopifyService
            .observeCartQuantity()
            .compose(Transformer.applyComputationScheduler())
            .subscribe(quantity -> getBottomBarView().setBadge(INDEX_CART, quantity > 0 ? String.valueOf(quantity) : null),
                Throwable::printStackTrace)
    );
  }

  private void onBottomBarItemChanged(BottomBarItem bottomBarItem) {
    if (bottomBarItem.index == mCurrentIndex) {
      return;
    }
    NavigationUtils.startActivityAndFinishWithoutAnimation(this, newIntent(this, bottomBarItem.index));
  }

  private void setBottomBarItemEnabled(@Index int index) {
    mCurrentIndex = index;
    getBottomBarView().setItemEnabled(index);
  }

  @IntDef({INDEX_SHOPPING, INDEX_CART})
  @Retention(RetentionPolicy.SOURCE)
  protected @interface Index {

  }

  private static class BottomBarItem implements Parcelable {

    public static final Parcelable.Creator<BottomBarItem> CREATOR = new Parcelable.Creator<BottomBarItem>() {
      @Override
      public BottomBarItem createFromParcel(Parcel source) {
        return new BottomBarItem(source);
      }

      @Override
      public BottomBarItem[] newArray(int size) {
        return new BottomBarItem[size];
      }
    };

    @DrawableRes final private int icon;

    final private int index;

    final private String tag;

    @StringRes final private int title;

    public BottomBarItem(int index, String tag, int icon, int title) {
      this.index = index;
      this.tag = tag;
      this.icon = icon;
      this.title = title;
    }

    protected BottomBarItem(Parcel in) {
      this.icon = in.readInt();
      this.index = in.readInt();
      this.tag = in.readString();
      this.title = in.readInt();
    }

    @Override
    public int describeContents() {
      return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
      dest.writeInt(this.icon);
      dest.writeInt(this.index);
      dest.writeString(this.tag);
      dest.writeInt(this.title);
    }
  }
}
