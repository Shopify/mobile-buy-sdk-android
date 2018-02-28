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

import android.app.Activity;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleRegistry;
import android.arch.lifecycle.LifecycleRegistryOwner;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.wallet.AutoResolveHelper;
import com.google.android.gms.wallet.CardRequirements;
import com.google.android.gms.wallet.PaymentData;
import com.google.android.gms.wallet.PaymentDataRequest;
import com.google.android.gms.wallet.PaymentMethodTokenizationParameters;
import com.google.android.gms.wallet.PaymentsClient;
import com.google.android.gms.wallet.ShippingAddressRequirements;
import com.google.android.gms.wallet.TransactionInfo;
import com.google.android.gms.wallet.Wallet;
import com.google.android.gms.wallet.WalletConstants;
import com.google.gson.Gson;
import com.shopify.buy3.pay.PayAddress;
import com.shopify.buy3.pay.PayCart;
import com.shopify.buy3.pay.PayHelper;
import com.shopify.buy3.pay.PaymentToken;
import com.shopify.sample.BaseApplication;
import com.shopify.sample.BuildConfig;
import com.shopify.sample.R;
import com.shopify.sample.domain.interactor.CheckoutCompleteInteractor;
import com.shopify.sample.domain.interactor.CheckoutShippingAddressUpdateInteractor;
import com.shopify.sample.domain.interactor.CheckoutShippingLineUpdateInteractor;
import com.shopify.sample.domain.interactor.CheckoutShippingRatesInteractor;
import com.shopify.sample.domain.interactor.RealCheckoutCompleteInteractor;
import com.shopify.sample.domain.interactor.RealCheckoutShippingAddressUpdateInteractor;
import com.shopify.sample.domain.interactor.RealCheckoutShippingLineUpdateInteractor;
import com.shopify.sample.domain.interactor.RealCheckoutShippingRatesInteractor;
import com.shopify.sample.domain.model.Checkout;
import com.shopify.sample.domain.model.ShopSettings;
import com.shopify.sample.view.ProgressDialogHelper;
import com.shopify.sample.view.ScreenRouter;
import com.shopify.sample.view.checkout.CheckoutViewModel;

import java.util.Arrays;
import java.util.Locale;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;

