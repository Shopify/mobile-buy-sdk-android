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

package com.shopify.buy3.pay;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.util.Base64;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wallet.FullWallet;
import com.google.android.gms.wallet.FullWalletRequest;
import com.google.android.gms.wallet.IsReadyToPayRequest;
import com.google.android.gms.wallet.MaskedWallet;
import com.google.android.gms.wallet.MaskedWalletRequest;
import com.google.android.gms.wallet.Wallet;
import com.google.android.gms.wallet.WalletConstants;
import com.google.android.gms.wallet.fragment.SupportWalletFragment;
import com.google.android.gms.wallet.fragment.WalletFragmentInitParams;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import static com.shopify.buy3.pay.Util.checkNotEmpty;
import static com.shopify.buy3.pay.Util.checkNotNull;

@SuppressWarnings({"WeakerAccess", "unused"})
public final class PayHelper {
  public static final int REQUEST_CODE_MASKED_WALLET = 500;
  public static final int REQUEST_CODE_CHANGE_MASKED_WALLET = 501;
  public static final int REQUEST_CODE_FULL_WALLET = 502;

  public static void requestMaskedWallet(final GoogleApiClient googleApiClient, final PayCart payCart, final String androidPayPublicKey) {
    MaskedWalletRequest maskedWalletRequest = payCart.maskedWalletRequest(androidPayPublicKey);
    Wallet.Payments.loadMaskedWallet(googleApiClient, maskedWalletRequest, PayHelper.REQUEST_CODE_MASKED_WALLET);
  }

  public static void initializeWalletFragment(final SupportWalletFragment walletFragment, final MaskedWallet maskedWallet) {
    WalletFragmentInitParams initParams = WalletFragmentInitParams.newBuilder()
      .setMaskedWallet(maskedWallet)
      .setMaskedWalletRequestCode(PayHelper.REQUEST_CODE_CHANGE_MASKED_WALLET)
      .build();
    walletFragment.initialize(initParams);
  }

  public static void requestFullWallet(final GoogleApiClient googleApiClient, final PayCart payCart, final MaskedWallet maskedWallet) {
    FullWalletRequest fullWalletRequest = payCart.fullWalletRequest(maskedWallet);
    Wallet.Payments.loadFullWallet(googleApiClient, fullWalletRequest, PayHelper.REQUEST_CODE_FULL_WALLET);
  }

  public static boolean handleWalletResponse(final int requestCode, final int resultCode, final Intent data,
    final WalletResponseHandler handler) {
    if (requestCode != REQUEST_CODE_CHANGE_MASKED_WALLET
      && requestCode != REQUEST_CODE_MASKED_WALLET
      && requestCode != REQUEST_CODE_FULL_WALLET) {
      return false;
    }

    if (resultCode != Activity.RESULT_OK) {
      handler.onWalletRequestCancel(requestCode);
      return true;
    }

    if (data != null) {
      int errorCode = data.getIntExtra(WalletConstants.EXTRA_ERROR_CODE, -1);
      if (errorCode != -1) {
        if (errorCode == WalletConstants.ERROR_CODE_INVALID_TRANSACTION) {
          handler.onMaskedWalletRequest();
        } else {
          handler.onWalletError(requestCode, errorCode);
        }
        return true;
      } else {
        MaskedWallet maskedWallet = data.getParcelableExtra(WalletConstants.EXTRA_MASKED_WALLET);
        FullWallet fullWallet = data.getParcelableExtra(WalletConstants.EXTRA_FULL_WALLET);
        if (maskedWallet != null) {
          handler.onMaskedWallet(maskedWallet);
          return true;
        } else if (fullWallet != null) {
          handler.onFullWallet(fullWallet);
          return true;
        }
      }
    }

    handler.onWalletError(requestCode, -1);
    return true;
  }

