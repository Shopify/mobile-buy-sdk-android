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

import android.arch.lifecycle.LiveData;

@SuppressWarnings("WeakerAccess")
public final class ProgressLiveData extends LiveData<ProgressLiveData.Progress> {

  public void show(final int requestId) {
    setValue(new Progress(requestId, true));
  }

  public void hide(final int requestId) {
    Progress progress = getValue();
    if (progress == null || !progress.show || progress.requestId != requestId) {
      return;
    }
    setValue(new Progress(requestId, false));
  }

  public void hide() {
    Progress progress = getValue();
    if (progress == null || !progress.show) {
      return;
    }
    setValue(new Progress(progress.requestId, false));
  }

  public static final class Progress {
    public final int requestId;
    public final boolean show;

    private Progress(final int requestId, final boolean show) {
      this.requestId = requestId;
      this.show = show;
    }
  }
}
