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

package com.shopify.buy.dataprovider;

import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import retrofit.RetrofitError;

public class BuyError {

    private RetrofitError retrofitError = null;
    private Exception exception = null;
    private String message = null;
    private String code = null;
    private Map<String, Object> options = null;

    public BuyError(RetrofitError retrofitError) {
        this.retrofitError = retrofitError;

        if (retrofitError != null) {
            this.message = retrofitError.getMessage();
        }
    }

    public BuyError(Exception exception) {
        this.exception = exception;

        if (exception != null) {
            this.message = exception.getMessage();
        }
    }

    public BuyError(String message) {
        this.message = message;
    }

    public BuyError(String code, String message, Map<String, Object> options) {
        this.code = code;
        this.message = message;
        this.options = options;
    }

    /**
     * Returns a BuyError object given an {@link Exception}
     *
     * @param exception
     */
    public static BuyError exception(Exception exception) {
        return new BuyError(exception);
    }

    /**
     * Returns a BuyError object given a {@link RetrofitError}
     *
     * @param retrofitError
     */
    public static BuyError retrofitError(RetrofitError retrofitError) {
        return new BuyError(retrofitError);
    }

    /**
     * Returns a BuyError object given a message
     *
     * @param message
     */
    public static BuyError genericError(String message) {
        return new BuyError(message);
    }

    /**
     * Returns a BuyError object given a code, message, and options
     *
     * @param code      the type of error
     * @param message   the description of the error
     * @param options   error details to help resolve the error
     */
    public static BuyError error(String code, String message, Map<String, Object> options) {
        return new BuyError(code, message, options);
    }

    /**
     * Returns true if the BuyError is a network error
     * If {@link #retrofitError} is null, false is returned
     */
    public boolean isNoNetworkError() {
        if (retrofitError != null) {
            return RetrofitError.Kind.NETWORK.equals(retrofitError.getKind());
        }
        return false;
    }

    /**
     * Returns network status code of BuyError
     * If {@link #retrofitError} is null, 0 is returned
     */
    public int getNetworkStatusCode() {
        if (retrofitError != null && retrofitError.getResponse() != null) {
            return retrofitError.getResponse().getStatus();
        }
        return 0;
    }

    public RetrofitError getRetrofitError() {
        return retrofitError;
    }

    public Exception getException() {
        return exception;
    }

    public String getMessage() {
        return message;
    }

    public String getCode() {
        return code;
    }

    public Map<String, Object> getOptions() {
        return options;
    }

    /**
     * Returns true if the email specified is already taken when trying to sign up given a {@link RetrofitError}
     *
     * @param retrofitError
     * @param email
     */
    public static boolean isEmailAlreadyTakenError(RetrofitError retrofitError, String email) {
        return isEmailAlreadyTakenError(BuyClient.getErrorBody(retrofitError), email);
    }

    /**
     * Returns true if the email specified is already taken when trying to sign up given a json string error
     *
     * @param jsonString
     * @param email
     */
    public static boolean isEmailAlreadyTakenError(String jsonString, String email) {

        if (TextUtils.isEmpty(jsonString) || TextUtils.isEmpty(email)) {
            return false;
        }

        List<BuyError> list = errorsForCustomer(jsonString);

        for (int i = 0; i < list.size(); i++) {

            BuyError error = list.get(i);

            if (error != null && !TextUtils.isEmpty(error.getCode()) && error.getOptions() != null && error.getOptions().containsKey("value")) {

                if (error.getCode().equals("taken") && error.getOptions().get("value").toString().equals(email)) {
                    return true;
                }

            }
        }

        return false;

    }

    /**
     * Returns a list of BuyError objects for customer errors specifically given a {@link RetrofitError}
     * {@link #getCode()}, {@link #getMessage()}, and {@link #getOptions()} can be called on each BuyError object
     * to retrieve the {@link #code}, {@link #message}, and {@link #options} respectively
     *
     * If a {@link JSONException} is thrown, a list of one BuyError object consisting the exception error is returned
     * {@link #getException()} can be called on the BuyError object to retrieve the {@link JSONException} object
     * {@link #getMessage()} can be called on the BuyError object to retrieve the {@link #message}
     *
     * @param retrofitError
     *
     * @return list of BuyError objects for customer errors
     */
    public static List<BuyError> errorsForCustomer(RetrofitError retrofitError) {
        return errorsForCustomer(BuyClient.getErrorBody(retrofitError));
    }

