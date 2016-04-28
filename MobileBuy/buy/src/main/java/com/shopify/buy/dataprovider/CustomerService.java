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
package com.shopify.buy.dataprovider;

import com.shopify.buy.model.AccountCredentials;
import com.shopify.buy.model.Customer;
import com.shopify.buy.model.CustomerToken;

import rx.Observable;

public interface CustomerService {

    void createCustomer(AccountCredentials accountCredentials, Callback<Customer> callback);

    Observable<Customer> createCustomer(AccountCredentials accountCredentials);

    void activateCustomer(Long customerId, String activationToken, AccountCredentials accountCredentials, Callback<Customer> callback);

    Observable<Customer> activateCustomer(Long customerId, String activationToken, AccountCredentials accountCredentials);

    void resetPassword(Long customerId, String resetToken, AccountCredentials accountCredentials, Callback<Customer> callback);

    Observable<Customer> resetPassword(Long customerId, String resetToken, AccountCredentials accountCredentials);

    void loginCustomer(AccountCredentials accountCredentials, Callback<CustomerToken> callback);

    Observable<CustomerToken> loginCustomer(AccountCredentials accountCredentials);

    void logoutCustomer(Callback<Void> callback);

    Observable<Void> logoutCustomer();

    void updateCustomer(Customer customer, Callback<Customer> callback);

    Observable<Customer> updateCustomer(Customer customer);

    void getCustomer(Long customerId, Callback<Customer> callback);

    Observable<Customer> getCustomer(Long customerId);

    void renewCustomer(Callback<CustomerToken> callback);

    Observable<CustomerToken> renewCustomer();

    void recoverPassword(String email, Callback<Void> callback);

    Observable<Void> recoverPassword(String email);
}
