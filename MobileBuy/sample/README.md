
The sample application demonstrates how to perform tasks related to creating a checkout, including how to:

* fetch products
* launch the ProductDetailsActivity with a custom theme
* apply a shipping rate
* apply gift cards and discounts
* complete checkout via web browser, Android Pay, or native checkout implementation.

Before getting started, make sure that you've [added the mobile app sales channel to your store](https://www.shopify.com/admin/channels").

## Getting started

1. Launch Android Studio.
2. Choose *Import Project*.
3. Select <kbd>build.gradle</kbd> from the sample directory to import it.
4. Add these credentials to a file named <kbd>shop.properties</kbd>:
    * Shop domain
    * API key
    * App ID
5. Place the <kbd>shop.properties</kbd> in the MobileBuy/sample directory of the project
	You can retrieve your **API key** and **App ID** in Shopify on the [Mobile App Integration page]("https://www.shopify.com/admin/mobile_app/integration").


Your <kbd>shop.properties</kbd> should have the following format:

```
SHOP_DOMAIN=<yourshop>.myshopify.com
API_KEY=<Your API Key>
APP_ID=8
```


## Overview

The sample application demonstrates tasks that can be performed using the Android Buy SDK. These tasks have been broken up into a sequential flow:

<ol>
<li><a href="#collection-list">CollectionListActivity</a></li>
<li><a href="#product-list">ProductListActivity</a></li>
<li><a href="#shipping-rate-list">ShippingRateListActivity</a></li>
<li><a href="#discount">DiscountActivity</a></li>
<li><a href="#checkout">CheckoutActivity</a></li>
</ol>

<h3 id="collection-list">CollectionListActivity</h3>

The first activity in the application flow. This activity allows you to browse the list of collections and drill down into a list of products. An item called **All Products** is added to the top of the list for convenience.

<h3 id="product-list">ProductListActivity</h3>

This activity allows you to select a product to purchase from a list of all products in a collection. There are also several additional controls above the list:

* **Demo ProductDetailsActivity** - controls whether selecting a product will display the next Activity in the sample application flow, or launch `ProductDetailsActivity` (a UI component in the SDK).

* **Theme Style** - controls the style of the theme that will appear in the `ProductDetailsActivity` (either `ProductDetailsTheme.Style.LIGHT` or `ProductDetailsTheme.Style.DARK`).

* **Shows Product Image BG** - controls whether the `ProductDetailsActivity` will show a background color behind the product images.

* **Choose Accent Color** - displays a color picker that controls the accent color for the `ProductDetailsActivity` theme.

<h3 id="shipping-rate-list">ShippingRateListActivity</h3>

If the selected product requires shipping, this activity allows the user to select a list of shipping rates. For the sample application, the shipping address has been hardcoded and you will only see the shipping rates applicable to that address.

<h3 id="discount">DiscountActivity</h3>

After a shipping rate is selected, this activity allows the user to add discount codes or gift card codes to the order. It also shows a summary of the order, including the line item price, any discounts or gift cards used, the shipping charge, the taxes, and the total price.

<h3 id="checkout">CheckoutActivity</h3>

The final activity in the app flow. Allows the user to choose between either:

* a **native checkout** - payment info is hardcoded and the checkout is completed within the app, or
* a **web checkout** - the user enters their payment info and completes the checkout in a web browser.

## Customer Account
The overflow menu has two items for Customer Accounts, Log In and Orders.

### CustomerLoginActivity
This activity shows how to sign up a new customer, or log an existing customer in.

### CustomerOrderListActivity
This activity shows how to retrieve the orders associated with a customer account.


## Android Pay

To enable Android Pay in the sample application, enable Wallet support in the <kbd>AndroidManifest.xml</kbd>

```
<meta-data
  android:name="com.google.android.gms.wallet.api.enabled"
  android:value="true"/>
```

and add your Android Public key to the <kbd>shop.propertes</kbd> file

```
ANDROID_PAY_PUBLIC_KEY=<your public key>
```

By default the sample app will use the Androd Pay TEST environment.  The `Wallet` environment can be changed by editing the <kbd>SampleActivity</kbd> class:

```java

 // Use ENVIRONMENT_TEST for testing
    public static final int WALLET_ENVIRONMENT = WalletConstants.ENVIRONMENT_TEST;
    
```

*Note: You must install the Android Pay App on your test device, and add a valid credit card to Android Pay or you will not see Android Pay in the sample app*

Because the sample app uses the `Wallet` test environment you will be able to get `MaskedWallet` and `FullWallet` objects from Android Pay, but you will not be able to complete a `Checkout` as the payment information returned in the `FullWallet` will not be valid.  Your credit card will not be charged.

We recommend reading the great [Android Pay](https://developers.google.com/android-pay) documention before integrating Android Pay into your own application.

There are three Activities that are important for Android Pay:

### DiscountActivity 
This activity shows how to test for AndroidPay support, and how to retrieve a `MaskedWallet` which contains information about the User, including their address

### AndroidPayCheckoutActivity
This activity shows how to retrieve a FullWallet, and complete the `Checkout`

### 

