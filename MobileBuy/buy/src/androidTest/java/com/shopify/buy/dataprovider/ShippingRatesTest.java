package com.shopify.buy.dataprovider;

import android.support.test.runner.AndroidJUnit4;

import com.shopify.buy.extensions.ShopifyAndroidTestCase;
import com.shopify.buy.model.Checkout;
import com.shopify.buy.model.ShippingRate;
import com.shopify.buy.model.internal.CheckoutWrapper;
import com.shopify.buy.model.internal.ShippingRatesWrapper;

import org.apache.http.HttpStatus;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.Protocol;
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
public class ShippingRatesTest extends ShopifyAndroidTestCase {

    Checkout checkout;
    CheckoutWrapper checkoutWrapper;

    CheckoutRetrofitService checkoutRetrofitService;

    ShippingRatesWrapper shippingRatesWrapper;

    // keep track of any tasks that we want to force a cancel on for testing
    CancellableTask taskToCancel;
    CountDownLatch taskCancelLatch;

    @Override
    public void setUp() throws Exception {
        super.setUp();

        checkout = Checkout.fromJson("{\"id\":100, \"channel\":\"mobile_app\",\"payment_due\":\"payment_due\",\"token\":\"checkout_token\"}");
        checkoutWrapper = new CheckoutWrapper(checkout);

        checkoutRetrofitService = Mockito.mock(CheckoutRetrofitService.class);

        final Field retrofitServiceField = CheckoutServiceDefault.class.getDeclaredField("retrofitService");
        retrofitServiceField.setAccessible(true);
        retrofitServiceField.set((((BuyClientDefault) buyClient).checkoutService), checkoutRetrofitService);

        shippingRatesWrapper = Mockito.mock(ShippingRatesWrapper.class);
        Mockito.when(shippingRatesWrapper.getContent()).thenReturn(new ArrayList<ShippingRate>());
    }

    @Test
    public void testFetchingShippingRatesWithNullCheckoutToken() throws InterruptedException {
        try {
            buyClient.getShippingRates(null, null);
        } catch (NullPointerException e) {
            return;
        }

        fail("Expected a NullPointerException");
    }


    @Test
    public void testFetchingShippingRatesWithIOError() throws InterruptedException, IOException {

        final int retryCount = 1;

        // Create an observable that will return one IOException, then a valid response
        final ExceptionOnSubscribe exceptionOnSubscribe = new ExceptionOnSubscribe(retryCount, new IOException());
        final Observable<Response<ShippingRatesWrapper>> response = Observable.create(exceptionOnSubscribe);

        Mockito.when(checkoutRetrofitService.getShippingRates(Mockito.anyString())).thenReturn(response);

        final CountDownLatch latch = new CountDownLatch(1);

        buyClient.getShippingRates(checkout.getToken(), new Callback<List<ShippingRate>>() {
            @Override
            public void success(List<ShippingRate> body) {
                assertEquals(true, body != null);
                assertEquals(retryCount, exceptionOnSubscribe.getCallCount());
                latch.countDown();
            }

            @Override
            public void failure(BuyClientError error) {
                fail("Expected success but received" + error.toString());
            }
        });

        latch.await();
    }


    @Test
    public void testFetchingShippingRatesWithMultipleIOError() throws InterruptedException {

        final int retryCount = 2;

        // Create an observable that will return one IOException, then a valid response
        final Observable<Response<ShippingRatesWrapper>> response = Observable.create(new ExceptionOnSubscribe(retryCount, new IOException()));

        Mockito.when(checkoutRetrofitService.getShippingRates(Mockito.anyString())).thenReturn(response);

        final CountDownLatch latch = new CountDownLatch(1);

        buyClient.getShippingRates(checkout.getToken(), new Callback<List<ShippingRate>>() {
            @Override
            public void success(List<ShippingRate> body) {
                fail("Expected an IOException");
            }

            @Override
            public void failure(BuyClientError error) {
                Throwable throwable = error.getCause();
                assertEquals(true, throwable instanceof IOException);
                latch.countDown();
            }
        });

        latch.await();
    }


