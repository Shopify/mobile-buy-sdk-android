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

package com.shopify.sample.presenter.product;

import android.support.annotation.NonNull;

import com.shopify.sample.interactor.product.FetchProductDetails;
import com.shopify.sample.model.cart.CartItem;
import com.shopify.sample.model.cart.CartManager;
import com.shopify.sample.mvp.BaseViewPresenter;
import com.shopify.sample.util.WeakObserver;

import io.reactivex.android.schedulers.AndroidSchedulers;

import static com.shopify.sample.util.Util.checkNotNull;
import static com.shopify.sample.util.Util.firstItem;
import static com.shopify.sample.util.Util.mapItems;

public final class ProductDetailsViewPresenter extends BaseViewPresenter<ProductDetailsViewPresenter.View> {
  public static final int REQUEST_ID_PRODUCT_DETAILS = 1;
  private final String productId;
  private final FetchProductDetails fetchProductDetails;
  private Product product;

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

  public void addToCart() {
    if (product == null) {
      return;
    }

    Product.Variant firstVariant = checkNotNull(firstItem(product.variants), "can't find default variant");
    CartItem cartItem = new CartItem(product.id, firstVariant.id, product.title, firstVariant.price,
      mapItems(firstVariant.selectedOptions, it -> new CartItem.Option(it.name, it.value)), firstItem(product.images));
    CartManager.instance().addCartItem(cartItem);
  }

  private void onProductDetailsResponse(final Product product) {
    this.product = product;
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
