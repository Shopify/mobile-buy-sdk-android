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

package com.shopify.sample.view;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

import static com.shopify.sample.util.Util.checkNotNull;

public final class ProgressDialogHelper {
  private final ProgressDialog dialog;
  private final AtomicLong requestId = new AtomicLong(0);
  private final AtomicBoolean canBeShown = new AtomicBoolean(true);
  private final Handler uiHandler = new Handler();

  public ProgressDialogHelper(@NonNull final Context context) {
    dialog = new ProgressDialog(checkNotNull(context, "context == null"));
    dialog.setIndeterminate(true);
    dialog.setCancelable(true);
  }

  public boolean show(@Nullable final String title, @Nullable final String message, @Nullable final Runnable onCancel) {
    return show(0, title, message, onCancel);
  }

  public boolean show(final long requestId, @Nullable final String title, @Nullable final String message,
    @Nullable final Runnable onCancel) {
    uiHandler.post(() -> {
      this.requestId.set(requestId);
      dialog.setCanceledOnTouchOutside(false);
      dialog.setTitle(title);
      dialog.setMessage(message);
      dialog.setOnCancelListener(dialog -> {
        dialog.dismiss();
        if (onCancel != null) {
          onCancel.run();
        }
      });
      dialog.show();
    });
    return true;
  }

  public void dismiss() {
    canBeShown.set(true);
    uiHandler.post(dialog::dismiss);
  }

  public boolean dismiss(final long requestId) {
    if (this.requestId.compareAndSet(requestId, 0)) {
      canBeShown.set(true);
      uiHandler.post(dialog::dismiss);
      return true;
    } else {
      return false;
    }
  }
}
