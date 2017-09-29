package com.shopify.sample.view;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

public final class ViewUtils {

  public static void setOnNextPageListener(@NonNull final RecyclerView list, final int threshold, @NonNull final OnNextPageListener onNextPageListener) {
    list.addOnScrollListener(new RecyclerView.OnScrollListener() {
      @Override
      public void onScrollStateChanged(final RecyclerView recyclerView, final int newState) {
        boolean isLastPageReached = false;
        final RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        final int itemCount = recyclerView.getAdapter().getItemCount();
        if (layoutManager instanceof LinearLayoutManager) {
          isLastPageReached = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition() >= itemCount - threshold;
        }
        if (isLastPageReached) {
          onNextPageListener.onNextPage();
        }
      }
    });
  }

  public interface OnNextPageListener {

    void onNextPage();
  }

  private ViewUtils() {
  }
}
