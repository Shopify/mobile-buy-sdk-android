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

package com.shopify.buy3;

import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.io.IOException;

/**
 * <p>Call to card server for vaulting credit card.</p>
 * Credit cards cannot be sent to the checkout API directly. They must be sent to the card vault which in response will return an token.
 * This token should be used for completion checkout with credit card.
 */
@SuppressWarnings("WeakerAccess")
public interface CreditCardVaultCall {

  /**
   * Cancels this call if possible.
   */
  void cancel();

  /**
   * Checks if this call has been canceled.
   *
   * @return {@code true} if this call has been canceled otherwise {@code false}
   */
  boolean isCanceled();

  /**
   * Creates a new, identical call to this one, which can be enqueued or executed even if this call has already been executed or canceled.
   *
   * @return {@link CreditCardVaultCall} cloned call
   */
  @NonNull CreditCardVaultCall clone();

  /**
   * Sends credit card vault request immediately and blocks until the response can be processed or is an error.
   *
   * @return stored credit card vault token
   * @throws IOException           if request failed
   * @throws IllegalStateException when the call has already been executed
   */
  @NonNull String execute() throws IOException;

  /**
   * Schedules this call to be executed at some point in the future.
   *
   * @param callback {@link CreditCardVaultCall.Callback} to handle the response or a failure
   * @return {@link CreditCardVaultCall} scheduled for execution
   * @throws IllegalStateException when the call has already been executed
   * @see GraphCall.Callback
   */
  @NonNull CreditCardVaultCall enqueue(@NonNull CreditCardVaultCall.Callback callback);

  /**
   * Schedules the call to be executed at some point in the future.
   *
   * @param callback        {@link CreditCardVaultCall.Callback} to handle the response or a failure
   * @param callbackHandler the callback will be running on the thread to which this handler is attached to
   * @return {@link CreditCardVaultCall} scheduled for execution
   * @throws IllegalStateException when the call has already been executed
   * @see CreditCardVaultCall.Callback
   */
  @NonNull CreditCardVaultCall enqueue(@NonNull CreditCardVaultCall.Callback callback, @Nullable Handler callbackHandler);

  /**
   * <p>Callback for {@link CreditCardVaultCall}</p>
   * Callback used to handle response of the scheduled for execution call.
   */
  interface Callback {
    /**
     * Called when credit card vault token is received and parsed successfully.
     *
     * @param token stored credit card vault token
     */
    void onResponse(@NonNull String token);

    /**
     * Called when call could not be executed due to cancellation, timeouts, network failure, parsing error, etc.
     *
     * @param error that has been thrown during call execution
     */
    void onFailure(@NonNull IOException error);
  }
}
