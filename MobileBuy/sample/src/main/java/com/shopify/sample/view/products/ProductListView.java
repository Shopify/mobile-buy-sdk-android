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

import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

import com.shopify.sample.R;
import com.shopify.sample.domain.model.Product;
import com.shopify.sample.view.Constant;
import com.shopify.sample.view.ScreenRouter;
import com.shopify.sample.view.ViewUtils;
import com.shopify.sample.view.base.LifecycleSwipeRefreshLayout;
import com.shopify.sample.view.base.ListItemViewModel;
import com.shopify.sample.view.base.RecyclerViewAdapter;
import com.shopify.sample.view.collections.CollectionListViewModel;

import butterknife.BindView;
import butterknife.ButterKnife;

public final class ProductListView extends LifecycleSwipeRefreshLayout implements RecyclerViewAdapter.OnItemClickListener, ViewUtils.OnNextPageListener, SwipeRefreshLayout.OnRefreshListener {

  @BindView(R.id.list) RecyclerView listView;

  private RecyclerViewAdapter adapter;
  private ProductListViewModel viewModel;

  public ProductListView(final Context context) {
    super(context);
  }

  public ProductListView(final Context context, final AttributeSet attrs) {
    super(context, attrs);
  }

  @Override
  public void onItemClick(@NonNull final ListItemViewModel itemViewModel) {
    if (itemViewModel.payload() instanceof Product) {
      ScreenRouter.route(getContext(), new ProductClickActionEvent((Product) itemViewModel.payload()));
    }
  }

  @Override
  public void onNextPage() {
    viewModel.fetchData();
  }

  @Override
  public void onRefresh() {
    viewModel.reset();
    viewModel.fetchData();
  }

  public void bindViewModel(@NonNull final ProductListViewModel viewModel) {
    this.viewModel = viewModel;
    viewModel.fetchDataIfNecessary();
    viewModel
      .state()
      .observe(this, state -> setRefreshing(state == CollectionListViewModel.State.FETCHING));
    viewModel
      .error()
      .observe(this, error -> {
        Snackbar snackbar = Snackbar.make(this, R.string.default_error, Snackbar.LENGTH_LONG);
        snackbar.getView().setBackgroundResource(R.color.snackbar_error_background);
        snackbar.show();
      });
    viewModel
      .items()
      .observe(this, adapter::swapItemsAndNotify);
  }

  @Override
  protected void onFinishInflate() {
    super.onFinishInflate();
    ButterKnife.bind(this);

    adapter = new RecyclerViewAdapter(this);
    listView.setLayoutManager(new GridLayoutManager(getContext(), 2));
    listView.setHasFixedSize(true);
    listView.setAdapter(adapter);

    int defaultPadding = getResources().getDimensionPixelOffset(R.dimen.default_padding);
    listView.addItemDecoration(new RecyclerView.ItemDecoration() {
      @Override
      public void getItemOffsets(final Rect outRect, final View view, final RecyclerView parent, final RecyclerView.State state) {
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

    ViewUtils.setOnNextPageListener(listView, Constant.THRESHOLD, this);
    setOnRefreshListener(this);
  }
}
