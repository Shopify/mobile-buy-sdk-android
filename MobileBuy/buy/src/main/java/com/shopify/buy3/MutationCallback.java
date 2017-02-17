package com.shopify.buy3;

public interface MutationCallback extends OperationCallback<APISchema.Mutation> {
  @Override
  void onResponse(APISchema.Mutation response);

  @Override
  void onError();
}
