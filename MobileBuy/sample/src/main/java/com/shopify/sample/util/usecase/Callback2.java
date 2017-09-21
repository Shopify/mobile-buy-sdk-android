package com.shopify.sample.util.usecase;

public interface Callback2<T1, T2> extends Callback {

  void onResponse(T1 t1, T2 t2);
}
