![Mobile Buy SDK](https://cloud.githubusercontent.com/assets/5244861/26738020/885c12ac-479a-11e7-8914-2853ec09f89f.png)

# Storefront App

The Mobile Buy SDK ships with a sample application that demonstrates how to build a custom storefront on Android. Using this sample application, a user can browse your shop's products and collections, add merchandise to a cart, and then checkout using Android Pay or a web checkout.

## Setup

To run the sample application, you need to provide credentials to the shop that the app will point to:

1. Make sure that you have the **Mobile App** channel installed in your Shopify admin.
2. From your Shopify admin, navigate to the **Mobile App** channel, and then copy your API key.
3. Open the Sample Application project in `/MobileBuy/sample`
4. Run `git submodule update --init --recursive` to update recursevly git submodules
5. Sample application provides 2 product flavours: `shopify` and `apollo`. You should select `shopify` in AS to switch sample application to use BuySDK. At the same time `apollo` flavour demonstrates how you can use Apollo GraphQL client with Shopify StoreFront schema. (NOTE: please make sure you clean the project after you switching between these 2 flavours)
6. Create `shop.properties` file under the root folder and add next lines:

```
SHOP_DOMAIN=<your-shop-here>.myshopify.com
API_KEY=<your-api-key>
ANDROID_PAY_PUBLIC_KEY=<your-android-pay-public-key>
ANDROID_PAY_ENVIRONMENT=com.google.android.gms.wallet.WalletConstants.<ENVIRONMENT_TEST OR ENVIRONMENT_PRODUCTION>
```

## License

The Mobile Buy SDK is provided under an MIT License.  See the [LICENSE](../../LICENSE) file
