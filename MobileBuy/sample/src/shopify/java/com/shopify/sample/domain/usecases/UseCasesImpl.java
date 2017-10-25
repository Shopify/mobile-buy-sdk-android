package com.shopify.sample.domain.usecases;

import com.shopify.buy3.GraphClient;
import com.shopify.sample.util.CallbackExecutors;

public final class UseCasesImpl implements UseCases {

  private final CallbackExecutors callbackExecutors;
  private final GraphClient graphClient;

  public UseCasesImpl(final CallbackExecutors callbackExecutors, final GraphClient graphClient) {
    this.callbackExecutors = callbackExecutors;
    this.graphClient = graphClient;
  }

  @Override
  public FetchCollectionsUseCase fetchCollections() {
    return new FetchCollectionsUseCaseImpl(callbackExecutors, graphClient);
  }

  @Override
  public FetchProductsUseCaseImpl fetchProducts() {
    return new FetchProductsUseCaseImpl(callbackExecutors, graphClient);
  }
}
