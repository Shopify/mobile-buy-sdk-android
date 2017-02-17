package com.shopify.buy3;

import com.shopify.graphql.support.AbstractResponse;

interface OperationCallback<T extends AbstractResponse<T>> {
  void onResponse(T response);
  void onError();
}
