
/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 Shopify
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
 *
 */

package com.shopify.buy.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.BooleanResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.identity.intents.model.UserAddress;
import com.google.android.gms.wallet.Cart;
import com.google.android.gms.wallet.FullWalletRequest;
import com.google.android.gms.wallet.LineItem;
import com.google.android.gms.wallet.MaskedWallet;
import com.google.android.gms.wallet.MaskedWalletRequest;
import com.google.android.gms.wallet.PaymentMethodTokenizationParameters;
import com.google.android.gms.wallet.PaymentMethodTokenizationType;
import com.google.android.gms.wallet.Wallet;
import com.shopify.buy.dataprovider.BuyClient;
import com.shopify.buy.model.Address;
import com.shopify.buy.model.Checkout;
import com.shopify.buy.model.Shop;

import java.math.BigDecimal;

import retrofit.Callback;


/**
 * Contains utility functions to help with Android Pay
 */

public class AndroidPayHelper {

    private static final int FIRST_NAME_PART = 0;
    private static final int LAST_NAME_PART = 1;

    public static final int REQUEST_CODE_MASKED_WALLET = 500;
    public static final int REQUEST_CODE_CHANGE_MASKED_WALLET = 501;
    public static final int REQUEST_CODE_FULL_WALLET = 502;

    /**
     * Creates a Wallet Cart from a Shopify Cart for use in Wallet Requests.
     *
     * @param shopifyCart The {@link com.shopify.buy.model.Cart} to convert, not Null.
     * @param shop        The {@link Shop}, not null.
     * @return A {@link Cart}
     */
    public static Cart createWalletCart(com.shopify.buy.model.Cart shopifyCart, Shop shop) {
        if (shopifyCart == null) {
            throw new NullPointerException("shopifyCart cannot be null");
        }
        if (shop == null) {
            throw new NullPointerException("shop cannot be null");
        }

        Cart.Builder builder = Cart.newBuilder();
        builder.setCurrencyCode(shop.getCurrency());
        BigDecimal totalPrice = new BigDecimal(0);
        for (com.shopify.buy.model.LineItem shopifyLineItem : shopifyCart.getLineItems()) {
            LineItem lineItem = createWalletLineItem(shopifyLineItem, shop.getCurrency());
            totalPrice = totalPrice.add(new BigDecimal(lineItem.getTotalPrice()));
            builder.addLineItem(lineItem);
        }
        builder.setTotalPrice(totalPrice.toString());
        return builder.build();
    }

    /**
     * Creates a {@link Cart} from a {@link Checkout} for use in Wallet Requests. This is the preferred way to create a Wallet for use in {@link FullWalletRequest}
     *
     * @param checkout The Checkout to convert, not null.
     * @return A {@link Cart}
     */
    public static Cart createWalletCart(Checkout checkout) {
        if (checkout == null) {
            throw new NullPointerException("checkout cannot be null");
        }

        Cart.Builder builder = Cart.newBuilder();
        builder.setCurrencyCode(checkout.getCurrency());
        for (com.shopify.buy.model.LineItem shopifyLineItem : checkout.getLineItems()) {
            LineItem lineItem = createWalletLineItem(shopifyLineItem, checkout.getCurrency());
            builder.addLineItem(lineItem);
        }
        builder.setTotalPrice(checkout.getTotalPrice());
        return builder.build();
    }

    private static LineItem createWalletLineItem(com.shopify.buy.model.LineItem shopifyLineItem, String currencyCode) {
        BigDecimal lineTotal = new BigDecimal(shopifyLineItem.getPrice()).multiply(new BigDecimal(shopifyLineItem.getQuantity()));
        return LineItem.newBuilder().setQuantity(Long.toString(shopifyLineItem.getQuantity()))
                .setUnitPrice(shopifyLineItem.getPrice())
                .setTotalPrice(lineTotal.toString())
                .setDescription(shopifyLineItem.getTitle())
                .setCurrencyCode(currencyCode)
                .build();
    }

