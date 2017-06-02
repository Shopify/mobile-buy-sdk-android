package com.shopify.buy3.cache;

import android.support.annotation.NonNull;

import okio.Source;

interface ResponseCacheRecord {
  @NonNull Source headerSource();

  @NonNull Source bodySource();

  void close();
}
