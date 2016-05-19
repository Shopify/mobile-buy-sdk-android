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
public class ShippingRatesTest extends ShopifyAndroidTestCase {

    Checkout checkout;
    CheckoutWrapper checkoutWrapper;

    CheckoutRetrofitService checkoutRetrofitService;

    int callCount;

    ShippingRatesWrapper shippingRatesWrapper;

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
        final CountDownLatch latch = new CountDownLatch(1);

        try {
            buyClient.getShippingRates(null, null);
        } catch (NullPointerException e) {
            latch.countDown();
            return;
        }

        latch.await();

        fail("Expected a NullPointerException");
    }


    @Test
    public void testFetchingShippingRatesWithIOError() throws InterruptedException, IOException {

        final int retryCount = 1;

        // Create an observable that will return one IOException, then a valid response
        final Observable<Response<ShippingRatesWrapper>> response = Observable.create(new ExceptionOnSubscribe(retryCount, new IOException()));

        Mockito.when(checkoutRetrofitService.getShippingRates(Mockito.anyString())).thenReturn(response);

        final CountDownLatch latch = new CountDownLatch(1);

        buyClient.getShippingRates(checkout.getToken(), new Callback<List<ShippingRate>>() {
            @Override
            public void success(List<ShippingRate> body) {
                assertNotNull(body);
                assertEquals(retryCount, callCount);
                latch.countDown();
            }

            @Override
            public void failure(RetrofitError error) {
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
            public void failure(RetrofitError error) {
                Throwable throwable = error.getException().getCause();
                assertEquals(true, throwable instanceof IOException);
                latch.countDown();
            }
        });

        latch.await();
    }


    @Test
    public void testFetchingShippingRatesWithNonIOError() throws InterruptedException{
        final Observable<Response<ShippingRatesWrapper>> response = Observable.error(new RetrofitError(HttpStatus.SC_PRECONDITION_FAILED, "precondition failed"));
        Mockito.when(checkoutRetrofitService.getShippingRates(Mockito.anyString())).thenReturn(response);

        final CountDownLatch latch = new CountDownLatch(1);

        buyClient.getShippingRates(checkout.getToken(), new Callback<List<ShippingRate>>() {
            @Override
            public void success(List<ShippingRate> body) {
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
    public void testFetchingShippingRatesWithPolling() throws InterruptedException {
        final int retryCount = 5;

        final Observable<Response<ShippingRatesWrapper>> response = Observable.create(new ResponseOnSubscribe(retryCount, HttpStatus.SC_ACCEPTED));
        Mockito.when(checkoutRetrofitService.getShippingRates(Mockito.anyString())).thenReturn(response);

        final CountDownLatch latch = new CountDownLatch(1);

        buyClient.getShippingRates(checkout.getToken(), new Callback<List<ShippingRate>>() {
            @Override
            public void success(List<ShippingRate> body) {
                assertNotNull(body);
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

    private class ExceptionOnSubscribe implements Observable.OnSubscribe<Response<ShippingRatesWrapper>> {

        private int count;

        private Throwable throwable;

        public ExceptionOnSubscribe(int count, Throwable throwable) {
            this.count = count;
            this.throwable = throwable;
        }

        @Override
        public void call(Subscriber<? super Response<ShippingRatesWrapper>> subscriber) {
            if (callCount < count) {
                callCount++;
                subscriber.onError(throwable);
            } else {
                subscriber.onNext(Response.success(shippingRatesWrapper));
            }
        }
    }

    private class ResponseOnSubscribe implements Observable.OnSubscribe<Response<ShippingRatesWrapper>> {

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
        public void call(Subscriber<? super Response<ShippingRatesWrapper>> subscriber) {
            if ( callCount < count) {
                callCount++;

                Response<ShippingRatesWrapper> response = Response.success(null, this.response);
                subscriber.onNext(response);
            } else {
                subscriber.onNext(Response.success(shippingRatesWrapper));
            }
        }
    }

}