    /**
     * Returns a list of BuyError objects for customer errors specifically given a json string
     * {@link #getCode()}, {@link #getMessage()}, and {@link #getOptions()} can be called on each BuyError object
     * to retrieve the {@link #code}, {@link #message}, and {@link #options} respectively
     *
     * If a {@link JSONException} is thrown, a list of one BuyError object consisting the exception error is returned
     * {@link #getException()} can be called on the BuyError object to retrieve the {@link JSONException} object
     * {@link #getMessage()} can be called on the BuyError object to retrieve the {@link #message}
     *
     * @param jsonString
     *
     * @return list of BuyError objects for customer errors
     */
    public static List<BuyError> errorsForCustomer(String jsonString) {
        return parseErrors(jsonString, "errors", "customer");
    }

    /**
     * Returns a list of BuyError objects for checkout errors specifically given a {@link RetrofitError}
     * {@link #getCode()}, {@link #getMessage()}, and {@link #getOptions()} can be called on each BuyError object
     * to retrieve the {@link #code}, {@link #message}, and {@link #options} respectively
     *
     * If a {@link JSONException} is thrown, a list of one BuyError object consisting the exception error is returned
     * {@link #getException()} can be called on the BuyError object to retrieve the {@link JSONException} object
     * {@link #getMessage()} can be called on the BuyError object to retrieve the {@link #message}
     *
     * @param retrofitError
     *
     * @return list of BuyError objects for checkout errors
     */
    public static List<BuyError> errorsForCheckout(RetrofitError retrofitError) {
        return errorsForCheckout(BuyClient.getErrorBody(retrofitError));
    }

    /**
     * Returns a list of BuyError objects for checkout errors specifically given a json string
     * {@link #getCode()}, {@link #getMessage()}, and {@link #getOptions()} can be called on each BuyError object
     * to retrieve the {@link #code}, {@link #message}, and {@link #options} respectively
     *
     * If a {@link JSONException} is thrown, a list of one BuyError object consisting the exception error is returned
     * {@link #getException()} can be called on the BuyError object to retrieve the {@link JSONException} object
     * {@link #getMessage()} can be called on the BuyError object to retrieve the {@link #message}
     *
     * @param jsonString
     *
     * @return list of BuyError objects for checkout errors
     */
    public static List<BuyError> errorsForCheckout(String jsonString) {
        return parseErrors(jsonString, "errors", "checkout");
    }

    /**
     * Returns a list of BuyError objects for checkout line item errors specifically given a {@link RetrofitError}
     * Each BuyError object of the list corresponds to the checkout line items respectively
     * If a line item does not consist an error, it will be null in the list as a placeholder
     *
     * {@link #getCode()}, {@link #getMessage()}, and {@link #getOptions()} can be called on each BuyError object
     * to retrieve the {@link #code}, {@link #message}, and {@link #options} respectively
     *
     * If a {@link JSONException} is thrown, a list of one BuyError object consisting the exception error is returned
     * {@link #getException()} can be called on the BuyError object to retrieve the {@link JSONException} object
     * {@link #getMessage()} can be called on the BuyError object to retrieve the {@link #message}
     *
     * @param retrofitError
     *
     * @return list of BuyError objects corresponding to the checkout inline items
     */
    public static List<BuyError> errorsForLineItems(RetrofitError retrofitError) {
        return errorsForLineItems(BuyClient.getErrorBody(retrofitError));
    }

    /**
     * Returns a list of BuyError objects for checkout line item errors specifically given a json string
     * Each BuyError object of the list corresponds to the checkout line items respectively
     * If a line item does not consist an error, it will be null in the list as a placeholder
     *
     * {@link #getCode()}, {@link #getMessage()}, and {@link #getOptions()} can be called on each BuyError object
     * to retrieve the {@link #code}, {@link #message}, and {@link #options} respectively
     *
     * If a {@link JSONException} is thrown, a list of one BuyError object consisting the exception error is returned
     * {@link #getException()} can be called on the BuyError object to retrieve the {@link JSONException} object
     * {@link #getMessage()} can be called on the BuyError object to retrieve the {@link #message}
     *
     * @param jsonString
     *
     * @return list of BuyError objects corresponding to the checkout inline items
     */
    public static List<BuyError> errorsForLineItems(String jsonString) {
        return parseErrors(jsonString, "errors", "checkout", "line_item");
    }

