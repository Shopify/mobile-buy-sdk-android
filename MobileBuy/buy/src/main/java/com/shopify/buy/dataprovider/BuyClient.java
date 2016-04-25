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

import org.apache.commons.codec.binary.Base64;

import com.google.gson.Gson;
import com.shopify.buy.BuildConfig;
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
import com.shopify.buy.model.internal.PaymentSessionCheckout;
import com.shopify.buy.model.internal.PaymentSessionCheckoutWrapper;
import com.shopify.buy.model.internal.ProductListings;
import com.shopify.buy.model.internal.ShippingRatesWrapper;
import com.shopify.buy.utils.CollectionUtils;
import com.shopify.buy.utils.StringUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * The {@code BuyClient} provides all requests needed to perform request on the Shopify Checkout API. Use this class to perform tasks such as getting a shop, getting collections and products for a shop, creating a {@link Checkout} on Shopify and completing Checkouts.
 * All API methods presented here run asynchronously and return results via callback on the Main thread.
 */
public class BuyClient {

    public static final int MAX_PAGE_SIZE = 250;
    public static final int MIN_PAGE_SIZE = 1;
    public static final int DEFAULT_PAGE_SIZE = 25;

    public static final String EMPTY_BODY = "";

    private static final String CUSTOMER_TOKEN_HEADER = "X-Shopify-Customer-Access-Token";
    private static final MediaType jsonMediateType = MediaType.parse("application/json; charset=utf-8");

    private final BuyRetrofitService retrofitService;
    private final OkHttpClient httpClient;
    private int pageSize = DEFAULT_PAGE_SIZE;

    private final String shopDomain;
    private final String apiKey;
    private final String appId;
    private final String applicationName;
    private String webReturnToUrl;
    private String webReturnToLabel;
    private CustomerToken customerToken;

    public String getApiKey() {
        return apiKey;
    }

    public String getAppId() {
        return appId;
    }

    public String getApplicationName() {
        return applicationName;
    }

    public String getWebReturnToUrl() {
        return webReturnToUrl;
    }

    public String getWebReturnToLabel() {
        return webReturnToLabel;
    }

    public String getShopDomain() {
        return shopDomain;
    }

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

                builder.header("Authorization", "Basic " + StringUtils.convertToBase64String(apiKey));

                if (BuyClient.this.customerToken != null && !StringUtils.isEmpty(BuyClient.this.customerToken.getAccessToken())) {
                    builder.header(CUSTOMER_TOKEN_HEADER, BuyClient.this.customerToken.getAccessToken());
                }

                // Using the full package name for BuildConfig here as a work around for Javadoc.  The source paths need to be adjusted
                builder.header("User-Agent", "Mobile Buy SDK Android/" + com.shopify.buy.BuildConfig.VERSION_NAME + "/" + applicationName);

