package com.shopify.sample.mvp;

import android.content.Context;

import com.shopify.sample.util.RequestRegister;

import java.lang.ref.WeakReference;

import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

import static com.shopify.sample.util.Util.checkNotNull;

public abstract class BaseViewPresenter<V extends View> implements ViewPresenter<V> {
  private boolean viewAttached;
  private WeakReference<V> viewRef;
  private RequestRegister<Integer> requestRegister;

  @Override
  public void attachView(final V view) {
    viewRef = new WeakReference<>(view);

    if (requestRegister != null) {
      requestRegister.dispose();
    }
    requestRegister = new RequestRegister<>();

    viewAttached = true;
  }

  @Override
  public void detachView() {
    viewAttached = false;
    viewRef.clear();

    if (requestRegister != null) {
      requestRegister.dispose();
    }
    requestRegister = null;
  }

  @Override
  public boolean isViewAttached() {
    return viewAttached;
  }

  protected V view() {
    return viewRef.get();
  }

  protected Context getContext() {
    final V view = viewRef.get();
    return view != null ? view.getContext() : null;
  }

  protected void registerRequest(final int requestId, @NonNull final Disposable disposable) {
    if (isViewAttached()) {
      cancelRequest(requestId);
      requestRegister.add(requestId, checkNotNull(disposable, "disposable == null"));
    }
  }

  protected void cancelRequest(final int requestId) {
    if (requestRegister != null) {
      requestRegister.delete(requestId);
    }
  }

  protected void cancelAllRequests() {
    if (requestRegister != null) {
      requestRegister.deleteAll();
    }
  }

  protected void showProgress(final long requestId) {
    if (isViewAttached()) {
      final V view = view();
      if (view != null) {
        view.showProgress(requestId);
      }
    }
  }

  protected void onRequestError(final long requestId, final Throwable t) {
    hideProgress(requestId);
    showError(requestId, t);
  }

  protected void hideProgress(final long requestId) {
    if (isViewAttached()) {
      final V view = view();
      if (view != null) {
        view.hideProgress(requestId);
      }
    }
  }

  protected void showError(final long requestId, final Throwable t) {
    if (isViewAttached()) {
      final V view = view();
      if (view != null) {
        view.showError(requestId, t);
      }
    }
  }
}
