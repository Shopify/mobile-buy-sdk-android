/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 Shopify Inc.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.shopify.sample.ui;

import android.content.Intent;
import android.os.Bundle;
import android.test.AndroidTestCase;

import com.shopify.buy.dataprovider.BuyClient;
import com.shopify.buy.dataprovider.BuyClientBuilder;

public class ProductDetailsBuilderTest extends AndroidTestCase {

    private static final String WEB_RETURN_TO_LABEL = "Go Back";
    private static final String WEB_RETURN_TO_URL = "myapp://";

    protected BuyClient buyClient;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        buyClient = new BuyClientBuilder()
                .shopDomain(getShopDomain())
                .apiKey(getApiKey())
                .appId(getAppId())
                .applicationName(getApplicationName())
                .build();
    }

    public void testBuild() {
        Intent intent = new ProductDetailsBuilder(this.getContext())
                .setShopDomain(getShopDomain())
                .setApiKey(getApiKey())
                .setAppId(getAppId())
                .setApplicationName(getApplicationName())
                .setProductId(getProductId())
                .build();

        validateIntent(intent);
    }

    public void testBuildWithBuyClient() {
        Intent intent = new ProductDetailsBuilder(getContext(), buyClient)
                .setProductId(getProductId())
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
        assertTrue(getProductId() == bundle.getLong(ProductDetailsConfig.EXTRA_SHOP_PRODUCT_ID, -1));
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

    public Long getProductId() {
        return 2096063363L;
    }

    public String getApplicationName() {
        return "MobileBuyTest";
    }
}
