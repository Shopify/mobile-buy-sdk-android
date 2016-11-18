package com.shopify.buy.dataprovider;

import com.shopify.buy.model.AccountCredentials;
import com.shopify.buy.model.Customer;
import com.shopify.buy.model.CustomerToken;

import rx.Observable;

/**
 * Represents request interceptor for Product API calls {@link com.shopify.buy.dataprovider.CustomerService}
 */
public interface CustomerApiInterceptor {

    /**
     * Intercepts request to {@link com.shopify.buy.dataprovider.CustomerService#createCustomer(AccountCredentials)}
     *
     * @param accountCredentials the account credentials with an email, password, first name, and last name of the {@link Customer} to be created, not null
     * @param originalObservable original request observable
     * @return modified or the same version of original request observable
     */
    Observable<Customer> createCustomer(AccountCredentials accountCredentials, Observable<Customer> originalObservable);

    /**
     * Intercepts request to {@link com.shopify.buy.dataprovider.CustomerService#activateCustomer(Long, String, AccountCredentials)}
     *
     * @param customerId         the id of the {@link Customer} to activate, not null
     * @param activationToken    the activation token for the Customer, not null or empty
     * @param accountCredentials the account credentials with a password of the {@link Customer} to be activated, not null
     * @param originalObservable original request observable
     * @return modified or the same version of original request observable
     */
    Observable<Customer> activateCustomer(Long customerId, String activationToken, AccountCredentials accountCredentials, Observable<Customer> originalObservable);

    /**
     * Intercepts request to {@link com.shopify.buy.dataprovider.CustomerService#resetPassword(Long, String, AccountCredentials)}
     *
     * @param customerId         the id of the {@link Customer} to activate, not null
     * @param resetToken         the reset token for the Customer, not null or empty
     * @param accountCredentials the account credentials with the new password of the {@link Customer}. not null
     * @param originalObservable original request observable
     * @return modified or the same version of original request observable
     */
    Observable<Customer> resetPassword(Long customerId, String resetToken, AccountCredentials accountCredentials, Observable<Customer> originalObservable);

    /**
     * Intercepts request to {@link com.shopify.buy.dataprovider.CustomerService#loginCustomer(AccountCredentials)}
     *
     * @param accountCredentials the account credentials with an email and password of the {@link Customer}, not null
     * @param originalObservable original request observable
     * @return modified or the same version of original request observable
     */
    Observable<CustomerToken> loginCustomer(AccountCredentials accountCredentials, Observable<CustomerToken> originalObservable);

    /**
     * Intercepts request to {@link com.shopify.buy.dataprovider.CustomerService#logoutCustomer()}
     *
     * @param customerToken      logged in customer token
     * @param originalObservable original request observable
     * @return modified or the same version of original request observable
     */
    Observable<Void> logoutCustomer(CustomerToken customerToken, Observable<Void> originalObservable);

    /**
     * Intercepts request to {@link com.shopify.buy.dataprovider.CustomerService#updateCustomer(Customer)}
     *
     * @param customer           the {@link Customer} to update, not null
     * @param originalObservable original request observable
     * @return modified or the same version of original request observable
     */
    Observable<Customer> updateCustomer(Customer customer, Observable<Customer> originalObservable);

    /**
     * Intercepts request to {@link com.shopify.buy.dataprovider.CustomerService#getCustomer()}
     *
     * @param originalObservable original request observable
     * @return modified or the same version of original request observable
     */
    Observable<Customer> getCustomer(Observable<Customer> originalObservable);

    /**
     * Intercepts request to {@link com.shopify.buy.dataprovider.CustomerService#renewCustomer()}
     *
     * @param originalObservable original request observable
     * @return modified or the same version of original request observable
     */
    Observable<CustomerToken> renewCustomer(Observable<CustomerToken> originalObservable);

    /**
     * Intercepts request to {@link com.shopify.buy.dataprovider.CustomerService#recoverPassword(String)}
     *
     * @param email              the email address to send the password recovery email to, not null or empty
     * @param originalObservable original request observable
     * @return modified or the same version of original request observable
     */
    Observable<Void> recoverPassword(String email, Observable<Void> originalObservable);
}
