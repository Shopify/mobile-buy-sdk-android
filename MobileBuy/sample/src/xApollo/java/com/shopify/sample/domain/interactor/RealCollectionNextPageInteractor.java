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

package com.shopify.sample.domain.interactor;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.shopify.sample.SampleApplication;
import com.shopify.sample.domain.CollectionPageWithProductsQuery;
import com.shopify.sample.domain.model.Collection;
import com.shopify.sample.domain.repository.CollectionRepository;
import com.shopify.sample.domain.type.CollectionSortKeys;

import java.util.List;

import io.reactivex.Single;

public final class RealCollectionNextPageInteractor implements CollectionNextPageInteractor {
  private final CollectionRepository repository;

  public RealCollectionNextPageInteractor() {
    repository = new CollectionRepository(SampleApplication.apolloClient());
  }

  @NonNull @Override public Single<List<Collection>> execute(@Nullable final String cursor, final int perPage) {
    CollectionPageWithProductsQuery query = CollectionPageWithProductsQuery.builder()
      .perPage(perPage)
      .nextPageCursor(TextUtils.isEmpty(cursor) ? null : cursor)
      .collectionSortKey(CollectionSortKeys.TITLE)
      .build();

    return repository.nextPage(query).map(Converters::convertToCollections);
  }
}
