package com.shopify.buy.model;

import android.support.test.runner.AndroidJUnit4;

import com.shopify.buy.dataprovider.BuyClientError;
import com.shopify.buy.dataprovider.Callback;
import com.shopify.buy.extensions.ShopifyAndroidTestCase;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;
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

    @Test
    public void testGetProductTags() throws InterruptedException {
        final CountDownLatch latch = new CountDownLatch(1);
        buyClient.getProductTags(1, new Callback<List<String>>() {
            @Override
            public void success(List<String> response) {
                assertNotNull(response);
                assertEquals(true, response.contains("don't touch me"));
                latch.countDown();
                // well we can't really test response for some values, as we can't control from client list of tags
            }

            @Override
            public void failure(BuyClientError error) {
                fail(error.getRetrofitErrorBody());
            }
        });
        latch.await();
    }


    @Test
    public void testGetProductTagsByCollection() throws InterruptedException {
        final CountDownLatch latch = new CountDownLatch(1);
        buyClient.getProductTags(1, data.getCollectionId(), new Callback<List<String>>() {
            @Override
            public void success(List<String> response) {
                assertNotNull(response);
                assertEquals(true, response.contains("don't touch me"));
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
    public void testGetProductTagsByCollectionWithoutTag() throws InterruptedException {
        final CountDownLatch latch = new CountDownLatch(1);
        buyClient.getProductTags(1, data.getCollectionIdWithoutTags(), new Callback<List<String>>() {
            @Override
            public void success(List<String> response) {
                assertNotNull(response);
                assertEquals(false, response.contains("don't touch me"));
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
    public void testGetCollectionsByIds() throws Exception {
        final CountDownLatch getCollectionIdsLatch = new CountDownLatch(1);
        final AtomicReference<List<Long>> collectionIdsRef = new AtomicReference<>();
        buyClient.getCollections(1, new Callback<List<Collection>>() {
            @Override
            public void success(List<Collection> response) {
                assertNotNull(response);
                assertTrue(!response.isEmpty());

                final List<Long> collectionIds = new ArrayList<>();
                for (Collection collection : response) {
                    collectionIds.add(collection.getCollectionId());
                }
                collectionIdsRef.set(collectionIds);

                getCollectionIdsLatch.countDown();
            }

            @Override
            public void failure(BuyClientError error) {
                fail(error.getRetrofitErrorBody());
            }
        });
        getCollectionIdsLatch.await();

        final CountDownLatch getCollectionsLatch = new CountDownLatch(1);
        final List<Long> expectedCollectionIds = collectionIdsRef.get();
        buyClient.getCollections(1, expectedCollectionIds, new Callback<List<Collection>>() {
            @Override
            public void success(List<Collection> response) {
                assertNotNull(response);
                assertTrue(!response.isEmpty());

                for (Collection collection : response) {
                    assertTrue(expectedCollectionIds.contains(collection.getCollectionId()));
                }

                getCollectionsLatch.countDown();
            }

            @Override
            public void failure(BuyClientError error) {
                fail(error.getRetrofitErrorBody());
            }
        });
        getCollectionsLatch.await();
    }


    @Test
    public void testGetProductsByTags() throws InterruptedException {
        Set<String> tags = new HashSet<>();
        tags.add("MISSION");
        tags.add("IMPOSSIBLE");

        final CountDownLatch latch = new CountDownLatch(1);

        buyClient.getProducts(1, tags, new Callback<List<Product>>() {
            @Override
            public void success(List<Product> response) {
                assertNotNull(response);
                assertTrue(response.isEmpty());
                latch.countDown();
            }

            @Override
            public void failure(BuyClientError error) {
                fail(error.getRetrofitErrorBody());
            }
        });

        latch.await();

        tags = new HashSet<>();
        tags.add(data.getValidTag());

        final CountDownLatch latch1 = new CountDownLatch(1);

        buyClient.getProducts(1, tags, new Callback<List<Product>>() {
            @Override
            public void success(List<Product> response) {
                assertNotNull(response);
                assertTrue(!response.isEmpty());
                latch1.countDown();
            }

            @Override
            public void failure(BuyClientError error) {
                fail(error.getRetrofitErrorBody());
            }
        });

        latch1.await();
    }

    @Test
    public void testGetProductByCollectionAndTags() throws InterruptedException {
        final AtomicReference<Collection> collectionRef = new AtomicReference<>();

        final CountDownLatch getCollectionLatch = new CountDownLatch(1);
        buyClient.getCollections(1, new Callback<List<Collection>>() {
            @Override
            public void success(List<Collection> response) {
                assertNotNull(response);
                assertTrue(!response.isEmpty());
                for (Collection collection : response) {
                    if (data.getCollectionHandle().equals(collection.getHandle())) {
                        collectionRef.set(collection);
                    }
                }
                getCollectionLatch.countDown();
            }

            @Override
            public void failure(BuyClientError error) {
                fail(error.getRetrofitErrorBody());
            }
        });
        getCollectionLatch.await();

        final Set<String> tags = new HashSet<>();
        tags.add(data.getValidTag());

        final CountDownLatch getProductsLatch = new CountDownLatch(1);
        buyClient.getProducts(1, collectionRef.get().getCollectionId(), tags, null, new Callback<List<Product>>() {
            @Override
            public void success(List<Product> response) {
                assertNotNull(response);
                assertTrue(!response.isEmpty());
                getProductsLatch.countDown();
            }

            @Override
            public void failure(BuyClientError error) {
                fail(error.getRetrofitErrorBody());
            }
        });

        getProductsLatch.await();
    }

}
