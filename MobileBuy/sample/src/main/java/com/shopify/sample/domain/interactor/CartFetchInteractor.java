package com.shopify.sample.domain.interactor;

import android.support.annotation.NonNull;

import com.shopify.sample.domain.model.Cart;

public interface CartFetchInteractor {
  @NonNull Cart execute();
}
