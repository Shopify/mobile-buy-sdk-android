/*
 *   The MIT License (MIT)
 *
 *   Copyright (c) 2015 Shopify Inc.
 *
 *   Permission is hereby granted, free of charge, to any person obtaining a copy
 *   of this software and associated documentation files (the "Software"), to deal
 *   in the Software without restriction, including without limitation the rights
 *   to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *   copies of the Software, and to permit persons to whom the Software is
 *   furnished to do so, subject to the following conditions:
 *
 *   The above copyright notice and this permission notice shall be included in
 *   all copies or substantial portions of the Software.
 *
 *   THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *   IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *   FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *   AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *   LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *   OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 *   THE SOFTWARE.
 */

package com.shopify.sample.util;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

public final class Util {

  public static <I, T extends Collection<I>> T checkNotEmpty(T reference, @Nullable Object errorMessage) {
    if (reference == null) throw new NullPointerException(String.valueOf(errorMessage));
    if (reference.isEmpty()) throw new IllegalArgumentException(String.valueOf(errorMessage));
    return reference;
  }

  public static String checkNotBlank(String reference, @Nullable Object errorMessage) {
    if (reference == null) throw new NullPointerException(String.valueOf(errorMessage));
    if (reference.isEmpty()) throw new IllegalArgumentException(String.valueOf(errorMessage));
    return reference;
  }

  public static <T> T checkNotNull(final T reference, @Nullable final Object errorMessage) {
    if (reference == null) {
      throw new NullPointerException(String.valueOf(errorMessage));
    }
    return reference;
  }

  @NonNull
  public static <T, R> List<R> mapItems(@NonNull final Collection<T> source, @NonNull final Function<T, R> transformer) {
    checkNotNull(source, "source == null");
    checkNotNull(transformer, "transformer == null");
    List<R> result = new ArrayList<>();
    for (T item : source) {
      result.add(transformer.apply(item));
    }
    return result;
  }

  @Nullable
  public static <T> T firstItem(@Nullable final List<T> source) {
    return source != null && !source.isEmpty() ? source.get(0) : null;
  }

  @Nullable
  public static <T> List<T> filter(@Nullable final List<T> source, @NonNull final Function<T, Boolean> condition) {
    checkNotNull(source, "source == null");
    checkNotNull(condition, "condition == null");
    List<T> result = new ArrayList<>();
    for (T item : source) {
      if (condition.apply(item)) {
        result.add(item);
      }
    }
    return result;
  }

  @Nullable
  public static <T, R> R firstItem(@Nullable final List<T> source, @NonNull final Function<T, R> transformer) {
    checkNotNull(transformer, "transformer == null");
    return source != null && !source.isEmpty() ? transformer.apply(source.get(0)) : null;
  }

  @NonNull
  public static <T> T minItem(@NonNull final Collection<T> source, @NonNull final T defaultValue,
                              @NonNull final Comparator<T> comparator) {
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

  public static <T, R> R fold(@Nullable final R initialValue, @NonNull final Collection<T> source,
                              @NonNull final BiFunction<R, T, R> accumulator) {
    checkNotNull(source, "source == null");
    checkNotNull(accumulator, "accumulator == null");
    R result = initialValue;
    for (T item : source) {
      result = accumulator.apply(result, item);
    }
    return result;
  }

  public static <T, R> R reduce(@Nullable final Collection<T> source, @NonNull final ReduceCallback<T, R> callback, @Nullable final R initialValue) {
    if (source == null) {
      return initialValue;
    }
    R acc = initialValue;
    for (T val : source) {
      acc = callback.reduce(acc, val);
    }
    return acc;
  }

  public interface ReduceCallback<T, R> {

    R reduce(R acc, T val);
  }

  private Util() {
  }
}
