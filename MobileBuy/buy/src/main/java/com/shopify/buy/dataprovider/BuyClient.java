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
import com.shopify.buy.model.internal.CollectionPublication;
import com.shopify.buy.model.internal.CustomerTokenWrapper;
import com.shopify.buy.model.internal.CustomerWrapper;
import com.shopify.buy.model.internal.EmailWrapper;
import com.shopify.buy.model.internal.GiftCardWrapper;
import com.shopify.buy.model.internal.MarketingAttribution;
import com.shopify.buy.model.internal.OrderWrapper;
import com.shopify.buy.model.internal.OrdersWrapper;
import com.shopify.buy.model.internal.PaymentSessionCheckout;
import com.shopify.buy.model.internal.PaymentSessionCheckoutWrapper;
import com.shopify.buy.model.internal.PaymentToken;
import com.shopify.buy.model.internal.PaymentTokenWrapper;
import com.shopify.buy.model.internal.ProductPublication;
import com.shopify.buy.model.internal.ShippingRatesWrapper;
import com.shopify.buy.utils.CollectionUtils;
import com.squareup.okhttp.Cache;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import retrofit.Callback;
import retrofit.RequestInterceptor;
import retrofit.ResponseCallback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Header;
import retrofit.client.OkClient;
import retrofit.client.Response;
import retrofit.converter.GsonConverter;
import retrofit.mime.TypedByteArray;

/**
 * The {@code BuyClient} provides all requests needed to perform request on the Shopify Checkout API. Use this class to perform tasks such as getting a shop, getting collections and products for a shop, creating a {@link Checkout} on Shopify and completing Checkouts.
 * All API methods presented here run asynchronously and return results via callback on the Main thread.
 */
public class BuyClient {

    public static final int MAX_PAGE_SIZE = 250;
    public static final int MIN_PAGE_SIZE = 1;
    public static final int DEFAULT_PAGE_SIZE = 25;

    private static final String PAYMENT_TOKEN_TYPE_ANDROID_PAY = "android_pay";
    public static final String EMPTY_BODY = "";
    private static final MediaType jsonMediateType = MediaType.parse("application/json; charset=utf-8");

    private final BuyRetrofitService retrofitService;
    private final OkHttpClient httpClient;
    private int pageSize = DEFAULT_PAGE_SIZE;

    private final String shopDomain;
    private final String apiKey;
    private final String channelId;
    private final String applicationName;
    private String webReturnToUrl;
    private String webReturnToLabel;
    private CustomerToken customerToken;

    private String androidPayPublicKey;
    private String androidPayPublicKeyHash;

    public String getApiKey() {
        return apiKey;
    }

