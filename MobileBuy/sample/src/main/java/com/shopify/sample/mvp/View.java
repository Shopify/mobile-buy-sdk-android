package com.shopify.sample.mvp;

import android.content.Context;

public interface View {

    Context getContext();

    void showProgress(long requestId);

    void hideProgress(long requestId);

    void showError(long requestId, Throwable t);
}
