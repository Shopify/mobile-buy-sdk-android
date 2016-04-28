package com.shopify.sample.ui;

import android.content.Intent;
import android.os.Bundle;
import android.test.AndroidTestCase;

import com.shopify.buy.dataprovider.BuyClientFactory;
import com.shopify.buy.dataprovider.BuyClient;

/**
 * Created by krisorr on 2015-07-14.
 */
public class ProductDetailsBuilderTest extends AndroidTestCase {

    private static final String WEB_RETURN_TO_LABEL = "Go Back";
    private static final String WEB_RETURN_TO_URL = "myapp://";

    protected BuyClient buyClient;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        buyClient = BuyClientFactory.getBuyClient(getShopDomain(), getApiKey(), getAppId(), getApplicationName());
    }

    public void testBuild() {
        Intent intent = new ProductDetailsBuilder(this.getContext())
                .setShopDomain(getShopDomain())
                .setApiKey(getApiKey())
                .setAppId(getAppId())
                .setApplicationName(getApplicationName())
                .setProductId(getProductId())
                .setWebReturnToLabel(WEB_RETURN_TO_LABEL)
                .setWebReturnToUrl(WEB_RETURN_TO_URL)
                .build();

        validateIntent(intent);
    }

    public void testBuildWithBuyClient() {
        Intent intent = new ProductDetailsBuilder(getContext(), buyClient)
                .setProductId(getProductId())
                .setWebReturnToLabel(WEB_RETURN_TO_LABEL)
                .setWebReturnToUrl(WEB_RETURN_TO_URL)
                .build();

        validateIntent(intent);
    }

    public void testBuildWithMissingShopDomain() {
        try {
            Intent intent = new ProductDetailsBuilder(getContext())
                    .setApiKey(getApiKey())
                    .setAppId(getAppId())
                    .setApplicationName(getApplicationName())
                    .setProductId(getProductId())
                    .build();

            fail("Expected IllegalArgumentException");
        } catch (Exception e) {
        }

    }

    public void testBuildWithMissingApiKey() {
        try {
            Intent intent = new ProductDetailsBuilder(getContext())
                    .setShopDomain(getShopDomain())
                    .setAppId(getAppId())
                    .setApplicationName(getApplicationName())
                    .setProductId(getProductId())
                    .build();

            fail("Expected IllegalArgumentException");
        } catch (Exception e) {
        }
    }

    public void testBuildWithMissingApplicationName() {
        try {
            Intent intent = new ProductDetailsBuilder(getContext())
                    .setShopDomain(getShopDomain())
                    .setApiKey(getApiKey())
                    .setAppId(getAppId())
                    .setProductId(getProductId())
                    .build();

            fail("Expected IllegalArgumentException");
        } catch (Exception e) {
        }
    }

    private void validateIntent(Intent intent) {
        assertNotNull(intent);

        Bundle bundle = intent.getExtras();

        assertEquals(bundle.getString(ProductDetailsConfig.EXTRA_SHOP_DOMAIN), getShopDomain());
        assertEquals(bundle.getString(ProductDetailsConfig.EXTRA_SHOP_API_KEY), getApiKey());
        assertEquals(bundle.getString(ProductDetailsConfig.EXTRA_SHOP_PRODUCT_ID), getProductId());
        assertEquals(bundle.getString(ProductDetailsConfig.EXTRA_WEB_RETURN_TO_LABEL), WEB_RETURN_TO_LABEL);
        assertEquals(bundle.getString(ProductDetailsConfig.EXTRA_WEB_RETURN_TO_URL), WEB_RETURN_TO_URL);
        assertEquals(bundle.getString(ProductDetailsConfig.EXTRA_SHOP_APPLICATION_NAME), getApplicationName());
    }

    public String getShopDomain() {
        return "placeholder.myshopify.com";
    }

    public String getApiKey() {
        return "placeholderApiKey";
    }

    public String getAppId() {
        return "placeholderAppId";
    }

    public String getProductId() {
        return "2096063363";
    }

    public String getApplicationName() {
        return "MobileBuyTest";
    }
}