  public static boolean isAndroidPayEnabledInManifest(@NonNull final Context context) {
    boolean enabled = false;
    try {
      ApplicationInfo ai = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
      enabled = ai.metaData.getBoolean("com.google.android.gms.wallet.api.enabled");
    } catch (PackageManager.NameNotFoundException ignore) {
      // ignore
    }
    return enabled;
  }

  /**
   * Checks to see if Play Services are available on device
   *
   * @param context The context to use.
   * @return true if Play Services are available
   */
  public static boolean hasGooglePlayServices(@NonNull final Context context) {
    checkNotNull(context, "context can't be null");
    GoogleApiAvailability googleAPI = GoogleApiAvailability.getInstance();
    int result = googleAPI.isGooglePlayServicesAvailable(context);
    return result == ConnectionResult.SUCCESS;
  }

  public static void isReadyToPay(@NonNull final Context context, @NonNull final GoogleApiClient apiClient,
    @NonNull final List<Integer> supportedCardNetworks, @NonNull final AndroidPayReadyCallback delegate) {
    checkNotNull(apiClient, "apiClient can't be null");
    checkNotNull(delegate, "delegate can't be null");
    checkNotNull(supportedCardNetworks, "supportedCardNetworks can't be null");

    // make sure that device supports SHA-256 and UTF-8 required by hashing android pay public key for payment token creation
    try {
      MessageDigest.getInstance("SHA-256");
      byte[] ignore = "foo".getBytes("UTF-8");
    } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
      // if not then android pay feature should be disabled
      delegate.onResult(false);
      return;
    }

    if (!hasGooglePlayServices(context)) {
      delegate.onResult(false);
      return;
    }

    IsReadyToPayRequest.Builder payRequestBuilder = IsReadyToPayRequest.newBuilder();
    if (supportedCardNetworks.isEmpty()) {
      payRequestBuilder.addAllowedCardNetwork(WalletConstants.CardNetwork.AMEX)
        .addAllowedCardNetwork(WalletConstants.CardNetwork.DISCOVER)
        .addAllowedCardNetwork(WalletConstants.CardNetwork.JCB)
        .addAllowedCardNetwork(WalletConstants.CardNetwork.MASTERCARD)
        .addAllowedCardNetwork(WalletConstants.CardNetwork.VISA);
    } else {
      for (Integer supportedCardNetwork : supportedCardNetworks) {
        payRequestBuilder.addAllowedCardNetwork(supportedCardNetwork);
      }
    }

    Wallet.Payments.isReadyToPay(apiClient, payRequestBuilder.build())
      .setResultCallback(booleanResult -> {
        if (booleanResult.getStatus().isSuccess()) {
          delegate.onResult(booleanResult.getValue());
        } else {
          delegate.onResult(false);
        }
      });
  }

  public static PaymentToken extractPaymentToken(@NonNull final FullWallet fullWallet, @NonNull final String androidPayPublicKey) {
    checkNotNull(fullWallet, "fullWallet can't be null");
    checkNotEmpty(androidPayPublicKey, "androidPayPublicKey can't be empty");
    try {
      final MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
      final byte[] digest = messageDigest.digest(androidPayPublicKey.getBytes("UTF-8"));
      return new PaymentToken(fullWallet.getPaymentMethodToken().getToken(), Base64.encodeToString(digest,
        Base64.DEFAULT));
    } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
      return null;
    }
  }

  /**
   * Interface for receiving results from {@link PayHelper#androidPayIsAvailable(Context, GoogleApiClient, AndroidPayReadyCallback)}
   */
  public interface AndroidPayReadyCallback {
    void onResult(boolean result);
  }

  public static abstract class WalletResponseHandler {
    public abstract void onWalletError(int requestCode, int errorCode);

    public void onMaskedWalletRequest() {
    }

    public void onMaskedWallet(MaskedWallet maskedWallet) {
    }

    public void onFullWallet(FullWallet fullWallet) {
    }

    public void onWalletRequestCancel(int requestCode) {

    }
  }

  private PayHelper() {
  }
}