    /**
     * Returns a list of BuyError objects given a json string
     * Keys can be provided as an argument to parse specific nested sections of a json
     *
     * {@link #getCode()}, {@link #getMessage()}, and {@link #getOptions()} can be called on each BuyError object
     * to retrieve the {@link #code}, {@link #message}, and {@link #options} respectively
     *
     * If a {@link JSONException} is thrown, a list of one BuyError object consisting the exception error is returned
     * {@link #getException()} can be called on the BuyError object to retrieve the {@link JSONException} object
     * {@link #getMessage()} can be called on the BuyError object to retrieve the {@link #message}
     *
     * @param jsonString
     * @param keys
     *
     * @return list of BuyError objects
     */
    public static List<BuyError> parseErrors(String jsonString, String... keys) {

        List<BuyError> list = new ArrayList<>();

        if (TextUtils.isEmpty(jsonString)) {
            return list;
        }

        try {

            JSONObject jsonObject = new JSONObject(jsonString);

            for (int i = 0; i < keys.length; i++) {

                if (jsonObject.has(keys[i])) {

                    if (jsonObject.get(keys[i]) instanceof JSONObject) {
                        jsonObject = jsonObject.getJSONObject(keys[i]);
                    } else if (jsonObject.get(keys[i]) instanceof JSONArray) {
                        JSONArray jsonArray = jsonObject.getJSONArray(keys[i]);
                        jsonObject = new JSONObject().put(keys[i], jsonArray);
                    }

                }

            }

            parseJsonObjectForErrors(jsonObject, list);

        } catch (JSONException e) {
            list.clear();
            list.add(exception(e));
        }

        return list;
    }

    /**
     * Helper methods
     */

    /**
     * Extracts all key, value pairs of a {@link JSONObject} and returns it as a map
     */
    private static Map<String, Object> mapJson(JSONObject jsonObject) throws JSONException {

        Map<String, Object> map = new HashMap<>();

        Iterator<String> iterator = jsonObject.keys();

        while (iterator.hasNext()) {
            String key = iterator.next();
            map.put(key, jsonObject.get(key));
        }

        return map;

    }

    /**
     * Extracts code, message, and options from a {@link JSONObject} and returns it as a BuyError object
     */
    private static BuyError parseJsonObjectForError(JSONObject jsonObject) throws JSONException{

        String code = jsonObject.has("code") ? jsonObject.getString("code") : null;
        String message = jsonObject.has("message") ? jsonObject.getString("message") : null;
        Map<String, Object> options = jsonObject.has("options") ? mapJson(jsonObject.getJSONObject("options")) : null;

        return error(code, message, options);

    }

    /**
     * Recursively extracts BuyError objects with code, message, and options into a list given a {@link JSONObject}
     */
    private static void parseJsonObjectForErrors(JSONObject jsonObject, List<BuyError> list) throws JSONException {

        if (jsonObject.has("code")) {
            list.add (parseJsonObjectForError(jsonObject));
        }

        Iterator<String> iterator = jsonObject.keys();

        while (iterator.hasNext()) {

            String key = iterator.next();

            if (jsonObject.get(key) instanceof JSONObject) {
                parseJsonObjectForErrors(jsonObject.getJSONObject(key), list);
            } else if (jsonObject.get(key) instanceof JSONArray) {
                parseJsonArrayForErrors(jsonObject.getJSONArray(key), list);
            }

        }

    }

    /**
     * Recursively extracts BuyError objects with code, message, and options into a list given a {@link JSONArray}
     */
    private static void parseJsonArrayForErrors(JSONArray jsonArray, List<BuyError> list) throws JSONException {

        for (int i = 0; i < jsonArray.length(); i++) {

            if (jsonArray.get(i) instanceof JSONObject) {
                parseJsonObjectForErrors(jsonArray.getJSONObject(i), list);
            } else if (jsonArray.get(i) instanceof JSONArray) {
                parseJsonArrayForErrors(jsonArray.getJSONArray(i), list);
            } else if (jsonArray.get(i).toString().equals("null")) {
                list.add(null);
            }

        }

    }

}
