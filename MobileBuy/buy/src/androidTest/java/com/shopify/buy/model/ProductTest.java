package com.shopify.buy.model;

import com.shopify.buy.dataprovider.BuyClient;
import com.shopify.buy.extensions.ShopifyAndroidTestCase;

import java.util.List;
import java.util.concurrent.CountDownLatch;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

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

}
