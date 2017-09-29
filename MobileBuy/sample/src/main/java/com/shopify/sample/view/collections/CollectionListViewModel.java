package com.shopify.sample.view.collections;

import android.support.annotation.NonNull;

import com.shopify.sample.view.Constant;
import com.shopify.sample.domain.model.Collection;
import com.shopify.sample.core.UseCase.Cancelable;
import com.shopify.sample.util.Util;
import com.shopify.sample.view.base.BasePaginatedListViewModel;

import java.util.List;

public final class CollectionListViewModel extends BasePaginatedListViewModel<Collection> {

  @Override
  protected Cancelable onFetchData(@NonNull final List<Collection> data) {
    String cursor = Util.reduce(data, (acc, val) -> val.cursor, null);
    return useCases()
      .fetchCollections()
      .execute(cursor, Constant.PAGE_SIZE, this);
  }
}
