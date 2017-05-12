![Mobile Buy SDK](https://raw.github.com/Shopify/mobile-buy-sdk-ios/master/Assets/Mobile_Buy_SDK_Github_banner.png)

[![Build Status](https://travis-ci.org/Shopify/mobile-buy-sdk-android.svg?branch=develop-v3)](https://travis-ci.org/Shopify/mobile-buy-sdk-android)
[![GitHub license](https://img.shields.io/badge/license-MIT-lightgrey.svg)](https://github.com/Shopify/mobile-buy-sdk-ios/blob/master/LICENSE)
[![GitHub release](https://img.shields.io/github/release/shopify/mobile-buy-sdk-android.svg)](https://github.com/Shopify/mobile-buy-sdk-android/releases)

# Buy SDK

Shopify’s Mobile Buy SDK makes it easy to create custom storefronts in your mobile app. With the power and flexibility of GraphQL you can build native storefront experiences using the Shopify platform. The Buy SDK lets you connect your app with the Shopify platform, where your users can buy your products using Android Pay or their credit card.

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
  - [Retry & polling](#retry-)
  - [Errors](#errors-)
      - [GraphQL Error](#graphql-error)
      - [GraphError](#grapherror)

- [Card vaulting](#card-vaulting-)
  - [Card client](#card-client-)

- [Android Pay](#android-pay-)
  - [PayCart](#paycart-)
  - [PayHelper](#payhelper-)
      - [Masked Wallet](#masked-wallet-)
      - [Full Wallet](#full-wallet-)
      - [SupportWalletFragment](#supportwalletfragment-)
      - [Retry Purchase](#retry-purchase-)
  
- [Case studies](#case-studies-)
  - [Fetch shop](#fetch-shop-)
  - [Fetch collections and products](#fetch-collections-and-products-)
  - [Pagination](#pagination-)
  - [Fetch product details](#fetch-product-details-)
  - [Checkout](#checkout-)
      - [Creating a checkout](#checkout-)
      - [Updating a checkout](#updating-a-checkout-)
      - [Polling for shipping rates](#polling-for-shipping-rates-)
      - [Completing a checkout](#completing-a-checkout-)
          - [Web](#web-checkout)
          - [Credit card](#credit-card-checkout)
          - [Android Pay](#android-pay-checkout)
      - [Polling for checkout completion](#polling-for-checkout-completion-)

- [Sample application](#sample-application-)
- [Contributions](#contributions-)
- [Help](#help-)
- [License](#license-)

## Installation [⤴](#table-of-contents)

##### Download the latest JARs:
[Mobile Buy SDK - link TBD](https://bintray.com/shopify/shopify-android/mobile-buy-sdk/_latestVersion)

[Android Pay Support - link TBD](https://bintray.com/shopify/shopify-android/mobile-buy-sdk/_latestVersion)

##### or Gradle:

```
compile 'com.shopify.mobilebuysdk:buy3:3.0.0'
compile 'com.shopify.mobilebuysdk:pay:1.0.0'
```

##### or Maven:

```
<dependency>
  <groupId>com.shopify.mobilebuysdk</groupId>
  <artifactId>buy3</artifactId>
  <version>3.0.0</version>
</dependency>

<dependency>
  <groupId>com.shopify.mobilebuysdk</groupId>
  <artifactId>pay</artifactId>
  <version>1.0.0</version>
</dependency>
```


## Getting started [⤴](#table-of-contents)

The Buy SDK is built entirely on [GraphQL](http://graphql.org/). While some knowledge of GraphQL is good to have, you don't need to be an expert to start using it with the Buy SDK. Instead of writing stringed queries and parsing JSON responses, the SDK handles all the query generation and response parsing, exposing only typed models and compile-time checked query structures. The section below will give a brief introduction to this system and provide some examples of how it makes building custom storefronts safe and easy.

## Migration from SDK v2.0 [⤴](#table-of-contents)
Previous version of Mobile SDK v2.0 is based on REST API. With newer version 3.0 Shopify is migrating from REST to GraphQL. Unfortunately, specifics of generation GraphQL models makes almost impossible to create the migration path from v2.0 to v3.0, as domains models are not backward compatible. However, the concepts remain the same, such as collections, products, checkouts, and orders.

## Code Generation [⤴](#table-of-contents)

The Buy SDK is built on a hierarchy of generated classes that construct and parse GraphQL queries and response. These classes are generated manually by running a custom Ruby script that relies on the [GraphQL Java Generation](https://github.com/Shopify/graphql_java_gen) library. Majority of the generation functionality and supporting classes live inside the library. It works by downloading the GraphQL schema, generating Java class heirarchy and saving the generated files to the specified folder path. In addition, it provides overrides for custom GraphQL scalar types like `DateTime`.


### Request Models [⤴](#table-of-contents)

All generated request models are represeted by interfaces with one method `define` that takes single argument, generated query builder. Every query starts with generated `Storefront.QueryRootQueryDefinition` interface that defines the root of your query. Let's take a look at an example query for a shop's name:

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

Where:

-  `Storefront.query` is the entry point for building GraphQL queries
-  `Storefront.QueryRootQueryDefinition` represents the root of the query where we ask for shop `rootQueryBuilder.shop`
-  `Storefront.ShopQueryDefinition` represents the subquery definition for shop field where we request for shop's name `shopQueryBuilder.name`

Request models are been generated in such way where lmbda expression can come in handy, we can re-write the initial query with lamda support in more concise way:

```java
QueryRootQuery query = Storefront.query(rootQueryBuilder -> 
  rootQueryBuilder
    .shop(shopQueryBuilder -> 
      shopQueryBuilder
        .name()
    )
)
```

The code example above will produce next GraphQL query (you can call `query.toString()` to see built GraphQL query):

```graphql
query {
  shop {
    name
  }
}
```

### Response models [⤴](#table-of-contents)

All generated response models are derived from the `AbstractResponse` type. This abstract class provides a similar key-value type interface to a `Map` for accessing field values in GraphQL responses. You should never use these accessors directly, and instead rely on typed, derived properties in generated subclasses. Let's continue the example of accessing the result of a shop name query:

```java
// The right way

Storefront.QueryRoot response = ...;

String name = response.getShop().getName();
```

Never use the abstract class directly:

```java
// Wrong way, never do this

AbstractResponse response = ...;

AbstractResponse shop = (AbstractResponse) response.get("shop");
String name = (String) shop.get("name");
```

Again, both of the approaches produce the same result but the former case is safe and requires no casting as it already knows about the expect type.

### The `Node` protocol [⤴](#table-of-contents)

GraphQL shema defines a `Node` interface that declares an `id` field on any conforming type. This makes it convenient to query for any object in the schema given only it's `id`. The concept is carried across to the Buy SDK as well but requeries a cast to the correct type. It is your responsibility to ensure that the `Node` type is of the correct type, otherwise casting to an incorrect type will result in a runtime exception. Given this query:

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
accessing the `Storefront.Order` requires a cast:

```java
Storefront.QueryRoot response = ...;

String title = ((Storefront.Product)response.getNode()).getTitle();
```

#### Aliases [⤴](#table-of-contents)

Aliases are useful when a single query requests multiple fields with the same names at the same nesting level as GraphQL allows only unique field names. Multiple nodes can be queried by using a unique alias for each one:

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
The `GraphClient` is a network layer built on top of `OkHttp` from Square that prepares `GraphCall` to execute `query` and `mutation` requests. It also provides conveniences for polling and retrying requests. To get started with `GraphClient ` you need the following:

- Your shop's `.myshopify.com` domain.
- Your API key, which can be obtained in your shop's admin page.
- Optional `OkHttpClient` if you want to customize the configuration used for network requests or share your existing `OkHttpClient` with the `GraphClient`.
- Optional settings for http cache, like path to the cache folder and maximum allowed size in bytes.
- Optional http cache policy to be used by default for all GraphQL queries (will be ignored for mutation operations as cache not supported for them). By default http cache policy is set to `NETWORK_ONLY`.

```java
GraphClient.builder(this)
  .shopDomain(BuildConfig.SHOP_DOMAIN)
  .accessToken(BuildConfig.API_KEY)
  .httpClient(httpClient) // optional
  .httpCache(new File(getApplicationContext().getCacheDir(), "/http"), 10 * 1024 * 1024) // 10mb for http cache
  .defaultHttpCachePolicy(HttpCachePolicy.CACHE_FIRST.obtain(5, TimeUnit.MINUTES)) // cached response valid by default for 5 minutes
  .build()
```
GraphQL specifies two types of operations - queries and mutations. The `GraphClient` exposes these as two type-safe operations, while also offering some conveniences for retrying and polling in each.

### Queries [⤴](#table-of-contents)
Semantically a GraphQL `query` operation is equivalent to a `GET` RESTful call and guarantees that no resources will be mutated on the server. With `GraphClient` you can perform a query operation using:

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
Semantically a GraphQL `mutation` operation is equivalent to a `PUT`, `POST` or `DELETE` RESTful call. A mutation is almost always accompanied by an input that represents values to be updated and a query to fetch fields of the updated resource. You can think of a `mutation` as a two-step operation where the resource is first modified, and then queried using the provided `query`. The second half of the operation is identical to a regular `query` request. With `GraphClient` you can perform a mutation operation using:

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
More often than not, a mutation will rely on some kind of user input. While you should always validate user input before posting a mutation, there are never guarantees when it comes to dynamic data. For this reason, you should always request the `userErrors` field on mutations (where available) to provide useful feedback in your UI regarding any issues that were encountered in the mutation query. These errors can include anything from `invalid email address` to `password is too short`.

Learn more about [GraphQL mutations](http://graphql.org/learn/queries/#mutations).

### Retry [⤴](#table-of-contents)

Both `QueryGraphCall` and `MutationGraphCall` have `enqueue` function that accepts `RetryHandler`. This object encapsulates the retry state and customization parameters for how the `GraphCall` will retry subsequent requests (such as a delay, or a set number of retries). To enable retry or polling simply create a handler with a condition from one of two factory methods: `RetryHandler.delay(long delay, TimeUnit timeUnit)` or `RetryHandler.exponentialBackoff(long delay, TimeUnit timeUnit, float multiplier)` and provide optional retry condition for response `whenResponse(Condition<GraphResponse<T>> retryCondition)` or for error `whenError(Condition<GraphError> retryCondition)`. If the `retryCondition` evaluates to `true`, the `GraphCall` will continue executing the request:

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
The retry handler is generic and can handle both `QueryGraphCall` and `MutationGraphCall` calls equally well.

### Errors [⤴](#table-of-contents)

There are two type of errors that you need to handle in the response callback. List of `Error` returned inside `GraphResponse` that represents errors related to GraphQL query itself, and should be used for debug purpose only. Second type is `GraphError` represents more critical errors related to the GraphQL query execution and processing response.

#### GraphQL Error [⤴](#table-of-contents)

The `GraphResponse` class that represents GraphQL response for either a `QueryGraphCall` or `MutationGraphCall` request may contain an optional list of `Error` that represents the current error state of the GraphQL query. **It's important to note that `errors` and `data` are NOT mutually exclusively.** That is to say that it's perfectly valid to have a non-null error and data. `Error` will provide more in-depth information about the query error. Keep in mind that these errors are not meant to be displayed to the end-user. **They are for debug purposes only**.


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

Example of GraphQL error reponse:

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
      "fields": [
        "Shop"
      ]
    }
  ]
}
```
Learn more about [GraphQL errors](http://graphql.org/learn/validation/)

#### GraphError [⤴](#table-of-contents)

Second type of errors for either a `QueryGraphCall` or `MutationGraphCall` requests is defined by hierarchy of `GraphError` abstraction that represents more critical kind of error for query execution. This kind of errors will be notified in `GraphCall.Callback#onFailure` callback call. You should expect next types of such errors:

- `GraphError` generic error when `GraphCall` can't be executed due to uknown reason
- `GraphCallCanceledError` error when `GraphCall` has been canceled
- `GraphHttpError` error when `GraphCall` executed but http response status code is not from 200 series
- `GraphNetworkError` error when `GraphCall` can't be executed due to network issues like timeouts, network is offline, etc.
- `GraphParseError` error when `GraphCall` executed but parsing JSON response has failed

To handle this type of erros:

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

## Card Vaulting [⤴](#table-of-contents)

The Buy SDK offers support for native checkout via GraphQL, which lets you complete a checkout with a credit card. However, it doesn't accept credit card numbers directly. Instead, you need to vault the credit cards via the standalone, PCI-compliant web service. The Buy SDK makes it easy with `CardClient`.

### Card Client [⤴](#table-of-contents)

Like `GraphClient`, the `CardClient` manages your interactions with the card server that provides opaque credit card tokens. The tokens are used to complete checkouts. After collecting the user's credit card information in a secure manner, create a credit card representation and submit a vault request:

```java
StoreFront.Checkout checkout = ...;
CardClient cardClient = ...;

CreditCard creditCard = CreditCard.builder()
  .firstName("John")
  .lastName("Smith")
  .number("1")
  .expireMonth("06")
  .expireYear("2017")
  .verificationCode("111")
  .build();

cardClient.vault(creditCard, checkout.getVaultUrl()).enqueue(new CreditCardVaultCall.Callback() {
  @Override public void onResponse(@NonNull String token) {
    // proceed to complete checkout with token
  }

  @Override public void onFailure(@NonNull IOException error) {
        // handle error
  }
});
```
**IMPORTANT:** Keep in mind that the credit card vaulting service does **not** provide any validation for submitted credit cards. As a result, submitting invalid credit card numbers or even missing fields will always yield a vault `token`. Any errors related to invalid credit card information will be surfaced only when the provided `token` is used to complete a checkout.

## Android Pay [⤴](#table-of-contents)

Support for Android Pay is provided by the `com.shopify.mobilebuysdk:pay` extension library. It is separate module from the Buy SDK that offers helper classes for supporting Android Pay in your application. It is designed to help you with Android Pay work flow by providing convinence helper functions and structures.

### PayCart [⤴](#table-of-contents)

Structure that represents virtual user Android Pay shopping cart by incapsulating all the state necessary for the checkout:

- shop's currency
- merchant's name
- selected product line items
- selected shipping rate
- tax

```java
PayCart payCart = PayCart.builder()
  .merchantName(MERCHANT_NAME)
  .currencyCode(shop.currency)
  .shippingAddressRequired(checkout.requiresShipping)
  .phoneNumberRequired(true)
  .shipsToCountries(Arrays.asList("US", "CA"))
  .addLineItem("Product1", 1, BigDecimal.valueOf(1.99))
  .addLineItem("Product2", 10, BigDecimal.valueOf(3.99))
  .subtotal(checkout.subtotalPrice)
  .totalPrice(checkout.totalPrice)
  .taxPrice(checkout.taxPrice)
  .build();
```

Additionally `PayCart` provides two functions to create and prepare 

request for Masked Wallet information:

```java
MaskedWalletRequest maskedWalletRequest = payCart.maskedWalletRequest(ANDROID_PAY_PUBLIC_KEY);
```

request for Full Wallet inforamtion:

```java
FullWalletRequest fullWalletRequest = payCart.fullWalletRequest(maskedWallet);
```
 
 
### PayHelper [⤴](#table-of-contents)

Helper class that designed to simplify interaction with Android Pay. It provides next helper functions:

- `isAndroidPayEnabledInManifest` checks if Android Pay is enabled
- `isReadyToPay` checks if Android Pay is ready to start purchase flow
- `requestMaskedWallet` requests Masked Wallet information (such as billing address, shipping address, payment method etc.) from the Android Pay
- `initializeWalletFragment` initializes and prepares wallet confirmation fragment
- `requestFullWallet` requests Full Wallet information to get payment token and complete checkout
- `newMaskedWallet` requests Masked Wallet information from existing one with new Google Transaction Id. Usufull when user wants to retry failed purchase and current Google Transaction Id is not valid any more
- `handleWalletResponse` helps to handle Andorid Pay wallet response by delegation callbacks via `WalletResponseHandler`
- and finally extract `PaymentToken` to complete the checkout, see `PayHelper#extractPaymentToken`

#### Masked Wallet [⤴](#table-of-contents)

To request Masked Wallet information and begin the checkout flow, you need:

- check if Android Pay is enabled:

```java
if (PayHelper.isAndroidPayEnabledInManifest(context)) {
  // show Android Pay button
}
```

- build `GoogleApiClient` with `Wallet.API` enabled and connect it

```java
googleApiClient = new GoogleApiClient.Builder(context)
  .addApi(Wallet.API, new Wallet.WalletOptions.Builder()
    .setEnvironment(ANDROID_PAY_ENVIRONMENT)
    .setTheme(WalletConstants.THEME_DARK)
    .build())
.addConnectionCallbacks(callback)
.build();

... 

googleApiClient.connect();
```

- create `PayCart` with all mandatory fields like: currency code obtained from `Storefront.Shop`, Android Pay merchant name, line items (title, price, quantity)

```java
PayCart payCart = PayCart.builder()
  .merchantName(MERCHANT_NAME)
  .currencyCode(shop.currency)
  .shippingAddressRequired(checkout.requiresShipping)
  .phoneNumberRequired(true)
  .shipsToCountries(Arrays.asList("US", "CA"))
  .addLineItem("Product1", 1, BigDecimal.valueOf(1.99))
  .addLineItem("Product2", 10, BigDecimal.valueOf(3.99))
  .subtotal(checkout.subtotalPrice)
  .totalPrice(checkout.totalPrice)
  .taxPrice(checkout.taxPrice)
  .build();
```

- start payment flow by requesting Masked Wallet inforamtion

```java
PayHelper.requestMaskedWallet(googleApiClient, payCart, ANDROID_PAY_PUBLIC_KEY);
```

After user authorizes to use their payment information on Android Pay chooser screen, `onActivityResult` will be called with returned Masked Wallet information along with Google Transaction ID and all subsequent change Masked Wallet and Full Wallet requests pertaining to this transaction. Helper function `handleWalletResponse` is designed to help you with response handling for Masked Wallet request:

```java
@Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
  PayHelper.handleWalletResponse(requestCode, resultCode, data, new PayHelper.WalletResponseHandler() {
  
    @Override public void onWalletError(int requestCode, int errorCode) {
      // show error, errorCode is one of defined in com.google.android.gms.wallet.WalletConstants
    }

    @Override public void onMaskedWallet(final MaskedWallet maskedWallet) {
      // show order confirmation screen to complete checkout
      // update checkout with shipping address from Masked Wallet
      // fetch shipping rates
    }
  });
}
```

#### Full Wallet [⤴](#table-of-contents)

Masked Wallet information has the shipping address and billing address, to be used for calculating the exact total purchase price. After the app obtains the Masked Wallet, it should present a confirmation page showing the total cost (`Storefront.Checkout#getTotalPrice`) of the items purchased in the transaction, total taxes (`Storefront.Checkout#getTotalTax`) and option to select shipping method if checkout requires shipping (`StoreFront.Checkout#getRequiresShipping`). 

When the user confirms the order Full Wallet information should be requested (`PayHelper#requestFullWallet`) to obtain payment token required to complete checkout. To request Full Wallet information you must provide updated cart with the exact total purchase price and authorized Masked Wallet by user:

```java
PayHelper.requestFullWallet(googleApiClient, payCart, maskedWallet);
```

After you retrieve the Full Wallet in the `onActivityResult`, you have enough information to proceed to payment processing for this transaction:

```java
@Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
  PayHelper.handleWalletResponse(requestCode, resultCode, data, new PayHelper.WalletResponseHandler() {
    
    @Override public void onWalletError(int requestCode, int errorCode) {
      // show error, errorCode is one of defined in com.google.android.gms.wallet.WalletConstants
    }

    @Override public void onFullWallet(FullWallet fullWallet) {
      PaymentToken paymentToken = PayHelper.extractPaymentToken(fullWallet, ANDROID_PAY_PUBLIC_KEY);
      completeCheckout(paymentToken);
    }
  });
}
```

#### SupportWalletFragment [⤴](#table-of-contents)

In most cases on order confirmation page you will embed confirmation fragment `SupportWalletFragment` provided by Android Pay which displays "change" buttons to let users optionally modify the masked wallet information (such as payment method, shipping address, etc.). To proper initialize this fragment and be able use `PayHelper` for handling responses you should use `PayHelper#initializeWalletFragment`. 

Confirmation fragment will notify in `onActivityResult` about any changes made by user to Masked Wallet information (like shipping address change), to help you to handle response with these changes you can use the same helper function `PayHelper.handleWalletResponse`:

```java
@Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
  PayHelper.handleWalletResponse(requestCode, resultCode, data, new PayHelper.WalletResponseHandler() {
        
    @Override public void onMaskedWallet(MaskedWallet maskedWallet) {
      // called when user made changes to Masked Wallet inforamtion 
      // like shipping address that requires to update checkout to request new shipping rates
      
      updateMaskedWallet(maskedWallet);
    }
});
```

#### Retry Purchase [⤴](#table-of-contents)

There can be a case when checkout can't be comlpeted with payment token obtained from Full Wallet information and you want user to retry it again. As soon as you get Full Wallet information for specified Masked Wallet, transaction associated with it considered as completed and can't be used again for requesting Full Wallet information. You should start a new transaction by obtaining Masked Wallet with a new Google Transaction Id. Otherwise if you try to use it again you will get `WalletConstants.ERROR_CODE_INVALID_TRANSACTION`. 

To request a new Masked Wallet inforamtion with new Google Transaction Id associated with it:

```java
PayHelper.newMaskedWallet(googleApiClient, maskedWallet);
```

 due to some issue related to payment gateway or token can be proccessed at the moment and you want to re-try to complete checkout again by requesting Full Wallet information, you can get `WalletConstants.ERROR_CODE_INVALID_TRANSACTION` that means current Google Transaction Id is not valid any more. You will need to start a new transaction by requesting new Masked Wallet information:

```java
@Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
  PayHelper.handleWalletResponse(requestCode, resultCode, data, new PayHelper.WalletResponseHandler() {
        
    @Override public void onMaskedWalletRequest() {
      PayHelper.requestMaskedWallet(googleApiClient, payCart, ANDROID_PAY_PUBLIC_KEY);
    }
    
    @Override public void onMaskedWallet(MaskedWallet maskedWallet) {
      updateMaskedWallet(maskedWallet);
    }
});
```

Learn more about [Android Pay](https://developers.google.com/android-pay/get-started)

## Case studies [⤴](#table-of-contents)

Getting started with any SDK can be confusing. The purpose of this section is to explore all areas of the Buy SDK that may be necessary to build a custom storefront on Android and provide a solid starting point. Let's dive right in.

In this section we're going to assume that you've [setup a client](#graphclient-) somewhere in your source code. While it's possible to have multiple instance of `GraphClient`, reusing a single instance offers many behind-the-scenes performance improvements.

### Fetch shop [⤴](#table-of-contents)

Before displaying any products to the user it's often necessary to obtain various metadata about your shop. This can be anything from a currency code to your shop's name:

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

In our custom storefront we want to display collection with a preview of several products. With a conventional RESTful service, this would require a network call to get collections and another network call for **each** collection in that array. This is often refered to as the `n + 1` problem.

The Buy SDK is built on GraphQL, which solves the `n + 1` request problem.

```java
GraphClient client = ...;

...

Storefront.QueryRootQuery query = Storefront.query(rootQuery -> rootQuery
  .shop(shopQuery -> shopQuery
    .collections(10, collectionConnectionQuery -> collectionConnectionQuery
      .edges(collectionEdgeQuery -> collectionEdgeQuery
        .node(collectionQuery -> collectionQuery
          .title()
          .products(10, productConnectionQuery -> productConnectionQuery
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

The corresponding GraphQL query would look like this:

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

This single query will retrieve 10 collection and 10 products for each collection with just one network request. Since it only retrieves a small subset of properties for each resource it's also **much** more bandwidth-efficient than fetching 100 complete resources via conventional REST.

But what if you need to get more than 10 products in each collection?

### Pagination [⤴](#table-of-contents)

While it may be convenient to assume that a single network request will suffice for loading all collections and products, but that may be naive. The best practice is to paginate results. Since the Buy SDK is built on top of GraphQL, it inherits the concept of `edges` and `nodes`.

Learn more about [pagination in GraphQL](http://graphql.org/learn/pagination/).

Let's take a look at how we can paginate throught products in a collection.

```java
GraphClient client = ...;

...

Storefront.QueryRootQuery query = Storefront.query(rootQuery -> rootQuery
  .node(new ID("IjoxNDg4MTc3MzEsImxhc3R"), nodeQuery -> nodeQuery
    .onCollection(collectionQuery -> collectionQuery
      .products(10, args -> args.after(productPageCursor), productConnectionQuery -> productConnectionQuery
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
The corresponding GraphQL query would look like this:

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

Since we know exactly what collection we want to fetch products for, we'll use the [`node` interface](#the-node-protocol-) to query the collection by `id`. You may have also noticed that we're fetching a couple of additional fields and objects: `pageInfo` and `cursor`. We can then use a `cursor` of any product edge to fetch more products `before` it or `after` it. Likewise, the `pageInfo` object provides additional metadata about whether the next page (and potentially previous page) is available or not.

### Fetch product details [⤴](#table-of-contents)

In our app we likely want to have a detailed product page with images, variants and descriptions. Conventionally, we'd need multiple `REST` calls to fetch all the required information but with Buy SDK, we can do it with a single query.

```java
GraphClient client = ...;

Storefront.QueryRootQuery query = Storefront.query(rootQuery -> rootQuery
  .node(new ID("9Qcm9kdWN0LzMzMj"), nodeQuery -> nodeQuery
    .onProduct(productQuery -> productQuery
      .title()
      .description()
      .images(10, imageConnectionQuery -> imageConnectionQuery
        .edges(imageEdgeQuery -> imageEdgeQuery
          .node(imageQuery -> imageQuery
            .src()
          )
        )
      )
      .variants(10, variantConnectionQuery -> variantConnectionQuery
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
The corresponding GraphQL query would look like this:

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

### Checkout [⤴](#table-of-contents)

After browsing products and collections, a customer may eventually want to purchase something. The Buy SDK does not provide support for a local shopping cart since the requirements can vary between applications. Instead, the implementation is left up to the custom storefront. Nevertheless, when a customer is ready to make a purchse you'll need to create a checkout.

Almost every `mutation` query requires an input object. This is the object that dictates what fields will be mutated for a particular resource. In this case, we'll need to create a `Storefront.CheckoutCreateInput`:

```java
Storefront.CheckoutCreateInput input = new Storefront.CheckoutCreateInput()
  .setLineItems(Arrays.asList(
    new Storefront.CheckoutLineItemInput(new ID("mFyaWFu"), 5),
    new Storefront.CheckoutLineItemInput(new ID("8vc2hGl"), 3)
  ));
```
      
The checkout input object accepts other arguments like `email` and `shippingAddress` but in our example, we don't have access to that information from the customer until a later time so we won't include them in this mutation. Given the checkout input, we can execute the `checkoutCreate` mutation:

```java
Storefront.MutationQuery query = Storefront.mutation(mutationQuery -> mutationQuery
  .checkoutCreate(input, createPayloadQuery -> createPayloadQuery
    .checkout(checkoutQuery -> checkoutQuery
      .webUrl()
    )
    .userErrors(userErrorQuery -> userErrorQuery
      .field()
      .message()
    )
  )
);

client.mutateGraph(query).enqueue(new GraphCall.Callback<Storefront.Mutation>() {
  @Override public void onResponse(@NonNull GraphResponse<Storefront.Mutation> response) {
    if (!response.data().getCheckoutCreate().getUserErrors().isEmpty()) {
      // handle user friendly errors
    } else {
      String checkoutId = response.data().getCheckoutCreate().getCheckout().getId().toString();
      String checkoutWebUrl = response.data().getCheckoutCreate().getCheckout().getWebUrl();
    }
  }

  @Override public void onFailure(@NonNull GraphError error) {
    // handle errors
  }
});
```
**It is best practice to always include `userErrors` fields in your mutation payload query, where possible.** You should always validate user input before making mutation requests but it's possible that there might be a mismatch between the client and server. In this case, `userErrors` will contain an error with a `field` and `message` for any invalid or missing fields.

Since we'll need to update the checkout with additional information later, all we need from a checkout in this mutation is an `id` so we can keep a reference to it. We can skip all other fields on `Storefront.Checkout` for efficiency and reduced bandwidth.

#### Updating a checkout [⤴](#table-of-contents)

A customer's information may not be available when a checkout is created. The Buy SDK provides mutations for updating specific checkout fields that are required for completion. Namely the `email` and `shippingAddress` fields:

###### Updating email [⤴](#table-of-contents)

```java
ID checkoutId = ...;

Storefront.MutationQuery query = Storefront.mutation(mutationQuery -> mutationQuery
  .checkoutEmailUpdate(checkoutId, "john.smith@gmail.com", emailUpdatePayloadQuery -> emailUpdatePayloadQuery
    .checkout(checkoutQuery -> checkoutQuery
      .webUrl()
    )
    .userErrors(userErrorQuery -> userErrorQuery
      .field()
      .message()
    )
  )
);
```

###### Updating shipping address [⤴](#table-of-contents)

```java
PayAddress address = ...;

Storefront.MailingAddressInput input = new Storefront.MailingAddressInput()
  .setAddress1(address.address1)
  .setAddress2(address.address2)
  .setCity(address.city)
  .setCountry(address.country)
  .setFirstName(address.firstName)
  .setLastName(address.lastName)
  .setPhone(address.phone)
  .setProvince(address.province)
  .setZip(address.zip);
      
Storefront.Mutation query = Storefront.mutation((mutationQuery -> mutationQuery
    .checkoutShippingAddressUpdate(input, checkoutId, shippingAddressUpdatePayloadQuery -> shippingAddressUpdatePayloadQuery
      .checkout(checkoutQuery -> checkoutQuery
        .webUrl()
      )
      .userErrors(userErrorQuery -> userErrorQuery
        .field()
        .message()
      )
    )
  )
);
```

#### Polling for shipping rates [⤴](#table-of-contents)

Available shipping rates are specific to a checkout since the cost to ship items depends on the quantity, weight and other attributes of the items in the checkout. Shipping rates also require a checkout to have a valid `shippingAddress`, which can be updated using steps found in [updating a checkout](#updating-a-checkout-). Available shipping rates are a field on `Storefront.Checkout` so given a `checkoutId` (that we kept a reference to earlier) we can query for shipping rates:

```java
ID checkoutId = ...;

Storefront.QueryRootQuery query = Storefront.query(rootQuery -> rootQuery
  .node(checkoutId, nodeQuery -> nodeQuery
    .onCheckout(checkoutQuery -> checkoutQuery
      .availableShippingRates(availableShippingRatesQuery -> availableShippingRatesQuery
        .ready()
        .shippingRates(shippingRateQuery -> shippingRateQuery
          .handle()
          .price()
          .title()
        )
      )
    )
  )
);
```

The query above will kick off an asynchoronous task on the server to fetch shipping rates from multiple shipping providers. While the request may return immedietly (network latency aside), it does not mean that the list of shipping rates is complete. This is indicated by the `ready` field in the query above. It is your application's responsibility to continue retrying this query until `ready == true`. The Buy SDK has [built-in support for retrying requests](#retry-), so we'll create a retry handler and perform the query:

```java
GraphClient client = ...;
Storefront.QueryRootQuery query = ...;
...

client.queryGraph(query).enqueue(
      new GraphCall.Callback<Storefront.QueryRoot>() {
        @Override public void onResponse(@NonNull final GraphResponse<Storefront.QueryRoot> response) {
          Storefront.Checkout checkout = (Storefront.Checkout) response.data().getNode();
          List<Storefront.ShippingRate> shippingRates = checkout.getAvailableShippingRates().getShippingRates();
        }

        @Override public void onFailure(@NonNull final GraphError error) {
        }
      },
      null,
      RetryHandler.exponentialBackoff(1, TimeUnit.MILLISECONDS, 1.2f)
        .whenResponse(
          response -> ((Storefront.Checkout) response.data().getNode()).getAvailableShippingRates().getReady() == false
        )
        .maxCount(10)
        .build()
    );
```
The callback `onResponse` will be called only if `availableShippingRates.ready == true` or the retry count reaches 10.

#### Completing a checkout [⤴](#table-of-contents)

After all required fields have been filled and the customer is ready to pay, you have 3 ways to complete the checkout and process the payment.

##### Web checkout [⤴](#table-of-contents)

The simplest way to complete a checkout is by redirecting the customer to a web view where they will be presented with the same flow that they're familiar with on the web. The `Storefront.Checkout` resource provides a `webUrl` that you can use to present a web view.

**NOTE**: While using web checkout is the simplest out of the 3 approaches, it presents some difficulty when it comes to observing the checkout state. Since the web view doesn't provide any callbacks for various checkout states, you still need to [poll for checkout completion](#poll-for-checkout-completion-).

##### Credit card checkout [⤴](#table-of-contents)

The native credit card checkout offers the most conventional UX out of the 3 alternatives but is also requires the most effort to implement. You'll be required to implement UI for gathering your customers' name, email, address, payment information and other fields required to complete checkout.

Assuming your custom storefront has all the infomation it needs, the first step to completing a credit card checkout is to vault the provided credit card and exchange it for a payment token that will be used to complete the payment. Please reference the instructions for [vaulting a credit card](#card-vaulting-).

After obtaining a credit card vault token, we can proceed to complete the checkout by creating a `CreditCardPaymentInput` and executing the mutation query:

```java
GraphClient client = ...;
ID checkoutId = ...;
BigDecimal amount = ...;
String idempotencyKey = ...;
Storefront.MailingAddressInput billingAddress = ...;
String creditCardVaultToken = ...;

Storefront.CreditCardPaymentInput input = new Storefront.CreditCardPaymentInput(amount, idempotencyKey, billingAddress,
      creditCardVaultToken);

Storefront.MutationQuery query = Storefront.mutation(mutationQuery -> mutationQuery
  .checkoutCompleteWithCreditCard(checkoutId, input, payloadQuery -> payloadQuery
    .payment(paymentQuery -> paymentQuery
      .ready()
      .errorMessage()
    )
    .checkout(checkoutQuery -> checkoutQuery
      .ready()
    )
    .userErrors(userErrorQuery -> userErrorQuery
      .field()
      .message()
    )
  )
);

client.mutateGraph(query).enqueue(new GraphCall.Callback<Storefront.Mutation>() {
  @Override public void onResponse(@NonNull final GraphResponse<Storefront.Mutation> response) {
    if (!response.data().getCheckoutCompleteWithCreditCard().getUserErrors().isEmpty()) {
      // handle user friendly errors
    } else {
      boolean checkoutReady = response.data().getCheckoutCompleteWithCreditCard().getCheckout().getReady();
      boolean paymentReady = response.data().getCheckoutCompleteWithCreditCard().getPayment().getReady();
    }
  }

  @Override public void onFailure(@NonNull final GraphError error) {
    // handle errors
  }
});
```

##### Android Pay checkout [⤴](#table-of-contents)

The Buy SDK makes Android Pay integration easy with the provided `android-pay` module. Please refer to the [Android Pay](#android-pay-) section on how to helper classes and obtain a payment token. With token in-hand, we can complete the checkout:

```java
GraphClient client = ...;
ID checkoutId = ...;
PaymentToken paymentToken = ...;
PayAddress billingAddress = ...;
PayCart payCart = ...;
String idempotencyKey = ...;

Storefront.MailingAddressInput mailingAddressInput = new Storefront.MailingAddressInput()
  .setAddress1(billingAddress.address1)
  .setAddress2(billingAddress.address2)
  .setCity(billingAddress.city)
  .setCountry(billingAddress.country)
  .setFirstName(billingAddress.firstName)
  .setLastName(billingAddress.lastName)
  .setPhone(billingAddress.phone)
  .setProvince(billingAddress.province)
  .setZip(billingAddress.zip);

Storefront.TokenizedPaymentInput input = new Storefront.TokenizedPaymentInput(payCart.totalPrice, idempotencyKey,
  mailingAddressInput, paymentToken.token, "android_pay").setIdentifier(paymentToken.publicKeyHash);

Storefront.MutationQuery query = Storefront.mutation(mutationQuery -> mutationQuery
  .checkoutCompleteWithTokenizedPayment(checkoutId, input, payloadQuery -> payloadQuery
    .payment(paymentQuery -> paymentQuery
      .ready()
      .errorMessage()
    )
    .checkout(checkoutQuery -> checkoutQuery
      .ready()
    )
    .userErrors(userErrorQuery -> userErrorQuery
      .field()
      .message()
    )
  )
);

client.mutateGraph(query).enqueue(new GraphCall.Callback<Storefront.Mutation>() {
  @Override public void onResponse(@NonNull final GraphResponse<Storefront.Mutation> response) {
    if (!response.data().getCheckoutCompleteWithCreditCard().getUserErrors().isEmpty()) {
      // handle user friendly errors
    } else {
      boolean checkoutReady = response.data().getCheckoutCompleteWithCreditCard().getCheckout().getReady();
      boolean paymentReady = response.data().getCheckoutCompleteWithCreditCard().getPayment().getReady();
    }
  }

  @Override public void onFailure(@NonNull final GraphError error) {
    // handle errors
  }
});
```

#### Polling for checkout completion [⤴](#table-of-contents)

After a successful `checkoutCompleteWith...` mutation, the checkout process has started. This process is usually short but not immediate so polling is required to obtain an updated checkout in a `ready` state - with a `Storefront.Order`.

```java
GraphClient client = ...;
ID checkoutId = ...;

Storefront.QueryRootQuery query = Storefront.query(rootQuery -> rootQuery
  .node(checkoutId, nodeQuery -> nodeQuery
    .onCheckout(checkoutQuery -> checkoutQuery
      .order(orderQuery -> orderQuery
        .createdAt()
        .orderNumber()
        .totalPrice()
      )
    )
  )
);

client.queryGraph(query).enqueue(
  new GraphCall.Callback<Storefront.QueryRoot>() {
	@Override public void onResponse(@NonNull GraphResponse<Storefront.QueryRoot> response) {
      Storefront.Checkout checkout = (Storefront.Checkout) response.data().getNode();
      String orderId = checkout.getOrder().getId().toString();
    }

    @Override public void onFailure(@NonNull GraphError error) {
    }
  },
  null,
  RetryHandler.exponentialBackoff(1, TimeUnit.MILLISECONDS, 1.2f)
    .whenResponse(
      response -> ((Storefront.Checkout) response.data().getNode()).getOrder() == null
    )
    .maxCount(10)
    .build()
);
```
Again, just like when [polling for available shipping rates](#polling-for-shipping-rates-), we need to create a `RetryHandler` to provide a condition upon which to retry the request. In this case, we're asserting that the `Storefront.Order` is `null` an continue retrying the request if it is.

## Sample application [⤴](#table-of-contents)
The Buy SDK includes a comprehensive sample application that covers the most common use cases of the SDK. It's built on best practices and our recommended `ViewModel` architecture. You can use it as a template, a starting point, or a place to cherrypick components as needed. Check out the [Storefront readme](/Sample%20Apps/Storefront/) for more details.

## Contributions [⤴](#table-of-contents)

We welcome contributions. Please follow the steps in our [contributing guidelines](CONTRIBUTING.md).

## Help [⤴](#table-of-contents)

For help, post questions on [our forum](https://ecommerce.shopify.com/c/shopify-apis-and-technology), in `Shopify APIs & SDKs` section.

## License [⤴](#table-of-contents)

The Mobile Buy SDK is provided under an [MIT License](LICENSE).
