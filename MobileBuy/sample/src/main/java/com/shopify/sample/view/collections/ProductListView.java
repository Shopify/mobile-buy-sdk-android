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
import com.shopify.sample.presenter.collections.Collection;
import com.shopify.sample.view.base.ListItemViewModel;
import com.shopify.sample.view.base.RecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public final class ProductListView extends FrameLayout {
  @BindView(R.id.list) RecyclerView listView;
  private final RecyclerViewAdapter listViewAdapter = new RecyclerViewAdapter();

  public ProductListView(@NonNull final Context context) {
    super(context);
  }

  public ProductListView(@NonNull final Context context, @Nullable final AttributeSet attrs) {
    super(context, attrs);
  }

  public ProductListView(@NonNull final Context context, @Nullable final AttributeSet attrs, @AttrRes final int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  @Override protected void onFinishInflate() {
    super.onFinishInflate();
    ButterKnife.bind(this);

    LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
    layoutManager.setInitialPrefetchItemCount(prefetchItemCount());
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

  public void setItems(@NonNull final List<Collection.Product> items) {
    listViewAdapter.clearItems();

    List<ListItemViewModel> viewModels = new ArrayList<>();
    for (Collection.Product item : items) {
      viewModels.add(new ProductListItemViewModel(item));
    }
    listViewAdapter.addItems(viewModels);
  }

  private int prefetchItemCount() {
    return getResources().getDisplayMetrics().widthPixels
      / getResources().getDimensionPixelOffset(R.dimen.collection_list_product_image_height) + 1;
  }
}
