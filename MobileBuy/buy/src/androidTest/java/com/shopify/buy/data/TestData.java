package com.shopify.buy.data;

import android.content.Context;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.shopify.buy.BuildConfig;

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

    public String getCollectionId() {
        return data.get("collection_id").getAsString();
    }

    public String getProductId() {
        return data.get("product_ids").getAsJsonArray().get(0).getAsString();
    }

    public String getProductIdWithVariants() {
        return data.get("product_ids").getAsJsonArray().get(1).getAsString();
    }

    public String getProductIdWithTags() {
        return data.get("product_ids").getAsJsonArray().get(1).getAsString();
    }

    public String getProductIdWithoutTags() {
        return data.get("product_ids").getAsJsonArray().get(0).getAsString();
    }

    public String getValidTag() {
        return data.get("tags").getAsJsonArray().get(0).getAsString();
    }

    public String getInvalidTag() {
        return data.get("tags").getAsJsonArray().get(1).getAsString();
    }

    public List<String> getProductIds() {
        ArrayList<String> productIds = new ArrayList<>();
        productIds.add(data.get("product_ids").getAsJsonArray().get(0).getAsString());
        productIds.add(data.get("product_ids").getAsJsonArray().get(1).getAsString());
        return productIds;
    }

    public String getGiftCardId(GiftCardType type) {
        return data.get("gift_cards").getAsJsonObject().get(type.name().toLowerCase()).getAsJsonObject().get("id").getAsString();
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

}