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

import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.google.gson.stream.JsonReader
import com.shopify.buy3.GraphError
import com.shopify.buy3.GraphResponse
import com.shopify.graphql.support.AbstractResponse
import com.shopify.graphql.support.TopLevelResponse
import okhttp3.Response
import timber.log.Timber

typealias GraphQLResponseDataExtractor<T> = (response: TopLevelResponse) -> T

internal class HttpResponseParser<T : AbstractResponse<T>>(private val extractResponseData: GraphQLResponseDataExtractor<T>) {

    @Throws(GraphError::class)
    fun parse(response: Response): GraphResponse<T> {
        val topLevelResponse = parseTopLevelResponse(response.successResponse())
        try {
            val data = if (topLevelResponse.data != null) extractResponseData(topLevelResponse) else null
            return GraphResponse(data, topLevelResponse.errors)
        } catch (e: Exception) {
            Timber.w(e, "failed to process GraphQL response")
            throw GraphError.Unknown("Failed to process GraphQL response ", e)
        }
    }

    @Throws(GraphError::class)
    private fun Response.successResponse(): Response = apply {
        if (!isSuccessful) {
            use { throw GraphError.HttpError(it) }
        }
    }

    @Throws(GraphError::class)
    private fun parseTopLevelResponse(response: Response): TopLevelResponse {
        try {
            val reader = JsonReader(response.body?.charStream())
            val root = JsonParser().parse(reader) as JsonObject
            return TopLevelResponse(root)
        } catch (e: Exception) {
            Timber.w(e, "failed to parse GraphQL response")
            throw GraphError.ParseError("Failed to parse GraphQL http response", e)
        } finally {
            response.close()
        }
    }
}
