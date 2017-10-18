package com.shopify.sample.view.products;

import android.support.annotation.NonNull;

import com.shopify.sample.core.UseCase;
import com.shopify.sample.domain.model.Product;
import com.shopify.sample.util.Util;
import com.shopify.sample.view.Constant;
import com.shopify.sample.view.base.BasePaginatedListViewModel;

import java.util.List;

public class ProductListViewModel extends BasePaginatedListViewModel<Product> {

  private final String collectionId;

  public ProductListViewModel(String collectionId) {
    super();
    this.collectionId = collectionId;
  }

  @Override
  protected UseCase.Cancelable onFetchData(@NonNull final List<Product> data) {
    String cursor = Util.reduce(data, (acc, val) -> val.cursor, null);
    return useCases()
      .fetchProducts()
      .execute(collectionId, cursor, Constant.PAGE_SIZE, this);
  }
}
