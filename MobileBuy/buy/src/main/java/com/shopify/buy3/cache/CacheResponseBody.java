package com.shopify.buy3.cache;

import android.support.annotation.NonNull;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import okio.BufferedSource;
import okio.Okio;
import okio.Source;
import timber.log.Timber;

final class CacheResponseBody extends ResponseBody {
  private BufferedSource responseBodySource;
  private final String contentType;
  private final String contentLength;

  CacheResponseBody(@NonNull final Source responseBodySource, @NonNull final String contentType, @NonNull final String contentLength) {
    this.responseBodySource = Okio.buffer(responseBodySource);
    this.contentType = contentType;
    this.contentLength = contentLength;
  }

  @Override public MediaType contentType() {
    return contentType != null ? MediaType.parse(contentType) : null;
  }

  @Override public long contentLength() {
    try {
      return contentLength != null ? Long.parseLong(contentLength) : -1;
    } catch (NumberFormatException e) {
      Timber.w(e, "failed to parse content length");
      return -1;
    }
  }

  @Override public BufferedSource source() {
    return responseBodySource;
  }
}
