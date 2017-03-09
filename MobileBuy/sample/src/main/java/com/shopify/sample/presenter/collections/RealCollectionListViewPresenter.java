package com.shopify.sample.presenter.collections;

import com.shopify.sample.mvp.BaseViewPresenter;
import com.shopify.sample.presenter.collections.model.Collection;
import com.shopify.sample.repository.CatalogRepository;
import com.shopify.sample.repository.RealCatalogRepository;
import com.shopify.sample.util.WeakConsumer;
import com.shopify.sample.util.WeakObserver;
import com.shopify.sample.view.base.ListItemViewModel;
import com.shopify.sample.view.collections.CollectionImageListItemViewModel;
import com.shopify.sample.view.collections.CollectionTitleListItemViewModel;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public final class RealCollectionListViewPresenter extends BaseViewPresenter<CollectionListViewPresenter.View>
  implements CollectionListViewPresenter {
  private final CatalogRepository catalogRepository;
  private boolean resetRequested;

  public RealCollectionListViewPresenter() {
    this.catalogRepository = new RealCatalogRepository();
  }

  @Override public void reset() {
    cancelAllRequests();
    resetRequested = true;
    if (isViewAttached()) {
      initPageFetcher(REQUEST_ID_NEXT_PAGE, view().nextPageObservable());
    }
  }

  private void initPageFetcher(final int requestId, final Observable<String> pageCursorObservable) {
    registerRequest(
      requestId,
      pageCursorObservable
        .distinct()
        .doOnNext(WeakConsumer.<RealCollectionListViewPresenter, String>forTarget(this)
          .delegateAccept((target, cursor) -> {
            target.showProgress(REQUEST_ID_NEXT_PAGE);
          })
          .create())
        .compose(new NextPageRequestComposer(catalogRepository))
        .retry()
        .observeOn(AndroidSchedulers.mainThread())
        .doOnError(WeakConsumer.<RealCollectionListViewPresenter, Throwable>forTarget(this)
          .delegateAccept(RealCollectionListViewPresenter::onNextPageError)
          .create())
        .subscribeWith(
          WeakObserver.<RealCollectionListViewPresenter, List<ListItemViewModel>>forTarget(this)
            .delegateOnNext(RealCollectionListViewPresenter::onNextPageResponse)
            .create()
        )
    );
  }

  private void onNextPageResponse(List<ListItemViewModel> pageList) {
    hideProgress(REQUEST_ID_NEXT_PAGE);
    if (isViewAttached()) {
      if (resetRequested) {
        resetRequested = false;
        view().clearItems();
      }
      view().addItems(pageList);
    }
  }

  private void onNextPageError(final Throwable t) {
    hideProgress(REQUEST_ID_NEXT_PAGE);
    showError(REQUEST_ID_NEXT_PAGE, t);
  }

  private static class NextPageRequestComposer implements ObservableTransformer<String, List<ListItemViewModel>> {
    final CatalogRepository catalogRepository;

    NextPageRequestComposer(final CatalogRepository catalogRepository) {
      this.catalogRepository = catalogRepository;
    }

    @Override public ObservableSource<List<ListItemViewModel>> apply(final Observable<String> upstream) {
      return upstream.flatMapSingle(
        cursor -> catalogRepository.browseNextCollectionPage(cursor, PER_PAGE)
          .map(this::convertToViewModel)
          .subscribeOn(Schedulers.io())
      );
    }

    private List<ListItemViewModel> convertToViewModel(final List<Collection> collections) {
      List<ListItemViewModel> result = new ArrayList<>();
      for (Collection collection : collections) {
        result.add(new CollectionTitleListItemViewModel(collection));
        result.add(new CollectionImageListItemViewModel(collection));
      }
      return result;
    }
  }
}
