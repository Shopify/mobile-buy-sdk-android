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
import android.util.Base64;
import android.util.Log;

import com.shopify.buy.model.Checkout;
import com.shopify.buy.model.CreditCard;
import com.shopify.buy.model.GiftCard;
import com.shopify.buy.model.Payment;
import com.shopify.buy.model.PaymentSession;
import com.shopify.buy.model.ShippingRate;
import com.shopify.buy.model.internal.CheckoutWrapper;
import com.shopify.buy.model.internal.CreditCardWrapper;
import com.shopify.buy.model.internal.GiftCardWrapper;
import com.shopify.buy.model.internal.MarketingAttribution;
import com.shopify.buy.model.internal.PaymentRequest;
import com.shopify.buy.model.internal.PaymentRequestWrapper;
import com.shopify.buy.model.internal.PaymentToken;
import com.shopify.buy.model.internal.PaymentWrapper;
import com.shopify.buy.model.internal.ShippingRatesWrapper;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import retrofit2.Retrofit;
import rx.Observable;
import rx.Scheduler;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Default implementation of {@link CheckoutService}
 */
final class CheckoutServiceDefault implements CheckoutService {

    private static String LOG_TAG = CheckoutServiceDefault.class.getSimpleName();

    private static final String PAYMENT_TOKEN_TYPE_ANDROID_PAY = "android_pay";

    final CheckoutRetrofitService retrofitService;

    final String apiKey;

    final String applicationName;

    final String webReturnToUrl;

    final String webReturnToLabel;

    final NetworkRetryPolicyProvider networkRetryPolicyProvider;

    final Scheduler callbackScheduler;

    private String androidPayPublicKey;
    private String androidPayPublicKeyHash;

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

        return retrofitService
                .getShippingRates(checkoutToken)
                .retryWhen(networkRetryPolicyProvider.provide())
                .doOnNext(new RetrofitSuccessHttpStatusCodeHandler<>())
                .compose(new UnwrapRetrofitBodyTransformer<ShippingRatesWrapper, List<ShippingRate>>())
                .observeOn(callbackScheduler);
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
        final CreditCardWrapper creditCardWrapper = new CreditCardWrapper(card);
        return retrofitService
                .storeCreditCard(safeCheckout.getPaymentUrl(), creditCardWrapper, BuyClientUtils.formatBasicAuthorization(apiKey))
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
    public void completeCheckout(final Checkout checkout, final Callback<Payment> callback) {
        completeCheckout(checkout).subscribe(new InternalCallbackSubscriber<>(callback));
    }

    @Override
    public Observable<Payment> completeCheckout(final Checkout checkout) {
        if (checkout == null) {
            throw new NullPointerException("checkout cannot be null");
        }

        final PaymentRequest paymentRequest = new PaymentRequest(checkout.getPaymentSessionId());
        final PaymentRequestWrapper paymentRequestWrapper = new PaymentRequestWrapper(paymentRequest);
        return retrofitService
                .completeCheckout(paymentRequestWrapper, checkout.getToken())
                .doOnNext(new RetrofitSuccessHttpStatusCodeHandler<>())
                .compose(new UnwrapRetrofitBodyTransformer<PaymentWrapper, Payment>())
                .observeOn(callbackScheduler);
    }

    @Override
    public void completeCheckout(final String androidPayToken, final Checkout checkout, final Callback<Payment> callback) {
        completeCheckout(androidPayToken, checkout).subscribe(new InternalCallbackSubscriber<>(callback));
    }

    @Override
    public Observable<Payment> completeCheckout(final String androidPayToken, final Checkout checkout) {
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
        return retrofitService
                .completeCheckout(paymentToken, checkout.getToken())
                .doOnNext(new RetrofitSuccessHttpStatusCodeHandler<>())
                .compose(new UnwrapRetrofitBodyTransformer<PaymentWrapper, Payment>())
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

    /**
     * Enables Android Pay support in the {@link BuyClient}
     *
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
            Log.e(LOG_TAG, "Could not enable Android Pay: " + e.getMessage());
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
}
