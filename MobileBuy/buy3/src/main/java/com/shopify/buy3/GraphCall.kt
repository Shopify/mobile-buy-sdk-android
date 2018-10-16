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
import com.shopify.graphql.support.AbstractResponse
import com.shopify.graphql.support.Error
import okhttp3.Response

/**
 * [GraphCall] graph call result handler
 */
typealias GraphCallResultCallback<T> = (result: GraphCallResult<T>) -> Unit

/**
 *
 * A call to the `GraphQL` server.
 *
 * Represents `GraphQL` operation request that has been prepared for execution. Since this class represents a single
 * request/response pair, it can't be executed twice. To execute the request again, use the [GraphCall.clone] method which
 * creates a new identical call object.
 *
 * A call can be canceled.
 *
 * @param <T> subtype of the [AbstractResponse] response object this call serves.
 */
interface GraphCall<T : AbstractResponse<T>> : Cloneable {

    /**
     * Checks if this call has been canceled.
     */
    val isCanceled: Boolean

    /**
     * Cancels this call if possible.
     */
    fun cancel()

    /**
     * Creates a new, identical call to this one which can be enqueued or executed even if this call has already been executed or canceled.
     *
     * @return [GraphCall] cloned call
     */
    override fun clone(): GraphCall<T>

    /**
     * Schedules this call to be executed at some point in the future.
     *
     * @param callback to handle the response or a failure
     * @return [GraphCall] that has been scheduled for execution
     * @throws IllegalStateException when the call has already been executed
     */
    fun enqueue(callback: GraphCallResultCallback<T>): GraphCall<T>

    /**
     * Schedules this call to be executed at some point in the future.
     *
     * @param callbackHandler optional handler the callback will be running on the thread to which this it is attached
     * @param callback to handle the response or a failure
     * @return [GraphCall] that has been scheduled for execution
     * @throws IllegalStateException when the call has already been executed
     */
    fun enqueue(callbackHandler: Handler? = null, callback: GraphCallResultCallback<T>): GraphCall<T>

    /**
     * Schedules this call to be executed at some point in the future with the provided retry handler to repeat subsequent HTTP requests.
     *
     * Can be used for polling `GraphQL` resource that is not ready yet.
     *
     * @param callbackHandler optional handler the callback will be running on the thread to which this it is attached
     * @param retryHandler HTTP request retry policy
     * @param callback to handle the response or a failure
     * @return [GraphCall] that has been scheduled for execution
     * @throws IllegalStateException when the call has already been executed
     *
     * @see RetryHandler
     */
    fun enqueue(callbackHandler: Handler? = null, retryHandler: RetryHandler<T>, callback: GraphCallResultCallback<T>): GraphCall<T>
}

/**
 * Query `GraphQL` operation call.
 *
 * Performs [Storefront.QueryRootQuery] queries that serve [Storefront.QueryRoot] responses.
 */
interface QueryGraphCall : GraphCall<Storefront.QueryRoot> {

    /**
     * Sets http cache policy to be used with this call.
     *
     * @param httpCachePolicy [HttpCachePolicy] new HTTP cache policy
     * @return [QueryGraphCall] with updated cache policy
     * @see HttpCachePolicy
     */
    fun cachePolicy(httpCachePolicy: HttpCachePolicy): QueryGraphCall

    override fun enqueue(callback: GraphCallResultCallback<Storefront.QueryRoot>): QueryGraphCall

    override fun enqueue(callbackHandler: Handler?, callback: GraphCallResultCallback<Storefront.QueryRoot>): QueryGraphCall

    override fun enqueue(
        callbackHandler: Handler?,
        retryHandler: RetryHandler<Storefront.QueryRoot>,
        callback: GraphCallResultCallback<Storefront.QueryRoot>
    ): QueryGraphCall
}

/**
 * Mutation `GraphQL` operation call.
 *
 * Performs [Storefront.MutationQuery] queries that serve [Storefront.Mutation] responses.
 */
interface MutationGraphCall : GraphCall<Storefront.Mutation> {

    override fun enqueue(callback: GraphCallResultCallback<Storefront.Mutation>): MutationGraphCall

    override fun enqueue(callbackHandler: Handler?, callback: GraphCallResultCallback<Storefront.Mutation>): MutationGraphCall

    override fun enqueue(
        callbackHandler: Handler?,
        retryHandler: RetryHandler<Storefront.Mutation>,
        callback: GraphCallResultCallback<Storefront.Mutation>
    ): MutationGraphCall
}

/**
 * Represents `GraphQL` operation execution result.
 */
sealed class GraphCallResult<out T : AbstractResponse<out T>> {
    /**
     * `GraphQL` operation result returned for success execution, with the typed operation response that will contain fields that match
     * exactly the fields requested.
     */
    class Success<T : AbstractResponse<T>>(val response: GraphResponse<T>) : GraphCallResult<T>()

    /**
     * `GraphQL` operation result returned for failed execution, with the error caused this call to fail.
     *
     * @see GraphError
     */
    class Failure(val error: GraphError) : GraphCallResult<Nothing>()
}

/**
 * A `GraphQL` operation response.
 *
 * Response received as a result of [QueryGraphCall] or [MutationGraphCall] execution.
 *
 * ***Note:*** [GraphResponse.data] and [GraphResponse.errors] are not mutually exclusively.
 *
 * @param <T> subtype of parsed [AbstractResponse] data. Can be either [Storefront.QueryRoot] or [Storefront.Mutation]
 */
class GraphResponse<T : AbstractResponse<T>> internal constructor(

    /**
     * Parsed response data.
     *
     * It can be either [Storefront.QueryRoot] or [Storefront.Mutation] for [QueryGraphCall] and
     * [MutationGraphCall] calls respectively.
     */
    val data: T?,

    /**
     * `GraphQL` operation errors.
     *
     * These errors are not meant to be displayed to the end-user. **They are for debug purposes only**.
     */
    val errors: List<Error>
) {

    /**
     * Indication if returned response had any `GraphQL` operation errors.
     */
    val hasErrors: Boolean = errors.isNotEmpty()

    /**
     * Formatted `GraphQL` operation errors as one message.
     */
    val formattedErrorMessage: String
        get() = errors.joinToString(separator = "\n") { it.message() }
}

/**
 * An error encountered during the `GraphQL` operation execution.
 */
sealed class GraphError(message: String? = null, cause: Throwable? = null) : Exception(message, cause) {
    /**
     * Error when there are some issues with the network (connectivity problem, timeouts, etc.).
     */
    class NetworkError(message: String? = null, cause: Throwable? = null) : GraphError(message, cause)

    /**
     * Error when HTTP response status code is not from {@code 200} series.
     */
    class HttpError(rawResponse: Response) : GraphError(message = "HTTP(${rawResponse.code()}) ${rawResponse.message()}") {
        /**
         * HTTP response status code.
         */
        val statusCode = rawResponse.code()
    }

    /**
     * Error when `GraphQL` operation execution was canceled.
     *
     * @see GraphCall.cancel
     */
    class CallCanceledError(message: String? = null, cause: Throwable? = null) : GraphError(message, cause)

    /**
     * Error when `GraphQL` operation was successfully executed but http response is malformed and can't be parsed.
     */
    class ParseError(message: String? = null, cause: Throwable? = null) : GraphError(message, cause)

    /**
     * Unknown error encountered during the `GraphQL` operation execution.
     */
    class Unknown(message: String? = null, cause: Throwable? = null) : GraphError(message, cause)
}