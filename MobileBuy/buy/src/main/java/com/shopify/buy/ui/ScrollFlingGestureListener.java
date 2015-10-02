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

package com.shopify.buy.ui;

import android.view.GestureDetector;
import android.view.MotionEvent;

/**
 * Detects fling gestures for expanding and shrinking the product image.
 */
public class ScrollFlingGestureListener extends GestureDetector.SimpleOnGestureListener {

    private final ScrollFlingGestureCallback callback;

    public interface ScrollFlingGestureCallback {
        void isFlingingUp();
        void isFlingingDown();
    }

    public ScrollFlingGestureListener(ScrollFlingGestureCallback callback) {
        this.callback = callback;
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return true;
    }

    /**
     * Translates vertical scrolls on the listener into flings, and lets horizontal scrolls pass through.
     *
     * @return true, always handles the event
     */
    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        // do a quick calculation to see if the scroll event we received is more vertical than horizontal
        if (Math.abs(distanceX) <= Math.abs(distanceY)) {
            if (distanceY > 0) {
                callback.isFlingingUp();
            } else if (distanceY < 0) {
                callback.isFlingingDown();
            }
        }
        return true;
    }
}
