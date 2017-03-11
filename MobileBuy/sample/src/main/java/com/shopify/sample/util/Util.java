package com.shopify.sample.util;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;


public final class Util {

  public static <T> T checkNotNull(T reference, @Nullable Object errorMessage) {
    if (reference == null) {
      throw new NullPointerException(String.valueOf(errorMessage));
    }
    return reference;
  }

  @NonNull public static <T, R> List<R> mapItems(@NonNull Collection<T> source, @NonNull Function<T, R> transformer) {
    checkNotNull(source, "source == null");
    checkNotNull(transformer, "transformer == null");
    List<R> result = new ArrayList<>();
    for (T item : source) {
      result.add(transformer.apply(item));
    }
    return result;
  }

  @Nullable public static <T, R> R firstItem(@Nullable List<T> source, @NonNull Function<T, R> transformer) {
    checkNotNull(transformer, "transformer == null");
    return source != null && !source.isEmpty() ? transformer.apply(source.get(0)) : null;
  }

  private Util() {
  }
}
