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

/**
 * Service that provides Customer API endpoints.
 */
public interface CustomerService {

    /**
     * Returns customer token
     *
     * @return token
     */
    CustomerToken getCustomerToken();

    /**
     * Create a new Customer on Shopify
     *
     * @param accountCredentials the account credentials with an email, password, first name, and last name of the {@link Customer} to be created, not null
     * @param callback           the {@link Callback} that will be used to indicate the response from the asynchronous network operation, not null
     * @return cancelable task
     */
    CancellableTask createCustomer(AccountCredentials accountCredentials, Callback<Customer> callback);

    /**
     * Create a new Customer on Shopify
     *
     * @param accountCredentials the account credentials with an email, password, first name, and last name of the {@link Customer} to be created, not null
     * @return cold observable that emits created new customer
     */
    Observable<Customer> createCustomer(AccountCredentials accountCredentials);

    /**
     * Activate the customer account.
     *
     * @param customerId         the id of the {@link Customer} to activate, not null
     * @param activationToken    the activation token for the Customer, not null or empty
     * @param accountCredentials the account credentials with a password of the {@link Customer} to be activated, not null
     * @param callback           the {@link Callback} that will be used to indicate the response from the asynchronous network operation, not null
     * @return cancelable task
     */
    CancellableTask activateCustomer(Long customerId, String activationToken, AccountCredentials accountCredentials, Callback<Customer> callback);

    /**
     * Activate the customer account.
     *
     * @param customerId         the id of the {@link Customer} to activate, not null
     * @param activationToken    the activation token for the Customer, not null or empty
     * @param accountCredentials the account credentials with a password of the {@link Customer} to be activated, not null
     * @return cold observable that emits activated customer account
     */
    Observable<Customer> activateCustomer(Long customerId, String activationToken, AccountCredentials accountCredentials);

    /**
     * Reset the password for the customer account.
     *
     * @param customerId         the id of the {@link Customer} to activate, not null
     * @param resetToken         the reset token for the Customer, not null or empty
     * @param accountCredentials the account credentials with the new password of the {@link Customer}. not null
     * @param callback           the {@link Callback} that will be used to indicate the response from the asynchronous network operation, not null
     * @return cancelable task
     */
    CancellableTask resetPassword(Long customerId, String resetToken, AccountCredentials accountCredentials, Callback<Customer> callback);

    /**
     * Reset the password for the customer account.
     *
     * @param customerId         the id of the {@link Customer} to activate, not null
     * @param resetToken         the reset token for the Customer, not null or empty
     * @param accountCredentials the account credentials with the new password of the {@link Customer}. not null
     * @return cold observable that emits customer account with reset password
     */
    Observable<Customer> resetPassword(Long customerId, String resetToken, AccountCredentials accountCredentials);

    /**
     * Log an existing Customer into Shopify
     *
     * @param accountCredentials the account credentials with an email and password of the {@link Customer}, not null
     * @param callback           the {@link Callback} that will be used to indicate the response from the asynchronous network operation, not null
     * @return cancelable task
     */
    CancellableTask loginCustomer(AccountCredentials accountCredentials, Callback<CustomerToken> callback);

    /**
     * Log an existing Customer into Shopify
     *
     * @param accountCredentials the account credentials with an email and password of the {@link Customer}, not null
     * @return cold observable that emits logged in customer token
     */
    Observable<CustomerToken> loginCustomer(AccountCredentials accountCredentials);

    /**
     * Log a Customer out from Shopify
     *
     * @param callback the {@link Callback} that will be used to indicate the response from the asynchronous network operation, not null
     * @return cancelable task
     */
    CancellableTask logoutCustomer(Callback<Void> callback);

    /**
     * Log a Customer out from Shopify
     *
     * @return cold observable that log customer out and emits nothing
     */
    Observable<Void> logoutCustomer();

    /**
     * Update an existing Customer's attributes.
     *
     * @param customer the {@link Customer} to update, not null
     * @param callback the {@link Callback} that will be used to indicate the response from the asynchronous network operation, not null
     * @return cancelable task
     */
    CancellableTask updateCustomer(Customer customer, Callback<Customer> callback);

    /**
     * Update an existing Customer's attributes.
     *
     * @param customer the {@link Customer} to update, not null
     * @return cold observable that emits updated existent customer attributes
     */
    Observable<Customer> updateCustomer(Customer customer);

    /**
     * Retrieve a Customer's details from Shopify.
     *
     * @param customerId the identifier of a {@link CustomerToken} or {@link Customer}, not null
     * @param callback   the {@link Callback} that will be used to indicate the response from the asynchronous network operation, not null
     * @return cancelable task
     */
    CancellableTask getCustomer(Long customerId, Callback<Customer> callback);

    /**
     * Retrieve a Customer's details from Shopify.
     *
     * @param customerId the identifier of a {@link CustomerToken} or {@link Customer}, not null
     * @return cold observable tha emits requested customer details
     */
    Observable<Customer> getCustomer(Long customerId);

    /**
     * Renew a Customer login.  This should be called periodically to keep the token up to date.
     *
     * @param callback the {@link Callback} that will be used to indicate the response from the asynchronous network operation, not null
     * @return cancelable task
     */
    CancellableTask renewCustomer(Callback<CustomerToken> callback);

    /**
     * Renew a Customer login.  This should be called periodically to keep the token up to date.
     *
     * @return cold observable that emits renewed customer token
     */
    Observable<CustomerToken> renewCustomer();

    /**
     * Send a password recovery email. An email will be sent to the email address specified if a customer with that email address exists on Shopify.
     *
     * @param email    the email address to send the password recovery email to, not null or empty
     * @param callback the {@link Callback} that will be used to indicate the response from the asynchronous network operation, not null
     * @return cancelable task
     */
    CancellableTask recoverPassword(String email, Callback<Void> callback);

    /**
     * Send a password recovery email. An email will be sent to the email address specified if a customer with that email address exists on Shopify.
     *
     * @param email the email address to send the password recovery email to, not null or empty
     * @return cold observable that sends a password recovery email and emits nothing
     */
    Observable<Void> recoverPassword(String email);
}
