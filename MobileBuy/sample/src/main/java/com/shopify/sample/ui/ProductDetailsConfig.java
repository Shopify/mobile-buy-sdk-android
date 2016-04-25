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

import android.os.Bundle;

import com.shopify.buy.model.Product;
import com.shopify.buy.model.Shop;

/***
 * Used to serialize data for the {@link ProductDetailsActivity}.
 */
class ProductDetailsConfig {

    public static final String EXTRA_SHOP_DOMAIN = "com.shopify.buy.ui.SHOP_DOMAIN";
    public static final String EXTRA_SHOP_API_KEY = "com.shopify.buy.ui.API_KEY";
    public static final String EXTRA_SHOP_APP_ID = "com.shopify.buy.ui.APP_ID";
    public static final String EXTRA_SHOP_APPLICATION_NAME = "com.shopify.buy.ui.SHOP_APPLICATION_NAME";
    public static final String EXTRA_SHOP_PRODUCT_ID = "com.shopify.buy.ui.PRODUCT_ID";
    public static final String EXTRA_SHOP_PRODUCT = "com.shopify.buy.ui.PRODUCT";
    public static final String EXTRA_SHOP_SHOP = "com.shopify.buy.ui.SHOP";
    public static final String EXTRA_WEB_RETURN_TO_URL = "com.shopify.buy.ui.WEB_RETURN_TO_URL";
    public static final String EXTRA_WEB_RETURN_TO_LABEL = "com.shopify.buy.ui.WEB_RETURN_TO_LABEL";
    public static final String EXTRA_THEME = "com.shopify.buy.ui.THEME";

    private String shopDomain;
    private String apiKey;
    private String appId;
    private String applicationName;
    private String productId;
    private Product product;
    private String webReturnToUrl;
    private String webReturnToLabel;
    private ProductDetailsTheme theme;
    private Shop shop;

    public String getShopShopDomain() {
        return shopDomain;
    }

    public String getApiKey() {
        return apiKey;
    }

    public String getAppId() {
        return appId;
    }

    public String getApplicationName() {
        return applicationName;
    }

    public String getProductId() {
        return productId;
    }

    public Product getProduct() {
        return product;
    }

    public String getWebReturnToUrl() {
        return webReturnToUrl;
    }

    public String getWebReturnToLabel() {
        return webReturnToLabel;
    }

    public ProductDetailsTheme getTheme() {
        return theme;
    }

    public void setShopDomain(String shopDomain) {
        this.shopDomain = shopDomain;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public void setWebReturnToUrl(String webReturnToUrl) {
        this.webReturnToUrl = webReturnToUrl;
    }

    public void setWebReturnToLabel(String webReturnToLabel) {
        this.webReturnToLabel = webReturnToLabel;
    }

    public void setShop(Shop shop) {
        this.shop = shop;
    }

    public void setTheme(ProductDetailsTheme theme) {
        this.theme = theme;
    }


    public Bundle toBundle() {
        Bundle bundle = new Bundle();

        if (shopDomain != null) {
            bundle.putString(EXTRA_SHOP_DOMAIN, shopDomain);
        }

        if (apiKey != null) {
            bundle.putString(EXTRA_SHOP_API_KEY, apiKey);
        }

        if (appId != null) {
            bundle.putString(EXTRA_SHOP_APP_ID, appId);
        }

        if (applicationName != null) {
            bundle.putString(EXTRA_SHOP_APPLICATION_NAME, applicationName);
        }

        if (productId != null) {
            bundle.putString(EXTRA_SHOP_PRODUCT_ID, productId);
        }

        if (product != null) {
            bundle.putString(EXTRA_SHOP_PRODUCT, product.toJsonString());
        }

        if (shop != null) {
            bundle.putString(EXTRA_SHOP_SHOP, shop.toJsonString());
        }

        if (webReturnToUrl != null) {
            bundle.putString(EXTRA_WEB_RETURN_TO_URL, webReturnToUrl);
        }

        if (webReturnToLabel != null) {
            bundle.putString(EXTRA_WEB_RETURN_TO_LABEL, webReturnToLabel);
        }

        if (theme != null) {
            bundle.putParcelable(EXTRA_THEME, theme);
        }

        return bundle;
    }


}
