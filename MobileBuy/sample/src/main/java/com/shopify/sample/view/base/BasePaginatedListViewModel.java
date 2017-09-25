package com.shopify.sample.view.base;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.shopify.sample.util.UseCase.Callback1;
import com.shopify.sample.util.UseCase.Cancelable;

import java.util.Collections;
import java.util.List;

public abstract class BasePaginatedListViewModel<T> extends BaseViewModel implements Callback1<List<T>> {

  private final MutableLiveData<List<T>> data = new MutableLiveData<>();
  private final MutableLiveData<Throwable> error = new MutableLiveData<>();
  private final MutableLiveData<State> state = new MutableLiveData<>();

  public BasePaginatedListViewModel() {
    reset();
  }

  public BasePaginatedListViewModel(@NonNull List<T> data) {
    this();
    onResponse(data);
  }

  public void fetchDataIfNecessary() {
    if (isEmpty()) {
      fetchData();
    }
  }

  @SuppressWarnings("ConstantConditions")
  public void fetchData() {
    if (isFetching() || isEnded()) {
      return;
    }
    state.setValue(State.FETCHING);
    addTask(onFetchData(data.getValue()));
  }

  public LiveData<List<T>> getData() {
    return data;
  }

  public LiveData<Throwable> getError() {
    return error;
  }

  public LiveData<State> getState() {
    return state;
  }

  public void reset() {
    onCleared();
    state.setValue(State.NONE);
    data.setValue(Collections.emptyList());
  }

  private boolean isEmpty() {
    return data.getValue() == null || data.getValue().size() == 0;
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
    state.setValue(State.ERROR);
  }

  @SuppressWarnings("ConstantConditions")
  @Override
  public void onResponse(final List<T> data) {
    if (data.isEmpty()) {
      state.setValue(State.ENDED);
    } else {
      if (!isEmpty()) {
        data.addAll(0, this.data.getValue());
      }
      this.data.setValue(data);
      state.setValue(State.FETCHED);
    }
  }

  public enum State {
    NONE, FETCHING, FETCHED, ERROR, ENDED
  }

  protected abstract Cancelable onFetchData(@NonNull List<T> data);
}
