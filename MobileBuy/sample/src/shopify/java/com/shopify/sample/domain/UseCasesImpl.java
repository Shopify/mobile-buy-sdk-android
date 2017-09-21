package com.shopify.sample.domain;

import com.shopify.buy3.GraphClient;
import com.shopify.sample.domain.usecases.FetchCollectionsUseCase;
import com.shopify.sample.domain.usecases.FetchCollectionsUseCaseImpl;
import com.shopify.sample.util.Executors;

public class UseCasesImpl implements UseCases {

  private final Executors executors;
  private final GraphClient graphClient;

  public UseCasesImpl(final Executors executors, final GraphClient graphClient) {
    this.executors = executors;
    this.graphClient = graphClient;
  }

  @Override
  public FetchCollectionsUseCase fetchCollections() {
    return new FetchCollectionsUseCaseImpl(executors, graphClient);
  }
}
