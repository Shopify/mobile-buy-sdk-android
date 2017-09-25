package com.shopify.sample.domain;

import com.shopify.buy3.GraphClient;
import com.shopify.sample.domain.usecases.FetchCollectionsUseCase;
import com.shopify.sample.domain.usecases.FetchCollectionsUseCaseImpl;
import com.shopify.sample.util.CallbackExecutors;

public class UseCasesImpl implements UseCases {

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
}
