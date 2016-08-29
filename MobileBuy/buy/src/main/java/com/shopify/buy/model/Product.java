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

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.annotations.SerializedName;
import com.shopify.buy.dataprovider.BuyClientUtils;
import com.shopify.buy.utils.CollectionUtils;
import com.shopify.buy.utils.DateUtility;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * A {@code Product} is an individual item for sale in a Shopify store.
 */
public class Product extends ShopifyObject {

    @SerializedName("product_id")
    protected Long productId;

    protected String title;

    protected String handle;

    @SerializedName("body_html")
    protected String bodyHtml;

    @SerializedName("published_at")
    protected Date publishedAtDate;

    @SerializedName("created_at")
    protected Date createdAtDate;

    @SerializedName("updated_at")
    protected Date updatedAtDate;

    protected String vendor;

    @SerializedName("product_type")
    protected String productType;

    protected List<ProductVariant> variants;

    protected List<Image> images;

    protected List<Option> options;

    protected String tags;

    protected Set<String> tagSet;

    protected Boolean available;

    protected Boolean published;

    protected Set<String> prices;

    protected String minimumPrice;

    /**
     * @return {@code true} if this product has been published on the store, {@code false} otherwise.
     */
    public boolean isPublished() {
        return published != null && published;
    }

    /**
     * @return The unique identifier for this product.
     */
    public Long getProductId() {
        return productId;
    }

    /**
     * @return The title of this product.
     */
    public String getTitle() {
        return title;
    }

    /**
     * @return The handle of the product. Can be used to construct links to the web page for the product.
     */
    public String getHandle() {
        return handle;
    }

    /**
     * @return The description of the product, complete with HTML formatting.
     */
    public String getBodyHtml() {
        return bodyHtml;
    }

    /**
     * Use {@link #getPublishedAtDate() getPublishedAtDate()}.
     *
     * @return The date this product was published.
     */
    @Deprecated
    public String getPublishedAt() {
        return publishedAtDate == null ? null : DateUtility.createDefaultDateFormat().format(publishedAtDate);
    }

    /**
     * Use {@link #getCreatedAtDate() getCreatedAtDate()}.
     *
     * @return The date this product was created.
     */
    @Deprecated
    public String getCreatedAt() {
        return createdAtDate == null ? null : DateUtility.createDefaultDateFormat().format(createdAtDate);
    }

    /**
     * Use {@link #getUpdatedAtDate() getUpdatedAtDate()}.
     *
     * @return The date this product was updated.
     */
    @Deprecated
    public String getUpdatedAt() {
        return updatedAtDate == null ? null : DateUtility.createDefaultDateFormat().format(updatedAtDate);
    }

    /**
     * @return The date this product was published.
     */
    public Date getPublishedAtDate() {
        return publishedAtDate;
    }

    /**
     * @return The date this product was created.
     */
    public Date getCreatedAtDate() {
        return createdAtDate;
    }

    /**
     * @return The date this product was last updated.
     */
    public Date getUpdatedAtDate() {
        return updatedAtDate;
    }

    /**
     * @return The name of the vendor of this product.
     */
    public String getVendor() {
        return vendor;
    }

    /**
     * @return The categorization that this product was tagged with, commonly used for filtering and searching.
     */
    public String getProductType() {
        return productType;
    }

    /**
     * @return A list of additional categorizations that a product can be tagged with, commonly used for filtering and searching. Each tag has a character limit of 255.
     */
    public Set<String> getTags() {
        return tagSet;
    }

    /**
     * @return A list {@link ProductVariant} objects, each one representing a different version of this product.
     */
    public List<ProductVariant> getVariants() {
        return variants;
    }

    /**
     * @return A list of {@link Image} objects, each one representing an image associated with this product.
     */
    public List<Image> getImages() {
        return images;
    }

    /**
     * @return The first image URL from this Product's list of images.
     */
    public String getFirstImageUrl() {
        if (hasImage()) {
            Image image = images.get(0);
            if (image != null) {
                return image.getSrc();
            }
        }
        return null;
    }

    /**
     * @return {code true} if this product has at least one image, {@code false} otherwise.
     */
    public boolean hasImage() {
        return images != null && !images.isEmpty();
    }

    /**
     * @return A list of {@link Option} objects, which can be used to select a specific {@link ProductVariant}.
     */
    public List<Option> getOptions() {
        return options;
    }

