package com.shopify.sample.view.collections;

import android.support.annotation.NonNull;

import com.shopify.sample.domain.interactor.CollectionNextPageInteractor;
import com.shopify.sample.domain.interactor.RealCollectionNextPageInteractor;
import com.shopify.sample.domain.model.Collection;
import com.shopify.sample.view.BasePaginatedListViewModel;
import com.shopify.sample.view.base.ListItemViewModel;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.ObservableTransformer;

@SuppressWarnings("WeakerAccess")
public final class RealCollectionListViewModel extends BasePaginatedListViewModel<Collection> {
  private static final int PER_PAGE = 10;

  private final CollectionNextPageInteractor collectionNextPageInteractor = new RealCollectionNextPageInteractor();

  @Override protected ObservableTransformer<String, List<Collection>> nextPageRequestComposer() {
    return upstream -> upstream.flatMapSingle(
      cursor -> collectionNextPageInteractor.execute(cursor, PER_PAGE)
    );
  }

  @NonNull @Override protected List<ListItemViewModel> convertAndMerge(@NonNull final List<Collection> newItems,
    @NonNull final List<ListItemViewModel> existingItems) {
    List<ListItemViewModel> viewModels = new ArrayList<>();
    for (Collection collection : newItems) {
      viewModels.add(new CollectionTitleListItemViewModel(collection));
      viewModels.add(new CollectionImageListItemViewModel(collection));
      viewModels.add(new ProductsListItemViewModel(collection.products));
      viewModels.add(new CollectionDescriptionSummaryListItemViewModel(collection));
      viewModels.add(new CollectionDividerListItemViewModel(collection));
    }

    List<ListItemViewModel> result = new ArrayList<>(existingItems);
    result.addAll(viewModels);
    return result;
  }
}
