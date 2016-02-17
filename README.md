![Mobile Buy SDK](http://s3.amazonaws.com/shopify-marketing_assets/static/mbsdk-github.png)

[![GitHub license](https://img.shields.io/badge/license-MIT-lightgrey.svg)](https://github.com/Shopify/mobile-buy-sdk-android/blob/master/LICENSE)
[![Build status](https://badge.buildkite.com/79fc5fd5079ed7e7f5316bbcd36e2f8101ff0b6ac99670c3ee.svg)](https://buildkite.com/shopify/mobile-buy-sdk-android)
[![GitHub release](https://img.shields.io/github/release/shopify/mobile-buy-sdk-android.svg)](https://github.com/Shopify/mobile-buy-sdk-android/releases)

# Mobile Buy SDK for Android

Shopify’s Mobile Buy SDK makes it simple to sell physical products inside your mobile app. With a few lines of code, you can connect your app with the Shopify platform and let your users buy your products using their credit card.

### Documentation

Please find all documentation on the [Mobile Buy SDK for Android page](https://docs.shopify.com/mobile-buy-sdk/android).

### Requirements

- ** Android SDK Level 14 (Ice Cream Sandwich 4.0) or higher
- ** JDK 1.7 **
- **Android studio 1.3.x** or greater recommended
- The **Mobile App** sales channel [added to your Shopify store](https://docs.shopify.com/mobile-buy-sdk/adding-mobile-app-sales-channel)

** Google Wallet **

To enable Google Wallet payments in the sample app there are a couple of extra requirements:

1. Your shop must be based in the US
2. Your shop must use USD currency
3. Your shop must accept Discover Cards for payment

### Building the SDK

Clone this repo or download as .zip and import the build.gradle file into Android Studio.

The workspace includes the Mobile Buy SDK project and a sample app which demonstrates all of the features of the SDK.

### Modules

The Mobile Buy SDK includes a two modules.

* `buy`: This is the Mobile Buy SDK framework. This is an Android library project.
* `sample`: This is an Android app module which demonstrates how to use the SDK. For more details please refer to the [Sample App Guide](https://docs.shopify.com/mobile-buy-sdk/android/sample-app-guide).

### Gradle Targets
There are a number of custom gradle targets defined in the `buy` module.
These can be invoked on the command line with ./gradlew <target>

* `javadoc` - generates the javadoc for the project
* `javadocJar` - generates the javadoc, and wraps it in a jar
* `archiveReleasePackage` - builds the release `buy` release aar, javadoc, and bundles it all together in a zip file ready for distribution.  The output will be in buy/build/distributions
* `bintrayUpload` - publishes a new release to Bintray's jCenter repository (for Shopify internal use only).

### Integration

The simplest way to use the Mobile Buy SDK in your project is to add the following jCenter dependency to your `build.gradle` file:

	compile 'com.shopify.mobilebuysdk:buy:1.2.2'

The [Integration Guide](https://docs.shopify.com/api/sdks/mobile-buy-sdk/android/integration-guide) also contains step-by-step instructions and sample code for integrating the Mobile Buy SDK into your application. The `sample` module in the Android Studio project is also a great resource.

### Running the Tests

There are two ways to run the integration tests in the SDK:

1. With mocked network responses from a pre-defined shop.
2. With live network responses from a shop of your choosing.

In either case, you will need to create a `shop.properties` file in the MobileBuy project directory with this format:

	SHOP_DOMAIN=myshop.myshopify.com
	API_KEY=0123456789abcdefghijklmnopqrstuvwxyz
	CHANNEL_ID=2345678
	
**DO NOT CHECK THE `shop.properties` FILE INTO GIT**
	
You can start the tests by running the *Tests in 'com.shopify.buy'* target in Android Studio. You can also right-click on the *buy* module in the Project hierarchy and select *Run -> Tests in 'buy'*.

#### Testing with mocked network responses

The integration tests with the mocked network responses do not require any internet connection and run very quickly. To use this mode, simply remove the values from your `shop.properties` file:

	SHOP_DOMAIN=
	API_KEY=
	CHANNEL_ID=
	
#### Testing with live network responses

To run the integration tests against an actual shop, you will need a Shopify shop that is publicly accessible (not password protected). Please note that the integration tests will create an order on that shop. This is to validate that the SDK works properly with Shopify. Modify the `test_shop_data.json` file to contain your shop's credentials and the required product IDs, gift cards, and discounts as necessary. Also, make sure that your `shop.properties` file has the correct credentials for your shop.

Some notes about `test_shop_data.json`:

- The `gift_cards` array contains three valid gift cards, one expired gift card, and one invalid gift card. Make sure that the `value` field contains the correct dollar amount for each gift card.
- The `discounts` array contains one expired discount code and one valid discount code. Make sure that the `value` field contains the correct dollar amount (or percentage amount) for each discount.

### How Can I Contribute?

We welcome contributions. Follow the steps in the [CONTRIBUTING](CONTRIBUTING.md) file.

### License 

The Mobile Buy SDK is provided under an MIT Licence. See the [LICENSE](LICENSE) file.
