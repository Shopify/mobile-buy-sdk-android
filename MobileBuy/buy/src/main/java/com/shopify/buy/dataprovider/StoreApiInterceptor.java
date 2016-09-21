package com.shopify.buy.dataprovider;

import com.shopify.buy.model.Shop;

import java.util.List;

import rx.Observable;

/**
 * Represents request interceptor for Store API calls {@link com.shopify.buy.dataprovider.StoreService}
 */
public interface StoreApiInterceptor {

    /**
     * Fetch metadata about your shop
     *
     * @param originalObservable original request observable
     * @return modified or the same version of original request observable
     */
    Observable<Shop> getShop(Observable<Shop> originalObservable);
}
