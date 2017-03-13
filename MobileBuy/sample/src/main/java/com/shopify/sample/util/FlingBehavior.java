package com.shopify.sample.util;

import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
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
    if (swipeRefreshLayout.getChildCount() > 0 && swipeRefreshLayout.getChildAt(0) instanceof RecyclerView) {
      return consumed((RecyclerView) swipeRefreshLayout.getChildAt(0));
    }
    return defaultValue;
  }

  private boolean consumed(final RecyclerView target) {
    final View firstChild = target.getChildAt(0);
    final int childAdapterPosition = target.getChildAdapterPosition(firstChild);
    return childAdapterPosition > TOP_CHILD_FLING_THRESHOLD;
  }
}
