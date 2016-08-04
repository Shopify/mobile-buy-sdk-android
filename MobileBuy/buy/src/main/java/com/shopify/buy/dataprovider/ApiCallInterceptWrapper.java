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

import com.shopify.buy.interceptor.RequestInterceptorResponse;

import rx.Observable;
import rx.functions.Func1;
import rx.functions.Func2;

final class ApiCallInterceptWrapper {

    static <T, I1, I2> Observable<T> wrap(final Observable<T> apiRequest, final RequestInterceptor<I1, T> requestInterceptor, final ResponseInterceptor<I2, T> responseInterceptor) {
        return requestInterceptor.intercept(responseInterceptor.intercept(apiRequest));
    }

    static final class RequestInterceptor<I, T> {

        private final I requestInterceptor;

        private final Func1<I, Observable<RequestInterceptorResponse<T>>> requestInterceptorCall;

        public RequestInterceptor(final I requestInterceptor, final Func1<I, Observable<RequestInterceptorResponse<T>>> requestInterceptorCall) {
            this.requestInterceptor = requestInterceptor;
            this.requestInterceptorCall = requestInterceptorCall;
        }

        private Observable<T> intercept(final Observable<T> originalObservable) {
            if (requestInterceptor != null) {
                return requestInterceptorCall
                    .call(requestInterceptor)
                    .singleOrDefault(new RequestInterceptorResponse<T>(null, true))
                    .compose(new InterceptorResponseTransformer<>(originalObservable));
            } else {
                return originalObservable;
            }
        }
    }

    static final class ResponseInterceptor<I, T> {

        private final I responseInterceptor;

        private final Func2<I, Observable<T>, Observable<T>> responseInterceptorCall;

        public ResponseInterceptor(final I responseInterceptor, final Func2<I, Observable<T>, Observable<T>> responseInterceptorCall) {
            this.responseInterceptor = responseInterceptor;
            this.responseInterceptorCall = responseInterceptorCall;
        }

        private Observable<T> intercept(final Observable<T> originalObservable) {
            if (responseInterceptor != null) {
                return originalObservable.compose(new Observable.Transformer<T, T>() {
                    @Override
                    public Observable<T> call(final Observable<T> observable) {
                        final Observable<T> resultObservable = responseInterceptorCall.call(responseInterceptor, observable);
                        if (resultObservable != null) {
                            return resultObservable;
                        } else {
                            return observable;
                        }
                    }
                });
            } else {
                return originalObservable;
            }
        }
    }

    private static final class InterceptorResponseTransformer<T> implements Observable.Transformer<RequestInterceptorResponse<T>, T> {

        private final Observable<T> originalObservable;

        InterceptorResponseTransformer(final Observable<T> originalObservable) {
            this.originalObservable = originalObservable;
        }

        @Override
        public Observable<T> call(final Observable<RequestInterceptorResponse<T>> interceptorObservable) {
            return interceptorObservable.flatMap(new Func1<RequestInterceptorResponse<T>, Observable<T>>() {
                @Override
                public Observable<T> call(RequestInterceptorResponse<T> interceptorResponse) {
                    if (interceptorResponse.getPayload() != null) {
                        final Observable<T> interceptorResponseObservable = Observable.just(interceptorResponse.getPayload());
                        if (interceptorResponse.isStale()) {
                            return Observable.concat(interceptorResponseObservable, originalObservable);
                        } else {
                            return interceptorResponseObservable;
                        }
                    } else {
                        if (interceptorResponse.isStale()) {
                            return originalObservable;
                        } else {
                            return Observable.empty();
                        }
                    }
                }
            });
        }
    }
}
