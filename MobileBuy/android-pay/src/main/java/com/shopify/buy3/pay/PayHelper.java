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

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.util.Base64;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.BooleanResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.wallet.FullWallet;
import com.google.android.gms.wallet.Wallet;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static com.shopify.buy3.pay.Util.checkNotEmpty;
import static com.shopify.buy3.pay.Util.checkNotNull;

@SuppressWarnings({"WeakerAccess", "unused"})
public final class PayHelper {
  public static final int REQUEST_CODE_MASKED_WALLET = 500;
  public static final int REQUEST_CODE_CHANGE_MASKED_WALLET = 501;
  public static final int REQUEST_CODE_FULL_WALLET = 502;

  public static final String[] UNSUPPORTED_SHIPPING_COUNTRIES = {"MM", "SS", "GG", "IM", "KP", "SX", "SY", "IR", "BL", "BQ", "SD", "CU",
    "CW", "AX", "MF", "JE"};

  public static boolean isAndroidPayEnabledInManifest(@NonNull final Context context) {
    boolean enabled = false;
    try {
      ApplicationInfo ai = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
      enabled = ai.metaData.getBoolean("com.google.android.gms.wallet.api.enabled");
    } catch (PackageManager.NameNotFoundException e) {
      // ignore
    }
    return enabled;
  }

  /**
   * Checks to see if Android Pay is available on device.
   * <p>
   * It will check that:
   * 1) Play Services are available using {@link PayHelper#hasGooglePlayServices(Context)}
   * 2) The Android Pay application is installed on device, and user has setup a valid card for In App Purchase
   * using {@link PayHelper#isReadyToPay(GoogleApiClient, AndroidPayReadyCallback)}
   *
   * @param context   The context to use.
   * @param apiClient The {@link GoogleApiClient}, not null
   * @param delegate  The {@link AndroidPayReadyCallback} delegate for receiving the result
   */
  public static void androidPayIsAvailable(@NonNull final Context context, @NonNull final GoogleApiClient apiClient,
    @NonNull final AndroidPayReadyCallback delegate) {
    checkNotNull(context, "context can't be null");
    checkNotNull(apiClient, "apiClient can't be null");
    checkNotNull(delegate, "delegate can't be null");

    // make sure that device supports SHA-256 and UTF-8 required by hashing android pay public key for payment token creation
    try {
      MessageDigest.getInstance("SHA-256");
      byte[] ignore = "foo".getBytes("UTF-8");
    } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
      // if not then android pay feature should be disabled
      delegate.onResult(false);
      return;
    }

    // Check to see if Google play is up to date
    if (!hasGooglePlayServices(context)) {
      delegate.onResult(false);
      return;
    }

    isReadyToPay(apiClient, delegate);
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

  /**
   * Checks to see if the Android Pay App is installed on device and has a valid card for In App Purchase
   * using {@link com.google.android.gms.wallet.Payments#isReadyToPay(GoogleApiClient)}
   *
   * @param apiClient The {@link GoogleApiClient}, not null
   * @param delegate  The {@link AndroidPayReadyCallback} delegate for receiving the result
   */
  public static void isReadyToPay(@NonNull final GoogleApiClient apiClient, @NonNull final AndroidPayReadyCallback delegate) {
    checkNotNull(apiClient, "apiClient can't be null");
    checkNotNull(delegate, "delegate can't be null");
    // Check that the user has installed and setup the Android Pay app on their device
    Wallet.Payments.isReadyToPay(checkNotNull(apiClient, "apiClient can't be null"))
      .setResultCallback(new ResultCallback<BooleanResult>() {
        @Override
        public void onResult(@NonNull BooleanResult booleanResult) {
          if (booleanResult.getStatus().isSuccess()) {
            delegate.onResult(booleanResult.getValue());
          } else {
            // We could not make the call so must assume it is not available
            delegate.onResult(false);
          }
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

  private PayHelper() {
  }
}
