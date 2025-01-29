package com.shopify.graphql.support;

import java.io.Serializable;

/**
 * Created by henrytao on 9/7/17.
 */

public final class Input<T> implements Serializable {

  private final T value;
  private final boolean defined;

  public static <T> Input<T> value(@Nullable T value) {
    return new Input<>(value, true);
  }

  public static <T> Input<T> optional(@Nullable T value) {
    return value != null ? value(value) : Input.<T>undefined();
  }

  public static <T> Input<T> undefined() {
    return new Input<>(null, false);
  }

  private Input(T value, boolean defined) {
    this.value = value;
    this.defined = defined;
  }

  public T getValue() {
    return value;
  }

  public boolean isDefined() {
    return defined;
  }
}
