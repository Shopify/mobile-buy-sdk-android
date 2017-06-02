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

package com.shopify.sample.mvp;

import com.shopify.sample.util.WeakConsumer;
import com.shopify.sample.util.WeakObserver;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;

public abstract class BasePageListViewPresenter<ITEM, VIEW extends PageListViewPresenter.View<ITEM>>
  extends BaseViewPresenter<VIEW> implements PageListViewPresenter<ITEM, VIEW> {
  private boolean resetRequested;

  @Override public void reset() {
    cancelAllRequests();
    resetRequested = true;
    if (isViewAttached()) {
      initPageFetcher(REQUEST_ID_NEXT_PAGE, view().nextPageObservable());
    }
  }

  protected void initPageFetcher(final int requestId, final Observable<String> pageCursorObservable) {
    registerRequest(
      requestId,
      pageCursorObservable
        .distinct()
        .doOnNext(WeakConsumer.<BasePageListViewPresenter, String>forTarget(this)
          .delegateAccept((target, cursor) -> {
            target.showProgress(REQUEST_ID_NEXT_PAGE);
          })
          .create())
        .compose(nextPageRequestComposer())
        .observeOn(AndroidSchedulers.mainThread())
        .doOnError(WeakConsumer.<BasePageListViewPresenter, Throwable>forTarget(this)
          .delegateAccept(BasePageListViewPresenter::onNextPageError)
          .create())
        .retry()
        .subscribeWith(
          WeakObserver.<BasePageListViewPresenter, List<ITEM>>forTarget(this)
            .delegateOnNext(BasePageListViewPresenter::onNextPageResponse)
            .create()
        )
    );
  }

  protected abstract ObservableTransformer<String, List<ITEM>> nextPageRequestComposer();

  protected void onNextPageResponse(List<ITEM> pageList) {
    hideProgress(REQUEST_ID_NEXT_PAGE);
    if (isViewAttached()) {
      if (resetRequested) {
        resetRequested = false;
        view().clearItems();
      }
      view().addItems(pageList);
    }
  }

  protected void onNextPageError(final Throwable t) {
    t.printStackTrace();
    hideProgress(REQUEST_ID_NEXT_PAGE);
    showError(REQUEST_ID_NEXT_PAGE, t);
  }
}
