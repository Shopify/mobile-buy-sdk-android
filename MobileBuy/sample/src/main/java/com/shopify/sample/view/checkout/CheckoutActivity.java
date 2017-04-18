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

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.google.android.gms.wallet.FullWallet;
import com.google.android.gms.wallet.MaskedWallet;
import com.google.android.gms.wallet.WalletConstants;
import com.google.android.gms.wallet.fragment.SupportWalletFragment;
import com.google.android.gms.wallet.fragment.WalletFragmentInitParams;
import com.google.android.gms.wallet.fragment.WalletFragmentMode;
import com.google.android.gms.wallet.fragment.WalletFragmentOptions;
import com.google.android.gms.wallet.fragment.WalletFragmentStyle;
import com.shopify.buy3.pay.PayCart;
import com.shopify.buy3.pay.PayHelper;
import com.shopify.sample.BuildConfig;
import com.shopify.sample.R;
import com.shopify.sample.domain.model.Checkout;
import com.shopify.sample.domain.repository.RealCheckoutRepository;
import com.shopify.sample.presenter.checkout.CheckoutViewPresenter;
import com.shopify.sample.view.ProgressDialogHelper;

import java.math.BigDecimal;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.shopify.sample.presenter.checkout.CheckoutViewPresenter.REQUEST_ID_APPLY_SHIPPING_RATE;
import static com.shopify.sample.presenter.checkout.CheckoutViewPresenter.REQUEST_ID_COMPLETE_CHECKOUT;
import static com.shopify.sample.presenter.checkout.CheckoutViewPresenter.REQUEST_ID_FETCH_SHIPPING_RATES;
import static com.shopify.sample.presenter.checkout.CheckoutViewPresenter.REQUEST_ID_UPDATE_CHECKOUT_SHIPPING_ADDRESS;
import static com.shopify.sample.util.Util.checkNotBlank;
import static com.shopify.sample.util.Util.checkNotNull;

public final class CheckoutActivity extends AppCompatActivity implements CheckoutViewPresenter.View {
  public static final String EXTRAS_CHECKOUT_ID = "checkout_id";
  public static final String EXTRAS_PAY_CART = "pay_cart";
  public static final String EXTRAS_MASKED_WALLET = "masked_wallet";

  @BindView(R.id.toolbar) Toolbar toolbarView;
  @BindView(R.id.total_summary) TotalSummaryView totalSummaryView;
  @BindView(R.id.shipping_method) ShippingMethodView shippingMethodView;

  private ProgressDialogHelper progressDialogHelper;
  private CheckoutViewPresenter presenter;

  @Override protected void onCreate(@Nullable final Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_checkout);
    ButterKnife.bind(this);

    setSupportActionBar(toolbarView);
    getSupportActionBar().setTitle(R.string.checkout_title);
    getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    progressDialogHelper = new ProgressDialogHelper(this);

    String checkoutId = getIntent().getStringExtra(EXTRAS_CHECKOUT_ID);
    PayCart payCart = getIntent().getParcelableExtra(EXTRAS_PAY_CART);
    MaskedWallet maskedWallet = getIntent().getParcelableExtra(EXTRAS_MASKED_WALLET);
    presenter = new CheckoutViewPresenter(checkoutId, payCart, maskedWallet, new RealCheckoutRepository());

