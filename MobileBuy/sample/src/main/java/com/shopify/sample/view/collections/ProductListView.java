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

import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import com.shopify.sample.R;
import com.shopify.sample.domain.model.Product;
import com.shopify.sample.view.ScreenRouter;
import com.shopify.sample.view.base.ListItemViewModel;
import com.shopify.sample.view.base.RecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public final class ProductListView extends FrameLayout implements RecyclerViewAdapter.OnItemClickListener {
  @BindView(R.id.list) RecyclerView listView;
  private final RecyclerViewAdapter listViewAdapter = new RecyclerViewAdapter(this);

  public ProductListView(@NonNull final Context context) {
    super(context);
  }

  public ProductListView(@NonNull final Context context, @Nullable final AttributeSet attrs) {
    super(context, attrs);
  }

  public ProductListView(@NonNull final Context context, @Nullable final AttributeSet attrs, @AttrRes final int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  public void setItems(@NonNull final List<Product> items) {
    listViewAdapter.clearItems();

    List<ListItemViewModel> viewModels = new ArrayList<>();
    for (Product item : items) {
      viewModels.add(new ProductListItemViewModel(item));
    }
    listViewAdapter.addItems(viewModels);
  }

  @Override public void onItemClick(final ListItemViewModel itemViewModel) {
    ScreenRouter.route(getContext(), new CollectionProductClickActionEvent((Product) itemViewModel.payload()));
  }

  @Override protected void onFinishInflate() {
    super.onFinishInflate();
    ButterKnife.bind(this);

    LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
    layoutManager.setInitialPrefetchItemCount(prefetchItemCount());
    layoutManager.setItemPrefetchEnabled(true);
    listView.setLayoutManager(layoutManager);
    listView.setAdapter(listViewAdapter);

    int defaultPadding = getResources().getDimensionPixelOffset(R.dimen.default_padding);
    listView.addItemDecoration(new RecyclerView.ItemDecoration() {
      @Override public void getItemOffsets(final Rect outRect, final View view, final RecyclerView parent, final RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view);
        if (position == RecyclerView.NO_POSITION) {
          return;
        }
        outRect.left = position == 0 ? defaultPadding / 2 : defaultPadding / 4;
        outRect.right = position == parent.getAdapter().getItemCount() ? defaultPadding / 2 : defaultPadding / 4;
      }
    });
  }

  private int prefetchItemCount() {
    return getResources().getDisplayMetrics().widthPixels
      / getResources().getDimensionPixelOffset(R.dimen.product_thumbnail_size) + 1;
  }
}
