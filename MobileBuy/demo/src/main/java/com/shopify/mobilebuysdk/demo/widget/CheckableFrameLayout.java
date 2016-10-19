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
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Checkable;
import android.widget.FrameLayout;

/**
 * Created by henrytao on 8/27/16.
 */
public class CheckableFrameLayout extends FrameLayout implements Checkable {

  private static void propagateCheckedState(ViewGroup viewGroup) {
    int n = viewGroup.getChildCount();
    boolean isChecked = viewGroup instanceof Checkable && ((Checkable) viewGroup).isChecked();
    for (int i = 0; i < n; i++) {
      View view = viewGroup.getChildAt(i);
      if (view instanceof ViewGroup) {
        propagateCheckedState((ViewGroup) view);
      } else if (view instanceof Checkable) {
        ((Checkable) view).setChecked(isChecked);
      }
    }
  }

  private boolean mChecked;

  public CheckableFrameLayout(Context context) {
    super(context);
    init(context, null);
  }

  public CheckableFrameLayout(Context context, AttributeSet attrs) {
    super(context, attrs);
    init(context, attrs);
  }

  public CheckableFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init(context, attrs);
  }

  @TargetApi(Build.VERSION_CODES.LOLLIPOP)
  public CheckableFrameLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
    super(context, attrs, defStyleAttr, defStyleRes);
    init(context, attrs);
  }

  @Override
  public boolean isChecked() {
    return mChecked;
  }

  @Override
  public void setChecked(boolean checked) {
    mChecked = checked;
    propagateCheckedState(this);
  }

  @Override
  public void toggle() {
    setChecked(!mChecked);
  }

  private void init(Context context, AttributeSet attrs) {
    TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.Checkable, 0, 0);
    boolean checked = false;
    try {
      checked = a.getBoolean(R.styleable.Checkable_checked, false);
    } finally {
      a.recycle();
    }
    setChecked(checked);
  }
}