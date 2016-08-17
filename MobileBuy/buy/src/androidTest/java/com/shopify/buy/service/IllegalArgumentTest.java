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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import rx.Subscriber;

@RunWith(AndroidJUnit4.class)
public class IllegalArgumentTest extends ShopifyAndroidTestCase {

    @Test
    public void testProductServiceArguments() {
        checkException(IllegalArgumentException.class, new Runnable() {
            @Override
            public void run() {
                buyClient.getProducts(0);
            }
        });

        checkException(NullPointerException.class, new Runnable() {
            @Override
            public void run() {
                buyClient.getProductByHandle(null);
            }
        });

        checkException(IllegalArgumentException.class, new Runnable() {
            @Override
            public void run() {
                buyClient.getProductByHandle("");
            }
        });

        checkException(IllegalArgumentException.class, new Runnable() {
            @Override
            public void run() {
                buyClient.getCollectionByHandle("");
            }
        });

        checkException(NullPointerException.class, new Runnable() {
            @Override
            public void run() {
                buyClient.getCollections(1, (List<Long>) null);
            }
        });

        checkException(IllegalArgumentException.class, new Runnable() {
            @Override
            public void run() {
                buyClient.getCollections(1, Collections.<Long>emptyList());
            }
        });

        checkException(NullPointerException.class, new Runnable() {
            @Override
            public void run() {
                buyClient.getProduct(null);
            }
        });

        checkException(NullPointerException.class, new Runnable() {
            @Override
            public void run() {
                buyClient.getProducts(null);
            }
        });

        checkException(IllegalArgumentException.class, new Runnable() {
            @Override
            public void run() {
                buyClient.getProducts(new ArrayList<Long>());
            }
        });

        checkException(IllegalArgumentException.class, new Runnable() {
            @Override
            public void run() {
                buyClient.getProducts(0, null, null, null);
            }
        });

        checkException(NullPointerException.class, new Runnable() {
            @Override
            public void run() {
                buyClient.getProducts(1, null, null, null);
            }
        });

        checkException(IllegalArgumentException.class, new Runnable() {
            @Override
            public void run() {
                buyClient.getCollections(0);
            }
        });

        checkException(IllegalArgumentException.class, new Runnable() {
            @Override
            public void run() {
                buyClient.getProductTags(0);
            }
        });

        checkException(IllegalArgumentException.class, new Runnable() {
            @Override
            public void run() {
                buyClient.getProducts(0, (Set<String>) null);
            }
        });
    }

    @Test
    public void testCheckoutServiceArguments() {
        checkException(NullPointerException.class, new Runnable() {
            @Override
            public void run() {
                buyClient.createCheckout(null);
            }
        });

        checkException(NullPointerException.class, new Runnable() {
            @Override
            public void run() {
                buyClient.updateCheckout(null);
            }
        });

        checkException(NullPointerException.class, new Runnable() {
            @Override
            public void run() {
                buyClient.getShippingRates(null);
            }
        });

        checkException(IllegalArgumentException.class, new Runnable() {
            @Override
            public void run() {
                buyClient.getShippingRates("");
            }
        });

        checkException(NullPointerException.class, new Runnable() {
            @Override
            public void run() {
                buyClient.storeCreditCard(null, new Checkout("test"));
            }
        });

        checkException(NullPointerException.class, new Runnable() {
            @Override
            public void run() {
                buyClient.storeCreditCard(new CreditCard(), null);
            }
        });

        checkException(IllegalArgumentException.class, new Runnable() {
            @Override
            public void run() {
                buyClient.storeCreditCard(new CreditCard(), new Checkout(""));
            }
        });

        checkException(NullPointerException.class, new Runnable() {
            @Override
            public void run() {
                buyClient.completeCheckout(null, "test");
            }
        });

        checkException(NullPointerException.class, new Runnable() {
            @Override
            public void run() {
                buyClient.completeCheckout(PaymentToken.createCreditCardPaymentToken("test"), null);
            }
        });

        checkException(IllegalArgumentException.class, new Runnable() {
            @Override
            public void run() {
                buyClient.completeCheckout(PaymentToken.createCreditCardPaymentToken("test"), "");
            }
        });

        checkException(NullPointerException.class, new Runnable() {
            @Override
            public void run() {
                buyClient.completeCheckout(null, "test");
            }
        });

        checkException(NullPointerException.class, new Runnable() {
            @Override
            public void run() {
                buyClient.getCheckoutCompletionStatus(null);
            }
        });

        checkException(IllegalArgumentException.class, new Runnable() {
            @Override
            public void run() {
                buyClient.getCheckoutCompletionStatus("");
            }
        });

        checkException(NullPointerException.class, new Runnable() {
            @Override
            public void run() {
                buyClient.getCheckout(null);
            }
        });

        checkException(IllegalArgumentException.class, new Runnable() {
            @Override
            public void run() {
                buyClient.getCheckout("");
            }
        });

        checkException(NullPointerException.class, new Runnable() {
            @Override
            public void run() {
                buyClient.applyGiftCard(null, new Checkout("test"));
            }
        });

        checkException(IllegalArgumentException.class, new Runnable() {
            @Override
            public void run() {
                buyClient.applyGiftCard("", new Checkout("test"));
            }
        });

        checkException(NullPointerException.class, new Runnable() {
            @Override
            public void run() {
                buyClient.applyGiftCard("test", null);
            }
        });

        checkException(IllegalArgumentException.class, new Runnable() {
            @Override
            public void run() {
                buyClient.applyGiftCard("test", new Checkout(""));
            }
        });

        checkException(NullPointerException.class, new Runnable() {
            @Override
            public void run() {
                buyClient.removeGiftCard(null, new Checkout("test"));
            }
        });

        checkException(NullPointerException.class, new Runnable() {
            @Override
            public void run() {
                buyClient.removeGiftCard(1L, null);
            }
        });

        checkException(IllegalArgumentException.class, new Runnable() {
            @Override
            public void run() {
                buyClient.removeGiftCard(1L, new Checkout(""));
            }
        });

        checkException(NullPointerException.class, new Runnable() {
            @Override
            public void run() {
                buyClient.removeProductReservationsFromCheckout(null);
            }
        });

        checkException(IllegalArgumentException.class, new Runnable() {
            @Override
            public void run() {
                buyClient.removeProductReservationsFromCheckout("");
            }
        });
    }

