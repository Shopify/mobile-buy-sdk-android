package com.shopify.buy.extensions;

import android.test.AndroidTestCase;
import android.text.TextUtils;

import com.shopify.buy.BuildConfig;
import com.shopify.buy.data.MockResponder;
import com.shopify.buy.data.MockResponseGenerator;
import com.shopify.buy.data.TestData;
import com.shopify.buy.dataprovider.BuyClient;
import com.shopify.buy.dataprovider.BuyClientFactory;
import com.shopify.buy.model.Cart;
import com.shopify.buy.model.LineItem;
import com.shopify.buy.model.Product;
import com.shopify.buy.model.Shop;

import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Base class for Mobile Buy SDK Tests
 */
public class ShopifyAndroidTestCase extends AndroidTestCase {

    // FOR INTERNAL SHOPIFY USE ONLY. See MockResponseGenerator for instructions.
    private static final boolean GENERATE_MOCK_RESPONSES = false;

    protected static final boolean USE_MOCK_RESPONSES = TextUtils.isEmpty(BuildConfig.SHOP_DOMAIN)
                                                        || TextUtils.isEmpty(BuildConfig.API_KEY)
                                                        || TextUtils.isEmpty(BuildConfig.CHANNEL_ID);

    protected static TestData data;

    protected BuyClient buyClient;

    protected Shop shop;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        if (data == null) {
            data = new TestData(getContext());
        }

        buyClient = getBuyClient(getShopDomain(), getApiKey(), getChannelId(), data.getApplicationName());

        if (USE_MOCK_RESPONSES) {
            MockResponder.onNewTest(getName());
        } else if (GENERATE_MOCK_RESPONSES) {
            MockResponseGenerator.onNewTest(getName());
        }

        // Required by Mockito
        // Fixes "dexcache == null (and no default could be found; consider setting the 'dexmaker.dexcache' system property)"
        // See https://code.google.com/p/dexmaker/issues/detail?id=2
        System.setProperty("dexmaker.dexcache", getContext().getCacheDir().getPath());
    }

    protected BuyClient getBuyClient(String shopDomain, String apiKey, String channelId, String applicationName) {
        BuyClient buyClient = BuyClientFactory.getBuyClient(shopDomain, apiKey, channelId, applicationName);

        if (USE_MOCK_RESPONSES) {
            buyClient.addInterceptor(new MockResponder(getContext()));
        } else if (GENERATE_MOCK_RESPONSES) {
            buyClient.addInterceptor(new MockResponseGenerator(getContext()));
        }

        return buyClient;
    }

    protected String getShopDomain() {
        return USE_MOCK_RESPONSES ? "placeholder.myshopify.com" : BuildConfig.SHOP_DOMAIN;
    }

    protected String getApiKey() {
        return USE_MOCK_RESPONSES ? "placeholderApiKey" : BuildConfig.API_KEY;
    }

    protected String getChannelId() {
        return USE_MOCK_RESPONSES ? "placeholderChannelId" : BuildConfig.CHANNEL_ID;
    }

    protected String getAndroidPayPublicKey() {
        return USE_MOCK_RESPONSES ? "placeholderAndroidPayPublicKey" : BuildConfig.ANDROID_PAY_PUBLIC_KEY;
    }

    public Cart createCart() throws InterruptedException {
        final CountDownLatch latch = new CountDownLatch(1);

        final AtomicReference<Product> productRef = new AtomicReference<>();
        buyClient.getProduct(data.getProductId(), new Callback<Product>() {
            @Override
            public void success(Product product, Response response) {
                productRef.set(product);
                latch.countDown();
            }

            @Override
            public void failure(RetrofitError error) {
                fail(BuyClient.getErrorBody(error));
            }
        });

        latch.await();

        Cart cart = new Cart();
        cart.addVariant(productRef.get().getVariants().get(0));

        // add some custom properties
        LineItem lineItem = cart.getLineItems().get(0);
        Map<String, String> properties = lineItem.getProperties();
        properties.put("color", "red");
        properties.put("size", "large");

        return cart;
    }

}
