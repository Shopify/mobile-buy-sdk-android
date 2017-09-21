package com.shopify.sample.view.collections;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import com.shopify.sample.Constant;
import com.shopify.sample.domain.model.Collection;
import com.shopify.sample.util.Util;
import com.shopify.sample.util.usecase.Callback1;
import com.shopify.sample.view.base.BaseViewModel;

import java.util.List;

public class CollectionListViewModel extends BaseViewModel implements Callback1<List<Collection>> {

  private final MutableLiveData<List<Collection>> collections = new MutableLiveData<>();
  private final MutableLiveData<State> state = new MutableLiveData<>();
  private final MutableLiveData<Throwable> error = new MutableLiveData<>();

  public CollectionListViewModel() {
    state.setValue(State.NONE);
  }

  public void fetchDataIfNecessary() {
    if (isEmpty()) {
      fetchMore();
    }
  }

  public void fetchMore() {
    if (isFetching() || isEnded()) {
      return;
    }
    state.setValue(State.FETCHING);
    String cursor = Util.reduce(collections.getValue(), (acc, val) -> val.cursor, null);
    addTask(getUseCases()
      .fetchCollections()
      .execute(cursor, Constant.PAGE_SIZE, this));
  }

  public LiveData<List<Collection>> getCollections() {
    return collections;
  }

  public LiveData<Throwable> getError() {
    return error;
  }

  public LiveData<State> getState() {
    return state;
  }

  public void reset() {
    state.setValue(State.NONE);
    collections.setValue(null);
    fetchDataIfNecessary();
  }

  private boolean isEmpty() {
    return collections.getValue() == null || collections.getValue().size() == 0;
  }

  private boolean isEnded() {
    return state.getValue() == State.ENDED;
  }

  private boolean isFetching() {
    return state.getValue() == State.FETCHING;
  }

  @Override
  public void onError(final Throwable throwable) {
    error.setValue(throwable);
  }

  @SuppressWarnings("ConstantConditions")
  @Override
  public void onResponse(final List<Collection> collections) {
    if (collections.isEmpty()) {
      state.setValue(State.ENDED);
    } else {
      state.setValue(State.FETCHED);
      if (!isEmpty()) {
        collections.addAll(0, this.collections.getValue());
      }
      this.collections.setValue(collections);
    }
  }

  public enum State {
    NONE, FETCHING, FETCHED, ENDED
  }
}
