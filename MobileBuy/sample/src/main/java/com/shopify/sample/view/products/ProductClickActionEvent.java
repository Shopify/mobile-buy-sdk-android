package com.shopify.sample.view.products;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.shopify.sample.presenter.collections.Collection;
import com.shopify.sample.presenter.products.Product;
import com.shopify.sample.view.ScreenActionEvent;

import static com.shopify.sample.util.Util.checkNotNull;

public final class ProductClickActionEvent extends ScreenActionEvent implements Parcelable {
  public static final Creator<ProductClickActionEvent> CREATOR = new Creator<ProductClickActionEvent>() {
    @Override
    public ProductClickActionEvent createFromParcel(Parcel in) {
      return new ProductClickActionEvent(in);
    }

    @Override
    public ProductClickActionEvent[] newArray(int size) {
      return new ProductClickActionEvent[size];
    }
  };

  public static final String ACTION = ProductClickActionEvent.class.getSimpleName();
  private static final String EXTRAS_ID = "product_id";
  private static final String EXTRAS_IMAGE_URL = "product_image_url";
  private static final String EXTRAS_TITLE = "product_title";

  ProductClickActionEvent(@NonNull final Product product) {
    super(ACTION);
    checkNotNull(product, "collectionProduct == null");
    payload.putString(EXTRAS_ID, product.id());
    payload.putString(EXTRAS_IMAGE_URL, product.imageUrl());
    payload.putString(EXTRAS_TITLE, product.title());
  }

  @SuppressWarnings("WeakerAccess") ProductClickActionEvent(Parcel in) {
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
