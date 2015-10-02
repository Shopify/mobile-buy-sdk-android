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

import android.graphics.Color;

/**
 * Blends colors.
 */
public class ColorBlender {

    public static int getBlendedColor(int color1, int color2, float weightOfColor2) {
        float red1 = (float) Color.red(color1);
        float red2 = (float) Color.red(color2);
        float green1 = (float) Color.green(color1);
        float green2 = (float) Color.green(color2);
        float blue1 = (float) Color.blue(color1);
        float blue2 = (float) Color.blue(color2);

        int redBlended = (int) ((red1 * (1f - weightOfColor2)) + (red2 * weightOfColor2));
        int greenBlended = (int) ((green1 * (1f - weightOfColor2)) + (green2 * weightOfColor2));
        int blueBlended = (int) ((blue1 * (1f - weightOfColor2)) + (blue2 * weightOfColor2));

        return Color.argb(255, redBlended, greenBlended, blueBlended);
    }

}
