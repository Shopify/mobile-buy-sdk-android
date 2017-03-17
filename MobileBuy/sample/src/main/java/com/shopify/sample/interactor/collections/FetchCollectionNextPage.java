package com.shopify.sample.interactor.collections;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.shopify.sample.presenter.collections.Collection;

import java.util.List;

import io.reactivex.Single;

public interface FetchCollectionNextPage {

  @NonNull Single<List<Collection>> call(@Nullable String cursor, int perPage);
}
