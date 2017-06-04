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

package com.shopify.sample.view.collections;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LifecycleRegistry;
import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.shopify.sample.R;
import com.shopify.sample.domain.model.Collection;
import com.shopify.sample.view.BasePaginatedListViewModel;
import com.shopify.sample.view.ScreenRouter;
import com.shopify.sample.view.base.ListItemViewModel;
import com.shopify.sample.view.base.RecyclerViewAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.shopify.sample.util.Util.checkNotNull;

public final class CollectionListView extends FrameLayout implements LifecycleOwner, RecyclerViewAdapter.OnItemClickListener {
  @BindView(R.id.list) RecyclerView listView;
  @BindView(R.id.swipe_refresh_layout) SwipeRefreshLayout swipeRefreshLayoutView;

  private final LifecycleRegistry lifecycleRegistry = new LifecycleRegistry(this);
  private final RecyclerViewAdapter listViewAdapter = new RecyclerViewAdapter(this);
  private BasePaginatedListViewModel<Collection> viewModel;

  public CollectionListView(@NonNull final Context context) {
    super(context);
    lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_CREATE);
  }

  public CollectionListView(@NonNull final Context context, @Nullable final AttributeSet attrs) {
    super(context, attrs);
    lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_CREATE);
  }

  public CollectionListView(@NonNull final Context context, @Nullable final AttributeSet attrs, @AttrRes final int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_CREATE);
  }

  @Override public Lifecycle getLifecycle() {
    return lifecycleRegistry;
  }

  public void bindViewModel(@NonNull final BasePaginatedListViewModel<Collection> viewModel) {
    this.viewModel = checkNotNull(viewModel, "viewModel == null");

    viewModel.reset();
    viewModel.progressLiveData().observe(this, progress -> {
      if (progress != null) {
        swipeRefreshLayoutView.setRefreshing(progress.show);
      }
    });
    viewModel.errorErrorCallback().observe(this, error -> {
      if (error != null) {
        showDefaultErrorMessage();
      }
    });
    viewModel.listItemsLiveData().observe(this, this::swapItems);
  }

  @Override public void onItemClick(@NonNull final ListItemViewModel itemViewModel) {
    if (itemViewModel.payload() instanceof Collection) {
      ScreenRouter.route(getContext(), new CollectionClickActionEvent((Collection) itemViewModel.payload()));
    }
  }

  @Override protected void onFinishInflate() {
    super.onFinishInflate();
    ButterKnife.bind(this);
    listView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
    listView.setHasFixedSize(true);
    listView.setAdapter(listViewAdapter);
    listView.addOnScrollListener(new RecyclerView.OnScrollListener() {
      @Override public void onScrollStateChanged(final RecyclerView recyclerView, final int newState) {
        if (viewModel != null && shouldRequestNextPage(newState)) {
          viewModel.nextPage(nextPageCursor());
        }
      }
    });
    swipeRefreshLayoutView.setOnRefreshListener(() -> {
      if (viewModel != null) {
        viewModel.reset();
      }
    });
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
    LinearLayoutManager layoutManager = (LinearLayoutManager) listView.getLayoutManager();
    if (scrollState == RecyclerView.SCROLL_STATE_IDLE) {
      return layoutManager.findLastVisibleItemPosition() > listViewAdapter.getItemCount() - 2;
    } else {
      return layoutManager.findLastVisibleItemPosition() >= listViewAdapter.getItemCount() - 2;
    }
  }

  private String nextPageCursor() {
    for (int i = listViewAdapter.getItemCount(); i >= 0; i--) {
      ListItemViewModel item = listViewAdapter.itemAt(i);
      if (item != null && item.payload() instanceof Collection) {
        return ((Collection) item.payload()).cursor;
      }
    }
    return "";
  }
}
