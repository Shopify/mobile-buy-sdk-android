package com.shopify.buy.extensions;

import com.shopify.buy.BuildConfig;
import com.shopify.buy.utils.StringUtils;
import com.shopify.buy.data.MockResponder;
import com.shopify.buy.data.MockResponseGenerator;
import com.shopify.buy.data.TestData;
import com.shopify.buy.dataprovider.BuyClient;
import com.shopify.buy.dataprovider.BuyClientFactory;

import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TestName;

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

    protected static final boolean USE_MOCK_RESPONSES = StringUtils.isEmpty(BuildConfig.SHOP_DOMAIN)
                                                        || StringUtils.isEmpty(BuildConfig.API_KEY)
                                                        || StringUtils.isEmpty(BuildConfig.APP_ID);


    protected static TestData data;

    protected BuyClient buyClient;

    @Before
    public void setUp() throws Exception {

        if (data == null) {
            data = new TestData();
        }

        buyClient = getBuyClient(getShopDomain(), getApiKey(), getAppId(), data.getApplicationName());

        if (USE_MOCK_RESPONSES) {
            MockResponder.onNewTest(getName());
        } else if (GENERATE_MOCK_RESPONSES) {
            MockResponseGenerator.onNewTest(getName());
        }

    }

    protected BuyClient getBuyClient(String shopDomain, String apiKey, String appId, String applicationName) {
        BuyClient buyClient;
        if (USE_MOCK_RESPONSES) {
            buyClient = BuyClientFactory.getBuyClient(shopDomain, apiKey, appId, applicationName, new MockResponder());
        } else if (GENERATE_MOCK_RESPONSES) {
            buyClient = BuyClientFactory.getBuyClient(shopDomain, apiKey, appId, applicationName, new MockResponseGenerator());
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

    public String getName() {
        return name.getMethodName();
    }
}
