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
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import static com.shopify.buy3.pay.Util.checkNotEmpty;
import static com.shopify.buy3.pay.Util.checkNotNull;

/**
 * Helper class that provides utility functions to simplify the Android Pay checkout flow.
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public final class PayHelper {
  public static final int REQUEST_CODE_MASKED_WALLET = 500;
  public static final int REQUEST_CODE_CHANGE_MASKED_WALLET = 501;
  public static final int REQUEST_CODE_FULL_WALLET = 502;
  public static final Map<CardNetworkType, Integer> SUPPORTED_CARD_NETWORKS_MAP;

  static {
    Map<CardNetworkType, Integer> supportedCardNetworkMap = new HashMap<>(5);
    supportedCardNetworkMap.put(CardNetworkType.VISA, WalletConstants.CardNetwork.VISA);
    supportedCardNetworkMap.put(CardNetworkType.MASTERCARD, WalletConstants.CardNetwork.MASTERCARD);
    supportedCardNetworkMap.put(CardNetworkType.DISCOVER, WalletConstants.CardNetwork.DISCOVER);
    supportedCardNetworkMap.put(CardNetworkType.AMERICAN_EXPRESS, WalletConstants.CardNetwork.AMEX);
    supportedCardNetworkMap.put(CardNetworkType.JCB, WalletConstants.CardNetwork.JCB);
    SUPPORTED_CARD_NETWORKS_MAP = Collections.unmodifiableMap(supportedCardNetworkMap);
  }

  /**
   * Requests Masked Wallet for provided {@link PayCart}.
   *
   * @param googleApiClient     {@link GoogleApiClient}
   * @param payCart             {@link PayCart}
   * @param androidPayPublicKey Android Pay public key
   */
  public static void requestMaskedWallet(final GoogleApiClient googleApiClient, final PayCart payCart, final String androidPayPublicKey) {
    MaskedWalletRequest maskedWalletRequest = payCart.maskedWalletRequest(androidPayPublicKey);
    Wallet.Payments.loadMaskedWallet(googleApiClient, maskedWalletRequest, PayHelper.REQUEST_CODE_MASKED_WALLET);
  }

  /**
   * Creates Masked Wallet with new Google Transaction Id from existing one.
   *
   * @param googleApiClient {@link GoogleApiClient}
   * @param maskedWallet    {@link MaskedWallet} previous Masked Wallet
   */
  public static void newMaskedWallet(final GoogleApiClient googleApiClient, final MaskedWallet maskedWallet) {
    Wallet.Payments.changeMaskedWallet(googleApiClient, maskedWallet.getGoogleTransactionId(), null, PayHelper.REQUEST_CODE_MASKED_WALLET);
  }

  /**
   * Initializes Android Pay wallet fragment with provided Masked Wallet.
   *
   * @param walletFragment {@link SupportWalletFragment}
   * @param maskedWallet   {@link MaskedWallet}
   */
  public static void initializeWalletFragment(final SupportWalletFragment walletFragment, final MaskedWallet maskedWallet) {
    WalletFragmentInitParams initParams = WalletFragmentInitParams.newBuilder()
      .setMaskedWallet(maskedWallet)
      .setMaskedWalletRequestCode(PayHelper.REQUEST_CODE_CHANGE_MASKED_WALLET)
      .build();
    walletFragment.initialize(initParams);
  }

  /**
   * Requests full wallet for provided {@link PayCart} and {@link MaskedWallet}.
   *
   * @param googleApiClient {@link GoogleApiClient}
   * @param payCart         {@link PayCart}
   * @param maskedWallet    {@link MaskedWallet}
   */
  public static void requestFullWallet(final GoogleApiClient googleApiClient, final PayCart payCart, final MaskedWallet maskedWallet) {
    FullWalletRequest fullWalletRequest = payCart.fullWalletRequest(maskedWallet);
    Wallet.Payments.loadFullWallet(googleApiClient, fullWalletRequest, PayHelper.REQUEST_CODE_FULL_WALLET);
  }

  /**
   * Handles activity results for masked wallet and full wallet requests, and delegates a response to a specified call back handler.
   *
   * @param requestCode Android Pay Wallet activity request code
   * @param resultCode  Android Pay Wallet activity result code
   * @param data        Android Pay Wallet response data
   * @param handler     delegate call back handler
   * @return {@code true} if activity result can be handled, {@code false} otherwise
   * @see WalletResponseHandler
   */
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

    if (data == null) {
      handler.onWalletError(requestCode, -1);
      return true;
    }

    int errorCode = data.getIntExtra(WalletConstants.EXTRA_ERROR_CODE, -1);
    if (errorCode != -1) {
      handler.onWalletError(requestCode, errorCode);
      return true;
    }

    MaskedWallet maskedWallet = data.getParcelableExtra(WalletConstants.EXTRA_MASKED_WALLET);
    FullWallet fullWallet = data.getParcelableExtra(WalletConstants.EXTRA_FULL_WALLET);
    if (maskedWallet != null) {
      handler.onMaskedWallet(maskedWallet);
      return true;
    } else if (fullWallet != null) {
      handler.onFullWallet(fullWallet);
      return true;
    }

    return false;
  }

  /**
   * Checks if Android Pay is enabled in the Android manifest file.
   *
   * @param context Android context
   * @return {@code true} if Android Pay enabled, {@code false} otherwise
   */
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
   * Checks if Android Pay is enabled and ready to use.
   *
   * @param context               {@link Context}
   * @param apiClient             {@link GoogleApiClient}
   * @param supportedCardNetworks list of supported credit card networks for current checkout
   * @param delegate              callback result will be delegated to
   * @throws IllegalArgumentException in case of unsupported card network been provided with {@code supportedCardNetworks}
   */
  public static void isReadyToPay(@NonNull final Context context, @NonNull final GoogleApiClient apiClient,
    @NonNull final Set<CardNetworkType> supportedCardNetworks, @NonNull final AndroidPayReadyCallback delegate) {
    checkNotNull(apiClient, "apiClient can't be null");
    checkNotNull(delegate, "delegate can't be null");
    checkNotNull(supportedCardNetworks, "supportedCardNetworks can't be null");

    Set<Integer> cardNetworks = convertSupportedCardNetworks(supportedCardNetworks);
    if (cardNetworks.isEmpty()) {
      cardNetworks = new HashSet<>(SUPPORTED_CARD_NETWORKS_MAP.values());
    }

    // Make sure that the device supports SHA-256 and UTF-8 (required by hashing Android Pay public key for payment token creation)
    try {
      MessageDigest.getInstance("SHA-256");
      byte[] ignore = "foo".getBytes("UTF-8");
    } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
      // If not, then the Android Pay feature should be disabled
      delegate.onResult(false);
      return;
    }

    GoogleApiAvailability googleAPI = GoogleApiAvailability.getInstance();
    if (googleAPI.isGooglePlayServicesAvailable(context) != ConnectionResult.SUCCESS) {
      delegate.onResult(false);
      return;
    }

    IsReadyToPayRequest.Builder payRequestBuilder = IsReadyToPayRequest.newBuilder();
    for (Integer cardNetwork : cardNetworks) {
      payRequestBuilder.addAllowedCardNetwork(cardNetwork);
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

  /**
   * Extracts payment token from {@link FullWallet} information that will be required to complete checkout.
   *
   * @param fullWallet          {@link FullWallet}
   * @param androidPayPublicKey Android Pay public key
   * @return payment token
   */
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
   * Interface for receiving results from {@link PayHelper#isReadyToPay(Context, GoogleApiClient, Set, AndroidPayReadyCallback)}.
   */
  public interface AndroidPayReadyCallback {
    void onResult(boolean result);
  }

  static Set<Integer> convertSupportedCardNetworks(@NonNull final Set<CardNetworkType> supportedCardNetworks) {
    checkNotNull(supportedCardNetworks, "supportedCardNetworks can't be null");

    Set<Integer> cardNetworks = new LinkedHashSet<>(supportedCardNetworks.size());
    for (CardNetworkType type : supportedCardNetworks) {
      Integer cardNetwork = SUPPORTED_CARD_NETWORKS_MAP.get(type);
      if (cardNetwork != null) {
        cardNetworks.add(cardNetwork);
      }
    }

    return Collections.unmodifiableSet(cardNetworks);
  }

  /**
   * Callback for handling wallet activity results from {@link PayHelper#handleWalletResponse(int, int, Intent, WalletResponseHandler)}.
   */
  public abstract static class WalletResponseHandler {

    /**
     * Called when wallet request has failed.
     *
     * @param requestCode wallet request code. One of {@link PayHelper#REQUEST_CODE_MASKED_WALLET},
     *                    {@link PayHelper#REQUEST_CODE_CHANGE_MASKED_WALLET}, {@link PayHelper#REQUEST_CODE_FULL_WALLET}
     * @param errorCode   error code
     */
    public abstract void onWalletError(int requestCode, int errorCode);

    /**
     * Called when new Masked Wallet is returned.
     *
     * @param maskedWallet {@link MaskedWallet}
     */
    public void onMaskedWallet(MaskedWallet maskedWallet) {
    }

    /**
     * Called when new full wallet is returned.
     *
     * @param fullWallet {@link FullWallet}
     */
    public void onFullWallet(FullWallet fullWallet) {
    }

    /**
     * Called when wallet request has been canceled by user.
     *
     * @param requestCode one of {@link PayHelper#REQUEST_CODE_MASKED_WALLET},
     *                    {@link PayHelper#REQUEST_CODE_CHANGE_MASKED_WALLET}, {@link PayHelper#REQUEST_CODE_FULL_WALLET}
     */
    public void onWalletRequestCancel(int requestCode) {
    }
  }

  private PayHelper() {
  }
}
