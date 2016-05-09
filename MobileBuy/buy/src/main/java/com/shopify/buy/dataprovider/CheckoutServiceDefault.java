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

import android.text.TextUtils;
import android.util.Log;

import com.shopify.buy.model.Checkout;
import com.shopify.buy.model.CreditCard;
import com.shopify.buy.model.GiftCard;
import com.shopify.buy.model.PaymentSession;
import com.shopify.buy.model.ShippingRate;
import com.shopify.buy.model.internal.CheckoutWrapper;
import com.shopify.buy.model.internal.GiftCardWrapper;
import com.shopify.buy.model.internal.MarketingAttribution;
import com.shopify.buy.model.internal.PaymentSessionCheckout;
import com.shopify.buy.model.internal.PaymentSessionCheckoutWrapper;
import com.shopify.buy.model.internal.ShippingRatesWrapper;

import java.util.HashMap;
import java.util.List;

import retrofit2.Response;
import retrofit2.Retrofit;
import rx.Observable;
import rx.Scheduler;
import rx.functions.Action1;
import rx.functions.Func1;

import static java.net.HttpURLConnection.HTTP_OK;

/**
 * Default implementation of {@link CheckoutService}
 */
final class CheckoutServiceDefault implements CheckoutService {

    public static final long POLLING_INTERVAL = 500;

    final CheckoutRetrofitService retrofitService;

    final String apiKey;

    final String applicationName;

    final String webReturnToUrl;

    final String webReturnToLabel;

    final NetworkRetryPolicyProvider networkRetryPolicyProvider;

    final PollingPolicyProvider pollingRetryPolicyProvider;

    final Scheduler callbackScheduler;

