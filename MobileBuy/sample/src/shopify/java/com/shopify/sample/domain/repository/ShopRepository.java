package com.shopify.sample.domain.repository;

import android.support.annotation.NonNull;

import com.shopify.buy3.GraphCall;
import com.shopify.buy3.GraphClient;
import com.shopify.buy3.Storefront;

import com.shopify.sample.RxUtil;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

import static com.shopify.sample.RxUtil.rxGraphQueryCall;
import static com.shopify.sample.util.Util.checkNotNull;

public final class ShopRepository {

  private final GraphClient graphClient;

  public ShopRepository(@NonNull final GraphClient graphClient) {
    this.graphClient = checkNotNull(graphClient, "graphClient == null");
  }

  @NonNull
  public Single<Storefront.Shop> shopSettings(@NonNull final Storefront.ShopQueryDefinition query) {
    checkNotNull(query, "query == null");
    GraphCall<Storefront.QueryRoot> call = graphClient.queryGraph(Storefront.query(
        root -> root.shop(query)
    ));
    return Single.fromCallable(call::clone)
        .flatMap(RxUtil::rxGraphQueryCall)
        .map(Storefront.QueryRoot::getShop)
        .subscribeOn(Schedulers.io());
  }
}
