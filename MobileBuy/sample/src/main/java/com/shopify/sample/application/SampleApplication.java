/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 Shopify Inc.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.shopify.sample.application;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.widget.Toast;

import com.google.android.gms.wallet.FullWallet;
import com.google.android.gms.wallet.MaskedWallet;
import com.google.android.gms.wallet.WalletConstants;
import com.shopify.buy.dataprovider.BuyClient;
import com.shopify.buy.dataprovider.BuyClientBuilder;
import com.shopify.buy.dataprovider.BuyClientError;
import com.shopify.buy.dataprovider.Callback;
import com.shopify.buy.model.Address;
import com.shopify.buy.model.Cart;
import com.shopify.buy.model.Checkout;
import com.shopify.buy.model.Collection;
import com.shopify.buy.model.CreditCard;
import com.shopify.buy.model.Customer;
import com.shopify.buy.model.LineItem;
import com.shopify.buy.model.PaymentToken;
import com.shopify.buy.model.Product;
import com.shopify.buy.model.ShippingRate;
import com.shopify.buy.model.Shop;
import com.shopify.buy.utils.AndroidPayHelper;
import com.shopify.sample.BuildConfig;
import com.shopify.sample.R;
import com.shopify.sample.ui.ProductDetailsBuilder;
import com.shopify.sample.ui.ProductDetailsTheme;

import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.logging.HttpLoggingInterceptor;

/**
 * Application class that maintains instances of BuyClient and Checkout for the lifetime of the app.
 */
public class SampleApplication extends Application {

  private static final String SHOP_PROPERTIES_INSTRUCTION =
      "\n\tAdd your shop credentials to a shop.properties file in the main app folder (e.g. 'app/shop.properties'). Include these keys:\n" +
          "\t\tSHOP_DOMAIN=<myshop>.myshopify.com\n" +
          "\t\tAPI_KEY=0123456789abcdefghijklmnopqrstuvw\n";

  private static SampleApplication instance;

  private static Customer customer;

  public static BuyClient getBuyClient() {
    return instance.buyClient;
  }

  public static Customer getCustomer() {
    return customer;
  }

  public static void setCustomer(Customer customer) {
    SampleApplication.customer = customer;
  }

  private BuyClient buyClient;
  private Checkout checkout;
  private PaymentToken paymentToken;
  private Shop shop;

  private MaskedWallet maskedWallet;

  public static final String ANDROID_PAY_FLOW = "com.shopify.sample.androidpayflow";

  // Use ENVIRONMENT_TEST for testing
  public static final int WALLET_ENVIRONMENT = WalletConstants.ENVIRONMENT_TEST;

  @Override
  public void onCreate() {
    super.onCreate();

    instance = this;

    initializeBuyClient();
  }

  private void initializeBuyClient() {
    String shopUrl = BuildConfig.SHOP_DOMAIN;
    if (TextUtils.isEmpty(shopUrl)) {
      throw new IllegalArgumentException(SHOP_PROPERTIES_INSTRUCTION + "You must add 'SHOP_DOMAIN' entry in app/shop.properties, in the form '<myshop>.myshopify.com'");
    }

    String shopifyApiKey = BuildConfig.API_KEY;
    if (TextUtils.isEmpty(shopifyApiKey)) {
      throw new IllegalArgumentException(SHOP_PROPERTIES_INSTRUCTION + "You must populate the 'API_KEY' entry in app/shop.properties");
    }

    String shopifyAppId = BuildConfig.APP_ID;
    if (TextUtils.isEmpty(shopifyAppId)) {
      throw new IllegalArgumentException(SHOP_PROPERTIES_INSTRUCTION + "You must populate the 'APP_ID' entry in app/shop.properties");
    }

    String applicationName = getPackageName();

    final HttpLoggingInterceptor logging = new HttpLoggingInterceptor().setLevel(BuildConfig.OKHTTP_LOG_LEVEL);

    /**
     * Create the BuyClient
     */

    buyClient = new BuyClientBuilder()
        .shopDomain(shopUrl)
        .apiKey(shopifyApiKey)
        .appId(shopifyAppId)
        .applicationName(applicationName)
        .httpInterceptors(logging)
        .networkRequestRetryPolicy(3, TimeUnit.MILLISECONDS.toMillis(200), 1.5f)
        .build();

    buyClient.getShop(new Callback<Shop>() {
      @Override
      public void success(Shop shop) {
        SampleApplication.this.shop = shop;
      }

      @Override
      public void failure(BuyClientError error) {
        Toast.makeText(SampleApplication.this, R.string.shop_error, Toast.LENGTH_LONG).show();
      }
    });
  }

