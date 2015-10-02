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

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Window;
import android.view.WindowManager;

import com.shopify.buy.R;
import com.shopify.buy.model.Product;
import com.shopify.buy.utils.DeviceUtils;

/**
 * Activity that shows the details of a {@link Product}.
 */
public class ProductDetailsActivity extends AppCompatActivity implements ProductDetailsListener {

    protected ProductDetailsFragment productDetailsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        if (DeviceUtils.isTablet(getResources())) {
            makeActivityDialog();

        }
        
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null) {
            productDetailsFragment = getFragment();

            Intent intent = getIntent();
            if (intent != null) {
                productDetailsFragment.setArguments(intent.getExtras());
            }

            getFragmentManager().beginTransaction()
                    .add(R.id.product_details_activity, productDetailsFragment)
                    .commit();
        } else {
            productDetailsFragment = (ProductDetailsFragment) getFragmentManager().findFragmentById(R.id.product_details_activity);
        }

        initContentView();
    }

    private ProductDetailsFragment getFragment() {
        if (productDetailsFragment == null) {
            productDetailsFragment = new ProductDetailsFragment();
        }
        return productDetailsFragment;
    }

    private void initContentView() {
        setContentView(R.layout.activity_product_details);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        if (savedInstanceState == null) {
            onNewIntent(getIntent());
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }

    // ProductDetailsListener Callbacks

    public void onSuccess(Bundle bundle) {
        setResult(Activity.RESULT_OK, bundle);
    }

    public void onFailure(Bundle bundle) {
        setResult(Activity.RESULT_CANCELED, bundle);
        finish();
    }

    public void onCancel(Bundle bundle) {
        setResult(Activity.RESULT_CANCELED, bundle);
        finish();
    }

    private void setResult(int resultCode, Bundle bundle) {
        Intent intent = getIntent();
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        setResult(resultCode, intent);
    }

    private void makeActivityDialog() {
        // Turn on rotation for large devices
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);

        // Get the full screen size
        WindowManager windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        int screenWidth = metrics.widthPixels;
        int screenHeight = metrics.heightPixels;
        WindowManager.LayoutParams params = getWindow().getAttributes();

        // Adjust the screen size to make it a dialog
        if (screenHeight > screenWidth) {// portrait mode
            params.width = Math.round(screenWidth * 0.80f);
            params.height = Math.min(Math.round(params.width * 1.25f), screenHeight);
        } else {
            // landscape
            params.height = Math.round(screenHeight * 0.85f);
            params.width = Math.min(Math.round(params.height * 0.80f), screenWidth);
        }

        // Update the window with our new settings
        getWindow().setAttributes(params);
    }
}
