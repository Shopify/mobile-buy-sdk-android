package com.shopify.buy3;

public interface QueryCallback extends OperationCallback<APISchema.QueryRoot> {
  @Override
  void onResponse(APISchema.QueryRoot response);

  @Override
  void onError();
}