                return chain.proceed(builder.build());
            }
        };

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(BuildConfig.OKHTTP_LOG_LEVEL);

        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .addInterceptor(requestInterceptor);

        for(Interceptor interceptor : interceptors){
            builder.addInterceptor(interceptor);
        }

        httpClient = builder.build();

        Retrofit adapter = new Retrofit.Builder()
                .baseUrl("https://" + shopDomain + "/")
                .addConverterFactory(GsonConverterFactory.create(BuyClientFactory.createDefaultGson()))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(httpClient)
                .build();

        retrofitService = adapter.create(BuyRetrofitService.class);
    }

    /**
     * Sets the {@link Customer} specific token
     *
     * @param customerToken
     */
    public void setCustomerToken(CustomerToken customerToken) {
        this.customerToken = customerToken;
    }

    /**
     * Returns the {@link Customer} specific token
     *
     * @return customer token
     */
    public CustomerToken getCustomerToken() {
        return customerToken;
    }

    /**
     * Sets the web url to be invoked by the button on the completion page of the web checkout.
     *
     * @param webReturnToUrl a url defined as a custom scheme in the Android Manifest file.
     */
    public void setWebReturnToUrl(String webReturnToUrl) {
        this.webReturnToUrl = webReturnToUrl;
    }

    /**
     * Sets the text to be displayed on the button on the completion page of the web checkout
     *
     * @param webReturnToLabel the text to display on the button.
     */
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
    public void getShop(final Callback<Shop> callback) {
        retrofitService.getShop().subscribe(new InternalCallback<Shop>() {
            @Override
            public void success(Shop body, Response response) {
                callback.success(body, response);
            }

            @Override
            public void failure(RetrofitError error) {
                callback.failure(error);
            }
        });
    }

    /**
     * Fetch a page of products
     *
     * @param page     the 1-based page index. The page size can be set with
     *                 {@link #setPageSize(int)}
     * @param callback the {@link Callback} that will be used to indicate the response from the asynchronous network operation, not null
     */
    public void getProductPage(int page, final Callback<List<Product>> callback) {
        if (page < 1) {
            throw new IllegalArgumentException("page is a 1-based index, value cannot be less than 1");
        }

        // All product responses from the server are wrapped in a ProductListings object which contains and array of products
        // For this call, we will clamp the size of the product array returned to the page size
        retrofitService.getProductPage(appId, page, pageSize).subscribe(new InternalCallback<ProductListings>() {

            @Override
            public void success(ProductListings productPage, Response response) {
                List<Product> products = null;
                if (productPage != null) {
                    products = productPage.getProducts();
                }
                callback.success(products, response);
            }

            @Override
            public void failure(RetrofitError error) {
                callback.failure(error);
            }
        });
    }

    /**
     * Fetch the product with the specified handle
     *
     * @param handle   the handle for the product to fetch
     * @param callback the {@link Callback} that will be used to indicate the response from the asynchronous network operation, not null
     */
    public void getProductWithHandle(String handle, final Callback<Product> callback) {
        if (handle == null) {
            throw new NullPointerException("handle cannot be null");
        }

        retrofitService.getProductWithHandle(appId, handle).subscribe(new InternalCallback<ProductListings>() {
            @Override
            public void success(ProductListings productPublications, Response response) {
                List<Product> products = null;
                if (productPublications != null) {
                    products = productPublications.getProducts();
                }
                if (!CollectionUtils.isEmpty(products)) {
                    callback.success(products.get(0), response);
                } else {
                    callback.success(null, response);
                }
            }

            @Override
            public void failure(RetrofitError error) {
                callback.failure(error);
            }
        });
    }

    /**
     * Fetch a single Product
     *
     * @param productId the productId for the product to fetch
     * @param callback  the {@link Callback} that will be used to indicate the response from the asynchronous network operation, not null
     */
    public void getProduct(String productId, final Callback<Product> callback) {
        if (productId == null) {
            throw new NullPointerException("productId cannot be null");
        }

        // All product responses from the server are wrapped in a ProductListings object
        // The same endpoint is used for single and multiple product queries.
        // For this call we will query with a single id, and the returned product array
        // If the id was found the array will contain a single object, otherwise it will be empty
        retrofitService.getProducts(appId, productId).subscribe(new InternalCallback<ProductListings>() {

            @Override
            public void success(ProductListings productPublications, Response response) {
                Product product = null;

                if (productPublications != null) {
                    List<Product> products = productPublications.getProducts();

                    if (products != null && products.size() > 0) {
                        product = products.get(0);
                    }
                }
                callback.success(product, response);
            }

            @Override
            public void failure(RetrofitError error) {
                callback.failure(error);
            }
        });
    }

    /**
     * Fetch a list of Products
     *
     * @param productIds a List of the productIds to fetch
     * @param callback   the {@link Callback} that will be used to indicate the response from the asynchronous network operation, not null
     */
    public void getProducts(List<String> productIds, final Callback<List<Product>> callback) {
        if (productIds == null) {
            throw new NullPointerException("productIds List cannot be null");
        }
        if (productIds.size() < 1) {
            throw new IllegalArgumentException("productIds List cannot be empty");
        }
        String queryString = StringUtils.join(",", productIds.toArray());

        // All product responses from the server are wrapped in a ProductListings object
        // The same endpoint is used for single and multiple product queries.
        // For this call we will query with multiple ids.
        // The returned product array will contain products for each id found.
        // If no ids were found, the array will be empty
        retrofitService.getProducts(appId, queryString).subscribe(new InternalCallback<ProductListings>() {

            @Override
            public void success(ProductListings productPublications, Response response) {
                List<Product> products = null;

                if (productPublications != null) {
                    products = productPublications.getProducts();
                }

                callback.success(products, response);
            }

            @Override
            public void failure(RetrofitError error) {
                callback.failure(error);
            }
        });
    }

    /**
     * Fetch the list of Products in a Collection using the sort order defined in the shop admin
     *
     * @param page         the 1-based page index. The page size can be set with {@link #setPageSize(int)}
     * @param collectionId the collectionId that we want to fetch products for
     * @param callback     the {@link Callback} that will be used to indicate the response from the asynchronous network operation, not null
     */
    public void getProducts(int page, String collectionId, final Callback<List<Product>> callback) {
        getProducts(page, collectionId, SortOrder.COLLECTION_DEFAULT, callback);
    }

    /**
     * Fetch the list of Products in a Collection
     *
     * @param page         the 1-based page index. The page size can be set with {@link #setPageSize(int)}
     * @param collectionId the collectionId that we want to fetch products for
     * @param sortOrder    the sort order for the collection.
     * @param callback     the {@link Callback} that will be used to indicate the response from the asynchronous network operation, not null
     */
    public void getProducts(int page, String collectionId, SortOrder sortOrder, final Callback<List<Product>> callback) {
        if (page < 1) {
            throw new IllegalArgumentException("page is a 1-based index, value cannot be less than 1");
        }
        if (collectionId == null) {
            throw new IllegalArgumentException("collectionId cannot be null");
        }

        retrofitService.getProducts(appId, collectionId, pageSize, page, sortOrder.toString()).subscribe(new InternalCallback<ProductListings>() {

            @Override
            public void success(ProductListings productPublications, Response response) {
                List<Product> products = null;

                if (productPublications != null) {
                    products = productPublications.getProducts();
                }

                callback.success(products, response);
            }

            @Override
            public void failure(RetrofitError error) {
                callback.failure(error);
            }
        });
    }

    /**
     * Fetch a list of Collections
     *
     * @param callback the {@link Callback} that will be used to indicate the response from the asynchronous network operation, not null
     */
    public void getCollections(final Callback<List<Collection>> callback) {
        getCollectionPage(1, callback);
    }

    /**
     * Fetch a page of collections
     *
     * @param page     the 1-based page index. The page size can be set with
     *                 {@link #setPageSize(int)}
     * @param callback the {@link Callback} that will be used to indicate the response from the asynchronous network operation, not null
     */
    public void getCollectionPage(int page, final Callback<List<Collection>> callback) {
        if (page < 1) {
            throw new IllegalArgumentException("page is a 1-based index, value cannot be less than 1");
        }

        // All collection responses from the server are wrapped in a CollectionListings object which contains and array of collections
        // For this call, we will clamp the size of the collection array returned to the page size
        retrofitService.getCollectionPage(appId, page, pageSize).subscribe(new InternalCallback<CollectionListings>() {

            @Override
            public void success(CollectionListings collectionPage, Response response) {
                List<Collection> collections = null;
                if (collectionPage != null) {
                    collections = collectionPage.getCollections();
                }
                callback.success(collections, response);
            }

            @Override
            public void failure(RetrofitError error) {
                callback.failure(error);
            }
        });
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
    public void createCheckout(Checkout checkout, final Callback<Checkout> callback) {
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

        retrofitService.createCheckout(new CheckoutWrapper(checkout)).subscribe(new InternalCallback<CheckoutWrapper>() {

            @Override
            public void success(CheckoutWrapper checkoutWrapper, Response response) {
                Checkout checkout = null;
                if(checkoutWrapper != null){
                    checkout = checkoutWrapper.getCheckout();
                }

                callback.success(checkout, response);
            }

            @Override
            public void failure(RetrofitError error) {
                callback.failure(error);
            }
        });
    }

    /**
     * Update an existing Checkout's attributes
     *
     * @param checkout the {@link Checkout} to update
     * @param callback the {@link Callback} that will be used to indicate the response from the asynchronous network operation, not null
     */
    public void updateCheckout(final Checkout checkout, final Callback<Checkout> callback) {
        if (checkout == null) {
            throw new NullPointerException("checkout cannot be null");
        }

        Checkout cleanCheckout = checkout.copyForUpdate();

        retrofitService.updateCheckout(new CheckoutWrapper(cleanCheckout), cleanCheckout.getToken()).subscribe(new InternalCallback<CheckoutWrapper>() {
            @Override
            public void success(CheckoutWrapper checkoutWrapper, Response response) {
                callback.success(checkoutWrapper.getCheckout(), response);
            }

            @Override
            public void failure(RetrofitError error) {
                callback.failure(error);
            }
        });
    }

    /**
     * Fetch shipping rates for a given Checkout
     *
     * @param checkoutToken the {@link Checkout#token} from an existing Checkout
     * @param callback      the {@link Callback} that will be used to indicate the response from the asynchronous network operation, not null
     */
    public void getShippingRates(String checkoutToken, final Callback<List<ShippingRate>> callback) {
        if (checkoutToken == null) {
            throw new NullPointerException("checkoutToken cannot be null");
        }

        retrofitService.getShippingRates(checkoutToken).subscribe(new InternalCallback<ShippingRatesWrapper>() {
            @Override
            public void success(ShippingRatesWrapper shippingRatesWrapper, Response response) {
                if (HttpURLConnection.HTTP_OK == response.code() && shippingRatesWrapper != null) {
                    callback.success(shippingRatesWrapper.getShippingRates(), response);
                } else {
                    callback.success(null, response);
                }
            }

            @Override
            public void failure(RetrofitError error) {
                callback.failure(error);
            }
        });
    }

    /**
     * Post a credit card to Shopify's card server and associate it with a Checkout
     *
     * @param card     the {@link CreditCard} to associate
     * @param checkout the {@link Checkout} to associate the card with
     * @param callback the {@link Callback} that will be used to indicate the response from the asynchronous network operation, not null
     */
    public void storeCreditCard(final CreditCard card, final Checkout checkout, final Callback<Checkout> callback) {
        if (card == null) {
            throw new NullPointerException("card cannot be null");
        }

        if (checkout == null) {
            throw new NullPointerException("checkout cannot be null");
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                PaymentSessionCheckoutWrapper dataWrapper = new PaymentSessionCheckoutWrapper();
                PaymentSessionCheckout data = new PaymentSessionCheckout();
                data.setToken(checkout.getToken());
                data.setCreditCard(card);
                data.setBillingAddress(checkout.getBillingAddress());
                dataWrapper.setCheckout(data);

                RequestBody body = RequestBody.create(jsonMediateType, new Gson().toJson(dataWrapper));

                Request request = new Request.Builder()
                        .url(checkout.getPaymentUrl())
                        .post(body)
                        .addHeader("Authorization", "Basic " + StringUtils.convertToBase64String(apiKey))
                        .addHeader("Content-Type", "application/json")
                        .addHeader("Accept", "application/json")
                        .build();
                try {
                    okhttp3.Response httpResponse = httpClient.newCall(request).execute();
                    String paymentSessionId = parsePaymentSessionResponse(httpResponse);
                    checkout.setPaymentSessionId(paymentSessionId);

                    Response retrofitResponse = Response.success(checkout, httpResponse);
                    callback.success(checkout, retrofitResponse);
                } catch (IOException e) {
                    e.printStackTrace();

                    callback.failure(RetrofitError.exception(e));
                }
            }
        }).start();
    }

    /**
     * Complete the checkout and process the payment session
     *
     * @param checkout a {@link Checkout} that has had a {@link CreditCard} associated with it using {@link #storeCreditCard(CreditCard, Checkout, Callback)}
     * @param callback the {@link Callback} that will be used to indicate the response from the asynchronous network operation, not null
     */
    public void completeCheckout(Checkout checkout, final Callback<Checkout> callback) {
        if (checkout == null) {
            throw new NullPointerException("checkout cannot be null");
        }

        HashMap<String, String> requestBodyMap = new HashMap<>();

        String paymentSessionId = checkout.getPaymentSessionId();
        if (!StringUtils.isEmpty(paymentSessionId)) {
            requestBodyMap.put("payment_session_id", checkout.getPaymentSessionId());
        }

        retrofitService.completeCheckout(requestBodyMap, checkout.getToken()).subscribe(new InternalCallback<CheckoutWrapper>() {
            @Override
            public void success(CheckoutWrapper checkoutWrapper, Response response) {
                callback.success(checkoutWrapper.getCheckout(), response);
            }

            @Override
            public void failure(RetrofitError error) {
                callback.failure(error);
            }
        });
    }

    /**
     * Get the status of the payment session associated with {@code checkout}. {@code callback} will be
     * called with a boolean value indicating whether the session has completed or not. This method
     * should be polled until the {@code callback} response is {@code true}
     *
     * @param checkout a {@link Checkout} that has been passed as a parameter to {@link #completeCheckout(Checkout, Callback)}
     * @param callback the {@link Callback} that will be used to indicate the response from the asynchronous network operation, not null
     */
    public void getCheckoutCompletionStatus(Checkout checkout, final Callback<Boolean> callback) {
        if (checkout == null) {
            throw new NullPointerException("checkout cannot be null");
        }

        retrofitService.getCheckoutCompletionStatus(checkout.getToken()).subscribe(new InternalCallback<Void>() {

            @Override
            public void success(Void aVoid, Response response) {
                if (HttpURLConnection.HTTP_OK == response.code()) {
                    callback.success(true, response);
                } else {
                    callback.success(false, response);
                }

            }

            @Override
            public void failure(RetrofitError error) {
                callback.failure(error);
            }
        });
    }

    /**
     * Fetch an existing Checkout from Shopify
     *
     * @param checkoutToken the token associated with the existing {@link Checkout}
     * @param callback      the {@link Callback} that will be used to indicate the response from the asynchronous network operation, not null
     */
    public void getCheckout(String checkoutToken, final Callback<Checkout> callback) {
        if (checkoutToken == null) {
            throw new NullPointerException("checkoutToken cannot be null");
        }

        retrofitService.getCheckout(checkoutToken).subscribe(new InternalCallback<CheckoutWrapper>() {
            @Override
            public void success(CheckoutWrapper checkoutWrapper, Response response) {
                callback.success(checkoutWrapper.getCheckout(), response);
            }

            @Override
            public void failure(RetrofitError error) {
                callback.failure(error);
            }
        });
    }


    /**
     * Apply a gift card to a Checkout
     *
     * @param giftCardCode the gift card code for a gift card associated with the current Shop
     * @param checkout     the {@link Checkout} object to apply the gift card to
     * @param callback     the {@link Callback} that will be used to indicate the response from the asynchronous network operation, not null
     */
    public void applyGiftCard(String giftCardCode, final Checkout checkout, final Callback<Checkout> callback) {
        if (checkout == null) {
            throw new NullPointerException("checkout cannot be null");
        }

        final Checkout cleanCheckout = checkout.copy();

        final GiftCard giftCard = new GiftCard(giftCardCode);

        retrofitService.applyGiftCard(new GiftCardWrapper(giftCard), checkout.getToken()).subscribe(new InternalCallback<GiftCardWrapper>() {
            @Override
            public void success(GiftCardWrapper giftCardWrapper, Response response) {
                GiftCard updatedGiftCard = giftCardWrapper.getGiftCard();
                cleanCheckout.addGiftCard(updatedGiftCard);
                cleanCheckout.setPaymentDue(updatedGiftCard.getCheckout().getPaymentDue());
                callback.success(cleanCheckout, response);
            }

            @Override
            public void failure(RetrofitError error) {
                callback.failure(error);
            }
        });
    }

    /**
     * Remove a gift card that was previously applied to a Checkout
     *
     * @param giftCard the {@link GiftCard} to remove from the {@link Checkout}
     * @param checkout the {@code Checkout} to remove the {@code GiftCard} from
     * @param callback the {@link Callback} that will be used to indicate the response from the asynchronous network operation, not null
     */
    public void removeGiftCard(final GiftCard giftCard, final Checkout checkout, final Callback<Checkout> callback) {
        if (checkout == null) {
            throw new NullPointerException("checkout cannot be null");
        }

        if (giftCard == null) {
            throw new NullPointerException("giftCard cannot be null");
        }

        retrofitService.removeGiftCard(giftCard.getId(), checkout.getToken()).subscribe(new InternalCallback<GiftCardWrapper>() {
            @Override
            public void success(GiftCardWrapper giftCardWrapper, Response response) {
                GiftCard updatedGiftCard = giftCardWrapper.getGiftCard();
                checkout.removeGiftCard(updatedGiftCard);
                checkout.setPaymentDue(updatedGiftCard.getCheckout().getPaymentDue());
                callback.success(checkout, response);
            }

            @Override
            public void failure(RetrofitError error) {
                callback.failure(error);
            }
        });
    }

    /**
     * Create a new Customer on Shopify
     * @param accountCredentials the account credentials with an email, password, first name, and last name of the {@link Customer} to be created, not null
     * @param callback the {@link Callback} that will be used to indicate the response from the asynchronous network operation, not null
     */
    public void createCustomer(final AccountCredentials accountCredentials, final Callback<Customer> callback) {
        if (accountCredentials == null) {
            throw new NullPointerException("accountCredentials cannot be null");
        }

        final AccountCredentialsWrapper accountCredentialsWrapper = new AccountCredentialsWrapper(accountCredentials);

        retrofitService.createCustomer(accountCredentialsWrapper).subscribe(new InternalCallback<CustomerWrapper>() {
            @Override
            public void success(CustomerWrapper customerWrapper, Response response) {
                callback.success(customerWrapper.getCustomer(), response);
            }

            @Override
            public void failure(RetrofitError error) {
                callback.failure(error);
            }
        });
    }

    /**
     * Activate the customer account.
     * @param customerId         the id of the {@link Customer} to activate
     * @param activationToken    the activation token for the Customer, not null or empty
     * @param accountCredentials the account credentials with a password of the {@link Customer} to be activated, not null
     * @param callback           the {@link Callback} that will be used to indicate the response from the asynchronous network operation, not null
     */
    public void activateCustomer(final Long customerId, final String activationToken, final AccountCredentials accountCredentials, final Callback<Customer> callback) {
        if (StringUtils.isEmpty(activationToken)) {
            throw new IllegalArgumentException("activation token cannot be empty");
        }

        if (accountCredentials == null) {
            throw new NullPointerException("accountCredentials cannot be null");
        }

        AccountCredentialsWrapper accountCredentialsWrapper = new AccountCredentialsWrapper(accountCredentials);

        retrofitService.activateCustomer(activationToken, accountCredentialsWrapper, customerId).subscribe(new InternalCallback<CustomerWrapper>() {
            @Override
            public void success(CustomerWrapper customerWrapper, Response response) {
                callback.success(customerWrapper.getCustomer(), response);
            }

            @Override
            public void failure(RetrofitError error) {
                callback.failure(error);
            }
        });

    }

    /**
     * Reset the password for the customer account.
     * @param customerId         the id of the {@link Customer} to activate
     * @param resetToken         the reset token for the Customer, not null or empty
     * @param accountCredentials the account credentials with the new password of the {@link Customer}. not null
     * @param callback           the {@link Callback} that will be used to indicate the response from the asynchronous network operation, not null
     */
    public void resetPassword(final Long customerId, final String resetToken, final AccountCredentials accountCredentials, final Callback<Customer> callback) {
        if (StringUtils.isEmpty(resetToken)) {
            throw new IllegalArgumentException("reset token cannot be empty");
        }

        if (accountCredentials == null) {
            throw new NullPointerException("accountCredentials cannot be null");
        }

        AccountCredentialsWrapper accountCredentialsWrapper = new AccountCredentialsWrapper(accountCredentials);

        retrofitService.resetPassword(resetToken, accountCredentialsWrapper, customerId).subscribe(new InternalCallback<CustomerWrapper>() {
            @Override
            public void success(CustomerWrapper customerWrapper, Response response) {
                callback.success(customerWrapper.getCustomer(), response);
            }

            @Override
            public void failure(RetrofitError error) {
                callback.failure(error);
            }
        });

    }

    /**
     * Log an existing Customer into Shopify
     * @param accountCredentials the account credentials with an email and password of the {@link Customer}, not null
     * @param callback the {@link Callback} that will be used to indicate the response from the asynchronous network operation, not null
     */
    public void loginCustomer(final AccountCredentials accountCredentials, final Callback<CustomerToken> callback) {
        if (accountCredentials == null) {
            throw new NullPointerException("accountCredentials cannot be null");
        }

        final AccountCredentialsWrapper accountCredentialsWrapper = new AccountCredentialsWrapper(accountCredentials);

        retrofitService.getCustomerToken(accountCredentialsWrapper).subscribe(new InternalCallback<CustomerTokenWrapper>() {
            @Override
            public void success(CustomerTokenWrapper customerTokenWrapper, Response response) {
                customerToken = customerTokenWrapper.getCustomerToken();
                callback.success(customerTokenWrapper.getCustomerToken(), response);
            }

            @Override
            public void failure(RetrofitError error) {
                callback.failure(error);
            }
        });
    }

    /**
     * Log a Customer out from Shopify
     *
     * @param callback the {@link Callback} that will be used to indicate the response from the asynchronous network operation, not null
     */
    public void logoutCustomer(final Callback<Void> callback) {
        if (customerToken == null) {
            return;
        }

        retrofitService.removeCustomerToken(customerToken.getCustomerId()).subscribe(new InternalCallback<Void>() {
            @Override
            public void success(Void aVoid, Response response) {
                customerToken = null;
                callback.success(aVoid, response);
            }

            @Override
            public void failure(RetrofitError error) {
                callback.failure(error);
            }
        });
    }

    /**
     * Update an existing Customer's attributes.
     *
     * @param customer the {@link Customer} to update
     * @param callback the {@link Callback} that will be used to indicate the response from the asynchronous network operation, not null
     */
    public void updateCustomer(final Customer customer, final Callback<Customer> callback) {
        if (customer == null) {
            throw new NullPointerException("customer cannot be null");
        }

        retrofitService.updateCustomer(customer.getId(), new CustomerWrapper(customer)).subscribe(new InternalCallback<CustomerWrapper>() {
            @Override
            public void success(CustomerWrapper customerWrapper, Response response) {
                callback.success(customerWrapper.getCustomer(), response);
            }

            @Override
            public void failure(RetrofitError error) {
                callback.failure(error);
            }
        });
    }

    /**
     * Retrieve a Customer's details from Shopify.
     *
     * @param customerId the identifier of a {@link CustomerToken} or {@link Customer}
     * @param callback the {@link Callback} that will be used to indicate the response from the asynchronous network operation, not null
     */
    public void getCustomer(final Long customerId, final Callback<Customer> callback) {
        if (customerId == null) {
            throw new NullPointerException("customer Id cannot be null");
        }

        retrofitService.getCustomer(customerId).subscribe(new InternalCallback<CustomerWrapper>() {
            @Override
            public void success(CustomerWrapper customerWrapper, Response response) {
                callback.success(customerWrapper.getCustomer(), response);
            }

            @Override
            public void failure(RetrofitError error) {
                callback.failure(error);
            }
        });
    }

    /**
     * Renew a Customer login.  This should be called periodically to keep the token up to date.
     *
     * @param callback the {@link Callback} that will be used to indicate the response from the asynchronous network operation, not null
     */
    public void renewCustomer(final Callback<CustomerToken> callback) {
        if (customerToken == null) {
            return;
        }

        retrofitService.renewCustomerToken(EMPTY_BODY, customerToken.getCustomerId()).subscribe(new InternalCallback<CustomerTokenWrapper>() {
            @Override
            public void success(CustomerTokenWrapper customerTokenWrapper, Response response) {
                customerToken = customerTokenWrapper.getCustomerToken();
                callback.success(customerTokenWrapper.getCustomerToken(), response);
            }

            @Override
            public void failure(RetrofitError error) {
                callback.failure(error);
            }
        });
    }

    /**
     * Send a password recovery email. An email will be sent to the email address specified if a customer with that email address exists on Shopify.
     *
     * @param email    the email address to send the password recovery email to
     * @param callback the {@link Callback} that will be used to indicate the response from the asynchronous network operation, not null
     */
    public void recoverPassword(final String email, final Callback<Void> callback) {
        if (StringUtils.isEmpty(email)) {
            throw new IllegalArgumentException("email cannot be empty");
        }

        retrofitService.recoverCustomer(new EmailWrapper(email)).subscribe(new InternalCallback<Void>() {
            @Override
            public void success(Void aVoid, Response response) {
                callback.success(aVoid, response);
            }

            @Override
            public void failure(RetrofitError error) {
                callback.failure(error);
            }
        });
    }

    /**
     * Fetch the Orders associated with a Customer.
     *
     * @param customer the {@link Customer} to fetch the orders for, not null
     * @param callback the {@link Callback} that will be used to indicate the response from the asynchronous network operation, not null
     */
    public void getOrders(final Customer customer, final Callback<List<Order>> callback) {
        if (customer == null) {
            throw new NullPointerException("customer cannot be null");
        }

        retrofitService.getOrders(customer.getId()).subscribe(new InternalCallback<OrdersWrapper>() {
            @Override
            public void success(OrdersWrapper ordersWrapper, Response response) {
                callback.success(ordersWrapper.getOrders(), response);
            }

            @Override
            public void failure(RetrofitError error) {
                callback.failure(error);
            }
        });
    }

    /**
     * Fetch an existing Order from Shopify
     *
     * @param customer the {@link Customer} to fetch the order for
     * @param orderId  the identifier of the {@link Order} to retrieve
     * @param callback the {@link Callback} that will be used to indicate the response from the asynchronous network operation, not null
     */
    public void getOrder(final Customer customer, final String orderId, final Callback<Order> callback) {
        if (StringUtils.isEmpty(orderId)) {
            throw new IllegalArgumentException("orderId cannot be empty");
        }

        if (customer == null) {
            throw new NullPointerException("customer cannot be null");
        }

        retrofitService.getOrder(customer.getId(), orderId).subscribe(new InternalCallback<OrderWrapper>() {
            @Override
            public void success(OrderWrapper orderWrapper, Response response) {
                callback.success(orderWrapper.getOrder(), response);
            }

            @Override
            public void failure(RetrofitError error) {
                callback.failure(error);
            }
        });
    }

    /**
     * Create an Address and associate it with a Customer
     *
     * @param customer the {@link Customer} to create and address for, not null
     * @param address  the {@link Address} to create, not null
     * @param callback the {@link Callback} that will be used to indicate the response from the asynchronous network operation, not null
     */
    public void createAddress(final Customer customer, final Address address, final Callback<Address> callback) {
        if (address == null) {
            throw new NullPointerException("address cannot be null");
        }

        if (customer == null) {
            throw new NullPointerException("customer cannot be null");
        }

        retrofitService.createAddress(customer.getId(), new AddressWrapper(address)).subscribe(new InternalCallback<AddressWrapper>() {
            @Override
            public void success(AddressWrapper addressWrapper, Response response) {
                callback.success(addressWrapper.getAddress(), response);
            }

            @Override
            public void failure(RetrofitError error) {
                callback.failure(error);
            }
        });
    }

    /**
     * Fetch all of the Addresses associated with a Customer.
     *
     * @param customer the {@link Customer} to fetch addresses for, not null
     * @param callback the {@link Callback} that will be used to indicate the response from the asynchronous network operation, not null
     */
    public void getAddresses(final Customer customer, final Callback<List<Address>> callback) {
        if (customer == null) {
            throw new NullPointerException("customer cannot be null");
        }

        retrofitService.getAddresses(customer.getId()).subscribe(new InternalCallback<AddressesWrapper>() {
            @Override
            public void success(AddressesWrapper addressesWrapper, Response response) {
                callback.success(addressesWrapper.getAddresses(), response);
            }

            @Override
            public void failure(RetrofitError error) {
                callback.failure(error);
            }
        });
    }

    /**
     * Fetch an existing Address from Shopify
     *
     * @param customer the {@link Customer} to fetch an address for, not null
     * @param addressId the identifier of the {@link Address}
     * @param callback  the {@link Callback} that will be used to indicate the response from the asynchronous network operation, not null
     */
    public void getAddress(final Customer customer, final String addressId, final Callback<Address> callback) {
        if (StringUtils.isEmpty(addressId)) {
            throw new IllegalArgumentException("addressId cannot be empty");
        }

        if (customer == null) {
            throw new NullPointerException("customer cannot be null");
        }

        retrofitService.getAddress(customer.getId(), addressId).subscribe(new InternalCallback<AddressWrapper>() {
            @Override
            public void success(AddressWrapper addressWrapper, Response response) {
                callback.success(addressWrapper.getAddress(), response);
            }

            @Override
            public void failure(RetrofitError error) {
                callback.failure(error);
            }
        });
    }

    /**
     * Update the attributes of an existing Address
     *
     * @param customer the {@link Customer} to updatne an address for, not null
     * @param address  the {@link Address} to update
     * @param callback the {@link Callback} that will be used to indicate the response from the asynchronous network operation, not null
     */
    public void updateAddress(final Customer customer, final Address address, final Callback<Address> callback) {
        if (address == null) {
            throw new NullPointerException("address cannot be null");
        }

        if (customer == null) {
            throw new NullPointerException("customer cannot be null");
        }

        retrofitService.updateAddress(customer.getId(), new AddressWrapper(address), address.getAddressId()).subscribe(new InternalCallback<AddressWrapper>() {
            @Override
            public void success(AddressWrapper addressWrapper, Response response) {
                callback.success(addressWrapper.getAddress(), response);
            }

            @Override
            public void failure(RetrofitError error) {
                callback.failure(error);
            }
        });
    }

    /**
     * Convenience method to release all product inventory reservations by setting the `reservationTime` of the checkout `0` and calling {@link #updateCheckout(Checkout, Callback) updateCheckout(Checkout, Callback)}.
     * We recommend creating a new `Checkout` object from a `Cart` for further API calls.
     *
     * @param checkout the {@link Checkout} to expire
     * @param callback the {@link Callback} that will be used to indicate the response from the asynchronous network operation, not null
     */
    public void removeProductReservationsFromCheckout(final Checkout checkout, final Callback<Checkout> callback) {
        if (checkout == null || StringUtils.isEmpty(checkout.getToken())) {
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
     * Helper methods
     */

    /**
     * Extracts the body of the {@code RetrofitError} associated with this error
     *
     * @param errorResponse the {@link RetrofitError}
     * @return the body of the response, or the error message if the body is null
     */
    public static String getErrorBody(RetrofitError errorResponse) {
        return getErrorBody(errorResponse.getResponse());
    }

    /**
     * Extracts the body of the {@code Response} associated with this error
     *
     * @param error the {@link Response}
     * @return the body of the response, or the error message if the body is null
     */
    public static String getErrorBody(Response error) {
        if (error == null || error.isSuccessful()) {
            return "null";
        }
        try {
            return error.errorBody().string();
        } catch (Throwable e) {
            // ignore
        }
        return error.message();
    }

    private String parsePaymentSessionResponse(okhttp3.Response response) throws IOException {
        String paymentSessionId = null;
        if (response.isSuccessful()) {
            String jsonString = response.body().string();
            try {
                JSONObject json = new JSONObject(jsonString);
                paymentSessionId = (String) json.get("id");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return paymentSessionId;
    }

}
