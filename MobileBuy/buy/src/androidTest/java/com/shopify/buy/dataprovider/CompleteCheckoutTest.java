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

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import okio.BufferedSource;
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
        final Observable<Response<Void>> response = Observable.error(new BuyClientError(Response.error(new ResponseBody() {
            @Override
            public MediaType contentType() {
                return null;
            }

            @Override
            public long contentLength() {
                return 0;
            }

            @Override
            public BufferedSource source() {
                return null;
            }
        }, createResponse(HttpStatus.SC_PRECONDITION_FAILED))));

        Mockito.when(checkoutRetrofitService.completeCheckout(Mockito.any(PaymentToken.class), Mockito.anyString())).thenReturn(response);

        final CountDownLatch latch = new CountDownLatch(1);

        buyClient.completeCheckout(paymentToken, checkout.getToken(), new Callback<Checkout>() {
            @Override
            public void success(Checkout checkout) {
                fail("Expected a 422 precondition failed");
            }

            @Override
            public void failure(BuyClientError error) {
                assertEquals(HttpStatus.SC_PRECONDITION_FAILED, error.getRetrofitResponse().code());
                latch.countDown();
            }
        });

        latch.await();
    }


    @Test
    public void testCompleteCheckoutWithPolling() throws InterruptedException {
        final int retryCount = 5;

        final Observable<Response<Void>> completeCheckoutResponse = Observable.just(Response.success((Void)null));
        Mockito.when(checkoutRetrofitService.completeCheckout(Mockito.any(PaymentToken.class), Mockito.anyString())).thenReturn(completeCheckoutResponse);

        final ResponseOnSubscribe pollingResponseOnSubscribe = new ResponseOnSubscribe(retryCount, HttpStatus.SC_ACCEPTED);
        final Observable<Response<Void>> getCheckoutCompletionStatusResponse = Observable.create(pollingResponseOnSubscribe);
        Mockito.when(checkoutRetrofitService.getCheckoutCompletionStatus(Mockito.anyString())).thenReturn(getCheckoutCompletionStatusResponse);

        final Observable<Response<CheckoutWrapper>> getCheckoutResponse = Observable.just(Response.success(checkoutWrapper));
        Mockito.when(checkoutRetrofitService.getCheckout(Mockito.anyString())).thenReturn(getCheckoutResponse);

        final CountDownLatch latch = new CountDownLatch(1);

        buyClient.completeCheckout(paymentToken, checkout.getToken(), new Callback<Checkout>() {
            @Override
            public void success(Checkout checkout) {
                assertNotNull(checkout);
                assertEquals(retryCount, pollingResponseOnSubscribe.getCallCount());
                latch.countDown();
            }

            @Override
            public void failure(BuyClientError error) {
                fail("Expected success");
            }
        });

        latch.await();
    }


    private class ResponseOnSubscribe implements Observable.OnSubscribe<Response<Void>> {

        final private int retryCount;
        private int callCount;

        private okhttp3.Response response;

        public ResponseOnSubscribe(int retryCount, int code) {
            this.retryCount = retryCount;
            response = createResponse(code);
        }

        @Override
        public void call(Subscriber<? super Response<Void>> subscriber) {
            if (callCount < retryCount) {
                callCount++;

                Response<Void> response = Response.success(null, this.response);
                subscriber.onNext(response);
            } else {
                subscriber.onNext(Response.success((Void)null));
            }
        }

        public int getCallCount() {
            return callCount;
        }
    }

}

