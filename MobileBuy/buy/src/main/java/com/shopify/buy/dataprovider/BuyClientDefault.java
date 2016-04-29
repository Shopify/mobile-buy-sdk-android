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
import com.shopify.buy.model.GiftCard;
import com.shopify.buy.model.Order;
import com.shopify.buy.model.Payment;
import com.shopify.buy.model.Product;
import com.shopify.buy.model.ShippingRate;
import com.shopify.buy.model.Shop;

import java.io.IOException;
import java.util.List;
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

    private final StoreService storeService;
    private final AddressService addressService;
    private final CheckoutService checkoutService;
    private final CustomerService customerService;
    private final OrderService orderService;
    private final ProductService productService;

    BuyClientDefault(
            final String apiKey,
            final String appId,
            final String applicationName,
            final String shopDomain,
            final String completeCheckoutWebReturnUrl,
            final String completeCheckoutWebReturnLabel,
            final CustomerToken customerToken,
            final Scheduler callbackScheduler,
            final int productPageSize,
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
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .addInterceptor(requestInterceptor);

        for (Interceptor interceptor : interceptors) {
            builder.addInterceptor(interceptor);
        }

        final OkHttpClient httpClient = builder.build();

        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://" + shopDomain + "/")
                .addConverterFactory(GsonConverterFactory.create(BuyClientUtils.createDefaultGson()))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.createWithScheduler(Schedulers.io()))
                .client(httpClient)
                .build();

        storeService = new StoreServiceDefault(retrofit, callbackScheduler);
        addressService = new AddressServiceDefault(retrofit, callbackScheduler);
        checkoutService = new CheckoutServiceDefault(retrofit, apiKey, applicationName, completeCheckoutWebReturnUrl, completeCheckoutWebReturnLabel, callbackScheduler);
        customerService = new CustomerServiceDefault(retrofit, customerToken, callbackScheduler);
        orderService = new OrderServiceDefault(retrofit, callbackScheduler);
        productService = new ProductServiceDefault(retrofit, appId, productPageSize, callbackScheduler);
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
    public void getShop(Callback<Shop> callback) {
        storeService.getShop(callback);
    }

    @Override
    public Observable<Shop> getShop() {
        return storeService.getShop();
    }

    // ----------- CheckoutService API ---------------

    @Override
    public String getWebReturnToUrl() {
        return checkoutService.getWebReturnToUrl();
    }

    @Override
    public String getWebReturnToLabel() {
        return checkoutService.getWebReturnToLabel();
    }

    @Override
    public void createCheckout(final Checkout checkout, final Callback<Checkout> callback) {
        checkoutService.createCheckout(checkout, callback);
    }

    @Override
    public Observable<Checkout> createCheckout(final Checkout checkout) {
        return checkoutService.createCheckout(checkout);
    }

    @Override
    public void updateCheckout(final Checkout checkout, final Callback<Checkout> callback) {
        checkoutService.updateCheckout(checkout, callback);
    }

    @Override
    public Observable<Checkout> updateCheckout(final Checkout checkout) {
        return checkoutService.updateCheckout(checkout);
    }

    @Override
    public void getShippingRates(final String checkoutToken, final Callback<List<ShippingRate>> callback) {
        checkoutService.getShippingRates(checkoutToken, callback);
    }

    @Override
    public Observable<List<ShippingRate>> getShippingRates(final String checkoutToken) {
        return checkoutService.getShippingRates(checkoutToken);
    }

    @Override
    public void storeCreditCard(final CreditCard card, final Checkout checkout, final Callback<Checkout> callback) {
        checkoutService.storeCreditCard(card, checkout, callback);
    }

    @Override
    public Observable<Checkout> storeCreditCard(final CreditCard card, final Checkout checkout) {
        return checkoutService.storeCreditCard(card, checkout);
    }

    @Override
    public void completeCheckout(final Checkout checkout, final Callback<Payment> callback) {
        checkoutService.completeCheckout(checkout, callback);
    }

    @Override
    public Observable<Payment> completeCheckout(final Checkout checkout) {
        return checkoutService.completeCheckout(checkout);
    }

    @Override
    public void getCheckout(final String checkoutToken, final Callback<Checkout> callback) {
        checkoutService.getCheckout(checkoutToken, callback);
    }

    @Override
    public Observable<Checkout> getCheckout(final String checkoutToken) {
        return checkoutService.getCheckout(checkoutToken);
    }

    @Override
    public void applyGiftCard(final String giftCardCode, final Checkout checkout, final Callback<Checkout> callback) {
        checkoutService.applyGiftCard(giftCardCode, checkout, callback);
    }

    @Override
    public Observable<Checkout> applyGiftCard(final String giftCardCode, final Checkout checkout) {
        return checkoutService.applyGiftCard(giftCardCode, checkout);
    }

    @Override
    public void removeGiftCard(final GiftCard giftCard, final Checkout checkout, final Callback<Checkout> callback) {
        checkoutService.removeGiftCard(giftCard, checkout, callback);
    }

    @Override
    public Observable<Checkout> removeGiftCard(final GiftCard giftCard, final Checkout checkout) {
        return checkoutService.removeGiftCard(giftCard, checkout);
    }

    @Override
    public void removeProductReservationsFromCheckout(final Checkout checkout, final Callback<Checkout> callback) {
        checkoutService.removeProductReservationsFromCheckout(checkout, callback);
    }

    @Override
    public Observable<Checkout> removeProductReservationsFromCheckout(final Checkout checkout) {
        return checkoutService.removeProductReservationsFromCheckout(checkout);
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
    public void createCustomer(AccountCredentials accountCredentials, Callback<Customer> callback) {
        customerService.createCustomer(accountCredentials, callback);
    }

    @Override
    public Observable<Customer> createCustomer(AccountCredentials accountCredentials) {
        return customerService.createCustomer(accountCredentials);
    }

    @Override
    public void activateCustomer(Long customerId, String activationToken, AccountCredentials accountCredentials, Callback<Customer> callback) {
        customerService.activateCustomer(customerId, activationToken, accountCredentials, callback);
    }

    @Override
    public Observable<Customer> activateCustomer(Long customerId, String activationToken, AccountCredentials accountCredentials) {
        return customerService.activateCustomer(customerId, activationToken, accountCredentials);
    }

    @Override
    public void resetPassword(Long customerId, String resetToken, AccountCredentials accountCredentials, Callback<Customer> callback) {
        customerService.resetPassword(customerId, resetToken, accountCredentials, callback);
    }

    @Override
    public Observable<Customer> resetPassword(Long customerId, String resetToken, AccountCredentials accountCredentials) {
        return customerService.resetPassword(customerId, resetToken, accountCredentials);
    }

    @Override
    public void loginCustomer(AccountCredentials accountCredentials, Callback<CustomerToken> callback) {
        customerService.loginCustomer(accountCredentials, callback);
    }

    @Override
    public Observable<CustomerToken> loginCustomer(AccountCredentials accountCredentials) {
        return customerService.loginCustomer(accountCredentials);
    }

    @Override
    public void logoutCustomer(Callback<Void> callback) {
        customerService.logoutCustomer(callback);
    }

    @Override
    public Observable<Void> logoutCustomer() {
        return customerService.logoutCustomer();
    }

    @Override
    public void updateCustomer(Customer customer, Callback<Customer> callback) {
        customerService.updateCustomer(customer, callback);
    }

    @Override
    public Observable<Customer> updateCustomer(Customer customer) {
        return customerService.updateCustomer(customer);
    }

    @Override
    public void getCustomer(Long customerId, Callback<Customer> callback) {
        customerService.getCustomer(customerId, callback);
    }

    @Override
    public Observable<Customer> getCustomer(Long customerId) {
        return customerService.getCustomer(customerId);
    }

    @Override
    public void renewCustomer(Callback<CustomerToken> callback) {
        customerService.renewCustomer(callback);
    }

    @Override
    public Observable<CustomerToken> renewCustomer() {
        return customerService.renewCustomer();
    }

    @Override
    public void recoverPassword(String email, Callback<Void> callback) {
        customerService.recoverPassword(email, callback);
    }

    @Override
    public Observable<Void> recoverPassword(String email) {
        return customerService.recoverPassword(email);
    }

    // ----------- OrderService API ---------------

    @Override
    public void getOrders(Customer customer, Callback<List<Order>> callback) {
        orderService.getOrders(customer, callback);
    }

    @Override
    public Observable<List<Order>> getOrders(Customer customer) {
        return orderService.getOrders(customer);
    }

    @Override
    public void getOrder(Customer customer, String orderId, Callback<Order> callback) {
        orderService.getOrder(customer, orderId, callback);
    }

    @Override
    public Observable<Order> getOrder(Customer customer, String orderId) {
        return orderService.getOrder(customer, orderId);
    }

    // ----------- AddressService API ---------------

    @Override
    public void createAddress(final Customer customer, final Address address, final Callback<Address> callback) {
        addressService.createAddress(customer, address, callback);
    }

    @Override
    public Observable<Address> createAddress(final Customer customer, final Address address) {
        return addressService.createAddress(customer, address);
    }

    @Override
    public void getAddresses(final Customer customer, final Callback<List<Address>> callback) {
        addressService.getAddresses(customer, callback);
    }

    @Override
    public Observable<List<Address>> getAddresses(final Customer customer) {
        return addressService.getAddresses(customer);
    }

    @Override
    public void getAddress(final Customer customer, final String addressId, final Callback<Address> callback) {
        addressService.getAddress(customer, addressId, callback);
    }

    @Override
    public Observable<Address> getAddress(final Customer customer, final String addressId) {
        return addressService.getAddress(customer, addressId);
    }

    @Override
    public void updateAddress(final Customer customer, final Address address, final Callback<Address> callback) {
        addressService.updateAddress(customer, address, callback);
    }

    @Override
    public Observable<Address> updateAddress(final Customer customer, final Address address) {
        return addressService.updateAddress(customer, address);
    }

    // ----------- ProductService API ---------------

    @Override
    public void getProductPage(int page, Callback<List<Product>> callback) {
        productService.getProductPage(page, callback);
    }

    @Override
    public Observable<List<Product>> getProductPage(int page) {
        return productService.getProductPage(page);
    }

    @Override
    public void getProductWithHandle(String handle, Callback<Product> callback) {
        productService.getProductWithHandle(handle, callback);
    }

    @Override
    public Observable<Product> getProductWithHandle(String handle) {
        return productService.getProductWithHandle(handle);
    }

    @Override
    public void getProduct(String productId, Callback<Product> callback) {
        productService.getProduct(productId, callback);
    }

    @Override
    public Observable<Product> getProduct(String productId) {
        return productService.getProduct(productId);
    }

    @Override
    public void getProducts(List<String> productIds, Callback<List<Product>> callback) {
        productService.getProducts(productIds, callback);
    }

    @Override
    public Observable<List<Product>> getProducts(List<String> productIds) {
        return productService.getProducts(productIds);
    }

    @Override
    public void getProducts(int page, String collectionId, Callback<List<Product>> callback) {
        productService.getProducts(page, collectionId, callback);
    }

    @Override
    public Observable<List<Product>> getProducts(int page, String collectionId) {
        return productService.getProducts(page, collectionId);
    }

    @Override
    public void getProducts(int page, String collectionId, SortOrder sortOrder, Callback<List<Product>> callback) {
        productService.getProducts(page, collectionId, sortOrder, callback);
    }

    @Override
    public Observable<List<Product>> getProducts(int page, String collectionId, SortOrder sortOrder) {
        return productService.getProducts(page, collectionId, sortOrder);
    }

    @Override
    public void getCollections(Callback<List<Collection>> callback) {
        productService.getCollections(callback);
    }

    @Override
    public Observable<List<Collection>> getCollections() {
        return productService.getCollections();
    }

    @Override
    public void getCollectionPage(int page, Callback<List<Collection>> callback) {
        productService.getCollectionPage(page, callback);
    }

    @Override
    public Observable<List<Collection>> getCollections(int page) {
        return productService.getCollections(page);
    }
}
