package com.shopify.sample.domain.usecases;

import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.ApolloClient;
import com.apollographql.apollo.ApolloQueryCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.api.internal.Optional;
import com.apollographql.apollo.exception.ApolloException;
import com.shopify.sample.data.apollo.Converter;
import com.shopify.sample.domain.CollectionPageWithProductsQuery;
import com.shopify.sample.domain.model.Collection;
import com.shopify.sample.domain.type.CollectionSortKeys;
import com.shopify.sample.util.CallbackExecutors;

import java.util.List;

import javax.annotation.Nonnull;

public final class FetchCollectionsUseCaseImpl implements FetchCollectionsUseCase {

  private final CallbackExecutors callbackExecutors;
  private final ApolloClient apolloClient;

  public FetchCollectionsUseCaseImpl(final CallbackExecutors callbackExecutors, final ApolloClient apolloClient) {
    this.callbackExecutors = callbackExecutors;
    this.apolloClient = apolloClient;
  }

  @Override
  public Cancelable execute(@Nullable final String cursor, final int perPage, @NonNull final Callback1<List<Collection>> callback) {
    CollectionPageWithProductsQuery query = CollectionPageWithProductsQuery.builder()
      .perPage(perPage)
      .nextPageCursor(cursor)
      .collectionSortKey(CollectionSortKeys.TITLE)
      .build();
    final ApolloQueryCall<Optional<CollectionPageWithProductsQuery.Data>> call = apolloClient.query(query);
    call.enqueue(new Callback(callback, callbackExecutors.handler()));
    return call::cancel;
  }

  private static class Callback extends ApolloCall.Callback<Optional<CollectionPageWithProductsQuery.Data>> {

    private final Callback1<List<Collection>> callback;
    private final Handler handler;

    public Callback(final Callback1<List<Collection>> callback, final Handler handler) {
      this.callback = callback;
      this.handler = handler;
    }

    @Override
    public void onResponse(@Nonnull final Response<Optional<CollectionPageWithProductsQuery.Data>> response) {
      final CollectionPageWithProductsQuery.Shop shop = response.data().get().shop();
      List<Collection> collections = Converter.convertCollections(shop.collectionConnection());
      handler.post(() -> callback.onResponse(collections));
    }

    @Override
    public void onFailure(@Nonnull final ApolloException error) {
      handler.post(() -> callback.onError(error));
    }
  }
}
