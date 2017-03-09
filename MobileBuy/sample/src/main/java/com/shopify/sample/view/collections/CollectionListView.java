package com.shopify.sample.view.collections;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.jakewharton.rxbinding2.support.v7.widget.RxRecyclerView;
import com.shopify.sample.R;
import com.shopify.sample.presenter.collections.CollectionListViewPresenter;
import com.shopify.sample.presenter.collections.RealCollectionListViewPresenter;
import com.shopify.sample.presenter.collections.model.Collection;
import com.shopify.sample.view.base.ListItemViewModel;
import com.shopify.sample.view.base.RecyclerViewAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.subjects.PublishSubject;

public final class CollectionListView extends FrameLayout implements CollectionListViewPresenter.View {
  @BindView(R.id.list) RecyclerView listView;
  @BindView(R.id.swipe_refresh_layout) SwipeRefreshLayout swipeRefreshLayoutView;

  private final RecyclerViewAdapter listViewAdapter = new RecyclerViewAdapter();
  private final CollectionListViewPresenter presenter = new RealCollectionListViewPresenter();
  private final PublishSubject<String> refreshSubject = PublishSubject.create();

  public CollectionListView(@NonNull final Context context) {
    super(context);
  }

  public CollectionListView(@NonNull final Context context, @Nullable final AttributeSet attrs) {
    super(context, attrs);
  }

  public CollectionListView(@NonNull final Context context, @Nullable final AttributeSet attrs, @AttrRes final int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  @Override public void showProgress(final long requestId) {
    swipeRefreshLayoutView.setRefreshing(true);
  }

  @Override public void hideProgress(final long requestId) {
    swipeRefreshLayoutView.setRefreshing(false);
  }

  @Override public void showError(final long requestId, final Throwable t) {
  }

  public void refresh() {
    presenter.reset();
    refreshSubject.onNext("");
  }

  @Override protected void onFinishInflate() {
    super.onFinishInflate();
    ButterKnife.bind(this);
    listView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
    listView.setAdapter(listViewAdapter);
    swipeRefreshLayoutView.setOnRefreshListener(this::refresh);
  }

  @Override public Observable<String> nextPageObservable() {
    return RxRecyclerView.scrollStateChanges(listView)
      .filter(event -> event.equals(RecyclerView.SCROLL_STATE_IDLE) && shouldRequestNextPage())
      .map(event -> nextPageCursor())
      .subscribeOn(AndroidSchedulers.mainThread())
      .mergeWith(refreshSubject);
  }

  @Override public void addItems(final List<ListItemViewModel> items) {
    listViewAdapter.addItems(items);
  }

  @Override public void clearItems() {
    listViewAdapter.clearItems();
  }

  @Override protected void onAttachedToWindow() {
    super.onAttachedToWindow();
    if (isInEditMode()) return;
    presenter.attachView(this);
    refresh();
  }

  @Override protected void onDetachedFromWindow() {
    super.onDetachedFromWindow();
    if (isInEditMode()) return;
    presenter.detachView();
  }

  private boolean shouldRequestNextPage() {
    LinearLayoutManager layoutManager = (LinearLayoutManager) listView.getLayoutManager();
    return layoutManager.findLastVisibleItemPosition() > listViewAdapter.getItemCount() - CollectionListViewPresenter.PER_PAGE / 2;
  }

  private String nextPageCursor() {
    ListItemViewModel<Collection> item = listViewAdapter.lastItem();
    if (item != null) {
      return item.getPayload().cursor();
    }
    return "";
  }
}
