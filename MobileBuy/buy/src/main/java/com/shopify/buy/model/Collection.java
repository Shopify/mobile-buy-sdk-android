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
import com.shopify.buy.dataprovider.BuyClientUtils;
import com.shopify.buy.model.internal.CollectionImage;
import com.shopify.buy.dataprovider.Callback;

import java.util.Date;

/**
 * Represents a collection of {@link Product}.
 * In order to get the list of products in a collection, use {@link BuyClientImpl#getProducts(int, String, SortOrder, Callback) getProducts(page, collectionId, sortOrder, callback}.
 */
public class Collection extends ShopifyObject {

    public enum SortOrder {
        COLLECTION_DEFAULT("collection-default"),
        BEST_SELLING("best-selling"),
        TITLE_ASCENDING("title-ascending"),
        TITLE_DESCENDING("title-descending"),
        PRICE_ASCENDING("price-ascending"),
        PRICE_DESCENDING("price-descending"),
        CREATED_ASCENDING("created-ascending"),
        CREATED_DESCENDING("created-descending");

        private final String order;

        SortOrder(String s) {
            order = s;
        }

        public String toString() {
            return order;
        }
    }

    protected String title;

    @SerializedName("body_html")
    protected String htmlDescription;

    protected String handle;

    protected boolean published;

    @SerializedName("collection_id")
    protected String collectionId;

    @SerializedName("created_at")
    protected Date createdAtDate;

    @SerializedName("updated_at")
    protected Date updatedAtDate;

    @SerializedName("published_at")
    protected Date publishedAtDate;

    protected CollectionImage image;

    /**
     * @return The creation date for this collection.
     */
    public Date getCreatedAtDate() {
        return createdAtDate;
    }

    /**
     * @return The updated date for this collection.
     */
    public Date getUpdatedAtDate() {
        return updatedAtDate;
    }

    /**
     * @return The publish date for this collection.
     */
    public Date getPublishedAtDate() {
        return publishedAtDate;
    }

    public CollectionImage getImage() {
        return image;
    }

    /**
     * @return This collection's image URL.
     */
    public String getImageUrl() {
        if (image == null) {
            return null;
        }
        return image.getSrc();
    }

    /**
     * @return The title of this collection.
     */
    public String getTitle() {
        return title;
    }

    /**
     * @return The html description of this collection.
     */
    public String getHtmlDescription() {
        return htmlDescription;
    }

    /**
     * @return The handle for this collection.
     */
    public String getHandle() {
        return handle;
    }

    /**
     * Use {@link Collection#isPublished() isPublished()}.
     */
    @Deprecated
    public String getPublished() {
        return String.valueOf(published);
    }

    /**
     * @return {@code true} if this collection has been published on the shop, {@code false} otherwise.
     */
    public boolean isPublished() {
        return published;
    }

    /**
     * @return The unique identifier for this collection.
     */
    public String getCollectionId() {
        return collectionId;
    }

    /**
     * @return A collection object created using the values in the JSON string.
     */
    public static Collection fromJson(String json) {
        return BuyClientUtils.createDefaultGson().fromJson(json, Collection.class);
    }

}