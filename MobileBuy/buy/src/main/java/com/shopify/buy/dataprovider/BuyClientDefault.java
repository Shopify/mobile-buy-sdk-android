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

import android.text.TextUtils;

import com.shopify.buy.model.AccountCredentials;
import com.shopify.buy.model.Address;
import com.shopify.buy.model.Checkout;
import com.shopify.buy.model.Collection;
import com.shopify.buy.model.Collection.SortOrder;
import com.shopify.buy.model.CreditCard;
import com.shopify.buy.model.Customer;
import com.shopify.buy.model.CustomerToken;
import com.shopify.buy.model.Order;
import com.shopify.buy.model.PaymentToken;
import com.shopify.buy.model.Product;
import com.shopify.buy.model.ShippingRate;
import com.shopify.buy.model.Shop;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Scheduler;
import rx.schedulers.Schedulers;

/**
 * Default implementation of {@link BuyClient} represents a facade for all Shopify Checkout API calls.
 */
final class BuyClientDefault implements BuyClient {

    private static final String CUSTOMER_TOKEN_HEADER = "X-Shopify-Customer-Access-Token";

    private final String shopDomain;
    private final String apiKey;
    private final String appId;
    private final String applicationName;

    final StoreService storeService;
    final AddressService addressService;
    final CheckoutService checkoutService;
    final CustomerService customerService;
    final OrderService orderService;
    final ProductService productService;

    BuyClientDefault(
        final String apiKey,
        final String appId,
        final String applicationName,
        final String shopDomain,
        final CustomerToken customerToken,
        final Scheduler callbackScheduler,
        final int productPageSize,
        final int networkRequestRetryMaxCount,
        final long networkRequestRetryDelayMs,
        final float networkRequestRetryBackoffMultiplier,
        final long httpConnectionTimeoutMs,
        final long httpReadWriteTimeoutMs,
        final Interceptor... interceptors
    ) {
        this.apiKey = apiKey;
        this.appId = appId;
        this.applicationName = applicationName;
        this.shopDomain = shopDomain;

        final Interceptor requestInterceptor = new Interceptor() {
            @Override
            public okhttp3.Response intercept(final Chain chain) throws IOException {
                final Request original = chain.request();
                final Request.Builder builder = original.newBuilder().method(original.method(), original.body());
                builder.header("Authorization", BuyClientUtils.formatBasicAuthorization(apiKey));

                final CustomerToken customerToken = customerService.getCustomerToken();
                if (customerToken != null && !TextUtils.isEmpty(customerToken.getAccessToken())) {
                    builder.header(CUSTOMER_TOKEN_HEADER, customerToken.getAccessToken());
                }

                // Using the full package name for BuildConfig here as a work around for Javadoc.  The source paths need to be adjusted
                builder.header("User-Agent", "Mobile Buy SDK Android/" + com.shopify.buy.BuildConfig.VERSION_NAME + "/" + applicationName);

                return chain.proceed(builder.build());
            }
        };

        final OkHttpClient.Builder builder = new OkHttpClient.Builder()
            .connectTimeout(httpConnectionTimeoutMs, TimeUnit.MILLISECONDS)
            .readTimeout(httpReadWriteTimeoutMs, TimeUnit.MILLISECONDS)
            .writeTimeout(httpReadWriteTimeoutMs, TimeUnit.MILLISECONDS)
            .addInterceptor(requestInterceptor);

        if (interceptors != null) {
            for (Interceptor interceptor : interceptors) {
                builder.addInterceptor(interceptor);
            }
        }

        final OkHttpClient httpClient = builder.build();

        final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://" + shopDomain + "/")
            .addConverterFactory(GsonConverterFactory.create(BuyClientUtils.createDefaultGson()))
            .addCallAdapterFactory(RxJavaCallAdapterFactory.createWithScheduler(Schedulers.io()))
            .client(httpClient)
            .build();

        final NetworkRetryPolicyProvider networkRetryPolicyProvider = new NetworkRetryPolicyProvider(networkRequestRetryMaxCount, networkRequestRetryDelayMs, networkRequestRetryBackoffMultiplier);

        storeService = new StoreServiceDefault(retrofit, networkRetryPolicyProvider, callbackScheduler);
        checkoutService = new CheckoutServiceDefault(retrofit, apiKey, applicationName, networkRetryPolicyProvider, callbackScheduler);
        customerService = new CustomerServiceDefault(retrofit, customerToken, networkRetryPolicyProvider, callbackScheduler);
        addressService = new AddressServiceDefault(retrofit, networkRetryPolicyProvider, callbackScheduler, customerService);
        orderService = new OrderServiceDefault(retrofit, networkRetryPolicyProvider, callbackScheduler, customerService);
        productService = new ProductServiceDefault(retrofit, appId, productPageSize, networkRetryPolicyProvider, callbackScheduler);
    }

