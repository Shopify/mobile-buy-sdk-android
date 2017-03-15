package com.shopify.sample.view.widget.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.shopify.sample.R;
import com.shopify.sample.util.Util;

import java.util.List;

@SuppressWarnings("WeakerAccess")
final class ImageGalleryViewPageAdapter extends PagerAdapter {
  final LayoutInflater layoutInflater;
  final List<String> images;
  final OnItemClickListener onItemClickListener;

  ImageGalleryViewPageAdapter(@NonNull final Context context, @NonNull final List<String> images,
    @NonNull final OnItemClickListener onItemClickListener) {
    Util.checkNotNull(context, "context == null");
    Util.checkNotNull(images, "images == null");
    Util.checkNotNull(onItemClickListener, "onClickListener == null");

    this.layoutInflater = LayoutInflater.from(context);
    this.images = images;
    this.onItemClickListener = onItemClickListener;
  }

  @Override
  public int getCount() {
    return images.size();
  }

  @Override
  public boolean isViewFromObject(android.view.View view, Object object) {
    return view == object;
  }

  @Override
  public int getItemPosition(Object object) {
    return POSITION_NONE;
  }

  @Override
  public Object instantiateItem(final ViewGroup container, final int position) {
    ShopifyDraweeView imageView = (ShopifyDraweeView) layoutInflater.inflate(R.layout.image_gallery_page, container, false);
    imageView.setOnClickListener(v -> onItemClickListener.onImageClick(copyImageToBitmap(imageView)));
    imageView.loadShopifyImage(images.get(position));
    container.addView(imageView);
    return imageView;
  }

  @NonNull
  private Bitmap copyImageToBitmap(ShopifyDraweeView imageView) {
    RectF imageBounds = new RectF();
    imageView.getHierarchy().getActualImageBounds(imageBounds);

    Bitmap bitmap = Bitmap.createBitmap((int) imageBounds.width(), (int) imageBounds.height(), Bitmap.Config.ARGB_8888);
    Canvas canvas = new Canvas(bitmap);
    Drawable drawable = imageView.getDrawable();
    Rect priorBounds = new Rect(drawable.getBounds());
    drawable.setBounds(0, 0, (int) imageBounds.width(), (int) imageBounds.height());
    drawable.draw(canvas);
    drawable.setBounds(priorBounds);

    return bitmap;
  }

  @Override
  public void destroyItem(final ViewGroup container, final int position, final Object object) {
    container.removeView((android.view.View) object);
  }

  interface OnItemClickListener {
    void onImageClick(Bitmap image);
  }
}
