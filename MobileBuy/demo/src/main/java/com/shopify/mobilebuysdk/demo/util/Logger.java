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

import android.util.Log;

/**
 * Created by henrytao on 8/27/16.
 */
public final class Logger {

  private final static String DEFAULT_TAG = "Logger";

  public static Logger newInstance(LogLevel level) {
    return new Logger(DEFAULT_TAG, level);
  }

  public static Logger newInstance(String tag, LogLevel logLevel) {
    return new Logger(tag, logLevel);
  }

  public final LogLevel mLogLevel;

  private final String mTag;

  protected Logger(String tag, LogLevel logLevel) {
    mTag = tag;
    mLogLevel = logLevel;
  }

  public void d(String format, Object... args) {
    if (shouldLog(LogLevel.DEBUG)) {
      Log.d(mTag, String.format(format, args));
    }
  }

  public void e(Throwable error) {
    e(error, "");
  }

  public void e(String format, Object... args) {
    e(null, format, args);
  }

  public void e(Throwable error, String format, Object... args) {
    if (shouldLog(LogLevel.ERROR)) {
      Log.e(mTag, String.format(format, args), error);
    }
  }

  public void i(String format, Object... args) {
    if (shouldLog(LogLevel.INFO)) {
      Log.i(mTag, String.format(format, args));
    }
  }

  public void v(String format, Object... args) {
    if (shouldLog(LogLevel.VERBOSE)) {
      Log.v(mTag, String.format(format, args));
    }
  }

  public void w(String format, Object... args) {
    if (shouldLog(LogLevel.WARN)) {
      Log.w(mTag, String.format(format, args));
    }
  }

  private boolean shouldLog(LogLevel level) {
    return mLogLevel.toInt() >= level.toInt();
  }

  public enum LogLevel {
    NONE(0),
    ERROR(1),
    WARN(2),
    INFO(3),
    DEBUG(4),
    VERBOSE(5);

    public static LogLevel valueOf(int value) {
      if (value == ERROR.toInt()) {
        return ERROR;
      } else if (value == WARN.toInt()) {
        return WARN;
      } else if (value == INFO.toInt()) {
        return INFO;
      } else if (value == DEBUG.toInt()) {
        return DEBUG;
      } else if (value == VERBOSE.toInt()) {
        return VERBOSE;
      } else {
        return NONE;
      }
    }

    private final int mValue;

    LogLevel(int value) {
      mValue = value;
    }

    public int toInt() {
      return mValue;
    }
  }
}