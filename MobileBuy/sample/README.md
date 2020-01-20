![Mobile Buy SDK](https://cloud.githubusercontent.com/assets/5244861/26738020/885c12ac-479a-11e7-8914-2853ec09f89f.png)

# Storefront App

The Mobile Buy SDK ships with a sample application that demonstrates how to build a custom storefront on Android. Using this sample application, a user can browse your shop's products and collections, add merchandise to a cart, and then checkout using Android Pay or a web checkout.

## Setup

To run the sample application, you need to provide credentials to the shop that the app will point to:

1. Create a private app with a Storefront API access token to access the storefront data. The storefront access token acts as the API key.
2. Open the Sample Application project in `/MobileBuy/sample`
3. Run `git submodule update --init --recursive` to update recursively git submodules
4. Sample application provides two product flavors: `shopify` and `apollo`. You should select `shopify` in AS to switch sample application to use BuySDK. At the same time `apollo` flavor demonstrates how you can use Apollo GraphQL client with Shopify StoreFront schema (NOTE: please make sure you clean the project after you switching between these two flavors).
5. Create `shop.properties` file under the root folder and add next lines:
    ```
    SHOP_DOMAIN=<your-shop-here>.myshopify.com
    API_KEY=<your-api-key>
    ANDROID_PAY_PUBLIC_KEY=<your-android-pay-public-key>
    ANDROID_PAY_ENVIRONMENT=com.google.android.gms.wallet.WalletConstants.<ENVIRONMENT_TEST OR ENVIRONMENT_PRODUCTION>
    ```
6. Install a virtual device using the AVD Manager and run the sample app on that device.

## Supporting multiple languages

If your store supports multiple languages, then the Storefront API can return translated content for supported resource types and fields.
Learn more about [translating content](https://help.shopify.com/en/api/guides/multi-language/translating-content-api).

To return translated content, include the `locale` parameter in `GraphClient#build`:
```
// For instance, when the `locale` param is set to `es`, 
// the API returns the available Spanish translations. 
GraphClient.build(
  context = mockContext, 
  shopDomain = shopDomain, 
  accessToken = accessToken, 
  configure = {
    httpClient = okHttpClient
  },
  locale = locale
)
```

## License

The Mobile Buy SDK is provided under an MIT License.  See the [LICENSE](../../LICENSE) file
