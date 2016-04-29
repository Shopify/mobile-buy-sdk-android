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

package com.shopify.buy.model;

import com.google.gson.annotations.SerializedName;
import com.shopify.buy.dataprovider.BuyClientFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Contains general settings and information about the shop
 */
public class Shop extends ShopifyObject {

    protected String name;

    protected String city;

    protected String province;

    protected String country;

    @SerializedName("contact_email")
    protected String contactEmail;

    protected String currency;

    protected String domain;

    protected String url;

    @SerializedName("myshopify_domain")
    protected String myshopifyDomain;

    protected String description;

    @SerializedName("ships_to_countries")
    protected List<String> shipsToCountries;

    @SerializedName("money_format")
    protected String moneyFormat;

    @SerializedName("published_products_count")
    protected long publishedProductsCount;

    /**
     * @return The name of this shop.
     */
    public String getName() {
        return name;
    }

    /**
     * @return The city in which the shop is located.
     */
    public String getCity() {
        return city;
    }

    /**
     * @return The shop's normalized province or state name.
     */
    public String getProvince() {
        return province;
    }

    /**
     * @return The country in which the shop is located.
     */
    public String getCountry() {
        return country;
    }

    /**
     * @return The contact email for the shop.
     */
    public String getContactEmail() {
        return contactEmail;
    }

    /**
     * @return The three-letter code for the currency that the shop accepts.
     */
    public String getCurrency() {
        return currency;
    }

    /**
     * @return The shop's domain (may be different than the 'myshopify.com' domain).
     */
    public String getDomain() {
        return domain;
    }

    /**
     * @return The full URL for the store's website.
     */
    public String getUrl() {
        return url;
    }

    /**
     * @return The shop's 'myshopify.com' domain.
     */
    public String getMyshopifyDomain() {
        return myshopifyDomain;
    }

    /**
     * @return The shop's description.
     */
    public String getDescription() {
        return description;
    }

    /**
     * @return A list of two-letter country codes identifying the countries that the shop ships to.
     */
    public List<String> getShipsToCountries() {
        if (shipsToCountries == null) {
            shipsToCountries = new ArrayList<>();
        }
        return shipsToCountries;
    }

    /**
     * @return A string representing the way currency is formatted when the currency isn't specified (e.g. "${{amount}}").
     */
    public String getMoneyFormat() {
        return moneyFormat;
    }

    /**
     * @return The number of products that have been published in this shop.
     */
    public long getPublishedProductsCount() {
        return publishedProductsCount;
    }

    /**
     * @return A shop object created using the values in the JSON string.
     */
    public static Shop fromJson(String json) {
        return BuyClientFactory.createDefaultGson().fromJson(json, Shop.class);
    }

}
