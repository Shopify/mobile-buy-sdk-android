package com.shopify.sample.view.collections;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.shopify.sample.presenter.collections.Collection;
import com.shopify.sample.view.ScreenActionEvent;

import static com.shopify.sample.util.Util.checkNotNull;

public final class CollectionClickActionEvent extends ScreenActionEvent implements Parcelable {
  public static final Creator<CollectionClickActionEvent> CREATOR = new Creator<CollectionClickActionEvent>() {
    @Override
    public CollectionClickActionEvent createFromParcel(Parcel in) {
      return new CollectionClickActionEvent(in);
    }

    @Override
    public CollectionClickActionEvent[] newArray(int size) {
      return new CollectionClickActionEvent[size];
    }
  };

  public static final String ACTION = CollectionClickActionEvent.class.getSimpleName();
  private static final String EXTRAS_ID = "collection_id";
  private static final String EXTRAS_IMAGE_URL = "collection_image_url";
  private static final String EXTRAS_TITLE = "collection_title";

  CollectionClickActionEvent(@NonNull final Collection collection) {
    super(ACTION);
    checkNotNull(collection, "collection == null");
    payload.putString(EXTRAS_ID, collection.id());
    payload.putString(EXTRAS_IMAGE_URL, collection.imageUrl());
    payload.putString(EXTRAS_TITLE, collection.title());
  }

  @SuppressWarnings("WeakerAccess") CollectionClickActionEvent(Parcel in) {
    super(in);
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    super.writeToParcel(dest, flags);
  }

  @Override
  public int describeContents() {
    return 0;
  }

  public String id() {
    return payload().getString(EXTRAS_ID);
  }

  public String imageUrl() {
    return payload().getString(EXTRAS_IMAGE_URL);
  }

  public String title() {
    return payload().getString(EXTRAS_TITLE);
  }
}
