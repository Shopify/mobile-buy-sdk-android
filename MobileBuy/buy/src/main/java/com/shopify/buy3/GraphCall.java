package com.shopify.buy3;

import android.support.annotation.NonNull;

import com.shopify.graphql.support.AbstractResponse;

public interface GraphCall<T extends AbstractResponse<T>> {

  void cancel();

  @NonNull GraphCall<T> clone();

  @NonNull GraphResponse<T> execute() throws GraphError;

  @NonNull GraphCall<T> enqueue(@NonNull Callback<T> callback);

  interface Callback<T extends AbstractResponse<T>> {

    void onResponse(@NonNull GraphResponse<T> response);

    void onFailure(@NonNull GraphError error);

  }
}
