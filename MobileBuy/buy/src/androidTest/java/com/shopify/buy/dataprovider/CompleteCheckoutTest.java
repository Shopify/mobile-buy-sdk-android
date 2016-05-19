package com.shopify.buy.dataprovider;

import android.support.test.runner.AndroidJUnit4;

import com.shopify.buy.extensions.ShopifyAndroidTestCase;
import com.shopify.buy.model.Checkout;
import com.shopify.buy.model.PaymentToken;
import com.shopify.buy.model.internal.CheckoutWrapper;

import org.apache.http.HttpStatus;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import java.lang.reflect.Field;
import java.util.concurrent.CountDownLatch;

import okhttp3.HttpUrl;
import okhttp3.Protocol;
import retrofit2.Response;
import rx.Observable;
import rx.Subscriber;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.fail;

/**
 * The integration level test for the Mobile Buy SDK
 */
@RunWith(AndroidJUnit4.class)
public class CompleteCheckoutTest extends ShopifyAndroidTestCase {

    Checkout checkout;
    CheckoutWrapper checkoutWrapper;

    CheckoutRetrofitService checkoutRetrofitService;

    PaymentToken paymentToken;

    int callCount;

    @Override
    public void setUp() throws Exception {
        super.setUp();

        checkout = Checkout.fromJson("{\"id\":100, \"channel\":\"mobile_app\",\"payment_due\":\"payment_due\",\"token\":\"checkout_token\"}");
        checkoutWrapper = new CheckoutWrapper(checkout);

        checkoutRetrofitService = Mockito.mock(CheckoutRetrofitService.class);

        paymentToken = PaymentToken.createCreditCardPaymentToken("paymenttoken");

        final Field retrofitServiceField = CheckoutServiceDefault.class.getDeclaredField("retrofitService");
        retrofitServiceField.setAccessible(true);
        retrofitServiceField.set((((BuyClientDefault) buyClient).checkoutService), checkoutRetrofitService);
    }

    @Test
    public void testCompletingCheckoutWithNullCheckoutToken() throws InterruptedException {
        try {
            buyClient.completeCheckout(paymentToken, null);
        } catch (IllegalArgumentException e) {
            return;
        }

        fail("Expected an IllegalArgumentException");
    }

    @Test
    public void testCompletingCheckoutWithEmptyCheckoutToken() throws InterruptedException {
        try {
            buyClient.completeCheckout(paymentToken, "");
        } catch (IllegalArgumentException e) {
            return;
        }

        fail("Expected an IllegalArgumentException");
    }

    @Test
    public void testCompletingCheckoutWithNullPaymentToken() throws InterruptedException {
        try {
            buyClient.completeCheckout(null, checkout.getToken());
        } catch (NullPointerException e) {
            return;
        }

        fail("Expected an NullPointerException");
    }


    @Test
    public void testCompleteCheckoutWithNonIOError() throws InterruptedException{
        final Observable<Response<CheckoutWrapper>> response = Observable.error(new RetrofitError(HttpStatus.SC_PRECONDITION_FAILED, "precondition failed"));
        Mockito.when(checkoutRetrofitService.completeCheckout(Mockito.any(PaymentToken.class), Mockito.anyString())).thenReturn(response);

        final CountDownLatch latch = new CountDownLatch(1);

        buyClient.completeCheckout(paymentToken, checkout.getToken(), new Callback<Checkout>() {
            @Override
            public void success(Checkout checkout) {
                fail("Expected a 422 precondition failed");
            }

            @Override
            public void failure(RetrofitError error) {
                assertEquals(error.getCode(), HttpStatus.SC_PRECONDITION_FAILED);
                latch.countDown();
            }
        });

        latch.await();
    }


    @Test
    public void testCompleteCheckoutWithPolling() throws InterruptedException {
        final int retryCount = 5;

        final Observable<Response<CheckoutWrapper>> completeCheckoutResponse = Observable.just(Response.success(checkoutWrapper));
        Mockito.when(checkoutRetrofitService.completeCheckout(Mockito.any(PaymentToken.class), Mockito.anyString())).thenReturn(completeCheckoutResponse);

        final Observable<Response<Void>> getCheckoutCompletionStatusResponse = Observable.create(new ResponseOnSubscribe(retryCount, HttpStatus.SC_ACCEPTED));
        Mockito.when(checkoutRetrofitService.getCheckoutCompletionStatus(Mockito.anyString())).thenReturn(getCheckoutCompletionStatusResponse);

        final Observable<Response<CheckoutWrapper>> getCheckoutResponse = Observable.just(Response.success(checkoutWrapper));
        Mockito.when(checkoutRetrofitService.getCheckout(Mockito.anyString())).thenReturn(getCheckoutResponse);

        final CountDownLatch latch = new CountDownLatch(1);

        buyClient.completeCheckout(paymentToken, checkout.getToken(), new Callback<Checkout>() {
            @Override
            public void success(Checkout checkout) {
                assertNotNull(checkout);
                assertEquals(retryCount, callCount);
                latch.countDown();
            }

            @Override
            public void failure(RetrofitError error) {
                fail("Expected success");
            }
        });

        latch.await();
    }


    private class ResponseOnSubscribe implements Observable.OnSubscribe<Response<Void>> {

        private int count;

        private okhttp3.Response response;

        public ResponseOnSubscribe(int count, int code) {
            this.count = count;

            HttpUrl httpUrl = new HttpUrl.Builder()
                    .scheme("https")
                    .host("example.com")
                    .build();

            okhttp3.Request request = new okhttp3.Request.Builder()
                    .url(httpUrl)
                    .build();

            response = new okhttp3.Response.Builder()
                    .code(code)
                    .request(request)
                    .protocol(Protocol.HTTP_1_0)
                    .build();
        }

        @Override
        public void call(Subscriber<? super Response<Void>> subscriber) {
            if ( callCount < count) {
                callCount++;

                Response<Void> response = Response.success(null, this.response);
                subscriber.onNext(response);
            } else {
                subscriber.onNext(Response.success((Void)null));
            }
        }
    }

}

