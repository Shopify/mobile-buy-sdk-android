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

package com.shopify.buy.utils;

import com.shopify.buy.model.Product;

/**
 * Downloads {@link Product} images.
 */
public class ImageUtility {

    /**
     * See <a href="http://docs.shopify.com/themes/filters/product-img-url">Product_img_url</a> for details.
     *
     * @param width  The view width, in pixels.
     * @param height The view height, in pixels.
     */
    public static String getImageSuffixForDimensions(int width, int height) {
        final int pixels = Math.max(width, height);
        if (pixels <= 16) {
            return "_pico";
        } else if (pixels <= 32) {
            return "_icon";
        } else if (pixels <= 50) {
            return "_thumb";
        } else if (pixels <= 100) {
            return "_small";
        } else if (pixels <= 160) {
            return "_compact";
        } else if (pixels <= 240) {
            return "_medium";
        } else if (pixels <= 480) {
            return "_large";
        } else if (pixels <= 600) {
            return "_grande";
        } else if (pixels <= 1024) {
            return "_1024x1024";
        } else {
            return "_2048x2048";
        }
    }

    public static String stripQueryFromUrl(String url) {
        if (url == null || !url.contains("?")) {
            return url;
        }
        return url.substring(0, url.lastIndexOf('?'));
    }

}
