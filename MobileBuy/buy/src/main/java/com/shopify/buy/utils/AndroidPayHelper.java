package com.shopify.buy.utils;

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

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Base64;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.BooleanResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.identity.intents.model.CountrySpecification;
import com.google.android.gms.identity.intents.model.UserAddress;
import com.google.android.gms.wallet.Cart;
import com.google.android.gms.wallet.Cart.Builder;
import com.google.android.gms.wallet.FullWallet;
import com.google.android.gms.wallet.FullWalletRequest;
import com.google.android.gms.wallet.LineItem;
import com.google.android.gms.wallet.LineItem.Role;
import com.google.android.gms.wallet.MaskedWallet;
import com.google.android.gms.wallet.MaskedWalletRequest;
import com.google.android.gms.wallet.PaymentMethodTokenizationParameters;
import com.google.android.gms.wallet.PaymentMethodTokenizationType;
import com.google.android.gms.wallet.Wallet;
import com.shopify.buy.model.Address;
import com.shopify.buy.model.Checkout;
import com.shopify.buy.model.PaymentToken;
import com.shopify.buy.model.Shop;
import com.shopify.buy.model.TaxLine;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;


/**
 * Contains utility functions to help with Android Pay
 */

public final class AndroidPayHelper {

    private static final int FIRST_NAME_PART = 0;
    private static final int LAST_NAME_PART = 1;

    public static final int REQUEST_CODE_MASKED_WALLET = 500;
    public static final int REQUEST_CODE_CHANGE_MASKED_WALLET = 501;
    public static final int REQUEST_CODE_FULL_WALLET = 502;

    public static final String[] UNSUPPORTED_COUNTRIES_FOR_SHIPPING = {"MM", "SS", "GG", "IM", "KP", "SX", "SY", "IR", "BL", "BQ", "SD", "CU", "CW", "AX", "MF", "JE"};

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

        Builder builder = Cart.newBuilder();
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

        Builder builder = Cart.newBuilder();
        builder.setCurrencyCode(checkout.getCurrency());
        for (com.shopify.buy.model.LineItem shopifyLineItem : checkout.getLineItems()) {
            LineItem lineItem = createWalletLineItem(shopifyLineItem, checkout.getCurrency());
            builder.addLineItem(lineItem);
        }

        if (checkout.getTaxLines().size() != 0) {
            for (TaxLine taxLine : checkout.getTaxLines()) {
                LineItem lineItem = LineItem.newBuilder()
                    .setRole(LineItem.Role.TAX)
                    .setTotalPrice(taxLine.getPrice())
                    .build();
                builder.addLineItem(lineItem);
            }
        }

        if (checkout.getShippingRate() != null) {
            LineItem lineItem = LineItem.newBuilder()
                .setRole(Role.SHIPPING)
                .setTotalPrice(checkout.getShippingRate().getPrice())
                .build();
            builder.addLineItem(lineItem);
        }

