package com.shopify.sample.presenter.product;

import android.support.annotation.NonNull;

import com.shopify.sample.interactor.product.FetchProductDetails;
import com.shopify.sample.mvp.BaseViewPresenter;
import com.shopify.sample.util.WeakObserver;

import io.reactivex.android.schedulers.AndroidSchedulers;

import static com.shopify.sample.util.Util.checkNotNull;

public final class ProductDetailsViewPresenter extends BaseViewPresenter<ProductDetailsViewPresenter.View> {
  public static final int REQUEST_ID_PRODUCT_DETAILS = 1;
  private final String productId;
  private final FetchProductDetails fetchProductDetails;

  public ProductDetailsViewPresenter(@NonNull final String productId, final FetchProductDetails fetchProductDetails) {
    checkNotNull(productId, "productId == null");
    this.productId = productId;
    this.fetchProductDetails = fetchProductDetails;
  }

  public void fetchProduct() {
    if (isViewAttached()) {
      view().showProgress(REQUEST_ID_PRODUCT_DETAILS);
      registerRequest(
        REQUEST_ID_PRODUCT_DETAILS,
        fetchProductDetails.call(productId)
          .toObservable()
          .observeOn(AndroidSchedulers.mainThread())
          .subscribeWith(
            WeakObserver.<ProductDetailsViewPresenter, Product>forTarget(this)
              .delegateOnNext(ProductDetailsViewPresenter::onProductDetailsResponse)
              .delegateOnError(ProductDetailsViewPresenter::onProductDetailsError)
              .create()
          )
      );
    }
  }

  private void onProductDetailsResponse(final Product product) {
    if (isViewAttached()) {
      view().hideProgress(REQUEST_ID_PRODUCT_DETAILS);
      view().renderProduct(product);
    }
  }

  private void onProductDetailsError(final Throwable t) {
    if (isViewAttached()) {
      view().hideProgress(REQUEST_ID_PRODUCT_DETAILS);
      view().showError(REQUEST_ID_PRODUCT_DETAILS, t);
    }
  }


  public interface View extends com.shopify.sample.mvp.View {
    void renderProduct(Product product);
  }
}
