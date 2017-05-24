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

package com.shopify.sample.view.checkout;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleRegistry;
import android.arch.lifecycle.LifecycleRegistryOwner;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wallet.MaskedWallet;
import com.google.android.gms.wallet.Wallet;
import com.google.android.gms.wallet.WalletConstants;
import com.google.android.gms.wallet.fragment.SupportWalletFragment;
import com.google.android.gms.wallet.fragment.WalletFragmentMode;
import com.google.android.gms.wallet.fragment.WalletFragmentOptions;
import com.google.android.gms.wallet.fragment.WalletFragmentStyle;
import com.shopify.buy3.pay.PayCart;
import com.shopify.buy3.pay.PayHelper;
import com.shopify.sample.BuildConfig;
import com.shopify.sample.R;
import com.shopify.sample.view.ProgressDialogHelper;

import java.math.BigDecimal;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.shopify.sample.util.Util.checkNotBlank;
import static com.shopify.sample.util.Util.checkNotNull;
import static com.shopify.sample.view.checkout.CheckoutShippingRatesViewModel.REQUEST_ID_FETCH_SHIPPING_RATES;
import static com.shopify.sample.view.checkout.CheckoutViewModel.REQUEST_ID_APPLY_SHIPPING_RATE;
import static com.shopify.sample.view.checkout.CheckoutViewModel.REQUEST_ID_COMPLETE_CHECKOUT;
import static com.shopify.sample.view.checkout.CheckoutViewModel.REQUEST_ID_CONFIRM_CHECKOUT;
import static com.shopify.sample.view.checkout.CheckoutViewModel.REQUEST_ID_UPDATE_CHECKOUT_SHIPPING_ADDRESS;

public final class CheckoutActivity extends AppCompatActivity implements LifecycleRegistryOwner {
  public static final String EXTRAS_CHECKOUT_ID = "checkout_id";
  public static final String EXTRAS_PAY_CART = "pay_cart";
  public static final String EXTRAS_MASKED_WALLET = "masked_wallet";

  @BindView(R.id.root) View rootView;
  @BindView(R.id.toolbar) Toolbar toolbarView;
  @BindView(R.id.total_summary) TotalSummaryView totalSummaryView;
  @BindView(R.id.shipping_rates) ShippingRatesView shippingRatesView;
  @BindView(R.id.confirm_layout) View confirmLayoutView;

  private String checkoutId;
  private PayCart payCart;
  private MaskedWallet maskedWallet;

  private final LifecycleRegistry lifecycleRegistry = new LifecycleRegistry(this);
  private CheckoutViewModel checkoutViewModel;

  private ProgressDialogHelper progressDialogHelper;
  private GoogleApiClient googleApiClient;

  @Override public LifecycleRegistry getLifecycle() {
    return lifecycleRegistry;
  }

  @Override protected void onCreate(@Nullable final Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_checkout);
    ButterKnife.bind(this);

    setSupportActionBar(toolbarView);
    getSupportActionBar().setTitle(R.string.checkout_title);
    getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    progressDialogHelper = new ProgressDialogHelper(this);

