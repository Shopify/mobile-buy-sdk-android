package com.shopify.buy.model;

import android.support.test.runner.AndroidJUnit4;

import com.shopify.buy.dataprovider.BuyClientError;
import com.shopify.buy.dataprovider.Callback;
import com.shopify.buy.extensions.ShopifyAndroidTestCase;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CountDownLatch;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.fail;

/**
 * Created by davepelletier on 15-08-25.
 */
@RunWith(AndroidJUnit4.class)
public class ProductTest extends ShopifyAndroidTestCase {

    @Test
    public void testGetVariantForOptionValues() throws InterruptedException {
        final CountDownLatch latch = new CountDownLatch(1);
        buyClient.getProduct(data.getProductIdWithVariants(), new Callback<Product>() {
            @Override
            public void success(Product product) {
                List<ProductVariant> variants = product.getVariants();
                ProductVariant variant = variants.get(variants.size() - 1);
                assertEquals(variant, product.getVariant(variant.getOptionValues()));

                latch.countDown();
            }

            @Override
            public void failure(BuyClientError error) {
                fail(error.getRetrofitErrorBody());
            }
        });
        latch.await();
    }

    @Test
    public void testGetValidTag() throws InterruptedException {
        final CountDownLatch latch = new CountDownLatch(1);
        buyClient.getProduct(data.getProductIdWithTags(), new Callback<Product>() {
            @Override
            public void success(Product product) {
                Set<String> tags = product.getTags();
                assertNotNull(tags);
                assertEquals(true, tags.size() > 0);
                assertEquals(true, tags.contains(data.getValidTag()));
                assertEquals(false, tags.contains(data.getInvalidTag()));

                latch.countDown();
            }

            @Override
            public void failure(BuyClientError error) {
                fail(error.getRetrofitErrorBody());
            }
        });
        latch.await();
    }

    @Test
    public void testGetInvalidTag() throws InterruptedException {
        final CountDownLatch latch = new CountDownLatch(1);
        buyClient.getProduct(data.getProductIdWithoutTags(), new Callback<Product>() {
            @Override
            public void success(Product product) {
                Set<String> tags = product.getTags();
                assertNotNull(tags);
                assertEquals(true, tags.size() == 0);
                assertEquals(false, tags.contains(data.getValidTag()));

                latch.countDown();
            }

            @Override
            public void failure(BuyClientError error) {
                fail(error.getRetrofitErrorBody());
            }
        });
        latch.await();
    }

}
