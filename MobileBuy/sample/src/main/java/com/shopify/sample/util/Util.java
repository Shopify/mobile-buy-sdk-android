package com.shopify.sample.util;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
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

  @NonNull public static <T> T minItem(@NonNull Collection<T> source, @NonNull T defaultValue, @NonNull Comparator<T> comparator) {
    checkNotNull(source, "source == null");
    checkNotNull(comparator, "comparator == null");
    if (source.isEmpty()) {
      return defaultValue;
    }

    T minItem = null;
    for (T item : source) {
      if (minItem == null) {
        minItem = item;
      } else {
        if (comparator.compare(minItem, item) >= 0) {
          minItem = item;
        }
      }
    }

    //noinspection ConstantConditions
    return minItem;
  }

  private Util() {
  }
}
