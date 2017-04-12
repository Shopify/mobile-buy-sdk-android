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

package com.shopify.sample.domain.model;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import static com.shopify.sample.util.Util.checkNotBlank;

public final class Payment {
  @NonNull public final String id;
  @Nullable public final String errorMessage;
  public final boolean ready;
  @Nullable public final Transaction transaction;

  public Payment(@NonNull final String id, @Nullable final String errorMessage, final boolean ready,
    @Nullable final Transaction transaction) {
    this.id = checkNotBlank(id, "id == null");
    this.errorMessage = errorMessage;
    this.ready = ready;
    this.transaction = transaction;
  }

  @Override public String toString() {
    return "Payment{" +
      "id='" + id + '\'' +
      ", errorMessage='" + errorMessage + '\'' +
      ", ready=" + ready +
      ", transaction=" + transaction +
      '}';
  }

  public static final class Transaction {
    @NonNull public final String status;

    public Transaction(@NonNull final String status) {
      this.status = checkNotBlank(status, "status == null");
    }

    @Override public String toString() {
      return "Transaction{" +
        "status='" + status + '\'' +
        '}';
    }
  }
}