    /**
     * @return {@code true} if this product is in stock and available for purchase, {@code false} otherwise.
     */
    public boolean isAvailable() {
        return available != null && available;
    }

    /**
     * For internal use only.
     *
     * @return true if this product has a default variant.
     */
    public boolean hasDefaultVariant() {
        if (CollectionUtils.isEmpty(variants) || variants.size() != 1) {
            return false;
        }

        ProductVariant firstVariant = variants.get(0);
        List<OptionValue> optionValues = firstVariant.getOptionValues();

        return firstVariant.getTitle().equals("Default Title")
                && !CollectionUtils.isEmpty(optionValues)
                && (optionValues.get(0).getValue().equals("Default Title") || optionValues.get(0).getValue().equals("Default"));
    }

    /**
     * Returns the {@code Image} for the {@code ProductVariant} with the given id
     *
     * @param variant the {@link ProductVariant} to find the {@link Image}
     * @return the {@link Image} corresponding to the {@link ProductVariant} if one was found, otherwise the {@code Image} for the {@link Product}.  This may return null if no applicable images were found.
     */
    public Image getImage(ProductVariant variant) {
        if (variant == null) {
            throw new NullPointerException("variant cannot be null");
        }

        List<Image> images = getImages();

        if (images == null || images.size() < 1) {
            // we did not find any images
            return null;
        }

        for (Image image : images) {
            if (image.getVariantIds() != null && image.getVariantIds().contains(variant.getId())) {
                return image;
            }
        }

        // The variant did not have an image, use the default image in the Product
        return images.get(0);
    }

    /**
     * @param optionValues A list of {@link OptionValue} objects that represent a specific variant selection.
     * @return The {@link ProductVariant} that matches the given list of the OptionValues, or {@code null} if no such variant exists.
     */
    public ProductVariant getVariant(List<OptionValue> optionValues) {
        if (optionValues == null) {
            return null;
        }

        int numOptions = optionValues.size();
        for (ProductVariant variant : variants) {
            for (int i = 0; i < numOptions; i++) {
                if (!variant.getOptionValues().get(i).getValue().equals(optionValues.get(i).getValue())) {
                    break;
                } else if (i == numOptions - 1) {
                    return variant;
                }
            }
        }

        return null;
    }

    /**
     * @return A Set containing all the unique prices of the variants.
     */
    public Set<String> getPrices() {
        if (prices != null) {
            return prices;
        }

        prices = new HashSet<>();
        if (!CollectionUtils.isEmpty(variants)) {
            for (ProductVariant variant : variants) {
                prices.add(variant.getPrice());
            }
        }

        return prices;
    }

    /**
     * @return The minimum price from the variants.
     */
    public String getMinimumPrice() {
        if (minimumPrice != null) {
            return minimumPrice;
        }

        prices = getPrices();

        for (String price : prices) {
            if (minimumPrice == null) {
                minimumPrice = price;
            }
            if (Float.valueOf(price) < Float.valueOf(minimumPrice)) {
                minimumPrice = price;
            }
        }

        return minimumPrice;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Product)) return false;

        Product product = (Product) o;

        return productId.equals(product.productId);

    }

    @Override
    public int hashCode() {
        return productId.hashCode();
    }

    public static class ProductDeserializer implements JsonDeserializer<Product> {

        @Override
        public Product deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            return fromJson(json.toString());
        }

    }

    /**
     * A product object created using the values in the JSON string.
     *
     * @param json The json representation of this product.
     * @return A {@link Product}
     */
    public static Product fromJson(String json) {
        Gson gson = BuyClientUtils.createDefaultGson(Product.class);

        Product product = gson.fromJson(json, Product.class);

        List<ProductVariant> variants = product.getVariants();

        if (variants != null) {
            for (ProductVariant variant : variants) {
                variant.productId = product.productId;
                variant.productTitle = product.getTitle();

                Image image = product.getImage(variant);
                if (image != null) {
                    variant.imageUrl = image.getSrc();
                }
            }
        }

        // Create the tagSet.
        product.tagSet = new HashSet<>();

        // Populate the tagSet from the comma separated list.
        if (!TextUtils.isEmpty(product.tags)) {
            for (String tag : product.tags.split(",")) {
                String myTag = tag.trim();
                product.tagSet.add(myTag);
            }
        }

        return product;
    }

}
