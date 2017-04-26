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

package com.shopify.sample.view.cart;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.shopify.sample.R;
import com.shopify.sample.view.ScreenRouter;

import butterknife.BindView;
import butterknife.ButterKnife;

public final class CartActivity extends AppCompatActivity {
  @BindView(R.id.cart_header) CartCheckoutView cartHeaderView;
  @BindView(R.id.cart_list) CartListView cartListView;
  @BindView(R.id.toolbar) Toolbar toolbarView;

  @SuppressWarnings("ConstantConditions") @Override protected void onCreate(@Nullable final Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_cart);
    ButterKnife.bind(this);

    setSupportActionBar(toolbarView);
    getSupportActionBar().setTitle("Cart");
    getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    cartHeaderView.setOnConfirmAndroidPayListener((checkoutId, payCart, maskedWallet) ->
      ScreenRouter.route(this, new AndroidPayConfirmationClickActionEvent(checkoutId, payCart, maskedWallet)));
  }

  @Override public boolean onSupportNavigateUp() {
    finish();
    return true;
  }

  @Override protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    cartHeaderView.handleMaskedWalletResponse(requestCode, resultCode, data);
  }
}
