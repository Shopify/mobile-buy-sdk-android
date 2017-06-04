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

package com.shopify.sample.view.products;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;

import com.shopify.sample.R;
import com.shopify.sample.view.ScreenRouter;
import com.shopify.sample.view.cart.CartClickActionEvent;
import com.shopify.sample.view.widget.image.ShopifyDraweeView;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.shopify.sample.util.Util.checkNotNull;

public final class ProductListActivity extends AppCompatActivity {
  public static final String EXTRAS_COLLECTION_ID = "collection_id";
  public static final String EXTRAS_COLLECTION_IMAGE_URL = "collection_image_url";
  public static final String EXTRAS_COLLECTION_TITLE = "collection_title";

  @BindView(R.id.toolbar) Toolbar toolbarView;
  @BindView(R.id.collection_image) ShopifyDraweeView collectionImageView;
  @BindView(R.id.product_list) ProductListView productListView;

  @Override protected void onCreate(@Nullable final Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_product_list);
    ButterKnife.bind(this);

    String collectionId = getIntent().getStringExtra(EXTRAS_COLLECTION_ID);
    String collectionTitle = getIntent().getStringExtra(EXTRAS_COLLECTION_TITLE);
    String collectionImageUrl = getIntent().getStringExtra(EXTRAS_COLLECTION_IMAGE_URL);

    checkNotNull(collectionId, "collectionId == null");
    checkNotNull(collectionTitle, "collectionTitle == null");

    setSupportActionBar(toolbarView);
    getSupportActionBar().setTitle(collectionTitle);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    collectionImageView.loadShopifyImage(collectionImageUrl);

    initViewModels(collectionId);
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

  private void initViewModels(final String collectionId) {
    RealProductListViewModel listViewModel = ViewModelProviders.of(this, new ViewModelProvider.Factory() {
      @SuppressWarnings("unchecked") @Override public <T extends ViewModel> T create(final Class<T> modelClass) {
        if (modelClass.equals(RealProductListViewModel.class)) {
          return (T) new RealProductListViewModel(collectionId);
        } else {
          return null;
        }
      }
    }).get(RealProductListViewModel.class);
    productListView.bindViewModel(listViewModel);
  }
}
