package com.shopify.sample.domain.usecases;

public interface UseCases {

  FetchCollectionsUseCase fetchCollections();

  FetchProductsUseCase fetchProducts();

  FetchProductDetailUseCase fetchProductDetail();
}
