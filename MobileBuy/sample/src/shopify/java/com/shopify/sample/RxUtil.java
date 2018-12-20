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

package com.shopify.sample;

import com.shopify.buy3.GraphCall;
import com.shopify.buy3.GraphCallResult;
import com.shopify.buy3.GraphResponse;
import com.shopify.buy3.Storefront;
import com.shopify.graphql.support.AbstractResponse;
import io.reactivex.Single;
import io.reactivex.SingleTransformer;
import kotlin.Unit;

import static com.shopify.sample.util.Util.fold;

public final class RxUtil {

  public static Single<Storefront.QueryRoot> rxGraphQueryCall(final GraphCall<Storefront.QueryRoot> call) {
    return Single.<GraphResponse<Storefront.QueryRoot>>create(emitter -> {
      emitter.setCancellable(call::cancel);
      call.enqueue(result -> {
        if (result instanceof GraphCallResult.Success) {
          emitter.onSuccess(((GraphCallResult.Success<Storefront.QueryRoot>) result).getResponse());
        } else {
          emitter.onError(((GraphCallResult.Failure) result).getError());
        }
        return Unit.INSTANCE;
      });
    }).compose(queryResponseDataTransformer());
  }

  public static Single<Storefront.Mutation> rxGraphMutationCall(final GraphCall<Storefront.Mutation> call) {
    return Single.<GraphResponse<Storefront.Mutation>>create(emitter -> {
      emitter.setCancellable(call::cancel);
      call.enqueue(result -> {
        if (result instanceof GraphCallResult.Success) {
          emitter.onSuccess(((GraphCallResult.Success<Storefront.Mutation>) result).getResponse());
        } else {
          emitter.onError(((GraphCallResult.Failure) result).getError());
        }
        return Unit.INSTANCE;
      });
    }).compose(queryResponseDataTransformer());
  }

  private static <T extends AbstractResponse<T>> SingleTransformer<GraphResponse<T>, T> queryResponseDataTransformer() {
    return upstream -> upstream.flatMap(response -> {
      if (response.getHasErrors()) {
        String errorMessage = fold(new StringBuilder(), response.getErrors(),
            (builder, error) -> builder.append(error.message()).append("\n")).toString();
        return Single.error(new RuntimeException(errorMessage));

      } else {
        return Single.just(response.getData());
      }
    });
  }

  private RxUtil() {
  }
}