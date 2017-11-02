package com.shopify.sample.view.product;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.shopify.sample.core.UseCase;
import com.shopify.sample.core.UseCase.Cancelable;
import com.shopify.sample.domain.model.ProductDetail;
import com.shopify.sample.view.base.BaseViewModel;
import com.shopify.sample.view.livedata.CartLiveData;

public class ProductDetailViewModel extends BaseViewModel implements UseCase.Callback1<ProductDetail> {

  private final String productId;

  private final CartLiveData cart = CartLiveData.get();
  private final MutableLiveData<ProductDetail> data = new MutableLiveData<>();
  private final MutableLiveData<Throwable> error = new MutableLiveData<>();
  private final MutableLiveData<State> state = new MutableLiveData<>();

  public ProductDetailViewModel(@NonNull String productId) {
    reset();
    this.productId = productId;
  }

  @Override
  public void onError(final Throwable throwable) {
    error.setValue(throwable);
    state.setValue(State.ERROR);
  }

  @Override
  public void onResponse(final ProductDetail productDetail) {
    data.setValue(productDetail);
    state.setValue(State.COMPLETED);
  }

  public void addToCart() {
    cart.addToCart(data().getValue());
  }

  public void fetchData() {
    if (state.getValue() == State.FETCHING) {
      return;
    }
    state.setValue(State.FETCHING);
    addTask(onFetchData());
  }

  public LiveData<ProductDetail> data() {
    return data;
  }

  public LiveData<Throwable> error() {
    return error;
  }

  public void reset() {
    onCleared();
    state.setValue(State.IDLE);
  }

  public LiveData<State> state() {
    return state;
  }

  private Cancelable onFetchData() {
    return useCases()
      .fetchProductDetail()
      .execute(productId, this);
  }

  public enum State {
    IDLE, FETCHING, ERROR, COMPLETED
  }
}