        builder.setTotalPrice(checkout.getPaymentDue());
        return builder.build();
    }

    private static LineItem createWalletLineItem(com.shopify.buy.model.LineItem shopifyLineItem, String currencyCode) {
        BigDecimal lineTotal = new BigDecimal(shopifyLineItem.getPrice()).multiply(new BigDecimal(shopifyLineItem.getQuantity()));
        return LineItem.newBuilder().setQuantity(Long.toString(shopifyLineItem.getQuantity()))
            .setUnitPrice(shopifyLineItem.getPrice())
            .setTotalPrice(lineTotal.toString())
            .setDescription(shopifyLineItem.getTitle())
            .setCurrencyCode(currencyCode)
            .setRole(Role.REGULAR)
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
        if (!TextUtils.isEmpty(walletUserAddress.getAddress2())) {
            address2.append(walletUserAddress.getAddress2());
            address2.append(", ");
        }
        if (!TextUtils.isEmpty(walletUserAddress.getAddress3())) {
            address2.append(walletUserAddress.getAddress3());
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
     * Creates a Masked Wallet Request from a Shopify Checkout.
     *
     * @param merchantName        The merchant name to show on the Android Pay dialogs, not null or empty
     * @param checkout            The {@link Checkout} to use, not null.
     * @param publicKey           The Public Key to use, not empty.
     * @param phoneNumberRequired If true, the phone number will be required as part of the Shipping Address in Android Pay
     * @return A {@link MaskedWalletRequest}
     *
     * @deprecated Use {@link AndroidPayHelper#createMaskedWalletRequest(String, Checkout, String, boolean, Collection)}
     */
    @Deprecated
    public static MaskedWalletRequest createMaskedWalletRequest(String merchantName, Checkout checkout, String publicKey, boolean phoneNumberRequired) {
        return createMaskedWalletRequest(merchantName, checkout, publicKey, phoneNumberRequired, null);
    }

    /**
     * Creates a Masked Wallet Request from a Shopify Checkout.
     *
     * @param merchantName        The merchant name to show on the Android Pay dialogs, not null or empty
     * @param checkout            The {@link Checkout} to use, not null.
     * @param publicKey           The Public Key to use, not empty.
     * @param phoneNumberRequired If true, the phone number will be required as part of the Shipping Address in Android Pay
     * @param shipsToCountries    The list of <a href="http://www.iso.org/iso/home/standards/country_codes.htm">ISO 3166-2 Country Codes</a> to allow shipping to.
     *                            If null, the default Android Pay settings will be used.
     *                            A value of '*' can be used to indicate support for shipping to all available countries supported by AP.
     *                            The following country codes are not supported by Android Pay: MM, SS, GG, IM, KP, SX, SY, IR, BL, BQ, SD, CU, CW, AX, MF
     * @return A {@link MaskedWalletRequest}
     */
    public static MaskedWalletRequest createMaskedWalletRequest(String merchantName, Checkout checkout, String publicKey, boolean phoneNumberRequired, Collection<String> shipsToCountries) {
        if (TextUtils.isEmpty(merchantName)) {
            throw new IllegalArgumentException("merchantName cannot be empty");
        }

        if (checkout == null) {
            throw new NullPointerException("checkout cannot be null");
        }

        if (publicKey == null) {
            throw new IllegalArgumentException("publicKey cannot be empty");
        }

        // Create the parameters that will be used for encrypting Network Tokens.
        PaymentMethodTokenizationParameters parameters =
            PaymentMethodTokenizationParameters.newBuilder()
                .setPaymentMethodTokenizationType(PaymentMethodTokenizationType.NETWORK_TOKEN)
                .addParameter("publicKey", publicKey)
                .build();

        // Create a Wallet cart from our Checkout.
        Cart walletCart = createWalletCart(checkout);

        MaskedWalletRequest.Builder builder = MaskedWalletRequest.newBuilder();

        builder.setMerchantName(merchantName)
            .setPhoneNumberRequired(phoneNumberRequired)
            .setShippingAddressRequired(checkout.isRequiresShipping())
            .setCurrencyCode(checkout.getCurrency())
            .setEstimatedTotalPrice(checkout.getPaymentDue())
            .setPaymentMethodTokenizationParameters(parameters)
            .setCart(walletCart);

        if (shipsToCountries != null) {
            builder.addAllowedCountrySpecificationsForShipping(getCountrySpecifications(shipsToCountries));
        }

        return builder.build();
    }


    private static Collection<CountrySpecification> getCountrySpecifications(Collection<String> shipsToCountries) {
        Set<String> countryCodes = new HashSet<>(shipsToCountries);

        String wildcard = "*";

        if (countryCodes.remove(wildcard)) {

            // Get all ISO Country Codes
            List<String> wildCardCodes = new ArrayList<>(Arrays.asList(Locale.getISOCountries()));

            // Remove the Country Codes not supported by Android Pay
            wildCardCodes.removeAll(Arrays.asList(UNSUPPORTED_COUNTRIES_FOR_SHIPPING));

            // Add the remaining wildcard codes to the list
            countryCodes.addAll(wildCardCodes);
        }

        List<CountrySpecification> countrySpecifications = new ArrayList<>(countryCodes.size());

        for (String countryCode : countryCodes) {
            countrySpecifications.add(new CountrySpecification(countryCode));
        }

        return countrySpecifications;
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
     * Checks if new {@link MaskedWallet} response requires specified checkout to be updated or not.
     * If any of the address attributes (City, Country Code, Province Code, ZIP code) or email have changed for specified checkout
     * it requires to be updated.
     *
     * @param checkout     the {@link Checkout} to be verified
     * @param maskedWallet the {@link MaskedWallet} to check with
     * @return {@code true} if checkout should be updated, {@code false} otherwise
     */
    public static boolean isCheckoutUpdateRequired(final Checkout checkout, final MaskedWallet maskedWallet) {
        if (checkout == null) {
            throw new NullPointerException("checkout cannot be null");
        }

        if (maskedWallet == null) {
            throw new NullPointerException("maskedWallet cannot be null");
        }

        return isShippingAddressChanged(checkout, maskedWallet) || isBillingAddressChanged(checkout, maskedWallet) || isEmailChanged(checkout, maskedWallet);
    }

    private static boolean isEmailChanged(final Checkout checkout, final MaskedWallet maskedWallet) {
        return !TextUtils.equals(maskedWallet.getEmail(), checkout.getEmail());
    }

    private static boolean isShippingAddressChanged(final Checkout checkout, final MaskedWallet maskedWallet) {
        if (checkout.isRequiresShipping()) {
            Address shippingAddress = createShopifyAddress(maskedWallet.getBuyerShippingAddress());
            return !shippingAddress.locationsAreEqual(checkout.getShippingAddress());
        }
        return false;
    }

    private static boolean isBillingAddressChanged(final Checkout checkout, final MaskedWallet maskedWallet) {
        final Address billingAddress = createShopifyAddress(maskedWallet.getBuyerBillingAddress());
        return !billingAddress.locationsAreEqual(checkout.getBillingAddress());
    }


    /**
     * Checks to see if Android Pay is available on device.
     * <p>
     * It will check that:
     * 1) Play Services are available using {@link AndroidPayHelper#hasGooglePlayServices(Context)}
     * 2) The Android Pay application is installed on device, and user has setup a valid card for In App Purchase using {@link AndroidPayHelper#isReadyToPay(GoogleApiClient, AndroidPayReadyCallback)}
     *
     * @param context   The context to use.
     * @param apiClient The {@link GoogleApiClient}, not null
     * @param delegate  The {@link AndroidPayReadyCallback} delegate for receiving the result
     */
    public static void androidPayIsAvailable(final Context context, final GoogleApiClient apiClient, final AndroidPayReadyCallback delegate) {
        if (context == null) {
            throw new NullPointerException("context cannot be null");
        }

        if (apiClient == null) {
            throw new NullPointerException("apiClient cannot be null");
        }

        if (delegate == null) {
            throw new NullPointerException("delegate cannot be null");
        }

        // make sure that device supports SHA-256 and UTF-8 required by hashing android pay public key for payment token creation
        try {
            MessageDigest.getInstance("SHA-256");
            final byte[] bytes = "foo".getBytes("UTF-8");
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            // if not then android pay feature should be disabled
            delegate.onResult(false);
            return;
        }

        // Check to see if Google play is up to date
        if (!hasGooglePlayServices(context)) {
            delegate.onResult(false);
            return;
        }

        isReadyToPay(apiClient, delegate);
    }

    /**
     * Checks to see if Play Services are available on device
     *
     * @param context The context to use.
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
     * @param delegate  The {@link AndroidPayReadyCallback} delegate for receiving the result
     */
    public static void isReadyToPay(final GoogleApiClient apiClient, final AndroidPayReadyCallback delegate) {
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

    public static PaymentToken getAndroidPaymentToken(final FullWallet fullWallet, final String androidPayPublicKey) {
        if (fullWallet == null) {
            throw new IllegalArgumentException("fullWallet cannot be empty");
        }

        if (TextUtils.isEmpty(androidPayPublicKey)) {
            throw new IllegalArgumentException("androidPayPublicKey cannot be empty");
        }

        try {
            final MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            final byte[] digest = messageDigest.digest(androidPayPublicKey.getBytes("UTF-8"));
            return PaymentToken.createAndroidPayPaymentToken(fullWallet.getPaymentMethodToken().getToken(), Base64.encodeToString(digest, Base64.DEFAULT));
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            return null;
        }
    }


    /**
     * Interface for receiving results from {@link AndroidPayHelper#androidPayIsAvailable(Context, GoogleApiClient, AndroidPayReadyCallback)}
     */
    public interface AndroidPayReadyCallback {

        void onResult(boolean result);
    }


    private AndroidPayHelper() {
    }
}