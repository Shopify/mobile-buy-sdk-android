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

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.Protocol;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.ResponseBody;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import retrofit.RetrofitError;
import retrofit.mime.TypedString;

public class MockResponder implements Interceptor {

    private static final String FILE_NAME = "mocked_responses.json";

    public static final String KEY_CODE = "code";
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_BODY= "body";

    private static final AtomicReference<String> currentTestName = new AtomicReference<>();
    private static final AtomicInteger currentTestRequestIndex = new AtomicInteger(0);

    public static void onNewTest(String name) {
        currentTestName.set(name);
        currentTestRequestIndex.set(0);
    }

    private final JsonObject data;

    public MockResponder(Context context) {
        InputStream in = null;
        try {
            in = context.getAssets().open(FILE_NAME);
        } catch (IOException e) {
            e.printStackTrace();
        }
        data = (JsonObject) new JsonParser().parse(new InputStreamReader(in));
    }

    @Override
    public Response intercept(Interceptor.Chain chain) throws IOException {
        Request request = chain.request();
        String key = currentTestName.get() + '_' + Integer.toString(currentTestRequestIndex.getAndIncrement());

        JsonElement element = data.get(key);
        JsonObject responseJson = element.getAsJsonObject();
        int code = responseJson.get(KEY_CODE).getAsInt();
        String message = responseJson.get(KEY_MESSAGE).getAsString();
        String body = responseJson.get(KEY_BODY).getAsString();

        if (code >= 400) {
            retrofit.client.Response retrofitResponse = new retrofit.client.Response(request.urlString(), code, "reason", Collections.EMPTY_LIST, new TypedString(body));
            throw RetrofitError.httpError(request.urlString(), retrofitResponse, null, null);
        }

        MediaType contentType = MediaType.parse("application/json; charset=utf-8");
        return new Response.Builder()
                .protocol(Protocol.HTTP_1_1)
                .code(code)
                .request(request)
                .body(ResponseBody.create(contentType, body))
                .message(message)
                .addHeader("key", "value")
                .build();
    }

}