  public void getCollections(final Callback<List<Collection>> callback) {
    buyClient.getCollections(1, callback);
  }


  public void getAllProducts(final int page, final List<Product> allProducts, final Callback<List<Product>> callback) {

    buyClient.getProducts(page, new Callback<List<Product>>() {
      @Override
      public void success(List<Product> products) {
        if (products.size() > 0) {
          allProducts.addAll(products);
          getAllProducts(page + 1, allProducts, callback);
        } else {
          callback.success(allProducts);
        }
      }

      @Override
      public void failure(BuyClientError error) {
        callback.failure(error);
      }
    });
  }

  public void getProducts(Long collectionId, Callback<List<Product>> callback) {
    // For this sample app, we'll just fetch the first page of products in the collection
    buyClient.getProducts(1, collectionId, null, null, callback);
  }

  /**
   * Create a new checkout with the selected product. For convenience in the sample app we will hardcode the user's shipping address.
   * The shipping rates fetched in ShippingRateListActivity will be for this address.
   *
   * For the Android Pay Checkout, we will replace this with the address and email returned in the {@link MaskedWallet}
   *
   * @param product
   * @param callback
   */
  public void createCheckout(final Product product, final Callback<Checkout> callback) {
    Cart cart = new Cart();
    cart.addVariant(product.getVariants().get(0));

    checkout = new Checkout(cart);
    checkout.setWebReturnToLabel(getString(R.string.web_return_to_label));
    checkout.setWebReturnToUrl(getString(R.string.web_return_to_url));

    // if we have logged in customer use customer email instead of hardcoded one
    if (customer != null) {
      checkout.setEmail(customer.getEmail());
    } else {
      checkout.setEmail("something@somehost.com");
    }

    // the same for shipping address if we have logged in customer use customer default shipping address instead of hardcoded one
    if (customer != null && customer.getDefaultAddress() != null) {
      checkout.setShippingAddress(customer.getDefaultAddress());
      checkout.setBillingAddress(customer.getDefaultAddress());
    } else {
      final Address address = new Address();
      address.setFirstName("Dinosaur");
      address.setLastName("Banana");
      address.setAddress1("421 8th Ave");
      address.setCity("New York");
      address.setProvinceCode("NY");
      address.setZip("10001");
      address.setCountryCode("US");
      checkout.setShippingAddress(address);
      checkout.setBillingAddress(address);
    }

    checkout.setWebReturnToUrl(getString(R.string.web_return_to_url));
    checkout.setWebReturnToLabel(getString(R.string.web_return_to_label));

    buyClient.createCheckout(checkout, wrapCheckoutCallback(callback));
  }

  public String getCartPermalink() {
    Uri.Builder uri = new Uri.Builder();
    uri.scheme("http").path(buyClient.getShopDomain()).appendPath("cart");

    StringBuilder lineItemsStr = new StringBuilder();
    String prefix = "";
    for (LineItem lineItem : checkout.getLineItems()) {
      lineItemsStr.append(prefix);
      lineItemsStr.append(Long.toString(lineItem.getVariantId()));
      lineItemsStr.append(":");
      lineItemsStr.append(Long.toString(lineItem.getQuantity()));
      prefix = ",";
    }
    uri.appendPath(lineItemsStr.toString());

    uri.appendQueryParameter("channel", "mobile_app");
    uri.appendQueryParameter("checkout[email]", "email@domain.com");

    uri.appendQueryParameter("checkout[shipping_address][address1]", "Cart Permalink");
    uri.appendQueryParameter("checkout[shipping_address][city]", "Toronto");
    uri.appendQueryParameter("checkout[shipping_address][company]", "Shopify");
    uri.appendQueryParameter("checkout[shipping_address][first_name]", "Dinosaur");
    uri.appendQueryParameter("checkout[shipping_address][last_name]", "Banana");
    uri.appendQueryParameter("checkout[shipping_address][phone]", "416-555-1234");
    uri.appendQueryParameter("checkout[shipping_address][country]", "Canada");
    uri.appendQueryParameter("checkout[shipping_address][province]", "Ontario");
    uri.appendQueryParameter("checkout[shipping_address][zip]", "M5V2J4");

    return uri.build().toString();
  }

