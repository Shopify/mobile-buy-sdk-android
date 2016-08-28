/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2016 Shopify Inc.
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
 *
 */

package com.shopify.mobilebuysdk.demo.ui.product;

import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.view.DraweeTransition;
import com.facebook.drawee.view.SimpleDraweeView;
import com.shopify.mobilebuysdk.demo.R;
import com.shopify.mobilebuysdk.demo.ui.base.BaseActivity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.transition.Explode;
import android.view.Window;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by henrytao on 8/28/16.
 */
public class ProductActivity extends BaseActivity {

  public static Intent newIntent(Activity activity) {
    Intent intent = new Intent(activity, ProductActivity.class);
    return intent;
  }

  @BindView(R.id.thumbnail) SimpleDraweeView vThumbnail;

  @BindView(R.id.toolbar) Toolbar vToolbar;

  @Override
  public void onSetContentView(Bundle savedInstanceState) {
    setContentView(R.layout.activity_product);
    ButterKnife.bind(this);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setSupportActionBar(vToolbar);
    vToolbar.setNavigationOnClickListener(view -> onBackPressed());

    vThumbnail.setImageURI(
        "https://firebasestorage.googleapis.com/v0/b/android-shopify.appspot.com/o/public%2FRoyale-Flight-Jacket-Nero-Product-01_8555ba9a-6945-4c9a-85d5-e794e54263f6.jpg?alt=media");
  }

  @TargetApi(Build.VERSION_CODES.LOLLIPOP)
  @Override
  protected void onSharedElementConfig(Window window) {
    super.onSharedElementConfig(window);
    window.requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
    window.setSharedElementEnterTransition(
        DraweeTransition.createTransitionSet(ScalingUtils.ScaleType.FIT_CENTER, ScalingUtils.ScaleType.FIT_CENTER));
    window.setSharedElementReturnTransition(
        DraweeTransition.createTransitionSet(ScalingUtils.ScaleType.FIT_CENTER, ScalingUtils.ScaleType.FIT_CENTER));
    window.setEnterTransition(new Explode());
  }
}
