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
