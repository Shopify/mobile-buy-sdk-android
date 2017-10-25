package com.shopify.sample.view.collections;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Transformations;
import android.support.annotation.NonNull;

import com.shopify.sample.core.UseCase.Cancelable;
import com.shopify.sample.domain.model.Collection;
import com.shopify.sample.util.Util;
import com.shopify.sample.view.Constant;
import com.shopify.sample.view.base.BasePaginatedListViewModel;
import com.shopify.sample.view.base.ListItemViewModel;

import java.util.ArrayList;
import java.util.List;

public final class CollectionListViewModel extends BasePaginatedListViewModel<Collection> {

  private final LiveData<List<ListItemViewModel>> items = Transformations
    .map(data(), collections -> Util.reduce(collections, (viewModels, collection) -> {
      viewModels.add(new CollectionTitleListItemViewModel(collection));
      viewModels.add(new CollectionImageListItemViewModel(collection));
      viewModels.add(new ProductsListItemViewModel(collection.products));
      viewModels.add(new CollectionDescriptionSummaryListItemViewModel(collection));
      viewModels.add(new CollectionDividerListItemViewModel(collection));
      return viewModels;
    }, new ArrayList<ListItemViewModel>()));

  public LiveData<List<ListItemViewModel>> items() {
    return items;
  }

  @Override
  protected Cancelable onFetchData(@NonNull final List<Collection> data) {
    String cursor = Util.reduce(data, (acc, val) -> val.cursor, null);
    return useCases()
      .fetchCollections()
      .execute(cursor, Constant.PAGE_SIZE, this);
  }
}