    /**
     * Creates a Shopify Address from a Wallet Address
     *
     * @param walletUserAddress {@link UserAddress} to convert, not null
     * @return {@link Address}
     */
    public static Address createShopifyAddress(UserAddress walletUserAddress) {
        if (walletUserAddress == null) {
            throw new NullPointerException("walletUserAddress cannot be null");
        }

        Address address = new Address();

        String[] nameParts = extractFirstAndLastNames(walletUserAddress.getName());
        address.setFirstName(nameParts[FIRST_NAME_PART]);
        address.setLastName(nameParts[LAST_NAME_PART]);

        address.setAddress1(walletUserAddress.getAddress1());

        // Google Wallet address has 5 address lines.
        // Convert to 2 address lines
        StringBuilder address2 = new StringBuilder();
        address2.append(walletUserAddress.getAddress2());

        if (!TextUtils.isEmpty(walletUserAddress.getAddress3())) {
            address2.append(walletUserAddress.getAddress3());
            address2.append(", ");
        }
        if (!TextUtils.isEmpty(walletUserAddress.getAddress4())) {
            address2.append(walletUserAddress.getAddress4());
            address2.append(", ");
        }
        if (!TextUtils.isEmpty(walletUserAddress.getAddress4())) {
            address2.append(walletUserAddress.getAddress4());
            address2.append(", ");
        }
        if (!TextUtils.isEmpty(walletUserAddress.getAddress5())) {
            address2.append(walletUserAddress.getAddress5());
        }

        address.setAddress2(address2.toString());

        address.setZip(walletUserAddress.getPostalCode());
        address.setCity(walletUserAddress.getLocality());
        address.setCountryCode(walletUserAddress.getCountryCode());
        address.setProvinceCode(walletUserAddress.getAdministrativeArea());
        address.setPhone(walletUserAddress.getPhoneNumber());
        return address;
    }

    private static String[] extractFirstAndLastNames(String name) {
        String[] firstAndLastName = new String[2];
        String[] nameParts = name.split(" ");
        if (nameParts.length > 0) {
            firstAndLastName[FIRST_NAME_PART] = (nameParts[FIRST_NAME_PART]);
        }
        int nameIndex = 1;
        String lastName = "";
        while (nameParts.length > nameIndex) {
            lastName += nameParts[nameIndex++];
        }
        firstAndLastName[LAST_NAME_PART] = lastName.trim();
        return firstAndLastName;
    }

    /**
     * Creates a Masked Wallet Request from a Shopify Checkout
     *
     * @param merchantName The merchant name to show on the Android Pay dialogs, not null or empty
     * @param checkout     The {@link Checkout} to use, not null.
     * @param buyClient    The {@link BuyClient} to use, not null.  Must have previously had Android Pay enabled via {@link BuyClient#enableAndroidPay(String)}
     * @param phoneNumberRequired If true, the phone number will be required as part of the Shipping Address in Android Pay
     * @return A {@link MaskedWalletRequest}
     */
    public static MaskedWalletRequest createMaskedWalletRequest(String merchantName, Checkout checkout, BuyClient buyClient, boolean phoneNumberRequired) {
        if (checkout == null) {
            throw new NullPointerException("checkout cannot be null");
        }

        if (buyClient == null) {
            throw new NullPointerException("buyClient cannot be null");
        }

        if (TextUtils.isEmpty(merchantName)) {
            throw new IllegalArgumentException("merchantName cannot be empty");
        }

        if (!buyClient.androidPayIsEnabled()) {
            throw new IllegalArgumentException("buyClient must have Android Pay enabled");
        }

        // Create the parameters that will be used for encrypting Network Tokens.
        PaymentMethodTokenizationParameters parameters =
                PaymentMethodTokenizationParameters.newBuilder()
                        .setPaymentMethodTokenizationType(PaymentMethodTokenizationType.NETWORK_TOKEN)
                        .addParameter("publicKey", buyClient.getAndroidPayPublicKey())
                        .build();

        // Create a Wallet cart from our Checkout.
        Cart walletCart = createWalletCart(checkout);

        // These settings should be updated to reflect the requirements of the app.
        // The merchant name will be shown on the top of the Android Pay dialogs
        return MaskedWalletRequest.newBuilder()
                .setMerchantName(merchantName)
                .setPhoneNumberRequired(phoneNumberRequired)
                .setShippingAddressRequired(checkout.isRequiresShipping())
                .setCurrencyCode(checkout.getCurrency())
                .setEstimatedTotalPrice(checkout.getTotalPrice())
                .setPaymentMethodTokenizationParameters(parameters)
                .setCart(walletCart)
                .setPhoneNumberRequired(phoneNumberRequired)
                .build();
    }

    /**
     * Creates a Full Wallet Request from a Shopify Checkout
     *
     * @param checkout     The {@link Checkout} to use, not null.
     * @param maskedWallet A {@link MaskedWallet} returned from a previous {@link MaskedWalletRequest} sent to Android Pay
     * @return A {@link FullWalletRequest} for use in retrieving a {@link com.google.android.gms.wallet.FullWallet}
     */
    public static FullWalletRequest createFullWalletRequest(Checkout checkout, MaskedWallet maskedWallet) {
        if (checkout == null) {
            throw new NullPointerException("checkout cannot be null");
        }

        if (maskedWallet == null) {
            throw new NullPointerException("maskedWallet cannot be null");
        }

        return FullWalletRequest.newBuilder()
                .setGoogleTransactionId(maskedWallet.getGoogleTransactionId())
                .setCart(createWalletCart(checkout))
                .build();
    }

