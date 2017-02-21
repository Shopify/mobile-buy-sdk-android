package com.shopify.buy3;

public interface GraphCallFactory {

  GraphCall<APISchema.QueryRoot> queryGraph(APISchema.QueryRootQuery query);

  GraphCall<APISchema.Mutation> mutateGraph(APISchema.MutationQuery query);
}
