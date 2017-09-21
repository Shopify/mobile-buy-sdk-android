package com.shopify.sample.util.usecase;

public interface Callback4<T1, T2, T3, T4> extends Callback {

  void onResponse(T1 t1, T2 t2, T3 t3, T4 t4);
}
