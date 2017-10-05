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

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.apollographql.apollo.internal.util.Cancelable;

import javax.annotation.Nonnull;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Single;
import io.reactivex.SingleTransformer;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.Exceptions;

import static com.shopify.sample.util.Util.fold;

public final class RxUtil {

  public static <T> Single<T> rxApolloCall(final ApolloCall<T> call) {
    return Observable.create((ObservableOnSubscribe<Response<T>>) emitter -> {
      cancelOnObservableDisposed(emitter, call);
      call.enqueue(new ApolloCall.Callback<T>() {
        @Override public void onResponse(@Nonnull Response<T> response) {
          if (!emitter.isDisposed()) {
            emitter.onNext(response);
          }
        }

        @Override public void onFailure(@Nonnull ApolloException e) {
          Exceptions.throwIfFatal(e);
          if (!emitter.isDisposed()) {
            emitter.onError(e);
          }
        }

        @Override public void onStatusEvent(@Nonnull ApolloCall.StatusEvent event) {
          if (event == ApolloCall.StatusEvent.COMPLETED && !emitter.isDisposed()) {
            emitter.onComplete();
          }
        }
      });
    })
      .firstOrError()
      .compose(queryResponseDataTransformer());
  }

  private static <T> SingleTransformer<Response<T>, T> queryResponseDataTransformer() {
    return upstream -> upstream.flatMap(response -> {
      if (response.errors().isEmpty()) {
        return Single.just(response.data());
      } else {
        String errorMessage = fold(new StringBuilder(), response.errors(),
          (builder, error) -> builder.append(error.message()).append("\n")).toString();
        return Single.error(new RuntimeException(errorMessage));
      }
    });
  }

  private static <T> void cancelOnObservableDisposed(ObservableEmitter<T> emitter, final Cancelable cancelable) {
    emitter.setDisposable(getRx2Disposable(cancelable));
  }

  private static Disposable getRx2Disposable(final Cancelable cancelable) {
    return new Disposable() {
      @Override public void dispose() {
        cancelable.cancel();
      }

      @Override public boolean isDisposed() {
        return cancelable.isCanceled();
      }
    };
  }

  private RxUtil() {
  }
}
