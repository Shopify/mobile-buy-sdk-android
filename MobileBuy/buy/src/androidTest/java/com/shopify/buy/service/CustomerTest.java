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

package com.shopify.buy.service;

import com.shopify.buy.dataprovider.BuyClient;
import com.shopify.buy.dataprovider.BuyError;
import com.shopify.buy.extensions.ShopifyAndroidTestCase;
import com.shopify.buy.model.AccountCredentials;
import com.shopify.buy.model.Address;
import com.shopify.buy.model.Customer;
import com.shopify.buy.model.CustomerToken;
import com.shopify.buy.model.Order;

import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class CustomerTest extends ShopifyAndroidTestCase {

    private static final String PASSWORD = "asdasd";
    private static final String EMAIL = "asd@asda.com";
    private static final String FIRSTNAME = "Testy";
    private static final String LASTNAME = "McTesterson2";
    private static final String WRONG_PASSWORD = "iii";
    private static final String MALFORMED_EMAIL = "aaaj*&^";
    private static final String OTHER_WRONG_PASSWORD = "7g63";

    private Customer customer;
    private List<Order> orders;
    private List<Address> addresses;
    private Address address;
    private CustomerToken customerToken;

    private static boolean ENABLE_ACTIVATION_TEST = false;

    public void testCustomerCreation() throws InterruptedException {
        final Customer randomCustomer = USE_MOCK_RESPONSES ? getExistingCustomer() : generateRandomCustomer();
        final AccountCredentials accountCredentials = new AccountCredentials(randomCustomer.getEmail(), PASSWORD, randomCustomer.getFirstName(), randomCustomer.getLastName());

        final CountDownLatch countDownLatch = new CountDownLatch(1);

        buyClient.createCustomer(accountCredentials, new Callback<Customer>() {
            @Override
            public void success(Customer customer, Response response) {
                assertNotNull(customer);
                assertEquals(randomCustomer.getEmail(), customer.getEmail());
                assertEquals(randomCustomer.getFirstName(), customer.getFirstName());
                assertEquals(randomCustomer.getLastName(), customer.getLastName());
                countDownLatch.countDown();
            }

            @Override
            public void failure(RetrofitError error) {
                fail(BuyClient.getErrorBody(error));
            }
        });

        countDownLatch.await();
    }

    public void testCustomerActivation() throws InterruptedException {

        if (ENABLE_ACTIVATION_TEST) {

            final CountDownLatch countDownLatch = new CountDownLatch(1);

            testCustomerLogin();

            final AccountCredentials accountCredentials = new AccountCredentials(customer.getEmail(), PASSWORD, customer.getFirstName(), customer.getLastName());

            // TODO update this test when we start to get real tokens
            buyClient.activateCustomer(customer.getId(), "need activation token not access token", accountCredentials, new Callback<Customer>() {
                @Override
                public void success(Customer customer, Response response) {
                    assertNotNull(customer);
                    countDownLatch.countDown();
                }

                @Override
                public void failure(RetrofitError error) {
                    fail(BuyClient.getErrorBody(error));
                }
            });

            countDownLatch.await();
        }
    }


    public void testCustomerLogin() throws InterruptedException {

        customer = getExistingCustomer();

        final AccountCredentials accountCredentials = new AccountCredentials(customer.getEmail(), PASSWORD, customer.getFirstName(), customer.getLastName());

        final CountDownLatch countDownLatch = new CountDownLatch(1);

        buyClient.loginCustomer(accountCredentials, new Callback<CustomerToken>() {
            @Override
            public void success(CustomerToken customerToken, Response response) {
                assertNotNull(buyClient.getCustomerToken());
                assertEquals(false, buyClient.getCustomerToken().getAccessToken().isEmpty());
                CustomerTest.this.customerToken = buyClient.getCustomerToken();
                countDownLatch.countDown();
            }

            @Override
            public void failure(RetrofitError error) {
                fail(BuyClient.getErrorBody(error));
            }
        });

        countDownLatch.await();

        // Update the customer
        getCustomerAfterLogin();

    }

    public void testCustomerLogout() throws InterruptedException {

        testCustomerLogin();
        buyClient.setCustomerToken(customerToken);

        final CountDownLatch countDownLatch = new CountDownLatch(1);

        buyClient.logoutCustomer(new Callback<Void>() {
            @Override
            public void success(Void aVoid, Response response) {
                countDownLatch.countDown();
            }

            @Override
            public void failure(RetrofitError error) {
                fail(BuyClient.getErrorBody(error));
            }
        });

        countDownLatch.await();
    }

    public void testCustomerRenew() throws InterruptedException {

        testCustomerLogin();
        buyClient.setCustomerToken(customerToken);

        final CountDownLatch countDownLatch = new CountDownLatch(1);

        buyClient.renewCustomer(new Callback<CustomerToken>() {
            @Override
            public void success(CustomerToken customerToken, Response response) {
                countDownLatch.countDown();
            }

            @Override
            public void failure(RetrofitError error) {
                fail(BuyClient.getErrorBody(error));
            }
        });

        countDownLatch.await();
    }

    public void testCustomerRecover() throws InterruptedException {

        customer = getExistingCustomer();

        final CountDownLatch countDownLatch = new CountDownLatch(1);

        buyClient.recoverPassword(EMAIL, new Callback<Void>() {
            @Override
            public void success(Void aVoid, Response response) {
                countDownLatch.countDown();
            }

            @Override
            public void failure(RetrofitError error) {
                fail(BuyClient.getErrorBody(error));
            }
        });

        countDownLatch.await();
    }

    public void testCustomerUpdate() throws InterruptedException {

        testCustomerLogin();
        buyClient.setCustomerToken(customerToken);

        customer.setLastName("Foo");

        final CountDownLatch countDownLatch = new CountDownLatch(1);

        buyClient.updateCustomer(customer, new Callback<Customer>() {
            @Override
            public void success(Customer customer, Response response) {
                assertNotNull(customer);
                assertEquals("Foo", customer.getLastName());
                countDownLatch.countDown();
            }

            @Override
            public void failure(RetrofitError error) {
                fail(BuyClient.getErrorBody(error));
            }
        });

        countDownLatch.await();
    }

    public void testGetCustomerOrders() throws InterruptedException {

        testCustomerLogin();
        buyClient.setCustomerToken(customerToken);

        final CountDownLatch countDownLatch = new CountDownLatch(1);

        buyClient.getOrders(customer, new Callback<List<Order>>() {
            @Override
            public void success(List<Order> orders, Response response) {
                assertNotNull(orders);
                assertEquals(true, orders.size() > 0);
                CustomerTest.this.orders = orders;
                countDownLatch.countDown();
            }

            @Override
            public void failure(RetrofitError error) {
                fail(BuyClient.getErrorBody(error));
            }

        });

        countDownLatch.await();
    }

    public void testGetOrder() throws InterruptedException {

        testGetCustomerOrders();
        buyClient.setCustomerToken(customerToken);

        String orderId = orders.get(0).getOrderId();

        final CountDownLatch countDownLatch = new CountDownLatch(1);

        buyClient.getOrder(customer, orderId, new Callback<Order>() {
            @Override
            public void success(Order order, Response response) {
                assertNotNull(order);
                countDownLatch.countDown();
            }

            @Override
            public void failure(RetrofitError error) {
                fail(BuyClient.getErrorBody(error));
            }
        });

        countDownLatch.await();
    }

    public void testGetCustomer() throws InterruptedException {

        testCustomerLogin();
        buyClient.setCustomerToken(customerToken);

        final CountDownLatch countDownLatch = new CountDownLatch(1);
        buyClient.getCustomer(customerToken.getCustomerId(), new Callback<Customer>() {
            @Override
            public void success(Customer customer, Response response) {
                assertNotNull(customer);
                countDownLatch.countDown();
            }

            @Override
            public void failure(RetrofitError error) {
                fail(BuyClient.getErrorBody(error));
            }
        });

        countDownLatch.await();
    }

    public void testCreateAddress() throws InterruptedException {

        testCustomerLogin();
        buyClient.setCustomerToken(customerToken);

        final Address inputAddress = USE_MOCK_RESPONSES ? getExistingAddress() : generateAddress();

        final CountDownLatch countDownLatch = new CountDownLatch(1);

        buyClient.createAddress(customer, inputAddress, new Callback<Address>() {
            @Override
            public void success(Address address, Response response) {
                assertEquals(inputAddress.getAddress1(), address.getAddress1());
                assertEquals(inputAddress.getAddress2(), address.getAddress2());
                assertEquals(inputAddress.getCity(), address.getCity());
                countDownLatch.countDown();
            }

            @Override
            public void failure(RetrofitError error) {
                fail(BuyClient.getErrorBody(error));
            }
        });

        countDownLatch.await();
    }

    public void testGetAddresses() throws InterruptedException {

        testCustomerLogin();
        buyClient.setCustomerToken(customerToken);

        final CountDownLatch countDownLatch = new CountDownLatch(1);

        buyClient.getAddresses(customer, new Callback<List<Address>>() {
            @Override
            public void success(List<Address> addresses, Response response) {
                assertNotNull(addresses);
                assertEquals(true, addresses.size() > 0);
                CustomerTest.this.addresses = addresses;
                countDownLatch.countDown();
            }

            @Override
            public void failure(RetrofitError error) {
                fail(BuyClient.getErrorBody(error));
            }
        });

        countDownLatch.await();
    }

    public void testGetAddress() throws InterruptedException {

        testGetAddresses();
        buyClient.setCustomerToken(customerToken);

        String addressId = addresses.get(0).getAddressId();

        final CountDownLatch countDownLatch = new CountDownLatch(1);

        buyClient.getAddress(customer, addressId, new Callback<Address>() {
            @Override
            public void success(Address address, Response response) {
                assertNotNull(address);
                CustomerTest.this.address = address;
                countDownLatch.countDown();
            }

            @Override
            public void failure(RetrofitError error) {
                fail(BuyClient.getErrorBody(error));
            }
        });

        countDownLatch.await();
    }

    public void testUpdateAddress() throws InterruptedException {

        testGetAddress();
        buyClient.setCustomerToken(customerToken);

        address.setCity("Toledo");

        final CountDownLatch countDownLatch = new CountDownLatch(1);

        buyClient.updateAddress(customer, address, new Callback<Address>() {
            @Override
            public void success(Address address, Response response) {
                assertNotNull(address);
                countDownLatch.countDown();
            }

            @Override
            public void failure(RetrofitError error) {
                fail(BuyClient.getErrorBody(error));
            }
        });

        countDownLatch.await();
    }

    private Customer getExistingCustomer() {
        Customer customer = new Customer();
        customer.setEmail(EMAIL);
        customer.setFirstName(FIRSTNAME);
        customer.setLastName(LASTNAME);

        return customer;
    }

    private Customer generateRandomCustomer() {
        Customer customer = new Customer();
        customer.setEmail(generateRandomString() + "customer" + generateRandomString() + "@example.com");
        customer.setFirstName("Customer");
        customer.setLastName("Randomly Generated");
        return customer;
    }

    private String generateRandomString() {
        return UUID.randomUUID().toString().substring(0, 8);
    }

    private int generateRandomInt(int min, int max) {
        Random random = new Random();
        return random.nextInt((max - min) + 1) + min;
    }


    private Address generateAddress() {
        Address shippingAddress = new Address();
        shippingAddress.setAddress1(String.format("%d - 150 Elgin Street", generateRandomInt(0, 9999)));
        shippingAddress.setAddress2("8th Floor");
        shippingAddress.setCity("Ottawa");
        shippingAddress.setProvinceCode("ON");
        shippingAddress.setCompany(String.format("Shopify Inc. (%s)", generateRandomString()));
        shippingAddress.setFirstName(String.format("%s (%s)", generateRandomString(), FIRSTNAME));
        shippingAddress.setLastName(String.format("%s (%s)", generateRandomString(), LASTNAME));
        shippingAddress.setPhone("1-555-555-5555");
        shippingAddress.setCountryCode("CA");
        shippingAddress.setZip("K1N5T5");
        return shippingAddress;
    }

    private Address getExistingAddress() {
        Address shippingAddress = new Address();
        shippingAddress.setAddress1("150 Elgin Street");
        shippingAddress.setAddress2("8th Floor");
        shippingAddress.setCity("Ottawa");
        shippingAddress.setProvinceCode("ON");
        shippingAddress.setCompany("Shopify Inc.");
        shippingAddress.setFirstName(FIRSTNAME);
        shippingAddress.setLastName(LASTNAME);
        shippingAddress.setPhone("1-555-555-5555");
        shippingAddress.setCountryCode("CA");
        shippingAddress.setZip("K1N5T5");
        return shippingAddress;
    }

    private void getCustomerAfterLogin() throws InterruptedException {

        final CountDownLatch countDownLatch = new CountDownLatch(1);

        buyClient.getCustomer(customerToken.getCustomerId(), new Callback<Customer>() {

            @Override
            public void success(Customer customer, Response response) {
                assertNotNull(customer);
                CustomerTest.this.customer = customer;
                CustomerTest.this.customerToken = buyClient.getCustomerToken();
                countDownLatch.countDown();
            }

            @Override
            public void failure(RetrofitError error) {
                fail(BuyClient.getErrorBody(error));
            }
        });

        countDownLatch.await();

    }

    public void testCustomerCreationDuplicateEmail() throws InterruptedException {

        final Customer input = getExistingCustomer();
        final AccountCredentials accountCredentials = new AccountCredentials(input.getEmail(), PASSWORD, input.getFirstName(), input.getLastName());

        final CountDownLatch countDownLatch = new CountDownLatch(1);

        buyClient.createCustomer(accountCredentials, new Callback<Customer>() {
            @Override
            public void success(Customer customer, Response response) {
                fail("Should not be able to create multiple accounts with same email");
            }

            @Override
            public void failure(RetrofitError error) {
                if (BuyError.isEmailAlreadyTakenError(error, input.getEmail())) {
                } else {
                    fail(String.format("Should be getting email already taken error. \nGot \"%s\"", error.getMessage()));
                }
                countDownLatch.countDown();
            }
        });

        countDownLatch.await();
    }

    public void testCustomerInvalidLogin() throws InterruptedException {
        final Customer customer = getExistingCustomer();
        final AccountCredentials accountCredentials = new AccountCredentials(customer.getEmail(), WRONG_PASSWORD);

        final CountDownLatch countDownLatch = new CountDownLatch(1);

        buyClient.loginCustomer(accountCredentials, new Callback<CustomerToken>() {

            @Override
            public void success(CustomerToken customerToken, Response response) {
                fail("Invalid credentials should not be able to login");
            }

            @Override
            public void failure(RetrofitError error) {
                assertEquals(401, error.getResponse().getStatus());
                countDownLatch.countDown();
            }
        });

        countDownLatch.await();
    }

    public void testCreateCustomerInvalidEmailPassword() throws InterruptedException {
        final AccountCredentials accountCredentials = new AccountCredentials(generateRandomString(), WRONG_PASSWORD, OTHER_WRONG_PASSWORD, FIRSTNAME, LASTNAME);

        final CountDownLatch countDownLatch = new CountDownLatch(1);

        buyClient.createCustomer(accountCredentials, new Callback<Customer>() {

            @Override
            public void success(Customer customerToken, Response response) {
                fail("Invalid email, password, confirmation password. Should not be able to create a customer.");
            }

            @Override
            public void failure(RetrofitError error) {
                List<BuyError> buyErrors = BuyError.errorsForCustomer(error);
                assertEquals(3, buyErrors.size());


                BuyError passwordTooShort = buyErrors.get(0);
                assertEquals(passwordTooShort.getCode(), "too_short");
                assertEquals(passwordTooShort.getMessage(), "is too short (minimum is 5 characters)");

                BuyError confirmationMatch = buyErrors.get(1);
                assertEquals(confirmationMatch.getCode(), "confirmation");
                assertEquals(confirmationMatch.getMessage(), "must match the provided password.");

                BuyError invalidEmail = buyErrors.get(2);
                assertEquals(invalidEmail.getCode(), "invalid");
                assertEquals(invalidEmail.getMessage(), "is invalid");

                assertEquals(422, error.getResponse().getStatus());

                countDownLatch.countDown();
            }
        });

        countDownLatch.await();
    }
}