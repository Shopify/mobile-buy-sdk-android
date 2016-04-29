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
import com.shopify.buy.dataprovider.BuyClientFactory;
import com.shopify.buy.model.Address;
import com.shopify.buy.model.Cart;
import com.shopify.buy.model.Checkout;
import com.shopify.buy.model.Collection;
import com.shopify.buy.model.CreditCard;
import com.shopify.buy.model.LineItem;
import com.shopify.buy.model.Product;
import com.shopify.buy.model.ShippingRate;
import com.shopify.buy.model.Shop;
import com.shopify.buy.ui.ProductDetailsBuilder;
import com.shopify.buy.ui.ProductDetailsTheme;
import com.shopify.buy.utils.AndroidPayHelper;
import com.shopify.sample.BuildConfig;
import com.shopify.sample.R;

import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Application class that maintains instances of BuyClient and Checkout for the lifetime of the app.
 */
public class SampleApplication extends Application {

    private BuyClient buyClient;
    private Checkout checkout;
    private Shop shop;

    private MaskedWallet maskedWallet;

    public static final String ANDROID_PAY_FLOW = "com.shopify.sample.androidpayflow";

    // Use ENVIRONMENT_TEST for testing
    public static final int WALLET_ENVIRONMENT = WalletConstants.ENVIRONMENT_SANDBOX;

    @Override
    public void onCreate() {
        super.onCreate();

        initializeBuyClient();
    }

    private void initializeBuyClient() {
        String shopUrl = BuildConfig.SHOP_DOMAIN;
        if (TextUtils.isEmpty(shopUrl)) {
            throw new IllegalArgumentException("You must populate the 'shop_url' entry in strings.xml, in the form '<myshop>.myshopify.com'");
        }

        String shopifyApiKey = BuildConfig.API_KEY;
        if (TextUtils.isEmpty(shopifyApiKey)) {
            throw new IllegalArgumentException("You must populate the 'shopify_api_key' entry in strings.xml");
        }

        String channelId = BuildConfig.CHANNEL_ID;
        if (TextUtils.isEmpty(channelId)) {
            throw new IllegalArgumentException("You must populate the 'channel_id' entry in the strings.xml");
        }

        String applicationName = getPackageName();

        /**
         * Create the BuyClient
         */
        buyClient = BuyClientFactory.getBuyClient(shopUrl, shopifyApiKey, channelId, applicationName);

        buyClient.getShop(new Callback<Shop>() {
            @Override
            public void success(Shop shop, Response response) {
                SampleApplication.this.shop = shop;
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(SampleApplication.this, R.string.shop_error, Toast.LENGTH_LONG).show();
            }
        });

        // Enable Android Pay if we have a public key
        String androidPayPublicKey = BuildConfig.ANDROID_PAY_PUBLIC_KEY;
        if (!TextUtils.isEmpty(androidPayPublicKey)) {
            buyClient.enableAndroidPay(androidPayPublicKey);
        }
    }

    public void getCollections(final Callback<List<Collection>> callback) {
        buyClient.getCollections(callback);
    }


    public void getAllProducts(final int page, final List<Product> allProducts, final Callback<List<Product>> callback) {

        buyClient.getProductPage(page, new Callback<List<Product>>() {
            @Override
            public void success(List<Product> products, Response response) {
                if (products.size() > 0) {
                    allProducts.addAll(products);
                    getAllProducts(page + 1, allProducts, callback);
                } else {
                    callback.success(allProducts, response);
                }
            }

            @Override
            public void failure(RetrofitError error) {
                callback.failure(error);
            }
        });
    }

    public void getProducts(String collectionId, Callback<List<Product>> callback) {
        // For this sample app, we'll just fetch the first page of products in the collection
        buyClient.getProducts(1, collectionId, callback);
    }

    /**
     * Create a new checkout with the selected product. For convenience in the sample app we will hardcode the user's shipping address.
     * The shipping rates fetched in ShippingRateListActivity will be for this address.
     * <p/>
     * For the Android Pay Checkout, we will replace this with the address and email returned in the {@link MaskedWallet}
     *
     * @param product
     * @param callback
     */
    public void createCheckout(final Product product, final Callback<Checkout> callback) {
        Cart cart = new Cart();
        cart.addVariant(product.getVariants().get(0));

        checkout = new Checkout(cart);

        Address address = new Address();
        address.setFirstName("Dinosaur");
        address.setLastName("Banana");
        address.setAddress1("421 8th Ave");
        address.setCity("New York");
        address.setProvince("NY");
        address.setZip("10001");
        address.setCountryCode("US");
        checkout.setShippingAddress(address);

        checkout.setEmail("something@somehost.com");

        checkout.setWebReturnToUrl(getString(R.string.web_return_to_url));
        checkout.setWebReturnToLabel(getString(R.string.web_return_to_label));

        buyClient.createCheckout(checkout, wrapCheckoutCallback(callback));
    }

    /**
     * Update a checkout.
     */
    public void updateCheckout(final Checkout checkout, final Callback<Checkout> callback) {
        buyClient.updateCheckout(checkout, wrapCheckoutCallback(callback));
    }

    public void updateCheckout(final Checkout checkout, MaskedWallet maskedWallet, final Callback<Checkout> callback) {
        // Update the checkout with the Address information in the Masked Wallet
        AndroidPayHelper.updateCheckoutAddressAndEmail(checkout, maskedWallet);
        updateCheckout(checkout, callback);
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

        uri.appendQueryParameter("channel_id", buyClient.getChannelId());
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

    public Checkout getCheckout() {
        return checkout;
    }

    public BuyClient getBuyClient() {
        return buyClient;
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

    public void storeCreditCard(final CreditCard card, final Callback<Checkout> callback) {
        checkout.setBillingAddress(checkout.getShippingAddress());
        buyClient.storeCreditCard(card, checkout, wrapCheckoutCallback(callback));
    }

    public void completeCheckout(final Callback<Checkout> callback) {
        buyClient.completeCheckout(checkout, wrapCheckoutCallback(callback));
    }

    public void completeCheckout(FullWallet fullWallet, final Callback<Checkout> callback) {
        buyClient.completeCheckout(fullWallet.getPaymentMethodToken().getToken(), checkout, wrapCheckoutCallback(callback));
    }

    public void getCheckoutCompletionStatus(final Callback<Boolean> callback) {
        buyClient.getCheckoutCompletionStatus(checkout, callback);
    }

    public void launchProductDetailsActivity(Activity activity, Product product, ProductDetailsTheme theme) {
        ProductDetailsBuilder builder = new ProductDetailsBuilder(this, buyClient);
        Intent intent = builder.setShopDomain(buyClient.getShopDomain())
                .setProduct(product)
                .setTheme(theme)
                .setShop(shop)
                .setWebReturnToUrl(getString(R.string.web_return_to_url))
                .setWebReturnToLabel(getString(R.string.web_return_to_label))
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
            public void success(Checkout checkout, Response response) {
                SampleApplication.this.checkout = checkout;
                callback.success(checkout, response);
            }

            @Override
            public void failure(RetrofitError error) {
                callback.failure(error);
            }
        };
    }

}