    @Override
    public String getApiKey() {
        return apiKey;
    }

    @Override
    public String getAppId() {
        return appId;
    }

    @Override
    public String getApplicationName() {
        return applicationName;
    }

    @Override
    public String getShopDomain() {
        return shopDomain;
    }

    // ----------- StoreService API ---------------

    @Override
    public CancellableTask getShop(Callback<Shop> callback) {
        return storeService.getShop(callback);
    }

    @Override
    public Observable<Shop> getShop() {
        return storeService.getShop();
    }

    // ----------- CheckoutService API ---------------

    @Override
    public CancellableTask createCheckout(final Checkout checkout, final Callback<Checkout> callback) {
        return checkoutService.createCheckout(checkout, callback);
    }

    @Override
    public Observable<Checkout> createCheckout(final Checkout checkout) {
        return checkoutService.createCheckout(checkout);
    }

    @Override
    public CancellableTask updateCheckout(final Checkout checkout, final Callback<Checkout> callback) {
        return checkoutService.updateCheckout(checkout, callback);
    }

    @Override
    public Observable<Checkout> updateCheckout(final Checkout checkout) {
        return checkoutService.updateCheckout(checkout);
    }

    @Override
    public CancellableTask getShippingRates(final String checkoutToken, final Callback<List<ShippingRate>> callback) {
        return checkoutService.getShippingRates(checkoutToken, callback);
    }

    @Override
    public Observable<List<ShippingRate>> getShippingRates(final String checkoutToken) {
        return checkoutService.getShippingRates(checkoutToken);
    }

    @Override
    public CancellableTask storeCreditCard(final CreditCard card, final Checkout checkout, final Callback<PaymentToken> callback) {
        return checkoutService.storeCreditCard(card, checkout, callback);
    }

    @Override
    public Observable<PaymentToken> storeCreditCard(final CreditCard card, final Checkout checkout) {
        return checkoutService.storeCreditCard(card, checkout);
    }

    @Override
    public CancellableTask getCheckoutCompletionStatus(String checkoutToken, final Callback<Boolean> callback) {
        return checkoutService.getCheckoutCompletionStatus(checkoutToken, callback);
    }

    @Override
    public Observable<Boolean> getCheckoutCompletionStatus(final String checkoutToken) {
        return checkoutService.getCheckoutCompletionStatus(checkoutToken);
    }

    @Override
    public Observable<Checkout> completeCheckout(final PaymentToken paymentToken, final String checkoutToken) {
        return checkoutService.completeCheckout(paymentToken, checkoutToken);
    }

    @Override
    public CancellableTask completeCheckout(final PaymentToken paymentToken, final String checkoutToken, final Callback<Checkout> callback) {
        return checkoutService.completeCheckout(paymentToken, checkoutToken, callback);
    }

    @Override
    public CancellableTask getCheckout(final String checkoutToken, final Callback<Checkout> callback) {
        return checkoutService.getCheckout(checkoutToken, callback);
    }

    @Override
    public Observable<Checkout> getCheckout(final String checkoutToken) {
        return checkoutService.getCheckout(checkoutToken);
    }

    @Override
    public CancellableTask applyGiftCard(final String giftCardCode, final Checkout checkout, final Callback<Checkout> callback) {
        return checkoutService.applyGiftCard(giftCardCode, checkout, callback);
    }

    @Override
    public Observable<Checkout> applyGiftCard(final String giftCardCode, final Checkout checkout) {
        return checkoutService.applyGiftCard(giftCardCode, checkout);
    }