    checkoutId = checkNotBlank(getIntent().getStringExtra(EXTRAS_CHECKOUT_ID), "checkoutId can't be empty");
    payCart = checkNotNull(getIntent().getParcelableExtra(EXTRAS_PAY_CART), payCart == null);
    maskedWallet = getIntent().getParcelableExtra(EXTRAS_MASKED_WALLET);

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
    checkoutViewModel.handleWalletResponse(requestCode, resultCode, data, googleApiClient);
  }

  @Override public boolean onSupportNavigateUp() {
    finish();
    return true;
  }

  @OnClick(R.id.confirm)
  void onConfirmClick() {
    checkoutViewModel.confirmCheckout(googleApiClient);
  }

  private void initViewModels() {
    RealCheckoutViewModel realCheckoutViewModel = ViewModelProviders.of(this, new ViewModelProvider.Factory() {
      @SuppressWarnings("unchecked") @Override public <T extends ViewModel> T create(final Class<T> modelClass) {
        if (modelClass.equals(RealCheckoutViewModel.class)) {
          return (T) new RealCheckoutViewModel(checkoutId, payCart, maskedWallet);
        } else {
          return null;
        }
      }
    }).get(RealCheckoutViewModel.class);
    realCheckoutViewModel.progressLiveData().observe(this, progress -> {
      if (progress != null) {
        if (progress.show) {
          showProgress(progress.requestId);
        } else {
          hideProgress(progress.requestId);
        }
      }
    });
    realCheckoutViewModel.errorErrorCallback().observe(this.getLifecycle(), error -> {
      if (error != null) {
        showError(error.requestId, error.t);
      }
    });
    realCheckoutViewModel.payCartLiveData().observe(this, payCart -> {
      if (payCart == null) {
        totalSummaryView.render(BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO);
      } else {
        totalSummaryView.render(payCart.subtotal, payCart.shippingPrice != null ? payCart.shippingPrice : BigDecimal.ZERO,
          payCart.taxPrice != null ? payCart.taxPrice : BigDecimal.ZERO, payCart.totalPrice);
      }
    });
    realCheckoutViewModel.maskedWalletLiveData().observe(this, maskedWallet -> {
      if (maskedWallet != null) {
        updateMaskedWallet(maskedWallet);
      }
    });

    checkoutViewModel = realCheckoutViewModel;
    shippingRatesView.bindViewModel(realCheckoutViewModel);
  }

  private void updateMaskedWallet(@NonNull final MaskedWallet maskedWallet) {
    final SupportWalletFragment walletFragment = (SupportWalletFragment) getSupportFragmentManager()
      .findFragmentById(R.id.android_pay_layout);
    if (walletFragment != null) {
      walletFragment.updateMaskedWallet(maskedWallet);
    } else {
      final WalletFragmentStyle walletFragmentStyle = new WalletFragmentStyle()
        .setMaskedWalletDetailsHeaderTextAppearance(R.style.WalletDetailsHeaderTextAppearance)
        .setMaskedWalletDetailsTextAppearance(R.style.WalletDetailsTextAppearance)
        .setMaskedWalletDetailsBackgroundColor(android.R.color.transparent)
        .setMaskedWalletDetailsButtonBackgroundColor(android.R.color.transparent)
        .setMaskedWalletDetailsButtonTextAppearance(R.style.WalletDetailsButton);

      final WalletFragmentOptions walletFragmentOptions = WalletFragmentOptions.newBuilder()
        .setEnvironment(BuildConfig.ANDROID_PAY_ENVIRONMENT)
        .setFragmentStyle(walletFragmentStyle)
        .setTheme(WalletConstants.THEME_LIGHT)
        .setMode(WalletFragmentMode.SELECTION_DETAILS)
        .build();

      final SupportWalletFragment newWalletFragment = SupportWalletFragment.newInstance(walletFragmentOptions);
      PayHelper.initializeWalletFragment(newWalletFragment, maskedWallet);

      getSupportFragmentManager()
        .beginTransaction()
        .replace(R.id.android_pay_layout, newWalletFragment)
        .commit();
    }
  }

  private void showProgress(final int requestId) {
    final String message;
    if (requestId == REQUEST_ID_UPDATE_CHECKOUT_SHIPPING_ADDRESS) {
      message = getString(R.string.checkout_update_shipping_address_progress);
    } else if (requestId == REQUEST_ID_FETCH_SHIPPING_RATES) {
      message = getResources().getString(R.string.checkout_fetch_shipping_rates_progress);
    } else if (requestId == REQUEST_ID_APPLY_SHIPPING_RATE) {
      message = getResources().getString(R.string.checkout_apply_shipping_rate_progress);
    } else if (requestId == REQUEST_ID_COMPLETE_CHECKOUT) {
      message = getString(R.string.checkout_complete_progress);
    } else {
      message = getString(R.string.progress_loading);
    }
    progressDialogHelper.show(requestId, null, message, this::finish);
  }

  private void hideProgress(final int requestId) {
    progressDialogHelper.dismiss(requestId);
  }

  private void showError(final int requestId, final Throwable t) {
    if (t instanceof CheckoutViewModel.ShippingRateMissingException) {
      showError(R.string.checkout_shipping_select_shipping_rate);
    } else {
      showError(R.string.default_error);
    }
  }

  private void showError(final int message) {
    Snackbar snackbar = Snackbar.make(rootView, message, Snackbar.LENGTH_LONG);
    snackbar.getView().setBackgroundResource(R.color.snackbar_error_background);
    snackbar.getView().setMinimumHeight(confirmLayoutView.getHeight());

    TextView textView = (TextView) snackbar.getView().findViewById(android.support.design.R.id.snackbar_text);
    ViewGroup.LayoutParams layoutParams = textView.getLayoutParams();
    layoutParams.height = confirmLayoutView.getHeight();
    textView.setLayoutParams(layoutParams);
    textView.setGravity(Gravity.CENTER_VERTICAL);

    snackbar.show();
  }

  private void connectGoogleApiClient() {
    if (PayHelper.isAndroidPayEnabledInManifest(this)) {
      googleApiClient = new GoogleApiClient.Builder(this)
        .addApi(Wallet.API, new Wallet.WalletOptions.Builder()
          .setEnvironment(BuildConfig.ANDROID_PAY_ENVIRONMENT)
          .setTheme(WalletConstants.THEME_DARK)
          .build())
        .build();
      googleApiClient.connect();
    }
  }
}
