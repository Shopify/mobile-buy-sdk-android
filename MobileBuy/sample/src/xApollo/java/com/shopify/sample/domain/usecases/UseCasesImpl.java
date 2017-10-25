package com.shopify.sample.domain.usecases;

import com.apollographql.apollo.ApolloClient;
import com.shopify.sample.util.CallbackExecutors;

public final class UseCasesImpl implements UseCases {

  private final CallbackExecutors callbackExecutors;
  private final ApolloClient apolloClient;

  public UseCasesImpl(final CallbackExecutors callbackExecutors, final ApolloClient apolloClient) {
    this.callbackExecutors = callbackExecutors;
    this.apolloClient = apolloClient;
  }

  @Override
  public FetchCollectionsUseCase fetchCollections() {
    return new FetchCollectionsUseCaseImpl(callbackExecutors, apolloClient);
  }

  @Override
  public FetchProductsUseCase fetchProducts() {
    return new FetchProductsUseCaseImpl(callbackExecutors, apolloClient);
  }
}
