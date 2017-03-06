package com.shopify.buy3;

import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.shopify.graphql.support.AbstractResponse;

public interface GraphCall<T extends AbstractResponse<T>> {

  void cancel();

  @NonNull GraphCall<T> clone();

  @NonNull GraphResponse<T> execute() throws GraphError;

  @NonNull GraphCall<T> enqueue(@NonNull Callback<T> callback);

  @NonNull GraphCall<T> enqueue(@NonNull Callback<T> callback, @Nullable Handler handler);

  interface Callback<T extends AbstractResponse<T>> {

    void onResponse(@NonNull GraphResponse<T> response);

    void onFailure(@NonNull GraphError error);

  }
}
