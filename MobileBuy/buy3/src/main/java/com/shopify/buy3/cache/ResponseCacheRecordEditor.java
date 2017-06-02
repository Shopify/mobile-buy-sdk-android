package com.shopify.buy3.cache;

import android.support.annotation.NonNull;

import java.io.IOException;

import okio.Sink;

interface ResponseCacheRecordEditor {
  @NonNull Sink headerSink();

  @NonNull Sink bodySink();

  void abort() throws IOException;

  void commit() throws IOException;
}
