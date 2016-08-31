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

package com.shopify.mobilebuysdk.demo.widget;

import com.shopify.mobilebuysdk.demo.R;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import me.henrytao.mdcore.core.MdVectorDrawableCompat;

/**
 * Created by henrytao on 8/27/16.
 */
public class BottomBar extends LinearLayout {

  private OnItemEnabledListener mOnItemEnabledListener;

  public BottomBar(Context context) {
    super(context, null);
  }

  public BottomBar(Context context, AttributeSet attrs) {
    this(context, attrs, R.attr.BottomBarStyle);
  }

  public BottomBar(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr > 0 ? defStyleAttr : R.attr.BottomBarStyle);
    initialize(context);
  }

  @TargetApi(Build.VERSION_CODES.LOLLIPOP)
  public BottomBar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
    super(context, attrs, defStyleAttr > 0 ? defStyleAttr : R.attr.BottomBarStyle, defStyleRes);
    initialize(context);
  }

  public void addView(@DrawableRes int icon, @StringRes int title) {
    CheckableFrameLayout view = (CheckableFrameLayout) LayoutInflater.from(getContext())
        .inflate(R.layout.widget_bottom_bar_item, this, false);
    ((ImageView) view.findViewById(R.id.icon)).setImageDrawable(MdVectorDrawableCompat.create(getContext(), icon));
    ((TextView) view.findViewById(R.id.title)).setText(title);
    view.setTag(R.id.item, getChildCount());
    view.setOnClickListener(v -> setItemEnabled((int) v.getTag(R.id.item)));
    addView(view);
  }

  @Nullable
  public ViewGroup getItemView(int index) {
    if (index < 0 && index >= getChildCount()) {
      return null;
    }
    View child = getChildAt(index);
    return child instanceof ViewGroup ? (ViewGroup) child : null;
  }

  public void setBadge(int index, CharSequence value) {
    ViewGroup item = getItemView(index);
    if (item == null) {
      return;
    }
    TextView badge = (TextView) item.findViewById(R.id.badge);
    badge.setVisibility(TextUtils.isEmpty(value) ? GONE : VISIBLE);
    badge.setText(!TextUtils.isEmpty(value) ? value : "");
  }

  public void setItemEnabled(int index) {
    int n = getChildCount();
    for (int i = 0; i < n; i++) {
      View view = getChildAt(i);
      if (view instanceof CheckableFrameLayout) {
        ((CheckableFrameLayout) view).setChecked(i == index);
      }
    }
    if (index >= 0 && index < n && mOnItemEnabledListener != null) {
      mOnItemEnabledListener.onItemEnabled(getChildAt(index), index);
    }
  }

  public void setOnItemEnabledListener(OnItemEnabledListener onItemEnabledListener) {
    mOnItemEnabledListener = onItemEnabledListener;
  }

  private void initialize(Context context) {
  }

  public interface OnItemEnabledListener {

    void onItemEnabled(View view, int index);
  }
}