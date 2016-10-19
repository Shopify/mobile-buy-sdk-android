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

package com.shopify.mobilebuysdk.demo.widget;

import com.shopify.mobilebuysdk.demo.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;

/**
 * Created by henrytao on 10/4/16.
 */
public class MaskedEditText extends AppCompatEditText {

  public static String mask(String text, String mask) {
    if (TextUtils.isEmpty(mask)) {
      return text;
    }
    // match mask
    text = !TextUtils.isEmpty(text) ? text : "";
    StringBuilder output = new StringBuilder(mask);
    int length = text.length();
    int maskLength = mask.length();
    int maskIndex = 0;
    for (int i = 0; i < length; i++) {
      if (i < maskLength) {
        char maskChar = mask.charAt(maskIndex);
        char textChar = text.charAt(i);
        if (!(maskChar == '#' || maskChar == textChar)) {
          maskIndex++;
        }
        output.replace(maskIndex, maskIndex + 1, String.valueOf(textChar));
        maskIndex++;
      } else {
        output.append(text.charAt(i));
        maskIndex++;
      }
    }
    // fix mask length
    if (maskIndex < output.length()) {
      output.delete(maskIndex, output.length());
    }
    // trim mask
    int i = output.length() - 1;
    while (i >= 0 && i < maskLength) {
      if (mask.charAt(i) != '#') {
        output.delete(i, i + 1);
      } else {
        break;
      }
      i--;
    }
    return output.toString();
  }

  public static String unmask(String maskedText, String mask) {
    if (TextUtils.isEmpty(mask)) {
      return maskedText;
    }
    StringBuilder output = new StringBuilder();
    int maskLength = mask.length();
    int n = maskedText.length();
    for (int i = 0; i < n; i++) {
      if (i >= maskLength || mask.charAt(i) == '#') {
        output.append(maskedText.charAt(i));
      }
    }
    return output.toString();
  }

  private String mMask;

  private TextWatcher mTextChangedListener;

  public MaskedEditText(Context context) {
    super(context);
    init(context, null);
  }

  public MaskedEditText(Context context, AttributeSet attrs) {
    super(context, attrs);
    init(context, attrs);
  }

  public MaskedEditText(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init(context, attrs);
  }

  @Override
  protected void onAttachedToWindow() {
    super.onAttachedToWindow();
    addTextChangedListener(mTextChangedListener);
  }

  @Override
  protected void onDetachedFromWindow() {
    super.onDetachedFromWindow();
    removeTextChangedListener(mTextChangedListener);
  }

  public String getUnmaskText() {
    return unmask(getText().toString(), mMask);
  }

  public void setMask(String mask) {
    mMask = mask;
  }

  private void applyMask() {
    String currentText = getText().toString();
    String maskedText = mask(currentText, mMask);
    if (!TextUtils.equals(currentText, maskedText)) {
      setText(maskedText);
      setSelection(getText().length());
    }
  }

  private void init(Context context, AttributeSet attrs) {
    TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.MaskedEditText, 0, 0);
    String mask = null;
    try {
      mask = a.getString(R.styleable.MaskedEditText_mask);
    } finally {
      a.recycle();
    }
    mTextChangedListener = new SimpleTextWatcher() {
      @Override
      public void afterTextChanged(Editable editable) {
        applyMask();
      }
    };
    setMask(mask);
  }

  private static abstract class SimpleTextWatcher implements TextWatcher {

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
    }
  }
}
