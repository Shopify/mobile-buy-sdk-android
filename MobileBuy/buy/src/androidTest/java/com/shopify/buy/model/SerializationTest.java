package com.shopify.buy.model;

import android.support.test.runner.AndroidJUnit4;

import com.google.gson.Gson;
import com.shopify.buy.dataprovider.BuyClient;
import com.shopify.buy.dataprovider.BuyClientFactory;
import com.shopify.buy.dataprovider.Callback;
import com.shopify.buy.dataprovider.RetrofitError;
import com.shopify.buy.extensions.ShopifyAndroidTestCase;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.CountDownLatch;

import retrofit2.Response;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.fail;

/**
 * Created by davepelletier on 15-08-11.
 */
@RunWith(AndroidJUnit4.class)
public class SerializationTest extends ShopifyAndroidTestCase {

    @Test
	public void testSerializeProductAndCart() throws InterruptedException {
        final CountDownLatch latch = new CountDownLatch(1);
        buyClient.getProduct(data.getProductId(), new Callback<Product>() {
            @Override
            public void success(Product product, Response response) {
                assertSerializable(product);

                Cart cart = new Cart();
                cart.addVariant(product.getVariants().get(0));
                assertSerializable(cart);

                latch.countDown();
            }

            @Override
            public void failure(RetrofitError error) {
                fail(BuyClient.getErrorBody(error));
            }
        });
        latch.await();
    }

    @Test
	public void testSerializeShop() throws InterruptedException {
        final CountDownLatch latch = new CountDownLatch(1);
        buyClient.getShop(new Callback<Shop>() {
            @Override
            public void success(Shop shop, Response response) {
                assertSerializable(shop);
                latch.countDown();
            }

            @Override
            public void failure(RetrofitError error) {
                fail();
            }
        });
        latch.await();
    }

    private void assertSerializable(Cart original) {
        try {
            Cart newCart = (Cart) serializeAndDeserialize(original);
            for (int i = 0; i < original.getLineItems().size(); i++) {
                LineItem originalLineItem = original.getLineItems().get(i);
                LineItem newLineItem = newCart.getLineItems().get(i);
                assertSerializationWorked(originalLineItem, newLineItem);

                ProductVariant originalProductVariant = original.getProductVariant(originalLineItem);
                ProductVariant newProductVariant = newCart.getProductVariant(newLineItem);
                assertSerializationWorked(originalProductVariant, newProductVariant);
            }
        } catch (Exception e) {
            e.printStackTrace();
            fail("Serialization test failed for Cart");
        }
    }

    private void assertSerializable(ShopifyObject obj) {
        try {
            assertSerializationWorked(obj, serializeAndDeserialize(obj));
        } catch (Exception e) {
            e.printStackTrace();
            fail("Serialization test failed for " + obj.getClass().getSimpleName());
        }
    }

    private Object serializeAndDeserialize(Object obj) {
        Gson gson = BuyClientFactory.createDefaultGson();
        String json = gson.toJson(obj);
        return gson.fromJson(json, obj.getClass());
    }

    private void assertSerializationWorked(Object obj1, Object obj2) throws InvocationTargetException, IllegalAccessException {
        Method[] methods = obj1.getClass().getMethods();
        for (Method method : methods) {
            if (method.getName().startsWith("get") && method.getParameterTypes().length == 0) {
                Object val1 = method.invoke(obj1);
                Object val2 = method.invoke(obj2);
                String failureMsg = String.format("%s objects did not match on %s", obj1.getClass().getSimpleName(), method.getName());
                assertEquals(failureMsg, val1, val2);
            }
        }
    }

}
