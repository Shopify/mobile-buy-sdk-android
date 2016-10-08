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

import com.shopify.mobilebuysdk.demo.widget.MaskedEditText;

import android.text.TextUtils;
import android.widget.EditText;

/**
 * Created by henrytao on 10/5/16.
 */
public class EditTextUtils {

  public static String getText(EditText editText, boolean required) throws IllegalArgumentException {
    if (editText == null) {
      throw new IllegalArgumentException("Input is null");
    }
    String value = editText instanceof MaskedEditText ? ((MaskedEditText) editText).getUnmaskText() : editText.getText().toString();
    if (TextUtils.isEmpty(value) && required) {
      throw new IllegalArgumentException("Input is required");
    }
    return value;
  }
}
