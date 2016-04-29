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

package com.shopify.buy.ui;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.shopify.buy.dataprovider.BuyClient;
import com.shopify.buy.model.Product;
import com.shopify.buy.model.Shop;

/**
 * Builds an {@link Intent} that can be used to start a {@link ProductDetailsActivity}
 */
public class ProductDetailsBuilder {

    private final Context context;

    private final ProductDetailsConfig productDetailsConfig = new ProductDetailsConfig();

    /**
     * Create a default ProductDetailsBuilder.
     * If this constructor is used, {@link #setShopDomain(String)}, {@link #setApplicationName(String)}, {@link #setApiKey(String)}, {@link #setChannelId(String)}, and {@link #setProductId(String)} must be called.
     *
     * @param context context to use for starting the {@code Activity}
     */
    public ProductDetailsBuilder(Context context) {
        this.context = context;
    }

    /**
     * Constructor that will use an existing {@link BuyClient} to configure the {@link ProductDetailsActivity}.
     * If this constructor is user, {@link #setProductId(String)} must be called.
     *
     * @param context context to use for launching the {@code Activity}
     * @param client  the {@link BuyClient} to use to configure the ProductDetailsActivity
     */
    public ProductDetailsBuilder(Context context, BuyClient client) {
        this.context = context;

        productDetailsConfig.setShopDomain(client.getShopDomain());
        productDetailsConfig.setApiKey(client.getApiKey());
        productDetailsConfig.setApplicationName(client.getApplicationName());
        productDetailsConfig.setChannelId(client.getChannelId());
        productDetailsConfig.setWebReturnToUrl(client.getWebReturnToUrl());
        productDetailsConfig.setWebReturnToLabel(client.getWebReturnToLabel());
    }

    public ProductDetailsBuilder setShopDomain(String shopDomain) {
        productDetailsConfig.setShopDomain(shopDomain);
        return this;
    }

    public ProductDetailsBuilder setApiKey(String apiKey) {
        productDetailsConfig.setApiKey(apiKey);
        return this;
    }

    @Deprecated
    public ProductDetailsBuilder setChannelid(String channelId) {
        productDetailsConfig.setChannelId(channelId);
        return this;
    }

    public ProductDetailsBuilder setChannelId(String channelId) {
        productDetailsConfig.setChannelId(channelId);
        return this;
    }

    public ProductDetailsBuilder setApplicationName(String applicationName) {
        productDetailsConfig.setApplicationName(applicationName);
        return this;
    }

    public ProductDetailsBuilder setProductId(String productId) {
        productDetailsConfig.setProductId(productId);
        return this;
    }

    public ProductDetailsBuilder setProduct(Product product) {
        productDetailsConfig.setProduct(product);
        return this;
    }

    public ProductDetailsBuilder setWebReturnToUrl(String webReturnToUrl) {
        productDetailsConfig.setWebReturnToUrl(webReturnToUrl);
        return this;
    }

    public ProductDetailsBuilder setWebReturnToLabel(String webReturnToLabel) {
        productDetailsConfig.setWebReturnToLabel(webReturnToLabel);
        return this;
    }

    public ProductDetailsBuilder setShop(Shop shop) {
        productDetailsConfig.setShop(shop);
        return this;
    }

    public ProductDetailsBuilder setTheme(ProductDetailsTheme theme) {
        productDetailsConfig.setTheme(theme);
        return this;
    }

    public Intent build() {

        if (TextUtils.isEmpty(productDetailsConfig.getShopShopDomain())) {
            throw new IllegalArgumentException("shopDomain must be provided, and cannot be empty");
        }

        if (TextUtils.isEmpty(productDetailsConfig.getApiKey())) {
            throw new IllegalArgumentException("apiKey must be provided, and cannot be empty");
        }

        if (TextUtils.isEmpty(productDetailsConfig.getApplicationName())) {
            throw new IllegalArgumentException("applicationName must be provided, and cannot be empty");
        }

        if (TextUtils.isEmpty(productDetailsConfig.getChannelId())) {
            throw new IllegalArgumentException("channelId must be provided, and cannot be empty");
        }

        if (TextUtils.isEmpty(productDetailsConfig.getProductId()) && productDetailsConfig.getProduct() == null) {
            throw new IllegalArgumentException("One of productId or product must be provided, and cannot be empty");
        }

        Intent intent = new Intent(context, ProductDetailsActivity.class);
        intent.putExtras(productDetailsConfig.toBundle());

        return intent;
    }
}
