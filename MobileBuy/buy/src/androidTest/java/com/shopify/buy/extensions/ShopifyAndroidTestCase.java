package com.shopify.buy.extensions;

import android.test.AndroidTestCase;
import android.text.TextUtils;

import com.shopify.buy.BuildConfig;
import com.shopify.buy.data.MockResponder;
import com.shopify.buy.data.MockResponseGenerator;
import com.shopify.buy.data.TestData;
import com.shopify.buy.dataprovider.BuyClient;
import com.shopify.buy.dataprovider.BuyClientFactory;

/**
 * Base class for Mobile Buy SDK Tests
 */
public class ShopifyAndroidTestCase extends AndroidTestCase{

    /**
     * FOR INTERNAL SHOPIFY USE ONLY. See {@link MockResponseGenerator} for instructions.
     */
    private static final boolean GENERATE_MOCK_RESPONSES = false;

    protected static final boolean USE_MOCK_RESPONSES = TextUtils.isEmpty(BuildConfig.SHOP_DOMAIN)
                                                        || TextUtils.isEmpty(BuildConfig.API_KEY)
                                                        || TextUtils.isEmpty(BuildConfig.APP_ID);


    protected static TestData data;

    protected BuyClient buyClient;

    @Override
    public void setUp() throws Exception {
        super.setUp();

        if (data == null) {
            data = new TestData(getContext());
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
        System.setProperty("dexmaker.dexcache", getContext().getCacheDir().getPath());
    }

    protected BuyClient getBuyClient(String shopDomain, String apiKey, String appId, String applicationName) {
        BuyClient buyClient;
        if (USE_MOCK_RESPONSES) {
            buyClient = BuyClientFactory.getBuyClient(shopDomain, apiKey, appId, applicationName, new MockResponder(getContext()));
        } else if (GENERATE_MOCK_RESPONSES) {
            buyClient = BuyClientFactory.getBuyClient(shopDomain, apiKey, appId, applicationName, new MockResponseGenerator(getContext()));
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

}
