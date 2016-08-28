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
import com.shopify.mobilebuysdk.demo.util.LayoutInflaterUtils;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.henrytao.recyclerview.SimpleRecyclerViewAdapter;
import me.henrytao.recyclerview.config.Constants;
import rx.functions.Action0;

/**
 * Created by henrytao on 8/20/16.
 */
public class RecyclerViewEndlessWrapperAdapter extends SimpleRecyclerViewAdapter {

  private final Action0 mOnRetryAction;

  private State mState;

  public RecyclerViewEndlessWrapperAdapter(RecyclerView.Adapter baseAdapter, Action0 onRetryAction) {
    super(baseAdapter);
    mOnRetryAction = onRetryAction;
    setVisibility(0, View.GONE, Constants.Type.FOOTER);
  }

  @Override
  public void onBindFooterViewHolder(RecyclerView.ViewHolder holder, int index) {
    if (holder instanceof EndlessFooterViewHolder) {
      ((EndlessFooterViewHolder) holder).bind(mState);
    } else {
      super.onBindFooterViewHolder(holder, index);
    }
  }

  @Override
  public RecyclerView.ViewHolder onCreateFooterViewHolder(LayoutInflater inflater, ViewGroup parent) {
    return new EndlessFooterViewHolder(parent, mOnRetryAction);
  }

  @Override
  public RecyclerView.ViewHolder onCreateHeaderViewHolder(LayoutInflater inflater, ViewGroup parent) {
    return null;
  }

  public void hide() {
    setVisibility(0, View.GONE, Constants.Type.FOOTER);
  }

  public void showErrorView() {
    mState = State.ERROR;
    setVisibility(0, View.VISIBLE, Constants.Type.FOOTER);
    notifyFooterChanged(0);
  }

  public void showLoadingView() {
    mState = State.LOADING;
    setVisibility(0, View.VISIBLE, Constants.Type.FOOTER);
    notifyFooterChanged(0);
  }

  private enum State {
    LOADING, ERROR
  }

  protected static class EndlessFooterViewHolder extends RecyclerView.ViewHolder {

    private final Action0 mOnRetryAction;

    @BindView(R.id.loading) View vLoading;

    @BindView(R.id.retry) View vRetry;

    protected EndlessFooterViewHolder(ViewGroup parent, Action0 onRetryAction) {
      super(LayoutInflaterUtils.inflate(parent, R.layout.item_endless_footer));
      mOnRetryAction = onRetryAction;
      ButterKnife.bind(this, itemView);
      vRetry.setOnClickListener(view -> {
        if (mOnRetryAction != null) {
          mOnRetryAction.call();
        }
      });
    }

    public void bind(State data) {
      int retryVisibility = View.GONE;
      int loadingVisibility = View.GONE;
      switch (data) {
        case ERROR:
          retryVisibility = View.VISIBLE;
          break;
        case LOADING:
          loadingVisibility = View.VISIBLE;
          break;
      }
      vRetry.setVisibility(retryVisibility);
      vLoading.setVisibility(loadingVisibility);
    }
  }
}