    shippingMethodView.onShippingRateSelectListener(shippingRate -> presenter.applyShippingRate(shippingRate));
  }

  @Override protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
    super.onActivityResult(requestCode, resultCode, data);

    if (requestCode == PayHelper.REQUEST_CODE_CHANGE_MASKED_WALLET
      || requestCode == PayHelper.REQUEST_CODE_MASKED_WALLET) {
      final int errorCode = data != null ? data.getIntExtra(WalletConstants.EXTRA_ERROR_CODE, -1) : -1;
      if (errorCode != -1 || data == null) {
        //TODO show error
      } else if (resultCode == Activity.RESULT_OK) {
        final MaskedWallet maskedWallet = data.getParcelableExtra(WalletConstants.EXTRA_MASKED_WALLET);
        presenter.updateMaskedWallet(maskedWallet);
      }
    } else if (requestCode == PayHelper.REQUEST_CODE_FULL_WALLET) {
      final int errorCode = data != null ? data.getIntExtra(WalletConstants.EXTRA_ERROR_CODE, -1) : -1;
      if (errorCode != -1 || data == null) {
        if (errorCode == WalletConstants.ERROR_CODE_INVALID_TRANSACTION) {
          presenter.requestMaskedWalletUpdate();
        }
        //TODO show error
      } else if (resultCode == Activity.RESULT_OK) {
        final FullWallet fullWallet = data.getParcelableExtra(WalletConstants.EXTRA_FULL_WALLET);
        final MaskedWallet maskedWallet = data.getParcelableExtra(WalletConstants.EXTRA_MASKED_WALLET);
        if (fullWallet != null) {
          presenter.completeCheckout(fullWallet);
        } else if (maskedWallet != null) {
          // instead of full wallet response we got new version of masked wallet
          // try to update checkout and if update is not required then try to confirm the order again
          presenter.updateMaskedWallet(maskedWallet);
        } else {
          //TODO show error
        }
      }
    }
  }

  @Override public boolean onSupportNavigateUp() {
    finish();
    return true;
  }

  @Override public void onAttachedToWindow() {
    super.onAttachedToWindow();
    presenter.attachView(this);
  }

  @Override public void onDetachedFromWindow() {
    super.onDetachedFromWindow();
    presenter.detachView();
    progressDialogHelper.dismiss();
  }

  @Override public void showProgress(final int requestId) {
    final String message;
    if (requestId == REQUEST_ID_UPDATE_CHECKOUT_SHIPPING_ADDRESS) {
      message = getString(R.string.checkout_update_shipping_address_progress);
    } else if (requestId == REQUEST_ID_FETCH_SHIPPING_RATES) {
      message = getString(R.string.checkout_fetch_shipping_rates_progress);
    } else if (requestId == REQUEST_ID_APPLY_SHIPPING_RATE) {
      message = getString(R.string.checkout_apply_shipping_rate_progress);
    } else if (requestId == REQUEST_ID_COMPLETE_CHECKOUT) {
      message = getString(R.string.checkout_complete_progress);
    } else {
      message = getString(R.string.progress_loading);
    }
    progressDialogHelper.show(requestId, null, message, this::finish);
  }

  @Override public void hideProgress(final int requestId) {
    progressDialogHelper.dismiss(requestId);
  }

  @Override public void showError(final long requestId, final Throwable t) {
    t.printStackTrace();
  }

  @Override public void updateMaskedWallet(@NonNull final MaskedWallet maskedWallet) {
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

      final WalletFragmentInitParams startParams = WalletFragmentInitParams.newBuilder()
        .setMaskedWallet(maskedWallet)
        .setMaskedWalletRequestCode(PayHelper.REQUEST_CODE_CHANGE_MASKED_WALLET)
        .build();

      final SupportWalletFragment newWalletFragment = SupportWalletFragment.newInstance(walletFragmentOptions);
      newWalletFragment.initialize(startParams);
      getSupportFragmentManager()
        .beginTransaction()
        .replace(R.id.android_pay_layout, newWalletFragment)
        .commit();
    }
  }

  @Override public void renderTotalSummary(@NonNull final BigDecimal subtotal, @NonNull final BigDecimal shipping,
    @NonNull final BigDecimal tax, @NonNull final BigDecimal total) {
    totalSummaryView.render(subtotal, shipping, tax, total);
  }

  @Override public void renderShippingRates(@Nullable final Checkout.ShippingRates shippingRates,
    @Nullable final Checkout.ShippingRate shippingLine) {
    shippingMethodView.render(shippingLine, shippingRates);
  }

  @Override public Context context() {
    return this;
  }

  @OnClick(R.id.confirm)
  void onConfirmClick() {
    presenter.confirmCheckout();
  }
}
