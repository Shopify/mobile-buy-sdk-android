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

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.shopify.sample.util.Util;

public abstract class ScreenActionEvent implements Parcelable {
  protected @NonNull final String action;
  protected @NonNull final Bundle payload;

  protected ScreenActionEvent(@NonNull final String action) {
    this(action, new Bundle());
  }

  protected ScreenActionEvent(@NonNull final String action, @NonNull final Bundle payload) {
    Util.checkNotNull(action, "action == null");
    Util.checkNotNull(payload, "payload == null");
    this.action = action;
    this.payload = payload;
  }

  protected ScreenActionEvent(Parcel in) {
    action = in.readString();
    payload = in.readBundle();
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(action);
    dest.writeBundle(payload);
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @NonNull public String action() {
    return action;
  }

  @NonNull public Bundle payload() {
    return payload;
  }

  @Override public boolean equals(final Object o) {
    if (this == o) return true;
    if (!(o instanceof ScreenActionEvent)) return false;

    final ScreenActionEvent that = (ScreenActionEvent) o;

    return action.equals(that.action);

  }

  @Override public int hashCode() {
    return action.hashCode();
  }

  @Override public String toString() {
    return "ScreenActionEvent{" +
      "action='" + action + '\'' +
      ", payload=" + payload +
      '}';
  }
}
