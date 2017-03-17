package com.shopify.sample.mvp;

public interface View {

    void showProgress(long requestId);

    void hideProgress(long requestId);

    void showError(long requestId, Throwable t);
}
