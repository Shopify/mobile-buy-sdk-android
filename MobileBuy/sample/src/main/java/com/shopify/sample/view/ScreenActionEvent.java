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
