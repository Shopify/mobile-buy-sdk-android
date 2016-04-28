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
import android.util.Base64;

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
import com.shopify.buy.model.Product;
import com.shopify.buy.model.ShippingRate;
import com.shopify.buy.model.Shop;
import com.shopify.buy.model.internal.AccountCredentialsWrapper;
import com.shopify.buy.model.internal.AddressWrapper;
import com.shopify.buy.model.internal.AddressesWrapper;
import com.shopify.buy.model.internal.CheckoutWrapper;
import com.shopify.buy.model.internal.CollectionListings;
import com.shopify.buy.model.internal.CustomerTokenWrapper;
import com.shopify.buy.model.internal.CustomerWrapper;
import com.shopify.buy.model.internal.EmailWrapper;
import com.shopify.buy.model.internal.GiftCardWrapper;
import com.shopify.buy.model.internal.MarketingAttribution;
import com.shopify.buy.model.internal.OrderWrapper;
import com.shopify.buy.model.internal.OrdersWrapper;
import com.shopify.buy.model.internal.PaymentSession;
import com.shopify.buy.model.internal.PaymentSessionCheckout;
import com.shopify.buy.model.internal.PaymentSessionCheckoutWrapper;
import com.shopify.buy.model.internal.ProductListings;
import com.shopify.buy.model.internal.ShippingRatesWrapper;
import com.shopify.buy.utils.CollectionUtils;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Scheduler;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * The {@code BuyClient} provides all requests needed to perform request on the Shopify Checkout API. Use this class to perform tasks such as getting a shop, getting collections and products for a shop, creating a {@link Checkout} on Shopify and completing Checkouts.
 * All API methods presented here run asynchronously and return results via callback on the Main thread.
 */
public class BuyClient implements IBuyClient {

    public static final int MAX_PAGE_SIZE = 250;
    public static final int MIN_PAGE_SIZE = 1;
    public static final int DEFAULT_PAGE_SIZE = 25;

    public static final String EMPTY_BODY = "";

    private static final String CUSTOMER_TOKEN_HEADER = "X-Shopify-Customer-Access-Token";

    private final BuyRetrofitService retrofitService;
    private int pageSize = DEFAULT_PAGE_SIZE;

    private final String shopDomain;
    private final String apiKey;
    private final String appId;
    private final String applicationName;
    private String webReturnToUrl;
    private String webReturnToLabel;
    private CustomerToken customerToken;

    private Scheduler callbackScheduler = AndroidSchedulers.mainThread();

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
    public String getWebReturnToUrl() {
        return webReturnToUrl;
    }

    @Override
    public String getWebReturnToLabel() {
        return webReturnToLabel;
    }

    @Override
    public String getShopDomain() {
        return shopDomain;
    }

    @Override
    public int getPageSize() {
        return pageSize;
    }

