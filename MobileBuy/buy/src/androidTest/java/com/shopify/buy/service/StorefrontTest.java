package com.shopify.buy.service;

import android.support.test.runner.AndroidJUnit4;

import com.shopify.buy.dataprovider.Callback;
import com.shopify.buy.dataprovider.RetrofitError;
import com.shopify.buy.extensions.ShopifyAndroidTestCase;
import com.shopify.buy.model.Collection;
import com.shopify.buy.model.Product;
import com.shopify.buy.model.Shop;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import retrofit2.Response;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.fail;

/**
 * Unit test for retrieving store and product information
 */
@RunWith(AndroidJUnit4.class)
public class StorefrontTest extends ShopifyAndroidTestCase {

    @Test
    public void testGetProductPage() throws InterruptedException {
        final CountDownLatch latch = new CountDownLatch(1);
        buyClient.getProductPage(1, new Callback<List<Product>>() {
            @Override
            public void success(List<Product> products, Response response) {
                assertFalse(products.isEmpty());
                latch.countDown();
            }

            @Override
            public void failure(RetrofitError error) {
                fail();
            }
        });
        latch.await();
    }

    @Test
    public void testGetShop() throws InterruptedException {
        final CountDownLatch latch = new CountDownLatch(1);
        buyClient.getShop(new Callback<Shop>() {
            @Override
            public void success(Shop shop, Response response) {
                assertNotNull(shop);
                latch.countDown();
            }

            @Override
            public void failure(RetrofitError error) {
                fail();
            }
        });
        latch.await();
    }

    @Test
    public void testGetProduct() throws InterruptedException {
        final String productId = data.getProductId();
        final CountDownLatch latch = new CountDownLatch(1);
        buyClient.getProduct(productId, new Callback<Product>() {
            @Override
            public void success(Product product, Response response) {
                assertNotNull(product);
                assertEquals(product.getProductId(), productId);
                latch.countDown();
            }

            @Override
            public void failure(RetrofitError error) {
                fail();
            }
        });
        latch.await();
    }

    @Test
    public void testGetProducts() throws InterruptedException {
        final CountDownLatch latch = new CountDownLatch(1);
        final List<String> productIds = data.getProductIds();

        buyClient.getProducts(productIds, new Callback<List<Product>>() {
            @Override
            public void success(List<Product> products, Response response) {
                assertNotNull(products);
                assertEquals(products.size(), productIds.size());
                latch.countDown();
            }

            @Override
            public void failure(RetrofitError error) {
                fail();
            }
        });
        latch.await();
    }

    @Test
    public void testGetNonexistentProduct() throws InterruptedException {
        final CountDownLatch latch = new CountDownLatch(1);
        buyClient.getProduct("1337", new Callback<Product>() {
            @Override
            public void success(Product product, Response response) {
                assertNull(product);
                latch.countDown();
            }

            @Override
            public void failure(RetrofitError error) {
                fail();
            }
        });
        latch.await();
    }

    @Test
    public void testGetOutOfIndexProductPage() throws InterruptedException {
        final CountDownLatch latch = new CountDownLatch(1);
        buyClient.getProductPage(999, new Callback<List<Product>>() {
            @Override
            public void success(List<Product> products, Response response) {
                assertEquals(products.size(), 0);
                latch.countDown();
            }

            @Override
            public void failure(RetrofitError error) {
                fail();
            }
        });
        latch.await();
    }

    @Test
    public void testGetProductsWithOneInvalidId() throws InterruptedException {
        final CountDownLatch latch = new CountDownLatch(1);
        final List<String> productIds = new ArrayList<>();

        productIds.add("1337");
        productIds.add(data.getProductId());

        buyClient.getProducts(productIds, new Callback<List<Product>>() {
            @Override
            public void success(List<Product> products, Response response) {
                assertNotNull(products);
                assertEquals(products.size(), productIds.size() - 1);
                latch.countDown();
            }

            @Override
            public void failure(RetrofitError error) {
                fail();
            }
        });
        latch.await();
    }

    @Test
    public void testGetCollection() throws InterruptedException {
        final CountDownLatch latch = new CountDownLatch(1);

        buyClient.getCollections(new Callback<List<Collection>>() {
            @Override
            public void success(List<Collection> collections, Response response) {
                assertNotNull(collections);
                assertFalse(collections.isEmpty());
                latch.countDown();
            }

            @Override
            public void failure(RetrofitError error) {
                fail();
            }
        });
        latch.await();
    }

    @Test
    public void testGetProductsInCollection() throws InterruptedException {
        final CountDownLatch latch = new CountDownLatch(1);

        buyClient.getProducts(1, data.getCollectionId(), Collection.SortOrder.COLLECTION_DEFAULT, new Callback<List<Product>>() {
            @Override
            public void success(List<Product> products, Response response) {
                assertNotNull(products);
                assertFalse(products.isEmpty());
                latch.countDown();
            }

            @Override
            public void failure(RetrofitError error) {
                fail();
            }
        });
        latch.await();
    }

    @Test
    public void testGetCollectionPage() throws InterruptedException {
        final CountDownLatch latch = new CountDownLatch(1);

        buyClient.getCollectionPage(1, new Callback<List<Collection>>() {
            @Override
            public void success(List<Collection> collections, Response response) {
                assertNotNull(collections);
                assertFalse(collections.isEmpty());
                assertEquals(collections.get(0).getHandle(), "frontpage");
                latch.countDown();
            }

            @Override
            public void failure(RetrofitError error) {
                fail();
            }
        });
        latch.await();
    }

    @Test
    public void testGetOutOfIndexCollectionPage() throws InterruptedException {
        final CountDownLatch latch = new CountDownLatch(1);

        buyClient.getCollectionPage(999, new Callback<List<Collection>>() {
            @Override
            public void success(List<Collection> collections, Response response) {
                assertNotNull(collections);
                assertEquals(collections.size(), 0);
                latch.countDown();
            }

            @Override
            public void failure(RetrofitError error) {
                fail();
            }
        });
        latch.await();
    }
}
