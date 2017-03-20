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

package com.shopify.sample.util;

import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ScrollingView;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

public class FlingBehavior extends AppBarLayout.Behavior {
  private static final int TOP_CHILD_FLING_THRESHOLD = 2;
  private boolean isPositive;

  public FlingBehavior() {
  }

  public FlingBehavior(final Context context, final AttributeSet attrs) {
    super(context, attrs);
  }

  @Override
  public boolean onNestedFling(final CoordinatorLayout coordinatorLayout, final AppBarLayout child, final View target,
    final float velocityX, float velocityY, boolean consumed) {
    if (velocityY > 0 && !isPositive || velocityY < 0 && isPositive) {
      velocityY = velocityY * -1;
    }

    if (target instanceof SwipeRefreshLayout) {
      consumed = consumed((SwipeRefreshLayout) target, consumed);
    } else if (target instanceof RecyclerView) {
      consumed = consumed((RecyclerView) target);
    } else if (target instanceof ScrollingView) {
      consumed = target.getScrollY() < 0;
    }

    return super.onNestedFling(coordinatorLayout, child, target, velocityX, velocityY, consumed);
  }

  @Override
  public void onNestedPreScroll(final CoordinatorLayout coordinatorLayout, final AppBarLayout child, final View target, final int dx,
    final int dy, final int[] consumed) {
    super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed);
    isPositive = dy > 0;
  }

  private boolean consumed(final SwipeRefreshLayout swipeRefreshLayout, final boolean defaultValue) {
    if (swipeRefreshLayout.getChildCount() > 0) {
      if (swipeRefreshLayout.getChildAt(0) instanceof RecyclerView) {
        return consumed((RecyclerView) swipeRefreshLayout.getChildAt(0));
      } else if (swipeRefreshLayout.getChildAt(0) instanceof ScrollingView) {
        return swipeRefreshLayout.getScrollY() < 0;
      }
    }
    return defaultValue;
  }

  private boolean consumed(final RecyclerView target) {
    final View firstChild = target.getChildAt(0);
    final int childAdapterPosition = target.getChildAdapterPosition(firstChild);
    return childAdapterPosition > TOP_CHILD_FLING_THRESHOLD;
  }
}
