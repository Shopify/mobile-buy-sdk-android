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

import android.arch.lifecycle.*;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.shopify.sample.R;
import com.shopify.sample.view.ProgressDialogHelper;

import static com.shopify.sample.util.Util.checkNotBlank;
import static com.shopify.sample.view.checkout.CheckoutShippingRatesViewModel.REQUEST_ID_FETCH_SHIPPING_RATES;
import static com.shopify.sample.view.checkout.CheckoutViewModel.*;

public final class CheckoutActivity extends AppCompatActivity implements LifecycleRegistryOwner {
  public static final String EXTRAS_CHECKOUT_ID = "checkout_id";
  public static final String EXTRAS_PAY_CART = "pay_cart";
  public static final String EXTRAS_MASKED_WALLET = "masked_wallet";

  @BindView(R.id.root)
  View rootView;
  @BindView(R.id.toolbar)
  Toolbar toolbarView;
  @BindView(R.id.total_summary)
  TotalSummaryView totalSummaryView;
  @BindView(R.id.shipping_rates)
  ShippingRatesView shippingRatesView;
  @BindView(R.id.confirm_layout)
  View confirmLayoutView;

  private String checkoutId;

  private final LifecycleRegistry lifecycleRegistry = new LifecycleRegistry(this);
  private CheckoutViewModel checkoutViewModel;

  private ProgressDialogHelper progressDialogHelper;

  @Override
  public LifecycleRegistry getLifecycle() {
    return lifecycleRegistry;
  }

  @Override
  protected void onCreate(@Nullable final Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_checkout);
    ButterKnife.bind(this);

    setSupportActionBar(toolbarView);
    getSupportActionBar().setTitle(R.string.checkout_title);
    getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    progressDialogHelper = new ProgressDialogHelper(this);

    checkoutId = checkNotBlank(getIntent().getStringExtra(EXTRAS_CHECKOUT_ID), "checkoutId can't be empty");

    initViewModels();
    lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_CREATE);
  }

  @Override
  protected void onStart() {
    super.onStart();
    lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_START);
  }

  @Override
  protected void onResume() {
    super.onResume();
    lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_RESUME);
  }

  @Override
  protected void onPause() {
    lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_PAUSE);
    super.onPause();
  }

  @Override
  protected void onStop() {
    lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_STOP);
    super.onStop();
  }

  @Override
  protected void onDestroy() {
    lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_DESTROY);

    super.onDestroy();

    if (progressDialogHelper != null) {
      progressDialogHelper.dismiss();
      progressDialogHelper = null;
    }
  }

  @Override
  protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
  }

  @Override
  public boolean onSupportNavigateUp() {
    finish();
    return true;
  }

  @OnClick(R.id.confirm)
  void onConfirmClick() {
    checkoutViewModel.confirmCheckout();
  }

  private void initViewModels() {
    RealCheckoutViewModel realCheckoutViewModel = ViewModelProviders.of(this, new ViewModelProvider.Factory() {
      @SuppressWarnings("unchecked")
      @Override
      public <T extends ViewModel> T create(final Class<T> modelClass) {
        if (modelClass.equals(RealCheckoutViewModel.class)) {
          return (T) new RealCheckoutViewModel(checkoutId);
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
        showError(error.requestId, error.t, error.message);
      }
    });
    realCheckoutViewModel.successPaymentLiveData().observe(this, payment -> {
      if (payment != null) {
        showCheckoutSuccessMessage();
      }
    });

    checkoutViewModel = realCheckoutViewModel;
    shippingRatesView.bindViewModel(realCheckoutViewModel);
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

  private void showError(final int requestId, final Throwable t, final String message) {
    if (message != null) {
      showAlertErrorMessage(message);
      return;
    }

    if (t instanceof CheckoutViewModel.ShippingRateMissingException) {
      showAlertErrorMessage(getString(R.string.checkout_shipping_select_shipping_rate));
      return;
    }

    showDefaultErrorMessage();
  }

  private void showAlertErrorMessage(final String message) {
    new AlertDialog.Builder(this)
        .setMessage(message)
        .setPositiveButton(android.R.string.ok, (dialog, which) -> {
        })
        .show();
  }

  private void showDefaultErrorMessage() {
    Snackbar snackbar = Snackbar.make(rootView, R.string.default_error, Snackbar.LENGTH_LONG);
    snackbar.getView().setBackgroundResource(R.color.snackbar_error_background);
    snackbar.getView().setMinimumHeight(confirmLayoutView.getHeight());

    TextView textView = (TextView) snackbar.getView().findViewById(android.support.design.R.id.snackbar_text);
    ViewGroup.LayoutParams layoutParams = textView.getLayoutParams();
    layoutParams.height = confirmLayoutView.getHeight();
    textView.setLayoutParams(layoutParams);
    textView.setGravity(Gravity.CENTER_VERTICAL);

    snackbar.show();
  }

  private void showCheckoutSuccessMessage() {
    new AlertDialog.Builder(this)
        .setMessage(R.string.checkout_success)
        .setPositiveButton(android.R.string.ok, (dialog, which) -> finish())
        .show();
  }
}
