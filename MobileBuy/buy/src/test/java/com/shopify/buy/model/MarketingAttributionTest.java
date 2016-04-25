package com.shopify.buy.model;

import com.google.gson.Gson;
import com.shopify.buy.dataprovider.BuyClientFactory;
import com.shopify.buy.extensions.ShopifyAndroidTestCase;
import com.shopify.buy.model.internal.MarketingAttribution;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import static junit.framework.TestCase.*;

/**
 * Unit test for {@code MarketingAttribution}
 */
public class MarketingAttributionTest extends ShopifyAndroidTestCase {

    @Test
    public void testSerialization() throws JSONException {
        Gson gson = BuyClientFactory.createDefaultGson();

        final String appName = "myApp";
        MarketingAttribution marketingAttribution = new MarketingAttribution(appName);

        JSONObject json = new JSONObject();
        json.put("source", appName);
        json.put("medium", "android");

        String originalJson = gson.toJson(marketingAttribution);
        assertEquals(originalJson.length(), json.toString().length());
        assertTrue(originalJson.contains("\"medium\":\"android\""));
        assertTrue(originalJson.contains("\"source\":\"myApp\""));
    }

}
