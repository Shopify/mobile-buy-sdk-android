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

import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.Suppress;

import com.shopify.buy.dataprovider.BuyClientUtils;
import com.shopify.buy.dataprovider.BuyError;
import com.shopify.buy.dataprovider.Callback;
import com.shopify.buy.dataprovider.RetrofitError;
import com.shopify.buy.extensions.ShopifyAndroidTestCase;
import com.shopify.buy.model.AccountCredentials;
import com.shopify.buy.model.Address;
import com.shopify.buy.model.Customer;
import com.shopify.buy.model.CustomerToken;
import com.shopify.buy.model.Order;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;
import java.util.Random;
import java.util.UUID;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.fail;


@RunWith(AndroidJUnit4.class)
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

    @Test
	public void testCustomerCreation() throws InterruptedException {
        final Customer randomCustomer = USE_MOCK_RESPONSES ? getExistingCustomer() : generateRandomCustomer();
        final AccountCredentials accountCredentials = new AccountCredentials(randomCustomer.getEmail(), PASSWORD, randomCustomer.getFirstName(), randomCustomer.getLastName());

        buyClient.createCustomer(accountCredentials, new Callback<Customer>() {
            @Override
            public void success(Customer customer) {
                assertNotNull(customer);
                assertEquals(randomCustomer.getEmail(), customer.getEmail());
                assertEquals(randomCustomer.getFirstName(), customer.getFirstName());
                assertEquals(randomCustomer.getLastName(), customer.getLastName());
            }

            @Override
            public void failure(RetrofitError error) {
                fail(BuyClientUtils.getErrorBody(error));
            }
        });
    }

    @Suppress
    @Test
	public void testCustomerActivation() throws InterruptedException {
        
        testCustomerLogin();

        final AccountCredentials accountCredentials = new AccountCredentials(customer.getEmail(), PASSWORD, customer.getFirstName(), customer.getLastName());

        // TODO update this test when we start to get real tokens
        buyClient.activateCustomer(customer.getId(), "need activation token not access token", accountCredentials, new Callback<Customer>() {
            @Override
            public void success(Customer customer) {
                assertNotNull(customer);
            }

            @Override
            public void failure(RetrofitError error) {
                fail(BuyClientUtils.getErrorBody(error));
            }
        });
    }


    @Test
	public void testCustomerLogin() throws InterruptedException {
        
        customer = getExistingCustomer();

        final AccountCredentials accountCredentials = new AccountCredentials(customer.getEmail(), PASSWORD, customer.getFirstName(), customer.getLastName());

        buyClient.loginCustomer(accountCredentials, new Callback<CustomerToken>() {
            @Override
            public void success(CustomerToken customerToken) {
                assertNotNull(buyClient.getCustomerToken());
                assertEquals(false, buyClient.getCustomerToken().getAccessToken().isEmpty());
                CustomerTest.this.customerToken = buyClient.getCustomerToken();
                getCustomerAfterLogin();
            }

            @Override
            public void failure(RetrofitError error) {
                fail(BuyClientUtils.getErrorBody(error));
            }
        });
    }

    @Test
	public void testCustomerLogout() throws InterruptedException {
        
        testCustomerLogin();
        buyClient.setCustomerToken(customerToken);

        buyClient.logoutCustomer(new Callback<Void>() {
            @Override
            public void success(Void aVoid) {
            }

            @Override
            public void failure(RetrofitError error) {
                fail(BuyClientUtils.getErrorBody(error));
            }
        });
    }

    @Test
	public void testCustomerRenew() throws InterruptedException {
        
        testCustomerLogin();
        buyClient.setCustomerToken(customerToken);

        buyClient.renewCustomer(new Callback<CustomerToken>() {
            @Override
            public void success(CustomerToken customerToken) {
            }

            @Override
            public void failure(RetrofitError error) {
                fail(BuyClientUtils.getErrorBody(error));
            }
        });
    }

    @Test
	public void testCustomerRecover() throws InterruptedException {
        
        customer = getExistingCustomer();

        buyClient.recoverPassword(EMAIL, new Callback<Void>() {
            @Override
            public void success(Void aVoid) {
            }

            @Override
            public void failure(RetrofitError error) {
                fail(BuyClientUtils.getErrorBody(error));
            }
        });
    }

    @Test
	public void testCustomerUpdate() throws InterruptedException {
        
        testCustomerLogin();
        buyClient.setCustomerToken(customerToken);


        customer.setLastName("Foo");

        buyClient.updateCustomer(customer, new Callback<Customer>() {
            @Override
            public void success(Customer customer) {
                assertNotNull(customer);
                assertEquals("Foo", customer.getLastName());
            }

            @Override
            public void failure(RetrofitError error) {
                fail(BuyClientUtils.getErrorBody(error));
            }
        });
    }

    @Test
	public void testGetCustomerOrders() throws InterruptedException {
        
        testCustomerLogin();
        buyClient.setCustomerToken(customerToken);

        buyClient.getOrders(customer, new Callback<List<Order>>() {
            @Override
            public void success(List<Order> orders) {
                assertNotNull(orders);
                assertEquals(true, orders.size() > 0);
                CustomerTest.this.orders = orders;
            }

            @Override
            public void failure(RetrofitError error) {
                fail(BuyClientUtils.getErrorBody(error));
            }

        });
    }

    @Test
	public void testGetOrder() throws InterruptedException {
        
        testGetCustomerOrders();
        buyClient.setCustomerToken(customerToken);


        String orderId = orders.get(0).getOrderId();

        buyClient.getOrder(customer, orderId, new Callback<Order>() {
            @Override
            public void success(Order order) {
                assertNotNull(order);
            }

            @Override
            public void failure(RetrofitError error) {
                fail(BuyClientUtils.getErrorBody(error));
            }
        });
    }

    @Test
	public void testGetCustomer() throws InterruptedException {
        
        testCustomerLogin();
        buyClient.setCustomerToken(customerToken);

        buyClient.getCustomer(customerToken.getCustomerId(), new Callback<Customer>() {
            @Override
            public void success(Customer customer) {
                assertNotNull(customer);
            }

            @Override
            public void failure(RetrofitError error) {
                fail(BuyClientUtils.getErrorBody(error));
            }
        });
    }

    @Test
	public void testCreateAddress() throws InterruptedException {
        
        testCustomerLogin();
        buyClient.setCustomerToken(customerToken);

        final Address inputAddress = USE_MOCK_RESPONSES ? getExistingAddress() : generateAddress();

        buyClient.createAddress(customer, inputAddress, new Callback<Address>() {
            @Override
            public void success(Address address) {
                assertEquals(inputAddress.getAddress1(), address.getAddress1());
                assertEquals(inputAddress.getAddress2(), address.getAddress2());
                assertEquals(inputAddress.getCity(), address.getCity());
            }

            @Override
            public void failure(RetrofitError error) {
                fail(BuyClientUtils.getErrorBody(error));
            }
        });
    }

    @Test
	public void testGetAddresses() throws InterruptedException {
        
        testCustomerLogin();
        buyClient.setCustomerToken(customerToken);

        buyClient.getAddresses(customer, new Callback<List<Address>>() {
            @Override
            public void success(List<Address> addresses) {
                assertNotNull(addresses);
                assertEquals(true, addresses.size() > 0);
                CustomerTest.this.addresses = addresses;
            }

            @Override
            public void failure(RetrofitError error) {
                fail(BuyClientUtils.getErrorBody(error));
            }
        });
    }

    @Test
	public void testGetAddress() throws InterruptedException {
        
        testGetAddresses();
        buyClient.setCustomerToken(customerToken);

        String addressId = addresses.get(0).getAddressId();

        buyClient.getAddress(customer, addressId, new Callback<Address>() {
            @Override
            public void success(Address address) {
                assertNotNull(address);
                CustomerTest.this.address = address;
            }

            @Override
            public void failure(RetrofitError error) {
                fail(BuyClientUtils.getErrorBody(error));
            }
        });
    }

    @Test
	public void testUpdateAddress() throws InterruptedException {
        
        testGetAddress();
        buyClient.setCustomerToken(customerToken);

        address.setCity("Toledo");

        buyClient.updateAddress(customer, address, new Callback<Address>() {
            @Override
            public void success(Address address) {
                assertNotNull(address);
            }

            @Override
            public void failure(RetrofitError error) {
                fail(BuyClientUtils.getErrorBody(error));
            }
        });
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

    private void getCustomerAfterLogin(){

        buyClient.getCustomer(customerToken.getCustomerId(), new Callback<Customer>() {

            @Override
            public void success(Customer customer) {
                assertNotNull(customer);
                CustomerTest.this.customer = customer;
                CustomerTest.this.customerToken = buyClient.getCustomerToken();
            }

            @Override
            public void failure(RetrofitError error) {
                fail(BuyClientUtils.getErrorBody(error));
            }
        });

    }

    @Test
    public void testCustomerCreationDuplicateEmail() throws InterruptedException {

        final Customer input = getExistingCustomer();
        final AccountCredentials accountCredentials = new AccountCredentials(input.getEmail(), PASSWORD, input.getFirstName(), input.getLastName());

        buyClient.createCustomer(accountCredentials, new Callback<Customer>() {
            @Override
            public void success(Customer customer) {
                fail("Should not be able to create multiple accounts with same email");
            }

            @Override
            public void failure(RetrofitError error) {
                if (BuyError.isEmailAlreadyTakenError(error, input.getEmail())) {
                } else {
                    fail(String.format("Should be getting email already taken error. \nGot \"%s\"", error.getMessage()));
                }
            }
        });
    }

    @Test
    public void testCustomerInvalidLogin() throws InterruptedException {
        final Customer customer = getExistingCustomer();
        final AccountCredentials accountCredentials = new AccountCredentials(customer.getEmail(), WRONG_PASSWORD);

        buyClient.loginCustomer(accountCredentials, new Callback<CustomerToken>() {

            @Override
            public void success(CustomerToken customerToken) {
                fail("Invalid credentials should not be able to login");
            }

            @Override
            public void failure(RetrofitError error) {
                assertEquals(401, error.getCode());
                assertEquals("Unauthorized", error.getMessage());
            }
        });
    }

    @Test
    public void testCreateCustomerInvalidEmailPassword() throws InterruptedException {
        final AccountCredentials accountCredentials = new AccountCredentials(MALFORMED_EMAIL, WRONG_PASSWORD, OTHER_WRONG_PASSWORD, FIRSTNAME, LASTNAME);

        buyClient.createCustomer(accountCredentials, new Callback<Customer>() {

            @Override
            public void success(Customer customerToken) {
                fail("Invalid email, password, confirmation password. Should not be able to create a customer.");
            }

            @Override
            public void failure(RetrofitError error) {
                List<BuyError> buyErrors = BuyError.errorsForCustomer(error);
                assertEquals(3, buyErrors.size());

                BuyError passwordTooShort = buyErrors.get(0);
                assertEquals(passwordTooShort.getCode(), "too_short");
                assertEquals(passwordTooShort.getMessage(), "is too short (minimum is 5 characters)");
                assertEquals(1, passwordTooShort.getOptions().size());

                BuyError confirmationMatch = buyErrors.get(1);
                assertEquals(confirmationMatch.getCode(), "confirmation");
                assertEquals(confirmationMatch.getMessage(), "doesn't match Password");
                assertEquals(1, confirmationMatch.getOptions().size());

                BuyError invalidEmail = buyErrors.get(2);
                assertEquals(invalidEmail.getCode(), "invalid");
                assertEquals(invalidEmail.getMessage(), "is invalid");
                assertEquals(0, invalidEmail.getOptions().size());

                assertEquals(422, error.getCode());
                assertEquals("Unprocessable Entity", error.getMessage());
            }
        });
    }
}
