package com.shopify.buy.extensions;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.text.TextUtils;

import com.shopify.buy.BuildConfig;
import com.shopify.buy.data.MockResponder;
import com.shopify.buy.data.MockResponseGenerator;
import com.shopify.buy.data.TestData;
import com.shopify.buy.dataprovider.BuyClient;
import com.shopify.buy.dataprovider.BuyClientBuilder;

import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TestName;

import okhttp3.HttpUrl;
import okhttp3.Protocol;
import okhttp3.logging.HttpLoggingInterceptor;
import rx.schedulers.Schedulers;

/**
 * Base class for Mobile Buy SDK Tests
 */
public class ShopifyAndroidTestCase {

    protected final int productPageSize = 50;

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

        final BuyClientBuilder buyClientBuilder = new BuyClientBuilder()
            .shopDomain(shopDomain)
            .apiKey(apiKey)
            .appId(appId)
            .applicationName(applicationName)
            .callbackScheduler(Schedulers.immediate())
            .productPageSize(productPageSize)
            .networkRequestRetryPolicy(1, 100, 1);

        if (USE_MOCK_RESPONSES) {
            buyClientBuilder.interceptors(new MockResponder(context), new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY));
        } else if (GENERATE_MOCK_RESPONSES) {
            buyClientBuilder.interceptors(new MockResponseGenerator(context), new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY));
        } else {
            buyClientBuilder.interceptors(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY));
        }

        buyClient = buyClientBuilder.build();

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

    public okhttp3.Response createResponse(int code) {

        HttpUrl httpUrl = new HttpUrl.Builder()
            .scheme("https")
            .host("example.com")
            .build();

        okhttp3.Request request = new okhttp3.Request.Builder()
            .url(httpUrl)
            .build();

        return new okhttp3.Response.Builder()
            .code(code)
            .request(request)
            .protocol(Protocol.HTTP_1_0)
            .build();
    }
}
