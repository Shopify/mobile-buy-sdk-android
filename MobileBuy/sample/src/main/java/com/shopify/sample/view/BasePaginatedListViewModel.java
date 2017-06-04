package com.shopify.sample.view;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.shopify.sample.util.WeakConsumer;
import com.shopify.sample.util.WeakObserver;
import com.shopify.sample.view.base.ListItemViewModel;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.subjects.PublishSubject;
import timber.log.Timber;

import static com.shopify.sample.util.Util.checkNotNull;

@SuppressWarnings("WeakerAccess")
public abstract class BasePaginatedListViewModel<ITEM> extends BaseViewModel {
  private static final int REQUEST_ID_FETCH_NEXT_PAGE = UUID.randomUUID().hashCode();

  private boolean reset;
  private final PublishSubject<String> fetchNextPageSubject = PublishSubject.create();

  protected final MutableLiveData<List<ListItemViewModel>> listItemsLiveData = new MutableLiveData<>();

  public BasePaginatedListViewModel() {
    listItemsLiveData.setValue(Collections.emptyList());
  }

  public void reset() {
    reset = true;
    registerRequest(
      REQUEST_ID_FETCH_NEXT_PAGE,
      fetchNextPageSubject
        .distinct()
        .doOnNext(WeakConsumer.<BasePaginatedListViewModel<ITEM>, String>forTarget(this)
          .delegateAccept((target, cursor) -> target.showProgress(REQUEST_ID_FETCH_NEXT_PAGE))
          .create())
        .compose(nextPageRequestComposer())
        .observeOn(AndroidSchedulers.mainThread())
        .doOnError(WeakConsumer.<BasePaginatedListViewModel<ITEM>, Throwable>forTarget(this)
          .delegateAccept(BasePaginatedListViewModel::onNextPageError)
          .create())
        .retry()
        .subscribeWith(WeakObserver.<BasePaginatedListViewModel<ITEM>, List<ITEM>>forTarget(this)
          .delegateOnNext(BasePaginatedListViewModel::onNextPageResponseInternal)
          .create()
        )
    );
    fetchNextPageSubject.onNext("");
  }

  public void nextPage(@NonNull final String cursor) {
    fetchNextPageSubject.onNext(checkNotNull(cursor, "cursor == null"));
  }

  public LiveData<List<ListItemViewModel>> listItemsLiveData() {
    return listItemsLiveData;
  }

  protected abstract ObservableTransformer<String, List<ITEM>> nextPageRequestComposer();

  @NonNull protected abstract List<ListItemViewModel> convertAndMerge(@NonNull List<ITEM> newItems,
    @NonNull List<ListItemViewModel> existingItems);

  protected void onNextPageError(final Throwable t) {
    Timber.e(t);
    hideProgress(REQUEST_ID_FETCH_NEXT_PAGE);
    notifyUserError(REQUEST_ID_FETCH_NEXT_PAGE, t);
  }

  void onNextPageResponseInternal(@NonNull final List<ITEM> items) {
    hideProgress(REQUEST_ID_FETCH_NEXT_PAGE);

    if (reset) {
      reset = false;
      listItemsLiveData.setValue(Collections.emptyList());
    }
    //noinspection ConstantConditions
    listItemsLiveData.setValue(convertAndMerge(items, listItemsLiveData.getValue()));
  }
}
