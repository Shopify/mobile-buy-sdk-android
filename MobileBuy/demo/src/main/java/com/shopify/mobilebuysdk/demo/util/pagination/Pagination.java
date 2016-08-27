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

package com.shopify.mobilebuysdk.demo.util.pagination;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.List;

import rx.Observable;

/**
 * Created by henrytao on 8/14/16.
 */
public class Pagination<D, I> {

  public static <D, I> Pagination<D, I> create(@NonNull OnLoadDataListener<D, I> onLoadDataListener) {
    return new Pagination<>(null, onLoadDataListener);
  }

  private final I mPageInfo;

  private Wrapper<D, I> mData;

  private boolean mFinished;

  private OnLoadDataListener<D, I> mOnLoadDataListener;

  protected Pagination(I pageInfo, @NonNull OnLoadDataListener<D, I> onLoadDataListener) {
    mPageInfo = pageInfo;
    mOnLoadDataListener = onLoadDataListener;
  }

  public void finish() {
    mFinished = true;
    mOnLoadDataListener = null;
    mData = null;
  }

  public boolean isFinished() {
    return mFinished;
  }

  public Observable<List<D>> load() {
    return Observable.just(null)
        .flatMap(o -> !mFinished ? mOnLoadDataListener.onLoadData(this, mPageInfo)
            : Observable.error(createOnFinishException()))
        .flatMap(wrapper -> {
          mData = wrapper;
          return Observable.just(mData.data);
        });
  }

  public Pagination<D, I> next() {
    if (mFinished) {
      throw createOnFinishException();
    }
    OnLoadDataListener<D, I> listener = mOnLoadDataListener;
    Wrapper<D, I> data = mData;
    finish();
    return new Pagination<>(data != null ? data.nextPageInfo : null, listener);
  }

  private RuntimeException createOnFinishException() {
    return new IllegalStateException("Pagination is finished");
  }

  public interface OnLoadDataListener<D, I> {

    @NonNull
    Observable<Wrapper<D, I>> onLoadData(Pagination<D, I> pagination, @Nullable I pageInfo);
  }

  public static class Wrapper<D, I> {

    private final List<D> data;

    private final I nextPageInfo;

    public Wrapper(List<D> data, I nextPageInfo) {
      this.data = data;
      this.nextPageInfo = nextPageInfo;
    }
  }
}
