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

import com.shopify.buy.utils.CollectionUtils;

import java.util.List;

import rx.Observable;
import rx.functions.Func1;

/**
 * Transformer that returns first item in the list or default value if it's empty
 *
 * @param <T> type of items in the list
 */
final class FirstListItemOrDefaultTransformer<T> implements Observable.Transformer<List<T>, T> {

    final T defaultValue;

    FirstListItemOrDefaultTransformer() {
        defaultValue = null;
    }

    FirstListItemOrDefaultTransformer(T defaultValue) {
        this.defaultValue = defaultValue;
    }

    @Override
    public Observable<T> call(Observable<List<T>> collectionObservable) {
        return collectionObservable.map(new Func1<List<T>, T>() {
            @Override
            public T call(List<T> collection) {
                return !CollectionUtils.isEmpty(collection) ? collection.get(0) : defaultValue;
            }
        });
    }
}
