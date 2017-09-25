package com.shopify.sample.view.collections;

import android.support.annotation.NonNull;

import com.shopify.sample.Constant;
import com.shopify.sample.domain.model.Collection;
import com.shopify.sample.util.UseCase.Cancelable;
import com.shopify.sample.util.Util;
import com.shopify.sample.view.base.BasePaginatedListViewModel;

import java.util.List;

public class CollectionListViewModel extends BasePaginatedListViewModel<Collection> {

  @Override
  protected Cancelable onFetchData(@NonNull final List<Collection> data) {
    String cursor = Util.reduce(data, (acc, val) -> val.cursor, null);
    return getUseCases()
      .fetchCollections()
      .execute(cursor, Constant.PAGE_SIZE, this);
  }
}
