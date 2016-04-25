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
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * Handles fling gestures for expanding and shrinking the product image.
 */
class ProductDetailsImageAreaTouchHandler implements View.OnTouchListener, ScrollFlingGestureListener.ScrollFlingGestureCallback {

    private final GestureDetector detector;
    private final ImageAreaCallback callback;

    private boolean isFlingingUp = false;
    private boolean isFlingingDown = false;

    public interface ImageAreaCallback {
        void toggleImageAreaSize();
        void setImageAreaSize(boolean grow);
    }

    public ProductDetailsImageAreaTouchHandler(Context context, ImageAreaCallback callback) {
        detector = new GestureDetector(context, new ScrollFlingPlusSingleTap(this));
        this.callback = callback;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        detector.onTouchEvent(event);
        if (isFlingingUp) {
            isFlingingUp = false;
            callback.setImageAreaSize(false);
            return true;
        } else if (isFlingingDown) {
            isFlingingDown = false;
            callback.setImageAreaSize(true);
            return true;
        }
        return false;
    }

    @Override
    public void isFlingingUp() {
        isFlingingUp = true;
    }

    @Override
    public void isFlingingDown() {
        isFlingingDown = true;
    }

    class ScrollFlingPlusSingleTap extends ScrollFlingGestureListener {

        public ScrollFlingPlusSingleTap(ScrollFlingGestureCallback callback) {
            super(callback);
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            callback.toggleImageAreaSize();
            return true;
        }
    }

}
