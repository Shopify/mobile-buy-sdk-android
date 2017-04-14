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

import android.support.annotation.NonNull;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import com.shopify.graphql.support.AbstractResponse;
import com.shopify.graphql.support.TopLevelResponse;

import okhttp3.Response;

import static com.shopify.buy3.Utils.checkNotNull;

final class HttpResponseParser<T extends AbstractResponse<T>> {
  private final RealGraphCall.ResponseDataConverter<T> responseDataConverter;

  HttpResponseParser(@NonNull final RealGraphCall.ResponseDataConverter<T> responseDataConverter) {
    this.responseDataConverter = checkNotNull(responseDataConverter, "responseDataConverter == null");
  }

  GraphResponse<T> parse(final Response response) throws GraphError {
    TopLevelResponse topLevelResponse = parseTopLevelResponse(checkResponse(response));
    try {
      if (topLevelResponse.getErrors() != null && !topLevelResponse.getErrors().isEmpty()) {
        return new GraphResponse<T>(null, topLevelResponse.getErrors());
      } else {
        T data = responseDataConverter.convert(topLevelResponse);
        return new GraphResponse<T>(data, topLevelResponse.getErrors());
      }
    } catch (Exception e) {
      throw new GraphError("Failed to process GraphQL response ", e);
    }
  }

  private Response checkResponse(final Response httpResponse) throws GraphError {
    if (!httpResponse.isSuccessful()) {
      throw new GraphInvalidResponseError(httpResponse);
    }
    return httpResponse;
  }

  private TopLevelResponse parseTopLevelResponse(final Response response) throws GraphError {
    try {
      JsonReader reader = new JsonReader(response.body().charStream());
      JsonObject root = (JsonObject) new JsonParser().parse(reader);
      return new TopLevelResponse(root);
    } catch (Exception e) {
      throw new GraphParseError("Failed to parse GraphQL http response", e);
    } finally {
      response.close();
    }
  }
}
