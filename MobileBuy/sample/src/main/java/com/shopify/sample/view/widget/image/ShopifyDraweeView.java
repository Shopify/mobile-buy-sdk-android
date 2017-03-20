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

package com.shopify.sample.view.widget.image;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.AttributeSet;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.shopify.sample.R;
import com.shopify.sample.util.ImageUtility;

public final class ShopifyDraweeView extends SimpleDraweeView {
  private String shopifyImageBaseUrl;
  private int lastMeasureWidth = -1;
  private int lastMeasureHeight = -1;

  public ShopifyDraweeView(final Context context, final GenericDraweeHierarchy hierarchy) {
    super(context, hierarchy);
  }

  public ShopifyDraweeView(final Context context) {
    super(context);
  }

  public ShopifyDraweeView(final Context context, final AttributeSet attrs) {
    super(context, attrs);
  }

  public ShopifyDraweeView(final Context context, final AttributeSet attrs, final int defStyle) {
    super(context, attrs, defStyle);
  }

  public void loadShopifyImage(final String imageUrl) {
    shopifyImageBaseUrl = imageUrl;

    if (TextUtils.isEmpty(shopifyImageBaseUrl)) {
      final ImageRequest imageRequest = ImageRequestBuilder.newBuilderWithSource(Uri.EMPTY).build();
      loadImage(imageRequest);
      return;
    }

    final int measuredWidth = getMeasuredWidth();
    final int measuredHeight = getMeasuredHeight();
    performLoadShopifyImage(measuredWidth, measuredHeight);
  }

  public void loadImage(@NonNull final ImageRequest imageRequest) {
    setController(Fresco
      .newDraweeControllerBuilder()
      .setOldController(getController())
      .setImageRequest(imageRequest)
      .build());
  }

  @Override
  protected void onMeasure(final int widthMeasureSpec, final int heightMeasureSpec) {
    int newWidthMeasureSpec = widthMeasureSpec;
    if (MeasureSpec.getMode(newWidthMeasureSpec) == MeasureSpec.EXACTLY) {
      final Integer preferredWidth = (Integer) getTag(R.attr.preferred_width);
      if (preferredWidth != null && preferredWidth > 0) {
        newWidthMeasureSpec = MeasureSpec.makeMeasureSpec(preferredWidth, MeasureSpec.EXACTLY);
      }
    }

    int newHeightMeasureSpec = heightMeasureSpec;
    if (MeasureSpec.getMode(newHeightMeasureSpec) == MeasureSpec.EXACTLY) {
      final Integer preferredHeight = (Integer) getTag(R.attr.preferred_height);
      if (preferredHeight != null && preferredHeight > 0) {
        newHeightMeasureSpec = MeasureSpec.makeMeasureSpec(preferredHeight, MeasureSpec.EXACTLY);
      }
    }

    super.onMeasure(newWidthMeasureSpec, newHeightMeasureSpec);

    final int measuredWidth = getMeasuredWidth();
    final int measuredHeight = getMeasuredHeight();
    if (!TextUtils.isEmpty(shopifyImageBaseUrl)) {
      if (lastMeasureWidth != measuredWidth || lastMeasureHeight != measuredHeight) {
        lastMeasureWidth = measuredWidth;
        lastMeasureHeight = measuredHeight;
        performLoadShopifyImage(measuredWidth, measuredHeight);
      }
    }
  }

  private void performLoadShopifyImage(final int width, final int height) {
    if (width > 0 && height > 0) {
      final ImageRequest imageRequest = getShopifyImageRequest(shopifyImageBaseUrl, width, height);
      postOnAnimation(() -> loadImage(imageRequest));
    }
  }

  private ImageRequest getShopifyImageRequest(final String baseUrl, final int width, final int height) {
    if (TextUtils.isEmpty(baseUrl)) {
      return ImageRequestBuilder.newBuilderWithSource(Uri.EMPTY).build();
    } else {
      final String url = ImageUtility.getSizedImageUrl(baseUrl, width, height);
      final Uri imageUrl = Uri.parse(baseUrl.replace(".jpg", ".progressive.jpg").replace(".jpeg", ".progressive.jpeg"));
      return ImageRequestBuilder
        .newBuilderWithSource(imageUrl)
        .setProgressiveRenderingEnabled(true)
        .build();
    }
  }
}