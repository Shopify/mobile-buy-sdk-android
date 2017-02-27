package com.shopify.buy3;

import android.support.annotation.Nullable;
import android.util.Base64;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.nio.charset.Charset;

final class Utils {
  private static final String DATE_TIME_PATTERN = "yyyy-MM-dd'T'HH:mm:ssZ";
  private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormat.forPattern(DATE_TIME_PATTERN);

  static DateTime parseDateTime(String dateTime) {
    return DateTime.parse(dateTime, DATE_TIME_FORMATTER);
  }

  static String formatBasicAuthorization(final String token) {
    return String.format("Basic %s", Base64.encodeToString(token.getBytes(Charset.forName("UTF-8")), Base64.NO_WRAP));
  }

  static <T> T checkNotNull(T reference, @Nullable Object errorMessage) {
    if (reference == null) {
      throw new NullPointerException(String.valueOf(errorMessage));
    }
    return reference;
  }

  private Utils() {
  }
}