    @Override
    public CancellableTask removeGiftCard(final Long giftCardId, final Checkout checkout, final Callback<Checkout> callback) {
        return checkoutService.removeGiftCard(giftCardId, checkout, callback);
    }

    @Override
    public Observable<Checkout> removeGiftCard(final Long giftCardId, final Checkout checkout) {
        return checkoutService.removeGiftCard(giftCardId, checkout);
    }

    @Override
    public CancellableTask removeProductReservationsFromCheckout(final String checkoutToken, final Callback<Checkout> callback) {
        return checkoutService.removeProductReservationsFromCheckout(checkoutToken, callback);
    }

    @Override
    public Observable<Checkout> removeProductReservationsFromCheckout(final String checkoutToken) {
        return checkoutService.removeProductReservationsFromCheckout(checkoutToken);
    }

    // ----------- CustomerService API ---------------

    @Override
    public CustomerToken getCustomerToken() {
        return customerService.getCustomerToken();
    }

    @Override
    public void setCustomerToken(CustomerToken customerToken) {
        customerService.setCustomerToken(customerToken);
    }

    @Override
    public CancellableTask createCustomer(AccountCredentials accountCredentials, Callback<Customer> callback) {
        return customerService.createCustomer(accountCredentials, callback);
    }

    @Override
    public Observable<Customer> createCustomer(AccountCredentials accountCredentials) {
        return customerService.createCustomer(accountCredentials);
    }

    @Override
    public CancellableTask activateCustomer(Long customerId, String activationToken, AccountCredentials accountCredentials, Callback<Customer> callback) {
        return customerService.activateCustomer(customerId, activationToken, accountCredentials, callback);
    }

    @Override
    public Observable<Customer> activateCustomer(Long customerId, String activationToken, AccountCredentials accountCredentials) {
        return customerService.activateCustomer(customerId, activationToken, accountCredentials);
    }

    @Override
    public CancellableTask resetPassword(Long customerId, String resetToken, AccountCredentials accountCredentials, Callback<Customer> callback) {
        return customerService.resetPassword(customerId, resetToken, accountCredentials, callback);
    }

    @Override
    public Observable<Customer> resetPassword(Long customerId, String resetToken, AccountCredentials accountCredentials) {
        return customerService.resetPassword(customerId, resetToken, accountCredentials);
    }

    @Override
    public CancellableTask loginCustomer(AccountCredentials accountCredentials, Callback<Customer> callback) {
        return customerService.loginCustomer(accountCredentials, callback);
    }

    @Override
    public Observable<Customer> loginCustomer(AccountCredentials accountCredentials) {
        return customerService.loginCustomer(accountCredentials);
    }

    @Override
    public CancellableTask logoutCustomer(Callback<Void> callback) {
        return customerService.logoutCustomer(callback);
    }

    @Override
    public Observable<Void> logoutCustomer() {
        return customerService.logoutCustomer();
    }

    @Override
    public CancellableTask updateCustomer(Customer customer, Callback<Customer> callback) {
        return customerService.updateCustomer(customer, callback);
    }

    @Override
    public Observable<Customer> updateCustomer(Customer customer) {
        return customerService.updateCustomer(customer);
    }

    @Override
    public CancellableTask getCustomer(Callback<Customer> callback) {
        return customerService.getCustomer(callback);
    }

    @Override
    public Observable<Customer> getCustomer() {
        return customerService.getCustomer();
    }

    @Override
    public CancellableTask renewCustomer(Callback<CustomerToken> callback) {
        return customerService.renewCustomer(callback);
    }

    @Override
    public Observable<CustomerToken> renewCustomer() {
        return customerService.renewCustomer();
    }

    @Override
    public CancellableTask recoverPassword(String email, Callback<Void> callback) {
        return customerService.recoverPassword(email, callback);
    }

    @Override
    public Observable<Void> recoverPassword(String email) {
        return customerService.recoverPassword(email);
    }

    // ----------- OrderService API ---------------

    @Override
    public CancellableTask getOrders(Callback<List<Order>> callback) {
        return orderService.getOrders(callback);
    }

    @Override
    public Observable<List<Order>> getOrders() {
        return orderService.getOrders();
    }

    @Override
    public CancellableTask getOrder(Long orderId, Callback<Order> callback) {
        return orderService.getOrder(orderId, callback);
    }