    BuyClient(final String apiKey, final String appId, final String applicationName, final String shopDomain, final CustomerToken customerToken, Interceptor... interceptors) {
        this.apiKey = apiKey;
        this.appId = appId;
        this.applicationName = applicationName;
        this.shopDomain = shopDomain;
        this.customerToken = customerToken;

        Interceptor requestInterceptor = new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                Request original = chain.request();

                Request.Builder builder = original.newBuilder()
                        .method(original.method(), original.body());

                builder.header("Authorization", "Basic " + Base64.encodeToString(apiKey.getBytes(), Base64.NO_WRAP));

                if (BuyClient.this.customerToken != null && !TextUtils.isEmpty(BuyClient.this.customerToken.getAccessToken())) {
                    builder.header(CUSTOMER_TOKEN_HEADER, BuyClient.this.customerToken.getAccessToken());
                }

                // Using the full package name for BuildConfig here as a work around for Javadoc.  The source paths need to be adjusted
                builder.header("User-Agent", "Mobile Buy SDK Android/" + com.shopify.buy.BuildConfig.VERSION_NAME + "/" + applicationName);

                return chain.proceed(builder.build());
            }
        };

        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .addInterceptor(requestInterceptor);

        for (Interceptor interceptor : interceptors) {
            builder.addInterceptor(interceptor);
        }

        final OkHttpClient httpClient = builder.build();

        Retrofit adapter = new Retrofit.Builder()
                .baseUrl("https://" + shopDomain + "/")
                .addConverterFactory(GsonConverterFactory.create(BuyClientFactory.createDefaultGson()))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.createWithScheduler(Schedulers.io()))
                .client(httpClient)
                .build();

        retrofitService = adapter.create(BuyRetrofitService.class);
    }

    @Override
    public Scheduler getCallbackScheduler() {
        return callbackScheduler;
    }

    @Override
    public void setCallbackScheduler(Scheduler callbackScheduler) {
        this.callbackScheduler = callbackScheduler;
    }

    /**
     * Sets the {@link Customer} specific token
     *
     * @param customerToken
     */
    @Override
    public void setCustomerToken(CustomerToken customerToken) {
        this.customerToken = customerToken;
    }

    /**
     * Returns the {@link Customer} specific token
     *
     * @return customer token
     */
    @Override
    public CustomerToken getCustomerToken() {
        return customerToken;
    }

    /**
     * Sets the web url to be invoked by the button on the completion page of the web checkout.
     *
     * @param webReturnToUrl a url defined as a custom scheme in the Android Manifest file.
     */
    @Override
    public void setWebReturnToUrl(String webReturnToUrl) {
        this.webReturnToUrl = webReturnToUrl;
    }

    /**
     * Sets the text to be displayed on the button on the completion page of the web checkout
     *
     * @param webReturnToLabel the text to display on the button.
     */
    @Override
    public void setWebReturnToLabel(String webReturnToLabel) {
        this.webReturnToLabel = webReturnToLabel;
    }

    /**
     * Sets the page size used for paged API queries
     *
     * @param pageSize the number of {@link Product} to include in a page.  The maximum page size is {@link #MAX_PAGE_SIZE} and the minimum page size is {@link #MIN_PAGE_SIZE}.
     *                 If the page size is less than {@code MIN_PAGE_SIZE}, it will be set to {@code MIN_PAGE_SIZE}.  If the page size is greater than MAX_PAGE_SIZE it will be set to {@code MAX_PAGE_SIZE}.
     *                 The default value is {@link #DEFAULT_PAGE_SIZE}
     */
    @Override
    public void setPageSize(int pageSize) {
        this.pageSize = Math.max(Math.min(pageSize, MAX_PAGE_SIZE), MIN_PAGE_SIZE);
    }

    /*
     * Storefront API
     */

    /**
     * Fetch metadata about your shop
     *
     * @param callback the {@link Callback} that will be used to indicate the response from the asynchronous network operation, not null
     */
    @Override
    public void getShop(final Callback<Shop> callback) {
        getShop().subscribe(new InternalCallbackSubscriber<>(callback));
    }

    /**
     * Fetch metadata about your shop
     *
     * @return cold observable that emits requested shop metadata
     */
    @Override
    public Observable<Shop> getShop() {
        return retrofitService
                .getShop()
                .doOnNext(new RetrofitSuccessHttpStatusCodeHandler<>())
                .map(new Func1<Response<Shop>, Shop>() {
                    @Override
                    public Shop call(Response<Shop> response) {
                        return response.body();
                    }
                })
                .observeOn(getCallbackScheduler());
    }

    /**
     * Fetch a page of products
     *
     * @param page     the 1-based page index. The page size can be set with
     *                 {@link #setPageSize(int)}
     * @param callback the {@link Callback} that will be used to indicate the response from the asynchronous network operation, not null
     */
    @Override
    public void getProductPage(final int page, final Callback<List<Product>> callback) {
        getProductPage(page).subscribe(new InternalCallbackSubscriber<>(callback));
    }

    /**
     * Fetch a page of products
     *
     * @param page the 1-based page index. The page size can be set with
     *             {@link #setPageSize(int)}
     * @return cold observable that emits the requested list of products
     */
    @Override
    public Observable<List<Product>> getProductPage(final int page) {
        if (page < 1) {
            throw new IllegalArgumentException("page is a 1-based index, value cannot be less than 1");
        }

        return retrofitService
                .getProductPage(appId, page, pageSize)
                .doOnNext(new RetrofitSuccessHttpStatusCodeHandler<>())
                .map(new Func1<Response<ProductListings>, List<Product>>() {
                    @Override
                    public List<Product> call(Response<ProductListings> response) {
                        return response.body() != null ? response.body().getProducts() : null;
                    }
                })
                .observeOn(getCallbackScheduler());
    }

    /**
     * Fetch the product with the specified handle
     *
     * @param handle   the handle for the product to fetch
     * @param callback the {@link Callback} that will be used to indicate the response from the asynchronous network operation, not null
     */
    @Override
    public void getProductWithHandle(final String handle, final Callback<Product> callback) {
        getProductWithHandle(handle).subscribe(new InternalCallbackSubscriber<>(callback));
    }

    /**
     * Fetch the product with the specified handle
     *
     * @param handle the handle for the product to fetch
     * @return cold observable that emits requested product with the specified handle
     */
    @Override
    public Observable<Product> getProductWithHandle(final String handle) {
        if (handle == null) {
            throw new NullPointerException("handle cannot be null");
        }

        return retrofitService
                .getProductWithHandle(appId, handle)
                .doOnNext(new RetrofitSuccessHttpStatusCodeHandler<>())
                .map(new Func1<Response<ProductListings>, Product>() {
                    @Override
                    public Product call(Response<ProductListings> response) {
                        final List<Product> products = response.body() != null ? response.body().getProducts() : null;
                        return !CollectionUtils.isEmpty(products) ? products.get(0) : null;
                    }
                })
                .observeOn(getCallbackScheduler());
    }

    /**
     * Fetch a single Product
     *
     * @param productId the productId for the product to fetch
     * @param callback  the {@link Callback} that will be used to indicate the response from the asynchronous network operation, not null
     */
    @Override
    public void getProduct(final String productId, final Callback<Product> callback) {
        getProduct(productId).subscribe(new InternalCallbackSubscriber<>(callback));
    }

    /**
     * Fetch a single Product
     *
     * @param productId the productId for the product to fetch
     * @return cold observable that emits requested single product
     */
    @Override
    public Observable<Product> getProduct(final String productId) {
        if (productId == null) {
            throw new NullPointerException("productId cannot be null");
        }

        return retrofitService
                .getProducts(appId, productId)
                .doOnNext(new RetrofitSuccessHttpStatusCodeHandler<>())
                .doOnNext(new RetrofitSuccessHttpStatusCodeHandler<>())
                .map(new Func1<Response<ProductListings>, Product>() {
                    @Override
                    public Product call(Response<ProductListings> response) {
                        final List<Product> products = response.body() != null ? response.body().getProducts() : null;
                        return !CollectionUtils.isEmpty(products) ? products.get(0) : null;
                    }
                })
                .observeOn(getCallbackScheduler());
    }

    /**
     * Fetch a list of Products
     *
     * @param productIds a List of the productIds to fetch
     * @param callback   the {@link Callback} that will be used to indicate the response from the asynchronous network operation, not null
     */
    @Override
    public void getProducts(final List<String> productIds, final Callback<List<Product>> callback) {
        getProducts(productIds).subscribe(new InternalCallbackSubscriber<>(callback));
    }

    /**
     * Fetch a list of Products
     *
     * @param productIds a List of the productIds to fetch
     * @return cold observable that emits requested list of products
     */
    @Override
    public Observable<List<Product>> getProducts(final List<String> productIds) {
        if (productIds == null) {
            throw new NullPointerException("productIds List cannot be null");
        }
        if (productIds.size() < 1) {
            throw new IllegalArgumentException("productIds List cannot be empty");
        }

        // All product responses from the server are wrapped in a ProductListings object
        // The same endpoint is used for single and multiple product queries.
        // For this call we will query with multiple ids.
        // The returned product array will contain products for each id found.
        // If no ids were found, the array will be empty
        final String queryString = TextUtils.join(",", productIds.toArray());
        return retrofitService
                .getProducts(appId, queryString)
                .doOnNext(new RetrofitSuccessHttpStatusCodeHandler<>())
                .map(new Func1<Response<ProductListings>, List<Product>>() {
                    @Override
                    public List<Product> call(Response<ProductListings> response) {
                        return response.body() != null ? response.body().getProducts() : null;
                    }
                })
                .observeOn(getCallbackScheduler());
    }

    /**
     * Fetch the list of Products in a Collection using the sort order defined in the shop admin
     *
     * @param page         the 1-based page index. The page size can be set with {@link #setPageSize(int)}
     * @param collectionId the collectionId that we want to fetch products for
     * @param callback     the {@link Callback} that will be used to indicate the response from the asynchronous network operation, not null
     */
    @Override
    public void getProducts(int page, String collectionId, final Callback<List<Product>> callback) {
        getProducts(page, collectionId, SortOrder.COLLECTION_DEFAULT, callback);
    }

    /**
     * Fetch the list of Products in a Collection using the sort order defined in the shop admin
     *
     * @param page         the 1-based page index. The page size can be set with {@link #setPageSize(int)}
     * @param collectionId the collectionId that we want to fetch products for
     * @return cold observable that emits requested list of products
     */
    @Override
    public Observable<List<Product>> getProducts(final int page, final String collectionId) {
        return getProducts(page, collectionId, SortOrder.COLLECTION_DEFAULT);
    }

    /**
     * Fetch the list of Products in a Collection
     *
     * @param page         the 1-based page index. The page size can be set with {@link #setPageSize(int)}
     * @param collectionId the collectionId that we want to fetch products for
     * @param sortOrder    the sort order for the collection.
     * @param callback     the {@link Callback} that will be used to indicate the response from the asynchronous network operation, not null
     */
    @Override
    public void getProducts(final int page, final String collectionId, final SortOrder sortOrder, final Callback<List<Product>> callback) {
        getProducts(page, collectionId, sortOrder).subscribe(new InternalCallbackSubscriber<>(callback));
    }

    /**
     * Fetch the list of Products in a Collection
     *
     * @param page         the 1-based page index. The page size can be set with {@link #setPageSize(int)}
     * @param collectionId the collectionId that we want to fetch products for
     * @param sortOrder    the sort order for the collection.
     * @return cold observable that emits requested list of products
     */
    @Override
    public Observable<List<Product>> getProducts(final int page, final String collectionId, final SortOrder sortOrder) {
        if (page < 1) {
            throw new IllegalArgumentException("page is a 1-based index, value cannot be less than 1");
        }
        if (collectionId == null) {
            throw new IllegalArgumentException("collectionId cannot be null");
        }

        return retrofitService
                .getProducts(appId, collectionId, pageSize, page, sortOrder.toString())
                .doOnNext(new RetrofitSuccessHttpStatusCodeHandler<>())
                .map(new Func1<Response<ProductListings>, List<Product>>() {
                    @Override
                    public List<Product> call(Response<ProductListings> response) {
                        return response.body() != null ? response.body().getProducts() : null;
                    }
                })
                .observeOn(getCallbackScheduler());
    }

    /**
     * Fetch a list of Collections
     *
     * @param callback the {@link Callback} that will be used to indicate the response from the asynchronous network operation, not null
     */
    @Override
    public void getCollections(final Callback<List<Collection>> callback) {
        getCollectionPage(1, callback);
    }

    /**
     * Fetch a list of Collections
     *
     * @return cold observable that emits requested list of collections
     */
    @Override
    public Observable<List<Collection>> getCollections() {
        return getCollections(1);
    }

    /**
     * Fetch a page of collections
     *
     * @param page     the 1-based page index. The page size can be set with
     *                 {@link #setPageSize(int)}
     * @param callback the {@link Callback} that will be used to indicate the response from the asynchronous network operation, not null
     */
    @Override
    public void getCollectionPage(final int page, final Callback<List<Collection>> callback) {
        getCollections(page).subscribe(new InternalCallbackSubscriber<>(callback));
    }

    /**
     * Fetch a page of collections
     *
     * @param page the 1-based page index. The page size can be set with
     *             {@link #setPageSize(int)}
     * @return cold observable that emits requested list of collections
     */
    @Override
    public Observable<List<Collection>> getCollections(final int page) {
        if (page < 1) {
            throw new IllegalArgumentException("page is a 1-based index, value cannot be less than 1");
        }

        // All collection responses from the server are wrapped in a CollectionListings object which contains and array of collections
        // For this call, we will clamp the size of the collection array returned to the page size
        return retrofitService
                .getCollectionPage(appId, page, pageSize)
                .doOnNext(new RetrofitSuccessHttpStatusCodeHandler<>())
                .map(new Func1<Response<CollectionListings>, List<Collection>>() {
                    @Override
                    public List<Collection> call(Response<CollectionListings> response) {
                        return response.body() != null ? response.body().getCollections() : null;
                    }
                })
                .observeOn(getCallbackScheduler());
    }

    /*
     * Buy API
     */

    /**
     * Initiate the Shopify checkout process with a new Checkout object.
     *
     * @param checkout the {@link Checkout} object to use for initiating the checkout process
     * @param callback the {@link Callback} that will be used to indicate the response from the asynchronous network operation, not null
     */
    @Override
    public void createCheckout(final Checkout checkout, final Callback<Checkout> callback) {
        createCheckout(checkout).subscribe(new InternalCallbackSubscriber<>(callback));
    }

    /**
     * Initiate the Shopify checkout process with a new Checkout object.
     *
     * @param checkout the {@link Checkout} object to use for initiating the checkout process
     * @return cold observable that emits created checkout object
     */
    @Override
    public Observable<Checkout> createCheckout(final Checkout checkout) {
        if (checkout == null) {
            throw new NullPointerException("checkout cannot be null");
        }

        checkout.setMarketingAttribution(new MarketingAttribution(applicationName));
        checkout.setSourceName("mobile_app");
        if (webReturnToUrl != null) {
            checkout.setWebReturnToUrl(webReturnToUrl);
        }
        if (webReturnToLabel != null) {
            checkout.setWebReturnToLabel(webReturnToLabel);
        }

        return retrofitService
                .createCheckout(new CheckoutWrapper(checkout))
                .doOnNext(new RetrofitSuccessHttpStatusCodeHandler<>())
                .map(new Func1<Response<CheckoutWrapper>, Checkout>() {
                    @Override
                    public Checkout call(Response<CheckoutWrapper> response) {
                        return response.body() != null ? response.body().getCheckout() : null;
                    }
                })
                .observeOn(getCallbackScheduler());
    }

    /**
     * Update an existing Checkout's attributes
     *
     * @param checkout the {@link Checkout} to update
     * @param callback the {@link Callback} that will be used to indicate the response from the asynchronous network operation, not null
     */
    @Override
    public void updateCheckout(final Checkout checkout, final Callback<Checkout> callback) {
        updateCheckout(checkout).subscribe(new InternalCallbackSubscriber<>(callback));
    }

    /**
     * Update an existing Checkout's attributes
     *
     * @param checkout the {@link Checkout} to update
     * @return cold observable that emits updated checkout object
     */
    @Override
    public Observable<Checkout> updateCheckout(final Checkout checkout) {
        if (checkout == null) {
            throw new NullPointerException("checkout cannot be null");
        }

        final Checkout cleanCheckout = checkout.copyForUpdate();
        return retrofitService
                .updateCheckout(new CheckoutWrapper(cleanCheckout), cleanCheckout.getToken())
                .doOnNext(new RetrofitSuccessHttpStatusCodeHandler<>())
                .map(new Func1<Response<CheckoutWrapper>, Checkout>() {
                    @Override
                    public Checkout call(Response<CheckoutWrapper> response) {
                        return response.body() != null ? response.body().getCheckout() : null;
                    }
                })
                .observeOn(getCallbackScheduler());
    }

    /**
     * Fetch shipping rates for a given Checkout
     *
     * @param checkoutToken the {@link Checkout#token} from an existing Checkout
     * @param callback      the {@link Callback} that will be used to indicate the response from the asynchronous network operation, not null
     */
    @Override
    public void getShippingRates(final String checkoutToken, final Callback<List<ShippingRate>> callback) {
        getShippingRates(checkoutToken).subscribe(new InternalCallbackSubscriber<>(callback));
    }

    /**
     * Fetch shipping rates for a given Checkout
     *
     * @param checkoutToken the {@link Checkout#token} from an existing Checkout
     * @return cold observable that emits requested list of shipping rates for a given checkout
     */
    @Override
    public Observable<List<ShippingRate>> getShippingRates(final String checkoutToken) {
        if (checkoutToken == null) {
            throw new NullPointerException("checkoutToken cannot be null");
        }

        return retrofitService
                .getShippingRates(checkoutToken)
                .doOnNext(new RetrofitSuccessHttpStatusCodeHandler<>())
                .map(new Func1<Response<ShippingRatesWrapper>, List<ShippingRate>>() {
                    @Override
                    public List<ShippingRate> call(Response<ShippingRatesWrapper> response) {
                        return response.body() != null ? response.body().getShippingRates() : null;
                    }
                })
                .observeOn(getCallbackScheduler());
    }

    /**
     * Post a credit card to Shopify's card server and associate it with a Checkout
     *
     * @param card     the {@link CreditCard} to associate
     * @param checkout the {@link Checkout} to associate the card with
     * @param callback the {@link Callback} that will be used to indicate the response from the asynchronous network operation, not null
     */
    @Override
    public void storeCreditCard(final CreditCard card, final Checkout checkout, final Callback<Checkout> callback) {
        storeCreditCard(card, checkout).subscribe(new InternalCallbackSubscriber<>(callback));
    }

    /**
     * Post a credit card to Shopify's card server and associate it with a Checkout
     *
     * @param card     the {@link CreditCard} to associate
     * @param checkout the {@link Checkout} to associate the card with
     * @return cold observable that emits updated checkout with posted credit card
     */
    @Override
    public Observable<Checkout> storeCreditCard(final CreditCard card, final Checkout checkout) {
        if (card == null) {
            throw new NullPointerException("card cannot be null");
        }

        if (checkout == null) {
            throw new NullPointerException("checkout cannot be null");
        }

        final PaymentSessionCheckoutWrapper dataWrapper = new PaymentSessionCheckoutWrapper();

        final PaymentSessionCheckout data = new PaymentSessionCheckout();
        data.setToken(checkout.getToken());
        data.setCreditCard(card);
        data.setBillingAddress(checkout.getBillingAddress());
        dataWrapper.setCheckout(data);

        return retrofitService
                .storeCreditCard(checkout.getPaymentUrl(), dataWrapper, "Basic " + Base64.encodeToString(apiKey.getBytes(), Base64.NO_WRAP))
                .doOnNext(new RetrofitSuccessHttpStatusCodeHandler<>())
                .map(new Func1<Response<PaymentSession>, String>() {
                    @Override
                    public String call(Response<PaymentSession> response) {
                        return response.body() != null ? response.body().getId() : null;
                    }
                })
                .doOnNext(new Action1<String>() {
                    @Override
                    public void call(String paymentSessionId) {
                        checkout.setPaymentSessionId(paymentSessionId);
                    }
                })
                .map(new Func1<String, Checkout>() {
                    @Override
                    public Checkout call(String s) {
                        return checkout;
                    }
                })
                .observeOn(getCallbackScheduler());
    }

    /**
     * Complete the checkout and process the payment session
     *
     * @param checkout a {@link Checkout} that has had a {@link CreditCard} associated with it using {@link #storeCreditCard(CreditCard, Checkout)}
     * @param callback the {@link Callback} that will be used to indicate the response from the asynchronous network operation, not null
     */
    @Override
    public void completeCheckout(final Checkout checkout, final Callback<Checkout> callback) {
        completeCheckout(checkout).subscribe(new InternalCallbackSubscriber<>(callback));
    }


    /**
     * Complete the checkout and process the payment session
     *
     * @param checkout a {@link Checkout} that has had a {@link CreditCard} associated with it using {@link #storeCreditCard(CreditCard, Checkout)}
     * @return cold observable that emits completed checkout
     */
    @Override
    public Observable<Checkout> completeCheckout(final Checkout checkout) {
        if (checkout == null) {
            throw new NullPointerException("checkout cannot be null");
        }

        final HashMap<String, String> requestBodyMap = new HashMap<>();
        final String paymentSessionId = checkout.getPaymentSessionId();
        if (!TextUtils.isEmpty(paymentSessionId)) {
            requestBodyMap.put("payment_session_id", checkout.getPaymentSessionId());
        }

        return retrofitService
                .completeCheckout(requestBodyMap, checkout.getToken())
                .doOnNext(new RetrofitSuccessHttpStatusCodeHandler<>())
                .map(new Func1<Response<CheckoutWrapper>, Checkout>() {
                    @Override
                    public Checkout call(Response<CheckoutWrapper> response) {
                        return response.body() != null ? response.body().getCheckout() : null;
                    }
                })
                .observeOn(getCallbackScheduler());
    }

    /**
     * Get the status of the payment session associated with {@code checkout}. {@code callback} will be
     * called with a boolean value indicating whether the session has completed or not. This method
     * should be polled until the {@code callback} response is {@code true}
     *
     * @param checkout a {@link Checkout} that has been passed as a parameter to {@link #completeCheckout(Checkout, Callback)}
     * @param callback the {@link Callback} that will be used to indicate the response from the asynchronous network operation, not null
     */
    @Override
    public void getCheckoutCompletionStatus(final Checkout checkout, final Callback<Boolean> callback) {
        getCheckoutCompletionStatus(checkout).subscribe(new InternalCallbackSubscriber<>(callback));
    }

    /**
     * Get the status of the payment session associated with {@code checkout}. {@code callback} will be
     * called with a boolean value indicating whether the session has completed or not. This method
     * should be polled until the {@code callback} response is {@code true}
     *
     * @param checkout a {@link Checkout} that has been passed as a parameter to {@link #completeCheckout(Checkout, Callback)}
     * @return cold observable that emits the requested status for specified checkout
     */
    @Override
    public Observable<Boolean> getCheckoutCompletionStatus(final Checkout checkout) {
        if (checkout == null) {
            throw new NullPointerException("checkout cannot be null");
        }

        return retrofitService
                .getCheckoutCompletionStatus(checkout.getToken())
                .map(new Func1<Response<Void>, Boolean>() {
                    @Override
                    public Boolean call(Response<Void> response) {
                        return HttpURLConnection.HTTP_OK == response.code();
                    }
                })
                .observeOn(getCallbackScheduler());
    }

    /**
     * Fetch an existing Checkout from Shopify
     *
     * @param checkoutToken the token associated with the existing {@link Checkout}
     * @param callback      the {@link Callback} that will be used to indicate the response from the asynchronous network operation, not null
     */
    @Override
    public void getCheckout(final String checkoutToken, final Callback<Checkout> callback) {
        getCheckout(checkoutToken).subscribe(new InternalCallbackSubscriber<>(callback));
    }

    /**
     * Fetch an existing Checkout from Shopify
     *
     * @param checkoutToken the token associated with the existing {@link Checkout}
     * @return cold observable that emits requested existing checkout
     */
    @Override
    public Observable<Checkout> getCheckout(final String checkoutToken) {
        if (checkoutToken == null) {
            throw new NullPointerException("checkoutToken cannot be null");
        }

        return retrofitService
                .getCheckout(checkoutToken)
                .doOnNext(new RetrofitSuccessHttpStatusCodeHandler<>())
                .map(new Func1<Response<CheckoutWrapper>, Checkout>() {
                    @Override
                    public Checkout call(Response<CheckoutWrapper> response) {
                        return response.body() != null ? response.body().getCheckout() : null;
                    }
                })
                .observeOn(getCallbackScheduler());
    }

    /**
     * Apply a gift card to a Checkout
     *
     * @param giftCardCode the gift card code for a gift card associated with the current Shop
     * @param checkout     the {@link Checkout} object to apply the gift card to
     * @param callback     the {@link Callback} that will be used to indicate the response from the asynchronous network operation, not null
     */
    @Override
    public void applyGiftCard(final String giftCardCode, final Checkout checkout, final Callback<Checkout> callback) {
        applyGiftCard(giftCardCode, checkout).subscribe(new InternalCallbackSubscriber<>(callback));
    }

    /**
     * Apply a gift card to a Checkout
     *
     * @param giftCardCode the gift card code for a gift card associated with the current Shop
     * @param checkout     the {@link Checkout} object to apply the gift card to
     * @return cold observable that emits updated checkout
     */
    @Override
    public Observable<Checkout> applyGiftCard(final String giftCardCode, final Checkout checkout) {
        if (checkout == null) {
            throw new NullPointerException("checkout cannot be null");
        }

        final Checkout cleanCheckout = checkout.copy();
        final GiftCard giftCard = new GiftCard(giftCardCode);
        return retrofitService
                .applyGiftCard(new GiftCardWrapper(giftCard), checkout.getToken())
                .doOnNext(new RetrofitSuccessHttpStatusCodeHandler<>())
                .map(new Func1<Response<GiftCardWrapper>, Checkout>() {
                    @Override
                    public Checkout call(Response<GiftCardWrapper> response) {
                        if (response.body() != null) {
                            final GiftCard updatedGiftCard = response.body().getGiftCard();
                            cleanCheckout.addGiftCard(updatedGiftCard);
                            cleanCheckout.setPaymentDue(updatedGiftCard.getCheckout().getPaymentDue());
                        }
                        return cleanCheckout;
                    }
                })
                .observeOn(getCallbackScheduler());
    }

    /**
     * Remove a gift card that was previously applied to a Checkout
     *
     * @param giftCard the {@link GiftCard} to remove from the {@link Checkout}
     * @param checkout the {@code Checkout} to remove the {@code GiftCard} from
     * @param callback the {@link Callback} that will be used to indicate the response from the asynchronous network operation, not null
     */
    @Override
    public void removeGiftCard(final GiftCard giftCard, final Checkout checkout, final Callback<Checkout> callback) {
        removeGiftCard(giftCard, checkout).subscribe(new InternalCallbackSubscriber<>(callback));
    }

    /**
     * Remove a gift card that was previously applied to a Checkout
     *
     * @param giftCard the {@link GiftCard} to remove from the {@link Checkout}
     * @param checkout the {@code Checkout} to remove the {@code GiftCard} from
     * @return cold observable that emits updated checkout
     */
    @Override
    public Observable<Checkout> removeGiftCard(final GiftCard giftCard, final Checkout checkout) {
        if (checkout == null) {
            throw new NullPointerException("checkout cannot be null");
        }

        if (giftCard == null) {
            throw new NullPointerException("giftCard cannot be null");
        }

        return retrofitService
                .removeGiftCard(giftCard.getId(), checkout.getToken())
                .doOnNext(new RetrofitSuccessHttpStatusCodeHandler<>())
                .map(new Func1<Response<GiftCardWrapper>, Checkout>() {
                    @Override
                    public Checkout call(Response<GiftCardWrapper> response) {
                        if (response.body() != null) {
                            GiftCard updatedGiftCard = response.body().getGiftCard();
                            checkout.removeGiftCard(updatedGiftCard);
                            checkout.setPaymentDue(updatedGiftCard.getCheckout().getPaymentDue());
                        }
                        return checkout;
                    }
                })
                .observeOn(getCallbackScheduler());
    }

    /**
     * Create a new Customer on Shopify
     *
     * @param accountCredentials the account credentials with an email, password, first name, and last name of the {@link Customer} to be created, not null
     * @param callback           the {@link Callback} that will be used to indicate the response from the asynchronous network operation, not null
     */
    @Override
    public void createCustomer(final AccountCredentials accountCredentials, final Callback<Customer> callback) {
        createCustomer(accountCredentials).subscribe(new InternalCallbackSubscriber<>(callback));
    }

    /**
     * Create a new Customer on Shopify
     *
     * @param accountCredentials the account credentials with an email, password, first name, and last name of the {@link Customer} to be created, not null
     * @return cold observable that emits created new customer
     */
    @Override
    public Observable<Customer> createCustomer(final AccountCredentials accountCredentials) {
        if (accountCredentials == null) {
            throw new NullPointerException("accountCredentials cannot be null");
        }

        final AccountCredentialsWrapper accountCredentialsWrapper = new AccountCredentialsWrapper(accountCredentials);
        return retrofitService
                .createCustomer(accountCredentialsWrapper)
                .doOnNext(new RetrofitSuccessHttpStatusCodeHandler<>())
                .map(new Func1<Response<CustomerWrapper>, Customer>() {
                    @Override
                    public Customer call(Response<CustomerWrapper> response) {
                        return response.body() != null ? response.body().getCustomer() : null;
                    }
                })
                .observeOn(getCallbackScheduler());
    }

    /**
     * Activate the customer account.
     *
     * @param customerId         the id of the {@link Customer} to activate
     * @param activationToken    the activation token for the Customer, not null or empty
     * @param accountCredentials the account credentials with a password of the {@link Customer} to be activated, not null
     * @param callback           the {@link Callback} that will be used to indicate the response from the asynchronous network operation, not null
     */
    @Deprecated
    @Override
    public void activateCustomer(final Long customerId, final String activationToken, final AccountCredentials accountCredentials, final Callback<Customer> callback) {
        activateCustomer(customerId, activationToken, accountCredentials).subscribe(new InternalCallbackSubscriber<>(callback));
    }

    /**
     * Activate the customer account.
     *
     * @param customerId         the id of the {@link Customer} to activate
     * @param activationToken    the activation token for the Customer, not null or empty
     * @param accountCredentials the account credentials with a password of the {@link Customer} to be activated, not null
     * @return cold observable that emits activated customer account
     */
    @Override
    public Observable<Customer> activateCustomer(final Long customerId, final String activationToken, final AccountCredentials accountCredentials) {
        if (TextUtils.isEmpty(activationToken)) {
            throw new IllegalArgumentException("activation token cannot be empty");
        }

        if (accountCredentials == null) {
            throw new NullPointerException("accountCredentials cannot be null");
        }

        final AccountCredentialsWrapper accountCredentialsWrapper = new AccountCredentialsWrapper(accountCredentials);
        return retrofitService
                .activateCustomer(customerId, activationToken, accountCredentialsWrapper)
                .doOnNext(new RetrofitSuccessHttpStatusCodeHandler<>())
                .map(new Func1<Response<CustomerWrapper>, Customer>() {
                    @Override
                    public Customer call(Response<CustomerWrapper> response) {
                        return response.body() != null ? response.body().getCustomer() : null;
                    }
                })
                .observeOn(getCallbackScheduler());
    }

    /**
     * Reset the password for the customer account.
     *
     * @param customerId         the id of the {@link Customer} to activate
     * @param resetToken         the reset token for the Customer, not null or empty
     * @param accountCredentials the account credentials with the new password of the {@link Customer}. not null
     * @param callback           the {@link Callback} that will be used to indicate the response from the asynchronous network operation, not null
     */
    @Override
    public void resetPassword(final Long customerId, final String resetToken, final AccountCredentials accountCredentials, final Callback<Customer> callback) {
        resetPassword(customerId, resetToken, accountCredentials).subscribe(new InternalCallbackSubscriber<>(callback));
    }

    /**
     * Reset the password for the customer account.
     *
     * @param customerId         the id of the {@link Customer} to activate
     * @param resetToken         the reset token for the Customer, not null or empty
     * @param accountCredentials the account credentials with the new password of the {@link Customer}. not null
     * @return cold observable that emits customer account with reset password
     */
    @Override
    public Observable<Customer> resetPassword(final Long customerId, final String resetToken, final AccountCredentials accountCredentials) {
        if (TextUtils.isEmpty(resetToken)) {
            throw new IllegalArgumentException("reset token cannot be empty");
        }

        if (accountCredentials == null) {
            throw new NullPointerException("accountCredentials cannot be null");
        }

        final AccountCredentialsWrapper accountCredentialsWrapper = new AccountCredentialsWrapper(accountCredentials);
        return retrofitService
                .resetPassword(customerId, resetToken, accountCredentialsWrapper)
                .doOnNext(new RetrofitSuccessHttpStatusCodeHandler<>())
                .map(new Func1<Response<CustomerWrapper>, Customer>() {
                    @Override
                    public Customer call(Response<CustomerWrapper> response) {
                        return response.body() != null ? response.body().getCustomer() : null;
                    }
                })
                .observeOn(getCallbackScheduler());
    }

    /**
     * Log an existing Customer into Shopify
     *
     * @param accountCredentials the account credentials with an email and password of the {@link Customer}, not null
     * @param callback           the {@link Callback} that will be used to indicate the response from the asynchronous network operation, not null
     */
    @Override
    public void loginCustomer(final AccountCredentials accountCredentials, final Callback<CustomerToken> callback) {
        loginCustomer(accountCredentials).subscribe(new InternalCallbackSubscriber<>(callback));
    }

    /**
     * Log an existing Customer into Shopify
     *
     * @param accountCredentials the account credentials with an email and password of the {@link Customer}, not null
     * @return cold observable that emits logged in customer token
     */
    @Override
    public Observable<CustomerToken> loginCustomer(final AccountCredentials accountCredentials) {
        if (accountCredentials == null) {
            throw new NullPointerException("accountCredentials cannot be null");
        }

        final AccountCredentialsWrapper accountCredentialsWrapper = new AccountCredentialsWrapper(accountCredentials);
        return retrofitService
                .getCustomerToken(accountCredentialsWrapper)
                .doOnNext(new RetrofitSuccessHttpStatusCodeHandler<>())
                .map(new Func1<Response<CustomerTokenWrapper>, CustomerToken>() {
                    @Override
                    public CustomerToken call(Response<CustomerTokenWrapper> response) {
                        return response.body() != null ? response.body().getCustomerToken() : null;
                    }
                })
                .doOnNext(new Action1<CustomerToken>() {
                    @Override
                    public void call(CustomerToken token) {
                        customerToken = token;
                    }
                })
                .observeOn(getCallbackScheduler());
    }

    /**
     * Log a Customer out from Shopify
     *
     * @param callback the {@link Callback} that will be used to indicate the response from the asynchronous network operation, not null
     */
    @Override
    public void logoutCustomer(final Callback<Void> callback) {
        logoutCustomer().subscribe(new InternalCallbackSubscriber<>(callback));
    }

    /**
     * Log a Customer out from Shopify
     *
     * @return cold observable that log customer out and emits nothing
     */
    @Override
    public Observable<Void> logoutCustomer() {
        if (customerToken == null) {
            return Observable.just(null);
        }

        return retrofitService
                .removeCustomerToken(customerToken.getCustomerId())
                .doOnNext(new RetrofitSuccessHttpStatusCodeHandler<>())
                .map(new Func1<Response<Void>, Void>() {
                    @Override
                    public Void call(Response<Void> response) {
                        return response.body();
                    }
                })
                .observeOn(getCallbackScheduler())
                .doOnNext(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        customerToken = null;
                    }
                });
    }

    /**
     * Update an existing Customer's attributes.
     *
     * @param customer the {@link Customer} to update
     * @param callback the {@link Callback} that will be used to indicate the response from the asynchronous network operation, not null
     */
    @Override
    public void updateCustomer(final Customer customer, final Callback<Customer> callback) {
        updateCustomer(customer).subscribe(new InternalCallbackSubscriber<>(callback));
    }

    /**
     * Update an existing Customer's attributes.
     *
     * @param customer the {@link Customer} to update
     * @return cold observable that emits updated existent customer attributes
     */
    @Override
    public Observable<Customer> updateCustomer(final Customer customer) {
        if (customer == null) {
            throw new NullPointerException("customer cannot be null");
        }

        return retrofitService
                .updateCustomer(customer.getId(), new CustomerWrapper(customer))
                .doOnNext(new RetrofitSuccessHttpStatusCodeHandler<>())
                .map(new Func1<Response<CustomerWrapper>, Customer>() {
                    @Override
                    public Customer call(Response<CustomerWrapper> response) {
                        return response.body() != null ? response.body().getCustomer() : null;
                    }
                })
                .observeOn(getCallbackScheduler());
    }

    /**
     * Retrieve a Customer's details from Shopify.
     *
     * @param customerId the identifier of a {@link CustomerToken} or {@link Customer}
     * @param callback   the {@link Callback} that will be used to indicate the response from the asynchronous network operation, not null
     */
    @Override
    public void getCustomer(final Long customerId, final Callback<Customer> callback) {
        getCustomer(customerId).subscribe(new InternalCallbackSubscriber<>(callback));
    }

    /**
     * Retrieve a Customer's details from Shopify.
     *
     * @param customerId the identifier of a {@link CustomerToken} or {@link Customer}
     * @return cold observable tha emits requested customer details
     */
    @Override
    public Observable<Customer> getCustomer(final Long customerId) {
        if (customerId == null) {
            throw new NullPointerException("customer Id cannot be null");
        }

        return retrofitService
                .getCustomer(customerId)
                .doOnNext(new RetrofitSuccessHttpStatusCodeHandler<>())
                .map(new Func1<Response<CustomerWrapper>, Customer>() {
                    @Override
                    public Customer call(Response<CustomerWrapper> response) {
                        return response.body() != null ? response.body().getCustomer() : null;
                    }
                })
                .observeOn(getCallbackScheduler());
    }

    /**
     * Renew a Customer login.  This should be called periodically to keep the token up to date.
     *
     * @param callback the {@link Callback} that will be used to indicate the response from the asynchronous network operation, not null
     */
    @Override
    public void renewCustomer(final Callback<CustomerToken> callback) {
        renewCustomer().subscribe(new InternalCallbackSubscriber<>(callback));
    }

    /**
     * Renew a Customer login.  This should be called periodically to keep the token up to date.
     *
     * @return cold observable that emits renewed customer token
     */
    @Override
    public Observable<CustomerToken> renewCustomer() {
        if (customerToken == null) {
            return Observable.just(null);
        }

        return retrofitService
                .renewCustomerToken(EMPTY_BODY, customerToken.getCustomerId())
                .doOnNext(new RetrofitSuccessHttpStatusCodeHandler<>())
                .map(new Func1<Response<CustomerTokenWrapper>, CustomerToken>() {
                    @Override
                    public CustomerToken call(Response<CustomerTokenWrapper> response) {
                        return response.body() != null ? response.body().getCustomerToken() : null;
                    }
                })
                .observeOn(getCallbackScheduler())
                .doOnNext(new Action1<CustomerToken>() {
                    @Override
                    public void call(CustomerToken token) {
                        customerToken = token;
                    }
                });
    }

    /**
     * Send a password recovery email. An email will be sent to the email address specified if a customer with that email address exists on Shopify.
     *
     * @param email    the email address to send the password recovery email to
     * @param callback the {@link Callback} that will be used to indicate the response from the asynchronous network operation, not null
     */
    @Override
    public void recoverPassword(final String email, final Callback<Void> callback) {
        recoverPassword(email).subscribe(new InternalCallbackSubscriber<>(callback));
    }

    /**
     * Send a password recovery email. An email will be sent to the email address specified if a customer with that email address exists on Shopify.
     *
     * @param email the email address to send the password recovery email to
     * @return cold observable that sends a password recovery email and emits nothing
     */
    @Override
    public Observable<Void> recoverPassword(final String email) {
        if (TextUtils.isEmpty(email)) {
            throw new IllegalArgumentException("email cannot be empty");
        }

        return retrofitService
                .recoverCustomer(new EmailWrapper(email))
                .doOnNext(new RetrofitSuccessHttpStatusCodeHandler<>())
                .map(new Func1<Response<Void>, Void>() {
                    @Override
                    public Void call(Response<Void> response) {
                        return response.body();
                    }
                })
                .observeOn(getCallbackScheduler());
    }

    /**
     * Fetch the Orders associated with a Customer.
     *
     * @param customer the {@link Customer} to fetch the orders for, not null
     * @param callback the {@link Callback} that will be used to indicate the response from the asynchronous network operation, not null
     */
    @Override
    public void getOrders(final Customer customer, final Callback<List<Order>> callback) {
        getOrders(customer).subscribe(new InternalCallbackSubscriber<>(callback));
    }

    /**
     * Fetch the Orders associated with a Customer.
     *
     * @param customer the {@link Customer} to fetch the orders for, not null
     * @return cold observable that emits requested list of orders associated with a customer
     */
    @Override
    public Observable<List<Order>> getOrders(final Customer customer) {
        if (customer == null) {
            throw new NullPointerException("customer cannot be null");
        }

        return retrofitService
                .getOrders(customer.getId())
                .doOnNext(new RetrofitSuccessHttpStatusCodeHandler<>())
                .map(new Func1<Response<OrdersWrapper>, List<Order>>() {
                    @Override
                    public List<Order> call(Response<OrdersWrapper> response) {
                        return response.body() != null ? response.body().getOrders() : null;
                    }
                })
                .observeOn(getCallbackScheduler());
    }

    /**
     * Fetch an existing Order from Shopify
     *
     * @param customer the {@link Customer} to fetch the order for
     * @param orderId  the identifier of the {@link Order} to retrieve
     * @param callback the {@link Callback} that will be used to indicate the response from the asynchronous network operation, not null
     */
    @Override
    public void getOrder(final Customer customer, final String orderId, final Callback<Order> callback) {
        getOrder(customer, orderId).subscribe(new InternalCallbackSubscriber<>(callback));
    }

    /**
     * Fetch an existing Order from Shopify
     *
     * @param customer the {@link Customer} to fetch the order for
     * @param orderId  the identifier of the {@link Order} to retrieve
     * @return cold observable that emits requested existing order
     */
    @Override
    public Observable<Order> getOrder(final Customer customer, final String orderId) {
        if (TextUtils.isEmpty(orderId)) {
            throw new IllegalArgumentException("orderId cannot be empty");
        }

        if (customer == null) {
            throw new NullPointerException("customer cannot be null");
        }

        return retrofitService
                .getOrder(customer.getId(), orderId)
                .doOnNext(new RetrofitSuccessHttpStatusCodeHandler<>())
                .map(new Func1<Response<OrderWrapper>, Order>() {
                    @Override
                    public Order call(Response<OrderWrapper> response) {
                        return response.body() != null ? response.body().getOrder() : null;
                    }
                })
                .observeOn(getCallbackScheduler());
    }

    /**
     * Create an Address and associate it with a Customer
     *
     * @param customer the {@link Customer} to create and address for, not null
     * @param address  the {@link Address} to create, not null
     * @param callback the {@link Callback} that will be used to indicate the response from the asynchronous network operation, not null
     */
    @Override
    public void createAddress(final Customer customer, final Address address, final Callback<Address> callback) {
        createAddress(customer, address).subscribe(new InternalCallbackSubscriber<>(callback));
    }

    /**
     * Create an Address and associate it with a Customer
     *
     * @param customer the {@link Customer} to create and address for, not null
     * @param address  the {@link Address} to create, not null
     * @return cold observable that emits created address associated with a customer
     */
    @Override
    public Observable<Address> createAddress(final Customer customer, final Address address) {
        if (address == null) {
            throw new NullPointerException("address cannot be null");
        }

        if (customer == null) {
            throw new NullPointerException("customer cannot be null");
        }

        return retrofitService
                .createAddress(customer.getId(), new AddressWrapper(address))
                .doOnNext(new RetrofitSuccessHttpStatusCodeHandler<>())
                .map(new Func1<Response<AddressWrapper>, Address>() {
                    @Override
                    public Address call(Response<AddressWrapper> response) {
                        return response.body() != null ? response.body().getAddress() : null;
                    }
                })
                .observeOn(getCallbackScheduler());
    }

    /**
     * Fetch all of the Addresses associated with a Customer.
     *
     * @param customer the {@link Customer} to fetch addresses for, not null
     * @param callback the {@link Callback} that will be used to indicate the response from the asynchronous network operation, not null
     */
    @Override
    public void getAddresses(final Customer customer, final Callback<List<Address>> callback) {
        getAddresses(customer).subscribe(new InternalCallbackSubscriber<>(callback));
    }

    /**
     * Fetch all of the Addresses associated with a Customer.
     *
     * @param customer the {@link Customer} to fetch addresses for, not null
     * @return cold observable that emits the requested list of addresses associated with a customer
     */
    @Override
    public Observable<List<Address>> getAddresses(final Customer customer) {
        if (customer == null) {
            throw new NullPointerException("customer cannot be null");
        }

        return retrofitService
                .getAddresses(customer.getId())
                .doOnNext(new RetrofitSuccessHttpStatusCodeHandler<>())
                .map(new Func1<Response<AddressesWrapper>, List<Address>>() {
                    @Override
                    public List<Address> call(Response<AddressesWrapper> response) {
                        return response.body() != null ? response.body().getAddresses() : null;
                    }
                })
                .observeOn(getCallbackScheduler());
    }

    /**
     * Fetch an existing Address from Shopify
     *
     * @param customer  the {@link Customer} to fetch an address for, not null
     * @param addressId the identifier of the {@link Address}
     * @param callback  the {@link Callback} that will be used to indicate the response from the asynchronous network operation, not null
     */
    @Override
    public void getAddress(final Customer customer, final String addressId, final Callback<Address> callback) {
        getAddress(customer, addressId).subscribe(new InternalCallbackSubscriber<>(callback));
    }

    /**
     * Fetch an existing Address from Shopify
     *
     * @param customer  the {@link Customer} to fetch an address for, not null
     * @param addressId the identifier of the {@link Address}
     * @return cold observable that emits requested existing address
     */
    @Override
    public Observable<Address> getAddress(final Customer customer, final String addressId) {
        if (TextUtils.isEmpty(addressId)) {
            throw new IllegalArgumentException("addressId cannot be empty");
        }

        if (customer == null) {
            throw new NullPointerException("customer cannot be null");
        }

        return retrofitService
                .getAddress(customer.getId(), addressId)
                .doOnNext(new RetrofitSuccessHttpStatusCodeHandler<>())
                .map(new Func1<Response<AddressWrapper>, Address>() {
                    @Override
                    public Address call(Response<AddressWrapper> response) {
                        return response.body() != null ? response.body().getAddress() : null;
                    }
                })
                .observeOn(getCallbackScheduler());
    }

    /**
     * Update the attributes of an existing Address
     *
     * @param customer the {@link Customer} to updatne an address for, not null
     * @param address  the {@link Address} to update
     * @param callback the {@link Callback} that will be used to indicate the response from the asynchronous network operation, not null
     */
    @Override
    public void updateAddress(final Customer customer, final Address address, final Callback<Address> callback) {
        updateAddress(customer, address).subscribe(new InternalCallbackSubscriber<>(callback));
    }

    /**
     * Update the attributes of an existing Address
     *
     * @param customer the {@link Customer} to updatne an address for, not null
     * @param address  the {@link Address} to update
     * @return returns cold observable that emits updated existing address
     */
    @Override
    public Observable<Address> updateAddress(final Customer customer, final Address address) {
        if (address == null) {
            throw new NullPointerException("address cannot be null");
        }

        if (customer == null) {
            throw new NullPointerException("customer cannot be null");
        }

        return retrofitService
                .updateAddress(customer.getId(), new AddressWrapper(address), address.getAddressId())
                .doOnNext(new RetrofitSuccessHttpStatusCodeHandler<>())
                .map(new Func1<Response<AddressWrapper>, Address>() {
                    @Override
                    public Address call(Response<AddressWrapper> response) {
                        return response.body() != null ? response.body().getAddress() : null;
                    }
                })
                .observeOn(getCallbackScheduler());
    }

    /**
     * Convenience method to release all product inventory reservations by setting the `reservationTime` of the checkout `0` and calling {@link #updateCheckout(Checkout, Callback) updateCheckout(Checkout, Callback)}.
     * We recommend creating a new `Checkout` object from a `Cart` for further API calls.
     *
     * @param checkout the {@link Checkout} to expire
     * @param callback the {@link Callback} that will be used to indicate the response from the asynchronous network operation, not null
     */
    @Override
    public void removeProductReservationsFromCheckout(final Checkout checkout, final Callback<Checkout> callback) {
        if (checkout == null || TextUtils.isEmpty(checkout.getToken())) {
            callback.failure(null);
        } else {
            checkout.setReservationTime(0);

            Checkout expiredCheckout = new Checkout();
            expiredCheckout.setToken(checkout.getToken());
            expiredCheckout.setReservationTime(0);
            updateCheckout(expiredCheckout, callback);
        }
    }

    /**
     * Convenience method to release all product inventory reservations by setting the `reservationTime` of the checkout `0` and calling {@link #updateCheckout(Checkout, Callback) updateCheckout(Checkout, Callback)}.
     * We recommend creating a new `Checkout` object from a `Cart` for further API calls.
     *
     * @param checkout the {@link Checkout} to expire
     * @return cold observable that emits updated checkout
     */
    @Override
    public Observable<Checkout> removeProductReservationsFromCheckout(final Checkout checkout) {
        if (checkout == null || TextUtils.isEmpty(checkout.getToken())) {
            return Observable.error(new RuntimeException("Missing checkout token"));
        } else {
            checkout.setReservationTime(0);

            final Checkout expiredCheckout = new Checkout();
            expiredCheckout.setToken(checkout.getToken());
            expiredCheckout.setReservationTime(0);

            return updateCheckout(expiredCheckout);
        }
    }

    /**
     * Helper methods
     */

    /**
     * Extracts the body of the {@code RetrofitError} associated with this error
     *
     * @param errorResponse the {@link RetrofitError}
     * @return the body of the response, or the error message if the body is null
     */
    public static String getErrorBody(RetrofitError errorResponse) {
        if (errorResponse == null) {
            return "Error was null";
        }
        if (errorResponse.getResponse() != null && errorResponse.getResponse().isSuccessful()) {
            return String.format("Tried to parse error on successful response! Code: %d", errorResponse.getResponse().code());
        }

        if (errorResponse.getResponse() == null) {
            return String.format("\n\tMessage: %s\n\tCode: %d\n\tResponse error body: %s", errorResponse.getMessage(), errorResponse.getCode(), "null");
        }

        try {
            return errorResponse.getResponse().errorBody().string();
        } catch (Throwable e) {
            // ignore
        }
        return errorResponse.getMessage();
    }


}
