package com.shopify.sample.util.usecase;

public interface Callback3<T1, T2, T3> extends Callback {

  void onResponse(T1 t1, T2 t2, T3 t3);
}
