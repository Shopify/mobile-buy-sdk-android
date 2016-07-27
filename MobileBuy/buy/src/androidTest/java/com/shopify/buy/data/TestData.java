package com.shopify.buy.data;

import android.content.Context;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by davepelletier on 15-08-18.
 */
public class TestData {

    private final JsonObject data;

    public enum GiftCardType {
        VALID11,
        VALID25,
        VALID50,
        EXPIRED,
        INVALID
    }

    public enum DiscountType {
        VALID,
        EXPIRED
    }

    public TestData(Context context) throws IOException {
        InputStream in = context.getAssets().open("test_shop_data.json");
        data = (JsonObject) new JsonParser().parse(new InputStreamReader(in));
    }

    public String getApplicationName() {
        return data.get("application_name").getAsString();
    }

    public Long getCollectionId() {
        return data.get("collection_id").getAsLong();
    }

    public Long getProductId() {
        return data.get("product_ids").getAsJsonArray().get(0).getAsLong();
    }

    public Long getProductIdWithVariants() {
        return data.get("product_ids").getAsJsonArray().get(1).getAsLong();
    }

    public Long getProductIdWithTags() {
        return data.get("product_ids").getAsJsonArray().get(1).getAsLong();
    }

    public Long getProductIdWithoutTags() {
        return data.get("product_ids").getAsJsonArray().get(0).getAsLong();
    }

    public String getValidTag() {
        return data.get("tags").getAsJsonArray().get(0).getAsString();
    }

    public String getInvalidTag() {
        return data.get("tags").getAsJsonArray().get(1).getAsString();
    }

    public List<Long> getProductIds() {
        ArrayList<Long> productIds = new ArrayList<>();
        productIds.add(data.get("product_ids").getAsJsonArray().get(0).getAsLong());
        productIds.add(data.get("product_ids").getAsJsonArray().get(1).getAsLong());
        return productIds;
    }

    public Long getGiftCardId(GiftCardType type) {
        return data.get("gift_cards").getAsJsonObject().get(type.name().toLowerCase()).getAsJsonObject().get("id").getAsLong();
    }

    public String getGiftCardCode(GiftCardType type) {
        return data.get("gift_cards").getAsJsonObject().get(type.name().toLowerCase()).getAsJsonObject().get("code").getAsString();
    }

    public float getGiftCardValue(GiftCardType type) {
        return data.get("gift_cards").getAsJsonObject().get(type.name().toLowerCase()).getAsJsonObject().get("value").getAsFloat();
    }

    public String getDiscountCode(DiscountType type) {
        return data.get("discounts").getAsJsonObject().get(type.name().toLowerCase()).getAsJsonObject().get("code").getAsString();
    }

    public float getDiscountValue(DiscountType type) {
        return data.get("discounts").getAsJsonObject().get(type.name().toLowerCase()).getAsJsonObject().get("value").getAsFloat();
    }

    public String getCollectionHandle() {
        return data.get("collection_handle").getAsString();
    }
}