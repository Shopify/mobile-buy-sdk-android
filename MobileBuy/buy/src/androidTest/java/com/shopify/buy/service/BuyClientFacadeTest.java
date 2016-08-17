/*
 *   The MIT License (MIT)
 *
 *   Copyright (c) 2015 Shopify Inc.
 *
 *   Permission is hereby granted, free of charge, to any person obtaining a copy
 *   of this software and associated documentation files (the "Software"), to deal
 *   in the Software without restriction, including without limitation the rights
 *   to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *   copies of the Software, and to permit persons to whom the Software is
 *   furnished to do so, subject to the following conditions:
 *
 *   The above copyright notice and this permission notice shall be included in
 *   all copies or substantial portions of the Software.
 *
 *   THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *   IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *   FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *   AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *   LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *   OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 *   THE SOFTWARE.
 */
package com.shopify.buy.service;

import android.support.test.runner.AndroidJUnit4;

import com.shopify.buy.extensions.ShopifyAndroidTestCase;
import com.shopify.buy.model.AccountCredentials;
import com.shopify.buy.model.Address;
import com.shopify.buy.model.Checkout;
import com.shopify.buy.model.CreditCard;
import com.shopify.buy.model.Customer;
import com.shopify.buy.model.CustomerToken;
import com.shopify.buy.model.PaymentToken;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Arrays;
import java.util.LinkedHashSet;

@RunWith(AndroidJUnit4.class)
public class BuyClientFacadeTest extends ShopifyAndroidTestCase {

    @Test
    public void testFacade() {
        buyClient.setCustomerToken(new CustomerToken());

        Assert.assertNotNull(buyClient.getApiKey());
        Assert.assertNotNull(buyClient.getAppId());
        Assert.assertNotNull(buyClient.getApplicationName());
        Assert.assertNotNull(buyClient.getShopDomain());
        Assert.assertNotNull(buyClient.getShop());
        Assert.assertNotNull(buyClient.createCheckout(new Checkout("test")));
        Assert.assertNotNull(buyClient.updateCheckout(new Checkout("test")));
        Assert.assertNotNull(buyClient.getShippingRates("test"));
        Assert.assertNotNull(buyClient.storeCreditCard(new CreditCard(), new Checkout("test")));
        Assert.assertNotNull(buyClient.getCheckoutCompletionStatus("test"));
        Assert.assertNotNull(buyClient.completeCheckout(PaymentToken.createCreditCardPaymentToken("test"), "test"));
        Assert.assertNotNull(buyClient.getCheckout("test"));
        Assert.assertNotNull(buyClient.applyGiftCard("test", new Checkout("test")));
        Assert.assertNotNull(buyClient.removeGiftCard(1L, new Checkout("test")));
        Assert.assertNotNull(buyClient.removeProductReservationsFromCheckout("test"));
        Assert.assertNotNull(buyClient.createCustomer(new AccountCredentials("test")));
        Assert.assertNotNull(buyClient.activateCustomer(1L, "test", new AccountCredentials("test")));
        Assert.assertNotNull(buyClient.resetPassword(1L, "test", new AccountCredentials("test")));
        Assert.assertNotNull(buyClient.loginCustomer(new AccountCredentials("test")));
        Assert.assertNotNull(buyClient.logoutCustomer());
        Assert.assertNotNull(buyClient.updateCustomer(new Customer() {
            @Override
            public Long getId() {
                return 1L;
            }
        }));
        Assert.assertNotNull(buyClient.getCustomer());
        Assert.assertNotNull(buyClient.renewCustomer());
        Assert.assertNotNull(buyClient.recoverPassword("test"));
        Assert.assertNotNull(buyClient.getOrders());
        Assert.assertNotNull(buyClient.getOrder(1L));
        Assert.assertNotNull(buyClient.createAddress(new Address()));
        Assert.assertNotNull(buyClient.getAddresses());
        Assert.assertNotNull(buyClient.getAddress(1L));
        Assert.assertNotNull(buyClient.updateAddress(new Address()));
        Assert.assertNotNull(buyClient.deleteAddress(1L));
        Assert.assertTrue(buyClient.getProductPageSize() > 0);
        Assert.assertNotNull(buyClient.getProducts(1));
        Assert.assertNotNull(buyClient.getProductByHandle("test"));
        Assert.assertNotNull(buyClient.getProduct(1L));
        Assert.assertNotNull(buyClient.getProducts(Arrays.asList(1L)));
        Assert.assertNotNull(buyClient.getCollections(1));
        Assert.assertNotNull(buyClient.getCollections(1, Arrays.asList(1L)));
        Assert.assertNotNull(buyClient.getProductTags(1));
        Assert.assertNotNull(buyClient.getProducts(1, 1L, new LinkedHashSet<String>(), null));
    }
}
