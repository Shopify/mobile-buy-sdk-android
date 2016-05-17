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

import retrofit2.Response;
import rx.functions.Action1;

/**
 * Handler that checks if http response is successful and if not throws {@link RetrofitError}.
 * By default Retrofit doesn't propagate {@code .onError()} even if http response is not
 * from 200 series. In order to change such behavior we must explicitly throw exception in case
 * if http response isn't successful. To use this handler chain it right to observable returned
 * by Retrofit service via {@code .doOnNext(new RetrofitHttpSuccessStatusCodeHandler<>())}.
 */
final class RetrofitSuccessHttpStatusCodeHandler<T extends Response<?>> implements Action1<T> {

    final int[] httpSuccessCodes;

    RetrofitSuccessHttpStatusCodeHandler() {
        this.httpSuccessCodes = null;
    }

    RetrofitSuccessHttpStatusCodeHandler(final int[] httpSuccessCodes) {
        this.httpSuccessCodes = httpSuccessCodes;
    }

    @Override
    public void call(final T response) {
        if (httpSuccessCodes != null) {
            // if http status code one of the success codes don't throw exception
            for (int successCode : httpSuccessCodes) {
                if (response.code() == successCode) {
                    return;
                }
            }
            throw new BuyClientError(response, null);
        } else if (!response.isSuccessful()) {
            throw new BuyClientError(response, null);
        }
    }
}
