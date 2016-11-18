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
import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;

import com.shopify.sample.R;


/**
 * Describes the colors and backgrounds to use for the {@link ProductDetailsFragmentView}.
 */
public class ProductDetailsTheme implements Parcelable {

    public enum Style {
        DARK,
        LIGHT
    }

    private Style style;
    private int accentColor = -1;
    private boolean showProductImageBackground;

    public ProductDetailsTheme(Resources res) {
        style = Style.LIGHT;
        accentColor = res.getColor(R.color.default_accent);
        showProductImageBackground = true;
    }

    public ProductDetailsTheme(Style style, int accentColor, boolean showProductImageBackground) {
        this.style = style;
        this.accentColor = accentColor;
        this.showProductImageBackground = showProductImageBackground;
    }

    public Style getStyle() {
        return style;
    }

    public void setStyle(Style style) {
        this.style = style;
    }

    public int getAccentColor() {
        return accentColor;
    }

    public void setAccentColor(int accentColor) {
        this.accentColor = accentColor;
    }

    public void setShowProductImageBackground(boolean showProductImageBackground) {
        this.showProductImageBackground = showProductImageBackground;
    }

    public boolean shouldShowProductImageBackground() {
        return showProductImageBackground;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(style.ordinal());
        out.writeInt(accentColor);
        out.writeInt(showProductImageBackground ? 1 : 0);
    }

    public static final Parcelable.Creator<ProductDetailsTheme> CREATOR = new Parcelable.Creator<ProductDetailsTheme>() {
        public ProductDetailsTheme createFromParcel(Parcel in) {
            return new ProductDetailsTheme(in);
        }

        public ProductDetailsTheme[] newArray(int size) {
            return new ProductDetailsTheme[size];
        }
    };

    private ProductDetailsTheme(Parcel in) {
        style = Style.values()[in.readInt()];
        accentColor = in.readInt();
        showProductImageBackground = in.readInt() != 0;
    }

    int getBackgroundColor(Resources res) {
        switch (style) {
            case DARK:
                return res.getColor(R.color.dark_background);
            default:
                return res.getColor(R.color.light_background);
        }
    }

    int getAppBarBackgroundColor(Resources res) {
        switch (style) {
            case DARK:
                return res.getColor(R.color.dark_low_contrast_background);
            default:
                return res.getColor(R.color.light_low_contrast_background);
        }
    }

    int getProductTitleColor(Resources res) {
        switch (style) {
            case DARK:
                return res.getColor(R.color.dark_product_title);
            default:
                return res.getColor(R.color.light_product_title);
        }
    }

    int getVariantOptionNameColor(Resources res) {
        switch (style) {
            case DARK:
                return res.getColor(R.color.dark_low_contrast_grey);
            default:
                return res.getColor(R.color.light_low_contrast_grey);
        }
    }

    int getCompareAtPriceColor(Resources res) {
        switch (style) {
            case DARK:
                return res.getColor(R.color.body_grey);
            default:
                return res.getColor(R.color.body_grey);
        }
    }

    int getProductDescriptionColor(Resources res) {
        switch (style) {
            case DARK:
                return res.getColor(R.color.body_grey);
            default:
                return res.getColor(R.color.body_grey);
        }
    }

    int getDividerColor(Resources res) {
        switch (style) {
            case DARK:
                return res.getColor(R.color.dark_low_contrast_grey);
            default:
                return res.getColor(R.color.light_low_contrast_grey);
        }
    }

    int getDialogTitleColor(Resources res) {
        switch (style) {
            case DARK:
                return res.getColor(R.color.dark_dialog_title);
            default:
                return res.getColor(R.color.light_dialog_title);
        }
    }

    int getVariantBreadcrumbBackgroundColor(Resources res) {
        switch (style) {
            case DARK:
                return res.getColor(R.color.dark_low_contrast_grey);
            default:
                return res.getColor(R.color.light_low_contrast_grey);
        }
    }

    int getDialogListItemColor(Resources res) {
        switch (style) {
            case DARK:
                return res.getColor(R.color.body_grey);
            default:
                return res.getColor(R.color.body_grey);
        }
    }

    int getCheckoutLabelColor(Resources res) {
        switch (style) {
            case DARK:
                return res.getColor(R.color.dark_dialog_title);
            default:
                return res.getColor(R.color.light_dialog_title);
        }
    }

    Drawable getCheckmarkDrawable(Context context) {
        Drawable checkmark;
        switch (style) {
            case DARK:
                checkmark = context.getResources().getDrawable(R.drawable.ic_check_white_24dp);
                break;
            default:
                checkmark = context.getResources().getDrawable(R.drawable.ic_check_black_24dp);
                break;
        }
        checkmark.setColorFilter(new PorterDuffColorFilter(accentColor, PorterDuff.Mode.SRC_IN));
        return checkmark;
    }

    Drawable getBackgroundSelectorDrawable(Resources res) {
        switch (style) {
            case DARK:
                return res.getDrawable(R.drawable.dark_background_selector);
            default:
                return res.getDrawable(R.drawable.light_background_selector);
        }
    }

}