    CheckoutServiceDefault(
            final Retrofit retrofit,
            final String apiKey,
            final String applicationName,
            final String webReturnToUrl,
            final String webReturnToLabel,
            final NetworkRetryPolicyProvider networkRetryPolicyProvider,
            final Scheduler callbackScheduler
    ) {
        this.retrofitService = retrofit.create(CheckoutRetrofitService.class);
        this.apiKey = apiKey;
        this.applicationName = applicationName;
        this.webReturnToUrl = webReturnToUrl;
        this.webReturnToLabel = webReturnToLabel;
        this.networkRetryPolicyProvider = networkRetryPolicyProvider;
        this.callbackScheduler = callbackScheduler;

        pollingRetryPolicyProvider = new PollingPolicyProvider(POLLING_INTERVAL);
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
    public void createCheckout(final Checkout checkout, final Callback<Checkout> callback) {
        createCheckout(checkout).subscribe(new InternalCallbackSubscriber<>(callback));
    }

    @Override
    public Observable<Checkout> createCheckout(final Checkout checkout) {
        if (checkout == null) {
            throw new NullPointerException("checkout cannot be null");
        }

        final Checkout safeCheckout = checkout.copy();
        safeCheckout.setMarketingAttribution(new MarketingAttribution(applicationName));
        safeCheckout.setSourceName("mobile_app");
        if (webReturnToUrl != null) {
            safeCheckout.setWebReturnToUrl(webReturnToUrl);
        }
        if (webReturnToLabel != null) {
            safeCheckout.setWebReturnToLabel(webReturnToLabel);
        }

        return retrofitService
                .createCheckout(new CheckoutWrapper(safeCheckout))
                .doOnNext(new RetrofitSuccessHttpStatusCodeHandler<>())
                .compose(new UnwrapRetrofitBodyTransformer<CheckoutWrapper, Checkout>())
                .observeOn(callbackScheduler);
    }

    @Override
    public void updateCheckout(final Checkout checkout, final Callback<Checkout> callback) {
        updateCheckout(checkout).subscribe(new InternalCallbackSubscriber<>(callback));
    }

    @Override
    public Observable<Checkout> updateCheckout(final Checkout checkout) {
        if (checkout == null) {
            throw new NullPointerException("checkout cannot be null");
        }

        final Checkout safeCheckout = checkout.copyForUpdate();
        return retrofitService
                .updateCheckout(new CheckoutWrapper(safeCheckout), safeCheckout.getToken())
                .doOnNext(new RetrofitSuccessHttpStatusCodeHandler<>())
                .compose(new UnwrapRetrofitBodyTransformer<CheckoutWrapper, Checkout>())
                .observeOn(callbackScheduler);
    }

    @Override
    public void getShippingRates(final String checkoutToken, final Callback<List<ShippingRate>> callback) {
        getShippingRates(checkoutToken).subscribe(new InternalCallbackSubscriber<>(callback));
    }

    @Override
    public Observable<List<ShippingRate>> getShippingRates(final String checkoutToken) {
        if (checkoutToken == null) {
            throw new NullPointerException("checkoutToken cannot be null");
        }

        int[] successCodes = {HTTP_OK};

        return retrofitService
                .getShippingRates(checkoutToken)
                .retryWhen(networkRetryPolicyProvider.provide())
                .doOnNext(new RetrofitSuccessHttpStatusCodeHandler<>(successCodes))
                .retryWhen(pollingRetryPolicyProvider.provide())
                .compose(new UnwrapRetrofitBodyTransformer<ShippingRatesWrapper, List<ShippingRate>>())
                .observeOn(callbackScheduler)
                .doOnError(new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        System.out.println(throwable);
                    }
                });
    }

    @Override
    public void storeCreditCard(final CreditCard card, final Checkout checkout, final Callback<Checkout> callback) {
        storeCreditCard(card, checkout).subscribe(new InternalCallbackSubscriber<>(callback));
    }

    @Override
    public Observable<Checkout> storeCreditCard(final CreditCard card, final Checkout checkout) {
        if (card == null) {
            throw new NullPointerException("card cannot be null");
        }

        if (checkout == null) {
            throw new NullPointerException("checkout cannot be null");
        }


        final Checkout safeCheckout = checkout.copy();

        PaymentSessionCheckout paymentSessionCheckout = new PaymentSessionCheckout(checkout.getToken(), card, checkout.getBillingAddress());

        return retrofitService
                .storeCreditCard(safeCheckout.getPaymentUrl(), new PaymentSessionCheckoutWrapper(paymentSessionCheckout), BuyClientUtils.formatBasicAuthorization(apiKey))
                .doOnNext(new RetrofitSuccessHttpStatusCodeHandler<>())
                .compose(new UnwrapRetrofitBodyTransformer<PaymentSession, String>())
                .doOnNext(new Action1<String>() {
                    @Override
                    public void call(String paymentSessionId) {
                        safeCheckout.setPaymentSessionId(paymentSessionId);
                    }
                })
                .map(new Func1<String, Checkout>() {
                    @Override
                    public Checkout call(String s) {
                        return safeCheckout;
                    }
                })
                .observeOn(callbackScheduler);
    }

    @Override
    public void completeCheckout(final Checkout checkout, final Callback<Checkout> callback) {
        completeCheckout(checkout).subscribe(new InternalCallbackSubscriber<>(callback));
    }

    @Override
    public Observable<Checkout> completeCheckout(final Checkout checkout) {
        if (checkout == null) {
            throw new NullPointerException("checkout cannot be null");
        }

        HashMap<String, String> requestBodyMap = new HashMap<>();

        String paymentSessionId = checkout.getPaymentSessionId();
        if (!TextUtils.isEmpty(paymentSessionId)) {
            requestBodyMap.put("payment_session_id", checkout.getPaymentSessionId());
        }

        return retrofitService
                .completeCheckout(requestBodyMap, checkout.getToken())
                .doOnNext(new RetrofitSuccessHttpStatusCodeHandler<Response<CheckoutWrapper>>())
                .compose(new UnwrapRetrofitBodyTransformer<CheckoutWrapper, Checkout>())
                .flatMap(new Func1<Checkout, Observable<Checkout>>() {
                    @Override
                    public Observable<Checkout> call(final Checkout checkout) {

                        return getCheckoutCompletionStatus(checkout)
                                .flatMap(new Func1<Boolean, Observable<Checkout>>() {
                                    @Override
                                    public Observable<Checkout> call(Boolean aBoolean) {
                                        Log.e("FOO", "checking for completion status");
                                        if (aBoolean) {
                                            return getCheckout(checkout.getToken());
                                        }

                                        // Poll while aBoolean == false
                                        return Observable.error(new PollingRequiredException());
                                    }
                                })
                                .retryWhen(pollingRetryPolicyProvider.provide());
                    }
                })
                .observeOn(callbackScheduler);
    }


    @Override
    public void getCheckoutCompletionStatus(Checkout checkout, final Callback<Boolean> callback) {
        getCheckoutCompletionStatus(checkout).subscribe(new InternalCallbackSubscriber<>(callback));
    }

    @Override
    public Observable<Boolean> getCheckoutCompletionStatus(final Checkout checkout) {
        if (checkout == null) {
            throw new NullPointerException("checkout cannot be null");
        }

        return retrofitService
                .getCheckoutCompletionStatus(checkout.getToken())
                .retryWhen(networkRetryPolicyProvider.provide())
                .doOnNext(new RetrofitSuccessHttpStatusCodeHandler<>())
                .map(new Func1<Response<Void>, Boolean>() {
                         @Override
                         public Boolean call(Response<Void> voidResponse) {
                             return HTTP_OK == voidResponse.code();
                         }
                     }
                )
                .observeOn(callbackScheduler);
    }


    @Override
    public void getCheckout(final String checkoutToken, final Callback<Checkout> callback) {
        getCheckout(checkoutToken).subscribe(new InternalCallbackSubscriber<>(callback));
    }

    @Override
    public Observable<Checkout> getCheckout(final String checkoutToken) {
        if (checkoutToken == null) {
            throw new NullPointerException("checkoutToken cannot be null");
        }

        return retrofitService
                .getCheckout(checkoutToken)
                .retryWhen(networkRetryPolicyProvider.provide())
                .doOnNext(new RetrofitSuccessHttpStatusCodeHandler<>())
                .compose(new UnwrapRetrofitBodyTransformer<CheckoutWrapper, Checkout>())
                .observeOn(callbackScheduler);
    }

    @Override
    public void applyGiftCard(final String giftCardCode, final Checkout checkout, final Callback<Checkout> callback) {
        applyGiftCard(giftCardCode, checkout).subscribe(new InternalCallbackSubscriber<>(callback));
    }

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
                .compose(new UnwrapRetrofitBodyTransformer<GiftCardWrapper, GiftCard>())
                .map(new Func1<GiftCard, Checkout>() {
                    @Override
                    public Checkout call(GiftCard giftCard) {
                        if (giftCard != null) {
                            cleanCheckout.addGiftCard(giftCard);
                            cleanCheckout.setPaymentDue(giftCard.getCheckout().getPaymentDue());
                        }
                        return cleanCheckout;
                    }
                })
                .observeOn(callbackScheduler);
    }

    @Override
    public void removeGiftCard(final GiftCard giftCard, final Checkout checkout, final Callback<Checkout> callback) {
        removeGiftCard(giftCard, checkout).subscribe(new InternalCallbackSubscriber<>(callback));
    }

    @Override
    public Observable<Checkout> removeGiftCard(final GiftCard giftCard, final Checkout checkout) {
        if (checkout == null) {
            throw new NullPointerException("checkout cannot be null");
        }

        if (giftCard == null) {
            throw new NullPointerException("giftCard cannot be null");
        }

        final Checkout safeCheckout = checkout.copy();
        return retrofitService
                .removeGiftCard(giftCard.getId(), safeCheckout.getToken())
                .doOnNext(new RetrofitSuccessHttpStatusCodeHandler<>())
                .compose(new UnwrapRetrofitBodyTransformer<GiftCardWrapper, GiftCard>())
                .map(new Func1<GiftCard, Checkout>() {
                    @Override
                    public Checkout call(GiftCard giftCard) {
                        if (giftCard != null) {
                            safeCheckout.removeGiftCard(giftCard);
                            safeCheckout.setPaymentDue(giftCard.getCheckout().getPaymentDue());
                        }
                        return safeCheckout;
                    }
                })
                .observeOn(callbackScheduler);
    }

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
}
