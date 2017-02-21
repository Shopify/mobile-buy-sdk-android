package com.shopify.buy3;

import android.support.annotation.NonNull;

import com.shopify.graphql.support.AbstractResponse;
import com.shopify.graphql.support.SchemaViolationError;

import java.io.IOException;

public interface GraphCall<T extends AbstractResponse<T>> {
  void cancel();

  @NonNull GraphCall<T> clone();

  @NonNull T execute() throws IOException, SchemaViolationError;

  @NonNull GraphCall<T> enqueue(@NonNull Callback<T> callback);

  interface Callback<T extends AbstractResponse<T>> {
    void onResponse(@NonNull T response);

    void onFailure(@NonNull Throwable t);
  }
}
