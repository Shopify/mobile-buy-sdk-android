/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 Shopify Inc.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.shopify.sample.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.view.ViewPager;
import android.support.v7.graphics.Palette;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.shopify.buy.R;
import com.shopify.buy.model.Image;
import com.shopify.sample.utils.ImageUtility;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.Arrays;
import java.util.List;

/**
 * A {@link ViewPagerAdapter} that loads images and uses {@link Palette} to set a suitable background color.
 */
public class ProductImagePagerAdapter extends ViewPagerAdapter {

    public interface BackgroundColorListener {
        void onBackgroundColorFound(int color, int position);
        boolean shouldShowBackgroundColor();
    }

    private final int maxWidth;
    private final int maxHeight;
    private final List<Image> images;
    private final BackgroundColorListener bgColorListener;
    private final int defaultBackgroundColor;

    private Bitmap[] bitmaps = null;

    public ProductImagePagerAdapter(List<Image> images, int maxWidth, int maxHeight, BackgroundColorListener bgColorListener, int defaultBackgroundColor) {
        this.images = images;
        this.maxWidth = maxWidth;
        this.maxHeight = maxHeight;
        this.bgColorListener = bgColorListener;
        this.defaultBackgroundColor = defaultBackgroundColor;
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    private String stripQueryFromUrl(String url) {
        return url.substring(0, url.lastIndexOf('?'));
    }

    public List<Image> getImages() {
        return images;
    }

    @Override
    public View getView(final int pos, ViewPager pager) {
        Context context = pager.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ViewGroup layout = (ViewGroup) inflater.inflate(R.layout.view_product_image, pager, false);
        layout.setTag(pos);

        final View imageLoadingIndicator = layout.findViewById(R.id.image_loading_indicator);
        final ImageView imageView = (ImageView) layout.findViewById(R.id.image);

        if (bitmaps == null) {
            bitmaps = new Bitmap[images.size()];
            Arrays.fill(bitmaps, null);
        }

        final boolean needBitmap = bitmaps[pos] == null;
        if (!needBitmap) {
            imageLoadingIndicator.setVisibility(View.INVISIBLE);
            imageView.setImageBitmap(bitmaps[pos]);

        } else {
            // We don't ask Picasso to show a placeholder because we want to scale the placeholder differently than
            // the product image. The placeholder should be displayed using CENTER_INSIDE, while the image should be
            // loaded using Picasso's fit() request method (to reduce memory usage during decoding) and then cropped
            // or fitted into the target view.
            String imageUrl = stripQueryFromUrl(images.get(pos).getSrc());
            ImageUtility.loadRemoteImageIntoViewWithoutSize(Picasso.with(context), imageUrl, imageView, maxWidth, maxHeight, false, new Callback() {
                @Override
                public void onSuccess() {
                    bitmaps[pos] = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
                    imageLoadingIndicator.setVisibility(View.INVISIBLE);
                    addBackgroundColor(pos);
                }

                @Override
                public void onError() {
                    imageLoadingIndicator.setVisibility(View.VISIBLE);
                }
            });
        }

        return layout;
    }

    @Override
    public void destroyItem (ViewGroup container, int position, Object object) {
        Context context = container.getContext();
        if (context != null) {
            View view = container.findViewWithTag(position);
            ImageView imageView = (ImageView) view.findViewById(R.id.image);
            Picasso.with(context).cancelRequest(imageView);
        }
        super.destroyItem(container, position, object);
    }

    @Override
    public int getCount() {
        if (images == null) {
            return 0;
        }
        return images.size();
    }

    private void addBackgroundColor(final int position) {
        // Extract a prominent color from the product image and use it as the background behind the image
        if (bgColorListener != null) {
            if (!bgColorListener.shouldShowBackgroundColor()) {
                return;
            }

            try {
                Palette.from(bitmaps[position]).generate(new Palette.PaletteAsyncListener() {
                    public void onGenerated(Palette palette) {
                        int color = palette.getDarkMutedColor(0);
                        if (color == 0) {
                            color = palette.getDarkVibrantColor(0);
                        }
                        if (color == 0) {
                            color = palette.getLightMutedColor(0);
                        }
                        if (color == 0) {
                            color = palette.getLightVibrantColor(0);
                        }
                        if (color == 0) {
                            color = defaultBackgroundColor;
                        }
                        bgColorListener.onBackgroundColorFound(color, position);
                    }
                });
            } catch (Exception ignored) {}
        }
    }

}
