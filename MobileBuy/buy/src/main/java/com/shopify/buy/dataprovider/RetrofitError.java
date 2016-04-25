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

import java.io.IOException;

import retrofit2.Response;

public class RetrofitError extends IOException {

    private Response response = null;
    private Exception exception = null;
    private String message = null;
    private int code = 0;
    private Kind kind = Kind.UNEXPECTED;

    /** Identifies the event kind which triggered a {@link RetrofitError}. */
    public enum Kind {
        /** An {@link IOException} occurred while communicating to the server. */
        NETWORK,
        /** An exception was thrown while (de)serializing a body. */
        CONVERSION,
        /** A non-200 HTTP status code was received from the server. */
        HTTP,
        /**
         * An internal error occurred while attempting to execute a request. It is best practice to
         * re-throw this exception so your application crashes.
         */
        UNEXPECTED
    }

    public RetrofitError(Response response) {
        this.response = response;
        this.kind = Kind.HTTP;

        if (response != null) {
            this.code = response.code();
            this.message = response.message();
        }
    }

    public RetrofitError(Exception exception) {
        this.exception = exception;

        if (exception != null) {
            this.message = exception.getMessage();
        }
    }

    public RetrofitError(int code, String message) {
        this(code, message, Kind.UNEXPECTED);
    }

    public RetrofitError(int code, String message, Kind kind) {
        this.code = code;
        this.message = message;
        this.kind = kind;
    }

    /**
     * Returns a RetrofitError object given an {@link Exception}
     *
     * @param exception
     */
    public static RetrofitError exception(Exception exception) {
        return new RetrofitError(exception);
    }

    /**
     * Returns a RetrofitError object given a code, message
     *
     * @param code      the type of error
     * @param message   the description of the error
     */
    public static RetrofitError error(int code, String message) {
        return new RetrofitError(code, message);
    }

    public Response getResponse() {
        return response;
    }

    public Exception getException() {
        return exception;
    }

    public String getMessage() {
        return message;
    }

    public int getCode() {
        return code;
    }

    public Kind getKind() {
        return kind;
    }

}
