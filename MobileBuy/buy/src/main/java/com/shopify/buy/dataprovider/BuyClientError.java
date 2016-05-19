/*
 *   The MIT License (MIT)
 *
 *   Copyright (c) 2015 Shopify Inc.
 *
 *   Permission is hereby granted, free of charge, to any person obtaining a copy
 *   of this software and associated documentation files (the "Software"), to deal
 *   in the Software without restriction, including without limitation the rights
 *   to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *   copies of the Software, and to permit persons to whom the Software is
 *   furnished to do so, subject to the following conditions:
 *
 *   The above copyright notice and this permission notice shall be included in
 *   all copies or substantial portions of the Software.
 *
 *   THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *   IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *   FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *   AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *   LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *   OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 *   THE SOFTWARE.
 */
package com.shopify.buy.dataprovider;

import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import retrofit2.Response;

/**
 * Exception thrown when API call failed, and be returned in {@link Callback#failure(BuyClientError)}
 */
public final class BuyClientError extends RuntimeException {

    /**
     * Represents any errors returning from the backend
     */
    public static final int ERROR_TYPE_API = -2;

    /**
     * Represents any network related errors
     */
    public static final int ERROR_TYPE_NETWORK = -1;

    /**
     * Represent any unknown errors
     */
    public static final int ERROR_TYPE_UNKNOWN = 0;

    private final int type;

    private final Response retrofitResponse;

    private final JSONObject errorsRootJsonObject;

    public BuyClientError(final Throwable throwable) {
        super(throwable);

        retrofitResponse = null;
        if (throwable instanceof IOException) {
            type = ERROR_TYPE_NETWORK;
            errorsRootJsonObject = null;
        } else {
            type = ERROR_TYPE_UNKNOWN;
            errorsRootJsonObject = null;
        }
    }

    public BuyClientError(final Response retrofitResponse) {
        this.retrofitResponse = retrofitResponse;
        if (retrofitResponse != null) {
            type = ERROR_TYPE_API;
            errorsRootJsonObject = parseRetrofitErrorResponse(retrofitResponse);
        } else {
            type = ERROR_TYPE_UNKNOWN;
            errorsRootJsonObject = null;
        }
    }

    /**
     * Returns the type of error, on of: {@link BuyClientError#ERROR_TYPE_API}, {@link BuyClientError#ERROR_TYPE_NETWORK}, {@link BuyClientError#ERROR_TYPE_UNKNOWN}
     *
     * @return error type
     */
    public int getType() {
        return type;
    }

    /**
     * Returns raw retrofit response
     *
     * @return retrofit response
     */
    public Response getRetrofitResponse() {
        return retrofitResponse;
    }

    /**
     * Returns raw retrofit response error body
     *
     * @return retrofit response error body
     */
    public String getRetrofitErrorBody() {
        if (retrofitResponse != null) {
            try {
                return retrofitResponse.errorBody().string();
            } catch (Exception e) {
                // ignore
            }
        }
        return "";
    }

    /**
     * Returns parsed map of errors ({@code key = "code", value = "message"}).<br>
     * This is the util method that helps to parse json returned in the error body by specified path to the array of errors.<br>
     * For instance, to get all errors related to the customer email, {@code getErrors("customer", "email")} should be called.
     *
     * @param path of nested json fields to array of errors
     * @return map of error codes and messages if found, {@code null} otherwise
     */
    public Map<String, String> getErrors(final String... path) {
        if (errorsRootJsonObject == null) {
            return null;
        }

        if (path == null || path.length == 0) {
            return null;
        }

        JSONObject rootJsonObject = errorsRootJsonObject;
        final Iterator<String> pathIterator = Arrays.asList(path).iterator();
        while (pathIterator.hasNext()) {
            final String pathElement = pathIterator.next();
            try {
                if (rootJsonObject.has(pathElement)) {
                    if (!pathIterator.hasNext()) {
                        return parseErrorMessages(rootJsonObject.getJSONArray(pathElement));
                    } else {
                        rootJsonObject = rootJsonObject.getJSONObject(pathElement);
                    }
                } else {
                    return null;
                }
            } catch (Exception e) {
                return null;
            }
        }

        return null;
    }

    private JSONObject parseRetrofitErrorResponse(final Response retrofitResponse) {
        final String jsonStr;
        try {
            jsonStr = retrofitResponse.errorBody().string();
        } catch (Throwable e) {
            // ignore
            return null;
        }

        if (TextUtils.isEmpty(jsonStr)) {
            return null;
        }

        try {
            final JSONObject jsonObject = new JSONObject(jsonStr);
            if (jsonObject.has("errors")) {
                return jsonObject.getJSONObject("errors");
            }
        } catch (JSONException e) {
            //ignore
        }

        return null;
    }

    private Map<String, String> parseErrorMessages(final JSONArray errors) throws JSONException {
        final Map<String, String> result = new LinkedHashMap<>();
        for (int i = 0; i < errors.length(); i++) {
            final JSONObject error = errors.getJSONObject(i);
            result.put(error.getString("code"), error.getString("message"));
        }
        return result;
    }
}
