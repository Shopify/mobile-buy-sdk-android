package com.shopify.buy3;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

final class Utils {
  public static final String DATE_TIME_PATTERN = "yyyy-MM-dd'T'HH:mm:ssZ";
  public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormat.forPattern(DATE_TIME_PATTERN);

  public static DateTime parseDateTime(String dateTime) {
    return DateTime.parse(dateTime, DATE_TIME_FORMATTER);
  }

  private Utils() {
  }
}