public final class CartActivity extends AppCompatActivity implements LifecycleRegistryOwner,
  GoogleApiClient.ConnectionCallbacks {

  private static final int LOAD_PAYMENT_DATA_REQUEST_CODE = 991;

  @BindView(R.id.root) View rootView;
  @BindView(R.id.cart_header) CartHeaderView cartHeaderView;
  @BindView(R.id.cart_list) CartListView cartListView;
  @BindView(R.id.toolbar) Toolbar toolbarView;

  private final LifecycleRegistry lifecycleRegistry = new LifecycleRegistry(this);
  private CartDetailsViewModel cartDetailsViewModel;
  private CartHeaderViewModel cartHeaderViewModel;

  private GoogleApiClient googleApiClient;
  private ProgressDialogHelper progressDialogHelper;

  @Override
  public boolean onSupportNavigateUp() {
    finish();
    return true;
  }

  @Override
  public LifecycleRegistry getLifecycle() {
    return lifecycleRegistry;
  }

  @Override
  public void onConnected(@Nullable final Bundle bundle) {
    ShopSettings shopSettings = ((BaseApplication) getApplication()).shopSettings().getValue();
    PayHelper.isReadyToPay(getApplicationContext(), googleApiClient, shopSettings.acceptedCardBrands, result -> {
      if (lifecycleRegistry.getCurrentState().isAtLeast(Lifecycle.State.STARTED)) {
        cartDetailsViewModel.onGoogleApiClientConnectionChanged(true);
      }
    });
  }

  @Override
  public void onConnectionSuspended(final int i) {
    cartDetailsViewModel.onGoogleApiClientConnectionChanged(false);
  }

  @SuppressWarnings("ConstantConditions")
  @Override
  protected void onCreate(@Nullable final Bundle savedInstanceState) {
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

    if (googleApiClient != null) {
      googleApiClient.disconnect();
      googleApiClient = null;
    }
  }

  private final int REQUEST_ID_COMPLETE_CHECKOUT = UUID.randomUUID().hashCode();
  private final CheckoutCompleteInteractor checkoutCompleteInteractor = new RealCheckoutCompleteInteractor();
  private final CheckoutShippingAddressUpdateInteractor checkoutShippingAddressUpdateInteractor = new RealCheckoutShippingAddressUpdateInteractor();
  private final CheckoutShippingRatesInteractor checkoutShippingRatesInteractor = new RealCheckoutShippingRatesInteractor();
  private final CheckoutShippingLineUpdateInteractor checkoutShippingLineUpdateInteractor = new RealCheckoutShippingLineUpdateInteractor();

  @Override
  protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (requestCode == LOAD_PAYMENT_DATA_REQUEST_CODE) {
      switch (resultCode) {
        case Activity.RESULT_OK:
          PaymentData paymentData = PaymentData.getFromIntent(data);

          String token = paymentData.getPaymentMethodToken().getToken();
          final PaymentToken paymentToken = PayHelper.extractPaymentToken(token, BuildConfig.ANDROID_PAY_PUBLIC_KEY);
          PayAddress billingAddress = PayAddress.fromUserAddress(paymentData.getShippingAddress());
          String email = paymentData.getEmail();
          String checkoutId = cartDetailsViewModel.getCheckoutId();
          PayCart payCart = cartDetailsViewModel.getPayCart();

          showProgress(REQUEST_ID_COMPLETE_CHECKOUT);
          checkoutShippingAddressUpdateInteractor.execute(checkoutId, billingAddress)
            .flatMap(checkout -> {
              return checkoutShippingRatesInteractor.execute(checkoutId);
            })
            .flatMap(shippingRates -> {
              return checkoutShippingLineUpdateInteractor.execute(checkoutId, shippingRates.shippingRates.get(0).handle);
            })
            .flatMap(checkout -> {
              return checkoutCompleteInteractor.execute(cartDetailsViewModel.getCheckoutId(), payCart, paymentToken, email, billingAddress);
            })
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe((payment, throwable) -> {
              String log = String.format(Locale.US, "onPaymentCompleted | %s | %s", payment != null ? payment.errorMessage : "none", throwable != null ? throwable.getMessage() : "none");
              Log.d("custom", log);
              Toast.makeText(getApplicationContext(), log, Toast.LENGTH_SHORT).show();
              hideProgress(REQUEST_ID_COMPLETE_CHECKOUT);
            });
          break;
        case Activity.RESULT_CANCELED:
          break;
        case AutoResolveHelper.RESULT_ERROR:
          Status status = AutoResolveHelper.getStatusFromIntent(data);
          // Log the status for debugging.
          // Generally, there is no need to show an error to
          // the user as the Google Payment API will do that.
          break;
        default:
          // Do nothing.
      }
    }
  }

  @Override
  protected void onSaveInstanceState(final Bundle outState) {
    super.onSaveInstanceState(outState);
    outState.putBundle(RealCartViewModel.class.getName(), cartDetailsViewModel.saveState());
  }

  @Override
  protected void onRestoreInstanceState(final Bundle savedInstanceState) {
    super.onRestoreInstanceState(savedInstanceState);
    cartDetailsViewModel.restoreState(savedInstanceState.getBundle(RealCartViewModel.class.getName()));
  }

  private void initViewModels() {
    ShopSettings shopSettings = ((BaseApplication) getApplication()).shopSettings().getValue();
    RealCartViewModel cartViewModel = ViewModelProviders.of(this, new ViewModelProvider.Factory() {
      @Override
      public <T extends ViewModel> T create(final Class<T> modelClass) {
        if (modelClass.equals(RealCartViewModel.class)) {
          //noinspection unchecked
          return (T) new RealCartViewModel(shopSettings);
        } else {
          return null;
        }
      }
    }).get(RealCartViewModel.class);
    cartHeaderViewModel = cartViewModel;
    cartDetailsViewModel = cartViewModel;

    cartDetailsViewModel.webCheckoutCallback().observe(this.getLifecycle(), checkout -> {
      if (checkout != null) {
        onWebCheckoutConfirmation(checkout);
      }
    });
    cartDetailsViewModel.androidPayStartCheckoutCallback().observe(this.getLifecycle(), payCart -> {
      final PaymentDataRequest request = PaymentDataRequest.newBuilder()
        .setTransactionInfo(TransactionInfo.newBuilder()
          .setTotalPriceStatus(WalletConstants.TOTAL_PRICE_STATUS_FINAL)
           .setTotalPrice(payCart.totalPrice.toString())
          .setCurrencyCode(payCart.currencyCode)
          .build())
        .setPhoneNumberRequired(true)
        .setEmailRequired(true)
        .setShippingAddressRequired(true)
        .setShippingAddressRequirements(ShippingAddressRequirements.newBuilder()
          .addAllowedCountryCodes(Arrays.asList("US", "GB", "CA"))
          .build())
        .addAllowedPaymentMethods(Arrays.asList(WalletConstants.PAYMENT_METHOD_CARD, WalletConstants.PAYMENT_METHOD_TOKENIZED_CARD))
        .setCardRequirements(CardRequirements.newBuilder()
          .addAllowedCardNetworks(Arrays.asList(
            WalletConstants.CARD_NETWORK_AMEX,
            WalletConstants.CARD_NETWORK_DISCOVER,
            WalletConstants.CARD_NETWORK_VISA,
            WalletConstants.CARD_NETWORK_MASTERCARD))
          .setAllowPrepaidCards(true)
          .setBillingAddressRequired(true)
          .setBillingAddressFormat(WalletConstants.BILLING_ADDRESS_FORMAT_FULL)
          .build())
        .setPaymentMethodTokenizationParameters(PaymentMethodTokenizationParameters.newBuilder()
          .setPaymentMethodTokenizationType(WalletConstants.PAYMENT_METHOD_TOKENIZATION_TYPE_PAYMENT_GATEWAY)
          .addParameter("gateway", "shopify")
          .addParameter("gatewayMerchantId", "22648459")
          .build())
        .setUiRequired(true)
        .build();
      final PaymentsClient paymentsClient = Wallet.getPaymentsClient(BaseApplication.instance(), new Wallet.WalletOptions.Builder()
        .setEnvironment(BuildConfig.ANDROID_PAY_ENVIRONMENT)
        .build());
      AutoResolveHelper.resolveTask(paymentsClient.loadPaymentData(request), this, LOAD_PAYMENT_DATA_REQUEST_CODE);
    });
    cartDetailsViewModel.androidPayCheckoutCallback().observe(this.getLifecycle(), confirmation -> {
      if (confirmation != null) {
        ScreenRouter.route(this, new AndroidPayConfirmationClickActionEvent(confirmation.checkoutId, confirmation.payCart,
          confirmation.maskedWallet));
      }
    });
    cartDetailsViewModel.progressLiveData().observe(this, progress -> {
      if (progress != null) {
        if (progress.show) {
          showProgress(progress.requestId);
        } else {
          hideProgress(progress.requestId);
        }
      }
    });
    cartDetailsViewModel.errorErrorCallback().observe(this.getLifecycle(), error -> {
      if (error != null) {
        showError(error.requestId, error.t, error.message);
      }
    });

    cartHeaderView.bindViewModel(cartHeaderViewModel);

    CartListViewModel cartListViewModel = ViewModelProviders.of(this).get(CartListViewModel.class);
    cartListView.bindViewModel(cartListViewModel);
  }

  private void showProgress(final int requestId) {
    progressDialogHelper.show(requestId, null, getResources().getString(R.string.progress_loading), () -> {
      cartDetailsViewModel.cancelRequest(requestId);
      cartDetailsViewModel.progressLiveData().hide(requestId);
    });
  }

  private void hideProgress(final int requestId) {
    progressDialogHelper.dismiss(requestId);
  }

  private void onWebCheckoutConfirmation(final Checkout checkout) {
    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(checkout.webUrl));
    startActivity(intent);
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
    snackbar.show();
  }

  private void connectGoogleApiClient() {
//    if (PayHelper.isAndroidPayEnabledInManifest(this)) {
//      googleApiClient = new GoogleApiClient.Builder(this)
//        .addApi(Wallet.API, new Wallet.WalletOptions.Builder()
//          .setEnvironment(BuildConfig.ANDROID_PAY_ENVIRONMENT)
//          .setTheme(WalletConstants.THEME_DARK)
//          .build())
//        .addConnectionCallbacks(this)
//        .build();
//      googleApiClient.connect();
//    }
  }
}
