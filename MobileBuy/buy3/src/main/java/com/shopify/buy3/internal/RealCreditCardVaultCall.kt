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
package com.shopify.buy3.internal

import android.os.Handler
import android.support.annotation.VisibleForTesting
import com.shopify.buy3.CardVaultResult
import com.shopify.buy3.CardVaultResultCallback
import com.shopify.buy3.CreditCard
import com.shopify.buy3.CreditCardVaultCall
import okhttp3.Call
import okhttp3.HttpUrl
import okhttp3.MediaType
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.Response
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicReference

private const val ACCEPT_HEADER_NAME = "Accept"
private const val ACCEPT_HEADER = "application/json"
private val CONTENT_MEDIA_TYPE = MediaType.parse("application/json; charset=utf-8")

internal class RealCreditCardVaultCall(
    internal val creditCard: CreditCard,
    internal val endpointUrl: HttpUrl,
    internal val httpCallFactory: Call.Factory
) : CreditCardVaultCall {
    private val executed = AtomicBoolean()
    private var httpCall: Call? = null
    private var resultCallback: VaultHttpCallback? = null

    @Volatile override var isCanceled: Boolean = false
        private set

    override fun cancel() {
        isCanceled = true

        synchronized(this) {
            httpCall?.cancel()
            httpCall = null

            resultCallback?.cancel()
            resultCallback = null
        }
    }

    override fun clone(): CreditCardVaultCall {
        return RealCreditCardVaultCall(creditCard, endpointUrl, httpCallFactory)
    }

    override fun enqueue(callbackHandler: Handler?, callback: CardVaultResultCallback): CreditCardVaultCall {
        if (!executed.compareAndSet(false, true)) {
            throw IllegalStateException("Already Executed")
        }

        if (isCanceled) {
            val action = { callback(CardVaultResult.Failure(IllegalStateException("Call was canceled"))) }
            callbackHandler?.post(action) ?: action()
            return this
        }

        val payload = try {
            creditCard.toJson()
        } catch (e: JSONException) {
            val action = { callback(CardVaultResult.Failure(RuntimeException("Failed to serialize credit card", e))) }
            callbackHandler?.post(action) ?: action()
            return this
        }

        synchronized(this) {
            resultCallback = VaultHttpCallback {
                callbackHandler?.post { callback(it) } ?: callback(it)
            }
            httpCall = httpCallFactory.newVaultHttpCall(endpointUrl = endpointUrl, payload = payload)
                .apply { enqueue(resultCallback) }
        }

        return this
    }
}

private class VaultHttpCallback(val callback: CardVaultResultCallback) : okhttp3.Callback {
    private val originalCallbackRef = AtomicReference(callback)

    override fun onFailure(call: Call, e: IOException) {
        originalCallbackRef.getAndSet(null)?.also { callback ->
            callback(CardVaultResult.Failure(e))
        }
    }

    override fun onResponse(call: Call, response: Response) {
        originalCallbackRef.getAndSet(null)?.also { callback ->
            val token = try {
                response.parseVaultToken()
            } catch (e: Exception) {
                callback(CardVaultResult.Failure(e))
                return
            }
            callback(CardVaultResult.Success(token))
        }
    }

    fun cancel() {
        originalCallbackRef.getAndSet(null)?.also { callback ->
            callback(CardVaultResult.Failure(IllegalStateException("Call was canceled")))
        }
    }
}

private fun Call.Factory.newVaultHttpCall(endpointUrl: HttpUrl, payload: String): Call {
    val body = RequestBody.create(CONTENT_MEDIA_TYPE, payload)
    val request = Request.Builder()
        .url(endpointUrl)
        .post(body)
        .header(ACCEPT_HEADER_NAME, ACCEPT_HEADER)
        .build()
    return newCall(request)
}

@Throws(JSONException::class)
private fun CreditCard.toJson(): String {
    return JSONObject()
        .put(
            "credit_card", JSONObject()
                .put("number", number)
                .put("first_name", firstName)
                .put("last_name", lastName)
                .put("month", expireMonth)
                .put("year", expireYear)
                .put("verification_value", verificationCode)
        ).toString()
}

@Throws(IOException::class)
private fun Response.parseVaultToken(): String {
    return if (isSuccessful) {
        try {
            val responseJsonObject = JSONObject(body().string())
            responseJsonObject.getString("id")
        } catch (e: Exception) {
            throw IOException("Failed to parse vault response", e)
        }
    } else {
        throw IOException("Failed to vault credit card: HTTP(${code()} {${message()}}")
    }
}