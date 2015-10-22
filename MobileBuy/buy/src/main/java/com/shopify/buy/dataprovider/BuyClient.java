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
import com.shopify.buy.model.Checkout;
import com.shopify.buy.model.Collection;
import com.shopify.buy.model.Collection.SortOrder;
import com.shopify.buy.model.CreditCard;
import com.shopify.buy.model.GiftCard;
import com.shopify.buy.model.Product;
import com.shopify.buy.model.ShippingRate;
import com.shopify.buy.model.Shop;
import com.shopify.buy.model.internal.CheckoutWrapper;
import com.shopify.buy.model.internal.CollectionPublication;
import com.shopify.buy.model.internal.GiftCardWrapper;
import com.shopify.buy.model.internal.MarketingAttribution;
import com.shopify.buy.model.internal.PaymentSessionCheckout;
import com.shopify.buy.model.internal.PaymentSessionCheckoutWrapper;
import com.shopify.buy.model.internal.ProductPublication;
import com.shopify.buy.model.internal.ShippingRatesWrapper;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import retrofit.Callback;
import retrofit.ResponseCallback;
import retrofit.RetrofitError;
import retrofit.client.Header;
import retrofit.client.Response;
import retrofit.mime.TypedByteArray;

/**
 * The {@code BuyClient} provides all requests needed to perform request on the Shopify Checkout API. Use this class to perform tasks such as getting a shop, getting collections and products for a shop, creating a {@link Checkout} on Shopify and completing Checkouts.
 * All API methods presented here run asynchronously and return results via callback on the Main thread.
 */
public class BuyClient {

    public static final int MAX_PAGE_SIZE = 250;
    public static final int MIN_PAGE_SIZE = 1;
    public static final int DEFAULT_PAGE_SIZE = 25;

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

    BuyClient(BuyRetrofitService retrofitService, String apiKey, String channelId, String applicationName, String shopDomain, OkHttpClient httpClient) {
        this.retrofitService = retrofitService;
        this.apiKey = apiKey;
        this.channelId = channelId;
        this.applicationName = applicationName;
        this.shopDomain = shopDomain;
        this.httpClient = httpClient;
    }

    /**
     * For internal use only.
     */
    public void addInterceptor(Interceptor interceptor) {
        this.httpClient.interceptors().add(interceptor);
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
        retrofitService.getCollections(channelId, new Callback<CollectionPublication>() {
            @Override
            public void success(CollectionPublication collectionPublication, Response response) {
                List<Collection> collections = null;

                if (collectionPublication != null) {
                    collections = collectionPublication.getCollections();
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
    public void updateCheckout(Checkout checkout, final Callback<Checkout> callback) {
        if (checkout == null) {
            throw new NullPointerException("checkout cannot be null");
        }

        retrofitService.updateCheckout(new CheckoutWrapper(checkout), checkout.getToken(), new Callback<CheckoutWrapper>() {
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
     *  Convenience method to release all product inventory reservations by setting the `reservationTime` of the checkout `0` and calling {@link #updateCheckout(Checkout, Callback) updateCheckout(Checkout, Callback)}.
     *  We recommend creating a new `Checkout` object from a `Cart` for further API calls.
     *
     *  @param checkout the {@link Checkout} to expire
     *  @param callback the {@link Callback} that will be used to indicate the response from the asynchronous network operation, not null
     */
    public void removeProductReservationsFromCheckout(final Checkout checkout, final Callback<Checkout> callback) {
        if (checkout == null || TextUtils.isEmpty(checkout.getToken())) {
            callback.failure(null);
        } else {
            checkout.setReservationTime(0);
            updateCheckout(checkout, callback);
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
     * @return the body of the response
     */
    public static String getErrorBody(RetrofitError error) {
        String json = null;
        try {
            json = new String(((TypedByteArray) error.getResponse().getBody()).getBytes());
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return json;
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
