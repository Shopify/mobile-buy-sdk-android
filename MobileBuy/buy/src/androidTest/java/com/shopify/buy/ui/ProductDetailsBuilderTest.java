package com.shopify.buy.ui;

import android.content.Intent;
import android.os.Bundle;

import com.shopify.buy.extensions.ShopifyAndroidTestCase;

/**
 * Created by krisorr on 2015-07-14.
 */
public class ProductDetailsBuilderTest extends ShopifyAndroidTestCase {

    private static final String WEB_RETURN_TO_LABEL = "Go Back";
    private static final String WEB_RETURN_TO_URL = "myapp://";

    public void testBuild() {
        Intent intent = new ProductDetailsBuilder(this.getContext())
                .setShopDomain(getShopDomain())
                .setApiKey(getApiKey())
                .setChannelid(getChannelId())
                .setApplicationName(data.getApplicationName())
                .setProductId(data.getProductId())
                .setWebReturnToLabel(WEB_RETURN_TO_LABEL)
                .setWebReturnToUrl(WEB_RETURN_TO_URL)
                .build();

        validateIntent(intent);
    }

    public void testBuildWithBuyClient() {
        Intent intent = new ProductDetailsBuilder(getContext(), buyClient)
                .setProductId(data.getProductId())
                .setWebReturnToLabel(WEB_RETURN_TO_LABEL)
                .setWebReturnToUrl(WEB_RETURN_TO_URL)
                .build();

        validateIntent(intent);
    }

    public void testBuildWithMissingShopDomain() {
        try {
            Intent intent = new ProductDetailsBuilder(getContext())
                    .setApiKey(getApiKey())
                    .setApplicationName(data.getApplicationName())
                    .setChannelid(getChannelId())
                    .setProductId(data.getProductId())
                    .build();

            fail("Expected IllegalArgumentException");
        } catch (Exception e) {
        }

    }

    public void testBuildWithMissingApiKey() {
        try {
            Intent intent = new ProductDetailsBuilder(getContext())
                    .setShopDomain(getShopDomain())
                    .setChannelid(getChannelId())
                    .setApplicationName(data.getApplicationName())
                    .setProductId(data.getProductId())
                    .build();

            fail("Expected IllegalArgumentException");
        } catch (Exception e) {
        }
    }

    public void testBuildWithMissingChannelId() {
        try {
            Intent intent = new ProductDetailsBuilder(getContext())
                    .setShopDomain(getShopDomain())
                    .setApiKey(getApiKey())
                    .setApplicationName(data.getApplicationName())
                    .setProductId(data.getProductId())
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
                    .setChannelid(getChannelId())
                    .setProductId(data.getProductId())
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
        assertEquals(bundle.getString(ProductDetailsConfig.EXTRA_SHOP_CHANNEL_ID), getChannelId());
        assertEquals(bundle.getString(ProductDetailsConfig.EXTRA_SHOP_PRODUCT_ID), data.getProductId());
        assertEquals(bundle.getString(ProductDetailsConfig.EXTRA_WEB_RETURN_TO_LABEL), WEB_RETURN_TO_LABEL);
        assertEquals(bundle.getString(ProductDetailsConfig.EXTRA_WEB_RETURN_TO_URL), WEB_RETURN_TO_URL);
        assertEquals(bundle.getString(ProductDetailsConfig.EXTRA_SHOP_APPLICATION_NAME), data.getApplicationName());
    }

}
