package com.shopify.sample.domain.interactor;

import com.shopify.sample.SampleApplication;
import com.shopify.sample.domain.ShopSettingsQuery;
import com.shopify.sample.domain.model.ShopSettings;
import com.shopify.sample.domain.repository.ShopRepository;

import io.reactivex.Single;

public class RealShopSettingInteractor implements ShopSettingInteractor {
  private final ShopRepository repository;

  public RealShopSettingInteractor() {
    repository = new ShopRepository(SampleApplication.apolloClient());
  }

  @Override public Single<ShopSettings> execute() {
    ShopSettingsQuery query = new ShopSettingsQuery();
    return repository.shopSettings(query).map(Converters::convertToShopSettings);
  }
}
