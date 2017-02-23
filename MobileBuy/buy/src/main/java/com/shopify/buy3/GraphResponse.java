package com.shopify.buy3;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.shopify.graphql.support.AbstractResponse;
import com.shopify.graphql.support.Error;

import java.util.Collections;
import java.util.List;

public final class GraphResponse<T extends AbstractResponse<T>> {
  private final T data;
  private final List<Error> errors;

  GraphResponse(final T data, final List<Error> errors) {
    this.data = data;
    this.errors = errors != null ? errors : Collections.emptyList();
  }

  @Nullable public T data() {
    return data;
  }

  @NonNull public List<Error> getErrors() {
    return errors;
  }

  public boolean hasErrors() {
    return !errors.isEmpty();
  }
}
