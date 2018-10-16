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
package com.shopify.buy3

import android.os.Handler
import com.shopify.buy3.internal.RealCreditCardVaultCall
import okhttp3.Call
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit

private val DEFAULT_HTTP_CONNECTION_TIME_OUT_MS = TimeUnit.SECONDS.toMillis(10)
private val DEFAULT_HTTP_READ_WRITE_TIME_OUT_MS = TimeUnit.SECONDS.toMillis(20)

/**
 * [CardVaultResult] card vault result handler.
 */
typealias CardVaultResultCallback = (result: CardVaultResult) -> Unit


/**
 * Factory for network calls to the card server
 *
 * Should be shared and reused for all calls to the card server.
 */
class CardClient(internal val httpCallFactory: Call.Factory = defaultOkHttpClient()) {

    /**
     * Creates a call to vault credit card on the server
     *
     * Credit cards cannot be sent to the checkout API directly. They must be sent to the card vault which in response will return an token.
     * This token should be used for completion checkout with credit card.
     *
     * @param creditCard [CreditCard] credit card info
     * @param vaultServerUrl endpoint of card vault returned in [Storefront.PaymentSettings.getCardVaultUrl]
     * @return [CreditCardVaultCall]
     */
    fun vault(creditCard: CreditCard, vaultServerUrl: String): CreditCardVaultCall {
        return RealCreditCardVaultCall(creditCard, HttpUrl.parse(vaultServerUrl), httpCallFactory)
    }
}

private fun defaultOkHttpClient(): OkHttpClient {
    return OkHttpClient.Builder()
        .connectTimeout(DEFAULT_HTTP_CONNECTION_TIME_OUT_MS, TimeUnit.MILLISECONDS)
        .readTimeout(DEFAULT_HTTP_READ_WRITE_TIME_OUT_MS, TimeUnit.MILLISECONDS)
        .writeTimeout(DEFAULT_HTTP_READ_WRITE_TIME_OUT_MS, TimeUnit.MILLISECONDS)
        .build()
}

/**
 * Abstraction for call to the vaulting card server.
 *
 * Credit cards cannot be sent to the checkout API directly. They must be sent to the card vault which in response will return an token.
 *
 * This token should be used for completion checkout with credit card.
 */
interface CreditCardVaultCall {

    /**
     * Checks if this call has been canceled.
     */
    val isCanceled: Boolean

    /**
     * Cancels this call if possible.
     */
    fun cancel()

    /**
     * Creates a new, identical call to this one, which can be enqueued even if this call has already been executed or canceled.
     *
     * @return [CreditCardVaultCall] cloned call
     */
    fun clone(): CreditCardVaultCall

    /**
     * Schedules the call to be executed at some point in the future.
     *
     * @param callback [CardVaultResultCallback] to handle the response or a failure
     * @param callbackHandler optional handler provided callback will be running on the thread to which it is attached to
     * @return [CreditCardVaultCall] scheduled for execution
     * @throws IllegalStateException when the call has already been executed
     */
    fun enqueue(callbackHandler: Handler? = null, callback: CardVaultResultCallback): CreditCardVaultCall
}

/**
 * Represents result of the [CreditCardVaultCall] execution
 */
sealed class CardVaultResult {

    /**
     * Success result with a returned credit card token.
     */
    class Success(val token: String) : CardVaultResult()

    /**
     * Failure result with exception caused an error.
     */
    class Failure(val exception: Exception) : CardVaultResult()
}