    @Test
    public void testFetchingShippingRatesWithNonIOError() throws InterruptedException{
        final Observable<Response<ShippingRatesWrapper>> response = Observable.error(new BuyClientError(Response.error(HttpStatus.SC_PRECONDITION_FAILED, new ResponseBody() {
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
        })));

        Mockito.when(checkoutRetrofitService.getShippingRates(Mockito.anyString())).thenReturn(response);

        final CountDownLatch latch = new CountDownLatch(1);

        buyClient.getShippingRates(checkout.getToken(), new Callback<List<ShippingRate>>() {
            @Override
            public void success(List<ShippingRate> body) {
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
    public void testFetchingShippingRatesWithPolling() throws InterruptedException {
        final int retryCount = 5;

        final ResponseOnSubscribe responseOnSubscribe = new ResponseOnSubscribe(retryCount, HttpStatus.SC_ACCEPTED);
        final Observable<Response<ShippingRatesWrapper>> response = Observable.create(responseOnSubscribe);
        Mockito.when(checkoutRetrofitService.getShippingRates(Mockito.anyString())).thenReturn(response);

        final CountDownLatch latch = new CountDownLatch(1);

        buyClient.getShippingRates(checkout.getToken(), new Callback<List<ShippingRate>>() {
            @Override
            public void success(List<ShippingRate> body) {
                assertNotNull(body);
                assertEquals(retryCount, responseOnSubscribe.getCallCount());
                latch.countDown();
            }

            @Override
            public void failure(BuyClientError error) {
                fail("Expected success");
            }
        });

        latch.await();
    }

    @Test
    public void testFetchingShippingRatesWithPollingButCancelBeforeCompletion() throws InterruptedException {
        final int retryCount = 5;

        ResponseOnSubscribe responseOnSubscribe  = new ResponseOnSubscribe(retryCount, HttpStatus.SC_ACCEPTED);
        final Observable<Response<ShippingRatesWrapper>> response = Observable.create(responseOnSubscribe);
        Mockito.when(checkoutRetrofitService.getShippingRates(Mockito.anyString())).thenReturn(response);

        taskCancelLatch = new CountDownLatch(1);

        taskToCancel = buyClient.getShippingRates(checkout.getToken(), new Callback<List<ShippingRate>>() {
            @Override
            public void success(List<ShippingRate> body) {
                fail("Should not have hit success callback");
            }

            @Override
            public void failure(BuyClientError error) {
                fail("Should not have hit failure callback");
            }
        });

        taskCancelLatch.await();

        assertEquals(false, retryCount == responseOnSubscribe.getCallCount());

        // We cancelled the task after one iteration
        assertEquals(1, responseOnSubscribe.getCallCount());
    }

    private class ExceptionOnSubscribe implements Observable.OnSubscribe<Response<ShippingRatesWrapper>> {

        final private int retryCount;
        private int callCount;

        private Throwable throwable;

        public ExceptionOnSubscribe(int retryCount, Throwable throwable) {
            this.retryCount = retryCount;
            this.throwable = throwable;
        }

        @Override
        public void call(Subscriber<? super Response<ShippingRatesWrapper>> subscriber) {
            if (callCount < retryCount) {
                callCount++;
                subscriber.onError(throwable);
            } else {
                subscriber.onNext(Response.success(shippingRatesWrapper));
            }
        }

        public int getCallCount() {
            return callCount;
        }
    }

    private class ResponseOnSubscribe implements Observable.OnSubscribe<Response<ShippingRatesWrapper>> {

        final private int retryCount;
        private int callCount;

        private okhttp3.Response response;

        public ResponseOnSubscribe(int retryCount, int code) {
            this.retryCount = retryCount;

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
        public void call(Subscriber<? super Response<ShippingRatesWrapper>> subscriber) {
            if ( callCount < retryCount) {

                if (taskToCancel != null) {
                    taskToCancel.cancel();
                    taskCancelLatch.countDown();
                    return;
                }

                callCount++;

                Response<ShippingRatesWrapper> response = Response.success(null, this.response);
                subscriber.onNext(response);
            } else {
                subscriber.onNext(Response.success(shippingRatesWrapper));
            }
        }

        public int getCallCount() {
            return callCount;
        }
    }

}

