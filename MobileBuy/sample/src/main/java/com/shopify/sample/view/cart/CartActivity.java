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

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleRegistry;
import android.arch.lifecycle.LifecycleRegistryOwner;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wallet.Wallet;
import com.google.android.gms.wallet.WalletConstants;
import com.shopify.buy3.pay.PayHelper;
import com.shopify.sample.BuildConfig;
import com.shopify.sample.R;
import com.shopify.sample.domain.model.Checkout;
import com.shopify.sample.view.ProgressDialogHelper;
import com.shopify.sample.view.ScreenRouter;

import java.util.Collections;

import butterknife.BindView;
import butterknife.ButterKnife;

public final class CartActivity extends AppCompatActivity implements LifecycleRegistryOwner,
  GoogleApiClient.ConnectionCallbacks {
  @BindView(R.id.root) View rootView;
  @BindView(R.id.cart_header) CartHeaderView cartHeaderView;
  @BindView(R.id.cart_list) CartListView cartListView;
  @BindView(R.id.toolbar) Toolbar toolbarView;

  private final LifecycleRegistry lifecycleRegistry = new LifecycleRegistry(this);
  private CartViewModel cartViewModel;
  private CartHeaderViewModel cartHeaderViewModel;

  private GoogleApiClient googleApiClient;
  private ProgressDialogHelper progressDialogHelper;

  @Override public boolean onSupportNavigateUp() {
    finish();
    return true;
  }

  @Override public LifecycleRegistry getLifecycle() {
    return lifecycleRegistry;
  }

  @Override public void onConnected(@Nullable final Bundle bundle) {
    PayHelper.isReadyToPay(this, googleApiClient, Collections.emptyList(), result -> {
      if (lifecycleRegistry.getCurrentState().isAtLeast(Lifecycle.State.STARTED)) {
        cartViewModel.onGoogleApiClientConnectionChanged(true);
      }
    });
  }

  @Override public void onConnectionSuspended(final int i) {
    cartViewModel.onGoogleApiClientConnectionChanged(false);
  }

  @SuppressWarnings("ConstantConditions") @Override protected void onCreate(@Nullable final Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_cart);
    ButterKnife.bind(this);

    setSupportActionBar(toolbarView);
    getSupportActionBar().setTitle("Cart");
    getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    progressDialogHelper = new ProgressDialogHelper(this);

    initViewModels();
    connectGoogleApiClient();

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

    if (progressDialogHelper != null) {
      progressDialogHelper.dismiss();
      progressDialogHelper = null;
    }

    if (googleApiClient != null) {
      googleApiClient.disconnect();
      googleApiClient = null;
    }
  }

  @Override protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    cartViewModel.handleMaskedWalletResponse(requestCode, resultCode, data);
  }

  @Override protected void onSaveInstanceState(final Bundle outState) {
    super.onSaveInstanceState(outState);
    outState.putBundle(RealCartViewModel.class.getName(), cartViewModel.saveState());
  }

  @Override protected void onRestoreInstanceState(final Bundle savedInstanceState) {
    super.onRestoreInstanceState(savedInstanceState);
    cartViewModel.restoreState(savedInstanceState.getBundle(RealCartViewModel.class.getName()));
  }

  private void initViewModels() {
    RealCartViewModel realCartViewModel = ViewModelProviders.of(this).get(RealCartViewModel.class);
    cartHeaderViewModel = realCartViewModel;
    cartViewModel = realCartViewModel;

    cartViewModel.webCheckoutCallback().observe(this.getLifecycle(), checkout -> {
      if (checkout != null) {
        onWebCheckoutConfirmation(checkout);
      }
    });
    cartViewModel.androidPayStartCheckoutCallback().observe(this.getLifecycle(), payCart -> {
      if (cartHeaderViewModel.googleApiClientConnectionData().getValue() == Boolean.TRUE && payCart != null) {
        PayHelper.requestMaskedWallet(googleApiClient, payCart, BuildConfig.ANDROID_PAY_PUBLIC_KEY);
      }
    });
    cartViewModel.androidPayCheckoutCallback().observe(this.getLifecycle(), confirmation -> {
      if (confirmation != null) {
        ScreenRouter.route(this, new AndroidPayConfirmationClickActionEvent(confirmation.checkoutId, confirmation.payCart,
          confirmation.maskedWallet));
      }
    });
    cartViewModel.progressLiveData().observe(this, progress -> {
      if (progress != null) {
        if (progress.show) {
          showProgress(progress.requestId);
        } else {
          hideProgress(progress.requestId);
        }
      }
    });
    cartViewModel.errorErrorCallback().observe(this.getLifecycle(), error -> {
      if (error != null) {
        showError(error.requestId, error.t);
      }
    });

    cartHeaderView.bindViewModel(cartHeaderViewModel);
  }

  private void showProgress(final int requestId) {
    progressDialogHelper.show(requestId, null, getResources().getString(R.string.progress_loading), () -> {
      cartViewModel.cancelRequest(requestId);
      cartViewModel.progressLiveData().hide(requestId);
    });
  }

  private void hideProgress(final int requestId) {
    progressDialogHelper.dismiss(requestId);
  }

  private void onWebCheckoutConfirmation(final Checkout checkout) {
    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(checkout.webUrl));
    startActivity(intent);
  }

  private void showError(final int requestId, final Throwable t) {
    Snackbar snackbar = Snackbar.make(rootView, R.string.default_error, Snackbar.LENGTH_LONG);
    snackbar.getView().setBackgroundResource(R.color.snackbar_error_background);
    snackbar.show();
  }

  private void connectGoogleApiClient() {
    if (PayHelper.isAndroidPayEnabledInManifest(this)) {
      googleApiClient = new GoogleApiClient.Builder(this)
        .addApi(Wallet.API, new Wallet.WalletOptions.Builder()
          .setEnvironment(BuildConfig.ANDROID_PAY_ENVIRONMENT)
          .setTheme(WalletConstants.THEME_DARK)
          .build())
        .addConnectionCallbacks(this)
        .build();
      googleApiClient.connect();
    }
  }
}
