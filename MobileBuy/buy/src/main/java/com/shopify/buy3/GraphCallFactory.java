package com.shopify.buy3;

public interface GraphCallFactory {

  QueryGraphCall queryGraph(APISchema.QueryRootQuery query);

  MutationGraphCall mutateGraph(APISchema.MutationQuery query);
}
