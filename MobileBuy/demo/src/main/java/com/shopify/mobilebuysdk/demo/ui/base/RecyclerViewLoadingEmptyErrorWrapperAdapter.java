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

package com.shopify.mobilebuysdk.demo.ui.base;

import com.shopify.mobilebuysdk.demo.R;

import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import me.henrytao.recyclerview.RecyclerViewAdapter;
import me.henrytao.recyclerview.config.Constants;
import me.henrytao.recyclerview.holder.BaseHolder;
import rx.functions.Action0;

/**
 * Created by henrytao on 8/28/16.
 */
public class RecyclerViewLoadingEmptyErrorWrapperAdapter extends RecyclerViewAdapter {

  private static final int HEADER_EMPTY = 1;

  private static final int HEADER_ERROR = 2;

  private static final int HEADER_LOADING = 0;

  private final Action0 mOnRetryAction;

  private CharSequence mEmptyText;

  private CharSequence mErrorText;

  private CharSequence mLoadingText;

  public RecyclerViewLoadingEmptyErrorWrapperAdapter(RecyclerView.Adapter baseAdapter) {
    this(baseAdapter, null);
  }

  public RecyclerViewLoadingEmptyErrorWrapperAdapter(RecyclerView.Adapter baseAdapter, @Nullable Action0 onRetryAction) {
    super(3, 0, baseAdapter);
    mOnRetryAction = onRetryAction;
    hide();
  }

  @Override
  public void onBindFooterViewHolder(RecyclerView.ViewHolder holder, int index) {
    // do nothing
  }

  @Override
  public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder, int index) {
    switch (index) {
      case HEADER_LOADING:
        TextView loadingTitle = (TextView) holder.itemView.findViewById(R.id.title);
        if (mLoadingText != null && loadingTitle != null) {
          loadingTitle.setText(mLoadingText);
        }
        break;
      case HEADER_EMPTY:
        TextView emptyTitle = (TextView) holder.itemView.findViewById(R.id.title);
        if (mEmptyText != null && emptyTitle != null) {
          emptyTitle.setText(mEmptyText);
        }
        break;
      case HEADER_ERROR:
        TextView errorTitle = (TextView) holder.itemView.findViewById(R.id.title);
        if (mErrorText != null && errorTitle != null) {
          errorTitle.setText(mErrorText);
        }
        break;
    }
  }

  @Override
  public RecyclerView.ViewHolder onCreateFooterViewHolder(LayoutInflater inflater, ViewGroup parent, int index) {
    return null;
  }

  @Override
  public RecyclerView.ViewHolder onCreateHeaderViewHolder(LayoutInflater inflater, ViewGroup parent, int index) {
    switch (index) {
      case HEADER_LOADING:
        return new HeaderHolder(inflater, parent, R.layout.view_holder_loading_item);
      case HEADER_EMPTY:
        return new HeaderHolder(inflater, parent, R.layout.view_holder_empty_item);
      case HEADER_ERROR:
        HeaderHolder holder = new HeaderHolder(inflater, parent, R.layout.view_holder_error_item);
        holder.itemView.setOnClickListener(view -> {
          if (mOnRetryAction != null) {
            mOnRetryAction.call();
          }
        });
        return holder;
    }
    return null;
  }

  public void hide() {
    setBaseAdapterEnabled(true);
    setVisibility(HEADER_LOADING, View.GONE, Constants.Type.HEADER);
    setVisibility(HEADER_EMPTY, View.GONE, Constants.Type.HEADER);
    setVisibility(HEADER_ERROR, View.GONE, Constants.Type.HEADER);
  }

  public void setEmptyText(CharSequence emptyText) {
    mEmptyText = emptyText;
    notifyHeaderChanged(HEADER_LOADING);
  }

  public void setErrorText(CharSequence errorText) {
    mErrorText = errorText;
    notifyHeaderChanged(HEADER_ERROR);
  }

  public void setLoadingText(CharSequence loadingText) {
    mLoadingText = loadingText;
    notifyHeaderChanged(HEADER_LOADING);
  }

  public void showEmptyView() {
    setBaseAdapterEnabled(false);
    setVisibility(HEADER_LOADING, View.GONE, Constants.Type.HEADER);
    setVisibility(HEADER_EMPTY, View.VISIBLE, Constants.Type.HEADER);
    setVisibility(HEADER_ERROR, View.GONE, Constants.Type.HEADER);
  }

  public void showErrorView() {
    setBaseAdapterEnabled(false);
    setVisibility(HEADER_LOADING, View.GONE, Constants.Type.HEADER);
    setVisibility(HEADER_EMPTY, View.GONE, Constants.Type.HEADER);
    setVisibility(HEADER_ERROR, View.VISIBLE, Constants.Type.HEADER);
  }

  public void showLoadingView() {
    setBaseAdapterEnabled(false);
    setVisibility(HEADER_LOADING, View.VISIBLE, Constants.Type.HEADER);
    setVisibility(HEADER_EMPTY, View.GONE, Constants.Type.HEADER);
    setVisibility(HEADER_ERROR, View.GONE, Constants.Type.HEADER);
  }

  private static class HeaderHolder extends BaseHolder {

    public HeaderHolder(LayoutInflater inflater, ViewGroup parent, int layoutId) {
      super(inflater, parent, layoutId);
      if (parent != null) {
        int height = parent.getMeasuredHeight();
        height -= parent.getPaddingTop() + parent.getPaddingBottom();
        getItemView().getLayoutParams().height = height;
      }
    }
  }
}
