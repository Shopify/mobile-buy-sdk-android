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

package com.shopify.buy.data;

import android.content.Context;

import com.google.gson.JsonObject;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.ResponseBody;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class MockResponseGenerator implements Interceptor {

    /*
        This class generates a json file containing the http responses that can be used for mock (non-network) testing.
        To generate the file, do the following:
            1. Set ShopifyAndroidTestCase.GENERATE_MOCK_RESPONSES = true
            2. Make sure shop.properties has mobilebuysdktestshop credentials
            3. Command line: adb shell rm /storage/emulated/0/Android/data/com.shopify.buy.test/files/MobileBuy.json
            4. Run the tests
            5. Command line: adb pull /storage/emulated/0/Android/data/com.shopify.buy.test/files/MobileBuy.json
            6. Now you have the mock responses in a new file called MobileBuy.json
            7. Open up MobileBuy.json, remove the last comma, and surround the entire file with '{' and '}' to create one giant valid json object.
     */

    private static final AtomicReference<String> currentTestName = new AtomicReference<>();
    private static final AtomicInteger currentTestRequestIndex = new AtomicInteger(0);

    public static void onNewTest(String name) {
        currentTestName.set(name);
        currentTestRequestIndex.set(0);
    }

    private final Context context;

    public MockResponseGenerator(Context context) {
        this.context = context;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        File file = new File(context.getExternalFilesDir(null), "MobileBuy.json");

        if (file == null) {
            return chain.proceed(chain.request());
        }

        if (!file.exists()) {
            file.createNewFile();
        }

        String jsonKey = currentTestName.get() + '_' + Integer.toString(currentTestRequestIndex.getAndIncrement());

        Request request = chain.request();
        Response response = chain.proceed(request);
        String bodyString = response.body().string();

        JsonObject jsonValue = new JsonObject();
        jsonValue.addProperty(MockResponder.KEY_BODY, bodyString);
        jsonValue.addProperty(MockResponder.KEY_CODE, response.code());
        jsonValue.addProperty(MockResponder.KEY_MESSAGE, response.message());

        FileOutputStream fOut = new FileOutputStream(file, true);
        PrintWriter writer = new PrintWriter(new OutputStreamWriter(fOut));
        writer.println("\"" + jsonKey + "\":" + jsonValue.toString() + ",");

        if (!isPartialAddressTestAdded.getAndSet(true)) {
            writer.println(TEST_WITH_PARTIAL_ADDRESS_RESPONSE);
        }

        writer.flush();
        fOut.flush();
        writer.close();
        fOut.close();

        MediaType contentType = response.body().contentType();
        return response.newBuilder().body(ResponseBody.create(contentType, bodyString)).build();
    }

    // iOS need this response for Apple Pay partial address testing
    private static final String TEST_WITH_PARTIAL_ADDRESS_RESPONSE = "\"testCheckoutWithAPartialAddress\": {\"body\": \"{\\\"checkout\\\":{\\\"created_at\\\":\\\"2015-09-21T16:19:13-04:00\\\",\\\"currency\\\":\\\"CAD\\\",\\\"customer_id\\\":null,\\\"email\\\":\\\"test@test.com\\\",\\\"location_id\\\":null,\\\"order_id\\\":null,\\\"requires_shipping\\\":true,\\\"reservation_time\\\":300,\\\"source_name\\\":\\\"mobile_app\\\",\\\"source_identifier\\\":\\\"26915715\\\",\\\"source_url\\\":null,\\\"taxes_included\\\":false,\\\"token\\\":\\\"c128f1b20ee5b0d9959ce2ac877447f7\\\",\\\"updated_at\\\":\\\"2015-09-21T16:19:13-04:00\\\",\\\"payment_due\\\":\\\"2230.99\\\",\\\"payment_url\\\":\\\"https:\\\\/\\\\/us-west-2-deposit.cs.shopify.com\\\\/sessions\\\",\\\"reservation_time_left\\\":300,\\\"subtotal_price\\\":\\\"2230.99\\\",\\\"total_price\\\":\\\"2230.99\\\",\\\"total_tax\\\":\\\"0.00\\\",\\\"order_status_url\\\":null,\\\"privacy_policy_url\\\":null,\\\"refund_policy_url\\\":null,\\\"terms_of_service_url\\\":null,\\\"web_url\\\":\\\"https:\\\\/\\\\/checkout.shopify.com\\\\/9575792\\\\/checkouts\\\\/c128f1b20ee5b0d9959ce2ac877447f7\\\",\\\"tax_lines\\\":[],\\\"line_items\\\":[{\\\"id\\\":\\\"94a1ad4d716a7790\\\",\\\"product_id\\\":2096063363,\\\"variant_id\\\":6030700419,\\\"sku\\\":\\\"\\\",\\\"vendor\\\":\\\"McCullough Group\\\",\\\"title\\\":\\\"Actinian Fur Hat\\\",\\\"variant_title\\\":\\\"Teal\\\",\\\"taxable\\\":false,\\\"requires_shipping\\\":true,\\\"price\\\":\\\"2230.99\\\",\\\"compare_at_price\\\":null,\\\"line_price\\\":\\\"2230.99\\\",\\\"properties\\\":{\\\"size\\\":\\\"large\\\",\\\"color\\\":\\\"red\\\"},\\\"quantity\\\":1,\\\"grams\\\":4000,\\\"fulfillment_service\\\":\\\"manual\\\"}],\\\"gift_cards\\\":[],\\\"shipping_rate\\\":null,\\\"billing_address\\\":{\\\"first_name\\\":\\\"---\\\",\\\"last_name\\\":\\\"---\\\",\\\"phone\\\":\\\"---\\\",\\\"address1\\\":\\\"---\\\",\\\"city\\\":\\\"Ottawa\\\",\\\"province\\\":\\\"Ontario\\\",\\\"province_code\\\":\\\"ON\\\",\\\"country\\\":\\\"Canada\\\",\\\"country_code\\\":\\\"CA\\\",\\\"zip\\\":\\\"K1N5T5\\\"},\\\"credit_card\\\":null,\\\"discount\\\":null}}\",\"code\": 201,\"message\": \"Created\"},";
    private static final AtomicBoolean isPartialAddressTestAdded = new AtomicBoolean(false);

}