  /**
   * Update a checkout.
   */
  public void updateCheckout(final Checkout checkout, final Callback<Checkout> callback) {
    buyClient.updateCheckout(checkout, wrapCheckoutCallback(callback));
  }

  public void updateCheckout(final Checkout checkout, MaskedWallet maskedWallet, final Callback<Checkout> callback) {
    // Update the checkout with the Address information in the Masked Wallet
    final Checkout updateCheckout = new Checkout(checkout.getToken());
    updateCheckout.setShippingAddress(AndroidPayHelper.createShopifyAddress(maskedWallet.getBuyerShippingAddress()));
    updateCheckout.setBillingAddress(AndroidPayHelper.createShopifyAddress(maskedWallet.getBuyerBillingAddress()));
    updateCheckout.setEmail(maskedWallet.getEmail());
    updateCheckout(updateCheckout, callback);
  }

  public Checkout getCheckout() {
    return checkout;
  }

  public Shop getShop() {
    return shop;
  }

  public MaskedWallet getMaskedWallet() {
    return maskedWallet;
  }

  public void setMaskedWallet(MaskedWallet maskedWallet) {
    this.maskedWallet = maskedWallet;
  }

  public void getShippingRates(final Callback<List<ShippingRate>> callback) {
    buyClient.getShippingRates(checkout.getToken(), callback);
  }

  public void setShippingRate(ShippingRate shippingRate, final Callback<Checkout> callback) {
    checkout.setShippingRate(shippingRate);
    buyClient.updateCheckout(checkout, wrapCheckoutCallback(callback));
  }

  public void setDiscountCode(final String code, final Callback<Checkout> callback) {
    checkout.setDiscountCode(code);
    buyClient.updateCheckout(checkout, wrapCheckoutCallback(callback));
  }

  public void addGiftCard(final String code, final Callback<Checkout> callback) {
    buyClient.applyGiftCard(code, checkout, wrapCheckoutCallback(callback));
  }

  public void storeCreditCard(final CreditCard card, final Callback<PaymentToken> callback) {
    buyClient.storeCreditCard(card, checkout, new Callback<PaymentToken>() {
      @Override
      public void success(PaymentToken body) {
        SampleApplication.this.paymentToken = body;
        callback.success(body);
      }

      @Override
      public void failure(BuyClientError error) {
        callback.failure(error);
      }
    });
  }

  public void completeCheckout(final Callback<Checkout> callback) {
    buyClient.completeCheckout(paymentToken, checkout.getToken(), wrapCheckoutCallback(callback));
  }

  public void completeCheckout(FullWallet fullWallet, final Callback<Checkout> callback) {
    paymentToken = AndroidPayHelper.getAndroidPaymentToken(fullWallet, BuildConfig.ANDROID_PAY_PUBLIC_KEY);
    buyClient.completeCheckout(paymentToken, checkout.getToken(), wrapCheckoutCallback(callback));
  }

  public void launchProductDetailsActivity(Activity activity, Product product, ProductDetailsTheme theme) {
    ProductDetailsBuilder builder = new ProductDetailsBuilder(this, buyClient);
    Intent intent = builder.setShopDomain(buyClient.getShopDomain())
        .setProduct(product)
        .setTheme(theme)
        .setShop(shop)
        .build();
    activity.startActivityForResult(intent, 1);
  }

  /**
   * Wraps the callbacks that are provided by the activities so that the checkout ivar is always up to date.
   *
   * @param callback
   * @return
   */
  private Callback<Checkout> wrapCheckoutCallback(final Callback<Checkout> callback) {
    return new Callback<Checkout>() {
      @Override
      public void success(Checkout checkout) {
        SampleApplication.this.checkout = checkout;
        callback.success(checkout);
      }

      @Override
      public void failure(BuyClientError error) {
        callback.failure(error);
      }
    };
  }

}