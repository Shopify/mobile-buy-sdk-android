package com.shopify.sample.domain.usecases;

import android.support.annotation.NonNull;

import com.shopify.sample.core.UseCase.Callback1;
import com.shopify.sample.core.UseCase.Cancelable;
import com.shopify.sample.domain.model.ProductDetail;

public interface FetchProductDetailUseCase {

  Cancelable execute(@NonNull String productId, @NonNull Callback1<ProductDetail> callback);
}