    /**
     * Will update the local checkout with the Addresses and email from a Masked Wallet.
     * If any of the City, Country Code, Province Code, or ZIP code have changed any shipping rate previously set
     * on the Checkout will be invalid and set to null. The checkout should be updated on Shopify using {@link BuyClient#updateCheckout(Checkout, Callback)},
     * and the shipping rate will need to be fetched again using {@link BuyClient#getShippingRates(String, Callback)}
     *
     * @param checkout     The {@link Checkout} to update
     * @param maskedWallet The {@link MaskedWallet} to use the address from
     */
    public static void updateCheckoutAddressAndEmail(Checkout checkout, MaskedWallet maskedWallet) {
        if (checkout == null) {
            throw new NullPointerException("checkout cannot be null");
        }

        if (maskedWallet == null) {
            throw new NullPointerException("maskedWallet cannot be null");
        }

        Address shippingAddress = createShopifyAddress(maskedWallet.getBuyerShippingAddress());
        Address billingAddress = createShopifyAddress(maskedWallet.getBuyerBillingAddress());

        // If the location has changed, we need to invalidate the shipping address
        if (!shippingAddress.locationsAreEqual(checkout.getShippingAddress())) {
            checkout.setShippingRate(null);
        }

        checkout.setShippingAddress(shippingAddress);
        checkout.setBillingAddress(billingAddress);
        checkout.setEmail(maskedWallet.getEmail());
    }

    /**
     * Checks to see if Android Pay is available on device.
     *
     * It will check that:
     * 1) Play Services are available using {@link AndroidPayHelper#hasGooglePlayServices(Context)}
     * 2) The BuyClient has Android Pay enabled using {@link BuyClient#androidPayIsEnabled()}
     * 3) The Android Pay application is installed on device, and user has setup a valid card for In App Purchase using {@link AndroidPayHelper#isReadyToPay(GoogleApiClient, AndroidPayAvailableResponse)}
     *
     * @param context
     * @param buyClient The {@link BuyClient} to use, not null
     * @param apiClient The {@link GoogleApiClient}, not null
     * @param delegate  The {@link AndroidPayAvailableResponse} delegate for receiving the result
     */
    public static void androidPayIsAvailable(final Context context, final BuyClient buyClient, final GoogleApiClient apiClient, final AndroidPayAvailableResponse delegate) {
        if (context == null) {
            throw new NullPointerException("context cannot be null");
        }

        if (buyClient == null) {
            throw new NullPointerException("buyClient cannot be null");
        }

        if (apiClient == null) {
            throw new NullPointerException("apiClient cannot be null");
        }

        if (delegate == null) {
            throw new NullPointerException("delegate cannot be null");
        }

        // Check to see if Google play is up to date and the buyclient has android pay enabled
        if (!hasGooglePlayServices(context) || !buyClient.androidPayIsEnabled()) {
            delegate.onResult(false);
            return;
        }

        isReadyToPay(apiClient, delegate);
    }

    /**
     * Checks to see if Play Services are available on device
     *
     * @param context
     * @return true if Play Services are available
     */
    public static boolean hasGooglePlayServices(final Context context) {
        if (context == null) {
            throw new NullPointerException("context cannot be null");
        }

        GoogleApiAvailability googleAPI = GoogleApiAvailability.getInstance();
        int result = googleAPI.isGooglePlayServicesAvailable(context);

        if (result != ConnectionResult.SUCCESS) {
            return false;
        }

        return true;
    }


    /**
     * Checks to see if the Android Pay App is installed on device and has a valid card for In App Purchase using {@link com.google.android.gms.wallet.Payments#isReadyToPay(GoogleApiClient)}
     *
     * @param apiClient The {@link GoogleApiClient}, not null
     * @param delegate  The {@link AndroidPayAvailableResponse} delegate for receiving the result
     */
    public static void isReadyToPay(final GoogleApiClient apiClient, final AndroidPayAvailableResponse delegate) {
        if (apiClient == null) {
            throw new NullPointerException("apiClient cannot be null");
        }

        if (delegate == null) {
            throw new NullPointerException("delegate cannot be null");
        }

        // Check that the user has installed and setup the Android Pay app on their device
        Wallet.Payments.isReadyToPay(apiClient).setResultCallback(
                new ResultCallback<BooleanResult>() {
                    @Override
                    public void onResult(@NonNull BooleanResult booleanResult) {

                        if (booleanResult.getStatus().isSuccess()) {
                            delegate.onResult(booleanResult.getValue());
                        } else {
                            // We could not make the call so must assume it is not available
                            delegate.onResult(false);
                        }
                    }
                });
    }


    /**
     * Interface for receiving results from {@link AndroidPayHelper#androidPayIsAvailable(Context, BuyClient, GoogleApiClient, AndroidPayAvailableResponse)}
     */
    public interface AndroidPayAvailableResponse {

        void onResult(boolean result);
    }


}