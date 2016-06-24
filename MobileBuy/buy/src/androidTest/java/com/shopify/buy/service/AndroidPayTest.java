/*
 *
 * The MIT License (MIT)
 *
 * Copyright (c) 2016 Shopify Inc.
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

package com.shopify.buy.service;


import android.support.test.runner.AndroidJUnit4;
import android.util.Base64;

import com.google.android.gms.identity.intents.model.UserAddress;
import com.google.android.gms.wallet.Cart;
import com.google.android.gms.wallet.FullWallet;
import com.google.android.gms.wallet.FullWalletRequest;
import com.google.android.gms.wallet.InstrumentInfo;
import com.google.android.gms.wallet.MaskedWallet;
import com.google.android.gms.wallet.MaskedWalletRequest;
import com.google.android.gms.wallet.PaymentMethodToken;
import com.google.android.gms.wallet.ProxyCard;
import com.shopify.buy.extensions.ShopifyAndroidTestCase;
import com.shopify.buy.model.Address;
import com.shopify.buy.model.Checkout;
import com.shopify.buy.model.LineItem;
import com.shopify.buy.model.PaymentToken;
import com.shopify.buy.model.Shop;
import com.shopify.buy.utils.AndroidPayHelper;
import com.shopify.buy.utils.CurrencyFormatter;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.security.MessageDigest;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.Locale;

@RunWith(AndroidJUnit4.class)
public class AndroidPayTest extends ShopifyAndroidTestCase {

    private final static String CURRENCY = "CAD";

    public static final float LINE_ITEM_PRICE_1 = 100.05f;
    public static final long LINE_ITEM_QTY_1 = 3;
    public static final float LINE_ITEM_TOTAL_PRICE_1 = LINE_ITEM_PRICE_1 * LINE_ITEM_QTY_1;

    public static final float LINE_ITEM_PRICE_2 = 39.99f;
    public static final long LINE_ITEM_QTY_2 = 7;
    public static final float LINE_ITEM_TOTAL_PRICE_2 = LINE_ITEM_PRICE_2 * LINE_ITEM_QTY_2;

    private NumberFormat currencyFormatter;

    private Shop shop;

    private LineItem lineItem1;

    private LineItem lineItem2;

    @Before
    @Override
    public void setUp() throws Exception {
        super.setUp();

        currencyFormatter = CurrencyFormatter.getFormatter(Locale.CANADA, CURRENCY, false, false, true);

        shop = Mockito.mock(Shop.class);
        Mockito.when(shop.getCurrency()).thenReturn("CAD");

        lineItem1 = Mockito.mock(LineItem.class);
        Mockito.when(lineItem1.getTitle()).thenReturn("lineItem1");
        Mockito.when(lineItem1.getPrice()).thenReturn(String.valueOf(LINE_ITEM_PRICE_1));
        Mockito.when(lineItem1.getQuantity()).thenReturn(LINE_ITEM_QTY_1);

        lineItem2 = Mockito.mock(LineItem.class);
        Mockito.when(lineItem2.getTitle()).thenReturn("lineItem2");
        Mockito.when(lineItem2.getPrice()).thenReturn(String.valueOf(LINE_ITEM_PRICE_2));
        Mockito.when(lineItem2.getQuantity()).thenReturn(LINE_ITEM_QTY_2);
    }

    @Test
    public void testCreateWalletCartFromCart() {
        final com.shopify.buy.model.Cart shopifyCart = Mockito.mock(com.shopify.buy.model.Cart.class);
        Mockito.doReturn(Arrays.asList(lineItem1, lineItem2)).when(shopifyCart).getLineItems();

        final Cart cart = AndroidPayHelper.createWalletCart(shopifyCart, shop);
        assertWalletCart(cart);
    }

    @Test
    public void testCreateWalletCartFromCheckout() {
        final Checkout checkout = Mockito.mock(Checkout.class);
        Mockito.doReturn(Arrays.asList(lineItem1, lineItem2)).when(checkout).getLineItems();
        Mockito.doReturn(CURRENCY).when(checkout).getCurrency();
        Mockito.doReturn(currencyFormatter.format(LINE_ITEM_TOTAL_PRICE_1 + LINE_ITEM_TOTAL_PRICE_2)).when(checkout).getPaymentDue();

        final Cart cart = AndroidPayHelper.createWalletCart(checkout);
        assertWalletCart(cart);
    }

    @Test
    public void testCreateShopifyAddress() throws Exception {
        final UserAddress userAddress = createWalletUserAddress();

        final Address shopifyAddress = AndroidPayHelper.createShopifyAddress(userAddress);
        Assert.assertEquals(userAddress.getName().split(" ")[0], shopifyAddress.getFirstName());
        Assert.assertEquals(userAddress.getName().split(" ")[1], shopifyAddress.getLastName());
        Assert.assertEquals(userAddress.getAddress1(), shopifyAddress.getAddress1());
        Assert.assertEquals(userAddress.getAddress2() + ", " + userAddress.getAddress3() + ", " + userAddress.getAddress4() + ", " + userAddress.getAddress5(), shopifyAddress.getAddress2());
        Assert.assertEquals(userAddress.getPostalCode(), shopifyAddress.getZip());
        Assert.assertEquals(userAddress.getLocality(), shopifyAddress.getCity());
        Assert.assertEquals(userAddress.getCountryCode(), shopifyAddress.getCountryCode());
        Assert.assertEquals(userAddress.getAdministrativeArea(), shopifyAddress.getProvinceCode());
        Assert.assertEquals(userAddress.getPhoneNumber(), shopifyAddress.getPhone());
    }

    @Test
    public void testCreateMaskedWalletRequest() {
        final Checkout checkout = Mockito.mock(Checkout.class);
        Mockito.doReturn(Arrays.asList(lineItem1, lineItem2)).when(checkout).getLineItems();
        Mockito.doReturn(CURRENCY).when(checkout).getCurrency();
        Mockito.doReturn(currencyFormatter.format(LINE_ITEM_TOTAL_PRICE_1 + LINE_ITEM_TOTAL_PRICE_2)).when(checkout).getPaymentDue();
        Mockito.doReturn(true).when(checkout).isRequiresShipping();

        final MaskedWalletRequest request = AndroidPayHelper.createMaskedWalletRequest("merchantName", checkout, "ANDROID_PAY_PUBLIC_KEY", true);
        assertWalletCart(request.getCart());
        Assert.assertEquals("merchantName", request.getMerchantName());
        Assert.assertEquals(CURRENCY, request.getCurrencyCode());
        Assert.assertEquals(checkout.getPaymentDue(), request.getEstimatedTotalPrice());
    }

    @Test
    public void testCreateFullWalletRequest() throws Exception {
        final Checkout checkout = Mockito.mock(Checkout.class);
        Mockito.doReturn(Arrays.asList(lineItem1, lineItem2)).when(checkout).getLineItems();
        Mockito.doReturn(CURRENCY).when(checkout).getCurrency();
        Mockito.doReturn(currencyFormatter.format(LINE_ITEM_TOTAL_PRICE_1 + LINE_ITEM_TOTAL_PRICE_2)).when(checkout).getPaymentDue();
        Mockito.doReturn(true).when(checkout).isRequiresShipping();

        final MaskedWallet maskedWallet = createMaskedWallet(null, null, null, "GoogleTransactionId");
        final FullWalletRequest fullWalletRequest = AndroidPayHelper.createFullWalletRequest(checkout, maskedWallet);
        assertWalletCart(fullWalletRequest.getCart());
        Assert.assertEquals(maskedWallet.getGoogleTransactionId(), fullWalletRequest.getGoogleTransactionId());
    }

    @Test
    public void testIsCheckoutUpdateRequiredShippingAddress() throws Exception {
        final UserAddress userAddress = createWalletUserAddress();

        final Address shippingShopifyAddress = AndroidPayHelper.createShopifyAddress(userAddress);
        final Address billingShopifyAddress = AndroidPayHelper.createShopifyAddress(userAddress);

        final Checkout checkout = Mockito.mock(Checkout.class);
        Mockito.doReturn(shippingShopifyAddress).when(checkout).getShippingAddress();
        Mockito.doReturn(billingShopifyAddress).when(checkout).getBillingAddress();
        Mockito.doReturn("email").when(checkout).getEmail();

        final MaskedWallet maskedWallet = createMaskedWallet(userAddress, userAddress, "email", null);

        Assert.assertFalse(AndroidPayHelper.isCheckoutUpdateRequired(checkout, maskedWallet));

        String tmp = shippingShopifyAddress.getAddress1();
        shippingShopifyAddress.setAddress1("");
        Assert.assertTrue(AndroidPayHelper.isCheckoutUpdateRequired(checkout, maskedWallet));
        shippingShopifyAddress.setAddress1(tmp);

        tmp = shippingShopifyAddress.getAddress2();
        shippingShopifyAddress.setAddress2("");
        Assert.assertTrue(AndroidPayHelper.isCheckoutUpdateRequired(checkout, maskedWallet));
        shippingShopifyAddress.setAddress2(tmp);

        tmp = shippingShopifyAddress.getCity();
        shippingShopifyAddress.setCity("");
        Assert.assertTrue(AndroidPayHelper.isCheckoutUpdateRequired(checkout, maskedWallet));
        shippingShopifyAddress.setCity(tmp);

        tmp = shippingShopifyAddress.getCountryCode();
        shippingShopifyAddress.setCountryCode("");
        Assert.assertTrue(AndroidPayHelper.isCheckoutUpdateRequired(checkout, maskedWallet));
        shippingShopifyAddress.setCountryCode(tmp);

        tmp = shippingShopifyAddress.getProvinceCode();
        shippingShopifyAddress.setProvinceCode("");
        Assert.assertTrue(AndroidPayHelper.isCheckoutUpdateRequired(checkout, maskedWallet));
        shippingShopifyAddress.setProvinceCode(tmp);

        tmp = shippingShopifyAddress.getZip();
        shippingShopifyAddress.setZip("");
        Assert.assertTrue(AndroidPayHelper.isCheckoutUpdateRequired(checkout, maskedWallet));
        shippingShopifyAddress.setZip(tmp);
    }

    @Test
    public void testIsCheckoutUpdateRequiredBillingAddress() throws Exception {
        final UserAddress userAddress = createWalletUserAddress();

        final Address shippingShopifyAddress = AndroidPayHelper.createShopifyAddress(userAddress);
        final Address billingShopifyAddress = AndroidPayHelper.createShopifyAddress(userAddress);

        final Checkout checkout = Mockito.mock(Checkout.class);
        Mockito.doReturn(shippingShopifyAddress).when(checkout).getShippingAddress();
        Mockito.doReturn(billingShopifyAddress).when(checkout).getBillingAddress();
        Mockito.doReturn("email").when(checkout).getEmail();

        final MaskedWallet maskedWallet = createMaskedWallet(userAddress, userAddress, "email", null);

        Assert.assertFalse(AndroidPayHelper.isCheckoutUpdateRequired(checkout, maskedWallet));

        String tmp = billingShopifyAddress.getAddress1();
        billingShopifyAddress.setAddress1("");
        Assert.assertTrue(AndroidPayHelper.isCheckoutUpdateRequired(checkout, maskedWallet));
        billingShopifyAddress.setAddress1(tmp);

        tmp = billingShopifyAddress.getAddress2();
        billingShopifyAddress.setAddress2("");
        Assert.assertTrue(AndroidPayHelper.isCheckoutUpdateRequired(checkout, maskedWallet));
        billingShopifyAddress.setAddress2(tmp);

        tmp = billingShopifyAddress.getCity();
        billingShopifyAddress.setCity("");
        Assert.assertTrue(AndroidPayHelper.isCheckoutUpdateRequired(checkout, maskedWallet));
        billingShopifyAddress.setCity(tmp);

        tmp = billingShopifyAddress.getCountryCode();
        billingShopifyAddress.setCountryCode("");
        Assert.assertTrue(AndroidPayHelper.isCheckoutUpdateRequired(checkout, maskedWallet));
        billingShopifyAddress.setCountryCode(tmp);

        tmp = billingShopifyAddress.getProvinceCode();
        billingShopifyAddress.setProvinceCode("");
        Assert.assertTrue(AndroidPayHelper.isCheckoutUpdateRequired(checkout, maskedWallet));
        billingShopifyAddress.setProvinceCode(tmp);

        tmp = billingShopifyAddress.getZip();
        billingShopifyAddress.setZip("");
        Assert.assertTrue(AndroidPayHelper.isCheckoutUpdateRequired(checkout, maskedWallet));
        billingShopifyAddress.setZip(tmp);
    }

    @Test
    public void testIsCheckoutUpdateRequiredEmail() throws Exception {
        final UserAddress userAddress = createWalletUserAddress();

        final Address shippingShopifyAddress = AndroidPayHelper.createShopifyAddress(userAddress);
        final Address billingShopifyAddress = AndroidPayHelper.createShopifyAddress(userAddress);

        final Checkout checkout = Mockito.mock(Checkout.class);
        Mockito.doReturn(shippingShopifyAddress).when(checkout).getShippingAddress();
        Mockito.doReturn(billingShopifyAddress).when(checkout).getBillingAddress();
        Mockito.doReturn("email").when(checkout).getEmail();

        final MaskedWallet maskedWallet = createMaskedWallet(userAddress, userAddress, "email", null);

        Assert.assertFalse(AndroidPayHelper.isCheckoutUpdateRequired(checkout, maskedWallet));

        Mockito.doReturn("email1").when(checkout).getEmail();
        Assert.assertTrue(AndroidPayHelper.isCheckoutUpdateRequired(checkout, maskedWallet));
    }

    @Test
    public void testGetAndroidPaymentToken() throws Exception {
        final Constructor<PaymentMethodToken> paymentMethodTokenConstructor = PaymentMethodToken.class.getDeclaredConstructor(int.class, int.class, String.class);
        paymentMethodTokenConstructor.setAccessible(true);
        PaymentMethodToken paymentMethodToken = paymentMethodTokenConstructor.newInstance(1, 1, "paymentMethodToken");

        final Constructor<FullWallet> fullWalletConstructor = FullWallet.class.getDeclaredConstructor(int.class, String.class, String.class, ProxyCard.class, String.class, com.google.android.gms.wallet.Address.class, com.google.android.gms.wallet.Address.class, String[].class, UserAddress.class, UserAddress.class, InstrumentInfo[].class, PaymentMethodToken.class);
        fullWalletConstructor.setAccessible(true);
        final FullWallet fullWallet = fullWalletConstructor.newInstance(1, null, null, null, null, null, null, null, null, null, null, paymentMethodToken);

        final PaymentToken paymentToken = AndroidPayHelper.getAndroidPaymentToken(fullWallet, "androidPayPublicKey");

        final Field paymentDataField = Class.forName("com.shopify.buy.model.PaymentToken$Wrapper").getDeclaredField("paymentData");
        paymentDataField.setAccessible(true);

        final Field paymentIdentifierField = Class.forName("com.shopify.buy.model.PaymentToken$Wrapper").getDeclaredField("identifier");
        paymentIdentifierField.setAccessible(true);

        final Field paymentTypeField = Class.forName("com.shopify.buy.model.PaymentToken$Wrapper").getDeclaredField("type");
        paymentTypeField.setAccessible(true);

        final Field paymentTokenWrapperField = PaymentToken.class.getDeclaredField("wrapper");
        paymentTokenWrapperField.setAccessible(true);
        final Object paymentTokenWrapper = paymentTokenWrapperField.get(paymentToken);

        Assert.assertEquals("android_pay", paymentTypeField.get(paymentTokenWrapper));
        Assert.assertEquals(paymentMethodToken.getToken(), paymentDataField.get(paymentTokenWrapper));

        final MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
        final byte[] digest = messageDigest.digest("androidPayPublicKey".getBytes("UTF-8"));
        final String androidPayPublicKeyHash = Base64.encodeToString(digest, Base64.DEFAULT);
        Assert.assertEquals(androidPayPublicKeyHash, paymentIdentifierField.get(paymentTokenWrapper));
    }

    private void assertWalletCart(final Cart cart) {
        Assert.assertEquals(CURRENCY, cart.getCurrencyCode());
        Assert.assertEquals(currencyFormatter.format(LINE_ITEM_TOTAL_PRICE_1 + LINE_ITEM_TOTAL_PRICE_2), cart.getTotalPrice());

        Assert.assertEquals(2, cart.getLineItems().size());

        Assert.assertEquals(lineItem1.getTitle(), cart.getLineItems().get(0).getDescription());
        Assert.assertEquals(currencyFormatter.format(LINE_ITEM_TOTAL_PRICE_1), cart.getLineItems().get(0).getTotalPrice());
        Assert.assertEquals(shop.getCurrency(), cart.getLineItems().get(0).getCurrencyCode());

        Assert.assertEquals(lineItem2.getTitle(), cart.getLineItems().get(1).getDescription());
        Assert.assertEquals(currencyFormatter.format(LINE_ITEM_TOTAL_PRICE_2), cart.getLineItems().get(1).getTotalPrice());
        Assert.assertEquals(shop.getCurrency(), cart.getLineItems().get(1).getCurrencyCode());
    }

    private UserAddress createWalletUserAddress() throws Exception {
        final Constructor<UserAddress> constructor = UserAddress.class.getDeclaredConstructor(int.class, String.class, String.class, String.class, String.class, String.class, String.class, String.class, String.class, String.class, String.class, String.class, String.class, boolean.class, String.class, String.class);
        constructor.setAccessible(true);
        return constructor.newInstance(1, "firstName lastName", "address1", "address2", "address3", "address4", "address5", "administrativeArea", "locality", "countryCode", "postalCode", "sortingCode", "phoneNumber", false, "companyName", "emailAddress");
    }

    private MaskedWallet createMaskedWallet(final UserAddress shippingAddress, final UserAddress billingAddress, final String email, final String googleTransactionId) throws Exception {
        final Constructor<MaskedWallet> maskedWalletConstructor = MaskedWallet.class.getDeclaredConstructor();
        maskedWalletConstructor.setAccessible(true);

        final Constructor<MaskedWallet.Builder> maskedWalletBuilderConstructor = (Constructor<MaskedWallet.Builder>) Class.forName("com.google.android.gms.wallet.MaskedWallet$Builder").getDeclaredConstructor(MaskedWallet.class);
        maskedWalletBuilderConstructor.setAccessible(true);

        final MaskedWallet.Builder maskedWalletBuilder = maskedWalletBuilderConstructor.newInstance(maskedWalletConstructor.newInstance());
        maskedWalletBuilder.setBuyerShippingAddress(shippingAddress);
        maskedWalletBuilder.setBuyerBillingAddress(billingAddress);
        maskedWalletBuilder.setEmail(email);
        maskedWalletBuilder.setGoogleTransactionId(googleTransactionId);

        return maskedWalletBuilder.build();
    }
}
