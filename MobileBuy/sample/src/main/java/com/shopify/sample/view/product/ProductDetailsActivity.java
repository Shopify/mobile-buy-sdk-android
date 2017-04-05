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

package com.shopify.sample.view.product;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;

import com.shopify.sample.R;
import com.shopify.sample.domain.model.ProductDetails;
import com.shopify.sample.domain.repository.RealCartRepository;
import com.shopify.sample.domain.repository.RealProductRepository;
import com.shopify.sample.presenter.product.ProductDetailsViewPresenter;
import com.shopify.sample.view.ScreenRouter;
import com.shopify.sample.view.cart.CartClickActionEvent;
import com.shopify.sample.view.widget.image.ImageGalleryView;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.shopify.sample.util.Util.checkNotNull;

public final class ProductDetailsActivity extends AppCompatActivity implements ProductDetailsViewPresenter.View {
  public static final String EXTRAS_PRODUCT_ID = "product_id";
  public static final String EXTRAS_PRODUCT_IMAGE_URL = "product_image_url";
  public static final String EXTRAS_PRODUCT_TITLE = "product_title";
  public static final String EXTRAS_PRODUCT_PRICE = "product_price";

  @BindView(R.id.root) View rootView;
  @BindView(R.id.swipe_refresh) SwipeRefreshLayout swipeRefreshLayoutView;
  @BindView(R.id.toolbar) Toolbar toolbarView;
  @BindView(R.id.image_gallery) ImageGalleryView imageGalleryView;
  @BindView(R.id.product_description) ProductDescriptionView productDescriptionView;

  private ProductDetailsViewPresenter presenter;

  @Override protected void onCreate(@Nullable final Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_product_details);
    ButterKnife.bind(this);

    String productId = getIntent().getStringExtra(EXTRAS_PRODUCT_ID);
    String productTitle = getIntent().getStringExtra(EXTRAS_PRODUCT_TITLE);
    String productImageUrl = getIntent().getStringExtra(EXTRAS_PRODUCT_IMAGE_URL);
    double productPrice = getIntent().getDoubleExtra(EXTRAS_PRODUCT_PRICE, 0);

    checkNotNull(productId, "productId == null");
    checkNotNull(productTitle, "productTitle == null");

    presenter = new ProductDetailsViewPresenter(productId, new RealProductRepository(), new RealCartRepository());

    setSupportActionBar(toolbarView);
    getSupportActionBar().setTitle(productTitle);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    imageGalleryView.renderImages(Arrays.asList(productImageUrl));

    swipeRefreshLayoutView.setOnRefreshListener(() -> presenter.fetchProduct());

    productDescriptionView.renderProduct(productTitle, productPrice);
    productDescriptionView.setOnAddToCartClickListener(() -> presenter.addToCart());
  }

  @Override public boolean onSupportNavigateUp() {
    finish();
    return true;
  }

  @Override public void showProgress(final long requestId) {
    swipeRefreshLayoutView.setRefreshing(true);
  }

  @Override public void hideProgress(final long requestId) {
    swipeRefreshLayoutView.setRefreshing(false);
  }

  @Override public void onAttachedToWindow() {
    super.onAttachedToWindow();
    presenter.attachView(this);
    presenter.fetchProduct();
  }

  @Override public void onDetachedFromWindow() {
    super.onDetachedFromWindow();
    presenter.detachView();
  }

  @Override public void showError(final long requestId, final Throwable t) {
    Snackbar.make(rootView, R.string.default_error, Snackbar.LENGTH_LONG).show();
  }

  @Override public void renderProduct(final ProductDetails product) {
    imageGalleryView.renderImages(product.images);
    productDescriptionView.renderProduct(product);
  }

  @Override public boolean onCreateOptionsMenu(final Menu menu) {
    MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.menu, menu);
    menu.findItem(R.id.cart).getActionView().setOnClickListener(v -> {
      ScreenRouter.route(this, new CartClickActionEvent());
    });
    return true;
  }

  @SuppressWarnings("unused")
  public static final class FabBehaviour extends CoordinatorLayout.Behavior<View> {

    public FabBehaviour() {
    }

    public FabBehaviour(final Context context, final AttributeSet attrs) {
      super(context, attrs);
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, View child, View dependency) {
      return dependency instanceof AppBarLayout;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, View child, View dependency) {
      FloatingActionButton actionButton = (FloatingActionButton) child;
      if (dependency.getHeight() / 3 < Math.abs(dependency.getTop())) {
        if (actionButton.isShown()) {
          actionButton.hide();
        }
      } else {
        if (!actionButton.isShown()) {
          actionButton.show();
        }
      }
      return false;
    }
  }
}
