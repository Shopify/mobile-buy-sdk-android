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
package com.shopify.buy.interceptor;

/**
 * Represents response returned by request interceptor. If {@code payload} is {@code null} it will be ignored
 * and regular API call executed
 *
 * @param <T> type of payload
 */
public final class RequestInterceptorResponse<T> {

    private final T payload;

    private final boolean stale;

    /**
     * Creates requests interceptor response
     *
     * @param payload the response payload, null payload will be ignored and regular API call executed
     * @param stale   indicates if the payload is out of date and sync is required
     */
    public RequestInterceptorResponse(final T payload, final boolean stale) {
        this.payload = payload;
        this.stale = stale;
    }

    /**
     * Returns the response payload
     *
     * @return response payload
     */
    public T getPayload() {
        return payload;
    }

    /**
     * Return stale flag that indicates response payload is out of date and sync with is required
     *
     * @return stale flag
     */
    public boolean isStale() {
        return stale;
    }
}
