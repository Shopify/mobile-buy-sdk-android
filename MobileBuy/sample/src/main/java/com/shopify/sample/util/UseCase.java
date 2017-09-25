package com.shopify.sample.util;

public interface UseCase {

  interface Cancelable {

    void cancel();
  }

  interface Callback {

    void onError(Throwable throwable);
  }

  interface Callback0 extends Callback {

    void onResponse();
  }

  interface Callback1<T1> extends Callback {

    void onResponse(T1 t1);
  }

  interface Callback2<T1, T2> extends Callback {

    void onResponse(T1 t1, T2 t2);
  }
}
