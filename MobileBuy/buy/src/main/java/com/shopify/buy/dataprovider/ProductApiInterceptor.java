package com.shopify.buy.dataprovider;

import com.shopify.buy.model.Collection;
import com.shopify.buy.model.Product;

import java.util.List;
import java.util.Set;

import rx.Observable;

/**
 * Represents request interceptor for Product API calls {@link com.shopify.buy.dataprovider.ProductService}
 */
public interface ProductApiInterceptor {

    /**
     * Intercepts request to:
     * {@link com.shopify.buy.dataprovider.ProductService#getProducts(int)}
     * {@link com.shopify.buy.dataprovider.ProductService#getProducts(int, Set)}
     * {@link com.shopify.buy.dataprovider.ProductService#getProducts(int, Long, Set, Collection.SortOrder)}
     *
     * @param page               the 1-based page index
     * @param collectionId       the collectionId that we want to fetch products for
     * @param tags               set of tags which each product must contain
     * @param sortOrder          the sort order of products for the specified collection
     * @param originalObservable original request observable
     * @return modified or the same version of original request observable
     */
    Observable<List<Product>> getProducts(int page, Long collectionId, Set<String> tags, Collection.SortOrder sortOrder, Observable<List<Product>> originalObservable);

    /**
     * Intercepts request to {@link com.shopify.buy.dataprovider.ProductService#getProductByHandle(String)}
     *
     * @param handle             the handle for the product to fetch
     * @param originalObservable original request observable
     * @return modified or the same version of original request observable
     */
    Observable<Product> getProductByHandle(String handle, Observable<Product> originalObservable);

    /**
     * Intercepts request to {@link com.shopify.buy.dataprovider.ProductService#getProduct(Long)}
     *
     * @param productId          the productId for the product to fetch
     * @param originalObservable original request observable
     * @return modified or the same version of original request observable
     */
    Observable<Product> getProduct(Long productId, Observable<Product> originalObservable);

    /**
     * Intercepts request to {@link com.shopify.buy.dataprovider.ProductService#getProducts(List)}
     *
     * @param productIds         a List of the productIds to fetch
     * @param originalObservable original request observable
     * @return modified or the same version of original request observable
     */
    Observable<List<Product>> getProducts(List<Long> productIds, Observable<List<Product>> originalObservable);

    /**
     * Intercepts request to {@link com.shopify.buy.dataprovider.ProductService#getCollectionByHandle(String)}
     *
     * @param handle             the handle for the collection to fetch
     * @param originalObservable original request observable
     * @return modified or the same version of original request observable
     */
    Observable<Collection> getCollectionByHandle(String handle, Observable<Collection> originalObservable);

    /**
     * Intercepts request to {@link com.shopify.buy.dataprovider.ProductService#getCollections(int)}
     *
     * @param page               the 1-based page index
     * @param originalObservable original request observable
     * @return modified or the same version of original request observable
     */
    Observable<List<Collection>> getCollections(int page, Observable<List<Collection>> originalObservable);

    /**
     * Intercepts request to {@link com.shopify.buy.dataprovider.ProductService#getCollections(List)}
     *
     * @param collectionIds      a List of the ids to fetch
     * @param originalObservable original request observable
     * @return modified or the same version of original request observable
     */
    Observable<List<Collection>> getCollections(List<Long> collectionIds, Observable<List<Collection>> originalObservable);
}
