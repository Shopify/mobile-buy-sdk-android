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

package com.shopify.sample.view.order;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.FrameLayout;

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

import java.math.BigDecimal;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.shopify.sample.util.Util.checkNotBlank;
import static com.shopify.sample.util.Util.checkNotNull;

public final class OrderConfirmationActivity extends AppCompatActivity {
  public static final String EXTRAS_CHECKOUT_ID = "checkout_id";
  public static final String EXTRAS_PAY_CART = "pay_cart";

  @BindView(R.id.toolbar) Toolbar toolbarView;
  @BindView(R.id.order_total_summary) TotalSummaryView totalSummaryView;
  @BindView(R.id.android_pay_layout) FrameLayout androidPayLayoutView;

  private String checkoutId;
  private PayCart payCart;

  @Override protected void onCreate(@Nullable final Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_order_confirmation);
    ButterKnife.bind(this);

    setSupportActionBar(toolbarView);
    getSupportActionBar().setTitle(R.string.order_confirmation_title);
    getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    checkoutId = getIntent().getStringExtra(EXTRAS_CHECKOUT_ID);
    payCart = getIntent().getParcelableExtra(EXTRAS_PAY_CART);

    checkNotBlank(checkoutId, "checkoutId can't be empty");
    checkNotNull(payCart, "payCart == null");
    checkNotNull(payCart.maskedWallet, "payCart.maskedWallet == null");

    totalSummaryView.render(payCart.subtotal, payCart.shippingPrice != null ? payCart.shippingPrice : BigDecimal.ZERO,
      payCart.taxPrice != null ? payCart.taxPrice : BigDecimal.ZERO, payCart.totalPrice);

    updateWalletFragment(payCart.maskedWallet);
  }

  @Override protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
    super.onActivityResult(requestCode, resultCode, data);

  }

  @Override public boolean onSupportNavigateUp() {
    finish();
    return true;
  }

  private void updateWalletFragment(final MaskedWallet maskedWallet) {
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

      final WalletFragmentInitParams startParams = WalletFragmentInitParams.newBuilder()
        .setMaskedWallet(maskedWallet)
        .setMaskedWalletRequestCode(PayHelper.REQUEST_CODE_CHANGE_MASKED_WALLET)
        .build();

      newWalletFragment.initialize(startParams);

      getSupportFragmentManager()
        .beginTransaction()
        .replace(R.id.android_pay_layout, newWalletFragment)
        .commit();
    }
  }
}