    @Test
    public void testCustomerServiceArguments() {
        buyClient.setCustomerToken(new CustomerToken());

        checkException(NullPointerException.class, new Runnable() {
            @Override
            public void run() {
                buyClient.createCustomer(null);
            }
        });

        checkException(NullPointerException.class, new Runnable() {
            @Override
            public void run() {
                buyClient.activateCustomer(null, "test", new AccountCredentials(""));
            }
        });

        checkException(NullPointerException.class, new Runnable() {
            @Override
            public void run() {
                buyClient.activateCustomer(1L, null, new AccountCredentials(""));
            }
        });

        checkException(IllegalArgumentException.class, new Runnable() {
            @Override
            public void run() {
                buyClient.activateCustomer(1L, "", new AccountCredentials(""));
            }
        });

        checkException(NullPointerException.class, new Runnable() {
            @Override
            public void run() {
                buyClient.activateCustomer(1L, "test", null);
            }
        });

        checkException(NullPointerException.class, new Runnable() {
            @Override
            public void run() {
                buyClient.resetPassword(null, "test", new AccountCredentials(""));
            }
        });

        checkException(NullPointerException.class, new Runnable() {
            @Override
            public void run() {
                buyClient.resetPassword(1L, null, new AccountCredentials(""));
            }
        });

        checkException(IllegalArgumentException.class, new Runnable() {
            @Override
            public void run() {
                buyClient.resetPassword(1L, "", new AccountCredentials(""));
            }
        });

        checkException(NullPointerException.class, new Runnable() {
            @Override
            public void run() {
                buyClient.resetPassword(1L, "test", null);
            }
        });

        checkException(NullPointerException.class, new Runnable() {
            @Override
            public void run() {
                buyClient.loginCustomer(null);
            }
        });

        checkException(NullPointerException.class, new Runnable() {
            @Override
            public void run() {
                buyClient.updateCustomer(null);
            }
        });

        checkException(NullPointerException.class, new Runnable() {
            @Override
            public void run() {
                buyClient.recoverPassword(null);
            }
        });

        checkException(IllegalArgumentException.class, new Runnable() {
            @Override
            public void run() {
                buyClient.recoverPassword("");
            }
        });


        // now clear the token
        buyClient.setCustomerToken(null);

        buyClient.updateCustomer(new Customer()).subscribe(illegalStateSubscriber);

        buyClient.logoutCustomer().subscribe(illegalStateSubscriber);

        buyClient.getCustomer().subscribe(illegalStateSubscriber);

        buyClient.renewCustomer().subscribe(illegalStateSubscriber);
    }

    @Test
    public void testOrderServiceArguments() {
        buyClient.setCustomerToken(new CustomerToken());

        checkException(NullPointerException.class, new Runnable() {
            @Override
            public void run() {
                buyClient.getOrder(null);
            }
        });

        // now clear the token
        buyClient.setCustomerToken(null);

        buyClient.getOrders().subscribe(illegalStateSubscriber);

        buyClient.getOrder(1L).subscribe(illegalStateSubscriber);
    }

    @Test
    public void testAddressServiceArguments() {
        buyClient.setCustomerToken(new CustomerToken());

        checkException(NullPointerException.class, new Runnable() {
            @Override
            public void run() {
                buyClient.createAddress(null);
            }
        });

        checkException(NullPointerException.class, new Runnable() {
            @Override
            public void run() {
                buyClient.deleteAddress(null);
            }
        });

        checkException(NullPointerException.class, new Runnable() {
            @Override
            public void run() {
                buyClient.getAddress(null);
            }
        });

        checkException(NullPointerException.class, new Runnable() {
            @Override
            public void run() {
                buyClient.updateAddress(null);
            }
        });

        checkException(NullPointerException.class, new Runnable() {
            @Override
            public void run() {
                buyClient.updateAddress(null);
            }
        });

        // now clear the token
        buyClient.setCustomerToken(null);

        buyClient.createAddress(new Address()).subscribe(illegalStateSubscriber);

        buyClient.deleteAddress(1L).subscribe(illegalStateSubscriber);

        buyClient.getAddresses().subscribe(illegalStateSubscriber);

        buyClient.getAddress(1L).subscribe(illegalStateSubscriber);

        buyClient.updateAddress(new Address()).subscribe(illegalStateSubscriber);

    }

    private Subscriber illegalStateSubscriber = new Subscriber() {
        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {
            Assert.assertEquals(IllegalStateException.class, e.getCause().getClass());
        }

        @Override
        public void onNext(Object o) {
            Assert.fail("Expected exception");
        }
    };

    private static void checkException(final Class exceptionClazz, final Runnable call) {
        try {
            call.run();
            Assert.fail("Expected exception");
        } catch (Exception e) {
            Assert.assertEquals(exceptionClazz, e.getClass());
        }
    }

}
