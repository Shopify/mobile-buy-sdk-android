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

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleRegistry;
import android.arch.lifecycle.LifecycleRegistryOwner;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shopify.sample.R;
import com.shopify.sample.domain.model.ProductDetails;
import com.shopify.sample.view.ScreenRouter;
import com.shopify.sample.view.cart.CartClickActionEvent;
import com.shopify.sample.view.widget.image.ImageGalleryView;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.shopify.sample.util.Util.checkNotNull;

public final class ProductDetailsActivity extends AppCompatActivity implements LifecycleRegistryOwner {
  public static final String EXTRAS_PRODUCT_ID = "product_id";
  public static final String EXTRAS_PRODUCT_IMAGE_URL = "product_image_url";
  public static final String EXTRAS_PRODUCT_TITLE = "product_title";
  public static final String EXTRAS_PRODUCT_PRICE = "product_price";

  @BindView(R.id.root) View rootView;
  @BindView(R.id.swipe_refresh) SwipeRefreshLayout swipeRefreshLayoutView;
  @BindView(R.id.toolbar) Toolbar toolbarView;
  @BindView(R.id.image_gallery) ImageGalleryView imageGalleryView;
  @BindView(R.id.product_description) ProductDescriptionView productDescriptionView;

  private final LifecycleRegistry lifecycleRegistry = new LifecycleRegistry(this);
  private ProductDetailsViewModel productDetailsViewModel;

  @Override public LifecycleRegistry getLifecycle() {
    return lifecycleRegistry;
  }

  @Override public boolean onSupportNavigateUp() {
    finish();
    return true;
  }

  @Override public boolean onCreateOptionsMenu(final Menu menu) {
    MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.menu, menu);
    menu.findItem(R.id.cart).getActionView().setOnClickListener(v -> {
      ScreenRouter.route(this, new CartClickActionEvent());
    });
    return true;
  }

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

    setSupportActionBar(toolbarView);
    getSupportActionBar().setTitle(productTitle);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    initViewModels(productId);

    imageGalleryView.renderImages(Arrays.asList(productImageUrl));
    swipeRefreshLayoutView.setOnRefreshListener(() -> productDetailsViewModel.refetch());
    productDescriptionView.renderProduct(productTitle, productPrice);
    productDescriptionView.setOnAddToCartClickListener(() -> productDetailsViewModel.addToCart());

    lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_CREATE);
  }

  @Override protected void onStart() {
    super.onStart();
    lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_START);
  }

  @Override protected void onResume() {
    super.onResume();
    lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_RESUME);
  }

  @Override protected void onPause() {
    lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_PAUSE);
    super.onPause();
  }

  @Override protected void onStop() {
    lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_STOP);
    super.onStop();
  }

  @Override protected void onDestroy() {
    lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_DESTROY);
    super.onDestroy();
  }

  private void initViewModels(final String productId) {
    productDetailsViewModel = ViewModelProviders.of(this, new ViewModelProvider.Factory() {
      @SuppressWarnings("unchecked") @Override public <T extends ViewModel> T create(final Class<T> modelClass) {
        if (modelClass.equals(RealProductDetailsViewModel.class)) {
          return (T) new RealProductDetailsViewModel(productId);
        } else {
          return null;
        }
      }
    }).get(RealProductDetailsViewModel.class);
    productDetailsViewModel.productLiveData().observe(this, this::renderProduct);
    productDetailsViewModel.progressLiveData().observe(this, progress -> {
      if (progress != null) {
        swipeRefreshLayoutView.setRefreshing(progress.show);
      }
    });
    productDetailsViewModel.errorErrorCallback().observe(this.getLifecycle(), error -> {
      if (error != null) {
        showDefaultErrorMessage();
      }
    });
  }

  private void renderProduct(final ProductDetails product) {
    imageGalleryView.renderImages(product.images);
    productDescriptionView.renderProduct(product);
  }

  private void showDefaultErrorMessage() {
    Snackbar snackbar = Snackbar.make(rootView, R.string.default_error, Snackbar.LENGTH_LONG);
    snackbar.getView().setBackgroundResource(R.color.snackbar_error_background);
    snackbar.getView().setMinimumHeight(rootView.getHeight());

    TextView textView = (TextView) snackbar.getView().findViewById(android.support.design.R.id.snackbar_text);
    ViewGroup.LayoutParams layoutParams = textView.getLayoutParams();
    layoutParams.height = rootView.getHeight();
    textView.setLayoutParams(layoutParams);
    textView.setGravity(Gravity.CENTER_VERTICAL);

    snackbar.show();
  }
}
