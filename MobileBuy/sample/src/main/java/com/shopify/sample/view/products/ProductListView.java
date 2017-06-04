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

package com.shopify.sample.view.products;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LifecycleRegistry;
import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

import com.shopify.sample.R;
import com.shopify.sample.domain.model.Product;
import com.shopify.sample.view.BasePaginatedListViewModel;
import com.shopify.sample.view.ScreenRouter;
import com.shopify.sample.view.base.ListItemViewModel;
import com.shopify.sample.view.base.RecyclerViewAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.shopify.sample.util.Util.checkNotNull;

public final class ProductListView extends SwipeRefreshLayout implements LifecycleOwner, RecyclerViewAdapter.OnItemClickListener {
  @BindView(R.id.list) RecyclerView listView;

  private final LifecycleRegistry lifecycleRegistry = new LifecycleRegistry(this);
  private final RecyclerViewAdapter listViewAdapter = new RecyclerViewAdapter(this);
  private BasePaginatedListViewModel<Product> viewModel;

  public ProductListView(@NonNull final Context context) {
    super(context);
    lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_CREATE);
  }

  public ProductListView(@NonNull final Context context, @Nullable final AttributeSet attrs) {
    super(context, attrs);
    lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_CREATE);
  }

  @Override public Lifecycle getLifecycle() {
    return lifecycleRegistry;
  }

  public void bindViewModel(@NonNull final BasePaginatedListViewModel<Product> viewModel) {
    this.viewModel = checkNotNull(viewModel, "viewModel == null");

    viewModel.reset();
    viewModel.progressLiveData().observe(this, progress -> {
      if (progress != null) {
        setRefreshing(progress.show);
      }
    });
    viewModel.errorErrorCallback().observe(this, error -> {
      if (error != null) {
        showDefaultErrorMessage();
      }
    });
    viewModel.listItemsLiveData().observe(this, this::swapItems);
  }

  @Override protected void onFinishInflate() {
    super.onFinishInflate();
    ButterKnife.bind(this);

    listView.setLayoutManager(new GridLayoutManager(getContext(), 2));
    listView.setHasFixedSize(true);
    listView.setAdapter(listViewAdapter);
    listView.addOnScrollListener(new RecyclerView.OnScrollListener() {
      @Override public void onScrollStateChanged(final RecyclerView recyclerView, final int newState) {
        if (viewModel != null && shouldRequestNextPage(newState)) {
          viewModel.nextPage(nextPageCursor());
        }
      }
    });
    setOnRefreshListener(() -> {
      if (viewModel != null) {
        viewModel.reset();
      }
    });

    int defaultPadding = getResources().getDimensionPixelOffset(R.dimen.default_padding);
    listView.addItemDecoration(new RecyclerView.ItemDecoration() {
      @Override public void getItemOffsets(final Rect outRect, final View view, final RecyclerView parent, final RecyclerView.State state) {
        GridLayoutManager layoutManager = (GridLayoutManager) parent.getLayoutManager();

        int position = parent.getChildAdapterPosition(view);
        if (position == RecyclerView.NO_POSITION) {
          return;
        }
        outRect.left = position % 2 == 0 ? defaultPadding / 2 : defaultPadding / 4;
        outRect.right = position % 2 == 0 ? defaultPadding / 4 : defaultPadding / 2;
        if (position / layoutManager.getSpanCount() > 0) {
          outRect.top = defaultPadding / 4;
        }
        outRect.bottom = defaultPadding / 4;
      }
    });
  }

  @Override public void onItemClick(@NonNull final ListItemViewModel itemViewModel) {
    if (itemViewModel.payload() instanceof Product) {
      ScreenRouter.route(getContext(), new ProductClickActionEvent((Product) itemViewModel.payload()));
    }
  }

  @Override protected void onAttachedToWindow() {
    super.onAttachedToWindow();
    lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_START);
  }

  @Override protected void onDetachedFromWindow() {
    lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_DESTROY);
    super.onDetachedFromWindow();
  }

  private void swapItems(final List<ListItemViewModel> newItems) {
    listViewAdapter.swapItemsAndNotify(newItems);
  }

  private void showDefaultErrorMessage() {
    Snackbar snackbar = Snackbar.make(this, R.string.default_error, Snackbar.LENGTH_LONG);
    snackbar.getView().setBackgroundResource(R.color.snackbar_error_background);
    snackbar.show();
  }

  private boolean shouldRequestNextPage(final int scrollState) {
    GridLayoutManager layoutManager = (GridLayoutManager) listView.getLayoutManager();
    if (scrollState == RecyclerView.SCROLL_STATE_IDLE) {
      return layoutManager.findLastVisibleItemPosition() > listViewAdapter.getItemCount() - 2;
    } else {
      return layoutManager.findLastVisibleItemPosition() >= listViewAdapter.getItemCount() - 2;
    }
  }

  private String nextPageCursor() {
    for (int i = listViewAdapter.getItemCount(); i >= 0; i--) {
      ListItemViewModel item = listViewAdapter.itemAt(i);
      if (item != null) {
        return ((Product) item.payload()).cursor;
      }
    }
    return "";
  }
}
