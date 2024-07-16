![Mobile Buy SDK](https://cloud.githubusercontent.com/assets/5244861/26374751/6895a582-3fd4-11e7-80c4-2c1632262d66.png)

[![Tests](https://github.com/Shopify/mobile-buy-sdk-android/actions/workflows/test.yml/badge.svg)](https://github.com/Shopify/mobile-buy-sdk-android/actions/workflows/test.yml)
[![GitHub license](https://img.shields.io/badge/license-MIT-lightgrey.svg)](https://github.com/Shopify/mobile-buy-sdk-ios/blob/main/LICENSE)
[![GitHub release](https://img.shields.io/github/release/shopify/mobile-buy-sdk-android.svg)](https://github.com/Shopify/mobile-buy-sdk-android/releases)

# Mobile Buy SDK

The Mobile Buy SDK makes it easy to create custom storefronts in your mobile app. The SDK connects to the Shopify platform using GraphQL, and supports a wide range of native storefront experiences.

## Table of contents

- [Installation](#installation-)
- [Getting started](#getting-started-)
- [Code generation](#code-generation-)

  - [Request models](#request-models-)
  - [Response models](#response-models-)
  - [The `Node` protocol](#the-node-protocol-)
  - [Aliases](#aliases-)

- [GraphClient](#graphclient-)

  - [Queries](#queries-)
  - [Mutations](#mutations-)
  - [Retry and polling](#retry-)
  - [Caching](#caching-)
  - [Errors](#errors-)
    - [GraphQL Error](#graphql-error-)
    - [GraphError](#grapherror-)

- [Search](#search-)

  - [Fuzzy matching](#fuzzy-matching-)
  - [Field matching](#field-matching-)
  - [Negating field matching](#negating-field-matching-)
  - [Boolean operators](#boolean-operators-)
  - [Comparison operators](#comparison-operators-)
  - [Exists operator](#exists-operator-)

- [Case studies](#case-studies-)

  - [Fetch shop](#fetch-shop-)
  - [Fetch collections and products](#fetch-collections-and-products-)
  - [Pagination](#pagination-)
  - [Fetch product details](#fetch-product-details-)
  - [Customer Accounts](#customer-accounts-)
    - [Creating a customer](#creating-a-customer-)
    - [Customer login](#customer-login-)
    - [Password reset](#password-reset-)
    - [Create, update and delete address](#create-update-and-delete-address-)
    - [Customer information](#customer-information-)
    - [Customer Addresses](#customer-addresses-)
    - [Customer Orders](#customer-orders-)
    - [Customer Update](#customer-update-)

- [Sample application](#sample-application-)
- [Contributions](#contributions-)
- [Help](#help-)
- [License](#license-)

## Installation [⤴](#table-of-contents)

Mobile Buy SDK for Android is represented by runtime module that provides support to build and execute GraphQL queries.

##### Gradle:

```gradle
implementation 'com.shopify.mobilebuysdk:buy3:3.2.3'
```

##### or Maven:

```
<dependency>
  <groupId>com.shopify.mobilebuysdk</groupId>
  <artifactId>buy3</artifactId>
  <version>3.2.3</version>
</dependency>
```

## Getting started [⤴](#table-of-contents)

The Buy SDK is built on [GraphQL](http://graphql.org/). The SDK handles all the query generation and response parsing, exposing only typed models and compile-time checked query structures. It doesn't require you to write stringed queries, or parse JSON responses.

You don't need to be an expert in GraphQL to start using it with the Buy SDK (but it helps if you've used it before). The sections below provide a brief introduction to this system, and some examples of how you can use it to build secure custom storefronts.

## Migration from SDK v2.0 [⤴](#table-of-contents)

The previous version of the Mobile SDK (version 2.0) is based on a REST API. With version 3.0, Shopify is migrating from REST to GraphQL.

Unfortunately, the specifics of generation GraphQL models make it almost impossible to create a migration path from v2.0 to v3.0 (domains models are not backward compatible). However, the main concepts are the same across the two versions, such as collections, products, checkouts, and orders.

## Code Generation [⤴](#table-of-contents)

The Buy SDK is built on a hierarchy of generated classes that construct and parse GraphQL queries and response. These classes are generated manually by running a custom Ruby script that relies on the [GraphQL Java Generation](https://github.com/Shopify/graphql_java_gen) library. Most of the generation functionality and supporting classes live inside the library. It works by downloading the GraphQL schema, generating Java class hierarchy, and saving the generated files to the specified folder path. In addition, it provides overrides for custom GraphQL scalar types like `DateTime`.

### Request Models [⤴](#table-of-contents)

All generated request models are represented by interfaces with one method `define` that takes single argument, generated query builder. Every query starts with generated `Storefront.QueryRootQueryDefinition` interface that defines the root of your query.

Let's take a look at an example query for a shop's name:

```java
QueryRootQuery query = Storefront.query(new Storefront.QueryRootQueryDefinition() {
    @Override public void define(final Storefront.QueryRootQuery rootQueryBuilder) {
      rootQueryBuilder.shop(new Storefront.ShopQueryDefinition() {
        @Override public void define(final Storefront.ShopQuery shopQueryBuilder) {
          shopQueryBuilder.name();
        }
      });
    }
})
```

In this example:

- `Storefront.query` is the entry point for building GraphQL queries.
- `Storefront.QueryRootQueryDefinition` represents the root of the query where we ask for the shop's `rootQueryBuilder.shop`.
- `Storefront.ShopQueryDefinition` represents the subquery definition for shop field, where we request the shop's `shopQueryBuilder.name`.

Request models are generated in such way where lambda expressions can come in handy. We can use lambda expressions to make the initial query more concise:

```java
QueryRootQuery query = Storefront.query(rootQueryBuilder ->
  rootQueryBuilder
    .shop(shopQueryBuilder ->
      shopQueryBuilder
        .name()
    )
)
```

The code example above produces the following GraphQL query (you can call `query.toString()` to see a built GraphQL query):

```graphql
query {
  shop {
    name
  }
}
```

### Response models [⤴](#table-of-contents)

All generated response models are derived from the `AbstractResponse` type. This abstract class provides a similar key-value type interface to a `Map` for accessing field values in GraphQL responses. You should never use these accessors directly, and instead rely on typed, derived properties in generated subclasses.

Let's continue the example of accessing the result of a shop name query:

```java
// The right way

Storefront.QueryRoot response = ...;

String name = response.getShop().getName();
```

Never use the abstract class directly:

```java
// The wrong way (never do this)

AbstractResponse response = ...;

AbstractResponse shop = (AbstractResponse) response.get("shop");
String name = (String) shop.get("name");
```

Again, both of the approaches produce the same result, but the former case is safe and requires no casting since it already knows about the expected type.

### The `Node` protocol [⤴](#table-of-contents)

GraphQL schema defines a `Node` interface that declares an `id` field on any conforming type. This makes it convenient to query for any object in the schema given only its `id`. The concept is carried across to the Buy SDK as well, but requires a cast to the correct type. You need to make sure that the `Node` type is of the correct type, otherwise casting to an incorrect type will return a runtime exception.

Given this query:

```java
ID id = new ID("NkZmFzZGZhc");
Storefront.query(rootQueryBuilder ->
  rootQueryBuilder
    .node(id, nodeQuery ->
      nodeQuery
        .onProduct(productQuery ->
          productQuery
            .title()
            ...
        )
    )
);
```

The `Storefront.Order` requires a cast:

```java
Storefront.QueryRoot response = ...;

String title = ((Storefront.Product)response.getNode()).getTitle();
```

#### Aliases [⤴](#table-of-contents)

Aliases are useful when a single query requests multiple fields with the same names at the same nesting level, since GraphQL allows only unique field names. Multiple nodes can be queried by using a unique alias for each one:

```java
Storefront.query(rootQueryBuilder ->
  rootQueryBuilder
    .node(new ID("NkZmFzZGZhc"), nodeQuery ->
      nodeQuery
      .onCollection(collectionQuery ->
        collectionQuery
          .withAlias("collection")
          .title()
          .description()
          ...
      )
    )
    .node(new ID("GZhc2Rm"), nodeQuery ->
      nodeQuery
        .onProduct(productQuery ->
          productQuery
            .withAlias("product")
            .title()
            .description()
            ...
        )
    )
);
```

Accessing the aliased nodes is similar to a plain node:

```java
Storefront.QueryRoot response = ...;

Storefront.Collection collection = (Storefront.Collection) response.withAlias("collection").getNode();
Storefront.Product product = (Storefront.Product) response.withAlias("product").getNode();
```

Learn more about [GraphQL aliases](http://graphql.org/learn/queries/#aliases).

## GraphClient [⤴](#table-of-contents)

The `GraphClient` is a network layer built on top of Square's [**OkHttp**](https://github.com/square/okhttp/) client that prepares `GraphCall` to execute `query` and `mutation` requests. It also simplifies polling and retrying requests. To get started with `GraphClient`, you need the following:

- Your shop's `.myshopify.com` domain
- Your API key, which you can find in your shop's admin
- `OkHttpClient` (optional), if you want to customize the configuration used for network requests or share your existing `OkHttpClient` with the `GraphClient`
- Settings for HTTP cache (optional), like the path to the cache folder and maximum allowed size in bytes
- HTTP cache policy (optional), to be used as default for all GraphQL **query** operations (it be ignored for mutation operations, which aren't supported). By default, the HTTP cache policy is set to `NETWORK_ONLY`.

```java
GraphClient.builder(this)
  .shopDomain(BuildConfig.SHOP_DOMAIN)
  .accessToken(BuildConfig.API_KEY)
  .httpClient(httpClient) // optional
  .httpCache(new File(getApplicationContext().getCacheDir(), "/http"), 10 * 1024 * 1024) // 10mb for http cache
  .defaultHttpCachePolicy(HttpCachePolicy.CACHE_FIRST.expireAfter(5, TimeUnit.MINUTES)) // cached response valid by default for 5 minutes
  .build()
```

GraphQL specifies two types of operations: queries and mutations. The `GraphClient` exposes these as two type-safe operations, while also offering some conveniences for retrying and polling in each.

### Queries [⤴](#table-of-contents)

Semantically, a GraphQL `query` operation is equivalent to a `GET` RESTful call. It guarantees that no resources will be mutated on the server. With `GraphClient`, you can perform a query operation using:

```java
GraphClient graphClient = ...;
Storefront.QueryRootQuery query = ...;

QueryGraphCall call = graphClient.queryGraph(query);
```

For example, let's take a look at how we can query for a shop's name:

```java
GraphClient graphClient = ...;

Storefront.QueryRootQuery query = Storefront.query(rootQuery ->
  rootQuery
    .shop(shopQuery ->
      shopQuery
        .name()
    )
);

QueryGraphCall call = graphClient.queryGraph(query);

call.enqueue(new GraphCall.Callback<Storefront.QueryRoot>() {

  @Override public void onResponse(@NonNull GraphResponse<Storefront.QueryRoot> response) {
    String name = response.data().getShop().getName();
  }

  @Override public void onFailure(@NonNull GraphError error) {
    Log.e(TAG, "Failed to execute query", error);
  }
});
```

Learn more about [GraphQL queries](http://graphql.org/learn/queries/).

### Mutations [⤴](#table-of-contents)

Semantically a GraphQL `mutation` operation is equivalent to a `PUT`, `POST` or `DELETE` RESTful call. A mutation is almost always accompanied by an input that represents values to be updated and a query to fetch fields of the updated resource. You can think of a `mutation` as a two-step operation where the resource is first modified, and then queried using the provided `query`. The second half of the operation is identical to a regular `query` request.

With `GraphClient`, you can perform a mutation operation using:

```java
GraphClient graphClient = ...;
Storefront.MutationQuery query = ...;

MutationGraphCall call = graphClient.mutateGraph(query);
```

For example, let's take a look at how we can reset a customer's password using a recovery token:

```java
GraphClient graphClient = ...;

Storefront.CustomerResetInput input = new Storefront.CustomerResetInput("c29tZSB0b2tlbiB2YWx1ZQ", "abc123");

Storefront.MutationQuery query = Storefront.mutation(rootQuery ->
  rootQuery
    .customerReset(new ID("YSBjdXN0b21lciBpZA"), input, payloadQuery ->
      payloadQuery
        .customer(customerQuery ->
          customerQuery
            .firstName()
            .lastName()
        )
        .userErrors(userErrorQuery ->
          userErrorQuery
            .field()
            .message()
        )
    )
);

MutationGraphCall call = graphClient.mutateGraph(query);

call.enqueue(new GraphCall.Callback<Storefront.Mutation>() {

  @Override public void onResponse(@NonNull final GraphResponse<Storefront.Mutation> response) {
    if (response.data().getCustomerReset().getUserErrors().isEmpty()) {
      String firstName = response.data().getCustomerReset().getCustomer().getFirstName();
      String lastName = response.data().getCustomerReset().getCustomer().getLastName();
    } else {
      Log.e(TAG, "Failed to reset customer");
    }
  }

  @Override public void onFailure(@NonNull final GraphError error) {
    Log.e(TAG, "Failed to execute query", error);
  }
});
```

A mutation will often rely on some kind of user input. Although you should always validate user input before posting a mutation, there are never guarantees when it comes to dynamic data. For this reason, you should always request the `userErrors` field on mutations (where available) to provide useful feedback in your UI regarding any issues that were encountered in the mutation query. These errors can include anything from `Invalid email address` to `Password is too short`.

Learn more about [GraphQL mutations](http://graphql.org/learn/queries/#mutations).

### Retry [⤴](#table-of-contents)

Both `QueryGraphCall` and `MutationGraphCall` have an `enqueue` function that accepts `RetryHandler`. This object encapsulates the retry state and customization parameters for how the `GraphCall` will retry subsequent requests (such as after a delay, or a number of retries).

To enable retry or polling:

1. Create a handler with a condition from one of two factory methods: `RetryHandler.delay(long delay, TimeUnit timeUnit)` or `RetryHandler.exponentialBackoff(long delay, TimeUnit timeUnit, float multiplier)`.
2. Provide an optional retry condition for response `whenResponse(Condition<GraphResponse<T>> retryCondition)` or for error `whenError(Condition<GraphError> retryCondition)`.

If the `retryCondition` evaluates to `true`, then the `GraphCall` will continue to execute the request:

```java
GraphClient graphClient = ...;
Storefront.QueryRootQuery shopNameQuery = ...;

QueryGraphCall call = graphClient.queryGraph(shopNameQuery);

call.enqueue(new GraphCall.Callback<Storefront.QueryRoot>() {

  @Override public void onResponse(GraphResponse<Storefront.QueryRoot> response) {
    ...
  }

  @Override public void onFailure(GraphError error) {
    ...
  }
}, null, RetryHandler.delay(1, TimeUnit.SECONDS)
  .maxCount(5)
  .<Storefront.QueryRoot>whenResponse(response -> response.data().getShop().getName().equals("Empty"))
      .build());
}
```

The retry handler is generic, and can handle both `QueryGraphCall` and `MutationGraphCall` requests equally well.

### Caching [⤴](#table-of-contents)

Network queries and mutations can be both slow and expensive. For resources that change infrequently, you might want to use caching to help reduce both bandwidth and latency. Since GraphQL relies on `POST` requests, we can't easily take advantage of the HTTP caching that's available in `OkHttp`. For this reason, the `GraphClient` is equipped with an opt-in caching layer that can be enabled client-wide or on a per-request basis.

**IMPORTANT:** Caching is provided only for `query` operations. It isn't available for `mutation` operations.

There are four available cache policies `HttpCachePolicy`:

- `CACHE_ONLY` - Fetch a response from the cache only, ignoring the network. If the cached response doesn't exist or is expired, then return an error.
- `NETWORK_ONLY` - Fetch a response from the network only, ignoring any cached responses.
- `CACHE_FIRST` - Fetch a response from the cache first. If the response doesn't exist or is expired, then fetch a response from the network.
- `NETWORK_FIRST` - Fetch a response from the network first. If the network fails and the cached response isn't expired, then return cached data instead.

For `CACHE_ONLY`, `CACHE_FIRST` and `NETWORK_FIRST` policies you can define the timeout after what cached response is treated as expired and will be evicted from the http cache, `expireAfter(expireTimeout, timeUnit)`.

#### Enable client-wide caching

You can enable client-wide caching by providing a default `defaultHttpCachePolicy` for any instance of `GraphClient`. This sets all `query` operations to use your default cache policy, unless you specify an alternate policy for an individual request.

In this example, we set the client's `defaultHttpCachePolicy` property to `CACHE_FIRST `:

```java
GraphClient.Builder builder = ...;
builder.defaultHttpCachePolicy(HttpCachePolicy.CACHE_FIRST)
```

Now, all calls to `queryGraph` will yield a `QueryGraphCall` with a `CACHE_FIRST` cache policy.

If you want to override a client-wide cache policy for an individual request, then specify an alternate cache policy as a parameter of `QueryGraphCall`:

```java
GraphClient client = ...;
QueryGraphCall queryCall = client.queryGraph(query)
  .cachePolicy(HttpCachePolicy.NETWORK_FIRST.expireAfter(5, TimeUnit.MINUTES))
```

In this example, the `queryCall` cache policy changes to `NETWORK_FIRST`, which means that the cached response will be valid for 5 minutes from the time the response is received.

### Errors [⤴](#table-of-contents)

There are two types of errors that you need to handle in the response callback:

- `Error` returns a list of errors inside `GraphResponse`, which represent errors related to GraphQL query itself. These should be used for debugging purposes only.
- `GraphError` represents more critical errors related to the GraphQL query execution and processing response.

#### GraphQL Error [⤴](#table-of-contents)

The `GraphResponse` class represents a GraphQL response for either a `QueryGraphCall` or `MutationGraphCall` request. It can also contain a value for `Error`, which represents the current error state of the GraphQL query.

**It's important to note that `errors` and `data` are NOT mutually exclusive.** That is to say that it's perfectly valid to have a non-null error and data. `Error` will provide more in-depth information about the query error. Keep in mind that these errors are not meant to be displayed to the end-user. **They are for debug purposes only**.

```java
GraphClient graphClient = ...;

QueryRootQuery query = Storefront.query(rootQueryBuilder ->
  rootQueryBuilder
    .shop(shopQueryBuilder ->
      shopQueryBuilder
        .name()
    )
);

QueryGraphCall call = graphClient.queryGraph(query);
call.enqueue(new GraphCall.Callback<Storefront.QueryRoot>() {

  @Override public void onResponse(GraphResponse<Storefront.QueryRoot> response) {
  	if (response.hasErrors()) {
     String errorMessage = response.formatErrorMessage();
  	}
  }

  @Override public void onFailure(GraphError error) {

  }
});

```

The following example shows a GraphQL error response:

```json
{
  "errors": [
    {
      "message": "Field 'Shop' doesn't exist on type 'QueryRoot'",
      "locations": [
        {
          "line": 2,
          "column": 90
        }
      ],
      "fields": ["Shop"]
    }
  ]
}
```

Learn more about [GraphQL errors](http://graphql.org/learn/validation/)

#### GraphError [⤴](#table-of-contents)

Errors for either a `QueryGraphCall` or `MutationGraphCall` request are defined by the hierarchy of the `GraphError` abstraction, which represents critical errors for query execution. These errors appear in the `GraphCall.Callback#onFailure` callback call. The error codes include:

- `GraphError`: `GraphCall` can't be executed due to an unknown reason
- `GraphCallCanceledError`: `GraphCall` has been canceled
- `GraphHttpError`: `GraphCall` executed but the HTTP response status code is not from the 200 series
- `GraphNetworkError`: `GraphCall` can't be executed due to network issues, such as a timeout or the network being offline
- `GraphParseError`: `GraphCall` executed but parsing JSON response has failed

To handle this type of error:

```java
GraphClient graphClient = ...;
QueryRootQuery query = ...;

QueryGraphCall call = graphClient.queryGraph(query);
call.enqueue(new GraphCall.Callback<Storefront.QueryRoot>() {

  @Override public void onResponse(GraphResponse<Storefront.QueryRoot> response) {
       ...
  }

  @Override public void onFailure(GraphError error) {
    if (error instanceof GraphCallCanceledError) {
      Log.e(TAG, "Call has been canceled", error);
    } else if (error instanceof GraphHttpError) {
      Log.e(TAG, "Http request failed: " + ((GraphHttpError) error).formatMessage(), error);
    } else if (error instanceof GraphNetworkError) {
      Log.e(TAG, "Network is not available", error);
    } else if (error instanceof GraphParseError) {
      // in most cases should never happen
      Log.e(TAG, "Failed to parse GraphQL response", error);
    } else {
      Log.e(TAG, "Failed to execute GraphQL request", error);
    }
  }
});

```

## Search [⤴](#table-of-contents)

Some `Storefront` models accept search terms via the `query` parameter. For example, you can provide a `query` to search for collections that contain a specific search term in any of their fields.

The following example shows how you can find collections that contain the word "shoes":

```java
Storefront.query(root -> root
  .shop(shop -> shop
    .collections(
      arg -> arg
        .first(10)
        .query("shoes"),
      connection -> connection
        .edges(edges -> edges
          .node(node -> node
            .title()
            .description()
          )
        )
    )
  )
)
```

#### Fuzzy matching [⤴](#table-of-contents)

In the example above, the query is `shoes`. This will match collections that contain "shoes" in the description, title, and other fields. This is the simplest form of query. It provides fuzzy matching of search terms on all fields of a collection.

#### Field matching [⤴](#table-of-contents)

As an alternative to object-wide fuzzy matches, you can also specify individual fields to include in your search. For example, if you want to match collections of particular type, you can do so by specifying a field directly:

```java
.collections(arg -> arg.query("collection_type:runners"), ...
```

The format for specifying fields and search parameters is the following: `field:search_term`. Note that it's critical that there be no space between the `:` and the `search_term`. Fields that support search are documented in the generated interfaces of the Buy SDK.

**IMPORTANT:** If you specify a field in a search (as in the example above), then the `search_term` will be an **exact match** instead of a fuzzy match. For example, based on the query above, a collection with the type `blue_runners` will not match the query for `runners`.

#### Negating field matching [⤴](#table-of-contents)

Each search field can also be negated. Building on the example above, if you want to match all collections that were **not** of the type `runners`, then you can append a `-` to the relevant field:

```java
.collections(arg -> arg.query("-collection_type:runners"), ...
```

#### Boolean operators [⤴](#table-of-contents)

In addition to single field searches, you can build more complex searches using boolean operators. They very much like ordinary SQL operators.

The following example shows how you can search for products that are tagged with `blue` and that are of type `sneaker`:

```java
.products(arg -> arg.query("tag:blue AND product_type:sneaker"), ...
```

You can also group search terms:

```java
.products(arg -> arg.query("(tag:blue AND product_type:sneaker) OR tag:red"), ...
```

#### Comparison operators [⤴](#table-of-contents)

The search syntax also allows for comparing values that aren't exact matches. For example, you might want to get products that were updated only after a certain a date. You can do that as well:

```java
.products(arg -> arg.query("updated_at:>\"2017-05-29T00:00:00Z\""), ...
```

The query above will return products that have been updated after midnight on May 29, 2017. Note how the date is enclosed by another pair of escaped quotations. You can also use this technique for multiple words or sentences.

The SDK supports the following comparison operators:

- `:` equal to
- `:<` less than
- `:>` greater than
- `:<=` less than or equal to
- `:>=` greater than or equal to

**IMPORTANT:** `:=` is not a valid operator.

#### Exists operator [⤴](#table-of-contents)

There is one special operator that can be used for checking `null` or empty values.

The following example shows how you can find products that don't have any tags. You can do so using the `*` operator and negating the field:

```java
.products(arg -> arg.query("-tag:*"), ...
```

## Case studies [⤴](#table-of-contents)

Getting started with any SDK can be confusing. The purpose of this section is to explore all areas of the Buy SDK that may be necessary to build a custom storefront on Android and provide a solid starting point for your own implementation.

In this section we're going to assume that you've [setup a client](#graphclient-) somewhere in your source code. While it's possible to have multiple instances of `GraphClient`, reusing a single instance offers many behind-the-scenes performance improvements.

### Fetch shop [⤴](#table-of-contents)

Before you display products to the user, you typically need to obtain various metadata about your shop. This can be anything from a currency code to your shop's name:

```java
GraphClient client = ...;

...

Storefront.QueryRootQuery query = Storefront.query(rootQuery -> rootQuery
  .shop(shopQuery -> shopQuery
    .name()
    .currencyCode()
    .refundPolicy(refundPolicyQuery -> refundPolicyQuery
      .title()
      .url()
    )
  )
);

client.queryGraph(query).enqueue(new GraphCall.Callback<Storefront.QueryRoot>() {

  @Override public void onResponse(@NonNull GraphResponse<Storefront.QueryRoot> response) {
    String name = response.data().getShop().getName();
    String currencyCode = response.data().getShop().getCurrencyCode().toString();
    String refundPolicyUrl = response.data().getShop().getRefundPolicy().getUrl();
  }

  ...
});
```

The corresponding GraphQL query would look like this:

```graphql
query {
  shop {
    name
    currencyCode
    refundPolicy {
      title
      url
    }
  }
}
```

### Fetch collections and products [⤴](#table-of-contents)

In our custom storefront, we want to display collection with a preview of several products. With a conventional RESTful service, this would require one network call for collections and another network call for each collection in that array. This is often referred to as the `n + 1` problem.

The Buy SDK is built on GraphQL, which solves the `n + 1` request problem. In the following example, a single query retrieves 10 collection and 10 products for each collection with just one network request:

```java
GraphClient client = ...;

...

Storefront.QueryRootQuery query = Storefront.query(rootQuery -> rootQuery
  .shop(shopQuery -> shopQuery
    .collections(arg -> arg.first(10), collectionConnectionQuery -> collectionConnectionQuery
      .edges(collectionEdgeQuery -> collectionEdgeQuery
        .node(collectionQuery -> collectionQuery
          .title()
          .products(arg -> arg.first(10), productConnectionQuery -> productConnectionQuery
            .edges(productEdgeQuery -> productEdgeQuery
              .node(productQuery -> productQuery
                .title()
                .productType()
                .description()
              )
            )
          )
        )
      )
    )
  )
);

client.queryGraph(query).enqueue(new GraphCall.Callback<Storefront.QueryRoot>() {

  @Override public void onResponse(@NonNull GraphResponse<Storefront.QueryRoot> response) {
    List<Storefront.Collection> collections = new ArrayList<>();
    for (Storefront.CollectionEdge collectionEdge : response.data().getShop().getCollections().getEdges()) {
      collections.add(collectionEdge.getNode());

      List<Storefront.Product> products = new ArrayList<>();
      for (Storefront.ProductEdge productEdge : collectionEdge.getNode().getProducts().getEdges()) {
        products.add(productEdge.getNode());
      }
    }
  }

  ...
});
```

The corresponding GraphQL query looks like this:

```graphql
{
  shop {
    collections(first: 10) {
      edges {
        node {
          id
          title
          products(first: 10) {
            edges {
              node {
                id
                title
                productType
                description
              }
            }
          }
        }
      }
    }
  }
}
```

Since it retrieves only a small subset of properties for each resource, this GraphQL request is also much more bandwidth-efficient than it would be to fetch 100 complete resources via conventional REST.

But what if you need to get more than 10 products in each collection?

### Pagination [⤴](#table-of-contents)

Although it might be convenient to assume that a single network request will suffice for loading all collections and products, in many cases a single request . The best practice is to paginate results. Since the Buy SDK is built on top of GraphQL, it inherits the concept of `edges` and `nodes`.

Learn more about [pagination in GraphQL](http://graphql.org/learn/pagination/).

Let's take a look at how we can paginate through products in a collection:

```java
GraphClient client = ...;

...

Storefront.QueryRootQuery query = Storefront.query(rootQuery -> rootQuery
  .node(new ID("IjoxNDg4MTc3MzEsImxhc3R"), nodeQuery -> nodeQuery
    .onCollection(collectionQuery -> collectionQuery
      .products(
        args -> args
          .first(10)
          .after(productPageCursor),
        productConnectionQuery -> productConnectionQuery
          .pageInfo(pageInfoQuery -> pageInfoQuery
            .hasNextPage()
          )
          .edges(productEdgeQuery -> productEdgeQuery
            .cursor()
            .node(productQuery -> productQuery
              .title()
              .productType()
              .description()
            )
          )
      )
    )
  )
);

client.queryGraph(query).enqueue(new GraphCall.Callback<Storefront.QueryRoot>() {

  @Override public void onResponse(@NonNull GraphResponse<Storefront.QueryRoot> response) {
    Storefront.Collection collection = (Storefront.Collection) response.data().getNode();
    boolean hasNextProductPage = collection.getProducts().getPageInfo().getHasNextPage();
    List<Storefront.Product> products = new ArrayList<>();
    for (Storefront.ProductEdge productEdge : collection.getProducts().getEdges()) {
      String productPageCursor = productEdge.getCursor();
      products.add(productEdge.getNode());
    }
  }

  ...
});
```

The corresponding GraphQL query looks like this:

```graphql
query {
  node(id: "IjoxNDg4MTc3MzEsImxhc3R") {
    ... on Collection {
      products(first: 10, after: "sdWUiOiIxNDg4MTc3M") {
        pageInfo {
          hasNextPage
        }
        edges {
          cursor
          node {
            id
            title
            productType
            description
          }
        }
      }
    }
  }
}
```

Since we know exactly what collection we want to fetch products for, we'll use the [`node` interface](#the-node-protocol-) to query the collection by `id`. You might notice that we're fetching a couple of additional fields and objects: `pageInfo` and `cursor`. We can then use a `cursor` of any product edge to fetch more products `before` it or `after` it. Likewise, the `pageInfo` object provides additional metadata about whether the next page (and potentially previous page) is available or not.

### Fetch product details [⤴](#table-of-contents)

In our sample app we likely want to have a detailed product page with images, variants, and descriptions. Conventionally, we'd need multiple `REST` calls to fetch all the required information. But with Buy SDK, we can do it with a single query:

```java
GraphClient client = ...;

Storefront.QueryRootQuery query = Storefront.query(rootQuery -> rootQuery
  .node(new ID("9Qcm9kdWN0LzMzMj"), nodeQuery -> nodeQuery
    .onProduct(productQuery -> productQuery
      .title()
      .description()
      .images(arg -> arg.first(10), imageConnectionQuery -> imageConnectionQuery
        .edges(imageEdgeQuery -> imageEdgeQuery
          .node(imageQuery -> imageQuery
            .src()
          )
        )
      )
      .variants(arg -> arg.first(10), variantConnectionQuery -> variantConnectionQuery
        .edges(variantEdgeQuery -> variantEdgeQuery
          .node(productVariantQuery -> productVariantQuery
            .price()
            .title()
            .available()
          )
        )
      )
    )
  )
);

client.queryGraph(query).enqueue(new GraphCall.Callback<Storefront.QueryRoot>() {

  @Override public void onResponse(@NonNull GraphResponse<Storefront.QueryRoot> response) {
    Storefront.Product product = (Storefront.Product) response.data().getNode();
    List<Storefront.Image> productImages = new ArrayList<>();
    for (final Storefront.ImageEdge imageEdge : product.getImages().getEdges()) {
      productImages.add(imageEdge.getNode());
    }
    List<Storefront.ProductVariant> productVariants = new ArrayList<>();
    for (final Storefront.ProductVariantEdge productVariantEdge : product.getVariants().getEdges()) {
      productVariants.add(productVariantEdge.getNode());
    }
  }

  ...
});
```

The corresponding GraphQL query looks like this:

```graphql
{
  node(id: "9Qcm9kdWN0LzMzMj") {
    ... on Product {
      id
      title
      description
      images(first: 10) {
        edges {
          node {
            id
            src
          }
        }
      }
      variants(first: 10) {
        edges {
          node {
            id
            price
            title
            available
          }
        }
      }
    }
  }
}
```

## Customer Accounts [⤴](#table-of-contents)

Using the Buy SDK, you can build custom storefronts that let your customers create accounts, browse previously completed orders, and manage their information. Since most customer-related actions modify states on the server, they are performed using various `mutation` requests. Let's take a look at a few examples.

### Creating a customer [⤴](#table-of-contents)

Before a customer can log in, they must first create an account. In your application, you can provide a sign-up form that runs the following `mutation` request. In this example, the `input` for the mutation is some basic customer information that will create an account on your shop.

```java
Storefront.CustomerCreateInput input = new Storefront.CustomerCreateInput("john.smith@gmail.com", "123456")
  .setFirstName(Input.value("John"))
  .setLastName(Input.value("Smith"))
  .setAcceptsMarketing(Input.value(true))
  .setPhone(Input.value("1-123-456-7890"));

Storefront.MutationQuery mutationQuery = Storefront.mutation(mutation -> mutation
  .customerCreate(input, query -> query
    .customer(customer -> customer
      .id()
      .email()
      .firstName()
      .lastName()
    )
    .userErrors(userError -> userError
      .field()
      .message()
    )
  )
);
```

Keep in mind that this mutation returns a `Storefront.Customer` object, **not** an access token. After a successful mutation, the customer will still be required to [log in using their credentials](#customer-login-).

### Customer login [⤴](#table-of-contents)

Any customer who has an account can log in to your shop. All log-in operations are `mutation` requests that exchange customer credentials for an access token. You can log in your customers using the `customerAccessTokenCreate` mutation. Keep in mind that the return access token will eventually expire. The expiry `Date` is provided by the `expiresAt` property of the returned payload.

```java
Storefront.CustomerAccessTokenCreateInput input = new Storefront.CustomerAccessTokenCreateInput("john.smith@gmail.com", "123456");
Storefront.MutationQuery mutationQuery = Storefront.mutation(mutation -> mutation
  .customerAccessTokenCreate(input, query -> query
    .customerAccessToken(customerAccessToken -> customerAccessToken
      .accessToken()
      .expiresAt()
    )
    .userErrors(userError -> userError
      .field()
      .message()
    )
  )
);
```

Optionally, you can refresh the custom access token periodically using the `customerAccessTokenRenew` mutation.

**IMPORTANT:** It is your responsibility to securely store the customer access token.

### Password reset [⤴](#table-of-contents)

Occasionally, a customer might forget their account password. The SDK provides a way for your application to reset a customer's password. A minimalistic implementation can simply call the recover mutation, at which point the customer will receive an email with instructions on how to reset their password in a web browser.

The following mutation takes a customer's email as an argument and returns `userErrors` in the payload if there are issues with the input:

```java
Storefront.MutationQuery mutationQuery = Storefront.mutation(mutation -> mutation
  .customerRecover("john.smith@gmail.com", query -> query
    .userErrors(userError -> userError
      .field()
      .message()
    )
  )
);
```

### Create, update, and delete address [⤴](#table-of-contents)

You can create, update, and delete addresses on the customer's behalf using the appropriate `mutation`. Keep in mind that these mutations require customer authentication. Each query requires a customer access token as a parameter to perform the mutation.

The following example shows a mutation for creating an address:

```java
String accessToken = ...;

Storefront.MailingAddressInput input = new Storefront.MailingAddressInput()
  .setAddress1(Input.value("80 Spadina Ave."))
  .setAddress2(Input.value("Suite 400"))
  .setCity(Input.value("Toronto"))
  .setCountry(Input.value("Canada"))
  .setFirstName(Input.value("John"))
  .setLastName(Input.value("Smith"))
  .setPhone(Input.value("1-123-456-7890"))
  .setProvince(Input.value("ON"))
  .setZip(Input.value("M5V 2J4"));

Storefront.MutationQuery mutationQuery = Storefront.mutation(mutation -> mutation
  .customerAddressCreate(accessToken, input, query -> query
    .customerAddress(customerAddress -> customerAddress
      .address1()
      .address2()
    )
    .userErrors(userError -> userError
      .field()
      .message()
    )
  )
);
```

### Customer information [⤴](#table-of-contents)

Up to this point, our interaction with customer information has been through `mutation` requests. At some point, we'll also need to show the customer their information. We can do this using customer `query` operations.

Just like the address mutations, customer `query` operations are authenticated and require a valid access token to execute. The following example shows how to obtain some basic customer info:

```java
String accessToken = ...;

Storefront.QueryRootQuery query = Storefront.query(root -> root
  .customer(accessToken, customer -> customer
    .firstName()
    .lastName()
    .email()
  )
);
```

#### Customer Addresses [⤴](#table-of-contents)

You can obtain the addresses associated with the customer's account:

```java
String accessToken = ...;

Storefront.QueryRootQuery query = Storefront.query(root -> root
  .customer(accessToken, customer -> customer
    .addresses(arg -> arg.first(10), connection -> connection
      .edges(edge -> edge
        .node(node -> node
          .address1()
          .address2()
          .city()
          .province()
          .country()
        )
      )
    )
  )
);
```

#### Customer Orders [⤴](#table-of-contents)

You can also obtain a customer's order history:

```java
String accessToken = ...;

Storefront.QueryRootQuery query = Storefront.query(root -> root
  .customer(accessToken, customer -> customer
    .orders(arg -> arg.first(10), connection -> connection
      .edges(edge -> edge
        .node(node -> node
          .orderNumber()
          .totalPrice()
        )
      )
    )
  )
);
```

#### Customer Update [⤴](#table-of-contents)

Input objects, like `Storefront.CustomerUpdateInput`, use `Input<T>` (where `T` is the type of value) to represent optional fields and distinguish `null` values from `undefined` values (eg. phone: `Input<String>`).

The following example uses `Storefront.CustomerUpdateInput` to show how to update a customer's phone number:

```java
Storefront.CustomerUpdateInput input = new Storefront.CustomerUpdateInput()
  .setPhone(Input.value("1-123-456-7890"));
```

In this example, you create an input object by setting the phone field to the new phone number that you want to update the field with. Notice that you need to pass in an `Input.value()` instead of a simple string containing the phone number.

The `Storefront.CustomerUpdateInput` object also includes other fields besides the `phone` field. These fields all default to a value of `Input.undefined()` if you don't specify them otherwise. This means that the fields aren't serialized in the mutation, and will be omitted entirely. The result GraphQL query looks like this:

```graphql
mutation {
  customerUpdate(
    customer: { phone: "+16471234567" }
    customerAccessToken: "..."
  ) {
    customer {
      phone
    }
  }
}
```

This approach works well for setting a new phone number or updating an existing phone number to a new value. But what if the customer wants to remove the phone number completely? Leaving the phone number blank or sending an empty string are semantically different and won't achieve the intended result. The former approach indicates that we didn't define a value, and the latter returns an invalid phone number error. This is where the `Input<T>` is especially useful. You can use it to signal the intention to remove a phone number by specifying a `null` value:

```java
Storefront.CustomerUpdateInput input = new Storefront.CustomerUpdateInput()
  .setPhone(Input.value(null));
```

The result is a mutation that updates a customer's phone number to `null`:

```graphql
mutation {
  customerUpdate(customer: { phone: null }, customerAccessToken: "...") {
    customer {
      phone
    }
  }
}
```

## Sample application [⤴](#table-of-contents)

To help get started, have a look at the [Storefront Sample android app](https://github.com/Shopify/mobile-buy-android-sample). It covers the most common use cases of the SDK and how to integrate with it. You can also use the [Storefront sample android app](https://github.com/Shopify/mobile-buy-android-sample) as a template, a starting point, or a place to cherrypick components as needed. Check out the [sample app's readme](https://github.com/Shopify/mobile-buy-android-sample) for more details.

## Contributions [⤴](#table-of-contents)

We welcome contributions. Please follow the steps in our [contributing guidelines](CONTRIBUTING.md).

## Help [⤴](#table-of-contents)

For help, see the [Android Buy SDK documentation](https://help.shopify.com/en/api/storefront-api/tools/android-buy-sdk) or post questions on [our forum](https://ecommerce.shopify.com/c/shopify-apis-and-technology), in `Shopify APIs & SDKs` section.

## License [⤴](#table-of-contents)

The Mobile Buy SDK is provided under an [MIT License](LICENSE).
