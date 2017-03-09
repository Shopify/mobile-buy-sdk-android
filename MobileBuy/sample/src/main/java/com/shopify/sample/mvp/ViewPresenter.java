package com.shopify.sample.mvp;

public interface ViewPresenter<V extends View> {

    void attachView(V view);

    void detachView();

    boolean isViewAttached();
}
