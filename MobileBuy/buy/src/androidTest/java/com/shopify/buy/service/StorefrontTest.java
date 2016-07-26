package com.shopify.buy.service;

import android.support.test.runner.AndroidJUnit4;

import com.shopify.buy.dataprovider.Callback;
import com.shopify.buy.dataprovider.BuyClientError;
import com.shopify.buy.extensions.ShopifyAndroidTestCase;
import com.shopify.buy.model.Collection;
import com.shopify.buy.model.Product;
import com.shopify.buy.model.Shop;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;

/**
 * Unit test for retrieving store and product information
 */
@RunWith(AndroidJUnit4.class)
public class StorefrontTest extends ShopifyAndroidTestCase {

    @Test
    public void testGetProductPage() throws InterruptedException {
        final CountDownLatch latch = new CountDownLatch(1);

        buyClient.getProducts(1, new Callback<List<Product>>() {
            @Override
            public void success(List<Product> products) {
                assertFalse(products.isEmpty());
                latch.countDown();
            }

            @Override
            public void failure(BuyClientError error) {
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
            public void success(Shop shop) {
                assertNotNull(shop);
                latch.countDown();
            }

            @Override
            public void failure(BuyClientError error) {
                fail();
            }
        });

        latch.await();
    }

    @Test
    public void testGetProduct() throws InterruptedException {
        final CountDownLatch latch = new CountDownLatch(1);

        final Long productId = data.getProductId();
        buyClient.getProduct(productId, new Callback<Product>() {
            @Override
            public void success(Product product) {
                assertNotNull(product);
                assertEquals(product.getProductId(), productId);
                latch.countDown();
            }

            @Override
            public void failure(BuyClientError error) {
                fail();
            }
        });

        latch.await();
    }

    @Test
    public void testGetProducts() throws InterruptedException {
        final List<Long> productIds = data.getProductIds();

        final CountDownLatch latch = new CountDownLatch(1);

        buyClient.getProducts(productIds, new Callback<List<Product>>() {
            @Override
            public void success(List<Product> products) {
                assertNotNull(products);
                assertEquals(products.size(), productIds.size());
                latch.countDown();
            }

            @Override
            public void failure(BuyClientError error) {
                fail();
            }
        });

        latch.await();
    }

    @Test
    public void testGetNonexistentProduct() throws InterruptedException {
        final CountDownLatch latch = new CountDownLatch(1);

        buyClient.getProduct(1337l, new Callback<Product>() {
            @Override
            public void success(Product product) {
                assertNull(product);
                latch.countDown();
            }

            @Override
            public void failure(BuyClientError error) {
                fail();
            }
        });

        latch.await();
    }

    @Test
    public void testGetOutOfIndexProductPage() throws InterruptedException {
        final CountDownLatch latch = new CountDownLatch(1);

        buyClient.getProducts(999, new Callback<List<Product>>() {
            @Override
            public void success(List<Product> products) {
                assertEquals(products.size(), 0);
                latch.countDown();
            }

            @Override
            public void failure(BuyClientError error) {
                fail();
            }
        });

        latch.await();
    }

    @Test
    public void testGetProductsWithOneInvalidId() throws InterruptedException {
        final CountDownLatch latch = new CountDownLatch(1);

        final List<Long> productIds = new ArrayList<>();

        productIds.add(1337l);
        productIds.add(data.getProductId());

        buyClient.getProducts(productIds, new Callback<List<Product>>() {
            @Override
            public void success(List<Product> products) {
                assertNotNull(products);
                assertEquals(products.size(), productIds.size() - 1);
                latch.countDown();
            }

            @Override
            public void failure(BuyClientError error) {
                fail();
            }
        });

        latch.await();
    }

    @Test
    public void testGetProductsInCollection() throws InterruptedException {
        final CountDownLatch latch = new CountDownLatch(1);

        buyClient.getProducts(1, data.getCollectionId(), null, Collection.SortOrder.COLLECTION_DEFAULT, new Callback<List<Product>>() {
            @Override
            public void success(List<Product> products) {
                assertNotNull(products);
                assertFalse(products.isEmpty());
                latch.countDown();
            }

            @Override
            public void failure(BuyClientError error) {
                fail();
            }
        });

        latch.await();
    }

    @Test
    public void testGetCollectionPage() throws InterruptedException {
        final CountDownLatch latch = new CountDownLatch(1);

        buyClient.getCollections(1, new Callback<List<Collection>>() {
            @Override
            public void success(List<Collection> collections) {
                assertNotNull(collections);
                assertFalse(collections.isEmpty());


                boolean found = false;
                for (Collection collection : collections) {
                    if ("frontpage".equals(collection.getHandle())) {
                        found = true;
                        break;
                    }
                }
                assertTrue(found);
                latch.countDown();
            }

            @Override
            public void failure(BuyClientError error) {
                fail();
            }
        });

        latch.await();
    }

    @Test
    public void testGetOutOfIndexCollectionPage() throws InterruptedException {
        final CountDownLatch latch = new CountDownLatch(1);

        buyClient.getCollections(999, new Callback<List<Collection>>() {
            @Override
            public void success(List<Collection> collections) {
                assertNotNull(collections);
                assertEquals(collections.size(), 0);
                latch.countDown();
            }

            @Override
            public void failure(BuyClientError error) {
                fail();
            }
        });

        latch.await();
    }

    @Test
    public void testGetProductByHandle() throws InterruptedException {
        final AtomicReference<Product> productRef = new AtomicReference<>();

        final CountDownLatch getProductLatch = new CountDownLatch(1);
        buyClient.getProducts(1, new Callback<List<Product>>() {
            @Override
            public void success(List<Product> response) {
                if (response != null && response.size() > 0) {
                    productRef.set(response.get(0));
                }
                getProductLatch.countDown();
            }

            @Override
            public void failure(BuyClientError error) {
                getProductLatch.countDown();
            }
        });
        getProductLatch.await();

        final Product expectedProduct = productRef.get();
        if (expectedProduct == null) {
            fail("Expected some product");
        }

        productRef.set(null);

        final CountDownLatch getProductByHandleLatch = new CountDownLatch(1);
        buyClient.getProductByHandle(expectedProduct.getHandle(), new Callback<Product>() {
            @Override
            public void success(Product response) {
                productRef.set(response);
                getProductByHandleLatch.countDown();
            }

            @Override
            public void failure(BuyClientError error) {
                getProductByHandleLatch.countDown();
            }
        });
        getProductByHandleLatch.await();

        final Product actualProduct = productRef.get();
        if (actualProduct == null) {
            fail("Product by handle not found");
        }

        assertEquals(expectedProduct, actualProduct);
    }

    @Test
    public void testGetCollectionByHandle() throws InterruptedException {
        final AtomicReference<Collection> collectionRef = new AtomicReference<>();

        final CountDownLatch getCollectionLatch = new CountDownLatch(1);
        buyClient.getCollections(1, new Callback<List<Collection>>() {
            @Override
            public void success(List<Collection> response) {
                if (response != null && response.size() > 0) {
                    collectionRef.set(response.get(0));
                }
                getCollectionLatch.countDown();
            }

            @Override
            public void failure(BuyClientError error) {
                getCollectionLatch.countDown();
            }
        });
        getCollectionLatch.await();

        final Collection expectedCollection = collectionRef.get();
        if (expectedCollection == null) {
            fail("Expected some collection");
        }

        collectionRef.set(null);

        final CountDownLatch getCollectionByHandleLatch = new CountDownLatch(1);
        buyClient.getCollectionByHandle(expectedCollection.getHandle(), new Callback<Collection>() {
            @Override
            public void success(Collection response) {
                collectionRef.set(response);
                getCollectionByHandleLatch.countDown();
            }

            @Override
            public void failure(BuyClientError error) {
                getCollectionByHandleLatch.countDown();
            }
        });
        getCollectionByHandleLatch.await();

        final Collection actualCollection = collectionRef.get();
        if (actualCollection == null) {
            fail("Collection by handle not found");
        }

        assertEquals(expectedCollection, actualCollection);
    }
}
