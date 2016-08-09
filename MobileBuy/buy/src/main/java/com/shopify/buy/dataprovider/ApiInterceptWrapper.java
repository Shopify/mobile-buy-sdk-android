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

import rx.Observable;
import rx.functions.Func2;

final class ApiInterceptWrapper {

    static <T, I> Observable<T> wrap(final Observable<T> apiRequest, final I requestInterceptor, final I responseInterceptor, final InterceptorCall<I, T> interceptorCall) {
        Observable<T> observable = apiRequest;
        if (responseInterceptor != null) {
            observable = interceptorCall.intercept(responseInterceptor, observable);
        }
        if (requestInterceptor != null) {
            observable = interceptorCall.intercept(requestInterceptor, observable);
        }
        return observable;
    }

    static abstract class InterceptorCall<I, T> implements Func2<I, Observable<T>, Observable<T>> {

        @Override
        public abstract Observable<T> call(I interceptor, Observable<T> observable);

        private Observable<T> intercept(final I interceptor, final Observable<T> originalObservable) {
            final Func2<I, Observable<T>, Observable<T>> interceptorCall = this;
            return originalObservable.compose(new Observable.Transformer<T, T>() {
                @Override
                public Observable<T> call(final Observable<T> observable) {
                    if (interceptor != null) {
                        final Observable<T> resultObservable = interceptorCall.call(interceptor, observable);
                        if (resultObservable != null) {
                            return resultObservable;
                        }
                    }
                    return observable;
                }
            });
        }
    }
}
