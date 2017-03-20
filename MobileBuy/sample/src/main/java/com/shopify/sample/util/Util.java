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
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;

public final class Util {

  public static <I, T extends Collection<I>> T checkNotEmpty(T reference, @Nullable Object errorMessage) {
    if (reference == null) throw new NullPointerException(String.valueOf(errorMessage));
    if (reference.isEmpty()) throw new IllegalArgumentException(String.valueOf(errorMessage));
    return reference;
  }

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

  @Nullable public static <T> T firstItem(@Nullable List<T> source) {
    return source != null && !source.isEmpty() ? source.get(0) : null;
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
