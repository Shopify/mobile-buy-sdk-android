package com.shopify.buy.extensions;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.text.TextUtils;

import com.shopify.buy.BuildConfig;
import com.shopify.buy.data.MockResponder;
import com.shopify.buy.data.MockResponseGenerator;
import com.shopify.buy.data.TestData;
import com.shopify.buy.dataprovider.BuyClient;
import com.shopify.buy.dataprovider.BuyClientFactory;
import com.shopify.buy.model.CartTest;
import com.shopify.buy.model.CheckoutTest;

import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TestName;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Base class for Mobile Buy SDK Tests
 */
public class ShopifyAndroidTestCase {
    @Rule
    public TestName name = new TestName();

    /**
     * FOR INTERNAL SHOPIFY USE ONLY. See {@link MockResponseGenerator} for instructions.
     */
    private static final boolean GENERATE_MOCK_RESPONSES = false;

    protected static final boolean USE_MOCK_RESPONSES = TextUtils.isEmpty(BuildConfig.SHOP_DOMAIN)
                                                        || TextUtils.isEmpty(BuildConfig.API_KEY)
                                                        || TextUtils.isEmpty(BuildConfig.APP_ID);



    private Context context;

    protected static TestData data;

    protected BuyClient buyClient;

    @Before
    public void setUp() throws Exception {
        context = InstrumentationRegistry.getContext();

        if (data == null) {
            data = new TestData(context);
        }

        buyClient = getBuyClient(getShopDomain(), getApiKey(), getAppId(), data.getApplicationName());

        if (USE_MOCK_RESPONSES) {
            MockResponder.onNewTest(getName());
        } else if (GENERATE_MOCK_RESPONSES) {
            MockResponseGenerator.onNewTest(getName());
        }

        // Required by Mockito
        // Fixes "dexcache == null (and no default could be found; consider setting the 'dexmaker.dexcache' system property)"
        // See https://code.google.com/p/dexmaker/issues/detail?id=2
        System.setProperty("dexmaker.dexcache", context.getCacheDir().getPath());
    }

    private String getName() {
        return name.getMethodName();
    }

    protected BuyClient getBuyClient(String shopDomain, String apiKey, String appId, String applicationName) {
        BuyClient buyClient;
        if (USE_MOCK_RESPONSES) {
            buyClient = BuyClientFactory.getBuyClient(shopDomain, apiKey, appId, applicationName, new MockResponder(context));
        } else if (GENERATE_MOCK_RESPONSES) {
            buyClient = BuyClientFactory.getBuyClient(shopDomain, apiKey, appId, applicationName, new MockResponseGenerator(context));
        } else {
            buyClient = BuyClientFactory.getBuyClient(shopDomain, apiKey, appId, applicationName);
        }

        return buyClient;
    }

    protected String getShopDomain() {
        return USE_MOCK_RESPONSES ? "placeholder.myshopify.com" : BuildConfig.SHOP_DOMAIN;
    }

    protected String getApiKey() {
        return USE_MOCK_RESPONSES ? "placeholderApiKey" : BuildConfig.API_KEY;
    }

    public String getAppId() {
        return USE_MOCK_RESPONSES ? "placeholderAppId" : BuildConfig.APP_ID;
    }

    protected String getAndroidPayPublicKey() {
        return USE_MOCK_RESPONSES ? "placeholderAndroidPayPublicKey" : BuildConfig.ANDROID_PAY_PUBLIC_KEY;
    }
}
