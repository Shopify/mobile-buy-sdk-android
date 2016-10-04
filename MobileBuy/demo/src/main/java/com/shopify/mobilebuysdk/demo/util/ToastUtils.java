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

package com.shopify.mobilebuysdk.demo.util;

import com.shopify.mobilebuysdk.demo.R;

import android.content.Context;
import android.support.annotation.StringRes;
import android.widget.Toast;

/**
 * Created by henrytao on 9/28/16.
 */
public class ToastUtils {

  public static Toast showAndroidPaySetupMessage(Context context) {
    return showShortToast(context, R.string.text_android_pay_setup_message);
  }

  public static Toast showCheckRequiredFieldsToast(Context context) {
    return showShortToast(context, R.string.text_check_required_fields);
  }

  public static Toast showGenericErrorToast(Context context) {
    return showShortToast(context, R.string.text_generic_error);
  }

  private static Toast showLongToast(Context context, @StringRes int id) {
    Toast toast = Toast.makeText(context, id, Toast.LENGTH_LONG);
    toast.show();
    return toast;
  }

  private static Toast showLongToast(Context context, CharSequence text) {
    Toast toast = Toast.makeText(context, text, Toast.LENGTH_LONG);
    toast.show();
    return toast;
  }

  private static Toast showShortToast(Context context, CharSequence text) {
    Toast toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
    toast.show();
    return toast;
  }

  private static Toast showShortToast(Context context, @StringRes int id) {
    Toast toast = Toast.makeText(context, id, Toast.LENGTH_SHORT);
    toast.show();
    return toast;
  }
}