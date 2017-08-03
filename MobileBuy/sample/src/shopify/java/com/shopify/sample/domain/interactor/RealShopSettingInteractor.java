package com.shopify.sample.domain.interactor;

import android.support.annotation.NonNull;

import com.shopify.buy3.Storefront;
import com.shopify.sample.SampleApplication;
import com.shopify.sample.domain.model.ShopSettings;
import com.shopify.sample.domain.repository.ShopRepository;

import io.reactivex.Single;

public final class RealShopSettingInteractor implements ShopSettingInteractor {
  private final ShopRepository repository;

  public RealShopSettingInteractor() {
    repository = new ShopRepository(SampleApplication.graphClient());
  }

  @NonNull @Override public Single<ShopSettings> execute() {
    Storefront.ShopQueryDefinition query = q -> q
      .name()
      .paymentSettings(settings -> settings
        .countryCode()
        .acceptedCardBrands()
      );
    return repository
      .shopSettings(query)
      .map(Converters::convertToShopSettings);
  }
}