    @Override
    public Observable<Order> getOrder(Long orderId) {
        return orderService.getOrder(orderId);
    }

    // ----------- AddressService API ---------------

    @Override
    public CancellableTask createAddress(final Address address, final Callback<Address> callback) {
        return addressService.createAddress(address, callback);
    }

    @Override
    public Observable<Address> createAddress(final Address address) {
        return addressService.createAddress(address);
    }

    @Override
    public CancellableTask getAddresses(final Callback<List<Address>> callback) {
        return addressService.getAddresses(callback);
    }

    @Override
    public Observable<List<Address>> getAddresses() {
        return addressService.getAddresses();
    }

    @Override
    public CancellableTask getAddress(final Long addressId, final Callback<Address> callback) {
        return addressService.getAddress(addressId, callback);
    }

    @Override
    public Observable<Address> getAddress(final Long addressId) {
        return addressService.getAddress(addressId);
    }

    @Override
    public CancellableTask updateAddress(final Address address, final Callback<Address> callback) {
        return addressService.updateAddress(address, callback);
    }

    @Override
    public CancellableTask deleteAddress(final Long addressId, final Callback<Void> callback) {
        return addressService.deleteAddress(addressId, callback);
    }

    @Override
    public Observable<Void> deleteAddress(final Long addressId) {
        return addressService.deleteAddress(addressId);
    }

    public Observable<Address> updateAddress(final Address address) {
        return addressService.updateAddress(address);
    }

    // ----------- ProductService API ---------------


    @Override
    public int getProductPageSize() {
        return productService.getProductPageSize();
    }

    @Override
    public CancellableTask getProducts(int page, Callback<List<Product>> callback) {
        return productService.getProducts(page, callback);
    }

    @Override
    public Observable<List<Product>> getProducts(int page) {
        return productService.getProducts(page);
    }

    @Override
    public CancellableTask getProductByHandle(String handle, Callback<Product> callback) {
        return productService.getProductByHandle(handle, callback);
    }

    @Override
    public Observable<Product> getProductByHandle(String handle) {
        return productService.getProductByHandle(handle);
    }

    @Override
    public CancellableTask getProduct(Long productId, Callback<Product> callback) {
        return productService.getProduct(productId, callback);
    }

    @Override
    public Observable<Product> getProduct(Long productId) {
        return productService.getProduct(productId);
    }

    @Override
    public CancellableTask getProducts(List<Long> productIds, Callback<List<Product>> callback) {
        return productService.getProducts(productIds, callback);
    }

    @Override
    public Observable<List<Product>> getProducts(List<Long> productIds) {
        return productService.getProducts(productIds);
    }

    @Override
    public CancellableTask getCollections(int page, Callback<List<Collection>> callback) {
        return productService.getCollections(page, callback);
    }

    @Override
    public CancellableTask getCollectionByHandle(String handle, Callback<Collection> callback) {
        return productService.getCollectionByHandle(handle, callback);
    }

    @Override
    public Observable<Collection> getCollectionByHandle(String handle) {
        return productService.getCollectionByHandle(handle);
    }

    @Override
    public Observable<List<Collection>> getCollections(int page) {
        return productService.getCollections(page);
    }

    @Override
    public CancellableTask getProductTags(int page, Callback<List<String>> callback) {
        return productService.getProductTags(page, callback);
    }

    @Override
    public Observable<List<String>> getProductTags(int page) {
        return productService.getProductTags(page);
    }

    @Override
    public CancellableTask getProducts(int page, Set<String> tags, Callback<List<Product>> callback) {
        return productService.getProducts(page, tags, callback);
    }

    @Override
    public Observable<List<Product>> getProducts(int page, Set<String> tags) {
        return productService.getProducts(page, tags);
    }

    @Override
    public CancellableTask getProducts(int page, Long collectionId, Set<String> tags, SortOrder sortOrder, Callback<List<Product>> callback) {
        return productService.getProducts(page, collectionId, tags, sortOrder, callback);
    }

    @Override
    public Observable<List<Product>> getProducts(int page, Long collectionId, Set<String> tags, SortOrder sortOrder) {
        return productService.getProducts(page, collectionId, tags, sortOrder);
    }
}
