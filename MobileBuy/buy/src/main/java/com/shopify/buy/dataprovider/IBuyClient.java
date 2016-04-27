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

package com.shopify.buy.dataprovider;

import com.shopify.buy.model.AccountCredentials;
import com.shopify.buy.model.Address;
import com.shopify.buy.model.Checkout;
import com.shopify.buy.model.Collection;
import com.shopify.buy.model.CreditCard;
import com.shopify.buy.model.Customer;
import com.shopify.buy.model.CustomerToken;
import com.shopify.buy.model.GiftCard;
import com.shopify.buy.model.Order;
import com.shopify.buy.model.Product;
import com.shopify.buy.model.ShippingRate;
import com.shopify.buy.model.Shop;

import java.util.List;

import rx.Observable;
import rx.Scheduler;

public interface IBuyClient {

    String getApiKey();

    String getAppId();

    String getApplicationName();

    String getWebReturnToUrl();

    String getWebReturnToLabel();

    String getShopDomain();

    int getPageSize();

    void setCustomerToken(CustomerToken customerToken);

    Scheduler getCallbackScheduler();

    void setCallbackScheduler(Scheduler callbackScheduler);

    CustomerToken getCustomerToken();

    void setWebReturnToUrl(String webReturnToUrl);

    void setWebReturnToLabel(String webReturnToLabel);

    void setPageSize(int pageSize);

    void getShop(Callback<Shop> callback);

    Observable<Shop> getShop();

    void getProductPage(int page, Callback<List<Product>> callback);

    Observable<List<Product>> getProductPage(int page);

    void getProductWithHandle(String handle, Callback<Product> callback);

    Observable<Product> getProductWithHandle(String handle);

    void getProduct(String productId, Callback<Product> callback);

    Observable<Product> getProduct(String productId);

    void getProducts(List<String> productIds, Callback<List<Product>> callback);

    Observable<List<Product>> getProducts(List<String> productIds);

    void getProducts(int page, String collectionId, Callback<List<Product>> callback);

    Observable<List<Product>> getProducts(int page, String collectionId);

    void getProducts(int page, String collectionId, Collection.SortOrder sortOrder, Callback<List<Product>> callback);

    Observable<List<Product>> getProducts(int page, String collectionId, Collection.SortOrder sortOrder);

    void getCollections(Callback<List<Collection>> callback);

    Observable<List<Collection>> getCollections();

    void getCollectionPage(int page, Callback<List<Collection>> callback);

    Observable<List<Collection>> getCollections(int page);

    void createCheckout(Checkout checkout, Callback<Checkout> callback);

    Observable<Checkout> createCheckout(Checkout checkout);

    void updateCheckout(Checkout checkout, Callback<Checkout> callback);

    Observable<Checkout> updateCheckout(Checkout checkout);

    void getShippingRates(String checkoutToken, Callback<List<ShippingRate>> callback);

    Observable<List<ShippingRate>> getShippingRates(String checkoutToken);

    void storeCreditCard(CreditCard card, Checkout checkout, Callback<Checkout> callback);

    Observable<Checkout> storeCreditCard(CreditCard card, Checkout checkout);

    void completeCheckout(Checkout checkout, Callback<Checkout> callback);

    Observable<Checkout> completeCheckout(Checkout checkout);

    void getCheckoutCompletionStatus(Checkout checkout, Callback<Boolean> callback);

    Observable<Boolean> getCheckoutCompletionStatus(Checkout checkout);

    void getCheckout(String checkoutToken, Callback<Checkout> callback);

    Observable<Checkout> getCheckout(String checkoutToken);

    void applyGiftCard(String giftCardCode, Checkout checkout, Callback<Checkout> callback);

    Observable<Checkout> applyGiftCard(String giftCardCode, Checkout checkout);

    void removeGiftCard(GiftCard giftCard, Checkout checkout, Callback<Checkout> callback);

    Observable<Checkout> removeGiftCard(GiftCard giftCard, Checkout checkout);

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

    void getOrders(Customer customer, Callback<List<Order>> callback);

    Observable<List<Order>> getOrders(Customer customer);

    void getOrder(Customer customer, String orderId, Callback<Order> callback);

    Observable<Order> getOrder(Customer customer, String orderId);

    void createAddress(Customer customer, Address address, Callback<Address> callback);

    Observable<Address> createAddress(Customer customer, Address address);

    void getAddresses(Customer customer, Callback<List<Address>> callback);

    Observable<List<Address>> getAddresses(Customer customer);

    void getAddress(Customer customer, String addressId, Callback<Address> callback);

    Observable<Address> getAddress(Customer customer, String addressId);

    void updateAddress(Customer customer, Address address, Callback<Address> callback);

    Observable<Address> updateAddress(Customer customer, Address address);

    void removeProductReservationsFromCheckout(Checkout checkout, Callback<Checkout> callback);

    Observable<Checkout> removeProductReservationsFromCheckout(Checkout checkout);
}
