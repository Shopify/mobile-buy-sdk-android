package com.shopify.sample.domain.interactor;

import android.support.annotation.NonNull;

import com.shopify.sample.domain.model.ShopSettings;

import io.reactivex.Single;

public interface ShopSettingInteractor {

  @NonNull Single<ShopSettings> execute();
}
