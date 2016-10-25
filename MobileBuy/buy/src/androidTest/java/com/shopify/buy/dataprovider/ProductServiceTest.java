package com.shopify.buy.dataprovider;

import android.support.test.runner.AndroidJUnit4;

import com.shopify.buy.extensions.ShopifyAndroidTestCase;
import com.shopify.buy.model.internal.CollectionListings;
import com.shopify.buy.model.internal.ProductListings;
import com.shopify.buy.model.internal.ProductTagsWrapper;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import java.lang.reflect.Field;

import retrofit2.Response;
import rx.Observable;

@RunWith(AndroidJUnit4.class)
public class ProductServiceTest extends ShopifyAndroidTestCase {

    @Test
    public void testPageSizes() throws Exception {
        final ProductRetrofitService productRetrofitService = Mockito.mock(ProductRetrofitService.class);
        Mockito.when(
            productRetrofitService.getProducts(
                Mockito.anyString(),
                Mockito.anyLong(),
                Mockito.anyString(),
                Mockito.anyString(),
                Mockito.anyInt(),
                Mockito.anyInt()
            )
        ).thenReturn(Observable.<Response<ProductListings>>empty());
        Mockito.when(
            productRetrofitService.getCollectionPage(
                Mockito.anyString(),
                Mockito.anyInt(),
                Mockito.anyInt()
            )
        ).thenReturn(Observable.<Response<CollectionListings>>empty());
        Mockito.when(
            productRetrofitService.getProductTagPage(
                Mockito.anyString(),
                Mockito.anyInt(),
                Mockito.anyLong(),
                Mockito.anyInt()
            )
        ).thenReturn(Observable.<Response<ProductTagsWrapper>>empty());


        final Field retrofitServiceField = ProductServiceDefault.class.getDeclaredField("retrofitService");
        retrofitServiceField.setAccessible(true);
        retrofitServiceField.set((((BuyClientDefault) buyClient).productService), productRetrofitService);

        buyClient.getProducts(1).subscribe();
        Mockito.verify(productRetrofitService).getProducts(Mockito.anyString(), Mockito.anyLong(), Mockito.anyString(), Mockito.anyString(), Mockito.eq(1), Mockito.eq(PRODUCT_PAGE_SIZE));

        buyClient.getCollections(1).subscribe();
        Mockito.verify(productRetrofitService).getCollectionPage(Mockito.anyString(), Mockito.eq(1), Mockito.eq(COLLECTION_PAGE_SIZE));

        buyClient.getProductTags(1, 2L).subscribe();
        Mockito.verify(productRetrofitService).getProductTagPage(Mockito.anyString(), Mockito.eq(1), Mockito.eq(2L), Mockito.eq(PRODUCT_TAG_PAGE_SIZE));
    }
}