    public String getChannelId() {
        return channelId;
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

    BuyClient(final String apiKey, final String channelId, final String applicationName, final String shopDomain, final CustomerToken customerToken) {
        this.apiKey = apiKey;
        this.channelId = channelId;
        this.applicationName = applicationName;
        this.shopDomain = shopDomain;
        this.customerToken = customerToken;

        RequestInterceptor requestInterceptor = new RequestInterceptor() {
            @Override
            public void intercept(RequestFacade request) {
                request.addHeader("Authorization", "Basic " + Base64.encodeToString(apiKey.getBytes(), Base64.NO_WRAP));

                if (BuyClient.this.customerToken != null && !TextUtils.isEmpty(BuyClient.this.customerToken.getAccessToken())) {
                    request.addHeader("X-Shopify-Customer-Access-Token", BuyClient.this.customerToken.getAccessToken());
                }

                // Using the full package name for BuildConfig here as a work around for Javadoc.  The source paths need to be adjusted
                request.addHeader("User-Agent", "Mobile Buy SDK Android/" + com.shopify.buy.BuildConfig.VERSION_NAME + "/" + applicationName);
            }
        };

        httpClient = new OkHttpClient();
        httpClient.setConnectTimeout(30, TimeUnit.SECONDS);
        httpClient.setReadTimeout(60, TimeUnit.SECONDS);
        httpClient.setWriteTimeout(60, TimeUnit.SECONDS);

        RestAdapter adapter = new RestAdapter.Builder()
                .setEndpoint("https://" + shopDomain)
                .setConverter(new GsonConverter(BuyClientFactory.createDefaultGson()))
                .setClient(new OkClient(httpClient))
                .setLogLevel(BuildConfig.RETROFIT_LOG_LEVEL)
                .setRequestInterceptor(requestInterceptor)
                .build();

        retrofitService = adapter.create(BuyRetrofitService.class);
    }

    /**
     * For internal use only.
     */
    public void addInterceptor(Interceptor interceptor) {
        httpClient.interceptors().add(interceptor);
    }

    public void addNetworkInterceptor(Interceptor interceptor) {
        httpClient.networkInterceptors().add(interceptor);
    }

    public void setCacheForOkHTTP(Cache cache) {
        httpClient.setCache(cache);
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
     * Enables Android Pay support in the {@link BuyClient}
     * @param androidPayPublicKey The base64 encoded public key associated with Android Pay.
     */
    public void enableAndroidPay(String androidPayPublicKey) {
        if (TextUtils.isEmpty(androidPayPublicKey)) {
            throw new IllegalArgumentException("androidPayPublicKey cannot be empty");
        }

        byte[] digest;
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            digest = messageDigest.digest(androidPayPublicKey.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            // Do not enable Android Pay if the hash could not be computed
            return;
        }

        // Enable Android Pay by setting the hash and key
        this.androidPayPublicKeyHash = Base64.encodeToString(digest, Base64.DEFAULT);
        this.androidPayPublicKey = androidPayPublicKey;
    }

    public boolean androidPayIsEnabled() {
        return !TextUtils.isEmpty(androidPayPublicKey);
    }

    public void disableAndroidPay() {
        androidPayPublicKeyHash = null;
        androidPayPublicKey = null;
    }

    public String getAndroidPayPublicKey() {
        return androidPayPublicKey;
    }

    public String getAndroidPayPublicKeyHash() {
        return androidPayPublicKeyHash;
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
    public void getShop(Callback<Shop> callback) {
        retrofitService.getShop(callback);
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

        // All product responses from the server are wrapped in a ProductPublication object which contains and array of products
        // For this call, we will clamp the size of the product array returned to the page size
        retrofitService.getProductPage(channelId, page, pageSize, new Callback<ProductPublication>() {
            @Override
            public void success(ProductPublication productPage, Response response) {
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

        retrofitService.getProductWithHandle(channelId, handle, new Callback<ProductPublication>() {
            @Override
            public void success(ProductPublication productPublications, Response response) {
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

        // All product responses from the server are wrapped in a ProductPublication object
        // The same endpoint is used for single and multiple product queries.
        // For this call we will query with a single id, and the returned product array
        // If the id was found the array will contain a single object, otherwise it will be empty
        retrofitService.getProducts(channelId, productId, new Callback<ProductPublication>() {
            @Override
            public void success(ProductPublication productPublications, Response response) {
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
        String queryString = TextUtils.join(",", productIds.toArray());

        // All product responses from the server are wrapped in a ProductPublication object
        // The same endpoint is used for single and multiple product queries.
        // For this call we will query with multiple ids.
        // The returned product array will contain products for each id found.
        // If no ids were found, the array will be empty
        retrofitService.getProducts(channelId, queryString, new Callback<ProductPublication>() {
            @Override
            public void success(ProductPublication productPublications, Response response) {
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

        retrofitService.getProducts(channelId, collectionId, pageSize, page, sortOrder.toString(), new Callback<ProductPublication>() {
            @Override
            public void success(ProductPublication productPublications, Response response) {
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

        // All collection responses from the server are wrapped in a CollectionPublication object which contains and array of collections
        // For this call, we will clamp the size of the collection array returned to the page size
        retrofitService.getCollectionPage(channelId, page, pageSize, new Callback<CollectionPublication>() {
            @Override
            public void success(CollectionPublication collectionPage, Response response) {
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

        checkout.setChannelId(channelId);
        checkout.setMarketingAttribution(new MarketingAttribution(applicationName));
        checkout.setSourceName("mobile_app");
        checkout.setSourceIdentifier(channelId);

        if (webReturnToUrl != null) {
            checkout.setWebReturnToUrl(webReturnToUrl);
        }

        if (webReturnToLabel != null) {
            checkout.setWebReturnToLabel(webReturnToLabel);
        }

        retrofitService.createCheckout(new CheckoutWrapper(checkout), new Callback<CheckoutWrapper>() {
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

        retrofitService.updateCheckout(new CheckoutWrapper(cleanCheckout), cleanCheckout.getToken(), new Callback<CheckoutWrapper>() {
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

        retrofitService.getShippingRates(checkoutToken, new Callback<ShippingRatesWrapper>() {
            @Override
            public void success(ShippingRatesWrapper shippingRatesWrapper, Response response) {
                if (HttpURLConnection.HTTP_OK == response.getStatus() && shippingRatesWrapper != null) {
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
                        .addHeader("Authorization", "Basic " + Base64.encodeToString(apiKey.getBytes(), Base64.NO_WRAP))
                        .addHeader("Content-Type", "application/json")
                        .addHeader("Accept", "application/json")
                        .build();
                try {
                    com.squareup.okhttp.Response httpResponse = httpClient.newCall(request).execute();
                    String paymentSessionId = parsePaymentSessionResponse(httpResponse);
                    checkout.setPaymentSessionId(paymentSessionId);

                    Response retrofitResponse = new Response(request.urlString(), httpResponse.code(), httpResponse.message(), Collections.<Header>emptyList(), null);
                    callback.success(checkout, retrofitResponse);
                } catch (IOException e) {
                    e.printStackTrace();

                    callback.failure(RetrofitError.unexpectedError(request.urlString(), e));
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
        if (!TextUtils.isEmpty(paymentSessionId)) {
            requestBodyMap.put("payment_session_id", checkout.getPaymentSessionId());
        }

        retrofitService.completeCheckout(requestBodyMap, checkout.getToken(), new Callback<CheckoutWrapper>() {
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
     * Complete the checkout and process the Android Pay Token for payment
     *
     * @param androidPayToken the token returned in the {@link com.google.android.gms.wallet.FullWallet}
     * @param checkout the {@link Checkout} to complete
     * @param callback the {@link Callback} that will be used to indicate the response from the asynchronous network operation, not null
     */
    public void completeCheckout(String androidPayToken, Checkout checkout, final Callback<Checkout> callback) {
        if (!androidPayIsEnabled()) {
            throw new UnsupportedOperationException("Android Pay is not enabled");
        }
        if (checkout == null) {
            throw new NullPointerException("checkout cannot be null");
        }
        if (TextUtils.isEmpty(androidPayToken)) {
            throw new IllegalArgumentException("androidPayToken cannot be null");
        }

        PaymentToken paymentToken = new PaymentToken(androidPayToken, PAYMENT_TOKEN_TYPE_ANDROID_PAY, androidPayPublicKeyHash);

        retrofitService.completeCheckout(new PaymentTokenWrapper(paymentToken), checkout.getToken(), new Callback<CheckoutWrapper>() {
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

        retrofitService.getCheckoutCompletionStatus(checkout.getToken(), new ResponseCallback() {
            @Override
            public void success(Response response) {
                if (HttpURLConnection.HTTP_OK == response.getStatus()) {
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

        retrofitService.getCheckout(checkoutToken, new Callback<CheckoutWrapper>() {
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

        final GiftCard giftCard = new GiftCard(giftCardCode);

        retrofitService.applyGiftCard(new GiftCardWrapper(giftCard), checkout.getToken(), new Callback<GiftCardWrapper>() {
            @Override
            public void success(GiftCardWrapper giftCardWrapper, Response response) {
                GiftCard updatedGiftCard = giftCardWrapper.getGiftCard();
                checkout.addGiftCard(updatedGiftCard);
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

        retrofitService.removeGiftCard(giftCard.getId(), checkout.getToken(), new Callback<GiftCardWrapper>() {
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

        retrofitService.createCustomer(accountCredentialsWrapper, new Callback<CustomerWrapper>() {
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
        if (TextUtils.isEmpty(activationToken)) {
            throw new IllegalArgumentException("activation token cannot be empty");
        }

        if (accountCredentials == null) {
            throw new NullPointerException("accountCredentials cannot be null");
        }

        AccountCredentialsWrapper accountCredentialsWrapper = new AccountCredentialsWrapper(accountCredentials);

        retrofitService.activateCustomer(activationToken, accountCredentialsWrapper, customerId, new Callback<CustomerWrapper>() {
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
        if (TextUtils.isEmpty(resetToken)) {
            throw new IllegalArgumentException("reset token cannot be empty");
        }

        if (accountCredentials == null) {
            throw new NullPointerException("accountCredentials cannot be null");
        }

        AccountCredentialsWrapper accountCredentialsWrapper = new AccountCredentialsWrapper(accountCredentials);

        retrofitService.resetPassword(resetToken, accountCredentialsWrapper, customerId, new Callback<CustomerWrapper>() {
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

        retrofitService.getCustomerToken(accountCredentialsWrapper, new Callback<CustomerTokenWrapper>() {

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

        retrofitService.removeCustomerToken(customerToken.getCustomerId(), new Callback<Void>() {
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

        retrofitService.updateCustomer(customer.getId(), new CustomerWrapper(customer), new Callback<CustomerWrapper>() {
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

        retrofitService.getCustomer(customerId, new Callback<CustomerWrapper>() {
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

        retrofitService.renewCustomerToken(EMPTY_BODY, customerToken.getCustomerId(), new Callback<CustomerTokenWrapper>() {
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
        if (TextUtils.isEmpty(email)) {
            throw new IllegalArgumentException("email cannot be empty");
        }

        retrofitService.recoverCustomer(new EmailWrapper(email), new Callback<Void>() {
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

        retrofitService.getOrders(customer.getId(), new Callback<OrdersWrapper>() {
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
        if (TextUtils.isEmpty(orderId)) {
            throw new IllegalArgumentException("orderId cannot be empty");
        }

        if (customer == null) {
            throw new NullPointerException("customer cannot be null");
        }

        retrofitService.getOrder(customer.getId(), orderId, new Callback<OrderWrapper>() {
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

        retrofitService.createAddress(customer.getId(), new AddressWrapper(address), new Callback<AddressWrapper>() {
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

        retrofitService.getAddresses(customer.getId(), new Callback<AddressesWrapper>() {
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
        if (TextUtils.isEmpty(addressId)) {
            throw new IllegalArgumentException("addressId cannot be empty");
        }

        if (customer == null) {
            throw new NullPointerException("customer cannot be null");
        }

        retrofitService.getAddress(customer.getId(), addressId, new Callback<AddressWrapper>() {
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

        retrofitService.updateAddress(customer.getId(), new AddressWrapper(address), address.getAddressId(), new Callback<AddressWrapper>() {
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
     * Test the integration with your shop.  This should not be shipped in production code
     *
     * @param callback the {@link Callback} that will be used to indicate the response from the asynchronous network operation, not null
     */
    public void testIntegration(final Callback<Void> callback) {
        retrofitService.testIntegration(apiKey, channelId, new Callback<Void>() {
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
     * Helper methods
     */

    /**
     * Extracts the body of the {@code Response} associated with this error
     *
     * @param error the {@link RetrofitError}
     * @return the body of the response, or the error message if the body is null
     */
    public static String getErrorBody(RetrofitError error) {
        if (error == null) {
            return "null";
        }
        try {
            return new String(((TypedByteArray) error.getResponse().getBody()).getBytes());
        } catch (Throwable e) {
            // ignore
        }
        return error.getMessage();
    }

    private String parsePaymentSessionResponse(com.squareup.okhttp.Response response) throws IOException {
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
