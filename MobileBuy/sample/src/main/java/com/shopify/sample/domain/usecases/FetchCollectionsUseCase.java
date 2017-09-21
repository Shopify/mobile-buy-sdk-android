package com.shopify.sample.domain.usecases;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.shopify.sample.domain.model.Collection;
import com.shopify.sample.util.usecase.Callback1;
import com.shopify.sample.util.usecase.Cancelable;

import java.util.List;

public interface FetchCollectionsUseCase {

  Cancelable execute(@Nullable String cursor, int perPage, @NonNull Callback1<List<Collection>> callback);
}
