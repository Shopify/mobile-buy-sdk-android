package com.shopify.buy.model;

import com.shopify.buy.dataprovider.BuyClient;
import com.shopify.buy.dataprovider.Callback;
import com.shopify.buy.dataprovider.RetrofitError;
import com.shopify.buy.extensions.ShopifyAndroidTestCase;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CountDownLatch;

import retrofit2.Response;

/**
 * Created by davepelletier on 15-08-25.
 */
public class ProductTest extends ShopifyAndroidTestCase {

    public void testGetVariantForOptionValues() throws InterruptedException {
        final CountDownLatch latch = new CountDownLatch(1);
        buyClient.getProduct(data.getProductIdWithVariants(), new Callback<Product>() {
            @Override
            public void success(Product product, Response response) {
                List<ProductVariant> variants = product.getVariants();
                ProductVariant variant = variants.get(variants.size() - 1);
                assertEquals(variant, product.getVariant(variant.getOptionValues()));

                latch.countDown();
            }

            @Override
            public void failure(RetrofitError error) {
                fail(BuyClient.getErrorBody(error));
            }
        });
        latch.await();
    }

    public void testGetValidTag() throws InterruptedException {
        final CountDownLatch latch = new CountDownLatch(1);
        buyClient.getProduct(data.getProductIdWithTags(), new Callback<Product>() {
            @Override
            public void success(Product product, Response response) {
                Set<String> tags = product.getTags();
                assertNotNull(tags);
                assertEquals(true, tags.size() > 0);
                assertEquals(true, tags.contains(data.getValidTag()));
                assertEquals(false, tags.contains(data.getInvalidTag()));

                latch.countDown();
            }

            @Override
            public void failure(RetrofitError error) {
                fail(BuyClient.getErrorBody(error));
            }
        });
        latch.await();
    }

    public void testGetInvalidTag() throws InterruptedException {
        final CountDownLatch latch = new CountDownLatch(1);
        buyClient.getProduct(data.getProductIdWithoutTags(), new Callback<Product>() {
            @Override
            public void success(Product product, Response response) {
                Set<String> tags = product.getTags();
                assertNotNull(tags);
                assertEquals(true, tags.size() == 0);
                assertEquals(false, tags.contains(data.getValidTag()));

                latch.countDown();
            }

            @Override
            public void failure(RetrofitError error) {
                fail(BuyClient.getErrorBody(error));
            }
        });
        latch.await();
    }

}
